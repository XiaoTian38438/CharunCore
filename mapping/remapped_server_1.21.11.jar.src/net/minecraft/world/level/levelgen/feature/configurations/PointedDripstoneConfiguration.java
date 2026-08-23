/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class PointedDripstoneConfiguration implements FeatureConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_dripstone").orElse(Float.valueOf(0.2F)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(Float.valueOf(0.7F)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(Float.valueOf(0.5F)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(Float.valueOf(0.5F)).forGetter(())).apply((Applicative)paramInstance, PointedDripstoneConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<PointedDripstoneConfiguration> CODEC;
/*    */   
/*    */   public final float chanceOfTallerDripstone;
/*    */   
/*    */   public final float chanceOfDirectionalSpread;
/*    */   
/*    */   public final float chanceOfSpreadRadius2;
/*    */   
/*    */   public final float chanceOfSpreadRadius3;
/*    */ 
/*    */   
/*    */   public PointedDripstoneConfiguration(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 24 */     this.chanceOfTallerDripstone = paramFloat1;
/* 25 */     this.chanceOfDirectionalSpread = paramFloat2;
/* 26 */     this.chanceOfSpreadRadius2 = paramFloat3;
/* 27 */     this.chanceOfSpreadRadius3 = paramFloat4;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\PointedDripstoneConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */