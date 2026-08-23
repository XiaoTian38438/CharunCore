/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class ProbabilityFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(())).apply((Applicative)paramInstance, ProbabilityFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final Codec<ProbabilityFeatureConfiguration> CODEC;
/*    */   public final float probability;
/*    */   
/*    */   public ProbabilityFeatureConfiguration(float paramFloat) {
/* 14 */     this.probability = paramFloat;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\ProbabilityFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */