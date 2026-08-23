/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface FunctionUserBuilder<T extends FunctionUserBuilder<T>>
/*    */ {
/*    */   T apply(LootItemFunction.Builder paramBuilder);
/*    */   
/*    */   default <E> T apply(Iterable<E> paramIterable, Function<E, LootItemFunction.Builder> paramFunction) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokeinterface unwrap : ()Lnet/minecraft/world/level/storage/loot/functions/FunctionUserBuilder;
/*    */     //   6: astore_3
/*    */     //   7: aload_1
/*    */     //   8: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */     //   13: astore #4
/*    */     //   15: aload #4
/*    */     //   17: invokeinterface hasNext : ()Z
/*    */     //   22: ifeq -> 55
/*    */     //   25: aload #4
/*    */     //   27: invokeinterface next : ()Ljava/lang/Object;
/*    */     //   32: astore #5
/*    */     //   34: aload_3
/*    */     //   35: aload_2
/*    */     //   36: aload #5
/*    */     //   38: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   43: checkcast net/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder
/*    */     //   46: invokeinterface apply : (Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/functions/FunctionUserBuilder;
/*    */     //   51: astore_3
/*    */     //   52: goto -> 15
/*    */     //   55: aload_3
/*    */     //   56: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     //   #11	-> 7
/*    */     //   #12	-> 34
/*    */     //   #13	-> 52
/*    */     //   #14	-> 55
/*    */   }
/*    */   
/*    */   default <E> T apply(E[] paramArrayOfE, Function<E, LootItemFunction.Builder> paramFunction) {
/* 18 */     return apply(Arrays.asList(paramArrayOfE), paramFunction);
/*    */   }
/*    */   
/*    */   T unwrap();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\FunctionUserBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */