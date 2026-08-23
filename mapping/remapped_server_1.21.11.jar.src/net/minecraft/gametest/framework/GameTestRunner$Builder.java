/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.server.level.ServerLevel;
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
/*     */   private final ServerLevel level;
/*  54 */   private final GameTestTicker testTicker = GameTestTicker.SINGLETON;
/*  55 */   private GameTestRunner.GameTestBatcher batcher = GameTestBatchFactory.fromGameTestInfo();
/*  56 */   private GameTestRunner.StructureSpawner existingStructureSpawner = GameTestRunner.StructureSpawner.IN_PLACE;
/*  57 */   private GameTestRunner.StructureSpawner newStructureSpawner = GameTestRunner.StructureSpawner.NOT_SET;
/*     */   private final Collection<GameTestBatch> batches;
/*     */   private boolean haltOnError = false;
/*     */   private boolean clearBetweenBatches = false;
/*     */   
/*     */   private Builder(Collection<GameTestBatch> paramCollection, ServerLevel paramServerLevel) {
/*  63 */     this.batches = paramCollection;
/*  64 */     this.level = paramServerLevel;
/*     */   }
/*     */   
/*     */   public static Builder fromBatches(Collection<GameTestBatch> paramCollection, ServerLevel paramServerLevel) {
/*  68 */     return new Builder(paramCollection, paramServerLevel);
/*     */   }
/*     */   
/*     */   public static Builder fromInfo(Collection<GameTestInfo> paramCollection, ServerLevel paramServerLevel) {
/*  72 */     return fromBatches(GameTestBatchFactory.fromGameTestInfo().batch(paramCollection), paramServerLevel);
/*     */   }
/*     */   
/*     */   public Builder haltOnError() {
/*  76 */     this.haltOnError = true;
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public Builder clearBetweenBatches() {
/*  81 */     this.clearBetweenBatches = true;
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public Builder newStructureSpawner(GameTestRunner.StructureSpawner paramStructureSpawner) {
/*  86 */     this.newStructureSpawner = paramStructureSpawner;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public Builder existingStructureSpawner(StructureGridSpawner paramStructureGridSpawner) {
/*  91 */     this.existingStructureSpawner = paramStructureGridSpawner;
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder batcher(GameTestRunner.GameTestBatcher paramGameTestBatcher) {
/*  96 */     this.batcher = paramGameTestBatcher;
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestRunner build() {
/* 101 */     return new GameTestRunner(this.batcher, this.batches, this.level, this.testTicker, this.existingStructureSpawner, this.newStructureSpawner, this.haltOnError, this.clearBetweenBatches);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestRunner$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */