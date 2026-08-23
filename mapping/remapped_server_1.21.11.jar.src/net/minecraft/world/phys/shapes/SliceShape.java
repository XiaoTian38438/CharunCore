/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class SliceShape extends VoxelShape {
/*    */   private final VoxelShape delegate;
/*    */   private final Direction.Axis axis;
/*  9 */   private static final DoubleList SLICE_COORDS = (DoubleList)new CubePointRange(1);
/*    */   
/*    */   public SliceShape(VoxelShape paramVoxelShape, Direction.Axis paramAxis, int paramInt) {
/* 12 */     super(makeSlice(paramVoxelShape.shape, paramAxis, paramInt));
/* 13 */     this.delegate = paramVoxelShape;
/* 14 */     this.axis = paramAxis;
/*    */   }
/*    */   
/*    */   private static DiscreteVoxelShape makeSlice(DiscreteVoxelShape paramDiscreteVoxelShape, Direction.Axis paramAxis, int paramInt) {
/* 18 */     return new SubShape(paramDiscreteVoxelShape, paramAxis
/* 19 */         .choose(paramInt, 0, 0), paramAxis
/* 20 */         .choose(0, paramInt, 0), paramAxis
/* 21 */         .choose(0, 0, paramInt), paramAxis
/* 22 */         .choose(paramInt + 1, paramDiscreteVoxelShape.xSize, paramDiscreteVoxelShape.xSize), paramAxis
/* 23 */         .choose(paramDiscreteVoxelShape.ySize, paramInt + 1, paramDiscreteVoxelShape.ySize), paramAxis
/* 24 */         .choose(paramDiscreteVoxelShape.zSize, paramDiscreteVoxelShape.zSize, paramInt + 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis paramAxis) {
/* 30 */     if (paramAxis == this.axis) {
/* 31 */       return SLICE_COORDS;
/*    */     }
/* 33 */     return this.delegate.getCoords(paramAxis);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\SliceShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */