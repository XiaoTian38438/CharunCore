/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class TwoLayersFeatureSize extends FeatureSize {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.intRange(0, 81).fieldOf("limit").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(Integer.valueOf(1)).forGetter(()), (App)minClippedHeightCodec()).apply((Applicative)paramInstance, TwoLayersFeatureSize::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<TwoLayersFeatureSize> CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int upperSize;
/*    */   
/*    */   public TwoLayersFeatureSize(int paramInt1, int paramInt2, int paramInt3) {
/* 25 */     this(paramInt1, paramInt2, paramInt3, OptionalInt.empty());
/*    */   }
/*    */   
/*    */   public TwoLayersFeatureSize(int paramInt1, int paramInt2, int paramInt3, OptionalInt paramOptionalInt) {
/* 29 */     super(paramOptionalInt);
/* 30 */     this.limit = paramInt1;
/* 31 */     this.lowerSize = paramInt2;
/* 32 */     this.upperSize = paramInt3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FeatureSizeType<?> type() {
/* 37 */     return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int paramInt1, int paramInt2) {
/* 42 */     return (paramInt2 < this.limit) ? this.lowerSize : this.upperSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\featuresize\TwoLayersFeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */