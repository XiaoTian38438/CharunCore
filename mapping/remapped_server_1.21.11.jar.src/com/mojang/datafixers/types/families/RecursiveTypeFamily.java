/*     */ package com.mojang.datafixers.types.families;
/*     */ 
/*     */ import com.google.common.collect.Interner;
/*     */ import com.google.common.collect.Interners;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFree;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class RecursiveTypeFamily
/*     */   implements TypeFamily
/*     */ {
/*  34 */   private static final Interner<TypeTemplate> TEMPLATE_INTERNER = Interners.newWeakInterner();
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final TypeTemplate template;
/*     */   private final int size;
/*  40 */   private final Int2ObjectMap<RecursivePoint.RecursivePointType<?>> types = Int2ObjectMaps.synchronize((Int2ObjectMap)new Int2ObjectOpenHashMap());
/*     */   private final int hashCode;
/*     */   
/*     */   public RecursiveTypeFamily(String paramString, TypeTemplate paramTypeTemplate) {
/*  44 */     this.name = paramString;
/*  45 */     this.template = (TypeTemplate)TEMPLATE_INTERNER.intern(paramTypeTemplate);
/*  46 */     this.size = paramTypeTemplate.size();
/*  47 */     this.hashCode = Objects.hashCode(paramTypeTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> RecursivePoint.RecursivePointType<A> buildMuType(Type<A> paramType, @Nullable RecursiveTypeFamily paramRecursiveTypeFamily) {
/*  52 */     if (paramRecursiveTypeFamily == null) {
/*     */       
/*  54 */       TypeTemplate typeTemplate = paramType.template();
/*     */       
/*  56 */       if (Objects.equals(this.template, typeTemplate)) {
/*  57 */         paramRecursiveTypeFamily = this;
/*     */       } else {
/*  59 */         paramRecursiveTypeFamily = new RecursiveTypeFamily("ruled " + this.name, typeTemplate);
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     RecursivePoint.RecursivePointType<?> recursivePointType = null;
/*  64 */     for (byte b = 0; b < paramRecursiveTypeFamily.size; b++) {
/*  65 */       RecursivePoint.RecursivePointType<?> recursivePointType1 = paramRecursiveTypeFamily.apply(b);
/*  66 */       Type type = recursivePointType1.unfold();
/*  67 */       if (paramType.equals(type, true, false)) {
/*  68 */         recursivePointType = recursivePointType1;
/*     */         break;
/*     */       } 
/*     */     } 
/*  72 */     if (recursivePointType == null) {
/*  73 */       throw new IllegalStateException("Couldn't determine the new type properly");
/*     */     }
/*  75 */     return (RecursivePoint.RecursivePointType)recursivePointType;
/*     */   }
/*     */   
/*     */   public String name() {
/*  79 */     return this.name;
/*     */   }
/*     */   
/*     */   public TypeTemplate template() {
/*  83 */     return this.template;
/*     */   }
/*     */   
/*     */   public int size() {
/*  87 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> fold(Algebra paramAlgebra, RecursiveTypeFamily paramRecursiveTypeFamily) {
/*  94 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult = paramAlgebra.apply(paramInt);
/*     */         return RewriteResult.create(View.create(foldUnchecked(this, paramRecursiveTypeFamily, paramAlgebra, paramInt)), rewriteResult.recData());
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <A, B> PointFree<Function<A, B>> foldUnchecked(RecursiveTypeFamily paramRecursiveTypeFamily1, RecursiveTypeFamily paramRecursiveTypeFamily2, Algebra paramAlgebra, int paramInt) {
/* 103 */     RecursivePoint.RecursivePointType<?> recursivePointType1 = paramRecursiveTypeFamily1.apply(paramInt);
/* 104 */     RecursivePoint.RecursivePointType<?> recursivePointType2 = paramRecursiveTypeFamily2.apply(paramInt);
/* 105 */     return Functions.fold(recursivePointType1, recursivePointType2, paramAlgebra, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public RecursivePoint.RecursivePointType<?> apply(int paramInt) {
/* 110 */     if (paramInt < 0) {
/* 111 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 113 */     return (RecursivePoint.RecursivePointType)this.types.computeIfAbsent(paramInt, paramInt -> new RecursivePoint.RecursivePointType(this, paramInt, ()));
/*     */   }
/*     */   
/*     */   public <A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> findType(int paramInt, Type<A> paramType, Type<B> paramType1, Type.TypeMatcher<A, B> paramTypeMatcher, boolean paramBoolean) {
/* 117 */     return apply(paramInt).unfold().findType(paramType, paramType1, paramTypeMatcher, false).flatMap(paramTypedOptic -> {
/*     */           TypeTemplate typeTemplate = paramTypedOptic.tType().template();
/*     */           ArrayList<FamilyOptic> arrayList = Lists.newArrayList();
/*     */           RecursiveTypeFamily recursiveTypeFamily = new RecursiveTypeFamily(this.name, typeTemplate);
/*     */           RecursivePoint.RecursivePointType<?> recursivePointType1 = apply(paramInt);
/*     */           RecursivePoint.RecursivePointType<?> recursivePointType2 = recursiveTypeFamily.apply(paramInt);
/*     */           if (paramBoolean) {
/*     */             FamilyOptic familyOptic = ();
/*     */             arrayList.add(this.template.applyO(familyOptic, paramType1, paramType2));
/*     */             TypedOptic typedOptic = ((FamilyOptic)arrayList.get(0)).apply(paramInt);
/*     */             return Either.left(typedOptic.castOuterUnchecked((Type)recursivePointType1, (Type)recursivePointType2));
/*     */           } 
/*     */           return mkSimpleOptic(recursivePointType1, recursivePointType2, paramType1, paramType2, paramTypeMatcher);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <S, T, A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> mkSimpleOptic(RecursivePoint.RecursivePointType<S> paramRecursivePointType, RecursivePoint.RecursivePointType<T> paramRecursivePointType1, Type<A> paramType, Type<B> paramType1, Type.TypeMatcher<A, B> paramTypeMatcher) {
/* 138 */     return paramRecursivePointType.unfold().findType(paramType, paramType1, paramTypeMatcher, false).mapLeft(paramTypedOptic -> paramTypedOptic.castOuterUnchecked((Type)paramRecursivePointType1, (Type)paramRecursivePointType2));
/*     */   }
/*     */   
/*     */   public Optional<RewriteResult<?, ?>> everywhere(int paramInt, TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule) {
/* 142 */     Type type = apply(paramInt).unfold();
/* 143 */     RewriteResult rewriteResult1 = (RewriteResult)DataFixUtils.orElse(type.everywhere(paramTypeRewriteRule, paramPointFreeRule, false, false), RewriteResult.nop(type));
/* 144 */     RecursivePoint.RecursivePointType<?> recursivePointType = buildMuType(rewriteResult1.view().newType(), null);
/* 145 */     RecursiveTypeFamily recursiveTypeFamily = recursivePointType.family();
/*     */     
/* 147 */     ArrayList<RewriteResult<?, ?>> arrayList = Lists.newArrayList();
/* 148 */     boolean bool = false;
/*     */     
/* 150 */     for (byte b = 0; b < this.size; b++) {
/* 151 */       RecursivePoint.RecursivePointType<?> recursivePointType1 = apply(b);
/* 152 */       Type type1 = recursivePointType1.unfold();
/* 153 */       boolean bool1 = true;
/*     */       
/* 155 */       RewriteResult<?, ?> rewriteResult = (RewriteResult)DataFixUtils.orElse(type1.everywhere(paramTypeRewriteRule, paramPointFreeRule, false, true), RewriteResult.nop(type1));
/* 156 */       if (!rewriteResult.view().isNop()) {
/* 157 */         bool1 = false;
/*     */       }
/*     */       
/* 160 */       RecursivePoint.RecursivePointType<?> recursivePointType2 = buildMuType(rewriteResult.view().newType(), recursiveTypeFamily);
/* 161 */       boolean bool2 = cap2(arrayList, recursivePointType1, paramTypeRewriteRule, paramPointFreeRule, bool1, rewriteResult, recursivePointType2);
/* 162 */       bool = (bool || !bool2) ? true : false;
/*     */     } 
/* 164 */     if (!bool) {
/* 165 */       return Optional.empty();
/*     */     }
/* 167 */     ListAlgebra listAlgebra = new ListAlgebra("everywhere", arrayList);
/* 168 */     RewriteResult rewriteResult2 = fold(listAlgebra, recursiveTypeFamily).apply(paramInt);
/* 169 */     return Optional.of(RewriteResult.create(View.create(rewriteResult2.view().function()), rewriteResult2.recData()));
/*     */   }
/*     */ 
/*     */   
/*     */   private <A, B> boolean cap2(List<RewriteResult<?, ?>> paramList, RecursivePoint.RecursivePointType<A> paramRecursivePointType, TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, boolean paramBoolean, RewriteResult<?, ?> paramRewriteResult, RecursivePoint.RecursivePointType<B> paramRecursivePointType1) {
/* 174 */     RewriteResult rewriteResult = RewriteResult.create(paramRecursivePointType1.in(), new BitSet()).compose(paramRewriteResult);
/*     */     
/* 176 */     Optional<RewriteResult> optional = paramTypeRewriteRule.rewrite(rewriteResult.view().newType());
/* 177 */     if (optional.isPresent() && !((RewriteResult)optional.get()).view().isNop()) {
/* 178 */       paramBoolean = false;
/* 179 */       paramRewriteResult = ((RewriteResult)optional.get()).compose(rewriteResult);
/*     */     } 
/* 181 */     paramRewriteResult = RewriteResult.create(paramRewriteResult.view().rewriteOrNop(paramPointFreeRule), paramRewriteResult.recData());
/* 182 */     paramList.add(paramRewriteResult);
/* 183 */     return paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 188 */     return "Mu[" + this.name + ", " + this.size + ", " + String.valueOf(this.template) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 193 */     if (this == paramObject) {
/* 194 */       return true;
/*     */     }
/* 196 */     if (!(paramObject instanceof RecursiveTypeFamily)) {
/* 197 */       return false;
/*     */     }
/* 199 */     RecursiveTypeFamily recursiveTypeFamily = (RecursiveTypeFamily)paramObject;
/* 200 */     return (this.template == recursiveTypeFamily.template);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 205 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\families\RecursiveTypeFamily.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */