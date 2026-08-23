/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Section
/*    */ {
/*    */   private final int color;
/*    */   private int height;
/*    */   
/*    */   public Section(int paramInt) {
/* 11 */     this.color = paramInt;
/* 12 */     this.height = 1;
/*    */   }
/*    */   
/*    */   public void increaseHeight() {
/* 16 */     this.height++;
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 20 */     return this.color;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 24 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BeaconBeamOwner$Section.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */