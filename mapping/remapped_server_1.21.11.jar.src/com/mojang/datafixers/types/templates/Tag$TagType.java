/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
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
/*     */ public final class TagType<A>
/*     */   extends Type<A>
/*     */ {
/*     */   protected final String name;
/*     */   protected final Type<A> element;
/*     */   
/*     */   public TagType(String paramString, Type<A> paramType) {
/* 113 */     this.name = paramString;
/* 114 */     this.element = paramType;
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<A, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 119 */     return wrap(this.element.rewriteOrNop(paramTypeRewriteRule));
/*     */   }
/*     */   
/*     */   private <B> RewriteResult<A, B> wrap(RewriteResult<A, B> paramRewriteResult) {
/* 123 */     if (paramRewriteResult.view().isNop()) {
/* 124 */       return paramRewriteResult;
/*     */     }
/* 126 */     TagType tagType = DSL.field(this.name, paramRewriteResult.view().newType());
/* 127 */     return opticView(this, paramRewriteResult, new TypedOptic(Profunctor.Mu.TYPE_TOKEN, this, tagType, paramRewriteResult
/*     */ 
/*     */ 
/*     */           
/* 131 */           .view().type(), paramRewriteResult
/* 132 */           .view().newType(), 
/* 133 */           (Optic)Optics.id()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<A, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 139 */     Optional optional = paramTypeRewriteRule.rewrite(this.element);
/* 140 */     return optional.map(this::wrap);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 145 */     return DSL.field(this.name, this.element.updateMu(paramRecursiveTypeFamily));
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 150 */     return DSL.field(this.name, this.element.template());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<A> buildCodec() {
/* 155 */     return this.element.codec().fieldOf(this.name).codec();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return "Tag[\"" + this.name + "\", " + String.valueOf(this.element) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 165 */     if (this == paramObject) {
/* 166 */       return true;
/*     */     }
/* 168 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 169 */       return false;
/*     */     }
/* 171 */     TagType tagType = (TagType)paramObject;
/* 172 */     return (Objects.equals(this.name, tagType.name) && this.element.equals(tagType.element, paramBoolean1, paramBoolean2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 177 */     int i = this.name.hashCode();
/* 178 */     i = 31 * i + this.element.hashCode();
/* 179 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 184 */     if (Objects.equals(paramString, this.name)) {
/* 185 */       return Optional.of(this.element);
/*     */     }
/* 187 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<A> point(DynamicOps<?> paramDynamicOps) {
/* 192 */     return this.element.point(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/* 197 */     return this.element.findType(paramType, paramType1, paramTypeMatcher, paramBoolean).mapLeft(this::wrapOptic);
/*     */   }
/*     */   
/*     */   private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> paramTypedOptic) {
/* 201 */     return paramTypedOptic.castOuter(DSL.field(this.name, paramTypedOptic.sType()), DSL.field(this.name, paramTypedOptic.tType()));
/*     */   }
/*     */   
/*     */   public String name() {
/* 205 */     return this.name;
/*     */   }
/*     */   
/*     */   public Type<A> element() {
/* 209 */     return this.element;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Tag$TagType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */