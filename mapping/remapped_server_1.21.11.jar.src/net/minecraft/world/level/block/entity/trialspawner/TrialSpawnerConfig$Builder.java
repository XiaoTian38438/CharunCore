/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
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
/*  75 */   private int spawnRange = 4;
/*  76 */   private float totalMobs = 6.0F;
/*  77 */   private float simultaneousMobs = 2.0F;
/*  78 */   private float totalMobsAddedPerPlayer = 2.0F;
/*  79 */   private float simultaneousMobsAddedPerPlayer = 1.0F;
/*  80 */   private int ticksBetweenSpawn = 40;
/*  81 */   private WeightedList<SpawnData> spawnPotentialsDefinition = WeightedList.of();
/*  82 */   private WeightedList<ResourceKey<LootTable>> lootTablesToEject = WeightedList.builder()
/*  83 */     .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES)
/*  84 */     .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY)
/*  85 */     .build();
/*  86 */   private ResourceKey<LootTable> itemsToDropWhenOminous = BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS;
/*     */   
/*     */   public Builder spawnRange(int paramInt) {
/*  89 */     this.spawnRange = paramInt;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public Builder totalMobs(float paramFloat) {
/*  94 */     this.totalMobs = paramFloat;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder simultaneousMobs(float paramFloat) {
/*  99 */     this.simultaneousMobs = paramFloat;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder totalMobsAddedPerPlayer(float paramFloat) {
/* 104 */     this.totalMobsAddedPerPlayer = paramFloat;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Builder simultaneousMobsAddedPerPlayer(float paramFloat) {
/* 109 */     this.simultaneousMobsAddedPerPlayer = paramFloat;
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder ticksBetweenSpawn(int paramInt) {
/* 114 */     this.ticksBetweenSpawn = paramInt;
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder spawnPotentialsDefinition(WeightedList<SpawnData> paramWeightedList) {
/* 119 */     this.spawnPotentialsDefinition = paramWeightedList;
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder lootTablesToEject(WeightedList<ResourceKey<LootTable>> paramWeightedList) {
/* 124 */     this.lootTablesToEject = paramWeightedList;
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder itemsToDropWhenOminous(ResourceKey<LootTable> paramResourceKey) {
/* 129 */     this.itemsToDropWhenOminous = paramResourceKey;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public TrialSpawnerConfig build() {
/* 134 */     return new TrialSpawnerConfig(this.spawnRange, this.totalMobs, this.simultaneousMobs, this.totalMobsAddedPerPlayer, this.simultaneousMobsAddedPerPlayer, this.ticksBetweenSpawn, this.spawnPotentialsDefinition, this.lootTablesToEject, this.itemsToDropWhenOminous);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerConfig$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */