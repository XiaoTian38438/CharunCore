/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements Keyable
/*    */ {
/*    */   public <T> Stream<T> keys(DynamicOps<T> paramDynamicOps) {
/* 13 */     Objects.requireNonNull(paramDynamicOps); return ((Stream)keys.get()).map(paramDynamicOps::createString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Keyable$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */