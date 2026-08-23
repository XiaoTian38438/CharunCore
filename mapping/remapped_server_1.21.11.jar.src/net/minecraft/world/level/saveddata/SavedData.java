/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ public abstract class SavedData {
/*    */   private boolean dirty;
/*    */   
/*    */   public void setDirty() {
/*  7 */     setDirty(true);
/*    */   }
/*    */   
/*    */   public void setDirty(boolean paramBoolean) {
/* 11 */     this.dirty = paramBoolean;
/*    */   }
/*    */   
/*    */   public boolean isDirty() {
/* 15 */     return this.dirty;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\saveddata\SavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */