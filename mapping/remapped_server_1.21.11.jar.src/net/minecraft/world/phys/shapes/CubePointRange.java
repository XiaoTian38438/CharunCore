/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ 
/*    */ public class CubePointRange extends AbstractDoubleList {
/*    */   private final int parts;
/*    */   
/*    */   public CubePointRange(int paramInt) {
/*  9 */     if (paramInt <= 0) {
/* 10 */       throw new IllegalArgumentException("Need at least 1 part");
/*    */     }
/* 12 */     this.parts = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int paramInt) {
/* 17 */     return paramInt / this.parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 22 */     return this.parts + 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\CubePointRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */