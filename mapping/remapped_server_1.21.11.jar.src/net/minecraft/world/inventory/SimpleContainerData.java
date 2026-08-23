/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public class SimpleContainerData implements ContainerData {
/*    */   private final int[] ints;
/*    */   
/*    */   public SimpleContainerData(int paramInt) {
/*  7 */     this.ints = new int[paramInt];
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int paramInt) {
/* 12 */     return this.ints[paramInt];
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int paramInt1, int paramInt2) {
/* 17 */     this.ints[paramInt1] = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCount() {
/* 22 */     return this.ints.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\SimpleContainerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */