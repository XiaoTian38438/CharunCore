/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class DripstoneClusterConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(()), (App)IntProvider.codec(1, 128).fieldOf("height").forGetter(()), (App)IntProvider.codec(1, 128).fieldOf("radius").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(()), (App)IntProvider.codec(0, 128).fieldOf("dripstone_block_layer_thickness").forGetter(()), (App)FloatProvider.codec(0.0F, 2.0F).fieldOf("density").forGetter(()), (App)FloatProvider.codec(0.0F, 2.0F).fieldOf("wetness").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_dripstone_column_at_max_distance_from_center").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_dripstone_column").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter(())).apply((Applicative)paramInstance, DripstoneClusterConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<DripstoneClusterConfiguration> CODEC;
/*    */ 
/*    */   
/*    */   public final int floorToCeilingSearchRange;
/*    */   
/*    */   public final IntProvider height;
/*    */   
/*    */   public final IntProvider radius;
/*    */   
/*    */   public final int maxStalagmiteStalactiteHeightDiff;
/*    */   
/*    */   public final int heightDeviation;
/*    */   
/*    */   public final IntProvider dripstoneBlockLayerThickness;
/*    */   
/*    */   public final FloatProvider density;
/*    */   
/*    */   public final FloatProvider wetness;
/*    */   
/*    */   public final float chanceOfDripstoneColumnAtMaxDistanceFromCenter;
/*    */   
/*    */   public final int maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;
/*    */   
/*    */   public final int maxDistanceFromCenterAffectingHeightBias;
/*    */ 
/*    */   
/*    */   public DripstoneClusterConfiguration(int paramInt1, IntProvider paramIntProvider1, IntProvider paramIntProvider2, int paramInt2, int paramInt3, IntProvider paramIntProvider3, FloatProvider paramFloatProvider1, FloatProvider paramFloatProvider2, float paramFloat, int paramInt4, int paramInt5) {
/* 42 */     this.floorToCeilingSearchRange = paramInt1;
/* 43 */     this.height = paramIntProvider1;
/* 44 */     this.radius = paramIntProvider2;
/* 45 */     this.maxStalagmiteStalactiteHeightDiff = paramInt2;
/* 46 */     this.heightDeviation = paramInt3;
/* 47 */     this.dripstoneBlockLayerThickness = paramIntProvider3;
/* 48 */     this.density = paramFloatProvider1;
/* 49 */     this.wetness = paramFloatProvider2;
/* 50 */     this.chanceOfDripstoneColumnAtMaxDistanceFromCenter = paramFloat;
/* 51 */     this.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn = paramInt4;
/* 52 */     this.maxDistanceFromCenterAffectingHeightBias = paramInt5;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\DripstoneClusterConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */