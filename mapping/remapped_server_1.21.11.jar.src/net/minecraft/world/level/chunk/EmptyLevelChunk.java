/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.FullChunkStatus;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class EmptyLevelChunk
/*    */   extends LevelChunk {
/*    */   private final Holder<Biome> biome;
/*    */   
/*    */   public EmptyLevelChunk(Level paramLevel, ChunkPos paramChunkPos, Holder<Biome> paramHolder) {
/* 21 */     super(paramLevel, paramChunkPos);
/* 22 */     this.biome = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos paramBlockPos) {
/* 27 */     return Blocks.VOID_AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState setBlockState(BlockPos paramBlockPos, BlockState paramBlockState, @UpdateFlags int paramInt) {
/* 32 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos paramBlockPos) {
/* 37 */     return Fluids.EMPTY.defaultFluidState();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightEmission(BlockPos paramBlockPos) {
/* 42 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos paramBlockPos, LevelChunk.EntityCreationType paramEntityCreationType) {
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addAndRegisterBlockEntity(BlockEntity paramBlockEntity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBlockEntity(BlockEntity paramBlockEntity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeBlockEntity(BlockPos paramBlockPos) {}
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isYSpaceEmpty(int paramInt1, int paramInt2) {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public FullChunkStatus getFullStatus() {
/* 74 */     return FullChunkStatus.FULL;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int paramInt1, int paramInt2, int paramInt3) {
/* 79 */     return this.biome;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\EmptyLevelChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */