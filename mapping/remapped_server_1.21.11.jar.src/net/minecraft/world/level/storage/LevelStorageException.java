/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class LevelStorageException extends RuntimeException {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public LevelStorageException(Component paramComponent) {
/*  9 */     super(paramComponent.getString());
/* 10 */     this.messageComponent = paramComponent;
/*    */   }
/*    */   
/*    */   public Component getMessageComponent() {
/* 14 */     return this.messageComponent;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\LevelStorageException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */