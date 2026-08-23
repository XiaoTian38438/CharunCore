/*    */ package net.minecraft.server.gui;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import javax.swing.JList;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class PlayerListComponent extends JList<String> {
/*    */   private final MinecraftServer server;
/*    */   private int tickCount;
/*    */   
/*    */   public PlayerListComponent(MinecraftServer paramMinecraftServer) {
/* 13 */     this.server = paramMinecraftServer;
/* 14 */     paramMinecraftServer.addTickable(this::tick);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.tickCount++ % 20 == 0) {
/* 19 */       Vector<String> vector = new Vector();
/* 20 */       for (byte b = 0; b < this.server.getPlayerList().getPlayers().size(); b++) {
/* 21 */         vector.add(((ServerPlayer)this.server.getPlayerList().getPlayers().get(b)).getGameProfile().name());
/*    */       }
/* 23 */       setListData(vector);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\gui\PlayerListComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */