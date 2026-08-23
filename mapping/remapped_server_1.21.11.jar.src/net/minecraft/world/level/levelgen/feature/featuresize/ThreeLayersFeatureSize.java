/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class ThreeLayersFeatureSize extends FeatureSize {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.intRange(0, 80).fieldOf("limit").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 80).fieldOf("upper_limit").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("middle_size").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(Integer.valueOf(1)).forGetter(()), (App)minClippedHeightCodec()).apply((Applicative)paramInstance, ThreeLayersFeatureSize::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ThreeLayersFeatureSize> CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int upperLimit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int middleSize;
/*    */   private final int upperSize;
/*    */   
/*    */   public ThreeLayersFeatureSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, OptionalInt paramOptionalInt) {
/* 27 */     super(paramOptionalInt);
/* 28 */     this.limit = paramInt1;
/* 29 */     this.upperLimit = paramInt2;
/* 30 */     this.lowerSize = paramInt3;
/* 31 */     this.middleSize = paramInt4;
/* 32 */     this.upperSize = paramInt5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FeatureSizeType<?> type() {
/* 37 */     return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int paramInt1, int paramInt2) {
/* 42 */     if (paramInt2 < this.limit) {
/* 43 */       return this.lowerSize;
/*    */     }
/* 45 */     if (paramInt2 >= paramInt1 - this.upperLimit) {
/* 46 */       return this.upperSize;
/*    */     }
/* 48 */     return this.middleSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\featuresize\ThreeLayersFeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */