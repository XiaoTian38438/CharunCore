/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OptionalFieldCodec<A>
/*    */   extends MapCodec<Optional<A>>
/*    */ {
/*    */   private final String name;
/*    */   private final Codec<A> elementCodec;
/*    */   private final boolean lenient;
/*    */   
/*    */   public OptionalFieldCodec(String paramString, Codec<A> paramCodec, boolean paramBoolean) {
/* 23 */     this.name = paramString;
/* 24 */     this.elementCodec = paramCodec;
/* 25 */     this.lenient = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Optional<A>> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike) {
/* 30 */     Object object = paramMapLike.get(this.name);
/* 31 */     if (object == null) {
/* 32 */       return DataResult.success(Optional.empty());
/*    */     }
/* 34 */     DataResult dataResult = this.elementCodec.parse(paramDynamicOps, object);
/* 35 */     if (dataResult.isError() && this.lenient) {
/* 36 */       return DataResult.success(Optional.empty());
/*    */     }
/* 38 */     return dataResult.map(Optional::of).setPartial(dataResult.resultOrPartial());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Optional<A> paramOptional, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 43 */     if (paramOptional.isPresent()) {
/* 44 */       return paramRecordBuilder.add(this.name, this.elementCodec.encodeStart(paramDynamicOps, paramOptional.get()));
/*    */     }
/* 46 */     return paramRecordBuilder;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 51 */     return Stream.of((T)paramDynamicOps.createString(this.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 56 */     if (this == paramObject) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 60 */       return false;
/*    */     }
/* 62 */     OptionalFieldCodec optionalFieldCodec = (OptionalFieldCodec)paramObject;
/* 63 */     return (Objects.equals(this.name, optionalFieldCodec.name) && Objects.equals(this.elementCodec, optionalFieldCodec.elementCodec) && this.lenient == optionalFieldCodec.lenient);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return Objects.hash(new Object[] { this.name, this.elementCodec, Boolean.valueOf(this.lenient) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return "OptionalFieldCodec[" + this.name + ": " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\codecs\OptionalFieldCodec.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */