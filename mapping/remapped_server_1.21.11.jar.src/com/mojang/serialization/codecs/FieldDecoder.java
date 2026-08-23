/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapDecoder;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public final class FieldDecoder<A>
/*    */   extends MapDecoder.Implementation<A>
/*    */ {
/*    */   protected final String name;
/*    */   private final Decoder<A> elementCodec;
/*    */   
/*    */   public FieldDecoder(String paramString, Decoder<A> paramDecoder) {
/* 19 */     this.name = paramString;
/* 20 */     this.elementCodec = paramDecoder;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<A> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 25 */     Object object = paramMapLike.get(this.name);
/* 26 */     if (object == null) {
/* 27 */       return DataResult.error(() -> "No key " + this.name + " in " + String.valueOf(paramMapLike));
/*    */     }
/* 29 */     return this.elementCodec.parse(paramDynamicOps, object);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 34 */     return Stream.of((T)paramDynamicOps.createString(this.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 39 */     if (this == paramObject) {
/* 40 */       return true;
/*    */     }
/* 42 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 43 */       return false;
/*    */     }
/* 45 */     FieldDecoder fieldDecoder = (FieldDecoder)paramObject;
/* 46 */     return (Objects.equals(this.name, fieldDecoder.name) && Objects.equals(this.elementCodec, fieldDecoder.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return Objects.hash(new Object[] { this.name, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "FieldDecoder[" + this.name + ": " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\FieldDecoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */