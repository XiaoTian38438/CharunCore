/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class ServerDebugSubscribers
/*    */ {
/*    */   private final MinecraftServer server;
/* 17 */   private final Map<DebugSubscription<?>, List<ServerPlayer>> enabledSubscriptions = new HashMap<>();
/*    */   
/*    */   public ServerDebugSubscribers(MinecraftServer paramMinecraftServer) {
/* 20 */     this.server = paramMinecraftServer;
/*    */   }
/*    */   
/*    */   private List<ServerPlayer> getSubscribersFor(DebugSubscription<?> paramDebugSubscription) {
/* 24 */     return this.enabledSubscriptions.getOrDefault(paramDebugSubscription, List.of());
/*    */   }
/*    */   
/*    */   public void tick() {
/* 28 */     this.enabledSubscriptions.values().forEach(List::clear);
/* 29 */     for (ServerPlayer serverPlayer : this.server.getPlayerList().getPlayers()) {
/* 30 */       for (DebugSubscription<?> debugSubscription : (Iterable<DebugSubscription<?>>)serverPlayer.debugSubscriptions()) {
/* 31 */         ((List<ServerPlayer>)this.enabledSubscriptions.computeIfAbsent(debugSubscription, paramDebugSubscription -> new ArrayList())).add(serverPlayer);
/*    */       }
/*    */     } 
/* 34 */     this.enabledSubscriptions.values().removeIf(List::isEmpty);
/*    */   }
/*    */   
/*    */   public void broadcastToAll(DebugSubscription<?> paramDebugSubscription, Packet<?> paramPacket) {
/* 38 */     for (ServerPlayer serverPlayer : getSubscribersFor(paramDebugSubscription)) {
/* 39 */       serverPlayer.connection.send(paramPacket);
/*    */     }
/*    */   }
/*    */   
/*    */   public Set<DebugSubscription<?>> enabledSubscriptions() {
/* 44 */     return Set.copyOf(this.enabledSubscriptions.keySet());
/*    */   }
/*    */   
/*    */   public boolean hasAnySubscriberFor(DebugSubscription<?> paramDebugSubscription) {
/* 48 */     return !getSubscribersFor(paramDebugSubscription).isEmpty();
/*    */   }
/*    */   
/*    */   public boolean hasRequiredPermissions(ServerPlayer paramServerPlayer) {
/* 52 */     NameAndId nameAndId = paramServerPlayer.nameAndId();
/* 53 */     if (SharedConstants.IS_RUNNING_IN_IDE && this.server.isSingleplayerOwner(nameAndId)) {
/* 54 */       return true;
/*    */     }
/* 56 */     return this.server.getPlayerList().isOp(nameAndId);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\ServerDebugSubscribers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */