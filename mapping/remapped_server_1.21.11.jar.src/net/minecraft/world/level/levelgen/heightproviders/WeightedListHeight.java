/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedListHeight extends HeightProvider {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeightedList.nonEmptyCodec(HeightProvider.CODEC).fieldOf("distribution").forGetter(())).apply((Applicative)paramInstance, WeightedListHeight::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<WeightedListHeight> CODEC;
/*    */   private final WeightedList<HeightProvider> distribution;
/*    */   
/*    */   public WeightedListHeight(WeightedList<HeightProvider> paramWeightedList) {
/* 17 */     this.distribution = paramWeightedList;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext) {
/* 22 */     return ((HeightProvider)this.distribution.getRandomOrThrow(paramRandomSource)).sample(paramRandomSource, paramWorldGenerationContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 27 */     return HeightProviderType.WEIGHTED_LIST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\WeightedListHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */