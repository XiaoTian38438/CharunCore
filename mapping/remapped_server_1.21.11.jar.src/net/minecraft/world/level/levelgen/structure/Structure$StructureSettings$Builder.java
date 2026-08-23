/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/*    */   private final HolderSet<Biome> biomes;
/* 72 */   private Map<MobCategory, StructureSpawnOverride> spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/* 73 */   private GenerationStep.Decoration step = Structure.StructureSettings.DEFAULT.step;
/* 74 */   private TerrainAdjustment terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*    */   
/*    */   public Builder(HolderSet<Biome> paramHolderSet) {
/* 77 */     this.biomes = paramHolderSet;
/*    */   }
/*    */   
/*    */   public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> paramMap) {
/* 81 */     this.spawnOverrides = paramMap;
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public Builder generationStep(GenerationStep.Decoration paramDecoration) {
/* 86 */     this.step = paramDecoration;
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public Builder terrainAdapation(TerrainAdjustment paramTerrainAdjustment) {
/* 91 */     this.terrainAdaption = paramTerrainAdjustment;
/* 92 */     return this;
/*    */   }
/*    */   
/*    */   public Structure.StructureSettings build() {
/* 96 */     return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\Structure$StructureSettings$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */