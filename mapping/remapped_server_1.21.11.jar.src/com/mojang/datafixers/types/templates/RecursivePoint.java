/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class RecursivePoint extends Record implements TypeTemplate {
/*     */   private final int index;
/*     */   
/*  30 */   public RecursivePoint(int paramInt) { this.index = paramInt; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/RecursivePoint;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  30 */     //   #30	-> 0 } public int index() { return this.index; } public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/RecursivePoint;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */   } public int size() {
/*  33 */     return this.index + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily paramTypeFamily) {
/*  38 */     final Type result = paramTypeFamily.apply(this.index);
/*  39 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  42 */           return result;
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  55 */     return TypeFamily.familyOptic(paramInt -> paramFamilyOptic.apply(this.index));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  60 */     return Either.right(new Type.FieldNotFoundException("Recursion point"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  65 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult = paramIntFunction.apply(this.index);
/*     */         return cap(paramTypeFamily, rewriteResult);
/*     */       };
/*     */   }
/*     */   
/*     */   public <S, T> RewriteResult<S, T> cap(TypeFamily paramTypeFamily, RewriteResult<S, T> paramRewriteResult) {
/*  72 */     Type type = paramTypeFamily.apply(this.index);
/*  73 */     if (!(type instanceof RecursivePointType)) {
/*  74 */       throw new IllegalArgumentException("Type error: Recursive point template template got a non-recursice type as an input.");
/*     */     }
/*  76 */     if (!Objects.equals(paramRewriteResult.view().type(), type)) {
/*  77 */       throw new IllegalArgumentException("Type error: hmap function input type");
/*     */     }
/*  79 */     BitSet bitSet = (BitSet)paramRewriteResult.recData().clone();
/*  80 */     bitSet.set(this.index);
/*  81 */     return RewriteResult.create(paramRewriteResult.view(), bitSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  86 */     return "Id[" + this.index + "]";
/*     */   }
/*     */   
/*     */   public static final class RecursivePointType<A> extends Type<A> {
/*     */     private final RecursiveTypeFamily family;
/*     */     private final int index;
/*     */     private final Supplier<Type<A>> delegate;
/*     */     @Nullable
/*     */     private volatile Type<A> type;
/*     */     
/*     */     public RecursivePointType(RecursiveTypeFamily param1RecursiveTypeFamily, int param1Int, Supplier<Type<A>> param1Supplier) {
/*  97 */       this.family = param1RecursiveTypeFamily;
/*  98 */       this.index = param1Int;
/*  99 */       this.delegate = param1Supplier;
/*     */     }
/*     */     
/*     */     public RecursiveTypeFamily family() {
/* 103 */       return this.family;
/*     */     }
/*     */     
/*     */     public int index() {
/* 107 */       return this.index;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<A> unfold() {
/* 112 */       if (this.type == null) {
/* 113 */         this.type = this.delegate.get();
/*     */       }
/* 115 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 121 */       return new Codec<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 124 */             return RecursivePoint.RecursivePointType.this.unfold().codec().decode(param2DynamicOps, param2T).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(A param2A, DynamicOps<T> param2DynamicOps, T param2T) {
/* 129 */             return RecursivePoint.RecursivePointType.this.unfold().codec().encode(param2A, param2DynamicOps, param2T).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 137 */       return unfold().all(param1TypeRewriteRule, param1Boolean1, param1Boolean2);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 142 */       return unfold().one(param1TypeRewriteRule);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule param1TypeRewriteRule, PointFreeRule param1PointFreeRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 147 */       if (param1Boolean1) {
/* 148 */         Optional<RewriteResult<A, ?>> optional = this.family.everywhere(this.index, param1TypeRewriteRule, param1PointFreeRule).map(param1RewriteResult -> param1RewriteResult);
/* 149 */         if (optional.isPresent()) {
/* 150 */           return optional;
/*     */         }
/*     */       } 
/* 153 */       return Optional.of(RewriteResult.nop(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 158 */       return param1RecursiveTypeFamily.apply(this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 163 */       return DSL.id(this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 168 */       return unfold().findChoiceType(param1String, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 173 */       return unfold().findCheckedType(this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 178 */       return unfold().findFieldTypeOpt(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> param1DynamicOps) {
/* 183 */       return unfold().point(param1DynamicOps);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 193 */       return this.family.findType(this.index, param1Type, param1Type1, param1TypeMatcher, param1Boolean).mapLeft(param1TypedOptic -> {
/*     */             if (!Objects.equals(this, param1TypedOptic.sType())) {
/*     */               throw new IllegalStateException(":/");
/*     */             }
/*     */             return param1TypedOptic;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 233 */       return "MuType[" + this.family.name() + "_" + this.index + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 238 */       if (!(param1Object instanceof RecursivePointType)) {
/* 239 */         return false;
/*     */       }
/* 241 */       RecursivePointType recursivePointType = (RecursivePointType)param1Object;
/* 242 */       return ((param1Boolean1 || Objects.equals(this.family, recursivePointType.family)) && this.index == recursivePointType.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 247 */       int i = this.family.hashCode();
/* 248 */       i = 31 * i + this.index;
/* 249 */       return i;
/*     */     }
/*     */     
/*     */     public View<A, A> in() {
/* 253 */       return View.create(Functions.in(this));
/*     */     }
/*     */     
/*     */     public View<A, A> out() {
/* 257 */       return View.create(Functions.out(this));
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Codec<A> {
/*     */     public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.unfold().codec().decode(param1DynamicOps, param1T).setLifecycle(Lifecycle.experimental());
/*     */     }
/*     */     
/*     */     public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.unfold().codec().encode(param1A, param1DynamicOps, param1T).setLifecycle(Lifecycle.experimental());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\RecursivePoint.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */