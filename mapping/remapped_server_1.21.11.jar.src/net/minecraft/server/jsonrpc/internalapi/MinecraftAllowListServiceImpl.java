/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserWhiteListEntry;
/*    */ 
/*    */ public class MinecraftAllowListServiceImpl
/*    */   implements MinecraftAllowListService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftAllowListServiceImpl(DedicatedServer paramDedicatedServer, JsonRpcLogger paramJsonRpcLogger) {
/* 17 */     this.server = paramDedicatedServer;
/* 18 */     this.jsonrpcLogger = paramJsonRpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<UserWhiteListEntry> getEntries() {
/* 23 */     return this.server.getPlayerList().getWhiteList().getEntries();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(UserWhiteListEntry paramUserWhiteListEntry, ClientInfo paramClientInfo) {
/* 28 */     this.jsonrpcLogger.log(paramClientInfo, "Add player '{}' to allowlist", new Object[] { paramUserWhiteListEntry.getUser() });
/* 29 */     return this.server.getPlayerList().getWhiteList().add(paramUserWhiteListEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear(ClientInfo paramClientInfo) {
/* 34 */     this.jsonrpcLogger.log(paramClientInfo, "Clear allowlist", new Object[0]);
/* 35 */     this.server.getPlayerList().getWhiteList().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(NameAndId paramNameAndId, ClientInfo paramClientInfo) {
/* 40 */     this.jsonrpcLogger.log(paramClientInfo, "Remove player '{}' from allowlist", new Object[] { paramNameAndId });
/* 41 */     this.server.getPlayerList().getWhiteList().remove(paramNameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void kickUnlistedPlayers(ClientInfo paramClientInfo) {
/* 46 */     this.jsonrpcLogger.log(paramClientInfo, "Kick unlisted players", new Object[0]);
/* 47 */     this.server.kickUnlistedPlayers();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftAllowListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */