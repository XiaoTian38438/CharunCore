/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Streams;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameTestBatchFactory
/*    */ {
/*    */   private static final int MAX_TESTS_PER_BATCH = 50;
/*    */   public static final TestDecorator DIRECT;
/*    */   
/*    */   static {
/* 24 */     DIRECT = ((paramReference, paramServerLevel) -> Stream.of(new GameTestInfo(paramReference, Rotation.NONE, paramServerLevel, RetryOptions.noRetries())));
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<GameTestBatch> divideIntoBatches(Collection<Holder.Reference<GameTestInstance>> paramCollection, TestDecorator paramTestDecorator, ServerLevel paramServerLevel) {
/* 29 */     Map map = (Map)paramCollection.stream().flatMap(paramReference -> paramTestDecorator.decorate(paramReference, paramServerLevel)).collect(Collectors.groupingBy(paramGameTestInfo -> paramGameTestInfo.getTest().batch()));
/*    */     
/* 31 */     return map.entrySet().stream().flatMap(paramEntry -> {
/*    */           Holder holder = (Holder)paramEntry.getKey();
/*    */ 
/*    */           
/*    */           List list = (List)paramEntry.getValue();
/*    */           
/*    */           return Streams.mapWithIndex(Lists.partition(list, 50).stream(), ());
/* 38 */         }).toList();
/*    */   }
/*    */   
/*    */   public static GameTestRunner.GameTestBatcher fromGameTestInfo() {
/* 42 */     return fromGameTestInfo(50);
/*    */   }
/*    */   
/*    */   public static GameTestRunner.GameTestBatcher fromGameTestInfo(int paramInt) {
/* 46 */     return paramCollection -> {
/*    */         Map map = (Map)paramCollection.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(()));
/*    */         return map.entrySet().stream().flatMap(()).toList();
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static GameTestBatch toGameTestBatch(Collection<GameTestInfo> paramCollection, Holder<TestEnvironmentDefinition> paramHolder, int paramInt) {
/* 61 */     return new GameTestBatch(paramInt, paramCollection, paramHolder);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TestDecorator {
/*    */     Stream<GameTestInfo> decorate(Holder.Reference<GameTestInstance> param1Reference, ServerLevel param1ServerLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestBatchFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */