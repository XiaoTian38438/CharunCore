/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Seq
/*     */   implements TypeRewriteRule
/*     */ {
/*     */   protected final List<TypeRewriteRule> rules;
/*     */   private final int hashCode;
/*     */   
/*     */   public Seq(List<TypeRewriteRule> paramList) {
/*  68 */     this.rules = (List<TypeRewriteRule>)ImmutableList.copyOf(paramList);
/*  69 */     this.hashCode = this.rules.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType) {
/*  74 */     RewriteResult<A, A> rewriteResult = RewriteResult.nop(paramType);
/*  75 */     for (TypeRewriteRule typeRewriteRule : this.rules) {
/*  76 */       Optional<RewriteResult<A, ?>> optional = cap1(typeRewriteRule, rewriteResult);
/*  77 */       if (!optional.isPresent()) {
/*  78 */         return Optional.empty();
/*     */       }
/*  80 */       rewriteResult = (RewriteResult<A, A>)optional.get();
/*     */     } 
/*  82 */     return Optional.of(rewriteResult);
/*     */   }
/*     */   
/*     */   protected <A, B> Optional<RewriteResult<A, ?>> cap1(TypeRewriteRule paramTypeRewriteRule, RewriteResult<A, B> paramRewriteResult) {
/*  86 */     return paramTypeRewriteRule.rewrite(paramRewriteResult.view().newType()).map(paramRewriteResult2 -> paramRewriteResult2.compose(paramRewriteResult1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  91 */     if (paramObject == this) {
/*  92 */       return true;
/*     */     }
/*  94 */     if (!(paramObject instanceof Seq)) {
/*  95 */       return false;
/*     */     }
/*  97 */     Seq seq = (Seq)paramObject;
/*  98 */     return Objects.equals(this.rules, seq.rules);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypeRewriteRule$Seq.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */