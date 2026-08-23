/*     */ package net.minecraft.world.level.gameevent;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.debug.DebugGameEventListenerInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EuclideanGameEventListenerRegistry implements GameEventListenerRegistry {
/*  19 */   private final List<GameEventListener> listeners = Lists.newArrayList();
/*  20 */   private final Set<GameEventListener> listenersToRemove = Sets.newHashSet();
/*  21 */   private final List<GameEventListener> listenersToAdd = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   private boolean processing;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final int sectionY;
/*     */   
/*     */   private final OnEmptyAction onEmptyAction;
/*     */ 
/*     */   
/*     */   public EuclideanGameEventListenerRegistry(ServerLevel paramServerLevel, int paramInt, OnEmptyAction paramOnEmptyAction) {
/*  34 */     this.level = paramServerLevel;
/*  35 */     this.sectionY = paramInt;
/*  36 */     this.onEmptyAction = paramOnEmptyAction;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  41 */     return this.listeners.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(GameEventListener paramGameEventListener) {
/*  46 */     if (this.processing) {
/*  47 */       this.listenersToAdd.add(paramGameEventListener);
/*     */     } else {
/*  49 */       this.listeners.add(paramGameEventListener);
/*     */     } 
/*  51 */     sendDebugInfo(this.level, paramGameEventListener);
/*     */   }
/*     */   
/*     */   private static void sendDebugInfo(ServerLevel paramServerLevel, GameEventListener paramGameEventListener) {
/*  55 */     if (!paramServerLevel.debugSynchronizers().hasAnySubscriberFor(DebugSubscriptions.GAME_EVENT_LISTENERS)) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     DebugGameEventListenerInfo debugGameEventListenerInfo = new DebugGameEventListenerInfo(paramGameEventListener.getListenerRadius());
/*  60 */     PositionSource positionSource = paramGameEventListener.getListenerSource();
/*  61 */     if (positionSource instanceof BlockPositionSource) { BlockPositionSource blockPositionSource = (BlockPositionSource)positionSource;
/*  62 */       paramServerLevel.debugSynchronizers().sendBlockValue(blockPositionSource.pos(), DebugSubscriptions.GAME_EVENT_LISTENERS, debugGameEventListenerInfo); }
/*  63 */     else if (positionSource instanceof EntityPositionSource) { EntityPositionSource entityPositionSource = (EntityPositionSource)positionSource;
/*  64 */       Entity entity = paramServerLevel.getEntity(entityPositionSource.getUuid());
/*  65 */       if (entity != null) {
/*  66 */         paramServerLevel.debugSynchronizers().sendEntityValue(entity, DebugSubscriptions.GAME_EVENT_LISTENERS, debugGameEventListenerInfo);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregister(GameEventListener paramGameEventListener) {
/*  73 */     if (this.processing) {
/*  74 */       this.listenersToRemove.add(paramGameEventListener);
/*     */     } else {
/*  76 */       this.listeners.remove(paramGameEventListener);
/*     */     } 
/*     */     
/*  79 */     if (this.listeners.isEmpty()) {
/*  80 */       this.onEmptyAction.apply(this.sectionY);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visitInRangeListeners(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext, GameEventListenerRegistry.ListenerVisitor paramListenerVisitor) {
/*  86 */     this.processing = true;
/*  87 */     boolean bool = false;
/*     */     try {
/*  89 */       for (Iterator<GameEventListener> iterator = this.listeners.iterator(); iterator.hasNext(); ) {
/*  90 */         GameEventListener gameEventListener = iterator.next();
/*  91 */         if (this.listenersToRemove.remove(gameEventListener)) {
/*  92 */           iterator.remove();
/*     */           
/*     */           continue;
/*     */         } 
/*  96 */         Optional<Vec3> optional = getPostableListenerPosition(this.level, paramVec3, gameEventListener);
/*  97 */         if (optional.isPresent()) {
/*  98 */           paramListenerVisitor.visit(gameEventListener, optional.get());
/*  99 */           bool = true;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 103 */       this.processing = false;
/*     */     } 
/*     */     
/* 106 */     if (!this.listenersToAdd.isEmpty()) {
/* 107 */       this.listeners.addAll(this.listenersToAdd);
/* 108 */       this.listenersToAdd.clear();
/*     */     } 
/*     */     
/* 111 */     if (!this.listenersToRemove.isEmpty()) {
/* 112 */       this.listeners.removeAll(this.listenersToRemove);
/* 113 */       this.listenersToRemove.clear();
/*     */     } 
/* 115 */     return bool;
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> getPostableListenerPosition(ServerLevel paramServerLevel, Vec3 paramVec3, GameEventListener paramGameEventListener) {
/* 119 */     Optional<Vec3> optional = paramGameEventListener.getListenerSource().getPosition((Level)paramServerLevel);
/*     */     
/* 121 */     if (optional.isEmpty()) {
/* 122 */       return Optional.empty();
/*     */     }
/*     */     
/* 125 */     double d = BlockPos.containing((Position)optional.get()).distSqr((Vec3i)BlockPos.containing((Position)paramVec3));
/* 126 */     int i = paramGameEventListener.getListenerRadius() * paramGameEventListener.getListenerRadius();
/*     */     
/* 128 */     if (d > i) {
/* 129 */       return Optional.empty();
/*     */     }
/* 131 */     return optional;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OnEmptyAction {
/*     */     void apply(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\EuclideanGameEventListenerRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */