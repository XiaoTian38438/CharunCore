/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ 
/*    */ public class CanyonCarverConfiguration extends CarverConfiguration {
/*    */   public static final Codec<CanyonCarverConfiguration> CODEC;
/*    */   public final FloatProvider verticalRotation;
/*    */   public final CanyonShapeConfiguration shape;
/*    */   
/*    */   public static class CanyonShapeConfiguration {
/*    */     static {
/* 14 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)FloatProvider.CODEC.fieldOf("distance_factor").forGetter(()), (App)FloatProvider.CODEC.fieldOf("thickness").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("width_smoothness").forGetter(()), (App)FloatProvider.CODEC.fieldOf("horizontal_radius_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(())).apply((Applicative)param1Instance, CanyonShapeConfiguration::new));
/*    */     }
/*    */ 
/*    */     
/*    */     public static final Codec<CanyonShapeConfiguration> CODEC;
/*    */     
/*    */     public final FloatProvider distanceFactor;
/*    */     
/*    */     public final FloatProvider thickness;
/*    */     
/*    */     public final int widthSmoothness;
/*    */     
/*    */     public final FloatProvider horizontalRadiusFactor;
/*    */     public final float verticalRadiusDefaultFactor;
/*    */     public final float verticalRadiusCenterFactor;
/*    */     
/*    */     public CanyonShapeConfiguration(FloatProvider param1FloatProvider1, FloatProvider param1FloatProvider2, int param1Int, FloatProvider param1FloatProvider3, float param1Float1, float param1Float2) {
/* 31 */       this.widthSmoothness = param1Int;
/* 32 */       this.horizontalRadiusFactor = param1FloatProvider3;
/* 33 */       this.verticalRadiusDefaultFactor = param1Float1;
/* 34 */       this.verticalRadiusCenterFactor = param1Float2;
/* 35 */       this.distanceFactor = param1FloatProvider1;
/* 36 */       this.thickness = param1FloatProvider2;
/*    */     } }
/*    */   
/*    */   static {
/* 40 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)CarverConfiguration.CODEC.forGetter(()), (App)FloatProvider.CODEC.fieldOf("vertical_rotation").forGetter(()), (App)CanyonShapeConfiguration.CODEC.fieldOf("shape").forGetter(())).apply((Applicative)paramInstance, CanyonCarverConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CanyonCarverConfiguration(float paramFloat, HeightProvider paramHeightProvider, FloatProvider paramFloatProvider1, VerticalAnchor paramVerticalAnchor, CarverDebugSettings paramCarverDebugSettings, HolderSet<Block> paramHolderSet, FloatProvider paramFloatProvider2, CanyonShapeConfiguration paramCanyonShapeConfiguration) {
/* 50 */     super(paramFloat, paramHeightProvider, paramFloatProvider1, paramVerticalAnchor, paramCarverDebugSettings, paramHolderSet);
/* 51 */     this.verticalRotation = paramFloatProvider2;
/* 52 */     this.shape = paramCanyonShapeConfiguration;
/*    */   }
/*    */   
/*    */   public CanyonCarverConfiguration(CarverConfiguration paramCarverConfiguration, FloatProvider paramFloatProvider, CanyonShapeConfiguration paramCanyonShapeConfiguration) {
/* 56 */     this(paramCarverConfiguration.probability, paramCarverConfiguration.y, paramCarverConfiguration.yScale, paramCarverConfiguration.lavaLevel, paramCarverConfiguration.debugSettings, paramCarverConfiguration.replaceable, paramFloatProvider, paramCanyonShapeConfiguration);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\CanyonCarverConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */