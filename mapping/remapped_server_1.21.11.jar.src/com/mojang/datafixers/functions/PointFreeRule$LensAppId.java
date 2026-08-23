/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.Func;
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
/*    */ public enum LensAppId
/*    */   implements PointFreeRule
/*    */ {
/* 80 */   INSTANCE;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> paramPointFree) {
/* 86 */     if (paramPointFree instanceof Apply) { Apply apply = (Apply)paramPointFree;
/* 87 */       PointFree pointFree = apply.func;
/* 88 */       if (pointFree instanceof ProfunctorTransformer && Functions.isId(apply.arg)) {
/* 89 */         return Optional.of((PointFree)Functions.id(((Func)apply.type()).first()));
/*    */       } }
/*    */     
/* 92 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\PointFreeRule$LensAppId.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */