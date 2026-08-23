/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ public final class Sum extends Record implements TypeTemplate {
/*     */   private final TypeTemplate f;
/*     */   private final TypeTemplate g;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Sum;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */   }
/*     */   
/*  30 */   public Sum(TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) { this.f = paramTypeTemplate1; this.g = paramTypeTemplate2; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Sum;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  30 */     //   #30	-> 0 } public TypeTemplate f() { return this.f; } public TypeTemplate g() { return this.g; }
/*     */   
/*     */   public int size() {
/*  33 */     return Math.max(this.f.size(), this.g.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  38 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  41 */           return DSL.or(Sum.this.f.apply(family).apply(param1Int), Sum.this.g.apply(family).apply(param1Int));
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  67 */     return TypeFamily.familyOptic(paramInt -> cap(this.f.applyO(paramFamilyOptic, paramType1, paramType2), this.g.applyO(paramFamilyOptic, paramType1, paramType2), paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <A, B, LS, RS, LT, RT> TypedOptic<?, ?, A, B> cap(FamilyOptic<A, B> paramFamilyOptic1, FamilyOptic<A, B> paramFamilyOptic2, int paramInt) {
/*  78 */     return SumType.mergeOptics(paramFamilyOptic1.apply(paramInt), paramFamilyOptic2.apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  83 */     Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.findFieldOrType(paramInt, paramString, paramType, paramType1);
/*  84 */     return (Either<TypeTemplate, Type.FieldNotFoundException>)either.map(paramTypeTemplate -> Either.left(new Sum(paramTypeTemplate, this.g)), paramFieldNotFoundException -> this.g.findFieldOrType(paramInt, paramString, paramType1, paramType2).mapLeft(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  92 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult1 = this.f.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         RewriteResult<?, ?> rewriteResult2 = this.g.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(apply(paramTypeFamily).apply(paramInt), rewriteResult1, rewriteResult2);
/*     */       };
/*     */   }
/*     */   
/*     */   private <L, R> RewriteResult<?, ?> cap(Type<?> paramType, RewriteResult<L, ?> paramRewriteResult, RewriteResult<R, ?> paramRewriteResult1) {
/* 100 */     return ((SumType)paramType).mergeViews(paramRewriteResult, paramRewriteResult1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "(" + String.valueOf(this.f) + " | " + String.valueOf(this.g) + ")";
/*     */   }
/*     */   
/*     */   public static final class SumType<F, G> extends Type<Either<F, G>> {
/*     */     protected final Type<F> first;
/*     */     protected final Type<G> second;
/*     */     private int hashCode;
/*     */     
/*     */     public SumType(Type<F> param1Type, Type<G> param1Type1) {
/* 114 */       this.first = param1Type;
/* 115 */       this.second = param1Type1;
/*     */     }
/*     */     
/*     */     public Type<F> first() {
/* 119 */       return this.first;
/*     */     }
/*     */     
/*     */     public Type<G> second() {
/* 123 */       return this.second;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Either<F, G>, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 128 */       return mergeViews(this.first.rewriteOrNop(param1TypeRewriteRule), this.second.rewriteOrNop(param1TypeRewriteRule));
/*     */     }
/*     */     
/*     */     public <F2, G2> RewriteResult<Either<F, G>, ?> mergeViews(RewriteResult<F, F2> param1RewriteResult, RewriteResult<G, G2> param1RewriteResult1) {
/* 132 */       RewriteResult<Either<F, G>, Either<F2, G>> rewriteResult = fixLeft(this, this.first, this.second, param1RewriteResult);
/* 133 */       RewriteResult<Either<?, G>, Either<?, G2>> rewriteResult1 = fixRight(rewriteResult.view().newType(), param1RewriteResult.view().newType(), this.second, param1RewriteResult1);
/* 134 */       return rewriteResult1.compose(rewriteResult);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Either<F, G>, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 139 */       return DataFixUtils.or(param1TypeRewriteRule
/* 140 */           .rewrite(this.first).map(param1RewriteResult -> fixLeft(this, this.first, this.second, param1RewriteResult)), () -> param1TypeRewriteRule.rewrite(this.second).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <F, G, F2> RewriteResult<Either<F, G>, Either<F2, G>> fixLeft(Type<Either<F, G>> param1Type, Type<F> param1Type1, Type<G> param1Type2, RewriteResult<F, F2> param1RewriteResult) {
/* 146 */       return opticView(param1Type, param1RewriteResult, TypedOptic.inj1(param1Type1, param1Type2, param1RewriteResult.view().newType()));
/*     */     }
/*     */     
/*     */     private static <F, G, G2> RewriteResult<Either<F, G>, Either<F, G2>> fixRight(Type<Either<F, G>> param1Type, Type<F> param1Type1, Type<G> param1Type2, RewriteResult<G, G2> param1RewriteResult) {
/* 150 */       return opticView(param1Type, param1RewriteResult, TypedOptic.inj2(param1Type1, param1Type2, param1RewriteResult.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 155 */       return DSL.or(this.first.updateMu(param1RecursiveTypeFamily), this.second.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 160 */       return DSL.or(this.first.template(), this.second.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 165 */       return DataFixUtils.or(this.first.findChoiceType(param1String, param1Int), () -> this.second.findChoiceType(param1String, param1Int));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 170 */       return DataFixUtils.or(this.first.findCheckedType(param1Int), () -> this.second.findCheckedType(param1Int));
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<Either<F, G>> buildCodec() {
/* 175 */       return Codec.either(this.first.codec(), this.second.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 180 */       return "(" + String.valueOf(this.first) + " | " + String.valueOf(this.second) + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 185 */       if (!(param1Object instanceof SumType)) {
/* 186 */         return false;
/*     */       }
/* 188 */       SumType sumType = (SumType)param1Object;
/* 189 */       return (this.first.equals(sumType.first, param1Boolean1, param1Boolean2) && this.second.equals(sumType.second, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 194 */       if (this.hashCode == 0) {
/* 195 */         int i = this.first.hashCode();
/* 196 */         i = 31 * i + this.second.hashCode();
/* 197 */         this.hashCode = i;
/*     */       } 
/* 199 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 204 */       return DataFixUtils.or(this.first.findFieldTypeOpt(param1String), () -> this.second.findFieldTypeOpt(param1String));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<Either<F, G>> point(DynamicOps<?> param1DynamicOps) {
/* 210 */       return DataFixUtils.or(this.second.point(param1DynamicOps).map(Either::right), () -> this.first.point(param1DynamicOps).map(Either::left));
/*     */     }
/*     */     
/*     */     private static <A, B, LS, RS, LT, RT> TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B> mergeOptics(final TypedOptic<LS, LT, A, B> lo, final TypedOptic<RS, RT, A, B> ro) {
/* 214 */       final TypeToken bound = TraversalP.Mu.TYPE_TOKEN;
/*     */       
/* 216 */       return new TypedOptic(typeToken, 
/*     */           
/* 218 */           DSL.or(lo.sType(), ro.sType()), 
/* 219 */           DSL.or(lo.tType(), ro.tType()), lo
/* 220 */           .aType(), lo
/* 221 */           .bType(), (Optic)new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>()
/*     */           {
/*     */             public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(Applicative<F, ?> param2Applicative, FunctionType<A, App<F, B>> param2FunctionType)
/*     */             {
/* 225 */               return param2Either -> (App)param2Either.map((), ());
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Either<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 242 */       Either either1 = this.first.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean);
/* 243 */       Either either2 = this.second.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean);
/* 244 */       if (either1.left().isPresent() && either2.left().isPresent()) {
/* 245 */         return Either.left(mergeOptics(either1.left().get(), either2.left().get()));
/*     */       }
/* 247 */       if (either1.left().isPresent()) {
/* 248 */         return either1.mapLeft(this::capLeft);
/*     */       }
/* 250 */       return either2.mapLeft(this::capRight);
/*     */     }
/*     */     
/*     */     private <FT, FR, F2> TypedOptic<Either<F, G>, ?, FT, FR> capLeft(TypedOptic<F, F2, FT, FR> param1TypedOptic) {
/* 254 */       return TypedOptic.inj1(param1TypedOptic.sType(), this.second, param1TypedOptic.tType()).compose(param1TypedOptic);
/*     */     }
/*     */     
/*     */     private <FT, FR, G2> TypedOptic<Either<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> param1TypedOptic) {
/* 258 */       return TypedOptic.inj2(this.first, param1TypedOptic.sType(), param1TypedOptic.tType()).compose(param1TypedOptic);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Traversal<Either<LS, RS>, Either<LT, RT>, A, B> {
/*     */     public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(Applicative<F, ?> param1Applicative, FunctionType<A, App<F, B>> param1FunctionType) {
/*     */       return param1Either -> (App)param1Either.map((), ());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Sum.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */