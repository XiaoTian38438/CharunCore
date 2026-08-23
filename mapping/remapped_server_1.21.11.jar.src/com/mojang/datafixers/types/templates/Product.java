/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class Product extends Record implements TypeTemplate {
/*     */   private final TypeTemplate f;
/*     */   private final TypeTemplate g;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Product;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */   }
/*     */   
/*  33 */   public Product(TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) { this.f = paramTypeTemplate1; this.g = paramTypeTemplate2; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Product;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  33 */     //   #33	-> 0 } public TypeTemplate f() { return this.f; } public TypeTemplate g() { return this.g; }
/*     */   
/*     */   public int size() {
/*  36 */     return Math.max(this.f.size(), this.g.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  41 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  44 */           return DSL.and(Product.this.f.apply(family).apply(param1Int), Product.this.g.apply(family).apply(param1Int));
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  68 */     return TypeFamily.familyOptic(paramInt -> cap(this.f.applyO(paramFamilyOptic, paramType1, paramType2), this.g.applyO(paramFamilyOptic, paramType1, paramType2), paramInt));
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
/*  79 */     TypeToken typeToken = TraversalP.Mu.TYPE_TOKEN;
/*     */     
/*  81 */     TypedOptic typedOptic1 = paramFamilyOptic1.apply(paramInt);
/*  82 */     TypedOptic typedOptic2 = paramFamilyOptic2.apply(paramInt);
/*     */     
/*  84 */     Optic optic1 = (Optic)typedOptic1.upCast(typeToken).orElseThrow(IllegalArgumentException::new);
/*  85 */     Optic optic2 = (Optic)typedOptic2.upCast(typeToken).orElseThrow(IllegalArgumentException::new);
/*     */     
/*  87 */     final Traversal lt = Optics.toTraversal(optic1);
/*  88 */     final Traversal rt = Optics.toTraversal(optic2);
/*     */     
/*  90 */     return new TypedOptic(
/*  91 */         (Set)ImmutableSet.of(typeToken), 
/*  92 */         DSL.and(typedOptic1.sType(), typedOptic2.sType()), 
/*  93 */         DSL.and(typedOptic1.tType(), typedOptic2.tType()), typedOptic1
/*  94 */         .aType(), typedOptic1.bType(), (Optic)new Traversal<Pair<LS, RS>, Pair<LT, RT>, A, B>()
/*     */         {
/*     */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Pair<LS, RS>, App<F, Pair<LT, RT>>> wander(Applicative<F, ?> param1Applicative, FunctionType<A, App<F, B>> param1FunctionType)
/*     */           {
/*  98 */             return param1Pair -> param1Applicative.ap2(param1Applicative.point(Pair::of), (App)param1Traversal1.wander(param1Applicative, param1FunctionType).apply(param1Pair.getFirst()), (App)param1Traversal2.wander(param1Applicative, param1FunctionType).apply(param1Pair.getSecond()));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/* 109 */     Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.findFieldOrType(paramInt, paramString, paramType, paramType1);
/* 110 */     return (Either<TypeTemplate, Type.FieldNotFoundException>)either.map(paramTypeTemplate -> Either.left(new Product(paramTypeTemplate, this.g)), paramFieldNotFoundException -> this.g.findFieldOrType(paramInt, paramString, paramType1, paramType2).mapLeft(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/* 118 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult1 = this.f.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         RewriteResult<?, ?> rewriteResult2 = this.g.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(apply(paramTypeFamily).apply(paramInt), rewriteResult1, rewriteResult2);
/*     */       };
/*     */   }
/*     */   
/*     */   private <L, R> RewriteResult<?, ?> cap(Type<?> paramType, RewriteResult<L, ?> paramRewriteResult, RewriteResult<R, ?> paramRewriteResult1) {
/* 126 */     return ((ProductType)paramType).mergeViews(paramRewriteResult, paramRewriteResult1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return "(" + String.valueOf(this.f) + ", " + String.valueOf(this.g) + ")";
/*     */   }
/*     */   
/*     */   public static final class ProductType<F, G> extends Type<Pair<F, G>> {
/*     */     protected final Type<F> first;
/*     */     protected final Type<G> second;
/*     */     private int hashCode;
/*     */     
/*     */     public ProductType(Type<F> param1Type, Type<G> param1Type1) {
/* 140 */       this.first = param1Type;
/* 141 */       this.second = param1Type1;
/*     */     }
/*     */     
/*     */     public Type<F> first() {
/* 145 */       return this.first;
/*     */     }
/*     */     
/*     */     public Type<G> second() {
/* 149 */       return this.second;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Pair<F, G>, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 154 */       return mergeViews(this.first.rewriteOrNop(param1TypeRewriteRule), this.second.rewriteOrNop(param1TypeRewriteRule));
/*     */     }
/*     */     
/*     */     public <F2, G2> RewriteResult<Pair<F, G>, ?> mergeViews(RewriteResult<F, F2> param1RewriteResult, RewriteResult<G, G2> param1RewriteResult1) {
/* 158 */       RewriteResult<Pair<F, G>, Pair<F2, G>> rewriteResult = fixLeft(this, this.first, this.second, param1RewriteResult);
/* 159 */       RewriteResult<Pair<?, G>, Pair<?, G2>> rewriteResult1 = fixRight(rewriteResult.view().newType(), param1RewriteResult.view().newType(), this.second, param1RewriteResult1);
/* 160 */       return rewriteResult1.compose(rewriteResult);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Pair<F, G>, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 165 */       return DataFixUtils.or(param1TypeRewriteRule
/* 166 */           .rewrite(this.first).map(param1RewriteResult -> fixLeft(this, this.first, this.second, param1RewriteResult)), () -> param1TypeRewriteRule.rewrite(this.second).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <F, G, F2> RewriteResult<Pair<F, G>, Pair<F2, G>> fixLeft(Type<Pair<F, G>> param1Type, Type<F> param1Type1, Type<G> param1Type2, RewriteResult<F, F2> param1RewriteResult) {
/* 172 */       return opticView(param1Type, param1RewriteResult, TypedOptic.proj1(param1Type1, param1Type2, param1RewriteResult.view().newType()));
/*     */     }
/*     */     
/*     */     private static <F, G, G2> RewriteResult<Pair<F, G>, Pair<F, G2>> fixRight(Type<Pair<F, G>> param1Type, Type<F> param1Type1, Type<G> param1Type2, RewriteResult<G, G2> param1RewriteResult) {
/* 176 */       return opticView(param1Type, param1RewriteResult, TypedOptic.proj2(param1Type1, param1Type2, param1RewriteResult.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 181 */       return DSL.and(this.first.updateMu(param1RecursiveTypeFamily), this.second.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 186 */       return DSL.and(this.first.template(), this.second.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 191 */       return DataFixUtils.or(this.first.findChoiceType(param1String, param1Int), () -> this.second.findChoiceType(param1String, param1Int));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 196 */       return DataFixUtils.or(this.first.findCheckedType(param1Int), () -> this.second.findCheckedType(param1Int));
/*     */     }
/*     */ 
/*     */     
/*     */     public Codec<Pair<F, G>> buildCodec() {
/* 201 */       return Codec.pair(this.first.codec(), this.second.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 206 */       return "(" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 211 */       if (!(param1Object instanceof ProductType)) {
/* 212 */         return false;
/*     */       }
/* 214 */       ProductType productType = (ProductType)param1Object;
/* 215 */       return (this.first.equals(productType.first, param1Boolean1, param1Boolean2) && this.second.equals(productType.second, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 220 */       if (this.hashCode == 0) {
/* 221 */         int i = this.first.hashCode();
/* 222 */         i = 31 * i + this.second.hashCode();
/* 223 */         this.hashCode = i;
/*     */       } 
/* 225 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 230 */       return DataFixUtils.or(this.first.findFieldTypeOpt(param1String), () -> this.second.findFieldTypeOpt(param1String));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Pair<F, G>> point(DynamicOps<?> param1DynamicOps) {
/* 235 */       return this.first.point(param1DynamicOps).flatMap(param1Object -> this.second.point(param1DynamicOps).map(()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 240 */       Either either = this.first.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean);
/* 241 */       return (Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException>)either.map(this::capLeft, param1FieldNotFoundException -> {
/*     */             Either either = this.second.findType(param1Type1, param1Type2, param1TypeMatcher, param1Boolean);
/*     */             return either.mapLeft(this::capRight);
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <FT, F2, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<F, F2, FT, FR> param1TypedOptic) {
/* 251 */       return Either.left(TypedOptic.proj1(param1TypedOptic.sType(), this.second, param1TypedOptic.tType()).compose(param1TypedOptic));
/*     */     }
/*     */     
/*     */     private <FT, G2, FR> TypedOptic<Pair<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> param1TypedOptic) {
/* 255 */       return TypedOptic.proj2(this.first, param1TypedOptic.sType(), param1TypedOptic.tType()).compose(param1TypedOptic);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Product.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */