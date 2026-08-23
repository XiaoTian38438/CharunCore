/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface MinecraftPlayerListService
/*    */ {
/*    */   default CompletableFuture<Optional<NameAndId>> getUser(Optional<UUID> paramOptional, Optional<String> paramOptional1) {
/* 21 */     if (paramOptional.isPresent()) {
/* 22 */       Optional<NameAndId> optional = getCachedUserById(paramOptional.get());
/* 23 */       if (optional.isPresent()) {
/* 24 */         return CompletableFuture.completedFuture(optional);
/*    */       }
/* 26 */       return CompletableFuture.supplyAsync(() -> fetchUserById(paramOptional.get()), (Executor)Util.nonCriticalIoPool());
/* 27 */     }  if (paramOptional1.isPresent()) {
/* 28 */       return CompletableFuture.supplyAsync(() -> fetchUserByName(paramOptional.get()), (Executor)Util.nonCriticalIoPool());
/*    */     }
/* 30 */     return CompletableFuture.completedFuture(Optional.empty());
/*    */   }
/*    */   
/*    */   List<ServerPlayer> getPlayers();
/*    */   
/*    */   ServerPlayer getPlayer(UUID paramUUID);
/*    */   
/*    */   Optional<NameAndId> fetchUserByName(String paramString);
/*    */   
/*    */   Optional<NameAndId> fetchUserById(UUID paramUUID);
/*    */   
/*    */   Optional<NameAndId> getCachedUserById(UUID paramUUID);
/*    */   
/*    */   Optional<ServerPlayer> getPlayer(Optional<UUID> paramOptional, Optional<String> paramOptional1);
/*    */   
/*    */   List<ServerPlayer> getPlayersWithAddress(String paramString);
/*    */   
/*    */   ServerPlayer getPlayerByName(String paramString);
/*    */   
/*    */   void remove(ServerPlayer paramServerPlayer, ClientInfo paramClientInfo);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftPlayerListService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */