/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ 
/*    */ public class LargeDripstoneConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(Integer.valueOf(30)).forGetter(()), (App)IntProvider.codec(1, 60).fieldOf("column_radius").forGetter(()), (App)FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(()), (App)Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(()), (App)FloatProvider.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(()), (App)FloatProvider.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(()), (App)FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(()), (App)Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(()), (App)Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(())).apply((Applicative)paramInstance, LargeDripstoneConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<LargeDripstoneConfiguration> CODEC;
/*    */ 
/*    */   
/*    */   public final int floorToCeilingSearchRange;
/*    */ 
/*    */   
/*    */   public final IntProvider columnRadius;
/*    */ 
/*    */   
/*    */   public final FloatProvider heightScale;
/*    */ 
/*    */   
/*    */   public final float maxColumnRadiusToCaveHeightRatio;
/*    */ 
/*    */   
/*    */   public final FloatProvider stalactiteBluntness;
/*    */ 
/*    */   
/*    */   public final FloatProvider stalagmiteBluntness;
/*    */ 
/*    */   
/*    */   public final FloatProvider windSpeed;
/*    */ 
/*    */   
/*    */   public final int minRadiusForWind;
/*    */   
/*    */   public final float minBluntnessForWind;
/*    */ 
/*    */   
/*    */   public LargeDripstoneConfiguration(int paramInt1, IntProvider paramIntProvider, FloatProvider paramFloatProvider1, float paramFloat1, FloatProvider paramFloatProvider2, FloatProvider paramFloatProvider3, FloatProvider paramFloatProvider4, int paramInt2, float paramFloat2) {
/* 44 */     this.floorToCeilingSearchRange = paramInt1;
/* 45 */     this.columnRadius = paramIntProvider;
/* 46 */     this.heightScale = paramFloatProvider1;
/* 47 */     this.maxColumnRadiusToCaveHeightRatio = paramFloat1;
/* 48 */     this.stalactiteBluntness = paramFloatProvider2;
/* 49 */     this.stalagmiteBluntness = paramFloatProvider3;
/* 50 */     this.windSpeed = paramFloatProvider4;
/* 51 */     this.minRadiusForWind = paramInt2;
/* 52 */     this.minBluntnessForWind = paramFloat2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\LargeDripstoneConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */