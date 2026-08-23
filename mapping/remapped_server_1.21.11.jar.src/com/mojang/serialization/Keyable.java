/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public interface Keyable
/*    */ {
/*    */   static Keyable forStrings(final Supplier<Stream<String>> keys) {
/* 10 */     return new Keyable()
/*    */       {
/*    */         public <T> Stream<T> keys(DynamicOps<T> param1DynamicOps) {
/* 13 */           Objects.requireNonNull(param1DynamicOps); return ((Stream)keys.get()).map(param1DynamicOps::createString);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   <T> Stream<T> keys(DynamicOps<T> paramDynamicOps);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Keyable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */