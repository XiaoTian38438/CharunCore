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
/*     */ class Matcher<FT, FR>
/*     */   implements Type.TypeMatcher<FT, FR>
/*     */ {
/*     */   private final Type<FR> resultType;
/*     */   private final String name;
/*     */   private final Type<FT> type;
/*     */   
/*     */   public Matcher(String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  58 */     this.resultType = paramType1;
/*  59 */     this.name = paramString;
/*  60 */     this.type = paramType;
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
/*     */   public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> paramType) {
/*  76 */     if (paramType instanceof TaggedChoice.TaggedChoiceType) {
/*  77 */       TaggedChoice.TaggedChoiceType<String> taggedChoiceType = (TaggedChoice.TaggedChoiceType)paramType;
/*  78 */       Type type = (Type)taggedChoiceType.types().get(this.name);
/*  79 */       if (type != null) {
/*  80 */         if (!Objects.equals(this.type, type)) {
/*  81 */           return Either.right(new Type.FieldNotFoundException(String.format("Type error for choice type \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, paramType, type })));
/*     */         }
/*  83 */         return Either.left(TypedOptic.tagged(taggedChoiceType, this.name, this.type, this.resultType));
/*     */       } 
/*  85 */       return Either.right(new Type.Continue());
/*     */     } 
/*  87 */     if (paramType instanceof com.mojang.datafixers.types.templates.Tag.TagType) {
/*  88 */       return Either.right(new Type.FieldNotFoundException("in tag"));
/*     */     }
/*  90 */     return Either.right(new Type.Continue());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  95 */     if (this == paramObject) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/*  99 */       return false;
/*     */     }
/* 101 */     Matcher matcher = (Matcher)paramObject;
/* 102 */     return (Objects.equals(this.resultType, matcher.resultType) && Objects.equals(this.name, matcher.name) && Objects.equals(this.type, matcher.type));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 107 */     int i = this.resultType.hashCode();
/* 108 */     i = 31 * i + this.name.hashCode();
/* 109 */     i = 31 * i + this.type.hashCode();
/* 110 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\NamedChoiceFinder$Matcher.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */