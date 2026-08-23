/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class MinecraftServerStateServiceImpl
/*    */   implements MinecraftServerStateService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftServerStateServiceImpl(DedicatedServer paramDedicatedServer, JsonRpcLogger paramJsonRpcLogger) {
/* 19 */     this.server = paramDedicatedServer;
/* 20 */     this.jsonrpcLogger = paramJsonRpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReady() {
/* 25 */     return this.server.isReady();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean saveEverything(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, ClientInfo paramClientInfo) {
/* 30 */     this.jsonrpcLogger.log(paramClientInfo, "Save everything. SuppressLogs: {}, flush: {}, force: {}", new Object[] { Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2), Boolean.valueOf(paramBoolean3) });
/* 31 */     return this.server.saveEverything(paramBoolean1, paramBoolean2, paramBoolean3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void halt(boolean paramBoolean, ClientInfo paramClientInfo) {
/* 36 */     this.jsonrpcLogger.log(paramClientInfo, "Halt server. WaitForShutdown: {}", new Object[] { Boolean.valueOf(paramBoolean) });
/* 37 */     this.server.halt(paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendSystemMessage(Component paramComponent, ClientInfo paramClientInfo) {
/* 42 */     this.jsonrpcLogger.log(paramClientInfo, "Send system message: '{}'", new Object[] { paramComponent.getString() });
/* 43 */     this.server.sendSystemMessage(paramComponent);
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendSystemMessage(Component paramComponent, boolean paramBoolean, Collection<ServerPlayer> paramCollection, ClientInfo paramClientInfo) {
/* 48 */     List list = paramCollection.stream().map(Player::getPlainTextName).toList();
/* 49 */     this.jsonrpcLogger.log(paramClientInfo, "Send system message to '{}' players (overlay: {}): '{}'", new Object[] { Integer.valueOf(list.size()), Boolean.valueOf(paramBoolean), paramComponent.getString() });
/*    */     
/* 51 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 52 */       if (paramBoolean) {
/* 53 */         serverPlayer.sendSystemMessage(paramComponent, true); continue;
/*    */       } 
/* 55 */       serverPlayer.sendSystemMessage(paramComponent);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void broadcastSystemMessage(Component paramComponent, boolean paramBoolean, ClientInfo paramClientInfo) {
/* 62 */     this.jsonrpcLogger.log(paramClientInfo, "Broadcast system message (overlay: {}): '{}'", new Object[] { Boolean.valueOf(paramBoolean), paramComponent.getString() });
/*    */     
/* 64 */     for (ServerPlayer serverPlayer : this.server.getPlayerList().getPlayers()) {
/* 65 */       if (paramBoolean) {
/* 66 */         serverPlayer.sendSystemMessage(paramComponent, true); continue;
/*    */       } 
/* 68 */       serverPlayer.sendSystemMessage(paramComponent);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerStateServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */