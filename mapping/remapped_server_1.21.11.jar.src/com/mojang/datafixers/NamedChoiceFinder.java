/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NamedChoiceFinder<FT>
/*     */   implements OpticFinder<FT>
/*     */ {
/*     */   private final String name;
/*     */   private final Type<FT> type;
/*     */   
/*     */   public NamedChoiceFinder(String paramString, Type<FT> paramType) {
/*  19 */     this.name = paramString;
/*  20 */     this.type = paramType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<FT> type() {
/*  25 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> paramType, Type<FR> paramType1, boolean paramBoolean) {
/*  30 */     return paramType.findTypeCached(this.type, paramType1, new Matcher<>(this.name, this.type, paramType1), paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  35 */     if (this == paramObject) {
/*  36 */       return true;
/*     */     }
/*  38 */     if (!(paramObject instanceof NamedChoiceFinder)) {
/*  39 */       return false;
/*     */     }
/*  41 */     NamedChoiceFinder namedChoiceFinder = (NamedChoiceFinder)paramObject;
/*  42 */     return (Objects.equals(this.name, namedChoiceFinder.name) && Objects.equals(this.type, namedChoiceFinder.type));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  47 */     int i = this.name.hashCode();
/*  48 */     i = 31 * i + this.type.hashCode();
/*  49 */     return i;
/*     */   }
/*     */   
/*     */   private static class Matcher<FT, FR> implements Type.TypeMatcher<FT, FR> {
/*     */     private final Type<FR> resultType;
/*     */     private final String name;
/*     */     private final Type<FT> type;
/*     */     
/*     */     public Matcher(String param1String, Type<FT> param1Type, Type<FR> param1Type1) {
/*  58 */       this.resultType = param1Type1;
/*  59 */       this.name = param1String;
/*  60 */       this.type = param1Type;
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
/*     */     public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> param1Type) {
/*  76 */       if (param1Type instanceof TaggedChoice.TaggedChoiceType) {
/*  77 */         TaggedChoice.TaggedChoiceType<String> taggedChoiceType = (TaggedChoice.TaggedChoiceType)param1Type;
/*  78 */         Type type = (Type)taggedChoiceType.types().get(this.name);
/*  79 */         if (type != null) {
/*  80 */           if (!Objects.equals(this.type, type)) {
/*  81 */             return Either.right(new Type.FieldNotFoundException(String.format("Type error for choice type \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, param1Type, type })));
/*     */           }
/*  83 */           return Either.left(TypedOptic.tagged(taggedChoiceType, this.name, this.type, this.resultType));
/*     */         } 
/*  85 */         return Either.right(new Type.Continue());
/*     */       } 
/*  87 */       if (param1Type instanceof com.mojang.datafixers.types.templates.Tag.TagType) {
/*  88 */         return Either.right(new Type.FieldNotFoundException("in tag"));
/*     */       }
/*  90 */       return Either.right(new Type.Continue());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/*  95 */       if (this == param1Object) {
/*  96 */         return true;
/*     */       }
/*  98 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/*  99 */         return false;
/*     */       }
/* 101 */       Matcher matcher = (Matcher)param1Object;
/* 102 */       return (Objects.equals(this.resultType, matcher.resultType) && Objects.equals(this.name, matcher.name) && Objects.equals(this.type, matcher.type));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 107 */       int i = this.resultType.hashCode();
/* 108 */       i = 31 * i + this.name.hashCode();
/* 109 */       i = 31 * i + this.type.hashCode();
/* 110 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\NamedChoiceFinder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */