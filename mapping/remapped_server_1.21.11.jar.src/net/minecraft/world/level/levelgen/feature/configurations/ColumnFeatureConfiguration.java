/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class ColumnFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)IntProvider.codec(0, 3).fieldOf("reach").forGetter(()), (App)IntProvider.codec(1, 10).fieldOf("height").forGetter(())).apply((Applicative)paramInstance, ColumnFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ColumnFeatureConfiguration> CODEC;
/*    */   private final IntProvider reach;
/*    */   private final IntProvider height;
/*    */   
/*    */   public ColumnFeatureConfiguration(IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/* 17 */     this.reach = paramIntProvider1;
/* 18 */     this.height = paramIntProvider2;
/*    */   }
/*    */   
/*    */   public IntProvider reach() {
/* 22 */     return this.reach;
/*    */   }
/*    */   
/*    */   public IntProvider height() {
/* 26 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\ColumnFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */