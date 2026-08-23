/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class BlockStateConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<BlockStateConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, paramBlockStateConfiguration -> paramBlockStateConfiguration.state).codec();
/*    */   }
/*    */   public final BlockState state;
/*    */   
/*    */   public BlockStateConfiguration(BlockState paramBlockState) {
/* 12 */     this.state = paramBlockState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockStateConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */