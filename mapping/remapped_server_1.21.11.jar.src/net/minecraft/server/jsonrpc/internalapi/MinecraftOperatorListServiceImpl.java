/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.ServerOpListEntry;
/*    */ 
/*    */ public class MinecraftOperatorListServiceImpl
/*    */   implements MinecraftOperatorListService
/*    */ {
/*    */   private final MinecraftServer minecraftServer;
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftOperatorListServiceImpl(MinecraftServer paramMinecraftServer, JsonRpcLogger paramJsonRpcLogger) {
/* 20 */     this.minecraftServer = paramMinecraftServer;
/* 21 */     this.jsonrpcLogger = paramJsonRpcLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<ServerOpListEntry> getEntries() {
/* 26 */     return this.minecraftServer.getPlayerList().getOps().getEntries();
/*    */   }
/*    */ 
/*    */   
/*    */   public void op(NameAndId paramNameAndId, Optional<PermissionLevel> paramOptional, Optional<Boolean> paramOptional1, ClientInfo paramClientInfo) {
/* 31 */     this.jsonrpcLogger.log(paramClientInfo, "Op '{}'", new Object[] { paramNameAndId });
/* 32 */     this.minecraftServer.getPlayerList().op(paramNameAndId, paramOptional.map(LevelBasedPermissionSet::forLevel), paramOptional1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void op(NameAndId paramNameAndId, ClientInfo paramClientInfo) {
/* 37 */     this.jsonrpcLogger.log(paramClientInfo, "Op '{}'", new Object[] { paramNameAndId });
/* 38 */     this.minecraftServer.getPlayerList().op(paramNameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void deop(NameAndId paramNameAndId, ClientInfo paramClientInfo) {
/* 43 */     this.jsonrpcLogger.log(paramClientInfo, "Deop '{}'", new Object[] { paramNameAndId });
/* 44 */     this.minecraftServer.getPlayerList().deop(paramNameAndId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear(ClientInfo paramClientInfo) {
/* 49 */     this.jsonrpcLogger.log(paramClientInfo, "Clear operator list", new Object[0]);
/* 50 */     this.minecraftServer.getPlayerList().getOps().clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftOperatorListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */