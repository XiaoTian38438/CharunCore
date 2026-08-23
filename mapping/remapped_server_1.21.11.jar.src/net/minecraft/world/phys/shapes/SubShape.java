/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class SubShape extends DiscreteVoxelShape {
/*    */   private final DiscreteVoxelShape parent;
/*    */   private final int startX;
/*    */   private final int startY;
/*    */   private final int startZ;
/*    */   private final int endX;
/*    */   private final int endY;
/*    */   private final int endZ;
/*    */   
/*    */   protected SubShape(DiscreteVoxelShape paramDiscreteVoxelShape, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 16 */     super(paramInt4 - paramInt1, paramInt5 - paramInt2, paramInt6 - paramInt3);
/* 17 */     this.parent = paramDiscreteVoxelShape;
/* 18 */     this.startX = paramInt1;
/* 19 */     this.startY = paramInt2;
/* 20 */     this.startZ = paramInt3;
/* 21 */     this.endX = paramInt4;
/* 22 */     this.endY = paramInt5;
/* 23 */     this.endZ = paramInt6;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull(int paramInt1, int paramInt2, int paramInt3) {
/* 28 */     return this.parent.isFull(this.startX + paramInt1, this.startY + paramInt2, this.startZ + paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fill(int paramInt1, int paramInt2, int paramInt3) {
/* 33 */     this.parent.fill(this.startX + paramInt1, this.startY + paramInt2, this.startZ + paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   public int firstFull(Direction.Axis paramAxis) {
/* 38 */     return clampToShape(paramAxis, this.parent.firstFull(paramAxis));
/*    */   }
/*    */ 
/*    */   
/*    */   public int lastFull(Direction.Axis paramAxis) {
/* 43 */     return clampToShape(paramAxis, this.parent.lastFull(paramAxis));
/*    */   }
/*    */   
/*    */   private int clampToShape(Direction.Axis paramAxis, int paramInt) {
/* 47 */     int i = paramAxis.choose(this.startX, this.startY, this.startZ);
/* 48 */     int j = paramAxis.choose(this.endX, this.endY, this.endZ);
/* 49 */     return Mth.clamp(paramInt, i, j) - i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\SubShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */