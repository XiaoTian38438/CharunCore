/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataFixUtils
/*    */ {
/*    */   public static int smallestEncompassingPowerOfTwo(int paramInt) {
/* 17 */     int i = paramInt - 1;
/* 18 */     i |= i >> 1;
/* 19 */     i |= i >> 2;
/* 20 */     i |= i >> 4;
/* 21 */     i |= i >> 8;
/* 22 */     i |= i >> 16;
/* 23 */     return i + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   private static boolean isPowerOfTwo(int paramInt) {
/* 28 */     return (paramInt != 0 && (paramInt & paramInt - 1) == 0);
/*    */   }
/*    */ 
/*    */   
/* 32 */   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
/*    */ 
/*    */ 
/*    */   
/*    */   public static int ceillog2(int paramInt) {
/* 37 */     paramInt = isPowerOfTwo(paramInt) ? paramInt : smallestEncompassingPowerOfTwo(paramInt);
/* 38 */     return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(paramInt * 125613361L >> 27L) & 0x1F];
/*    */   }
/*    */   
/*    */   public static <T> T make(Supplier<T> paramSupplier) {
/* 42 */     return paramSupplier.get();
/*    */   }
/*    */   
/*    */   public static <T> T make(T paramT, Consumer<T> paramConsumer) {
/* 46 */     paramConsumer.accept(paramT);
/* 47 */     return paramT;
/*    */   }
/*    */   
/*    */   public static <U> U orElse(Optional<? extends U> paramOptional, U paramU) {
/* 51 */     if (paramOptional.isPresent()) {
/* 52 */       return paramOptional.get();
/*    */     }
/* 54 */     return paramU;
/*    */   }
/*    */   
/*    */   public static <U> U orElseGet(Optional<? extends U> paramOptional, Supplier<? extends U> paramSupplier) {
/* 58 */     if (paramOptional.isPresent()) {
/* 59 */       return paramOptional.get();
/*    */     }
/* 61 */     return paramSupplier.get();
/*    */   }
/*    */   
/*    */   public static <U> Optional<U> or(Optional<? extends U> paramOptional, Supplier<? extends Optional<? extends U>> paramSupplier) {
/* 65 */     if (paramOptional.isPresent()) {
/* 66 */       return paramOptional.map(paramObject -> paramObject);
/*    */     }
/* 68 */     return ((Optional)paramSupplier.get()).map(paramObject -> paramObject);
/*    */   }
/*    */   
/*    */   public static byte[] toArray(ByteBuffer paramByteBuffer) {
/*    */     byte[] arrayOfByte;
/* 73 */     if (paramByteBuffer.hasArray()) {
/* 74 */       arrayOfByte = paramByteBuffer.array();
/*    */     } else {
/* 76 */       arrayOfByte = new byte[paramByteBuffer.capacity()];
/* 77 */       paramByteBuffer.get(arrayOfByte, 0, arrayOfByte.length);
/*    */     } 
/* 79 */     return arrayOfByte;
/*    */   }
/*    */   
/*    */   public static int makeKey(int paramInt) {
/* 83 */     return makeKey(paramInt, 0);
/*    */   }
/*    */   
/*    */   public static int makeKey(int paramInt1, int paramInt2) {
/* 87 */     return paramInt1 * 10 + paramInt2;
/*    */   }
/*    */   
/*    */   public static int getVersion(int paramInt) {
/* 91 */     return paramInt / 10;
/*    */   }
/*    */   
/*    */   public static int getSubVersion(int paramInt) {
/* 95 */     return paramInt % 10;
/*    */   }
/*    */   
/*    */   public static <T> UnaryOperator<T> consumerToFunction(Consumer<T> paramConsumer) {
/* 99 */     return paramObject -> {
/*    */         paramConsumer.accept(paramObject);
/*    */         return paramObject;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFixUtils.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */