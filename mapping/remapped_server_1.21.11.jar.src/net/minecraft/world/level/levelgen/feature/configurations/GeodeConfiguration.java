/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*    */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*    */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*    */ 
/*    */ public class GeodeConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0D, 1.0D); public static final Codec<GeodeConfiguration> CODEC;
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)GeodeBlockSettings.CODEC.fieldOf("blocks").forGetter(()), (App)GeodeLayerSettings.CODEC.fieldOf("layers").forGetter(()), (App)GeodeCrackSettings.CODEC.fieldOf("crack").forGetter(()), (App)CHANCE_RANGE.fieldOf("use_potential_placements_chance").orElse(Double.valueOf(0.35D)).forGetter(()), (App)CHANCE_RANGE.fieldOf("use_alternate_layer0_chance").orElse(Double.valueOf(0.0D)).forGetter(()), (App)Codec.BOOL.fieldOf("placements_require_layer0_alternate").orElse(Boolean.valueOf(true)).forGetter(()), (App)IntProvider.codec(1, 20).fieldOf("outer_wall_distance").orElse(UniformInt.of(4, 5)).forGetter(()), (App)IntProvider.codec(1, 20).fieldOf("distribution_points").orElse(UniformInt.of(3, 4)).forGetter(()), (App)IntProvider.codec(0, 10).fieldOf("point_offset").orElse(UniformInt.of(1, 2)).forGetter(()), (App)Codec.INT.fieldOf("min_gen_offset").orElse(Integer.valueOf(-16)).forGetter(()), (App)Codec.INT.fieldOf("max_gen_offset").orElse(Integer.valueOf(16)).forGetter(()), (App)CHANCE_RANGE.fieldOf("noise_multiplier").orElse(Double.valueOf(0.05D)).forGetter(()), (App)Codec.INT.fieldOf("invalid_blocks_threshold").forGetter(())).apply((Applicative)paramInstance, GeodeConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final GeodeBlockSettings geodeBlockSettings;
/*    */ 
/*    */   
/*    */   public final GeodeLayerSettings geodeLayerSettings;
/*    */ 
/*    */   
/*    */   public final GeodeCrackSettings geodeCrackSettings;
/*    */ 
/*    */   
/*    */   public final double usePotentialPlacementsChance;
/*    */ 
/*    */   
/*    */   public final double useAlternateLayer0Chance;
/*    */ 
/*    */   
/*    */   public final boolean placementsRequireLayer0Alternate;
/*    */ 
/*    */   
/*    */   public final IntProvider outerWallDistance;
/*    */ 
/*    */   
/*    */   public final IntProvider distributionPoints;
/*    */ 
/*    */   
/*    */   public final IntProvider pointOffset;
/*    */   
/*    */   public final int minGenOffset;
/*    */   
/*    */   public final int maxGenOffset;
/*    */   
/*    */   public final double noiseMultiplier;
/*    */   
/*    */   public final int invalidBlocksThreshold;
/*    */ 
/*    */   
/*    */   public GeodeConfiguration(GeodeBlockSettings paramGeodeBlockSettings, GeodeLayerSettings paramGeodeLayerSettings, GeodeCrackSettings paramGeodeCrackSettings, double paramDouble1, double paramDouble2, boolean paramBoolean, IntProvider paramIntProvider1, IntProvider paramIntProvider2, IntProvider paramIntProvider3, int paramInt1, int paramInt2, double paramDouble3, int paramInt3) {
/* 55 */     this.geodeBlockSettings = paramGeodeBlockSettings;
/* 56 */     this.geodeLayerSettings = paramGeodeLayerSettings;
/* 57 */     this.geodeCrackSettings = paramGeodeCrackSettings;
/* 58 */     this.usePotentialPlacementsChance = paramDouble1;
/* 59 */     this.useAlternateLayer0Chance = paramDouble2;
/* 60 */     this.placementsRequireLayer0Alternate = paramBoolean;
/* 61 */     this.outerWallDistance = paramIntProvider1;
/* 62 */     this.distributionPoints = paramIntProvider2;
/* 63 */     this.pointOffset = paramIntProvider3;
/* 64 */     this.minGenOffset = paramInt1;
/* 65 */     this.maxGenOffset = paramInt2;
/* 66 */     this.noiseMultiplier = paramDouble3;
/* 67 */     this.invalidBlocksThreshold = paramInt3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\GeodeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */