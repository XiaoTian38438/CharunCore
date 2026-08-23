/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ public class MinecraftBanListServiceImpl
/*    */   implements MinecraftBanListService
/*    */ {
/*    */   private final MinecraftServer server;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftBanListServiceImpl(MinecraftServer paramMinecraftServer, JsonRpcLogger paramJsonRpcLogger) {
/* 18 */     this.server = paramMinecraftServer;
/* 19 */     this.jsonrpcLogger = paramJsonRpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addUserBan(UserBanListEntry paramUserBanListEntry, ClientInfo paramClientInfo) {
/* 24 */     this.jsonrpcLogger.log(paramClientInfo, "Add player '{}' to banlist. Reason: '{}'", new Object[] { paramUserBanListEntry.getDisplayName(), paramUserBanListEntry.getReasonMessage().getString() });
/* 25 */     this.server.getPlayerList().getBans().add(paramUserBanListEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeUserBan(NameAndId paramNameAndId, ClientInfo paramClientInfo) {
/* 30 */     this.jsonrpcLogger.log(paramClientInfo, "Remove player '{}' from banlist", new Object[] { paramNameAndId });
/* 31 */     this.server.getPlayerList().getBans().remove(paramNameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearUserBans(ClientInfo paramClientInfo) {
/* 36 */     this.server.getPlayerList().getBans().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<UserBanListEntry> getUserBanEntries() {
/* 41 */     return this.server.getPlayerList().getBans().getEntries();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<IpBanListEntry> getIpBanEntries() {
/* 46 */     return this.server.getPlayerList().getIpBans().getEntries();
/*    */   }
/*    */ 
/*    */   
/*    */   public void addIpBan(IpBanListEntry paramIpBanListEntry, ClientInfo paramClientInfo) {
/* 51 */     this.jsonrpcLogger.log(paramClientInfo, "Add ip '{}' to ban list", new Object[] { paramIpBanListEntry.getUser() });
/* 52 */     this.server.getPlayerList().getIpBans().add(paramIpBanListEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearIpBans(ClientInfo paramClientInfo) {
/* 57 */     this.jsonrpcLogger.log(paramClientInfo, "Clear ip ban list", new Object[0]);
/* 58 */     this.server.getPlayerList().getIpBans().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeIpBan(String paramString, ClientInfo paramClientInfo) {
/* 63 */     this.jsonrpcLogger.log(paramClientInfo, "Remove ip '{}' from ban list", new Object[] { paramString });
/* 64 */     this.server.getPlayerList().getIpBans().remove(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftBanListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */