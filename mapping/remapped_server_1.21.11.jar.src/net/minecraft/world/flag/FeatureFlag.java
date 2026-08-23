/*   */ package net.minecraft.world.flag;
/*   */ 
/*   */ public class FeatureFlag {
/*   */   final FeatureFlagUniverse universe;
/*   */   final long mask;
/*   */   
/*   */   FeatureFlag(FeatureFlagUniverse paramFeatureFlagUniverse, int paramInt) {
/* 8 */     this.universe = paramFeatureFlagUniverse;
/* 9 */     this.mask = 1L << paramInt;
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\flag\FeatureFlag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */