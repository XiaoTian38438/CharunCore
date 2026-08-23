package net.minecraft.world.level.storage.loot.predicates;

import java.util.function.Function;

public interface ConditionUserBuilder<T extends ConditionUserBuilder<T>> {
  T when(LootItemCondition.Builder paramBuilder);
  
  default <E> T when(Iterable<E> paramIterable, Function<E, LootItemCondition.Builder> paramFunction) {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface unwrap : ()Lnet/minecraft/world/level/storage/loot/predicates/ConditionUserBuilder;
    //   6: astore_3
    //   7: aload_1
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore #4
    //   15: aload #4
    //   17: invokeinterface hasNext : ()Z
    //   22: ifeq -> 55
    //   25: aload #4
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: astore #5
    //   34: aload_3
    //   35: aload_2
    //   36: aload #5
    //   38: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
    //   43: checkcast net/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder
    //   46: invokeinterface when : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/ConditionUserBuilder;
    //   51: astore_3
    //   52: goto -> 15
    //   55: aload_3
    //   56: areturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #9	-> 0
    //   #10	-> 7
    //   #11	-> 34
    //   #12	-> 52
    //   #13	-> 55
  }
  
  T unwrap();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\ConditionUserBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */