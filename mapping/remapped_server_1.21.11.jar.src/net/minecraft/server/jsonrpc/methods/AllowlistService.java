/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.StoredUserEntry;
/*    */ import net.minecraft.server.players.UserWhiteListEntry;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AllowlistService
/*    */ {
/*    */   public static List<PlayerDto> get(MinecraftApi paramMinecraftApi) {
/* 18 */     return paramMinecraftApi.allowListService().getEntries().stream()
/* 19 */       .filter(paramUserWhiteListEntry -> (paramUserWhiteListEntry.getUser() != null))
/* 20 */       .map(paramUserWhiteListEntry -> PlayerDto.from((NameAndId)paramUserWhiteListEntry.getUser()))
/* 21 */       .toList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<PlayerDto> add(MinecraftApi paramMinecraftApi, List<PlayerDto> paramList, ClientInfo paramClientInfo) {
/* 27 */     List list = paramList.stream().map(paramPlayerDto -> paramMinecraftApi.playerListService().getUser(paramPlayerDto.id(), paramPlayerDto.name())).toList();
/*    */     
/* 29 */     for (Optional optional : Util.sequence(list).join()) {
/* 30 */       optional.ifPresent(paramNameAndId -> paramMinecraftApi.allowListService().add(new UserWhiteListEntry(paramNameAndId), paramClientInfo));
/*    */     }
/*    */     
/* 33 */     return get(paramMinecraftApi);
/*    */   }
/*    */   
/*    */   public static List<PlayerDto> clear(MinecraftApi paramMinecraftApi, ClientInfo paramClientInfo) {
/* 37 */     paramMinecraftApi.allowListService().clear(paramClientInfo);
/* 38 */     return get(paramMinecraftApi);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<PlayerDto> remove(MinecraftApi paramMinecraftApi, List<PlayerDto> paramList, ClientInfo paramClientInfo) {
/* 44 */     List list = paramList.stream().map(paramPlayerDto -> paramMinecraftApi.playerListService().getUser(paramPlayerDto.id(), paramPlayerDto.name())).toList();
/*    */     
/* 46 */     for (Optional optional : Util.sequence(list).join()) {
/* 47 */       optional.ifPresent(paramNameAndId -> paramMinecraftApi.allowListService().remove(paramNameAndId, paramClientInfo));
/*    */     }
/* 49 */     paramMinecraftApi.allowListService().kickUnlistedPlayers(paramClientInfo);
/* 50 */     return get(paramMinecraftApi);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<PlayerDto> set(MinecraftApi paramMinecraftApi, List<PlayerDto> paramList, ClientInfo paramClientInfo) {
/* 56 */     List list = paramList.stream().map(paramPlayerDto -> paramMinecraftApi.playerListService().getUser(paramPlayerDto.id(), paramPlayerDto.name())).toList();
/*    */ 
/*    */ 
/*    */     
/* 60 */     Set set1 = (Set)((List)Util.sequence(list).join()).stream().flatMap(Optional::stream).collect(Collectors.toSet());
/*    */ 
/*    */ 
/*    */     
/* 64 */     Set set2 = (Set)paramMinecraftApi.allowListService().getEntries().stream().map(StoredUserEntry::getUser).collect(Collectors.toSet());
/*    */     
/* 66 */     set2.stream()
/* 67 */       .filter(paramNameAndId -> !paramSet.contains(paramNameAndId))
/* 68 */       .forEach(paramNameAndId -> paramMinecraftApi.allowListService().remove(paramNameAndId, paramClientInfo));
/*    */     
/* 70 */     set1.stream()
/* 71 */       .filter(paramNameAndId -> !paramSet.contains(paramNameAndId))
/* 72 */       .forEach(paramNameAndId -> paramMinecraftApi.allowListService().add(new UserWhiteListEntry(paramNameAndId), paramClientInfo));
/*    */     
/* 74 */     paramMinecraftApi.allowListService().kickUnlistedPlayers(paramClientInfo);
/* 75 */     return get(paramMinecraftApi);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\methods\AllowlistService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */