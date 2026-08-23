/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
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
/*    */ public enum Nop
/*    */   implements TypeRewriteRule, Supplier<TypeRewriteRule>
/*    */ {
/* 23 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType) {
/* 27 */     return Optional.of(RewriteResult.nop(paramType));
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule get() {
/* 32 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypeRewriteRule$Nop.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */