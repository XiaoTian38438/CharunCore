/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
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
/*     */ public final class CheckType<A>
/*     */   extends Type<A>
/*     */ {
/*     */   private final String name;
/*     */   private final int index;
/*     */   private final int expectedIndex;
/*     */   private final Type<A> delegate;
/*     */   
/*     */   public CheckType(String paramString, int paramInt1, int paramInt2, Type<A> paramType) {
/*  88 */     this.name = paramString;
/*  89 */     this.index = paramInt1;
/*  90 */     this.expectedIndex = paramInt2;
/*  91 */     this.delegate = paramType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<A> buildCodec() {
/*  96 */     return Codec.of((Encoder)this.delegate
/*  97 */         .codec(), this::read);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> DataResult<Pair<A, T>> read(DynamicOps<T> paramDynamicOps, T paramT) {
/* 103 */     if (this.index != this.expectedIndex) {
/* 104 */       return DataResult.error(() -> "Index mismatch: " + this.index + " != " + this.expectedIndex);
/*     */     }
/* 106 */     return this.delegate.codec().decode(paramDynamicOps, paramT);
/*     */   }
/*     */   
/*     */   public static <A, B> RewriteResult<A, ?> fix(CheckType<A> paramCheckType, RewriteResult<A, B> paramRewriteResult) {
/* 110 */     if (paramRewriteResult.view().isNop()) {
/* 111 */       return RewriteResult.nop(paramCheckType);
/*     */     }
/* 113 */     return opticView(paramCheckType, paramRewriteResult, wrapOptic(paramCheckType, TypedOptic.adapter(paramRewriteResult.view().type(), paramRewriteResult.view().newType())));
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 118 */     if (paramBoolean2 && this.index != this.expectedIndex) {
/* 119 */       return RewriteResult.nop(this);
/*     */     }
/* 121 */     return fix(this, this.delegate.rewriteOrNop(paramTypeRewriteRule));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 126 */     if (paramBoolean2 && this.index != this.expectedIndex) {
/* 127 */       return Optional.empty();
/*     */     }
/* 129 */     return super.everywhere(paramTypeRewriteRule, paramPointFreeRule, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 134 */     return paramTypeRewriteRule.rewrite(this.delegate).map(paramRewriteResult -> fix(this, paramRewriteResult));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 139 */     return new CheckType(this.name, this.index, this.expectedIndex, this.delegate.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 144 */     return DSL.check(this.name, this.expectedIndex, this.delegate.template());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 149 */     if (paramInt == this.expectedIndex) {
/* 150 */       return this.delegate.findChoiceType(paramString, paramInt);
/*     */     }
/* 152 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 157 */     if (paramInt == this.expectedIndex) {
/* 158 */       return Optional.of(this.delegate);
/*     */     }
/* 160 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 165 */     if (this.index == this.expectedIndex) {
/* 166 */       return this.delegate.findFieldTypeOpt(paramString);
/*     */     }
/* 168 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<A> point(DynamicOps<?> paramDynamicOps) {
/* 173 */     if (this.index == this.expectedIndex) {
/* 174 */       return this.delegate.point(paramDynamicOps);
/*     */     }
/* 176 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 181 */     if (this.index != this.expectedIndex) {
/* 182 */       return Either.right(new Type.FieldNotFoundException("Incorrect index in CheckType"));
/*     */     }
/* 184 */     return this.delegate.findType(paramType, paramType1, paramTypeMatcher, paramBoolean).mapLeft(paramTypedOptic -> wrapOptic(this, paramTypedOptic));
/*     */   }
/*     */   
/*     */   protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(CheckType<A> paramCheckType, TypedOptic<A, B, FT, FR> paramTypedOptic) {
/* 188 */     return paramTypedOptic.castOuter(paramCheckType, new CheckType(paramCheckType.name, paramCheckType.index, paramCheckType.expectedIndex, paramTypedOptic.tType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     return "TypeTag[" + this.index + "~" + this.expectedIndex + "][" + this.name + ": " + String.valueOf(this.delegate) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 198 */     if (!(paramObject instanceof CheckType)) {
/* 199 */       return false;
/*     */     }
/* 201 */     CheckType checkType = (CheckType)paramObject;
/* 202 */     if (this.index == checkType.index && this.expectedIndex == checkType.expectedIndex) {
/* 203 */       if (!paramBoolean2) {
/* 204 */         return true;
/*     */       }
/* 206 */       if (this.delegate.equals(checkType.delegate, paramBoolean1, paramBoolean2)) {
/* 207 */         return true;
/*     */       }
/*     */     } 
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 215 */     int i = this.index;
/* 216 */     i = 31 * i + this.expectedIndex;
/* 217 */     i = 31 * i + this.delegate.hashCode();
/* 218 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Check$CheckType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */