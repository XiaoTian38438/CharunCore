/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.debug.DebugGameEventInfo;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GameEventDispatcher
/*    */ {
/*    */   public GameEventDispatcher(ServerLevel paramServerLevel) {
/* 19 */     this.level = paramServerLevel;
/*    */   }
/*    */   private final ServerLevel level;
/*    */   public void post(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext) {
/* 23 */     int i = ((GameEvent)paramHolder.value()).notificationRadius();
/* 24 */     BlockPos blockPos = BlockPos.containing((Position)paramVec3);
/* 25 */     int j = SectionPos.blockToSectionCoord(blockPos.getX() - i);
/* 26 */     int k = SectionPos.blockToSectionCoord(blockPos.getY() - i);
/* 27 */     int m = SectionPos.blockToSectionCoord(blockPos.getZ() - i);
/* 28 */     int n = SectionPos.blockToSectionCoord(blockPos.getX() + i);
/* 29 */     int i1 = SectionPos.blockToSectionCoord(blockPos.getY() + i);
/* 30 */     int i2 = SectionPos.blockToSectionCoord(blockPos.getZ() + i);
/*    */     
/* 32 */     ArrayList<GameEvent.ListenerInfo> arrayList = new ArrayList();
/*    */     
/* 34 */     GameEventListenerRegistry.ListenerVisitor listenerVisitor = (paramGameEventListener, paramVec32) -> {
/*    */         if (paramGameEventListener.getDeliveryMode() == GameEventListener.DeliveryMode.BY_DISTANCE) {
/*    */           paramList.add(new GameEvent.ListenerInfo(paramHolder, paramVec31, paramContext, paramGameEventListener, paramVec32));
/*    */         } else {
/*    */           paramGameEventListener.handleGameEvent(this.level, paramHolder, paramContext, paramVec31);
/*    */         } 
/*    */       };
/*    */     
/* 42 */     boolean bool = false;
/* 43 */     for (int i3 = j; i3 <= n; i3++) {
/* 44 */       for (int i4 = m; i4 <= i2; i4++) {
/* 45 */         LevelChunk levelChunk = this.level.getChunkSource().getChunkNow(i3, i4);
/*    */         
/* 47 */         if (levelChunk != null) {
/* 48 */           for (int i5 = k; i5 <= i1; i5++) {
/* 49 */             bool |= levelChunk.getListenerRegistry(i5).visitInRangeListeners(paramHolder, paramVec3, paramContext, listenerVisitor);
/*    */           }
/*    */         }
/*    */       } 
/*    */     } 
/* 54 */     if (!arrayList.isEmpty()) {
/* 55 */       handleGameEventMessagesInQueue(arrayList);
/*    */     }
/* 57 */     if (bool) {
/* 58 */       this.level.debugSynchronizers().broadcastEventToTracking(BlockPos.containing((Position)paramVec3), DebugSubscriptions.GAME_EVENTS, new DebugGameEventInfo(paramHolder, paramVec3));
/*    */     }
/*    */   }
/*    */   
/*    */   private void handleGameEventMessagesInQueue(List<GameEvent.ListenerInfo> paramList) {
/* 63 */     Collections.sort(paramList);
/* 64 */     for (GameEvent.ListenerInfo listenerInfo : paramList) {
/* 65 */       GameEventListener gameEventListener = listenerInfo.recipient();
/* 66 */       gameEventListener.handleGameEvent(this.level, listenerInfo.gameEvent(), listenerInfo.context(), listenerInfo.source());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\GameEventDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */