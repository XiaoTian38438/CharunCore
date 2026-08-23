/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.NoiseChunk;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class CarvingContext extends WorldGenerationContext {
/*    */   private final RegistryAccess registryAccess;
/*    */   private final NoiseChunk noiseChunk;
/*    */   private final RandomState randomState;
/*    */   private final SurfaceRules.RuleSource surfaceRule;
/*    */   
/*    */   public CarvingContext(NoiseBasedChunkGenerator paramNoiseBasedChunkGenerator, RegistryAccess paramRegistryAccess, LevelHeightAccessor paramLevelHeightAccessor, NoiseChunk paramNoiseChunk, RandomState paramRandomState, SurfaceRules.RuleSource paramRuleSource) {
/* 26 */     super((ChunkGenerator)paramNoiseBasedChunkGenerator, paramLevelHeightAccessor);
/* 27 */     this.registryAccess = paramRegistryAccess;
/* 28 */     this.noiseChunk = paramNoiseChunk;
/* 29 */     this.randomState = paramRandomState;
/* 30 */     this.surfaceRule = paramRuleSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public Optional<BlockState> topMaterial(Function<BlockPos, Holder<Biome>> paramFunction, ChunkAccess paramChunkAccess, BlockPos paramBlockPos, boolean paramBoolean) {
/* 38 */     return this.randomState.surfaceSystem().topMaterial(this.surfaceRule, this, paramFunction, paramChunkAccess, this.noiseChunk, paramBlockPos, paramBoolean);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public RegistryAccess registryAccess() {
/* 46 */     return this.registryAccess;
/*    */   }
/*    */   
/*    */   public RandomState randomState() {
/* 50 */     return this.randomState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\CarvingContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */