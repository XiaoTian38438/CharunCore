/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class NoOpFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public NoOpFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/*  8 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 13 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\NoOpFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */