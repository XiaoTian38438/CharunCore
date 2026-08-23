/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class PlayerMap
/*    */ {
/*  9 */   private final Object2BooleanMap<ServerPlayer> players = (Object2BooleanMap<ServerPlayer>)new Object2BooleanOpenHashMap();
/*    */   
/*    */   public Set<ServerPlayer> getAllPlayers() {
/* 12 */     return (Set<ServerPlayer>)this.players.keySet();
/*    */   }
/*    */   
/*    */   public void addPlayer(ServerPlayer paramServerPlayer, boolean paramBoolean) {
/* 16 */     this.players.put(paramServerPlayer, paramBoolean);
/*    */   }
/*    */   
/*    */   public void removePlayer(ServerPlayer paramServerPlayer) {
/* 20 */     this.players.removeBoolean(paramServerPlayer);
/*    */   }
/*    */   
/*    */   public void ignorePlayer(ServerPlayer paramServerPlayer) {
/* 24 */     this.players.replace(paramServerPlayer, true);
/*    */   }
/*    */   
/*    */   public void unIgnorePlayer(ServerPlayer paramServerPlayer) {
/* 28 */     this.players.replace(paramServerPlayer, false);
/*    */   }
/*    */   
/*    */   public boolean ignoredOrUnknown(ServerPlayer paramServerPlayer) {
/* 32 */     return this.players.getOrDefault(paramServerPlayer, true);
/*    */   }
/*    */   
/*    */   public boolean ignored(ServerPlayer paramServerPlayer) {
/* 36 */     return this.players.getBoolean(paramServerPlayer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\PlayerMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */