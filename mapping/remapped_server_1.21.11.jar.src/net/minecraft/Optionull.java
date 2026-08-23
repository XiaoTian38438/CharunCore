/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Optionull
/*    */ {
/*    */   @Deprecated
/*    */   public static <T> T orElse(T paramT1, T paramT2) {
/* 17 */     return Objects.requireNonNullElse(paramT1, paramT2);
/*    */   }
/*    */   
/*    */   public static <T, R> R map(T paramT, Function<T, R> paramFunction) {
/* 21 */     return (paramT == null) ? null : paramFunction.apply(paramT);
/*    */   }
/*    */   
/*    */   public static <T, R> R mapOrDefault(T paramT, Function<T, R> paramFunction, R paramR) {
/* 25 */     return (paramT == null) ? paramR : paramFunction.apply(paramT);
/*    */   }
/*    */   
/*    */   public static <T, R> R mapOrElse(T paramT, Function<T, R> paramFunction, Supplier<R> paramSupplier) {
/* 29 */     return (paramT == null) ? paramSupplier.get() : paramFunction.apply(paramT);
/*    */   }
/*    */   
/*    */   public static <T> T first(Collection<T> paramCollection) {
/* 33 */     Iterator<T> iterator = paramCollection.iterator();
/* 34 */     return iterator.hasNext() ? iterator.next() : null;
/*    */   }
/*    */   
/*    */   public static <T> T firstOrDefault(Collection<T> paramCollection, T paramT) {
/* 38 */     Iterator<T> iterator = paramCollection.iterator();
/* 39 */     return iterator.hasNext() ? iterator.next() : paramT;
/*    */   }
/*    */   
/*    */   public static <T> T firstOrElse(Collection<T> paramCollection, Supplier<T> paramSupplier) {
/* 43 */     Iterator<T> iterator = paramCollection.iterator();
/* 44 */     return iterator.hasNext() ? iterator.next() : paramSupplier.get();
/*    */   }
/*    */   
/*    */   public static <T> boolean isNullOrEmpty(T[] paramArrayOfT) {
/* 48 */     return (paramArrayOfT == null || paramArrayOfT.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(boolean[] paramArrayOfboolean) {
/* 52 */     return (paramArrayOfboolean == null || paramArrayOfboolean.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(byte[] paramArrayOfbyte) {
/* 56 */     return (paramArrayOfbyte == null || paramArrayOfbyte.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(char[] paramArrayOfchar) {
/* 60 */     return (paramArrayOfchar == null || paramArrayOfchar.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(short[] paramArrayOfshort) {
/* 64 */     return (paramArrayOfshort == null || paramArrayOfshort.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(int[] paramArrayOfint) {
/* 68 */     return (paramArrayOfint == null || paramArrayOfint.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(long[] paramArrayOflong) {
/* 72 */     return (paramArrayOflong == null || paramArrayOflong.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(float[] paramArrayOffloat) {
/* 76 */     return (paramArrayOffloat == null || paramArrayOffloat.length == 0);
/*    */   }
/*    */   
/*    */   public static boolean isNullOrEmpty(double[] paramArrayOfdouble) {
/* 80 */     return (paramArrayOfdouble == null || paramArrayOfdouble.length == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\Optionull.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */