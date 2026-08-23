/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class Tag extends Record implements TypeTemplate {
/*     */   private final String name;
/*     */   private final TypeTemplate element;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Tag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */   }
/*     */   
/*  24 */   public Tag(String paramString, TypeTemplate paramTypeTemplate) { this.name = paramString; this.element = paramTypeTemplate; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Tag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  24 */     //   #24	-> 0 } public String name() { return this.name; } public TypeTemplate element() { return this.element; }
/*     */   
/*     */   public int size() {
/*  27 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  32 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  35 */           return DSL.field(Tag.this.name, Tag.this.element.apply(family).apply(param1Int));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  65 */     return TypeFamily.familyOptic(paramInt -> this.element.<A, B>applyO(paramFamilyOptic, paramType1, paramType2).apply(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  70 */     if (!Objects.equals(paramString, this.name)) {
/*  71 */       return Either.right(new Type.FieldNotFoundException("Names don't match"));
/*     */     }
/*  73 */     if (this.element instanceof Const) {
/*  74 */       Const const_ = (Const)this.element;
/*  75 */       if (Objects.equals(paramType, const_.type())) {
/*  76 */         return Either.left(new Tag(paramString, new Const(paramType1)));
/*     */       }
/*  78 */       return Either.right(new Type.FieldNotFoundException("don't match"));
/*     */     } 
/*     */     
/*  81 */     if (Objects.equals(paramType, paramType1)) {
/*  82 */       return Either.left(this);
/*     */     }
/*  84 */     if (paramType instanceof RecursivePoint.RecursivePointType && this.element instanceof RecursivePoint && (
/*  85 */       (RecursivePoint)this.element).index() == ((RecursivePoint.RecursivePointType)paramType).index()) {
/*  86 */       if (paramType1 instanceof RecursivePoint.RecursivePointType) {
/*  87 */         if (((RecursivePoint.RecursivePointType)paramType1).index() == ((RecursivePoint)this.element).index()) {
/*  88 */           return Either.left(this);
/*     */         }
/*     */       } else {
/*  91 */         return Either.left(DSL.constType(paramType1));
/*     */       } 
/*     */     }
/*     */     
/*  95 */     return Either.right(new Type.FieldNotFoundException("Recursive field"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/* 100 */     return this.element.hmap(paramTypeFamily, paramIntFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "NameTag[" + this.name + ": " + String.valueOf(this.element) + "]";
/*     */   }
/*     */   
/*     */   public static final class TagType<A> extends Type<A> {
/*     */     protected final String name;
/*     */     protected final Type<A> element;
/*     */     
/*     */     public TagType(String param1String, Type<A> param1Type) {
/* 113 */       this.name = param1String;
/* 114 */       this.element = param1Type;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<A, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 119 */       return wrap(this.element.rewriteOrNop(param1TypeRewriteRule));
/*     */     }
/*     */     
/*     */     private <B> RewriteResult<A, B> wrap(RewriteResult<A, B> param1RewriteResult) {
/* 123 */       if (param1RewriteResult.view().isNop()) {
/* 124 */         return param1RewriteResult;
/*     */       }
/* 126 */       TagType tagType = DSL.field(this.name, param1RewriteResult.view().newType());
/* 127 */       return opticView(this, param1RewriteResult, new TypedOptic(Profunctor.Mu.TYPE_TOKEN, this, tagType, param1RewriteResult
/*     */ 
/*     */ 
/*     */             
/* 131 */             .view().type(), param1RewriteResult
/* 132 */             .view().newType(), 
/* 133 */             (Optic)Optics.id()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<A, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 139 */       Optional optional = param1TypeRewriteRule.rewrite(this.element);
/* 140 */       return optional.map(this::wrap);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 145 */       return DSL.field(this.name, this.element.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 150 */       return DSL.field(this.name, this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/* 155 */       return this.element.codec().fieldOf(this.name).codec();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 160 */       return "Tag[\"" + this.name + "\", " + String.valueOf(this.element) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 165 */       if (this == param1Object) {
/* 166 */         return true;
/*     */       }
/* 168 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/* 169 */         return false;
/*     */       }
/* 171 */       TagType tagType = (TagType)param1Object;
/* 172 */       return (Objects.equals(this.name, tagType.name) && this.element.equals(tagType.element, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 177 */       int i = this.name.hashCode();
/* 178 */       i = 31 * i + this.element.hashCode();
/* 179 */       return i;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 184 */       if (Objects.equals(param1String, this.name)) {
/* 185 */         return Optional.of(this.element);
/*     */       }
/* 187 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<A> point(DynamicOps<?> param1DynamicOps) {
/* 192 */       return this.element.point(param1DynamicOps);
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 197 */       return this.element.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean).mapLeft(this::wrapOptic);
/*     */     }
/*     */     
/*     */     private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> param1TypedOptic) {
/* 201 */       return param1TypedOptic.castOuter(DSL.field(this.name, param1TypedOptic.sType()), DSL.field(this.name, param1TypedOptic.tType()));
/*     */     }
/*     */     
/*     */     public String name() {
/* 205 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<A> element() {
/* 209 */       return this.element;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Tag.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */