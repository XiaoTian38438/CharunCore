/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class InactiveProfiler
/*    */   implements ProfileCollector
/*    */ {
/* 12 */   public static final InactiveProfiler INSTANCE = new InactiveProfiler();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startTick() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void endTick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(String paramString) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(Supplier<String> paramSupplier) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void markForCharting(MetricCategory paramMetricCategory) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void pop() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(String paramString) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(Supplier<String> paramSupplier) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Zone zone(String paramString) {
/* 51 */     return Zone.INACTIVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Zone zone(Supplier<String> paramSupplier) {
/* 56 */     return Zone.INACTIVE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(String paramString, int paramInt) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(Supplier<String> paramSupplier, int paramInt) {}
/*    */ 
/*    */   
/*    */   public ProfileResults getResults() {
/* 69 */     return EmptyProfileResults.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public ActiveProfiler.PathEntry getEntry(String paramString) {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Pair<String, MetricCategory>> getChartedPaths() {
/* 79 */     return (Set<Pair<String, MetricCategory>>)ImmutableSet.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\InactiveProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */