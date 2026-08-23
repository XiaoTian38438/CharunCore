/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.BaseRailBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ 
/*    */ public class MinecartCollisionContext extends EntityCollisionContext {
/*    */   private BlockPos ingoreBelow;
/*    */   private BlockPos slopeIgnore;
/*    */   
/*    */   protected MinecartCollisionContext(AbstractMinecart paramAbstractMinecart, boolean paramBoolean) {
/* 16 */     super((Entity)paramAbstractMinecart, paramBoolean, false);
/* 17 */     setupContext(paramAbstractMinecart);
/*    */   }
/*    */   
/*    */   private void setupContext(AbstractMinecart paramAbstractMinecart) {
/* 21 */     BlockPos blockPos = paramAbstractMinecart.getCurrentBlockPosOrRailBelow();
/* 22 */     BlockState blockState = paramAbstractMinecart.level().getBlockState(blockPos);
/* 23 */     boolean bool = BaseRailBlock.isRail(blockState);
/* 24 */     if (bool) {
/* 25 */       this.ingoreBelow = blockPos.below();
/* 26 */       RailShape railShape = (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty());
/* 27 */       if (railShape.isSlope()) {
/* 28 */         switch (railShape) { case ASCENDING_EAST: case ASCENDING_WEST: case ASCENDING_NORTH: case ASCENDING_SOUTH: default: break; }  this
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 33 */           .slopeIgnore = null;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public VoxelShape getCollisionShape(BlockState paramBlockState, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos) {
/* 41 */     if (paramBlockPos.equals(this.ingoreBelow) || paramBlockPos.equals(this.slopeIgnore)) {
/* 42 */       return Shapes.empty();
/*    */     }
/* 44 */     return super.getCollisionShape(paramBlockState, paramCollisionGetter, paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\MinecartCollisionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */