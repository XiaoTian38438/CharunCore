/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class CubeVoxelShape extends VoxelShape {
/*    */   protected CubeVoxelShape(DiscreteVoxelShape paramDiscreteVoxelShape) {
/*  9 */     super(paramDiscreteVoxelShape);
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis paramAxis) {
/* 14 */     return (DoubleList)new CubePointRange(this.shape.getSize(paramAxis));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int findIndex(Direction.Axis paramAxis, double paramDouble) {
/* 19 */     int i = this.shape.getSize(paramAxis);
/* 20 */     return Mth.floor(Mth.clamp(paramDouble * i, -1.0D, i));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\CubeVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */