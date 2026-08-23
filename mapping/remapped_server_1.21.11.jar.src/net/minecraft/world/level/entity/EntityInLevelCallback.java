/*   */ package net.minecraft.world.level.entity;
/*   */ 
/*   */ import net.minecraft.world.entity.Entity;
/*   */ 
/*   */ public interface EntityInLevelCallback {
/* 6 */   public static final EntityInLevelCallback NULL = new EntityInLevelCallback() {
/*   */       public void onMove() {}
/*   */       
/*   */       public void onRemove(Entity.RemovalReason param1RemovalReason) {}
/*   */     };
/*   */   
/*   */   void onMove();
/*   */   
/*   */   void onRemove(Entity.RemovalReason paramRemovalReason);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityInLevelCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */