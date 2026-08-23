/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.CarvingMask;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.ProtoChunk;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class PlacementContext extends WorldGenerationContext {
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator generator;
/*    */   private final Optional<PlacedFeature> topFeature;
/*    */   
/*    */   public PlacementContext(WorldGenLevel paramWorldGenLevel, ChunkGenerator paramChunkGenerator, Optional<PlacedFeature> paramOptional) {
/* 21 */     super(paramChunkGenerator, (LevelHeightAccessor)paramWorldGenLevel);
/* 22 */     this.level = paramWorldGenLevel;
/* 23 */     this.generator = paramChunkGenerator;
/* 24 */     this.topFeature = paramOptional;
/*    */   }
/*    */   
/*    */   public int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2) {
/* 28 */     return this.level.getHeight(paramTypes, paramInt1, paramInt2);
/*    */   }
/*    */   
/*    */   public CarvingMask getCarvingMask(ChunkPos paramChunkPos) {
/* 32 */     return ((ProtoChunk)this.level.getChunk(paramChunkPos.x, paramChunkPos.z)).getOrCreateCarvingMask();
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos paramBlockPos) {
/* 36 */     return this.level.getBlockState(paramBlockPos);
/*    */   }
/*    */   
/*    */   public int getMinY() {
/* 40 */     return this.level.getMinY();
/*    */   }
/*    */   
/*    */   public WorldGenLevel getLevel() {
/* 44 */     return this.level;
/*    */   }
/*    */   
/*    */   public Optional<PlacedFeature> topFeature() {
/* 48 */     return this.topFeature;
/*    */   }
/*    */   
/*    */   public ChunkGenerator generator() {
/* 52 */     return this.generator;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\PlacementContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */