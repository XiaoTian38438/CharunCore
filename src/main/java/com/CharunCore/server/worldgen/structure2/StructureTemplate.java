package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.worldgen.WorldGenLevel;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class StructureTemplate {

    private static final String TEMPLATE_DIR = "json/minecraft/structure/";

    public final int sizeX, sizeY, sizeZ;
    private final int[] paletteStateIds;
    private final List<StructureBlockInfo> blocks;
    private List<JigsawBlockInfo> jigsaws;
    /** 模板顶层 entities 列表(村民/铁傀儡/牛羊/掠夺者等), 放置时按旋转/镜像变换收集。 */
    private final List<TemplateEntity> entities = new ArrayList<>();

    /** 实体条目: blockPos 为模板相对坐标(整数格), entityName 为 nbt.id 去掉 minecraft: 前缀。 */
    public record TemplateEntity(int bx, int by, int bz, String entityName) {}

    public StructureTemplate(int sizeX, int sizeY, int sizeZ,
                              int[] paletteStateIds, List<StructureBlockInfo> blocks) {
        this.sizeX = sizeX; this.sizeY = sizeY; this.sizeZ = sizeZ;
        this.paletteStateIds = paletteStateIds;
        this.blocks = blocks;
    }

    public List<TemplateEntity> getEntities() { return entities; }

    public List<StructureBlockInfo> blocks() { return blocks; }

    public List<JigsawBlockInfo> getJigsaws() {
        if (jigsaws != null) return jigsaws;
        jigsaws = new ArrayList<>();
        for (StructureBlockInfo info : blocks) {
            String blockName = BlockStateHelper.getName(info.blockStateId());
            if (blockName != null && blockName.equals("jigsaw")) {
                NbtMap nbt = info.nbt();
                String pool = nbt != null ? nbt.getString("pool") : null;
                String name = nbt != null ? nbt.getString("name") : null;
                String target = nbt != null ? nbt.getString("target") : null;
                String joint = nbt != null ? nbt.getString("joint") : null;
                boolean rollable = "rollable".equals(joint);
                int placementPriority = nbt != null ? nbt.getInt("placement_priority") : 0;
                int selectionPriority = nbt != null ? nbt.getInt("selection_priority") : 0;
                if (pool != null) pool = stripMinecraft(pool);
                if (name != null) name = stripMinecraft(name);
                if (target != null) target = stripMinecraft(target);
                jigsaws.add(new JigsawBlockInfo(info.x(), info.y(), info.z(), info.blockStateId(),
                    info.nbt(), pool, name, target, rollable, placementPriority, selectionPriority));
            }
        }
        return jigsaws;
    }

    private static String stripMinecraft(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }

    public BoundingBox getBoundingBox(int originX, int originY, int originZ, Rotation rotation) {
        int[] rotated = rotatedSize(rotation);
        return new BoundingBox(originX, originY, originZ,
            originX + rotated[0] - 1, originY + rotated[1] - 1, originZ + rotated[2] - 1);
    }

    public int[] rotatedSize(Rotation rotation) {
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
            return new int[]{sizeZ, sizeY, sizeX};
        }
        return new int[]{sizeX, sizeY, sizeZ};
    }

    public void placeInWorld(WorldGenLevel level, int originX, int originY, int originZ,
                              Rotation rotation, Mirror mirror) {
        placeInWorld(level, originX, originY, originZ, rotation, mirror, false);
    }

    /** skipAir=true 时不覆盖目标处空气（原版 BlockIgnoreProcessor.STRUCTURE_AND_AIR 语义）。 */
    public void placeInWorld(WorldGenLevel level, int originX, int originY, int originZ,
                              Rotation rotation, Mirror mirror, boolean skipAir) {
        for (StructureBlockInfo info : blocks) {
            if (skipAir && info.blockStateId() == 0) continue;
            String _bn = BlockStateHelper.getName(info.blockStateId());
            if (skipAir && _bn != null && (_bn.equals("air") || _bn.endsWith(":air"))) continue;
            String blockName = BlockStateHelper.getName(info.blockStateId());
            // 【原版语义】structure_block 数据标记方块不放置, 记录到 level 延迟处理
            // (Chest→下方箱子战利品 / Sentry→潜影贝 / Elytra→鞘翅展示框, 由
            //  StructureMarkerProcessor 在生成完成后统一处理)。
            if (blockName != null && blockName.equals("structure_block")) {
                if (info.nbt() != null && info.nbt().containsKey("metadata")
                    && !info.nbt().getString("metadata").isEmpty()) {
                    int[] pos = transformPosition(info.x(), info.y(), info.z(), rotation, mirror);
                    level.addDataMarker(originX + pos[0], originY + pos[1], originZ + pos[2],
                        info.nbt().getString("metadata"), rotation, mirror);
                }
                continue;
            }
            // Bug49: jigsaw 位按原版 keepJigsaws=false 语义回填空气(曾 continue 留下地形残块,
            // 结构门口/通道被地形堵住); structure_void 是占位方块不得放置(结构里出现"幽灵方块")。
            if (blockName != null && blockName.equals("jigsaw")) {
                int[] jp = transformPosition(info.x(), info.y(), info.z(), rotation, mirror);
                level.setBlock(originX + jp[0], originY + jp[1], originZ + jp[2], 0);
                continue;
            }
            if (blockName != null && blockName.equals("structure_void")) continue;
            int[] pos = transformPosition(info.x(), info.y(), info.z(), rotation, mirror);
            int worldX = originX + pos[0];
            int worldY = originY + pos[1];
            int worldZ = originZ + pos[2];
            int stateId = BlockTransform.transform(info.blockStateId(), rotation, mirror);
            if (stateId == 0 && blockName != null && !blockName.equals("air")) continue;
            if (worldY < -64 || worldY > 319) continue;
            level.setBlock(worldX, worldY, worldZ, stateId);
            if (info.nbt() != null && !info.nbt().isEmpty() && isBlockEntityBlock(blockName)) {
                org.cloudburstmc.nbt.NbtMapBuilder beBuilder = org.cloudburstmc.nbt.NbtMap.builder();
                beBuilder.putString("id", "minecraft:" + blockName);
                beBuilder.putInt("x", worldX);
                beBuilder.putInt("y", worldY);
                beBuilder.putInt("z", worldZ);
                for (String key : info.nbt().keySet()) {
                    if (key.equals("id") || key.equals("x") || key.equals("y") || key.equals("z")) continue;
                    Object val = info.nbt().get(key);
                    if (val instanceof String || val instanceof Integer || val instanceof Long
                        || val instanceof Byte || val instanceof Short || val instanceof Float
                        || val instanceof Double || val instanceof org.cloudburstmc.nbt.NbtMap
                        || val instanceof org.cloudburstmc.nbt.NbtList
                        || val instanceof int[] || val instanceof long[] || val instanceof byte[]) {
                        beBuilder.put(key, val);
                    }
                }
                level.setBlockEntity(worldX, worldY, worldZ, beBuilder.build());
            }
        }

        // 结构自带生物: 模板 entities 列表随 piece 一起变换收集(村民/铁傀儡/动物/掠夺者等),
        // 由 WorldGenLevel 汇总、区块生成完成后统一 flush 到 EntityManager。
        for (TemplateEntity te : entities) {
            int[] p = transformPosition(te.bx(), te.by(), te.bz(), rotation, mirror);
            level.addPendingEntity(originX + p[0] + 0.5, originY + p[1], originZ + p[2] + 0.5, te.entityName());
        }
    }

    /** 收集到的数据标记方块（structure_block, 含 metadata）。 */
    public record DataMarker(int x, int y, int z, String metadata,
                             Rotation rotation, Mirror mirror) {}

    // 方块实体名称白名单（1.21）。无法访问方块注册表时作为回退判断。
    // 用后缀 + 精确名双重覆盖，补齐旧名单遗漏的 BE（smoker/blast_furnace/sculk_catalyst/
    // sculk_sensor/trial_spawner/vault/crafter/decorated_pot/smithing_table 等）。
    private static final java.util.Set<String> BLOCK_ENTITY_NAMES = java.util.Set.of(
        "spawner", "mob_spawner", "sign", "hanging_sign", "lectern", "brewing_stand",
        "furnace", "blast_furnace", "smoker", "dispenser", "dropper", "hopper", "beacon",
        "bed", "bell", "campfire", "soul_campfire", "command_block", "chain_command_block",
        "repeating_command_block", "enchanting_table", "end_portal", "end_gateway", "jigsaw",
        "jukebox", "skull", "structure_block", "conduit", "chiseled_bookshelf",
        "trial_spawner", "vault", "sculk_catalyst", "sculk_sensor", "calibrated_sculk_sensor",
        "smithing_table", "crafter", "decorated_pot", "daylight_detector", "note_block",
        "piston", "sticky_piston");

    private static boolean isBlockEntityBlock(String name) {
        if (name == null) return false;
        // 颜色/变体后缀：*_chest(含 ender_chest/trapped_chest)、*_barrel、*_shulker_box、
        // *_banner(含 wall_banner)、*_bed(含各色床)、*_campfire(含 soul_campfire)
        if (name.endsWith("chest") || name.endsWith("barrel") || name.endsWith("shulker_box")
            || name.endsWith("banner") || name.endsWith("bed") || name.endsWith("campfire")) {
            return true;
        }
        return BLOCK_ENTITY_NAMES.contains(name);
    }

    public int[] transformPosition(int x, int y, int z, Rotation rotation, Mirror mirror) {
        int mx = x, mz = z;
        if (mirror == Mirror.LEFT_RIGHT) {
            mx = sizeX - 1 - x;
        } else if (mirror == Mirror.FRONT_BACK) {
            mz = sizeZ - 1 - z;
        }
        switch (rotation) {
            case CLOCKWISE_90:
                return new int[]{sizeZ - 1 - mz, y, mx};
            case CLOCKWISE_180:
                return new int[]{sizeX - 1 - mx, y, sizeZ - 1 - mz};
            case COUNTERCLOCKWISE_90:
                return new int[]{mz, y, sizeX - 1 - mx};
            default:
                return new int[]{mx, y, mz};
        }
    }

    public static StructureTemplate load(String path) {
        String fullPath = TEMPLATE_DIR + path + ".nbt";
        File file = new File(fullPath);
        if (!file.exists()) {
            fullPath = "mapping/remapped_server_1.21.11.jar.src/data/minecraft/structure/" + path + ".nbt";
            file = new File(fullPath);
        }
        if (!file.exists()) return null;

        NbtMap template;
        try (FileInputStream fis = new FileInputStream(file);
             java.util.zip.GZIPInputStream gz = new java.util.zip.GZIPInputStream(fis);
             NBTInputStream nbtIn = new NBTInputStream(new DataInputStream(gz))) {
            template = (NbtMap) nbtIn.readTag();
        } catch (Exception e) {
            return null;
        }

        List<Integer> sizeList = template.getList("size", NbtType.INT);
        if (sizeList == null || sizeList.size() < 3) return null;
        int sizeX = sizeList.get(0), sizeY = sizeList.get(1), sizeZ = sizeList.get(2);

        List<NbtMap> paletteList = template.getList("palette", NbtType.COMPOUND);
        if (paletteList == null || paletteList.isEmpty()) return null;

        int[] stateIds = new int[paletteList.size()];
        for (int i = 0; i < paletteList.size(); i++) {
            NbtMap pEntry = paletteList.get(i);
            String blockName = pEntry.getString("Name");
            if (blockName == null) { stateIds[i] = 0; continue; }
            blockName = blockName.replace("minecraft:", "");
            NbtMap props = pEntry.getCompound("Properties");
            if (props != null && !props.isEmpty()) {
                stateIds[i] = resolveWithProps(blockName, props);
            } else {
                stateIds[i] = BlockStateHelper.getDefault(blockName);
            }
        }

        List<NbtMap> blocksList = template.getList("blocks", NbtType.COMPOUND);
        if (blocksList == null) return new StructureTemplate(sizeX, sizeY, sizeZ, stateIds, new ArrayList<>());

        List<StructureBlockInfo> blocks = new ArrayList<>();
        for (NbtMap blockEntry : blocksList) {
            List<Integer> pos = blockEntry.getList("pos", NbtType.INT);
            if (pos == null || pos.size() < 3) continue;
            int stateIdx = blockEntry.getInt("state");
            NbtMap nbt = blockEntry.getCompound("nbt");
            int stateId = stateIdx < stateIds.length ? stateIds[stateIdx] : 0;
            blocks.add(new StructureBlockInfo(pos.get(0), pos.get(1), pos.get(2), stateId, nbt));
        }

        blocks.sort((a, b) -> {
            int c = Integer.compare(a.y(), b.y());
            if (c != 0) return c;
            c = Integer.compare(a.x(), b.x());
            return c != 0 ? c : Integer.compare(a.z(), b.z());
        });

        StructureTemplate tpl = new StructureTemplate(sizeX, sizeY, sizeZ, stateIds, blocks);

        List<NbtMap> entityList = template.getList("entities", NbtType.COMPOUND);
        if (entityList != null) {
            for (NbtMap entry : entityList) {
                NbtMap entNbt = entry.getCompound("nbt");
                String id = entNbt != null ? entNbt.getString("id") : null;
                List<Integer> bp = entry.getList("blockPos", NbtType.INT);
                if (id == null || id.isEmpty() || bp == null || bp.size() < 3) continue;
                String name = id.startsWith("minecraft:") ? id.substring(10) : id;
                tpl.entities.add(new TemplateEntity(bp.get(0), bp.get(1), bp.get(2), name));
            }
        }
        return tpl;
    }

    private static int resolveWithProps(String name, NbtMap props) {
        int base = BlockStateHelper.getDefault(name);
        if (base == 0) return 0;
        java.util.Map<String, String> propMap = new java.util.HashMap<>();
        for (String key : props.keySet()) {
            propMap.put(key, props.getString(key));
        }
        return BlockStateHelper.getState(name, propMap);
    }
}
