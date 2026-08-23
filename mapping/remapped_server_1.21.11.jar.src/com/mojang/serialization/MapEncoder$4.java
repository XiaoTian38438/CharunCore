/*    */ package com.mojang.serialization;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends MapEncoder.Implementation<A>
/*    */ {
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 80 */     return MapEncoder.this.keys(paramDynamicOps);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(A paramA, DynamicOps<T> paramDynamicOps, RecordBuilder<T> paramRecordBuilder) {
/* 85 */     return MapEncoder.this.<T>encode(paramA, paramDynamicOps, paramRecordBuilder).setLifecycle(lifecycle);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return MapEncoder.this.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapEncoder$4.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */