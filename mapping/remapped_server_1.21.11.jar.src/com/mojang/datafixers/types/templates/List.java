/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class List extends Record implements TypeTemplate {
/*     */   private final TypeTemplate element;
/*     */   
/*  24 */   public List(TypeTemplate paramTypeTemplate) { this.element = paramTypeTemplate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/List;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  24 */     //   #24	-> 0 } public TypeTemplate element() { return this.element; } public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/List;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #24	-> 0
/*     */   } public int size() {
/*  27 */     return this.element.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(final TypeFamily family) {
/*  32 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  35 */           return DSL.list(List.this.element.apply(family).apply(param1Int));
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  52 */     return TypeFamily.familyOptic(paramInt -> cap(this.element.<A, B>applyO(paramFamilyOptic, paramType1, paramType2).apply(paramInt)));
/*     */   }
/*     */   
/*     */   private <S, T, A, B> TypedOptic<?, ?, A, B> cap(TypedOptic<S, T, A, B> paramTypedOptic) {
/*  56 */     return (new TypedOptic(TraversalP.Mu.TYPE_TOKEN, 
/*     */         
/*  58 */         DSL.list(paramTypedOptic.sType()), 
/*  59 */         DSL.list(paramTypedOptic.tType()), paramTypedOptic
/*  60 */         .sType(), paramTypedOptic
/*  61 */         .tType(), 
/*  62 */         (Optic)Optics.listTraversal()))
/*  63 */       .compose(paramTypedOptic);
/*     */   }
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  68 */     return this.element.<FT, FR>findFieldOrType(paramInt, paramString, paramType, paramType1).mapLeft(List::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  73 */     return paramInt -> {
/*     */         RewriteResult<?, ?> rewriteResult = this.element.hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */         return cap(apply(paramTypeFamily).apply(paramInt), rewriteResult);
/*     */       };
/*     */   }
/*     */   
/*     */   private <E> RewriteResult<?, ?> cap(Type<?> paramType, RewriteResult<E, ?> paramRewriteResult) {
/*  80 */     return ((ListType)paramType).fix(paramRewriteResult);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  85 */     return "List[" + String.valueOf(this.element) + "]";
/*     */   }
/*     */   
/*     */   public static final class ListType<A> extends Type<java.util.List<A>> {
/*     */     protected final Type<A> element;
/*     */     
/*     */     public ListType(Type<A> param1Type) {
/*  92 */       this.element = param1Type;
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<java.util.List<A>, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/*  97 */       RewriteResult<A, ?> rewriteResult = this.element.rewriteOrNop(param1TypeRewriteRule);
/*  98 */       return fix(rewriteResult);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<java.util.List<A>, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 103 */       return param1TypeRewriteRule.rewrite(this.element).map(this::fix);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 108 */       return DSL.list(this.element.updateMu(param1RecursiveTypeFamily));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 113 */       return DSL.list(this.element.template());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<java.util.List<A>> point(DynamicOps<?> param1DynamicOps) {
/* 118 */       return (Optional)Optional.of(ImmutableList.of());
/*     */     }
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<java.util.List<A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/* 123 */       Either either = this.element.findType(param1Type, param1Type1, param1TypeMatcher, param1Boolean);
/* 124 */       return either.mapLeft(this::capLeft);
/*     */     }
/*     */     
/*     */     private <FT, FR, B> TypedOptic<java.util.List<A>, ?, FT, FR> capLeft(TypedOptic<A, B, FT, FR> param1TypedOptic) {
/* 128 */       return TypedOptic.list(param1TypedOptic.sType(), param1TypedOptic.tType()).compose(param1TypedOptic);
/*     */     }
/*     */     
/*     */     public <B> RewriteResult<java.util.List<A>, ?> fix(RewriteResult<A, B> param1RewriteResult) {
/* 132 */       return opticView(this, param1RewriteResult, TypedOptic.list(this.element, param1RewriteResult.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Codec<java.util.List<A>> buildCodec() {
/* 137 */       return Codec.list(this.element.codec());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 142 */       return "List[" + String.valueOf(this.element) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 147 */       return (param1Object instanceof ListType && this.element.equals(((ListType)param1Object).element, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 152 */       return this.element.hashCode();
/*     */     }
/*     */     
/*     */     public Type<A> getElement() {
/* 156 */       return this.element;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\List.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */