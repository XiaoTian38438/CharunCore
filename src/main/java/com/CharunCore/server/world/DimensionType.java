package com.CharunCore.server.world;

/**
 * 维度类型元数据。
 * 对照原版 net.minecraft.world.level.dimension.DimensionType 与
 * data/worldgen/DimensionTypes 的 overworld / the_nether / the_end 定义。
 *
 * 注：config 阶段下发的注册表 dumped_registries/reg_14.bin 仅含维度类型
 * 名称索引（顺序：0=overworld, 1=overworld_caves, 2=the_end, 3=the_nether），
 * 客户端实际使用的 ambient_light / fixed_time / ultrawarm / piglin_safe 等
 * 来自其内置维度定义。本枚举补全这些字段，供服务端 worldgen / 逻辑使用。
 */
public enum DimensionType {
    OVERWORLD(-64, 384, 63, true, false, 1.0, 0, 0, "minecraft:overworld",
            0.0f, false, false, -1L, 384, true, false, false, "minecraft:infiniburn_overworld"),
    THE_NETHER(0, 256, 31, false, true, 8.0, 1, 3, "minecraft:the_nether",
            0.1f, true, true, 18000L, 128, false, true, true, "minecraft:infiniburn_nether"),
    THE_END(0, 256, 0, true, false, 1.0, 2, 2, "minecraft:the_end",
            0.25f, false, true, 18000L, 256, false, false, false, "minecraft:infiniburn_end");

    public final int minY;
    public final int height;
    public final int seaLevel;
    public final boolean hasSkylight;
    public final boolean hasCeiling;
    public final double coordinateScale;
    public final int id;
    /**
     * Index inside the vanilla minecraft:dimension_type registry sent during configuration.
     * Dump order (dumped_registries/reg_14.bin):
     *   0 = overworld, 1 = overworld_caves, 2 = the_end, 3 = the_nether
     * This is NOT the same as the internal id.
     */
    public final int registryId;
    public final String key;

    // ── 维度元数据（对照原版 DimensionType codec / DimensionTypes.bootstrap）──
    /** 环境光亮度：主世界 0.0，下界 0.1，末地 0.25。 */
    public final float ambientLight;
    /** 是否超暖（下界 true）：水蒸发、熔岩快速流动。 */
    public final boolean ultraWarm;
    /** 是否有固定时刻。下界/末地 true（fixedTime=18000 恒为夜晚）。 */
    public final boolean hasFixedTime;
    /** 固定时刻（游戏刻）。无固定时返回 -1。下界/末地 = 18000。 */
    public final long fixedTime;
    /** 逻辑高度（生存可放置区块的 Y 高度范围）。主世界 384 / 下界 128 / 末地 256。 */
    public final int logicalHeight;
    /** 床能否使用（主世界 true，下界/末地会爆炸）。 */
    public final boolean bedWorks;
    /** 猪灵是否中立（下界 true）。 */
    public final boolean piglinSafe;
    /** 重生锚是否可用（下界 true）。 */
    public final boolean respawnAnchorWorks;
    /** infiniburn 标签（原版 BlockTags.INFINIBURN_*），用于火焰燃烧的方块判定。 */
    public final String infiniburn;

    DimensionType(int minY, int height, int seaLevel, boolean hasSkylight,
                  boolean hasCeiling, double coordinateScale, int id,
                  int registryId, String key,
                  float ambientLight, boolean ultraWarm, boolean hasFixedTime,
                  long fixedTime, int logicalHeight, boolean bedWorks,
                  boolean piglinSafe, boolean respawnAnchorWorks, String infiniburn) {
        this.minY = minY;
        this.height = height;
        this.seaLevel = seaLevel;
        this.hasSkylight = hasSkylight;
        this.hasCeiling = hasCeiling;
        this.coordinateScale = coordinateScale;
        this.id = id;
        this.registryId = registryId;
        this.key = key;
        this.ambientLight = ambientLight;
        this.ultraWarm = ultraWarm;
        this.hasFixedTime = hasFixedTime;
        this.fixedTime = fixedTime;
        this.logicalHeight = logicalHeight;
        this.bedWorks = bedWorks;
        this.piglinSafe = piglinSafe;
        this.respawnAnchorWorks = respawnAnchorWorks;
        this.infiniburn = infiniburn;
    }

    /** 兼容历史调用方：是否为超暖维度（下界）。 */
    public boolean isUltraWarm() { return ultraWarm; }

    /** 固定时刻（游戏刻）；无固定时返回 -1。 */
    public long getFixedTime() { return fixedTime; }

    public boolean hasFixedTime() { return hasFixedTime; }

    public float getAmbientLight() { return ambientLight; }

    public int getLogicalHeight() { return logicalHeight; }

    public boolean bedWorks() { return bedWorks; }

    public boolean piglinSafe() { return piglinSafe; }

    public boolean respawnAnchorWorks() { return respawnAnchorWorks; }

    public static DimensionType byId(int id) {
        for (DimensionType d : values()) {
            if (d.id == id) return d;
        }
        return OVERWORLD;
    }

    public static DimensionType byKey(String key) {
        if (key == null) return OVERWORLD;
        for (DimensionType d : values()) {
            if (d.key.equals(key)) return d;
        }
        return OVERWORLD;
    }

    public String getFolderName() {
        return switch (this) {
            case OVERWORLD -> "";
            case THE_NETHER -> "DIM-1";
            case THE_END -> "DIM1";
        };
    }

    public String getRegionDir() {
        String base = "world";
        if (this == OVERWORLD) return base + "/region";
        return base + "/" + getFolderName() + "/region";
    }
}
