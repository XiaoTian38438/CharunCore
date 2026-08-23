/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
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
/*     */ public final class ListType<A>
/*     */   extends Type<List<A>>
/*     */ {
/*     */   protected final Type<A> element;
/*     */   
/*     */   public ListType(Type<A> paramType) {
/*  92 */     this.element = paramType;
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<List<A>, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/*  97 */     RewriteResult<A, ?> rewriteResult = this.element.rewriteOrNop(paramTypeRewriteRule);
/*  98 */     return fix(rewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<List<A>, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 103 */     return paramTypeRewriteRule.rewrite(this.element).map(this::fix);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 108 */     return DSL.list(this.element.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 113 */     return DSL.list(this.element.template());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<List<A>> point(DynamicOps<?> paramDynamicOps) {
/* 118 */     return (Optional)Optional.of(ImmutableList.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<List<A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 123 */     Either either = this.element.findType(paramType, paramType1, paramTypeMatcher, paramBoolean);
/* 124 */     return either.mapLeft(this::capLeft);
/*     */   }
/*     */   
/*     */   private <FT, FR, B> TypedOptic<List<A>, ?, FT, FR> capLeft(TypedOptic<A, B, FT, FR> paramTypedOptic) {
/* 128 */     return TypedOptic.list(paramTypedOptic.sType(), paramTypedOptic.tType()).compose(paramTypedOptic);
/*     */   }
/*     */   
/*     */   public <B> RewriteResult<List<A>, ?> fix(RewriteResult<A, B> paramRewriteResult) {
/* 132 */     return opticView(this, paramRewriteResult, TypedOptic.list(this.element, paramRewriteResult.view().newType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Codec<List<A>> buildCodec() {
/* 137 */     return Codec.list(this.element.codec());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     return "List[" + String.valueOf(this.element) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 147 */     return (paramObject instanceof ListType && this.element.equals(((ListType)paramObject).element, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 152 */     return this.element.hashCode();
/*     */   }
/*     */   
/*     */   public Type<A> getElement() {
/* 156 */     return this.element;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\List$ListType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */