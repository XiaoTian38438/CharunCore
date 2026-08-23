/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class BiasedToBottomHeight extends HeightProvider {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.intRange(1, 2147483647).optionalFieldOf("inner", Integer.valueOf(1)).forGetter(())).apply((Applicative)paramInstance, BiasedToBottomHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BiasedToBottomHeight> CODEC;
/*    */   
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int inner;
/*    */   
/*    */   private BiasedToBottomHeight(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2, int paramInt) {
/* 26 */     this.minInclusive = paramVerticalAnchor1;
/* 27 */     this.maxInclusive = paramVerticalAnchor2;
/* 28 */     this.inner = paramInt;
/*    */   }
/*    */   
/*    */   public static BiasedToBottomHeight of(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2, int paramInt) {
/* 32 */     return new BiasedToBottomHeight(paramVerticalAnchor1, paramVerticalAnchor2, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext) {
/* 37 */     int i = this.minInclusive.resolveY(paramWorldGenerationContext);
/* 38 */     int j = this.maxInclusive.resolveY(paramWorldGenerationContext);
/* 39 */     if (j - i - this.inner + 1 <= 0) {
/* 40 */       LOGGER.warn("Empty height range: {}", this);
/* 41 */       return i;
/*    */     } 
/*    */     
/* 44 */     int k = paramRandomSource.nextInt(j - i - this.inner + 1);
/* 45 */     return paramRandomSource.nextInt(k + this.inner) + i;
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 50 */     return HeightProviderType.BIASED_TO_BOTTOM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\BiasedToBottomHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */