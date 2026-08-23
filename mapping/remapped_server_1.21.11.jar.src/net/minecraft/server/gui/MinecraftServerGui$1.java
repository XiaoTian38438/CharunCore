/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.awt.event.WindowAdapter;
/*    */ import java.awt.event.WindowEvent;
/*    */ import javax.swing.JFrame;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends WindowAdapter
/*    */ {
/*    */   public void windowClosing(WindowEvent paramWindowEvent) {
/* 62 */     if (!gui.isClosing.getAndSet(true)) {
/* 63 */       frame.setTitle("Minecraft server - shutting down!");
/* 64 */       server.halt(true);
/* 65 */       gui.runFinalizers();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\gui\MinecraftServerGui$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */