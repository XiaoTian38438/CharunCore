/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public class ThrowingComponent extends Exception {
/*    */   private final Component component;
/*    */   
/*    */   public ThrowingComponent(Component paramComponent) {
/*  7 */     super(paramComponent.getString());
/*  8 */     this.component = paramComponent;
/*    */   }
/*    */   
/*    */   public ThrowingComponent(Component paramComponent, Throwable paramThrowable) {
/* 12 */     super(paramComponent.getString(), paramThrowable);
/* 13 */     this.component = paramComponent;
/*    */   }
/*    */   
/*    */   public Component getComponent() {
/* 17 */     return this.component;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\ThrowingComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */