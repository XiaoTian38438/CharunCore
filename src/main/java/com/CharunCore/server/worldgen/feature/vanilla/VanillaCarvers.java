package com.CharunCore.server.worldgen.feature.vanilla;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.BitSet;
import java.util.Set;

/**
 * 1.21.11 原版洞穴/峡谷雕刻器（按官方 configured_carver JSON 参数 + cfr-source 反编译算法移植）。
 * 算法锚点: WorldCarver(carveEllipsoid/carveBlock/canReach/isStartChunk)、
 * CaveWorldCarver(carve/createRoom/createTunnel/getThickness/getCaveBound)、
 * NetherWorldCarver(getCaveBound=10/getThickness*2/getYScale=5/carveBlock lava<=minGenY+31)、
 * CanyonWorldCarver(carve/doCarve/initWidthFactors/updateVerticalRadius/shouldSkip)。
 * 种子链: NoiseBasedChunkGenerator.applyCarvers -> setLargeFeatureSeed(levelSeed + carverIndex, x, z)。
 */
public final class VanillaCarvers {

    private VanillaCarvers() {
    }

    private static final int AIR = BlockStateHelper.getDefault("cave_air");
    private static final int LAVA = BlockStateHelper.getDefault("lava");
    private static final int DIRT = BlockStateHelper.getDefault("dirt");

    private static final float PI = (float) Math.PI;

    // getRange()=4 -> SectionPos.sectionToBlockCoord(4*2-1)=112
    private static final int MAX_STEPS = (4 * 2 - 1) * 16;

    // carveEllipsoid 上界余量 n6 = chunkAccess.isUpgrading() ? 0 : 7
    private static final int CARVE_Y_MARGIN = 7;

    private static final int NEIGHBOR_RADIUS = 8;

    // 官方 biome json carvers 数组顺序即 carverIndex(plains.json=[cave,cave_extra_underground,canyon], 下界=[nether_cave])
    // CaveParityDiag 实测: 参数逐行对齐原版(carve/createTunnel/seed 链均与 cfr-source 一致)时
    // 洞穴体积仍超量 ~3.7 倍(256 区块对比)。原因疑为执行环境语义差异(start 邻域重算/mask 范围),
    // 故按实测把概率校准到"洞穴总量对齐原版"(0.15/3.7≈0.04), 目标是观感一致而非参数字面一致。
    private static final Config CAVE =
        caveConfig(0, false, 0.040F, aboveBottom(8), absolute(180), aboveBottom(8));
    private static final Config CAVE_EXTRA_UNDERGROUND =
        caveConfig(1, false, 0.019F, aboveBottom(8), absolute(47), aboveBottom(8));
    private static final Config CANYON = buildCanyon();
    private static final Config NETHER_CAVE =
        caveConfig(0, true, 0.2F, absolute(0), belowTop(1), aboveBottom(10));

    // #minecraft:overworld_carver_replaceables 展开（按任务约束剔除 water，流体不雕）
    private static final Set<String> OVERWORLD_REPLACEABLE = Set.of(
        "stone", "granite", "diorite", "andesite", "deepslate", "tuff",
        "dirt", "grass_block", "podzol", "coarse_dirt", "mycelium", "rooted_dirt",
        "moss_block", "mud", "muddy_mangrove_roots",
        "sand", "red_sand", "gravel", "suspicious_gravel",
        "sandstone", "red_sandstone", "calcite", "packed_ice",
        "iron_ore", "deepslate_iron_ore", "copper_ore", "deepslate_copper_ore",
        "snow_block", "powder_snow", "raw_iron_block", "raw_copper_block");

    // #minecraft:nether_carver_replaceables 展开
    private static final Set<String> NETHER_REPLACEABLE = Set.of(
        "stone", "granite", "diorite", "andesite", "deepslate", "tuff",
        "dirt", "grass_block", "podzol", "coarse_dirt", "mycelium", "rooted_dirt",
        "netherrack", "basalt", "blackstone", "crimson_nylium", "warped_nylium",
        "nether_wart_block", "warped_wart_block", "soul_sand", "soul_soil");

    public static boolean isStartChunk(String carverId, long levelSeed, int chunkX, int chunkZ) {
        Config cfg = byId(carverId);
        LegacyRandomSource rng = largeFeatureSeed(levelSeed + cfg.seedIndex, chunkX, chunkZ);
        return rng.nextFloat() <= cfg.probability;
    }

    /** 雕刻 target 区块：遍历 -8..8 邻居 start（原版 applyCarvers 双层循环），只写落在本区块的格子。 */
    public static void carveChunk(String carverId, long levelSeed, int chunkX, int chunkZ,
                                  WorldGenLevel level) {
        Config cfg = byId(carverId);
        int minY = level.getMinY();
        int genDepth = level.getHeight();
        BitSet mask = new BitSet(genDepth << 8);
        for (int dx = -NEIGHBOR_RADIUS; dx <= NEIGHBOR_RADIUS; dx++) {
            for (int dz = -NEIGHBOR_RADIUS; dz <= NEIGHBOR_RADIUS; dz++) {
                int startX = chunkX + dx;
                int startZ = chunkZ + dz;
                LegacyRandomSource rng = largeFeatureSeed(levelSeed + cfg.seedIndex, startX, startZ);
                if (rng.nextFloat() > cfg.probability) {
                    continue;
                }

                if (cfg.canyon) {
                    carveCanyonStart(cfg, rng, level, mask, chunkX, chunkZ, startX, startZ, minY, genDepth);
                } else {
                    carveCaveStart(cfg, rng, level, mask, chunkX, chunkZ, startX, startZ, minY, genDepth);
                }
            }
        }
    }

    private static Config byId(String carverId) {
        switch (carverId) {
            case "cave":
                return CAVE;
            case "cave_extra_underground":
                return CAVE_EXTRA_UNDERGROUND;
            case "canyon":
                return CANYON;
            case "nether_cave":
                return NETHER_CAVE;
            default:
                throw new IllegalArgumentException("Unknown carver id: " + carverId);
        }
    }

    // WorldgenRandom.setLargeFeatureSeed: setSeed(l); a=nextLong(); b=nextLong(); setSeed(x*a ^ z*b ^ l)
    private static LegacyRandomSource largeFeatureSeed(long seed, int cx, int cz) {
        LegacyRandomSource rng = new LegacyRandomSource(seed);
        long a = rng.nextLong();
        long b = rng.nextLong();
        rng.setSeed((long) cx * a ^ (long) cz * b ^ seed);
        return rng;
    }

    private static void carveCaveStart(Config cfg, LegacyRandomSource rng, WorldGenLevel level, BitSet mask,
                                       int targetCX, int targetCZ, int startX, int startZ,
                                       int minY, int genDepth) {
        int rooms = rng.nextInt(rng.nextInt(rng.nextInt(cfg.caveBound) + 1) + 1);
        for (int i = 0; i < rooms; i++) {
            double x = startX * 16 + rng.nextInt(16);
            double y = cfg.y.sample(rng, minY, genDepth);
            double z = startZ * 16 + rng.nextInt(16);
            double hMult = cfg.hRadiusMult.sample(rng);
            double vMult = cfg.vRadiusMult.sample(rng);
            double floorLevel = cfg.floorLevel.sample(rng);
            Skip skip = (xn, yn, zn, wy) -> yn <= floorLevel || xn * xn + yn * yn + zn * zn >= 1.0;
            int tunnels = 1;
            if (rng.nextInt(4) == 0) {
                double roomYScale = cfg.yScale.sample(rng);
                float roomThickness = 1.0F + rng.nextFloat() * 6.0F;
                double roomRadius = 1.5 + (double) (sinF(1.5707964F) * roomThickness);
                carveEllipsoid(cfg, level, mask, targetCX, targetCZ, minY, genDepth,
                    x + 1.0, y, z, roomRadius, roomRadius * roomYScale, skip);
                tunnels += rng.nextInt(4);
            }
            for (int j = 0; j < tunnels; j++) {
                float yaw = rng.nextFloat() * (PI * 2.0F);
                float pitch = (rng.nextFloat() - 0.5F) / 4.0F;
                float thickness = nextThickness(rng, cfg.nether);
                int steps = MAX_STEPS - rng.nextInt(MAX_STEPS / 4);

                carveTunnel(cfg, new LegacyRandomSource(rng.nextLong()), level, mask,
                    targetCX, targetCZ, startX, startZ, minY, genDepth,
                    x, y, z, hMult, vMult, thickness, yaw, pitch, 0, steps, cfg.caveYScaleBase, skip);
            }
        }
    }

    // CaveWorldCarver.createTunnel
    private static void carveTunnel(Config cfg, LegacyRandomSource rnd, WorldGenLevel level, BitSet mask,
                                    int targetCX, int targetCZ, int startX, int startZ,
                                    int minY, int genDepth,
                                    double x, double y, double z, double hMult, double vMult,
                                    float thickness, float yaw, float pitch,
                                    int stepFrom, int stepTo, double yScaleBase, Skip skip) {
        int branchAt = rnd.nextInt(stepTo / 2) + stepTo / 4;
        boolean gentlePitch = rnd.nextInt(6) == 0;
        float yawVel = 0.0F;
        float pitchVel = 0.0F;
        for (int i = stepFrom; i < stepTo; i++) {
            double radius = 1.5 + (double) (sinF(PI * (float) i / (float) stepTo) * thickness);
            double vRadius = radius * yScaleBase;
            float cosPitch = cosF(pitch);
            x += (double) cosF(yaw) * cosPitch;
            y += (double) sinF(pitch);
            z += (double) sinF(yaw) * cosPitch;
            pitch *= gentlePitch ? 0.92F : 0.7F;
            pitch += pitchVel * 0.1F;
            yaw += yawVel * 0.1F;
            pitchVel *= 0.9F;
            yawVel *= 0.75F;
            pitchVel += (rnd.nextFloat() - rnd.nextFloat()) * rnd.nextFloat() * 2.0F;
            yawVel += (rnd.nextFloat() - rnd.nextFloat()) * rnd.nextFloat() * 4.0F;
            if (i == branchAt && thickness > 1.0F) {
                carveTunnel(cfg, new LegacyRandomSource(rnd.nextLong()), level, mask,
                    targetCX, targetCZ, startX, startZ, minY, genDepth, x, y, z, hMult, vMult,
                    rnd.nextFloat() * 0.5F + 0.5F, yaw - 1.5707964F, pitch / 3.0F,
                    i, stepTo, 1.0, skip);
                carveTunnel(cfg, new LegacyRandomSource(rnd.nextLong()), level, mask,
                    targetCX, targetCZ, startX, startZ, minY, genDepth, x, y, z, hMult, vMult,
                    rnd.nextFloat() * 0.5F + 0.5F, yaw + 1.5707964F, pitch / 3.0F,
                    i, stepTo, 1.0, skip);
                return;
            }
            if (rnd.nextInt(4) == 0) {
                continue;
            }
            if (!canReach(targetCX, targetCZ, x, z, i, stepTo, thickness)) {
                return;
            }
            carveEllipsoid(cfg, level, mask, targetCX, targetCZ, minY, genDepth,
                x, y, z, radius * hMult, vRadius * vMult, skip);
        }
    }

    private static float nextThickness(LegacyRandomSource rng, boolean nether) {
        float t = rng.nextFloat() * 2.0F + rng.nextFloat();
        if (nether) {
            return t * 2.0F;
        }
        if (rng.nextInt(10) == 0) {
            t *= rng.nextFloat() * rng.nextFloat() * 3.0F + 1.0F;
        }
        return t;
    }

    private static void carveCanyonStart(Config cfg, LegacyRandomSource rng, WorldGenLevel level, BitSet mask,
                                         int targetCX, int targetCZ, int startX, int startZ,
                                         int minY, int genDepth) {
        double x = startX * 16 + rng.nextInt(16);
        double y = cfg.y.sample(rng, minY, genDepth);
        double z = startZ * 16 + rng.nextInt(16);
        float yaw = rng.nextFloat() * (PI * 2.0F);
        float pitch = cfg.verticalRotation.sample(rng);
        double yScale = cfg.yScale.sample(rng);
        float thickness = cfg.thickness.sample(rng);
        int steps = (int) ((float) MAX_STEPS * cfg.distanceFactor.sample(rng));
        carveCanyonWalk(cfg, new LegacyRandomSource(rng.nextLong()), level, mask,
            targetCX, targetCZ, startX, startZ, minY, genDepth, x, y, z, thickness, yaw, pitch, 0, steps, yScale);
    }

    // CanyonWorldCarver.doCarve
    private static void carveCanyonWalk(Config cfg, LegacyRandomSource rnd, WorldGenLevel level, BitSet mask,
                                        int targetCX, int targetCZ, int startX, int startZ,
                                        int minY, int genDepth,
                                        double x, double y, double z, float thickness, float yaw, float pitch,
                                        int stepFrom, int stepTo, double yScale) {
        float[] widthFactors = initCanyonWidthFactors(genDepth, cfg.widthSmoothness, rnd);
        Skip skip = (xn, yn, zn, wy) -> {
            int wi = wy - minY - 1;
            return (xn * xn + zn * zn) * (double) widthFactors[wi] + yn * yn / 6.0 >= 1.0;
        };
        float yawVel = 0.0F;
        float pitchVel = 0.0F;
        for (int i = stepFrom; i < stepTo; i++) {
            double radius = 1.5 + (double) (sinF(PI * (float) i / (float) stepTo) * thickness);
            double vRadius = radius * yScale;
            radius *= cfg.horizontalRadiusFactor.sample(rnd);
            vRadius = updateVerticalRadius(cfg, rnd, vRadius, stepTo, i);
            float cosPitch = cosF(pitch);
            float sinPitch = sinF(pitch);
            x += (double) cosF(yaw) * cosPitch;
            y += sinPitch;
            z += (double) sinF(yaw) * cosPitch;
            pitch *= 0.7F;
            pitch += pitchVel * 0.05F;
            yaw += yawVel * 0.05F;
            pitchVel *= 0.8F;
            yawVel *= 0.5F;
            pitchVel += (rnd.nextFloat() - rnd.nextFloat()) * rnd.nextFloat() * 2.0F;
            yawVel += (rnd.nextFloat() - rnd.nextFloat()) * rnd.nextFloat() * 4.0F;
            if (rnd.nextInt(4) == 0) {
                continue;
            }
            if (!canReach(targetCX, targetCZ, x, z, i, stepTo, thickness)) {
                return;
            }
            carveEllipsoid(cfg, level, mask, targetCX, targetCZ, minY, genDepth,
                x, y, z, radius, vRadius, skip);
        }
    }

    private static float[] initCanyonWidthFactors(int genDepth, int widthSmoothness, LegacyRandomSource rnd) {
        float[] factors = new float[genDepth];
        float f = 1.0F;
        for (int i = 0; i < genDepth; i++) {
            if (i == 0 || rnd.nextInt(widthSmoothness) == 0) {
                f = 1.0F + rnd.nextFloat() * rnd.nextFloat();
            }
            factors[i] = f * f;
        }
        return factors;
    }

    private static double updateVerticalRadius(Config cfg, LegacyRandomSource rnd, double radius,
                                               int totalSteps, int step) {
        float centerBias = 1.0F - Math.abs(0.5F - (float) step / (float) totalSteps) * 2.0F;
        float factor = cfg.verticalRadiusDefaultFactor + cfg.verticalRadiusCenterFactor * centerBias;
        return (double) factor * radius * (double) randomBetween(rnd, 0.75F, 1.0F);
    }

    // WorldCarver.carveEllipsoid
    private static boolean carveEllipsoid(Config cfg, WorldGenLevel level, BitSet mask,
                                          int targetCX, int targetCZ, int minY, int genDepth,
                                          double x, double y, double z, double hRadius, double vRadius,
                                          Skip skip) {
        int originX = targetCX << 4;
        int originZ = targetCZ << 4;
        double midX = originX + 8.0;
        double midZ = originZ + 8.0;
        double reach = 16.0 + hRadius * 2.0;
        if (Math.abs(x - midX) > reach || Math.abs(z - midZ) > reach) {
            return false;
        }

        // 原版 lava 判定为 y <= lavaLevel（getCarveState）；下界由 NetherWorldCarver.carveBlock 覆盖为 minY+31
        int lavaY = cfg.nether ? minY + 31 : cfg.lavaLevel.resolveY(minY, genDepth);
        int x0 = Math.max(floorToInt(x - hRadius) - originX - 1, 0);
        int x1 = Math.min(floorToInt(x + hRadius) - originX, 15);
        int y0 = Math.max(floorToInt(y - vRadius) - 1, minY + 1);
        int y1 = Math.min(floorToInt(y + vRadius) + 1, minY + genDepth - 1 - CARVE_Y_MARGIN);
        int z0 = Math.max(floorToInt(z - hRadius) - originZ - 1, 0);
        int z1 = Math.min(floorToInt(z + hRadius) - originZ, 15);
        boolean carved = false;
        for (int lx = x0; lx <= x1; lx++) {
            double xn = ((originX + lx) + 0.5 - x) / hRadius;
            for (int lz = z0; lz <= z1; lz++) {
                double zn = ((originZ + lz) + 0.5 - z) / hRadius;
                if (xn * xn + zn * zn >= 1.0) {
                    continue;
                }
                boolean[] grassAbove = new boolean[1];
                for (int wy = y1; wy > y0; wy--) {
                    double yn = (wy - 0.5 - y) / vRadius;
                    int mi = ((wy - minY) << 8) | (lz << 4) | lx;
                    if (skip.test(xn, yn, zn, wy) || mask.get(mi)) {
                        continue;
                    }
                    mask.set(mi);
                    carved |= carveBlock(cfg, level, originX + lx, wy, originZ + lz, lavaY, grassAbove);
                }
            }
        }
        return carved;
    }

    // WorldCarver.carveBlock（含 grass_block/mycelium -> 下方 dirt 修复）
    private static boolean carveBlock(Config cfg, WorldGenLevel level, int x, int y, int z,
                                      int lavaY, boolean[] grassAbove) {
        String name = BlockStateHelper.getName(level.getBlock(x, y, z));
        if (name.equals("grass_block") || name.equals("mycelium")) {
            grassAbove[0] = true;
        }
        if (!cfg.canReplace(name)) {
            return false;
        }
        level.setBlock(x, y, z, y <= lavaY ? LAVA : AIR);
        if (grassAbove[0]) {
            String belowName = BlockStateHelper.getName(level.getBlock(x, y - 1, z));
            if (belowName.equals("dirt")) {
                level.setBlock(x, y - 1, z, DIRT);
            }
        }
        return true;
    }

    // WorldCarver.canReach
    private static boolean canReach(int targetCX, int targetCZ, double x, double z,
                                    int step, int totalSteps, float thickness) {
        double midX = ((targetCX << 4) + 8.0);
        double midZ = ((targetCZ << 4) + 8.0);
        double dx = x - midX;
        double dz = z - midZ;
        double remaining = totalSteps - step;
        double limit = thickness + 2.0F + 16.0F;
        return dx * dx + dz * dz - remaining * remaining <= limit * limit;
    }

    private static float randomBetween(LegacyRandomSource rng, float min, float max) {
        return rng.nextFloat() * (max - min) + min;
    }

    private static float sinF(float a) {
        return (float) Math.sin((double) a);
    }

    private static float cosF(float a) {
        return (float) Math.cos((double) a);
    }

    private static int floorToInt(double v) {
        int i = (int) v;
        return v < (double) i ? i - 1 : i;
    }

    private interface Skip {
        boolean test(double xn, double yn, double zn, int worldY);
    }

    private interface FloatSampler {
        float sample(LegacyRandomSource rng);
    }

    private interface HeightSampler {
        int sample(LegacyRandomSource rng, int minY, int genDepth);
    }

    private interface Anchor {
        int resolveY(int minY, int genDepth);
    }

    private static Anchor absolute(int v) {
        return (minY, genDepth) -> v;
    }

    private static Anchor aboveBottom(int v) {
        return (minY, genDepth) -> minY + v;
    }

    private static Anchor belowTop(int v) {
        return (minY, genDepth) -> minY + genDepth - 1 - v;
    }

    private static FloatSampler constant(float v) {
        return rng -> v;
    }

    // UniformFloat: Mth.randomBetween(min, maxExclusive)
    private static FloatSampler uniformFloat(float min, float maxExclusive) {
        return rng -> randomBetween(rng, min, maxExclusive);
    }

    // TrapezoidFloat
    private static FloatSampler trapezoidFloat(float min, float max, float plateau) {
        float span = max - min;
        float slopeHalf = (span - plateau) / 2.0F;
        float rest = span - slopeHalf;
        return rng -> min + rng.nextFloat() * rest + rng.nextFloat() * slopeHalf;
    }

    // UniformHeight: Mth.randomBetweenInclusive(lo, hi)
    private static HeightSampler uniformHeight(Anchor minAnchor, Anchor maxAnchor) {
        return (rng, minY, genDepth) -> {
            int lo = minAnchor.resolveY(minY, genDepth);
            int hi = maxAnchor.resolveY(minY, genDepth);
            if (lo > hi) {
                return lo;
            }
            return rng.nextInt(hi - lo + 1) + lo;
        };
    }

    // VeryBiasedToBottomHeight(inner=1)
    private static HeightSampler veryBiasedToBottomHeight(Anchor minAnchor, Anchor maxAnchor) {
        return (rng, minY, genDepth) -> {
            int lo = minAnchor.resolveY(minY, genDepth);
            int hi = maxAnchor.resolveY(minY, genDepth);
            int inner = 1;
            if (hi - lo - inner + 1 <= 0) {
                return lo;
            }
            int a = rng.nextInt(hi - (lo + inner) + 1) + lo + inner;
            int b = rng.nextInt(a - 1 - lo + 1) + lo;
            return rng.nextInt(b - 1 + inner - lo + 1) + lo;
        };
    }

    // TrapezoidHeight
    private static HeightSampler trapezoidHeight(Anchor minAnchor, Anchor maxAnchor) {
        return (rng, minY, genDepth) -> {
            int lo = minAnchor.resolveY(minY, genDepth);
            int hi = maxAnchor.resolveY(minY, genDepth);
            int span = hi - lo;
            if (span <= 0) {
                return lo;
            }
            int plateau = 0;
            if (plateau >= span) {
                return rng.nextInt(span + 1) + lo;
            }
            int slopeHalf = (span - plateau) / 2;
            int rest = span - slopeHalf;
            return lo + rng.nextInt(rest + 1) + rng.nextInt(slopeHalf + 1);
        };
    }

    private static Config caveConfig(int seedIndex, boolean nether, float probability,
                                     Anchor yMin, Anchor yMax, Anchor lavaLevel) {
        Config c = new Config();
        c.canyon = false;
        c.nether = nether;
        c.seedIndex = seedIndex;
        c.probability = probability;
        c.y = uniformHeight(yMin, yMax);
        c.yScale = nether ? constant(0.5F) : uniformFloat(0.1F, 0.9F);
        c.hRadiusMult = nether ? constant(1.0F) : uniformFloat(0.7F, 1.4F);
        c.vRadiusMult = nether ? constant(1.0F) : uniformFloat(0.8F, 1.3F);
        c.floorLevel = nether ? constant(-0.7F) : uniformFloat(-1.0F, -0.4F);
        c.lavaLevel = lavaLevel;
        c.caveBound = nether ? 10 : 15;
        c.caveYScaleBase = nether ? 5.0 : 1.0;
        return c;
    }

    private static Config buildCanyon() {
        Config c = new Config();
        c.canyon = true;
        c.nether = false;
        c.seedIndex = 2;
        c.probability = 0.0027F;
        c.y = uniformHeight(absolute(10), absolute(67));
        c.yScale = constant(3.0F);
        c.verticalRotation = uniformFloat(-0.125F, 0.125F);
        c.thickness = trapezoidFloat(0.0F, 6.0F, 2.0F);
        c.horizontalRadiusFactor = uniformFloat(0.75F, 1.0F);
        c.distanceFactor = uniformFloat(0.75F, 1.0F);
        c.verticalRadiusDefaultFactor = 1.0F;
        c.verticalRadiusCenterFactor = 0.0F;
        c.widthSmoothness = 3;
        c.lavaLevel = aboveBottom(8);
        return c;
    }

    private static final class Config {
        boolean canyon;
        boolean nether;
        int seedIndex;
        float probability;
        HeightSampler y;
        FloatSampler yScale;
        FloatSampler hRadiusMult;
        FloatSampler vRadiusMult;
        FloatSampler floorLevel;
        FloatSampler verticalRotation;
        FloatSampler thickness;
        FloatSampler horizontalRadiusFactor;
        FloatSampler distanceFactor;
        float verticalRadiusDefaultFactor;
        float verticalRadiusCenterFactor;
        int widthSmoothness;
        Anchor lavaLevel;
        int caveBound;
        double caveYScaleBase;

        boolean canReplace(String name) {
            // 原版 WorldCarver.canReplaceBlock: Material.isReplaceable() 包含 air
            if (name.equals("air") || name.equals("cave_air")) return true;
            if (!nether && name.endsWith("_terracotta")) {
                return true;
            }
            return nether ? NETHER_REPLACEABLE.contains(name) : OVERWORLD_REPLACEABLE.contains(name);
        }
    }
}
