/*    */ package net.minecraft.world.level.block.entity;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface BeaconBeamOwner {
/*    */   List<Section> getBeamSections();
/*    */   
/*    */   public static class Section {
/*    */     private final int color;
/*    */     
/*    */     public Section(int param1Int) {
/* 11 */       this.color = param1Int;
/* 12 */       this.height = 1;
/*    */     }
/*    */     private int height;
/*    */     public void increaseHeight() {
/* 16 */       this.height++;
/*    */     }
/*    */     
/*    */     public int getColor() {
/* 20 */       return this.color;
/*    */     }
/*    */     
/*    */     public int getHeight() {
/* 24 */       return this.height;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BeaconBeamOwner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */