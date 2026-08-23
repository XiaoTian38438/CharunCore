/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class FeatureSizeType<P extends FeatureSize> {
/*  8 */   public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE = register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);
/*  9 */   public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE = register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);
/*    */   
/*    */   private static <P extends FeatureSize> FeatureSizeType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 12 */     return (FeatureSizeType<P>)Registry.register(BuiltInRegistries.FEATURE_SIZE_TYPE, paramString, new FeatureSizeType<>(paramMapCodec));
/*    */   }
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private FeatureSizeType(MapCodec<P> paramMapCodec) {
/* 18 */     this.codec = paramMapCodec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 22 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\featuresize\FeatureSizeType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */