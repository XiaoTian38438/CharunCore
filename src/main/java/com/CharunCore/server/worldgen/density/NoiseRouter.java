package com.CharunCore.server.worldgen.density;

/**
 * 原版 net.minecraft.world.level.levelgen.NoiseRouter 移植。
 * 一个不可变数据类，包含 15 个 DensityFunction 字段，对应原版 NoiseRouter 所有子函数。
 * 字段顺序与原版构造器签名一致（参考 NoiseRouterData.overworld() 末尾 new NoiseRouter(...)）：
 *   (barrier, floodedness, levelSpread, lavaFluidLevel,
 *    temperature, vegetation, continents, erosion, depth, ridges,
 *    preliminarySurfaceLevel, finalDensity,
 *    veinToggle, veinRidged, auxiliary)
 */
public final class NoiseRouter {
    private final DensityFunction barrier;
    private final DensityFunction floodedness;
    private final DensityFunction levelSpread;       // ≡ fluidLevelSpread
    private final DensityFunction lavaFluidLevel;
    private final DensityFunction temperature;
    private final DensityFunction vegetation;
    private final DensityFunction continents;
    private final DensityFunction erosion;
    private final DensityFunction depth;
    private final DensityFunction ridges;
    private final DensityFunction preliminarySurfaceLevel;
    private final DensityFunction finalDensity;
    private final DensityFunction veinToggle;
    private final DensityFunction veinRidged;
    private final DensityFunction auxiliary;

    public NoiseRouter(DensityFunction barrier, DensityFunction floodedness, DensityFunction levelSpread,
                       DensityFunction lavaFluidLevel, DensityFunction temperature, DensityFunction vegetation,
                       DensityFunction continents, DensityFunction erosion, DensityFunction depth,
                       DensityFunction ridges, DensityFunction preliminarySurfaceLevel,
                       DensityFunction finalDensity, DensityFunction veinToggle, DensityFunction veinRidged,
                       DensityFunction auxiliary) {
        this.barrier = barrier;
        this.floodedness = floodedness;
        this.levelSpread = levelSpread;
        this.lavaFluidLevel = lavaFluidLevel;
        this.temperature = temperature;
        this.vegetation = vegetation;
        this.continents = continents;
        this.erosion = erosion;
        this.depth = depth;
        this.ridges = ridges;
        this.preliminarySurfaceLevel = preliminarySurfaceLevel;
        this.finalDensity = finalDensity;
        this.veinToggle = veinToggle;
        this.veinRidged = veinRidged;
        this.auxiliary = auxiliary;
    }

    // === 所有 getter ===
    public DensityFunction barrier()                  { return barrier; }
    public DensityFunction barrierNoise()            { return barrier; }              // 原版别名
    public DensityFunction floodedness()             { return floodedness; }
    public DensityFunction fluidLevelFloodednessNoise() { return floodedness; }       // 原版别名
    public DensityFunction levelSpread()             { return levelSpread; }
    public DensityFunction fluidLevelSpreadNoise()   { return levelSpread; }           // 原版别名
    public DensityFunction lavaFluidLevel()          { return lavaFluidLevel; }
    public DensityFunction lavaNoise()               { return lavaFluidLevel; }        // 原版别名
    public DensityFunction temperature()             { return temperature; }
    public DensityFunction vegetation()              { return vegetation; }
    public DensityFunction continents()              { return continents; }
    public DensityFunction erosion()                 { return erosion; }
    public DensityFunction depth()                  { return depth; }
    public DensityFunction ridges()                 { return ridges; }
    public DensityFunction preliminarySurface()      { return preliminarySurfaceLevel; }
    public DensityFunction preliminarySurfaceLevel() { return preliminarySurfaceLevel; }
    public DensityFunction finalDensity()            { return finalDensity; }
    public DensityFunction veinToggle()              { return veinToggle; }
    public DensityFunction veinRidged()             { return veinRidged; }
    public DensityFunction auxiliary()               { return auxiliary; }
    public DensityFunction veinGap()                { return auxiliary; }             // 原版别名

    /** Visitor 遍历整棵密度树 — 原版 mapAll 行为 */
    public NoiseRouter mapAll(DensityFunction.Visitor visitor) {
        return new NoiseRouter(
                barrier.mapAll(visitor), floodedness.mapAll(visitor), levelSpread.mapAll(visitor),
                lavaFluidLevel.mapAll(visitor), temperature.mapAll(visitor), vegetation.mapAll(visitor),
                continents.mapAll(visitor), erosion.mapAll(visitor), depth.mapAll(visitor),
                ridges.mapAll(visitor), preliminarySurfaceLevel.mapAll(visitor),
                finalDensity.mapAll(visitor), veinToggle.mapAll(visitor), veinRidged.mapAll(visitor),
                auxiliary.mapAll(visitor));
    }
}
