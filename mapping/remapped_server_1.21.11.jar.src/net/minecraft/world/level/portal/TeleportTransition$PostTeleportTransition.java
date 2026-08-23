/*    */ package net.minecraft.world.level.portal;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PostTeleportTransition
/*    */ {
/*    */   void onTransition(Entity paramEntity);
/*    */   
/*    */   default PostTeleportTransition then(PostTeleportTransition paramPostTeleportTransition) {
/* 22 */     return paramEntity -> {
/*    */         onTransition(paramEntity);
/*    */         paramPostTeleportTransition.onTransition(paramEntity);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\portal\TeleportTransition$PostTeleportTransition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */