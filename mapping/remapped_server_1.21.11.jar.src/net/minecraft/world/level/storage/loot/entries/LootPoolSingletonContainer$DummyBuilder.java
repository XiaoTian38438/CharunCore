/*     */ package net.minecraft.world.level.storage.loot.entries;
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
/*     */ class DummyBuilder
/*     */   extends LootPoolSingletonContainer.Builder<LootPoolSingletonContainer.DummyBuilder>
/*     */ {
/*     */   private final LootPoolSingletonContainer.EntryConstructor constructor;
/*     */   
/*     */   public DummyBuilder(LootPoolSingletonContainer.EntryConstructor paramEntryConstructor) {
/* 119 */     this.constructor = paramEntryConstructor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DummyBuilder getThis() {
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootPoolEntryContainer build() {
/* 129 */     return this.constructor.build(this.weight, this.quality, getConditions(), getFunctions());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$DummyBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */