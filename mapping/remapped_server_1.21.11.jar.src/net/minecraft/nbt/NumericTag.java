/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ public interface NumericTag
/*    */   extends PrimitiveTag {
/*    */   byte byteValue();
/*    */   
/*    */   short shortValue();
/*    */   
/*    */   int intValue();
/*    */   
/*    */   long longValue();
/*    */   
/*    */   float floatValue();
/*    */   
/*    */   double doubleValue();
/*    */   
/*    */   Number box();
/*    */   
/*    */   default Optional<Number> asNumber() {
/* 22 */     return Optional.of(box());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Byte> asByte() {
/* 27 */     return Optional.of(Byte.valueOf(byteValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Short> asShort() {
/* 32 */     return Optional.of(Short.valueOf(shortValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Integer> asInt() {
/* 37 */     return Optional.of(Integer.valueOf(intValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Long> asLong() {
/* 42 */     return Optional.of(Long.valueOf(longValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Float> asFloat() {
/* 47 */     return Optional.of(Float.valueOf(floatValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Double> asDouble() {
/* 52 */     return Optional.of(Double.valueOf(doubleValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Boolean> asBoolean() {
/* 57 */     return Optional.of(Boolean.valueOf((byteValue() != 0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\NumericTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */