/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class VeryBiasedToBottomHeight extends HeightProvider {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.intRange(1, 2147483647).optionalFieldOf("inner", Integer.valueOf(1)).forGetter(())).apply((Applicative)paramInstance, VeryBiasedToBottomHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<VeryBiasedToBottomHeight> CODEC;
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int inner;
/*    */   
/*    */   private VeryBiasedToBottomHeight(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2, int paramInt) {
/* 27 */     this.minInclusive = paramVerticalAnchor1;
/* 28 */     this.maxInclusive = paramVerticalAnchor2;
/* 29 */     this.inner = paramInt;
/*    */   }
/*    */   
/*    */   public static VeryBiasedToBottomHeight of(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2, int paramInt) {
/* 33 */     return new VeryBiasedToBottomHeight(paramVerticalAnchor1, paramVerticalAnchor2, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext) {
/* 38 */     int i = this.minInclusive.resolveY(paramWorldGenerationContext);
/* 39 */     int j = this.maxInclusive.resolveY(paramWorldGenerationContext);
/* 40 */     if (j - i - this.inner + 1 <= 0) {
/* 41 */       LOGGER.warn("Empty height range: {}", this);
/* 42 */       return i;
/*    */     } 
/*    */     
/* 45 */     int k = Mth.nextInt(paramRandomSource, i + this.inner, j);
/* 46 */     int m = Mth.nextInt(paramRandomSource, i, k - 1);
/* 47 */     return Mth.nextInt(paramRandomSource, i, m - 1 + this.inner);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 52 */     return HeightProviderType.VERY_BIASED_TO_BOTTOM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\VeryBiasedToBottomHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */