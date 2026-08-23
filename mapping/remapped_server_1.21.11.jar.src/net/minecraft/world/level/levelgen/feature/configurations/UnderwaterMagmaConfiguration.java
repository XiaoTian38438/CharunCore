/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class UnderwaterMagmaConfiguration implements FeatureConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.intRange(0, 512).fieldOf("floor_search_range").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("placement_radius_around_floor").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("placement_probability_per_valid_position").forGetter(())).apply((Applicative)paramInstance, UnderwaterMagmaConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<UnderwaterMagmaConfiguration> CODEC;
/*    */   
/*    */   public final int floorSearchRange;
/*    */   public final int placementRadiusAroundFloor;
/*    */   public final float placementProbabilityPerValidPosition;
/*    */   
/*    */   public UnderwaterMagmaConfiguration(int paramInt1, int paramInt2, float paramFloat) {
/* 18 */     this.floorSearchRange = paramInt1;
/* 19 */     this.placementRadiusAroundFloor = paramInt2;
/* 20 */     this.placementProbabilityPerValidPosition = paramFloat;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\UnderwaterMagmaConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */