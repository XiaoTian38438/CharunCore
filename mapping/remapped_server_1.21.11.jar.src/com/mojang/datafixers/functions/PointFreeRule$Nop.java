/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Nop
/*    */   implements PointFreeRule, Supplier<PointFreeRule>
/*    */ {
/* 48 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public <A> Optional<PointFree<A>> rewrite(PointFree<A> paramPointFree) {
/* 52 */     return Optional.of(paramPointFree);
/*    */   }
/*    */ 
/*    */   
/*    */   public PointFreeRule get() {
/* 57 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$Nop.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */