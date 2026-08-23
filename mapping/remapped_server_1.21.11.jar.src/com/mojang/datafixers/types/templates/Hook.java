/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class Hook extends Record implements TypeTemplate {
/*     */   private final TypeTemplate element;
/*     */   private final HookFunction preRead;
/*     */   private final HookFunction postWrite;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Hook;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */   }
/*     */   
/*  25 */   public Hook(TypeTemplate paramTypeTemplate, HookFunction paramHookFunction1, HookFunction paramHookFunction2) { this.element = paramTypeTemplate; this.preRead = paramHookFunction1; this.postWrite = paramHookFunction2; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Hook;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  25 */     //   #25	-> 0 } public TypeTemplate element() { return this.element; } public HookFunction preRead() { return this.preRead; } public HookFunction postWrite() { return this.postWrite; }
/*     */   
/*  27 */   public static interface HookFunction { public static final HookFunction IDENTITY = new HookFunction()
/*     */       {
/*     */         public <T> T apply(DynamicOps<T> param2DynamicOps, T param2T) {
/*  30 */           return param2T; } }; <T> T apply(DynamicOps<T> param1DynamicOps, T param1T); } class null implements HookFunction { public <T> T apply(DynamicOps<T> param1DynamicOps, T param1T) { return param1T; }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  39 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily paramTypeFamily) {
/*  44 */     return paramInt -> DSL.hook(this.element.apply(paramTypeFamily).apply(paramInt), this.preRead, this.postWrite);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  49 */     return TypeFamily.familyOptic(paramInt -> this.element.<A, B>applyO(paramFamilyOptic, paramType1, paramType2).apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  54 */     return this.element.findFieldOrType(paramInt, paramString, paramType, paramType1);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  59 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult = this.element.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(paramTypeFamily, paramInt, rewriteResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <A> RewriteResult<A, ?> cap(TypeFamily paramTypeFamily, int paramInt, RewriteResult<A, ?> paramRewriteResult) {
/*  66 */     return HookType.fix((HookType<A>)apply(paramTypeFamily).apply(paramInt), paramRewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  71 */     return "Hook[" + String.valueOf(this.element) + ", " + String.valueOf(this.preRead) + ", " + String.valueOf(this.postWrite) + "]";
/*     */   }
/*     */   
/*     */   public static final class HookType<A> extends Type<A> {
/*     */     private final Type<A> delegate;
/*     */     private final Hook.HookFunction preRead;
/*     */     private final Hook.HookFunction postWrite;
/*     */     
/*     */     public HookType(Type<A> param1Type, Hook.HookFunction param1HookFunction1, Hook.HookFunction param1HookFunction2) {
/*  80 */       this.delegate = param1Type;
/*  81 */       this.preRead = param1HookFunction1;
/*  82 */       this.postWrite = param1HookFunction2;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/*  87 */       return new Codec<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/*  90 */             return Hook.HookType.this.delegate.codec().decode(param2DynamicOps, Hook.HookType.this.preRead.apply(param2DynamicOps, param2T)).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(A param2A, DynamicOps<T> param2DynamicOps, T param2T) {
/*  95 */             return Hook.HookType.this.delegate.codec().encode(param2A, param2DynamicOps, param2T).map(param2Object -> Hook.HookType.this.postWrite.apply(param2DynamicOps, param2Object)).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 102 */       return fix(this, this.delegate.rewriteOrNop(param1TypeRewriteRule));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 107 */       return param1TypeRewriteRule.rewrite(this.delegate).map(param1RewriteResult -> fix(this, param1RewriteResult));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 112 */       return new HookType(this.delegate.updateMu(param1RecursiveTypeFamily), this.preRead, this.postWrite);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 117 */       return DSL.hook(this.delegate.template(), this.preRead, this.postWrite);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 122 */       return this.delegate.findChoiceType(param1String, param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 127 */       return this.delegate.findCheckedType(param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 132 */       return this.delegate.findFieldTypeOpt(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> param1DynamicOps) {
/* 137 */       return this.delegate.point(param1DynamicOps);
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 142 */       return this.delegate.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean).mapLeft(param1TypedOptic -> wrapOptic(param1TypedOptic, this.preRead, this.postWrite));
/*     */     }
/*     */     
/*     */     public static <A, B> RewriteResult<A, ?> fix(HookType<A> param1HookType, RewriteResult<A, B> param1RewriteResult) {
/* 146 */       if (param1RewriteResult.view().isNop()) {
/* 147 */         return RewriteResult.nop(param1HookType);
/*     */       }
/* 149 */       return opticView(param1HookType, param1RewriteResult, wrapOptic(TypedOptic.adapter(param1RewriteResult.view().type(), param1RewriteResult.view().newType()), param1HookType.preRead, param1HookType.postWrite));
/*     */     }
/*     */     
/*     */     protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> param1TypedOptic, Hook.HookFunction param1HookFunction1, Hook.HookFunction param1HookFunction2) {
/* 153 */       return param1TypedOptic.castOuter(
/* 154 */           DSL.hook(param1TypedOptic.sType(), param1HookFunction1, param1HookFunction2), 
/* 155 */           DSL.hook(param1TypedOptic.tType(), param1HookFunction1, param1HookFunction2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 161 */       return "HookType[" + String.valueOf(this.delegate) + ", " + String.valueOf(this.preRead) + ", " + String.valueOf(this.postWrite) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 166 */       if (!(param1Object instanceof HookType)) {
/* 167 */         return false;
/*     */       }
/* 169 */       HookType hookType = (HookType)param1Object;
/* 170 */       return (this.delegate.equals(hookType.delegate, param1Boolean1, param1Boolean2) && Objects.equals(this.preRead, hookType.preRead) && Objects.equals(this.postWrite, hookType.postWrite));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 175 */       int i = this.delegate.hashCode();
/* 176 */       i = 31 * i + this.preRead.hashCode();
/* 177 */       i = 31 * i + this.postWrite.hashCode();
/* 178 */       return i;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Codec<A> {
/*     */     public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.delegate.codec().decode(param1DynamicOps, this.this$0.preRead.apply(param1DynamicOps, param1T)).setLifecycle(Lifecycle.experimental());
/*     */     }
/*     */     
/*     */     public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.delegate.codec().encode(param1A, param1DynamicOps, param1T).map(param1Object -> this.this$0.postWrite.apply(param1DynamicOps, param1Object)).setLifecycle(Lifecycle.experimental());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Hook.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */