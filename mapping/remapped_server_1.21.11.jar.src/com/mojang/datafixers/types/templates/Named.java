/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class Named extends Record implements TypeTemplate {
/*     */   private final String name;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Named;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #27	-> 0
/*     */   }
/*     */   
/*  27 */   public Named(String paramString, TypeTemplate paramTypeTemplate) { this.name = paramString; this.element = paramTypeTemplate; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Named;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  27 */     //   #27	-> 0 } public String name() { return this.name; } public TypeTemplate element() { return this.element; }
/*     */   
/*     */   public int size() {
/*  30 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily paramTypeFamily) {
/*  35 */     return paramInt -> DSL.named(this.name, this.element.apply(paramTypeFamily).apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  40 */     return TypeFamily.familyOptic(paramInt -> this.element.<A, B>applyO(paramFamilyOptic, paramType1, paramType2).apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  45 */     return this.element.findFieldOrType(paramInt, paramString, paramType, paramType1);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  50 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult = this.element.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(paramTypeFamily, paramInt, rewriteResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <A> RewriteResult<Pair<String, A>, ?> cap(TypeFamily paramTypeFamily, int paramInt, RewriteResult<A, ?> paramRewriteResult) {
/*  57 */     return NamedType.fix((NamedType<A>)apply(paramTypeFamily).apply(paramInt), paramRewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  62 */     return "NamedTypeTag[" + this.name + ": " + String.valueOf(this.element) + "]";
/*     */   }
/*     */   
/*     */   public static final class NamedType<A> extends Type<Pair<String, A>> {
/*     */     protected final String name;
/*     */     protected final Type<A> element;
/*     */     
/*     */     public NamedType(String param1String, Type<A> param1Type) {
/*  70 */       this.name = param1String;
/*  71 */       this.element = param1Type;
/*     */     }
/*     */     
/*     */     public static <A, B> RewriteResult<Pair<String, A>, ?> fix(NamedType<A> param1NamedType, RewriteResult<A, B> param1RewriteResult) {
/*  75 */       if (param1RewriteResult.view().isNop()) {
/*  76 */         return RewriteResult.nop(param1NamedType);
/*     */       }
/*  78 */       return opticView(param1NamedType, param1RewriteResult, wrapOptic(param1NamedType.name, TypedOptic.adapter(param1RewriteResult.view().type(), param1RewriteResult.view().newType())));
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Pair<String, A>, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/*  83 */       RewriteResult<A, ?> rewriteResult = this.element.rewriteOrNop(param1TypeRewriteRule);
/*  84 */       return fix(this, rewriteResult);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Pair<String, A>, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/*  89 */       Optional optional = param1TypeRewriteRule.rewrite(this.element);
/*  90 */       return optional.map(param1RewriteResult -> fix(this, param1RewriteResult));
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/*  95 */       return DSL.named(this.name, this.element.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 100 */       return DSL.named(this.name, this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 105 */       return this.element.findChoiceType(param1String, param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 110 */       return this.element.findCheckedType(param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<Pair<String, A>> buildCodec() {
/* 115 */       return new Codec<Pair<String, A>>()
/*     */         {
/*     */           public <T> DataResult<Pair<Pair<String, A>, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 118 */             return Named.NamedType.this.element.codec().decode(param2DynamicOps, param2T).map(param2Pair -> param2Pair.mapFirst(())).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(Pair<String, A> param2Pair, DynamicOps<T> param2DynamicOps, T param2T) {
/* 123 */             if (!Objects.equals(param2Pair.getFirst(), Named.NamedType.this.name)) {
/* 124 */               return DataResult.error(() -> "Named type name doesn't match: expected: " + Named.NamedType.this.name + ", got: " + (String)param2Pair.getFirst(), param2T);
/*     */             }
/* 126 */             return Named.NamedType.this.element.codec().encode(param2Pair.getSecond(), param2DynamicOps, param2T).setLifecycle(Lifecycle.experimental());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 133 */       return "NamedType[\"" + this.name + "\", " + String.valueOf(this.element) + "]";
/*     */     }
/*     */     
/*     */     public String name() {
/* 137 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<A> element() {
/* 141 */       return this.element;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 146 */       if (this == param1Object) {
/* 147 */         return true;
/*     */       }
/* 149 */       if (!(param1Object instanceof NamedType)) {
/* 150 */         return false;
/*     */       }
/* 152 */       NamedType namedType = (NamedType)param1Object;
/* 153 */       return (Objects.equals(this.name, namedType.name) && this.element.equals(namedType.element, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 158 */       int i = this.name.hashCode();
/* 159 */       i = 31 * i + this.element.hashCode();
/* 160 */       return i;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 165 */       return this.element.findFieldTypeOpt(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Pair<String, A>> point(DynamicOps<?> param1DynamicOps) {
/* 170 */       return this.element.point(param1DynamicOps).map(param1Object -> Pair.of(this.name, param1Object));
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Pair<String, A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 175 */       return this.element.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean).mapLeft(param1TypedOptic -> wrapOptic(this.name, param1TypedOptic));
/*     */     }
/*     */     
/*     */     protected static <A, B, FT, FR> TypedOptic<Pair<String, A>, Pair<String, B>, FT, FR> wrapOptic(String param1String, TypedOptic<A, B, FT, FR> param1TypedOptic) {
/* 179 */       return (new TypedOptic(Cartesian.Mu.TYPE_TOKEN, 
/*     */           
/* 181 */           DSL.named(param1String, param1TypedOptic.sType()), 
/* 182 */           DSL.named(param1String, param1TypedOptic.tType()), param1TypedOptic
/* 183 */           .sType(), param1TypedOptic
/* 184 */           .tType(), 
/* 185 */           (Optic)Optics.proj2()))
/* 186 */         .compose(param1TypedOptic);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Codec<Pair<String, A>> {
/*     */     public <T> DataResult<Pair<Pair<String, A>, T>> decode(DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       return this.this$0.element.codec().decode(param1DynamicOps, param1T).map(param1Pair -> param1Pair.mapFirst(())).setLifecycle(Lifecycle.experimental());
/*     */     }
/*     */     
/*     */     public <T> DataResult<T> encode(Pair<String, A> param1Pair, DynamicOps<T> param1DynamicOps, T param1T) {
/*     */       if (!Objects.equals(param1Pair.getFirst(), this.this$0.name))
/*     */         return DataResult.error(() -> "Named type name doesn't match: expected: " + this.this$0.name + ", got: " + (String)param1Pair.getFirst(), param1T); 
/*     */       return this.this$0.element.codec().encode(param1Pair.getSecond(), param1DynamicOps, param1T).setLifecycle(Lifecycle.experimental());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Named.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */