/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
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
/*     */ final class MultiNeighborUpdate
/*     */   implements CollectingNeighborUpdater.NeighborUpdates
/*     */ {
/*     */   private final BlockPos sourcePos;
/*     */   private final Block sourceBlock;
/*     */   private Orientation orientation;
/*     */   private final Direction skipDirection;
/* 140 */   private int idx = 0;
/*     */   
/*     */   MultiNeighborUpdate(BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, Direction paramDirection) {
/* 143 */     this.sourcePos = paramBlockPos;
/* 144 */     this.sourceBlock = paramBlock;
/* 145 */     this.orientation = paramOrientation;
/* 146 */     this.skipDirection = paramDirection;
/* 147 */     if (NeighborUpdater.UPDATE_ORDER[this.idx] == paramDirection) {
/* 148 */       this.idx++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean runNext(Level paramLevel) {
/* 154 */     Direction direction = NeighborUpdater.UPDATE_ORDER[this.idx++];
/* 155 */     BlockPos blockPos = this.sourcePos.relative(direction);
/* 156 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/* 157 */     Orientation orientation = null;
/* 158 */     if (paramLevel.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
/* 159 */       if (this.orientation == null) {
/* 160 */         this.orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, (this.skipDirection == null) ? null : this.skipDirection.getOpposite(), null);
/*     */       }
/* 162 */       orientation = this.orientation.withFront(direction);
/*     */     } 
/* 164 */     NeighborUpdater.executeUpdate(paramLevel, blockState, blockPos, this.sourceBlock, orientation, false);
/* 165 */     if (this.idx < NeighborUpdater.UPDATE_ORDER.length && NeighborUpdater.UPDATE_ORDER[this.idx] == this.skipDirection) {
/* 166 */       this.idx++;
/*     */     }
/* 168 */     return (this.idx < NeighborUpdater.UPDATE_ORDER.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachUpdatedPos(Consumer<BlockPos> paramConsumer) {
/* 173 */     for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
/* 174 */       if (direction != this.skipDirection) {
/*     */ 
/*     */         
/* 177 */         BlockPos blockPos = this.sourcePos.relative(direction);
/* 178 */         paramConsumer.accept(blockPos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$MultiNeighborUpdate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */