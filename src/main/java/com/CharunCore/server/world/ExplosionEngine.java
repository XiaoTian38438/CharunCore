package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.entity.Entity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;
import com.CharunCore.server.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class ExplosionEngine {

    private static final Random RNG = new Random();

    private ExplosionEngine() {}

    public static void explode(DimensionType dim, double cx, double cy, double cz,
                               float power, boolean destroyBlocks) {
        Set<Long> destroyed = new HashSet<>();

        if (destroyBlocks) {
            int rays = 16;
            for (int rx = 0; rx < rays; rx++) {
                for (int ry = 0; ry < rays; ry++) {
                    for (int rz = 0; rz < rays; rz++) {
                        if (rx != 0 && rx != rays - 1 && ry != 0 && ry != rays - 1
                            && rz != 0 && rz != rays - 1) continue;

                        double dx = rx / (rays - 1.0) * 2.0 - 1.0;
                        double dy = ry / (rays - 1.0) * 2.0 - 1.0;
                        double dz = rz / (rays - 1.0) * 2.0 - 1.0;
                        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        if (len < 1e-6) continue;
                        dx /= len; dy /= len; dz /= len;

                        float intensity = power * (0.7f + RNG.nextFloat() * 0.6f);
                        double px = cx, py = cy, pz = cz;

                        while (intensity > 0.0f) {
                            int bx = (int) Math.floor(px);
                            int by = (int) Math.floor(py);
                            int bz = (int) Math.floor(pz);
                            if (by < dim.minY || by >= dim.minY + dim.height) break;

                            int state = WorldManager.getBlockState(dim, bx, by, bz);
                            if (state != 0) {
                                String name = BlockStateHelper.getName(state);
                                // 水吸收爆炸射线(原版): 水下爆炸不破坏方块, 仅伤害实体
                                if ("water".equals(name) || "lava".equals(name)) break;
                                float resistance = blastResistance(name);
                                if (resistance < 0) break;
                                intensity -= (resistance + 0.3f) * 0.3f;
                                if (intensity > 0.0f) {
                                    destroyed.add(pack(bx, by, bz));
                                }
                            }

                            px += dx * 0.3;
                            py += dy * 0.3;
                            pz += dz * 0.3;
                            intensity -= 0.225f * 0.3f * 3.0f;
                        }
                    }
                }
            }

            for (long key : destroyed) {
                int bx = (int) (key >> 38);
                int by = (int) ((key << 26) >> 52);
                int bz = (int) ((key << 38) >> 38);
                int state = WorldManager.getBlockState(dim, bx, by, bz);
                if (state == 0) continue;
                String name = BlockStateHelper.getName(state);
                // TNT 连锁: 爆炸波及的 TNT 被激活为实体(随机 10~30 刻引信, 原版行为)
                if ("tnt".equals(name)) {
                    RedstoneEngine.primeTnt(dim, bx, by, bz, 10 + RNG.nextInt(21));
                    continue;
                }
                WorldManager.setBlock(dim, bx, by, bz, 0);
                NetworkHandler.broadcastBlockChange(bx, by, bz, 0);
                if (RNG.nextFloat() < 1.0f / Math.max(1.0f, power)) {
                    String dropName = explosionDrop(name);
                    if (dropName != null) {
                        int itemId = BlockManager.getItemIdByName(dropName);
                        if (itemId > 0) {
                            ItemEntity ie = new ItemEntity(EntityManager.allocateId(),
                                bx + 0.5, by + 0.5, bz + 0.5, itemId, 1);
                            ie.dim = dim;
                            EntityManager.addEntity(ie);
                        }
                    }
                }
            }
        }

        double radius = power * 2.0;
        java.util.List<Entity> toDestroy = new java.util.ArrayList<>();
        for (Entity e : EntityManager.getAllEntities()) {
            if (e.dim != dim) continue;
            double dx = e.x - cx, dy = e.y - cy, dz = e.z - cz;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > radius || dist < 1e-6) continue;
            double factor = (1.0 - dist / radius);
            if (e instanceof LivingEntity le) {
                le.damage((float) ((factor * factor * 7 + factor) * 2 * power), "explosion");
            }
            // 原版: 爆炸摧毁范围内掉落物(防刷怪塔堆积, 也是用户报"TNT炸不掉落物"的修复)。
            if (e instanceof ItemEntity) {
                toDestroy.add(e);
                continue;
            }
            e.vx += dx / dist * factor;
            e.vy += dy / dist * factor;
            e.vz += dz / dist * factor;
        }
        for (Entity e : toDestroy) {
            EntityManager.removeEntity(e.id);
        }

        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != dim || p.ctx == null) continue;
            double dx = p.x - cx, dy = p.y - cy, dz = p.z - cz;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist <= radius && dist > 1e-6) {
                double factor = (1.0 - dist / radius);
                p.damagePlayer((float) ((factor * factor * 7 + factor) * 2 * power), "explosion");
                // Bug38 修复: 击退前检查落点是否为实心方块 —— 曾把玩家水平推进墙里,
                // 客户端本地碰撞卡死表现为"假死: 无法移动/F5 看不见自己/可交互",
                // 只能反复重进。目标格(脚/眼)任一为实心则大幅削减水平推力。
                double kx = dx / dist * factor, kz = dz / dist * factor;
                int tx = (int) Math.floor(p.x + kx * 4.0);
                int tz = (int) Math.floor(p.z + kz * 4.0);
                boolean wallAhead = isSolidForPush(dim, tx, (int) Math.floor(p.y), tz)
                        || isSolidForPush(dim, tx, (int) Math.floor(p.y + 1.0), tz);
                if (wallAhead) { kx *= 0.15; kz *= 0.15; }
                // 击退: 限制 y 分量避免把玩家推进方块/天花板导致"假死卡住"(原版也夹紧击退)。
                double ky = dy / dist * factor + 0.2;
                ky = Math.min(ky, 0.4);
                p.knockback(kx, kz, ky);
            }
            if (dist < 64) {
                p.sendExplosionEffect(cx, cy, cz, power);
            }
        }
    }

    private static long pack(int x, int y, int z) {
        return ((long) x & 0x3FFFFFF) << 38 | ((long) y & 0xFFF) << 26 | ((long) z & 0x3FFFFFF);
    }

    private static boolean isSolidForPush(DimensionType dim, int x, int y, int z) {
        int st = com.CharunCore.server.world.WorldManager.getBlockState(dim, x, y, z);
        if (st == 0) return false;
        String n = com.CharunCore.server.utils.BlockStateHelper.getName(st);
        return n != null && !n.equals("air") && !n.equals("cave_air") && !n.equals("void_air")
                && !n.equals("water") && !n.equals("lava")
                && com.CharunCore.server.utils.BlockManager.hasCollision(n);
    }

    private static float blastResistance(String name) {
        if (name == null) return 0.0f;
        return switch (name) {
            case "water", "lava" -> 100.0f;
            case "bedrock", "barrier", "end_portal_frame", "end_portal",
                 "obsidian", "crying_obsidian", "respawn_anchor",
                 "reinforced_deepslate", "ancient_debris" -> -1.0f;
            case "stone", "cobblestone", "deepslate", "cobbled_deepslate",
                 "granite", "diorite", "andesite", "tuff", "basalt", "blackstone" -> 6.0f;
            case "iron_block", "gold_block", "diamond_block", "netherite_block" -> 6.0f;
            case "dirt", "grass_block", "sand", "gravel", "clay", "snow_block",
                 "podzol", "coarse_dirt", "red_sand", "soul_sand", "soul_soil" -> 0.5f;
            case "oak_log", "birch_log", "spruce_log", "jungle_log", "acacia_log",
                 "dark_oak_log", "oak_planks", "birch_planks", "spruce_planks" -> 2.0f;
            case "oak_leaves", "birch_leaves", "spruce_leaves", "jungle_leaves",
                 "acacia_leaves", "dark_oak_leaves" -> 0.2f;
            case "glass", "glass_pane", "short_grass", "tall_grass", "fern" -> 0.0f;
            case "netherrack" -> 0.4f;
            case "end_stone" -> 9.0f;
            default -> 1.0f;
        };
    }

    private static String explosionDrop(String name) {
        if (name == null) return null;
        return switch (name) {
            case "grass_block", "podzol", "coarse_dirt", "farmland", "dirt_path" -> "dirt";
            case "stone" -> "cobblestone";
            case "deepslate" -> "cobbled_deepslate";
            case "coal_ore", "deepslate_coal_ore" -> "coal";
            case "iron_ore", "deepslate_iron_ore" -> "raw_iron";
            case "copper_ore", "deepslate_copper_ore" -> "raw_copper";
            case "gold_ore", "deepslate_gold_ore" -> "raw_gold";
            case "diamond_ore", "deepslate_diamond_ore" -> "diamond";
            case "emerald_ore", "deepslate_emerald_ore" -> "emerald";
            case "glass", "glass_pane", "ice", "short_grass", "tall_grass", "fern",
                 "oak_leaves", "birch_leaves", "spruce_leaves", "jungle_leaves",
                 "acacia_leaves", "dark_oak_leaves" -> null;
            default -> name;
        };
    }
}
