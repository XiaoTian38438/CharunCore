/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSpreaderConfig
/*     */   implements MultifaceSpreader.SpreadConfig
/*     */ {
/*     */   protected MultifaceBlock block;
/*     */   
/*     */   public DefaultSpreaderConfig(MultifaceBlock paramMultifaceBlock) {
/* 143 */     this.block = paramMultifaceBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 148 */     return this.block.getStateForPlacement(paramBlockState, paramBlockGetter, paramBlockPos, paramDirection);
/*     */   }
/*     */   
/*     */   protected boolean stateCanBeReplaced(BlockGetter paramBlockGetter, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Direction paramDirection, BlockState paramBlockState) {
/* 152 */     return (paramBlockState.isAir() || paramBlockState.is(this.block) || (paramBlockState.is(Blocks.WATER) && paramBlockState.getFluidState().isSource()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSpreadInto(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos) {
/* 157 */     BlockState blockState = paramBlockGetter.getBlockState(paramSpreadPos.pos());
/* 158 */     return (stateCanBeReplaced(paramBlockGetter, paramBlockPos, paramSpreadPos.pos(), paramSpreadPos.face(), blockState) && this.block.isValidStateForPlacement(paramBlockGetter, blockState, paramSpreadPos.pos(), paramSpreadPos.face()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MultifaceSpreader$DefaultSpreaderConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */