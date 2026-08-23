/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RecursivePointType<A>
/*     */   extends Type<A>
/*     */ {
/*     */   private final RecursiveTypeFamily family;
/*     */   private final int index;
/*     */   private final Supplier<Type<A>> delegate;
/*     */   @Nullable
/*     */   private volatile Type<A> type;
/*     */   
/*     */   public RecursivePointType(RecursiveTypeFamily paramRecursiveTypeFamily, int paramInt, Supplier<Type<A>> paramSupplier) {
/*  97 */     this.family = paramRecursiveTypeFamily;
/*  98 */     this.index = paramInt;
/*  99 */     this.delegate = paramSupplier;
/*     */   }
/*     */   
/*     */   public RecursiveTypeFamily family() {
/* 103 */     return this.family;
/*     */   }
/*     */   
/*     */   public int index() {
/* 107 */     return this.index;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<A> unfold() {
/* 112 */     if (this.type == null) {
/* 113 */       this.type = this.delegate.get();
/*     */     }
/* 115 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Codec<A> buildCodec() {
/* 121 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 124 */           return RecursivePoint.RecursivePointType.this.unfold().codec().decode(param2DynamicOps, param2T).setLifecycle(Lifecycle.experimental());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(A param2A, DynamicOps<T> param2DynamicOps, T param2T) {
/* 129 */           return RecursivePoint.RecursivePointType.this.unfold().codec().encode(param2A, param2DynamicOps, param2T).setLifecycle(Lifecycle.experimental());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 137 */     return unfold().all(paramTypeRewriteRule, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 142 */     return unfold().one(paramTypeRewriteRule);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 147 */     if (paramBoolean1) {
/* 148 */       Optional<RewriteResult<A, ?>> optional = this.family.everywhere(this.index, paramTypeRewriteRule, paramPointFreeRule).map(paramRewriteResult -> paramRewriteResult);
/* 149 */       if (optional.isPresent()) {
/* 150 */         return optional;
/*     */       }
/*     */     } 
/* 153 */     return Optional.of(RewriteResult.nop(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 158 */     return paramRecursiveTypeFamily.apply(this.index);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 163 */     return DSL.id(this.index);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 168 */     return unfold().findChoiceType(paramString, this.index);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 173 */     return unfold().findCheckedType(this.index);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 178 */     return unfold().findFieldTypeOpt(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<A> point(DynamicOps<?> paramDynamicOps) {
/* 183 */     return unfold().point(paramDynamicOps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 193 */     return this.family.findType(this.index, paramType, paramType1, paramTypeMatcher, paramBoolean).mapLeft(paramTypedOptic -> {
/*     */           if (!Objects.equals(this, paramTypedOptic.sType())) {
/*     */             throw new IllegalStateException(":/");
/*     */           }
/*     */           return paramTypedOptic;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     return "MuType[" + this.family.name() + "_" + this.index + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 238 */     if (!(paramObject instanceof RecursivePointType)) {
/* 239 */       return false;
/*     */     }
/* 241 */     RecursivePointType recursivePointType = (RecursivePointType)paramObject;
/* 242 */     return ((paramBoolean1 || Objects.equals(this.family, recursivePointType.family)) && this.index == recursivePointType.index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 247 */     int i = this.family.hashCode();
/* 248 */     i = 31 * i + this.index;
/* 249 */     return i;
/*     */   }
/*     */   
/*     */   public View<A, A> in() {
/* 253 */     return View.create(Functions.in(this));
/*     */   }
/*     */   
/*     */   public View<A, A> out() {
/* 257 */     return View.create(Functions.out(this));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\RecursivePoint$RecursivePointType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */