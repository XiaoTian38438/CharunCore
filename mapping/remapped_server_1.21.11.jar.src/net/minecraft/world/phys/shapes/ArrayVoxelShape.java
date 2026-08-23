/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrayVoxelShape
/*    */   extends VoxelShape
/*    */ {
/*    */   private final DoubleList xs;
/*    */   private final DoubleList ys;
/*    */   private final DoubleList zs;
/*    */   
/*    */   protected ArrayVoxelShape(DiscreteVoxelShape paramDiscreteVoxelShape, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3) {
/* 19 */     this(paramDiscreteVoxelShape, 
/*    */         
/* 21 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(paramArrayOfdouble1, paramDiscreteVoxelShape.getXSize() + 1)), 
/* 22 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(paramArrayOfdouble2, paramDiscreteVoxelShape.getYSize() + 1)), 
/* 23 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(paramArrayOfdouble3, paramDiscreteVoxelShape.getZSize() + 1)));
/*    */   }
/*    */ 
/*    */   
/*    */   ArrayVoxelShape(DiscreteVoxelShape paramDiscreteVoxelShape, DoubleList paramDoubleList1, DoubleList paramDoubleList2, DoubleList paramDoubleList3) {
/* 28 */     super(paramDiscreteVoxelShape);
/* 29 */     int i = paramDiscreteVoxelShape.getXSize() + 1;
/* 30 */     int j = paramDiscreteVoxelShape.getYSize() + 1;
/* 31 */     int k = paramDiscreteVoxelShape.getZSize() + 1;
/* 32 */     if (i != paramDoubleList1.size() || j != paramDoubleList2.size() || k != paramDoubleList3.size()) {
/* 33 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Lengths of point arrays must be consistent with the size of the VoxelShape."));
/*    */     }
/* 35 */     this.xs = paramDoubleList1;
/* 36 */     this.ys = paramDoubleList2;
/* 37 */     this.zs = paramDoubleList3;
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis paramAxis) {
/* 42 */     switch (paramAxis) { default: throw new MatchException(null, null);case X: case Y: case Z: break; }  return 
/*    */ 
/*    */       
/* 45 */       this.zs;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\ArrayVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */