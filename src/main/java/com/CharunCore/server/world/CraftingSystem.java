package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;

import java.util.Set;

/**
 * 合成系统 facade (完全重写于 2026-08-13).
 * 委托 RecipeRegistry 处理配方数据/匹配/索引;
 * 提供匹配、消耗映射、配方书通知.
 *
 * 兼容旧 API: matchRecipe/consumeMap 接受 String grid (item 名),
 * 内部转 int itemId 调 RecipeRegistry.
 */
public final class CraftingSystem {

    private CraftingSystem() {}

    /** 启动时加载所有配方数据 */
    public static void loadAll() { RecipeRegistry.loadAll(); }

    /** 兼容旧 API: 旧代码用 CraftingSystem.matchRecipe(String[]) 引用 */
    public static RecipeRegistry.Recipe matchRecipe(String[] grid) { return matchByNames(grid); }

    /** 兼容别名: 旧代码用 CraftingSystem.consumeMap(CraftResult, String[]) 引用 */
    public static int[] consumeMap(RecipeRegistry.Recipe recipe, String[] grid) { return consumeMapByNames(recipe, grid); }

    /** 新 API: 匹配 int[] itemId 3×3 grid (0=空气) */
    public static RecipeRegistry.Recipe match(int[] grid) { return RecipeRegistry.match(grid); }

    /** 兼容旧 String grid: 内部转 int[] itemId 调 RecipeRegistry.match */
    public static RecipeRegistry.Recipe matchByNames(String[] nameGrid) {
        if (nameGrid == null) return null;
        int[] intGrid = new int[9];
        for (int i = 0; i < 9; i++) {
            String s = nameGrid[i];
            if (s == null || s.isEmpty()) continue;
            int id = BlockManager.getItemIdByName(stripNs(s));
            intGrid[i] = Math.max(id, 0);
        }
        return RecipeRegistry.match(intGrid);
    }

    /**
     * 兼容旧 String grid: 返回每格应消耗 1 个的索引 (长度 9).
     * 返回 int[] intGrid 格式 (与 RecipeRegistry.consumeMap 兼容),
     * 上层 NetworkHandler 可调用 convertNameMapToStringMap 转为 String[].
     */
    public static int[] consumeMapByNames(RecipeRegistry.Recipe recipe, String[] nameGrid) {
        if (recipe == null || nameGrid == null) return null;
        int[] intGrid = new int[9];
        for (int i = 0; i < 9; i++) {
            String s = nameGrid[i];
            if (s == null || s.isEmpty()) continue;
            int id = BlockManager.getItemIdByName(stripNs(s));
            intGrid[i] = Math.max(id, 0);
        }
        return RecipeRegistry.consumeMap(recipe, intGrid);
    }

    private static String stripNs(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }

    // ---- 兼容旧 tag/representative API (目前 item tag 解析未实现, 直接返回; place_recipe 已用 itemId 全集匹配) ----

    /** 旧 API: 代表性 item 名 (cell = item 名 → 原名). item tag 解析待补 */
    public static String representativeItem(String cell) {
        if (cell == null) return null;
        return cell;
    }

    /** 旧 API: 若 cell 是标签名返回成员集合, 否则返回 null (目前未实现) */
    public static Set<String> getTagItems(String cell) {
        return null;
    }

    // ---- 配方书通知 ----
    public static void sendRecipeBookAdd(NetworkHandler h) {
        if (h == null) return;
        for (RecipeRegistry.Recipe r : RecipeRegistry.all()) {
            h.sendPacket(h.ctx, 0x79, pb -> {
                pb.writeString(r.key);
                pb.writeBoolean(true);
                pb.writeInt(0);
                pb.writeInt(4);
                for (int i = 0; i < 4; i++) {
                    int itemId = r.grid[i];
                    int count = itemId == 0 ? 0 : 1;
                    pb.writeVarInt(count);
                    if (count > 0) {
                        pb.writeVarInt(itemId);
                        pb.writeVarInt(0);
                        pb.writeVarInt(0);
                    }
                }
            });
        }
    }
}