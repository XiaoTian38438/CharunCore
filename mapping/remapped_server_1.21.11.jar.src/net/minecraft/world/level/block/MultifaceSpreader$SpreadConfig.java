/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SpreadConfig
/*     */ {
/*     */   BlockState getStateForPlacement(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*     */   
/*     */   boolean canSpreadInto(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos);
/*     */   
/*     */   default MultifaceSpreader.SpreadType[] getSpreadTypes() {
/* 111 */     return MultifaceSpreader.DEFAULT_SPREAD_ORDER;
/*     */   }
/*     */   
/*     */   default boolean hasFace(BlockState paramBlockState, Direction paramDirection) {
/* 115 */     return MultifaceBlock.hasFace(paramBlockState, paramDirection);
/*     */   }
/*     */   
/*     */   default boolean isOtherBlockValidAsSource(BlockState paramBlockState) {
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   default boolean canSpreadFrom(BlockState paramBlockState, Direction paramDirection) {
/* 123 */     return (isOtherBlockValidAsSource(paramBlockState) || hasFace(paramBlockState, paramDirection));
/*     */   }
/*     */   
/*     */   default boolean placeBlock(LevelAccessor paramLevelAccessor, MultifaceSpreader.SpreadPos paramSpreadPos, BlockState paramBlockState, boolean paramBoolean) {
/* 127 */     BlockState blockState = getStateForPlacement(paramBlockState, (BlockGetter)paramLevelAccessor, paramSpreadPos.pos(), paramSpreadPos.face());
/* 128 */     if (blockState != null) {
/*     */       
/* 130 */       if (paramBoolean) {
/* 131 */         paramLevelAccessor.getChunk(paramSpreadPos.pos()).markPosForPostprocessing(paramSpreadPos.pos());
/*     */       }
/* 133 */       return paramLevelAccessor.setBlock(paramSpreadPos.pos(), blockState, 2);
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */