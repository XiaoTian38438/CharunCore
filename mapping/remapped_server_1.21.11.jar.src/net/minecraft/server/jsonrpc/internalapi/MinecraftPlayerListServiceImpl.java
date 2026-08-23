/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class MinecraftPlayerListServiceImpl
/*    */   implements MinecraftPlayerListService
/*    */ {
/*    */   private final JsonRpcLogger jsonRpcLogger;
/*    */   private final DedicatedServer server;
/*    */   
/*    */   public MinecraftPlayerListServiceImpl(DedicatedServer paramDedicatedServer, JsonRpcLogger paramJsonRpcLogger) {
/* 20 */     this.jsonRpcLogger = paramJsonRpcLogger;
/* 21 */     this.server = paramDedicatedServer;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ServerPlayer> getPlayers() {
/* 26 */     return this.server.getPlayerList().getPlayers();
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerPlayer getPlayer(UUID paramUUID) {
/* 31 */     return this.server.getPlayerList().getPlayer(paramUUID);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<NameAndId> fetchUserByName(String paramString) {
/* 36 */     return this.server.services().nameToIdCache().get(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<NameAndId> fetchUserById(UUID paramUUID) {
/* 41 */     return Optional.<ProfileResult>ofNullable(this.server.services().sessionService().fetchProfile(paramUUID, true))
/* 42 */       .map(paramProfileResult -> new NameAndId(paramProfileResult.profile()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<NameAndId> getCachedUserById(UUID paramUUID) {
/* 47 */     return this.server.services().nameToIdCache().get(paramUUID);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<ServerPlayer> getPlayer(Optional<UUID> paramOptional, Optional<String> paramOptional1) {
/* 52 */     if (paramOptional.isPresent())
/* 53 */       return Optional.ofNullable(this.server.getPlayerList().getPlayer(paramOptional.get())); 
/* 54 */     if (paramOptional1.isPresent()) {
/* 55 */       return Optional.ofNullable(this.server.getPlayerList().getPlayerByName(paramOptional1.get()));
/*    */     }
/* 57 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ServerPlayer> getPlayersWithAddress(String paramString) {
/* 62 */     return this.server.getPlayerList().getPlayersWithAddress(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(ServerPlayer paramServerPlayer, ClientInfo paramClientInfo) {
/* 67 */     this.server.getPlayerList().remove(paramServerPlayer);
/* 68 */     this.jsonRpcLogger.log(paramClientInfo, "Remove player '{}'", new Object[] { paramServerPlayer.getPlainTextName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerPlayer getPlayerByName(String paramString) {
/* 73 */     return this.server.getPlayerList().getPlayerByName(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftPlayerListServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */