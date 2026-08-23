/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedListInt extends IntProvider {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeightedList.nonEmptyCodec(IntProvider.CODEC).fieldOf("distribution").forGetter(())).apply((Applicative)paramInstance, WeightedListInt::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<WeightedListInt> CODEC;
/*    */   private final WeightedList<IntProvider> distribution;
/*    */   private final int minValue;
/*    */   private final int maxValue;
/*    */   
/*    */   public WeightedListInt(WeightedList<IntProvider> paramWeightedList) {
/* 19 */     this.distribution = paramWeightedList;
/* 20 */     int i = Integer.MAX_VALUE;
/* 21 */     int j = Integer.MIN_VALUE;
/* 22 */     for (Weighted weighted : paramWeightedList.unwrap()) {
/* 23 */       int k = ((IntProvider)weighted.value()).getMinValue();
/* 24 */       int m = ((IntProvider)weighted.value()).getMaxValue();
/* 25 */       i = Math.min(i, k);
/* 26 */       j = Math.max(j, m);
/*    */     } 
/* 28 */     this.minValue = i;
/* 29 */     this.maxValue = j;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource) {
/* 34 */     return ((IntProvider)this.distribution.getRandomOrThrow(paramRandomSource)).sample(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 39 */     return this.minValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 44 */     return this.maxValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 49 */     return IntProviderType.WEIGHTED_LIST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\valueproviders\WeightedListInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */