/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class CompoundList extends Record implements TypeTemplate {
/*     */   private final TypeTemplate key;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/CompoundList;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */   }
/*     */   
/*  28 */   public CompoundList(TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) { this.key = paramTypeTemplate1; this.element = paramTypeTemplate2; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/CompoundList;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  28 */     //   #28	-> 0 } public TypeTemplate key() { return this.key; } public TypeTemplate element() { return this.element; }
/*     */   
/*     */   public int size() {
/*  31 */     return Math.max(this.key.size(), this.element.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily paramTypeFamily) {
/*  36 */     return paramInt -> DSL.compoundList(this.key.apply(paramTypeFamily).apply(paramInt), this.element.apply(paramTypeFamily).apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  41 */     return TypeFamily.familyOptic(paramInt -> cap(this.element.<A, B>applyO(paramFamilyOptic, paramType1, paramType2).apply(paramInt)));
/*     */   }
/*     */   
/*     */   private <S, T, A, B> TypedOptic<?, ?, A, B> cap(TypedOptic<S, T, A, B> paramTypedOptic) {
/*  45 */     Type type1 = DSL.and(DSL.string(), paramTypedOptic.sType());
/*  46 */     Type type2 = DSL.and(DSL.string(), paramTypedOptic.tType());
/*  47 */     return (new TypedOptic(TraversalP.Mu.TYPE_TOKEN, 
/*     */         
/*  49 */         DSL.compoundList(paramTypedOptic.sType()), 
/*  50 */         DSL.compoundList(paramTypedOptic.tType()), type1, type2, 
/*     */ 
/*     */         
/*  53 */         (Optic)Optics.listTraversal()))
/*  54 */       .compose(new TypedOptic(Cartesian.Mu.TYPE_TOKEN, type1, type2, paramTypedOptic
/*     */ 
/*     */ 
/*     */           
/*  58 */           .sType(), paramTypedOptic
/*  59 */           .tType(), 
/*  60 */           (Optic)Optics.proj2()))
/*  61 */       .compose(paramTypedOptic);
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  66 */     return this.element.<FT, FR>findFieldOrType(paramInt, paramString, paramType, paramType1).mapLeft(paramTypeTemplate -> new CompoundList(this.key, paramTypeTemplate));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  71 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult1 = this.key.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         RewriteResult<?, ?> rewriteResult2 = this.element.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(apply(paramTypeFamily).apply(paramInt), rewriteResult1, rewriteResult2);
/*     */       };
/*     */   }
/*     */   
/*     */   private <L, R> RewriteResult<?, ?> cap(Type<?> paramType, RewriteResult<L, ?> paramRewriteResult, RewriteResult<R, ?> paramRewriteResult1) {
/*  79 */     return ((CompoundListType)paramType).mergeViews(paramRewriteResult, paramRewriteResult1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     return "CompoundList[" + String.valueOf(this.element) + "]";
/*     */   }
/*     */   
/*     */   public static final class CompoundListType<K, V> extends Type<List<Pair<K, V>>> {
/*     */     protected final Type<K> key;
/*     */     protected final Type<V> element;
/*     */     
/*     */     public CompoundListType(Type<K> param1Type, Type<V> param1Type1) {
/*  92 */       this.key = param1Type;
/*  93 */       this.element = param1Type1;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<List<Pair<K, V>>, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/*  98 */       return mergeViews(this.key.rewriteOrNop(param1TypeRewriteRule), this.element.rewriteOrNop(param1TypeRewriteRule));
/*     */     }
/*     */     
/*     */     public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(RewriteResult<K, K2> param1RewriteResult, RewriteResult<V, V2> param1RewriteResult1) {
/* 102 */       RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> rewriteResult = fixKeys(this, this.key, this.element, param1RewriteResult);
/* 103 */       RewriteResult<List<Pair<?, V>>, List<Pair<?, V2>>> rewriteResult1 = fixValues(rewriteResult.view().newType(), param1RewriteResult.view().newType(), this.element, param1RewriteResult1);
/* 104 */       return rewriteResult1.compose(rewriteResult);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 109 */       return DataFixUtils.or(param1TypeRewriteRule
/* 110 */           .rewrite(this.key).map(param1RewriteResult -> fixKeys(this, this.key, this.element, param1RewriteResult)), () -> param1TypeRewriteRule.rewrite(this.element).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(Type<List<Pair<K, V>>> param1Type, Type<K> param1Type1, Type<V> param1Type2, RewriteResult<K, K2> param1RewriteResult) {
/* 116 */       return opticView(param1Type, param1RewriteResult, TypedOptic.compoundListKeys(param1Type1, param1RewriteResult.view().newType(), param1Type2));
/*     */     }
/*     */     
/*     */     private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(Type<List<Pair<K, V>>> param1Type, Type<K> param1Type1, Type<V> param1Type2, RewriteResult<V, V2> param1RewriteResult) {
/* 120 */       return opticView(param1Type, param1RewriteResult, TypedOptic.compoundListElements(param1Type1, param1Type2, param1RewriteResult.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 125 */       return DSL.compoundList(this.key.updateMu(param1RecursiveTypeFamily), this.element.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 130 */       return new CompoundList(this.key.template(), this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<List<Pair<K, V>>> point(DynamicOps<?> param1DynamicOps) {
/* 135 */       return (Optional)Optional.of(ImmutableList.of());
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 140 */       Either either = this.key.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean);
/* 141 */       return (Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException>)either.map(this::capLeft, param1FieldNotFoundException -> {
/*     */             Either either = this.element.findType(param1Type1, param1Type2, param1TypeMatcher, param1Boolean);
/*     */             return either.mapLeft(this::capRight);
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<K, K2, FT, FR> param1TypedOptic) {
/* 151 */       return Either.left(TypedOptic.compoundListKeys(param1TypedOptic.sType(), param1TypedOptic.tType(), this.element).compose(param1TypedOptic));
/*     */     }
/*     */     
/*     */     private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(TypedOptic<V, V2, FT, FR> param1TypedOptic) {
/* 155 */       return TypedOptic.compoundListElements(this.key, param1TypedOptic.sType(), param1TypedOptic.tType()).compose(param1TypedOptic);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<List<Pair<K, V>>> buildCodec() {
/* 160 */       return Codec.compoundList(this.key.codec(), this.element.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       return "CompoundList[" + String.valueOf(this.key) + " -> " + String.valueOf(this.element) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 170 */       if (!(param1Object instanceof CompoundListType)) {
/* 171 */         return false;
/*     */       }
/* 173 */       CompoundListType compoundListType = (CompoundListType)param1Object;
/* 174 */       return (this.key.equals(compoundListType.key, param1Boolean1, param1Boolean2) && this.element.equals(compoundListType.element, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 179 */       int i = this.key.hashCode();
/* 180 */       i = 31 * i + this.element.hashCode();
/* 181 */       return i;
/*     */     }
/*     */     
/*     */     public Type<K> getKey() {
/* 185 */       return this.key;
/*     */     }
/*     */     
/*     */     public Type<V> getElement() {
/* 189 */       return this.element;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\CompoundList.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */