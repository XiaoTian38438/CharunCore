/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
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
/*     */ public class Everywhere
/*     */   implements TypeRewriteRule
/*     */ {
/*     */   protected final TypeRewriteRule rule;
/*     */   protected final PointFreeRule optimizationRule;
/*     */   protected final boolean recurse;
/*     */   private final boolean checkIndex;
/*     */   private final int hashCode;
/*     */   
/*     */   public Everywhere(TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 241 */     this.rule = paramTypeRewriteRule;
/* 242 */     this.optimizationRule = paramPointFreeRule;
/* 243 */     this.recurse = paramBoolean1;
/* 244 */     this.checkIndex = paramBoolean2;
/* 245 */     this.hashCode = Objects.hash(new Object[] { paramTypeRewriteRule, paramPointFreeRule, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2) });
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType) {
/* 250 */     return paramType.everywhere(this.rule, this.optimizationRule, this.recurse, this.checkIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 255 */     if (paramObject == this) {
/* 256 */       return true;
/*     */     }
/* 258 */     if (!(paramObject instanceof Everywhere)) {
/* 259 */       return false;
/*     */     }
/* 261 */     Everywhere everywhere = (Everywhere)paramObject;
/* 262 */     return (Objects.equals(this.rule, everywhere.rule) && Objects.equals(this.optimizationRule, everywhere.optimizationRule) && this.recurse == everywhere.recurse && this.checkIndex == everywhere.checkIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 267 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypeRewriteRule$Everywhere.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */