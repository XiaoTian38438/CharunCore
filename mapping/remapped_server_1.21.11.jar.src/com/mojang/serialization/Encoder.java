/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.serialization.codecs.FieldEncoder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Encoder<A>
/*    */ {
/*    */   <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT);
/*    */   
/*    */   default <T> DataResult<T> encodeStart(DynamicOps<T> paramDynamicOps, A paramA) {
/* 14 */     return encode(paramA, paramDynamicOps, paramDynamicOps.empty());
/*    */   }
/*    */   
/*    */   default MapEncoder<A> fieldOf(String paramString) {
/* 18 */     return (MapEncoder<A>)new FieldEncoder(paramString, this);
/*    */   }
/*    */   
/*    */   default <B> Encoder<B> comap(final Function<? super B, ? extends A> function) {
/* 22 */     return new Encoder<B>()
/*    */       {
/*    */         public <T> DataResult<T> encode(B param1B, DynamicOps<T> param1DynamicOps, T param1T) {
/* 25 */           return Encoder.this.encode(function.apply(param1B), param1DynamicOps, param1T);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 30 */           return Encoder.this.toString() + "[comapped]";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default <B> Encoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
/* 36 */     return new Encoder<B>()
/*    */       {
/*    */         public <T> DataResult<T> encode(B param1B, DynamicOps<T> param1DynamicOps, T param1T) {
/* 39 */           return ((DataResult)function.apply(param1B)).flatMap(param1Object2 -> Encoder.this.encode(param1Object2, param1DynamicOps, param1Object1));
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 44 */           return Encoder.this.toString() + "[flatComapped]";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default Encoder<A> withLifecycle(final Lifecycle lifecycle) {
/* 50 */     return new Encoder<A>()
/*    */       {
/*    */         public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/* 53 */           return Encoder.this.<T>encode(param1A, param1DynamicOps, param1T).setLifecycle(lifecycle);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 58 */           return Encoder.this.toString();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <A> MapEncoder<A> empty() {
/* 64 */     return new MapEncoder.Implementation<A>()
/*    */       {
/*    */         public <T> RecordBuilder<T> encode(A param1A, DynamicOps<T> param1DynamicOps, RecordBuilder<T> param1RecordBuilder) {
/* 67 */           return param1RecordBuilder;
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 72 */           return Stream.empty();
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 77 */           return "EmptyEncoder";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <A> Encoder<A> error(final String error) {
/* 83 */     return new Encoder<A>()
/*    */       {
/*    */         public <T> DataResult<T> encode(A param1A, DynamicOps<T> param1DynamicOps, T param1T) {
/* 86 */           return DataResult.error(() -> param1String + " " + param1String);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 91 */           return "ErrorEncoder[" + error + "]";
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Encoder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */