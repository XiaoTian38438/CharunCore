/*     */ package com.mojang.datafixers;
/*     */ 
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
/*     */ public class All
/*     */   implements TypeRewriteRule
/*     */ {
/*     */   private final TypeRewriteRule rule;
/*     */   private final boolean recurse;
/*     */   private final boolean checkIndex;
/*     */   private final int hashCode;
/*     */   
/*     */   public All(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 186 */     this.rule = paramTypeRewriteRule;
/* 187 */     this.recurse = paramBoolean1;
/* 188 */     this.checkIndex = paramBoolean2;
/* 189 */     this.hashCode = Objects.hash(new Object[] { paramTypeRewriteRule, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2) });
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType) {
/* 194 */     return Optional.of(paramType.all(this.rule, this.recurse, this.checkIndex));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 199 */     if (paramObject == this) {
/* 200 */       return true;
/*     */     }
/* 202 */     if (!(paramObject instanceof All)) {
/* 203 */       return false;
/*     */     }
/* 205 */     All all = (All)paramObject;
/* 206 */     return (Objects.equals(this.rule, all.rule) && this.recurse == all.recurse && this.checkIndex == all.checkIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 211 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypeRewriteRule$All.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */