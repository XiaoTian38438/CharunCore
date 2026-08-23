/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ 
/*    */ public class CanyonShapeConfiguration
/*    */ {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)FloatProvider.CODEC.fieldOf("distance_factor").forGetter(()), (App)FloatProvider.CODEC.fieldOf("thickness").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("width_smoothness").forGetter(()), (App)FloatProvider.CODEC.fieldOf("horizontal_radius_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(())).apply((Applicative)paramInstance, CanyonShapeConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<CanyonShapeConfiguration> CODEC;
/*    */   
/*    */   public final FloatProvider distanceFactor;
/*    */   
/*    */   public final FloatProvider thickness;
/*    */   
/*    */   public final int widthSmoothness;
/*    */   
/*    */   public final FloatProvider horizontalRadiusFactor;
/*    */   public final float verticalRadiusDefaultFactor;
/*    */   public final float verticalRadiusCenterFactor;
/*    */   
/*    */   public CanyonShapeConfiguration(FloatProvider paramFloatProvider1, FloatProvider paramFloatProvider2, int paramInt, FloatProvider paramFloatProvider3, float paramFloat1, float paramFloat2) {
/* 31 */     this.widthSmoothness = paramInt;
/* 32 */     this.horizontalRadiusFactor = paramFloatProvider3;
/* 33 */     this.verticalRadiusDefaultFactor = paramFloat1;
/* 34 */     this.verticalRadiusCenterFactor = paramFloat2;
/* 35 */     this.distanceFactor = paramFloatProvider1;
/* 36 */     this.thickness = paramFloatProvider2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\carver\CanyonCarverConfiguration$CanyonShapeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */