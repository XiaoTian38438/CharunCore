/*    */ package com.mojang.datafixers.types;
/*    */ 
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Func<A, B>
/*    */   extends Type<Function<A, B>>
/*    */ {
/*    */   protected final Type<A> first;
/*    */   protected final Type<B> second;
/*    */   
/*    */   public Func(Type<A> paramType, Type<B> paramType1) {
/* 18 */     this.first = paramType;
/* 19 */     this.second = paramType1;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeTemplate buildTemplate() {
/* 24 */     throw new UnsupportedOperationException("No template for function types.");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Codec<Function<A, B>> buildCodec() {
/* 29 */     return Codec.of(
/* 30 */         Encoder.error("Cannot save a function"), 
/* 31 */         Decoder.error("Cannot read a function"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     return "(" + String.valueOf(this.first) + " -> " + String.valueOf(this.second) + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 42 */     if (!(paramObject instanceof Func)) {
/* 43 */       return false;
/*    */     }
/* 45 */     Func func = (Func)paramObject;
/* 46 */     return (this.first.equals(func.first, paramBoolean1, paramBoolean2) && this.second.equals(func.second, paramBoolean1, paramBoolean2));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     int i = this.first.hashCode();
/* 52 */     i = 31 * i + this.second.hashCode();
/* 53 */     return i;
/*    */   }
/*    */   
/*    */   public Type<A> first() {
/* 57 */     return this.first;
/*    */   }
/*    */   
/*    */   public Type<B> second() {
/* 61 */     return this.second;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\Func.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */