/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
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
/*     */ public final class ProductType<F, G>
/*     */   extends Type<Pair<F, G>>
/*     */ {
/*     */   protected final Type<F> first;
/*     */   protected final Type<G> second;
/*     */   private int hashCode;
/*     */   
/*     */   public ProductType(Type<F> paramType, Type<G> paramType1) {
/* 140 */     this.first = paramType;
/* 141 */     this.second = paramType1;
/*     */   }
/*     */   
/*     */   public Type<F> first() {
/* 145 */     return this.first;
/*     */   }
/*     */   
/*     */   public Type<G> second() {
/* 149 */     return this.second;
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<Pair<F, G>, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 154 */     return mergeViews(this.first.rewriteOrNop(paramTypeRewriteRule), this.second.rewriteOrNop(paramTypeRewriteRule));
/*     */   }
/*     */   
/*     */   public <F2, G2> RewriteResult<Pair<F, G>, ?> mergeViews(RewriteResult<F, F2> paramRewriteResult, RewriteResult<G, G2> paramRewriteResult1) {
/* 158 */     RewriteResult<Pair<F, G>, Pair<F2, G>> rewriteResult = fixLeft(this, this.first, this.second, paramRewriteResult);
/* 159 */     RewriteResult<Pair<?, G>, Pair<?, G2>> rewriteResult1 = fixRight(rewriteResult.view().newType(), paramRewriteResult.view().newType(), this.second, paramRewriteResult1);
/* 160 */     return rewriteResult1.compose(rewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<Pair<F, G>, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 165 */     return DataFixUtils.or(paramTypeRewriteRule
/* 166 */         .rewrite(this.first).map(paramRewriteResult -> fixLeft(this, this.first, this.second, paramRewriteResult)), () -> paramTypeRewriteRule.rewrite(this.second).map(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <F, G, F2> RewriteResult<Pair<F, G>, Pair<F2, G>> fixLeft(Type<Pair<F, G>> paramType, Type<F> paramType1, Type<G> paramType2, RewriteResult<F, F2> paramRewriteResult) {
/* 172 */     return opticView(paramType, paramRewriteResult, TypedOptic.proj1(paramType1, paramType2, paramRewriteResult.view().newType()));
/*     */   }
/*     */   
/*     */   private static <F, G, G2> RewriteResult<Pair<F, G>, Pair<F, G2>> fixRight(Type<Pair<F, G>> paramType, Type<F> paramType1, Type<G> paramType2, RewriteResult<G, G2> paramRewriteResult) {
/* 176 */     return opticView(paramType, paramRewriteResult, TypedOptic.proj2(paramType1, paramType2, paramRewriteResult.view().newType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 181 */     return DSL.and(this.first.updateMu(paramRecursiveTypeFamily), this.second.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 186 */     return DSL.and(this.first.template(), this.second.template());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 191 */     return DataFixUtils.or(this.first.findChoiceType(paramString, paramInt), () -> this.second.findChoiceType(paramString, paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 196 */     return DataFixUtils.or(this.first.findCheckedType(paramInt), () -> this.second.findCheckedType(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public Codec<Pair<F, G>> buildCodec() {
/* 201 */     return Codec.pair(this.first.codec(), this.second.codec());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 206 */     return "(" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 211 */     if (!(paramObject instanceof ProductType)) {
/* 212 */       return false;
/*     */     }
/* 214 */     ProductType productType = (ProductType)paramObject;
/* 215 */     return (this.first.equals(productType.first, paramBoolean1, paramBoolean2) && this.second.equals(productType.second, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 220 */     if (this.hashCode == 0) {
/* 221 */       int i = this.first.hashCode();
/* 222 */       i = 31 * i + this.second.hashCode();
/* 223 */       this.hashCode = i;
/*     */     } 
/* 225 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 230 */     return DataFixUtils.or(this.first.findFieldTypeOpt(paramString), () -> this.second.findFieldTypeOpt(paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Pair<F, G>> point(DynamicOps<?> paramDynamicOps) {
/* 235 */     return this.first.point(paramDynamicOps).flatMap(paramObject -> this.second.point(paramDynamicOps).map(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 240 */     Either either = this.first.findType(paramType, paramType1, paramTypeMatcher, paramBoolean);
/* 241 */     return (Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException>)either.map(this::capLeft, paramFieldNotFoundException -> {
/*     */           Either either = this.second.findType(paramType1, paramType2, paramTypeMatcher, paramBoolean);
/*     */           return either.mapLeft(this::capRight);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <FT, F2, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<F, F2, FT, FR> paramTypedOptic) {
/* 251 */     return Either.left(TypedOptic.proj1(paramTypedOptic.sType(), this.second, paramTypedOptic.tType()).compose(paramTypedOptic));
/*     */   }
/*     */   
/*     */   private <FT, G2, FR> TypedOptic<Pair<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> paramTypedOptic) {
/* 255 */     return TypedOptic.proj2(this.first, paramTypedOptic.sType(), paramTypedOptic.tType()).compose(paramTypedOptic);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Product$ProductType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */