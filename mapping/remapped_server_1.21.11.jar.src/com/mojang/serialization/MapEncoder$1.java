/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends MapEncoder.Implementation<B>
/*    */ {
/*    */   public <T> RecordBuilder<T> encode(B paramB, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 26 */     return MapEncoder.this.encode(function.apply(paramB), paramDynamicOps, paramRecordBuilder);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 31 */     return MapEncoder.this.keys(paramDynamicOps);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 36 */     return MapEncoder.this.toString() + "[comapped]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapEncoder$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */