/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ class null
/*    */   implements GameEventListenerRegistry
/*    */ {
/*    */   public boolean isEmpty() {
/* 10 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void register(GameEventListener paramGameEventListener) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void unregister(GameEventListener paramGameEventListener) {}
/*    */ 
/*    */   
/*    */   public boolean visitInRangeListeners(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext, GameEventListenerRegistry.ListenerVisitor paramListenerVisitor) {
/* 23 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */