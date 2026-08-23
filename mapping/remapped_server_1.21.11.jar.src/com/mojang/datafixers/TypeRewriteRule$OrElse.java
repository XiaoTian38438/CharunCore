/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OrElse
/*     */   implements TypeRewriteRule
/*     */ {
/*     */   protected final TypeRewriteRule first;
/*     */   protected final Supplier<TypeRewriteRule> second;
/*     */   private final int hashCode;
/*     */   
/*     */   public OrElse(TypeRewriteRule paramTypeRewriteRule, Supplier<TypeRewriteRule> paramSupplier) {
/* 121 */     this.first = paramTypeRewriteRule;
/* 122 */     this.second = paramSupplier;
/* 123 */     this.hashCode = Objects.hash(new Object[] { paramTypeRewriteRule, paramSupplier });
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType) {
/* 128 */     Optional<RewriteResult<A, ?>> optional = this.first.rewrite(paramType);
/* 129 */     if (optional.isPresent()) {
/* 130 */       return optional;
/*     */     }
/* 132 */     return ((TypeRewriteRule)this.second.get()).rewrite(paramType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 137 */     if (paramObject == this) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (!(paramObject instanceof OrElse)) {
/* 141 */       return false;
/*     */     }
/* 143 */     OrElse orElse = (OrElse)paramObject;
/* 144 */     return (Objects.equals(this.first, orElse.first) && Objects.equals(this.second, orElse.second));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypeRewriteRule$OrElse.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */