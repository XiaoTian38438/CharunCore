/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HookType<A>
/*     */   extends Type<A>
/*     */ {
/*     */   private final Type<A> delegate;
/*     */   private final Hook.HookFunction preRead;
/*     */   private final Hook.HookFunction postWrite;
/*     */   
/*     */   public HookType(Type<A> paramType, Hook.HookFunction paramHookFunction1, Hook.HookFunction paramHookFunction2) {
/*  80 */     this.delegate = paramType;
/*  81 */     this.preRead = paramHookFunction1;
/*  82 */     this.postWrite = paramHookFunction2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<A> buildCodec() {
/*  87 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/*  90 */           return Hook.HookType.this.delegate.codec().decode(param2DynamicOps, Hook.HookType.this.preRead.apply(param2DynamicOps, param2T)).setLifecycle(Lifecycle.experimental());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(A param2A, DynamicOps<T> param2DynamicOps, T param2T) {
/*  95 */           return Hook.HookType.this.delegate.codec().encode(param2A, param2DynamicOps, param2T).map(param2Object -> Hook.HookType.this.postWrite.apply(param2DynamicOps, param2Object)).setLifecycle(Lifecycle.experimental());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 102 */     return fix(this, this.delegate.rewriteOrNop(paramTypeRewriteRule));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 107 */     return paramTypeRewriteRule.rewrite(this.delegate).map(paramRewriteResult -> fix(this, paramRewriteResult));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 112 */     return new HookType(this.delegate.updateMu(paramRecursiveTypeFamily), this.preRead, this.postWrite);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 117 */     return DSL.hook(this.delegate.template(), this.preRead, this.postWrite);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 122 */     return this.delegate.findChoiceType(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 127 */     return this.delegate.findCheckedType(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 132 */     return this.delegate.findFieldTypeOpt(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<A> point(DynamicOps<?> paramDynamicOps) {
/* 137 */     return this.delegate.point(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 142 */     return this.delegate.findType(paramType, paramType1, paramTypeMatcher, paramBoolean).mapLeft(paramTypedOptic -> wrapOptic(paramTypedOptic, this.preRead, this.postWrite));
/*     */   }
/*     */   
/*     */   public static <A, B> RewriteResult<A, ?> fix(HookType<A> paramHookType, RewriteResult<A, B> paramRewriteResult) {
/* 146 */     if (paramRewriteResult.view().isNop()) {
/* 147 */       return RewriteResult.nop(paramHookType);
/*     */     }
/* 149 */     return opticView(paramHookType, paramRewriteResult, wrapOptic(TypedOptic.adapter(paramRewriteResult.view().type(), paramRewriteResult.view().newType()), paramHookType.preRead, paramHookType.postWrite));
/*     */   }
/*     */   
/*     */   protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> paramTypedOptic, Hook.HookFunction paramHookFunction1, Hook.HookFunction paramHookFunction2) {
/* 153 */     return paramTypedOptic.castOuter(
/* 154 */         DSL.hook(paramTypedOptic.sType(), paramHookFunction1, paramHookFunction2), 
/* 155 */         DSL.hook(paramTypedOptic.tType(), paramHookFunction1, paramHookFunction2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return "HookType[" + String.valueOf(this.delegate) + ", " + String.valueOf(this.preRead) + ", " + String.valueOf(this.postWrite) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 166 */     if (!(paramObject instanceof HookType)) {
/* 167 */       return false;
/*     */     }
/* 169 */     HookType hookType = (HookType)paramObject;
/* 170 */     return (this.delegate.equals(hookType.delegate, paramBoolean1, paramBoolean2) && Objects.equals(this.preRead, hookType.preRead) && Objects.equals(this.postWrite, hookType.postWrite));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 175 */     int i = this.delegate.hashCode();
/* 176 */     i = 31 * i + this.preRead.hashCode();
/* 177 */     i = 31 * i + this.postWrite.hashCode();
/* 178 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Hook$HookType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */