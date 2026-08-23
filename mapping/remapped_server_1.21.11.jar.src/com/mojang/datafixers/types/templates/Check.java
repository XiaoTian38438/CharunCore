/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class Check extends Record implements TypeTemplate {
/*     */   private final String name;
/*     */   private final int index;
/*     */   private final TypeTemplate element;
/*     */   
/*  24 */   public Check(String paramString, int paramInt, TypeTemplate paramTypeTemplate) { this.name = paramString; this.index = paramInt; this.element = paramTypeTemplate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Check;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  24 */     //   #24	-> 0 } public String name() { return this.name; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Check;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  24 */     //   #24	-> 0 } public int index() { return this.index; } public TypeTemplate element() { return this.element; }
/*     */   
/*     */   public int size() {
/*  27 */     return Math.max(this.index + 1, this.element.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  32 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  35 */           if (param1Int < 0) {
/*  36 */             throw new IndexOutOfBoundsException();
/*     */           }
/*  38 */           return new Check.CheckType(Check.this.name, param1Int, Check.this.index, Check.this.element.apply(family).apply(param1Int));
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  53 */     return TypeFamily.familyOptic(paramInt -> this.element.<A, B>applyO(paramFamilyOptic, paramType1, paramType2).apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  58 */     if (paramInt == this.index) {
/*  59 */       return this.element.findFieldOrType(paramInt, paramString, paramType, paramType1);
/*     */     }
/*  61 */     return Either.right(new Type.FieldNotFoundException("Not a matching index"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  66 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult = this.element.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(paramTypeFamily, paramInt, rewriteResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <A> RewriteResult<?, ?> cap(TypeFamily paramTypeFamily, int paramInt, RewriteResult<A, ?> paramRewriteResult) {
/*  73 */     return CheckType.fix((CheckType)apply(paramTypeFamily).apply(paramInt), paramRewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     return "Tag[" + this.name + ", " + this.index + ": " + String.valueOf(this.element) + "]";
/*     */   }
/*     */   
/*     */   public static final class CheckType<A> extends Type<A> {
/*     */     private final String name;
/*     */     private final int index;
/*     */     private final int expectedIndex;
/*     */     private final Type<A> delegate;
/*     */     
/*     */     public CheckType(String param1String, int param1Int1, int param1Int2, Type<A> param1Type) {
/*  88 */       this.name = param1String;
/*  89 */       this.index = param1Int1;
/*  90 */       this.expectedIndex = param1Int2;
/*  91 */       this.delegate = param1Type;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/*  96 */       return Codec.of((Encoder)this.delegate
/*  97 */           .codec(), this::read);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private <T> DataResult<Pair<A, T>> read(DynamicOps<T> param1DynamicOps, T param1T) {
/* 103 */       if (this.index != this.expectedIndex) {
/* 104 */         return DataResult.error(() -> "Index mismatch: " + this.index + " != " + this.expectedIndex);
/*     */       }
/* 106 */       return this.delegate.codec().decode(param1DynamicOps, param1T);
/*     */     }
/*     */     
/*     */     public static <A, B> RewriteResult<A, ?> fix(CheckType<A> param1CheckType, RewriteResult<A, B> param1RewriteResult) {
/* 110 */       if (param1RewriteResult.view().isNop()) {
/* 111 */         return RewriteResult.nop(param1CheckType);
/*     */       }
/* 113 */       return opticView(param1CheckType, param1RewriteResult, wrapOptic(param1CheckType, TypedOptic.adapter(param1RewriteResult.view().type(), param1RewriteResult.view().newType())));
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 118 */       if (param1Boolean2 && this.index != this.expectedIndex) {
/* 119 */         return RewriteResult.nop(this);
/*     */       }
/* 121 */       return fix(this, this.delegate.rewriteOrNop(param1TypeRewriteRule));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule param1TypeRewriteRule, PointFreeRule param1PointFreeRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 126 */       if (param1Boolean2 && this.index != this.expectedIndex) {
/* 127 */         return Optional.empty();
/*     */       }
/* 129 */       return super.everywhere(param1TypeRewriteRule, param1PointFreeRule, param1Boolean1, param1Boolean2);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 134 */       return param1TypeRewriteRule.rewrite(this.delegate).map(param1RewriteResult -> fix(this, param1RewriteResult));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 139 */       return new CheckType(this.name, this.index, this.expectedIndex, this.delegate.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 144 */       return DSL.check(this.name, this.expectedIndex, this.delegate.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 149 */       if (param1Int == this.expectedIndex) {
/* 150 */         return this.delegate.findChoiceType(param1String, param1Int);
/*     */       }
/* 152 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 157 */       if (param1Int == this.expectedIndex) {
/* 158 */         return Optional.of(this.delegate);
/*     */       }
/* 160 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 165 */       if (this.index == this.expectedIndex) {
/* 166 */         return this.delegate.findFieldTypeOpt(param1String);
/*     */       }
/* 168 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> param1DynamicOps) {
/* 173 */       if (this.index == this.expectedIndex) {
/* 174 */         return this.delegate.point(param1DynamicOps);
/*     */       }
/* 176 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 181 */       if (this.index != this.expectedIndex) {
/* 182 */         return Either.right(new Type.FieldNotFoundException("Incorrect index in CheckType"));
/*     */       }
/* 184 */       return this.delegate.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean).mapLeft(param1TypedOptic -> wrapOptic(this, param1TypedOptic));
/*     */     }
/*     */     
/*     */     protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(CheckType<A> param1CheckType, TypedOptic<A, B, FT, FR> param1TypedOptic) {
/* 188 */       return param1TypedOptic.castOuter(param1CheckType, new CheckType(param1CheckType.name, param1CheckType.index, param1CheckType.expectedIndex, param1TypedOptic.tType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 193 */       return "TypeTag[" + this.index + "~" + this.expectedIndex + "][" + this.name + ": " + String.valueOf(this.delegate) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 198 */       if (!(param1Object instanceof CheckType)) {
/* 199 */         return false;
/*     */       }
/* 201 */       CheckType checkType = (CheckType)param1Object;
/* 202 */       if (this.index == checkType.index && this.expectedIndex == checkType.expectedIndex) {
/* 203 */         if (!param1Boolean2) {
/* 204 */           return true;
/*     */         }
/* 206 */         if (this.delegate.equals(checkType.delegate, param1Boolean1, param1Boolean2)) {
/* 207 */           return true;
/*     */         }
/*     */       } 
/* 210 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 215 */       int i = this.index;
/* 216 */       i = 31 * i + this.expectedIndex;
/* 217 */       i = 31 * i + this.delegate.hashCode();
/* 218 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Check.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */