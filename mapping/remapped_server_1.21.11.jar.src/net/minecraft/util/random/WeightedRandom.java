/*    */ package net.minecraft.util.random;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
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
/*    */ public class WeightedRandom
/*    */ {
/*    */   public static <T> int getTotalWeight(List<T> paramList, ToIntFunction<T> paramToIntFunction) {
/*    */     // Byte code:
/*    */     //   0: lconst_0
/*    */     //   1: lstore_2
/*    */     //   2: aload_0
/*    */     //   3: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */     //   8: astore #4
/*    */     //   10: aload #4
/*    */     //   12: invokeinterface hasNext : ()Z
/*    */     //   17: ifeq -> 44
/*    */     //   20: aload #4
/*    */     //   22: invokeinterface next : ()Ljava/lang/Object;
/*    */     //   27: astore #5
/*    */     //   29: lload_2
/*    */     //   30: aload_1
/*    */     //   31: aload #5
/*    */     //   33: invokeinterface applyAsInt : (Ljava/lang/Object;)I
/*    */     //   38: i2l
/*    */     //   39: ladd
/*    */     //   40: lstore_2
/*    */     //   41: goto -> 10
/*    */     //   44: lload_2
/*    */     //   45: ldc2_w 2147483647
/*    */     //   48: lcmp
/*    */     //   49: ifle -> 62
/*    */     //   52: new java/lang/IllegalArgumentException
/*    */     //   55: dup
/*    */     //   56: ldc 'Sum of weights must be <= 2147483647'
/*    */     //   58: invokespecial <init> : (Ljava/lang/String;)V
/*    */     //   61: athrow
/*    */     //   62: lload_2
/*    */     //   63: l2i
/*    */     //   64: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     //   #16	-> 2
/*    */     //   #17	-> 29
/*    */     //   #18	-> 41
/*    */     //   #20	-> 44
/*    */     //   #21	-> 52
/*    */     //   #23	-> 62
/*    */   }
/*    */   
/*    */   public static <T> Optional<T> getRandomItem(RandomSource paramRandomSource, List<T> paramList, int paramInt, ToIntFunction<T> paramToIntFunction) {
/* 27 */     if (paramInt < 0) {
/* 28 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Negative total weight in getRandomItem"));
/*    */     }
/*    */     
/* 31 */     if (paramInt == 0) {
/* 32 */       return Optional.empty();
/*    */     }
/*    */     
/* 35 */     int i = paramRandomSource.nextInt(paramInt);
/* 36 */     return getWeightedItem(paramList, i, paramToIntFunction);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Optional<T> getWeightedItem(List<T> paramList, int paramInt, ToIntFunction<T> paramToIntFunction) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */     //   6: astore_3
/*    */     //   7: aload_3
/*    */     //   8: invokeinterface hasNext : ()Z
/*    */     //   13: ifeq -> 48
/*    */     //   16: aload_3
/*    */     //   17: invokeinterface next : ()Ljava/lang/Object;
/*    */     //   22: astore #4
/*    */     //   24: iload_1
/*    */     //   25: aload_2
/*    */     //   26: aload #4
/*    */     //   28: invokeinterface applyAsInt : (Ljava/lang/Object;)I
/*    */     //   33: isub
/*    */     //   34: istore_1
/*    */     //   35: iload_1
/*    */     //   36: ifge -> 45
/*    */     //   39: aload #4
/*    */     //   41: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*    */     //   44: areturn
/*    */     //   45: goto -> 7
/*    */     //   48: invokestatic empty : ()Ljava/util/Optional;
/*    */     //   51: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     //   #41	-> 24
/*    */     //   #42	-> 35
/*    */     //   #43	-> 39
/*    */     //   #45	-> 45
/*    */     //   #46	-> 48
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Optional<T> getRandomItem(RandomSource paramRandomSource, List<T> paramList, ToIntFunction<T> paramToIntFunction) {
/* 50 */     return getRandomItem(paramRandomSource, paramList, getTotalWeight(paramList, paramToIntFunction), paramToIntFunction);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\random\WeightedRandom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */