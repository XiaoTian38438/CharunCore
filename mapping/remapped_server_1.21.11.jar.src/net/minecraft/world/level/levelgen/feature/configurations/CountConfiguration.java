/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class CountConfiguration implements FeatureConfiguration {
/*  8 */   public static final Codec<CountConfiguration> CODEC = IntProvider.codec(0, 256).fieldOf("count")
/*  9 */     .xmap(CountConfiguration::new, CountConfiguration::count).codec();
/*    */   
/*    */   private final IntProvider count;
/*    */   
/*    */   public CountConfiguration(int paramInt) {
/* 14 */     this.count = (IntProvider)ConstantInt.of(paramInt);
/*    */   }
/*    */   
/*    */   public CountConfiguration(IntProvider paramIntProvider) {
/* 18 */     this.count = paramIntProvider;
/*    */   }
/*    */   
/*    */   public IntProvider count() {
/* 22 */     return this.count;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\CountConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */