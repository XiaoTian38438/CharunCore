/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.Func;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.Optional;
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
/*    */ public enum BangEta
/*    */   implements PointFreeRule
/*    */ {
/* 62 */   INSTANCE;
/*    */ 
/*    */ 
/*    */   
/*    */   public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> paramPointFree) {
/* 67 */     if (paramPointFree instanceof Bang) {
/* 68 */       return Optional.empty();
/*    */     }
/* 70 */     Type<A> type = paramPointFree.type(); if (type instanceof Func) { Func func = (Func)type;
/* 71 */       if (func.second() instanceof com.mojang.datafixers.types.constant.EmptyPart) {
/* 72 */         return Optional.of((PointFree)Functions.bang(func.first()));
/*    */       } }
/*    */     
/* 75 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$BangEta.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */