/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
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
/*     */ public final class SumType<F, G>
/*     */   extends Type<Either<F, G>>
/*     */ {
/*     */   protected final Type<F> first;
/*     */   protected final Type<G> second;
/*     */   private int hashCode;
/*     */   
/*     */   public SumType(Type<F> paramType, Type<G> paramType1) {
/* 114 */     this.first = paramType;
/* 115 */     this.second = paramType1;
/*     */   }
/*     */   
/*     */   public Type<F> first() {
/* 119 */     return this.first;
/*     */   }
/*     */   
/*     */   public Type<G> second() {
/* 123 */     return this.second;
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<Either<F, G>, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 128 */     return mergeViews(this.first.rewriteOrNop(paramTypeRewriteRule), this.second.rewriteOrNop(paramTypeRewriteRule));
/*     */   }
/*     */   
/*     */   public <F2, G2> RewriteResult<Either<F, G>, ?> mergeViews(RewriteResult<F, F2> paramRewriteResult, RewriteResult<G, G2> paramRewriteResult1) {
/* 132 */     RewriteResult<Either<F, G>, Either<F2, G>> rewriteResult = fixLeft(this, this.first, this.second, paramRewriteResult);
/* 133 */     RewriteResult<Either<?, G>, Either<?, G2>> rewriteResult1 = fixRight(rewriteResult.view().newType(), paramRewriteResult.view().newType(), this.second, paramRewriteResult1);
/* 134 */     return rewriteResult1.compose(rewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<Either<F, G>, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 139 */     return DataFixUtils.or(paramTypeRewriteRule
/* 140 */         .rewrite(this.first).map(paramRewriteResult -> fixLeft(this, this.first, this.second, paramRewriteResult)), () -> paramTypeRewriteRule.rewrite(this.second).map(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <F, G, F2> RewriteResult<Either<F, G>, Either<F2, G>> fixLeft(Type<Either<F, G>> paramType, Type<F> paramType1, Type<G> paramType2, RewriteResult<F, F2> paramRewriteResult) {
/* 146 */     return opticView(paramType, paramRewriteResult, TypedOptic.inj1(paramType1, paramType2, paramRewriteResult.view().newType()));
/*     */   }
/*     */   
/*     */   private static <F, G, G2> RewriteResult<Either<F, G>, Either<F, G2>> fixRight(Type<Either<F, G>> paramType, Type<F> paramType1, Type<G> paramType2, RewriteResult<G, G2> paramRewriteResult) {
/* 150 */     return opticView(paramType, paramRewriteResult, TypedOptic.inj2(paramType1, paramType2, paramRewriteResult.view().newType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 155 */     return DSL.or(this.first.updateMu(paramRecursiveTypeFamily), this.second.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 160 */     return DSL.or(this.first.template(), this.second.template());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 165 */     return DataFixUtils.or(this.first.findChoiceType(paramString, paramInt), () -> this.second.findChoiceType(paramString, paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 170 */     return DataFixUtils.or(this.first.findCheckedType(paramInt), () -> this.second.findCheckedType(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<Either<F, G>> buildCodec() {
/* 175 */     return Codec.either(this.first.codec(), this.second.codec());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 180 */     return "(" + String.valueOf(this.first) + " | " + String.valueOf(this.second) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 185 */     if (!(paramObject instanceof SumType)) {
/* 186 */       return false;
/*     */     }
/* 188 */     SumType sumType = (SumType)paramObject;
/* 189 */     return (this.first.equals(sumType.first, paramBoolean1, paramBoolean2) && this.second.equals(sumType.second, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 194 */     if (this.hashCode == 0) {
/* 195 */       int i = this.first.hashCode();
/* 196 */       i = 31 * i + this.second.hashCode();
/* 197 */       this.hashCode = i;
/*     */     } 
/* 199 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 204 */     return DataFixUtils.or(this.first.findFieldTypeOpt(paramString), () -> this.second.findFieldTypeOpt(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Either<F, G>> point(DynamicOps<?> paramDynamicOps) {
/* 210 */     return DataFixUtils.or(this.second.point(paramDynamicOps).map(Either::right), () -> this.first.point(paramDynamicOps).map(Either::left));
/*     */   }
/*     */   
/*     */   private static <A, B, LS, RS, LT, RT> TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B> mergeOptics(final TypedOptic<LS, LT, A, B> lo, final TypedOptic<RS, RT, A, B> ro) {
/* 214 */     final TypeToken bound = TraversalP.Mu.TYPE_TOKEN;
/*     */     
/* 216 */     return new TypedOptic(typeToken, 
/*     */         
/* 218 */         DSL.or(lo.sType(), ro.sType()), 
/* 219 */         DSL.or(lo.tType(), ro.tType()), lo
/* 220 */         .aType(), lo
/* 221 */         .bType(), (Optic)new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>()
/*     */         {
/*     */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(Applicative<F, ?> param2Applicative, FunctionType<A, App<F, B>> param2FunctionType)
/*     */           {
/* 225 */             return param2Either -> (App)param2Either.map((), ());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<Either<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 242 */     Either either1 = this.first.findType(paramType, paramType1, paramTypeMatcher, paramBoolean);
/* 243 */     Either either2 = this.second.findType(paramType, paramType1, paramTypeMatcher, paramBoolean);
/* 244 */     if (either1.left().isPresent() && either2.left().isPresent()) {
/* 245 */       return Either.left(mergeOptics(either1.left().get(), either2.left().get()));
/*     */     }
/* 247 */     if (either1.left().isPresent()) {
/* 248 */       return either1.mapLeft(this::capLeft);
/*     */     }
/* 250 */     return either2.mapLeft(this::capRight);
/*     */   }
/*     */   
/*     */   private <FT, FR, F2> TypedOptic<Either<F, G>, ?, FT, FR> capLeft(TypedOptic<F, F2, FT, FR> paramTypedOptic) {
/* 254 */     return TypedOptic.inj1(paramTypedOptic.sType(), this.second, paramTypedOptic.tType()).compose(paramTypedOptic);
/*     */   }
/*     */   
/*     */   private <FT, FR, G2> TypedOptic<Either<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> paramTypedOptic) {
/* 258 */     return TypedOptic.inj2(this.first, paramTypedOptic.sType(), paramTypedOptic.tType()).compose(paramTypedOptic);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Sum$SumType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */