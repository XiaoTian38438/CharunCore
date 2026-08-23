/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface GameEventListenerRegistry {
/*  7 */   public static final GameEventListenerRegistry NOOP = new GameEventListenerRegistry()
/*    */     {
/*    */       public boolean isEmpty() {
/* 10 */         return true;
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void register(GameEventListener param1GameEventListener) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public void unregister(GameEventListener param1GameEventListener) {}
/*    */ 
/*    */       
/*    */       public boolean visitInRangeListeners(Holder<GameEvent> param1Holder, Vec3 param1Vec3, GameEvent.Context param1Context, GameEventListenerRegistry.ListenerVisitor param1ListenerVisitor) {
/* 23 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   void register(GameEventListener paramGameEventListener);
/*    */   
/*    */   void unregister(GameEventListener paramGameEventListener);
/*    */   
/*    */   boolean visitInRangeListeners(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext, ListenerVisitor paramListenerVisitor);
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ListenerVisitor {
/*    */     void visit(GameEventListener param1GameEventListener, Vec3 param1Vec3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */