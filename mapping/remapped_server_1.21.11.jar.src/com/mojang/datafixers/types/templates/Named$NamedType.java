/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
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
/*     */ public final class NamedType<A>
/*     */   extends Type<Pair<String, A>>
/*     */ {
/*     */   protected final String name;
/*     */   protected final Type<A> element;
/*     */   
/*     */   public NamedType(String paramString, Type<A> paramType) {
/*  70 */     this.name = paramString;
/*  71 */     this.element = paramType;
/*     */   }
/*     */   
/*     */   public static <A, B> RewriteResult<Pair<String, A>, ?> fix(NamedType<A> paramNamedType, RewriteResult<A, B> paramRewriteResult) {
/*  75 */     if (paramRewriteResult.view().isNop()) {
/*  76 */       return RewriteResult.nop(paramNamedType);
/*     */     }
/*  78 */     return opticView(paramNamedType, paramRewriteResult, wrapOptic(paramNamedType.name, TypedOptic.adapter(paramRewriteResult.view().type(), paramRewriteResult.view().newType())));
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<Pair<String, A>, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/*  83 */     RewriteResult<A, ?> rewriteResult = this.element.rewriteOrNop(paramTypeRewriteRule);
/*  84 */     return fix(this, rewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<Pair<String, A>, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/*  89 */     Optional optional = paramTypeRewriteRule.rewrite(this.element);
/*  90 */     return optional.map(paramRewriteResult -> fix(this, paramRewriteResult));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/*  95 */     return DSL.named(this.name, this.element.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 100 */     return DSL.named(this.name, this.element.template());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 105 */     return this.element.findChoiceType(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 110 */     return this.element.findCheckedType(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<Pair<String, A>> buildCodec() {
/* 115 */     return new Codec<Pair<String, A>>()
/*     */       {
/*     */         public <T> DataResult<Pair<Pair<String, A>, T>> decode(DynamicOps<T> param2DynamicOps, T param2T) {
/* 118 */           return Named.NamedType.this.element.codec().decode(param2DynamicOps, param2T).map(param2Pair -> param2Pair.mapFirst(())).setLifecycle(Lifecycle.experimental());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(Pair<String, A> param2Pair, DynamicOps<T> param2DynamicOps, T param2T) {
/* 123 */           if (!Objects.equals(param2Pair.getFirst(), Named.NamedType.this.name)) {
/* 124 */             return DataResult.error(() -> "Named type name doesn't match: expected: " + Named.NamedType.this.name + ", got: " + (String)param2Pair.getFirst(), param2T);
/*     */           }
/* 126 */           return Named.NamedType.this.element.codec().encode(param2Pair.getSecond(), param2DynamicOps, param2T).setLifecycle(Lifecycle.experimental());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return "NamedType[\"" + this.name + "\", " + String.valueOf(this.element) + "]";
/*     */   }
/*     */   
/*     */   public String name() {
/* 137 */     return this.name;
/*     */   }
/*     */   
/*     */   public Type<A> element() {
/* 141 */     return this.element;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 146 */     if (this == paramObject) {
/* 147 */       return true;
/*     */     }
/* 149 */     if (!(paramObject instanceof NamedType)) {
/* 150 */       return false;
/*     */     }
/* 152 */     NamedType namedType = (NamedType)paramObject;
/* 153 */     return (Objects.equals(this.name, namedType.name) && this.element.equals(namedType.element, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 158 */     int i = this.name.hashCode();
/* 159 */     i = 31 * i + this.element.hashCode();
/* 160 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 165 */     return this.element.findFieldTypeOpt(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Pair<String, A>> point(DynamicOps<?> paramDynamicOps) {
/* 170 */     return this.element.point(paramDynamicOps).map(paramObject -> Pair.of(this.name, paramObject));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<Pair<String, A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 175 */     return this.element.findType(paramType, paramType1, paramTypeMatcher, paramBoolean).mapLeft(paramTypedOptic -> wrapOptic(this.name, paramTypedOptic));
/*     */   }
/*     */   
/*     */   protected static <A, B, FT, FR> TypedOptic<Pair<String, A>, Pair<String, B>, FT, FR> wrapOptic(String paramString, TypedOptic<A, B, FT, FR> paramTypedOptic) {
/* 179 */     return (new TypedOptic(Cartesian.Mu.TYPE_TOKEN, 
/*     */         
/* 181 */         DSL.named(paramString, paramTypedOptic.sType()), 
/* 182 */         DSL.named(paramString, paramTypedOptic.tType()), paramTypedOptic
/* 183 */         .sType(), paramTypedOptic
/* 184 */         .tType(), 
/* 185 */         (Optic)Optics.proj2()))
/* 186 */       .compose(paramTypedOptic);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Named$NamedType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */