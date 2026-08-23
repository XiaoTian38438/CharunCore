/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ public final class Const extends Record implements TypeTemplate {
/*     */   private final Type<?> type;
/*     */   
/*  22 */   public Const(Type<?> paramType) { this.type = paramType; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/templates/Const;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  22 */     //   #22	-> 0 } public Type<?> type() { return this.type; } public final boolean equals(Object paramObject) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/templates/Const;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */   } public int size() {
/*  25 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily paramTypeFamily) {
/*  30 */     return new TypeFamily()
/*     */       {
/*     */         public Type<?> apply(int param1Int) {
/*  33 */           return Const.this.type;
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
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  45 */     if (Objects.equals(this.type, paramType)) {
/*  46 */       return TypeFamily.familyOptic(paramInt -> new TypedOptic((Set)ImmutableSet.of(Profunctor.Mu.TYPE_TOKEN), paramType1, paramType2, paramType1, paramType2, (Optic)Optics.id()));
/*     */     }
/*  48 */     TypedOptic<?, ?, A, B> typedOptic = makeIgnoreOptic(this.type, paramType, paramType1);
/*  49 */     return TypeFamily.familyOptic(paramInt -> paramTypedOptic);
/*     */   }
/*     */   
/*     */   private <T, A, B> TypedOptic<T, T, A, B> makeIgnoreOptic(Type<T> paramType, Type<A> paramType1, Type<B> paramType2) {
/*  53 */     return new TypedOptic(AffineP.Mu.TYPE_TOKEN, paramType, paramType, paramType1, paramType2, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  59 */         (Optic)Optics.affine(Either::left, (paramObject1, paramObject2) -> paramObject2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<FT> paramType, Type<FR> paramType1) {
/*  65 */     return DSL.fieldFinder(paramString, paramType).findType(this.type, paramType1, false).mapLeft(paramTypedOptic -> new Const(paramTypedOptic.tType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  70 */     return paramInt -> RewriteResult.nop(this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  75 */     return "Const[" + String.valueOf(this.type) + "]";
/*     */   }
/*     */   
/*     */   public static final class PrimitiveType<A> extends Type<A> {
/*     */     private final Codec<A> codec;
/*     */     
/*     */     public PrimitiveType(Codec<A> param1Codec) {
/*  82 */       this.codec = param1Codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/*  87 */       return (this == param1Object);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/*  92 */       return DSL.constType(this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<A> buildCodec() {
/*  97 */       return this.codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 102 */       return this.codec.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Const.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */