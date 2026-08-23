/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import com.google.common.math.Quantiles;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleRBTreeMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMaps;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Percentiles
/*    */ {
/* 13 */   public static final Quantiles.ScaleAndIndexes DEFAULT_INDEXES = Quantiles.scale(100).indexes(new int[] { 50, 75, 90, 99 });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Map<Integer, Double> evaluate(long[] paramArrayOflong) {
/* 19 */     return (paramArrayOflong.length == 0) ? Map.<Integer, Double>of() : sorted(DEFAULT_INDEXES.compute(paramArrayOflong));
/*    */   }
/*    */   
/*    */   public static Map<Integer, Double> evaluate(int[] paramArrayOfint) {
/* 23 */     return (paramArrayOfint.length == 0) ? Map.<Integer, Double>of() : sorted(DEFAULT_INDEXES.compute(paramArrayOfint));
/*    */   }
/*    */   
/*    */   public static Map<Integer, Double> evaluate(double[] paramArrayOfdouble) {
/* 27 */     return (paramArrayOfdouble.length == 0) ? Map.<Integer, Double>of() : sorted(DEFAULT_INDEXES.compute(paramArrayOfdouble));
/*    */   }
/*    */   
/*    */   private static Map<Integer, Double> sorted(Map<Integer, Double> paramMap) {
/* 31 */     Int2DoubleSortedMap int2DoubleSortedMap = (Int2DoubleSortedMap)Util.make(new Int2DoubleRBTreeMap(Comparator.reverseOrder()), paramInt2DoubleRBTreeMap -> paramInt2DoubleRBTreeMap.putAll(paramMap));
/* 32 */     return (Map<Integer, Double>)Int2DoubleSortedMaps.unmodifiable(int2DoubleSortedMap);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\Percentiles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */