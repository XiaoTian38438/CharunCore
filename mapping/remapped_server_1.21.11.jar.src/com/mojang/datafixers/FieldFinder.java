/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FieldFinder<FT>
/*     */   implements OpticFinder<FT>
/*     */ {
/*     */   @Nullable
/*     */   private final String name;
/*     */   private final Type<FT> type;
/*     */   
/*     */   public FieldFinder(@Nullable String paramString, Type<FT> paramType) {
/*  25 */     this.name = paramString;
/*  26 */     this.type = paramType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<FT> type() {
/*  31 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> paramType, Type<FR> paramType1, boolean paramBoolean) {
/*  36 */     return paramType.findTypeCached(this.type, paramType1, new Matcher<>(this.name, this.type, paramType1), paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  41 */     if (this == paramObject) {
/*  42 */       return true;
/*     */     }
/*  44 */     if (!(paramObject instanceof FieldFinder)) {
/*  45 */       return false;
/*     */     }
/*  47 */     FieldFinder fieldFinder = (FieldFinder)paramObject;
/*  48 */     return (Objects.equals(this.name, fieldFinder.name) && Objects.equals(this.type, fieldFinder.type));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  53 */     int i = (this.name != null) ? this.name.hashCode() : 0;
/*  54 */     i = 31 * i + this.type.hashCode();
/*  55 */     return i;
/*     */   }
/*     */   
/*     */   private static final class Matcher<FT, FR> implements Type.TypeMatcher<FT, FR> {
/*     */     private final Type<FR> resultType;
/*     */     @Nullable
/*     */     private final String name;
/*     */     private final Type<FT> type;
/*     */     
/*     */     public Matcher(@Nullable String param1String, Type<FT> param1Type, Type<FR> param1Type1) {
/*  65 */       this.resultType = param1Type1;
/*  66 */       this.name = param1String;
/*  67 */       this.type = param1Type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> param1Type) {
/*  73 */       if (this.name == null && this.type.equals(param1Type, true, false)) {
/*  74 */         return Either.left(new TypedOptic<>(Profunctor.Mu.TYPE_TOKEN, param1Type, this.resultType, param1Type, this.resultType, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  80 */               (Optic<?, S, FR, S, FR>)Optics.id()));
/*     */       }
/*     */       
/*  83 */       if (param1Type instanceof Tag.TagType) {
/*  84 */         Tag.TagType tagType = (Tag.TagType)param1Type;
/*  85 */         if (!Objects.equals(tagType.name(), this.name)) {
/*  86 */           return Either.right(new Type.FieldNotFoundException(String.format("Not found: \"%s\" (in type: %s)", new Object[] { this.name, param1Type })));
/*     */         }
/*  88 */         if (!Objects.equals(this.type, tagType.element())) {
/*  89 */           return Either.right(new Type.FieldNotFoundException(String.format("Type error for field \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, this.type, tagType.element() })));
/*     */         }
/*  91 */         return Either.left(new TypedOptic<>(Profunctor.Mu.TYPE_TOKEN, (Type<?>)tagType, 
/*     */ 
/*     */               
/*  94 */               (Type<?>)DSL.field(tagType.name(), this.resultType), this.type, this.resultType, 
/*     */ 
/*     */               
/*  97 */               (Optic<?, ?, ?, FT, FR>)Optics.id()));
/*     */       } 
/*     */       
/* 100 */       if (param1Type instanceof TaggedChoice.TaggedChoiceType) {
/* 101 */         TaggedChoice.TaggedChoiceType taggedChoiceType = (TaggedChoice.TaggedChoiceType)param1Type;
/* 102 */         if (Objects.equals(this.name, taggedChoiceType.getName())) {
/* 103 */           if (!Objects.equals(this.type, taggedChoiceType.getKeyType())) {
/* 104 */             return Either.right(new Type.FieldNotFoundException(String.format("Type error for field \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, this.type, taggedChoiceType.getKeyType() })));
/*     */           }
/* 106 */           if (!Objects.equals(this.type, this.resultType)) {
/* 107 */             return Either.right(new Type.FieldNotFoundException("TaggedChoiceType key type change is unsupported."));
/*     */           }
/* 109 */           return Either.left(capChoice((Type<?>)taggedChoiceType));
/*     */         } 
/*     */       } 
/* 112 */       return Either.right(new Type.Continue());
/*     */     }
/*     */ 
/*     */     
/*     */     private <V> TypedOptic<Pair<FT, V>, ?, FT, FT> capChoice(Type<?> param1Type) {
/* 117 */       return (TypedOptic)new TypedOptic<>(Cartesian.Mu.TYPE_TOKEN, param1Type, param1Type, this.type, this.type, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 123 */           (Optic<?, ?, ?, FT, FT>)Optics.proj1());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 129 */       if (this == param1Object) {
/* 130 */         return true;
/*     */       }
/* 132 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/* 133 */         return false;
/*     */       }
/* 135 */       Matcher matcher = (Matcher)param1Object;
/* 136 */       return (Objects.equals(this.resultType, matcher.resultType) && Objects.equals(this.name, matcher.name) && Objects.equals(this.type, matcher.type));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 141 */       int i = this.resultType.hashCode();
/* 142 */       i = 31 * i + ((this.name != null) ? this.name.hashCode() : 0);
/* 143 */       i = 31 * i + this.type.hashCode();
/* 144 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\FieldFinder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */