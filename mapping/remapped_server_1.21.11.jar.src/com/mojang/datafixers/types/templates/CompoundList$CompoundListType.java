/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
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
/*     */ public final class CompoundListType<K, V>
/*     */   extends Type<List<Pair<K, V>>>
/*     */ {
/*     */   protected final Type<K> key;
/*     */   protected final Type<V> element;
/*     */   
/*     */   public CompoundListType(Type<K> paramType, Type<V> paramType1) {
/*  92 */     this.key = paramType;
/*  93 */     this.element = paramType1;
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<List<Pair<K, V>>, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/*  98 */     return mergeViews(this.key.rewriteOrNop(paramTypeRewriteRule), this.element.rewriteOrNop(paramTypeRewriteRule));
/*     */   }
/*     */   
/*     */   public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(RewriteResult<K, K2> paramRewriteResult, RewriteResult<V, V2> paramRewriteResult1) {
/* 102 */     RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> rewriteResult = fixKeys(this, this.key, this.element, paramRewriteResult);
/* 103 */     RewriteResult<List<Pair<?, V>>, List<Pair<?, V2>>> rewriteResult1 = fixValues(rewriteResult.view().newType(), paramRewriteResult.view().newType(), this.element, paramRewriteResult1);
/* 104 */     return rewriteResult1.compose(rewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 109 */     return DataFixUtils.or(paramTypeRewriteRule
/* 110 */         .rewrite(this.key).map(paramRewriteResult -> fixKeys(this, this.key, this.element, paramRewriteResult)), () -> paramTypeRewriteRule.rewrite(this.element).map(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(Type<List<Pair<K, V>>> paramType, Type<K> paramType1, Type<V> paramType2, RewriteResult<K, K2> paramRewriteResult) {
/* 116 */     return opticView(paramType, paramRewriteResult, TypedOptic.compoundListKeys(paramType1, paramRewriteResult.view().newType(), paramType2));
/*     */   }
/*     */   
/*     */   private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(Type<List<Pair<K, V>>> paramType, Type<K> paramType1, Type<V> paramType2, RewriteResult<V, V2> paramRewriteResult) {
/* 120 */     return opticView(paramType, paramRewriteResult, TypedOptic.compoundListElements(paramType1, paramType2, paramRewriteResult.view().newType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 125 */     return DSL.compoundList(this.key.updateMu(paramRecursiveTypeFamily), this.element.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 130 */     return new CompoundList(this.key.template(), this.element.template());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<List<Pair<K, V>>> point(DynamicOps<?> paramDynamicOps) {
/* 135 */     return (Optional)Optional.of(ImmutableList.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 140 */     Either either = this.key.findType(paramType, paramType1, paramTypeMatcher, paramBoolean);
/* 141 */     return (Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException>)either.map(this::capLeft, paramFieldNotFoundException -> {
/*     */           Either either = this.element.findType(paramType1, paramType2, paramTypeMatcher, paramBoolean);
/*     */           return either.mapLeft(this::capRight);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<K, K2, FT, FR> paramTypedOptic) {
/* 151 */     return Either.left(TypedOptic.compoundListKeys(paramTypedOptic.sType(), paramTypedOptic.tType(), this.element).compose(paramTypedOptic));
/*     */   }
/*     */   
/*     */   private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(TypedOptic<V, V2, FT, FR> paramTypedOptic) {
/* 155 */     return TypedOptic.compoundListElements(this.key, paramTypedOptic.sType(), paramTypedOptic.tType()).compose(paramTypedOptic);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<List<Pair<K, V>>> buildCodec() {
/* 160 */     return Codec.compoundList(this.key.codec(), this.element.codec());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return "CompoundList[" + String.valueOf(this.key) + " -> " + String.valueOf(this.element) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 170 */     if (!(paramObject instanceof CompoundListType)) {
/* 171 */       return false;
/*     */     }
/* 173 */     CompoundListType compoundListType = (CompoundListType)paramObject;
/* 174 */     return (this.key.equals(compoundListType.key, paramBoolean1, paramBoolean2) && this.element.equals(compoundListType.element, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     int i = this.key.hashCode();
/* 180 */     i = 31 * i + this.element.hashCode();
/* 181 */     return i;
/*     */   }
/*     */   
/*     */   public Type<K> getKey() {
/* 185 */     return this.key;
/*     */   }
/*     */   
/*     */   public Type<V> getElement() {
/* 189 */     return this.element;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\CompoundList$CompoundListType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */