/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.MapEncoder;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public class FieldEncoder<A>
/*    */   extends MapEncoder.Implementation<A>
/*    */ {
/*    */   private final String name;
/*    */   private final Encoder<A> elementCodec;
/*    */   
/*    */   public FieldEncoder(String paramString, Encoder<A> paramEncoder) {
/* 19 */     this.name = paramString;
/* 20 */     this.elementCodec = paramEncoder;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(A paramA, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 25 */     DataResult dataResult = this.elementCodec.encodeStart(paramDynamicOps, paramA);
/* 26 */     return paramRecordBuilder.add(this.name, dataResult);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 31 */     return Stream.of((T)paramDynamicOps.createString(this.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 36 */     if (this == paramObject) {
/* 37 */       return true;
/*    */     }
/* 39 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 40 */       return false;
/*    */     }
/* 42 */     FieldEncoder fieldEncoder = (FieldEncoder)paramObject;
/* 43 */     return (Objects.equals(this.name, fieldEncoder.name) && Objects.equals(this.elementCodec, fieldEncoder.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 48 */     return Objects.hash(new Object[] { this.name, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return "FieldEncoder[" + this.name + ": " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\FieldEncoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */