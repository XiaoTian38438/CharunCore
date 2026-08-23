/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  96 */   private final Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> spawners = Util.makeEnumMap(MobCategory.class, paramMobCategory -> WeightedList.builder());
/*  97 */   private final Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts = Maps.newLinkedHashMap();
/*  98 */   private float creatureGenerationProbability = 0.1F;
/*     */   
/*     */   public Builder addSpawn(MobCategory paramMobCategory, int paramInt, MobSpawnSettings.SpawnerData paramSpawnerData) {
/* 101 */     ((WeightedList.Builder)this.spawners.get(paramMobCategory)).add(paramSpawnerData, paramInt);
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder addMobCharge(EntityType<?> paramEntityType, double paramDouble1, double paramDouble2) {
/* 128 */     this.mobSpawnCosts.put(paramEntityType, new MobSpawnSettings.MobSpawnCost(paramDouble2, paramDouble1));
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public Builder creatureGenerationProbability(float paramFloat) {
/* 133 */     this.creatureGenerationProbability = paramFloat;
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public MobSpawnSettings build() {
/* 138 */     return new MobSpawnSettings(this.creatureGenerationProbability, (Map<MobCategory, WeightedList<MobSpawnSettings.SpawnerData>>)this.spawners
/*     */         
/* 140 */         .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, paramEntry -> ((WeightedList.Builder)paramEntry.getValue()).build())), 
/* 141 */         (Map<EntityType<?>, MobSpawnSettings.MobSpawnCost>)ImmutableMap.copyOf(this.mobSpawnCosts));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\MobSpawnSettings$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */