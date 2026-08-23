/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class BlockPileConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<BlockPileConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, paramBlockPileConfiguration -> paramBlockPileConfiguration.stateProvider).codec();
/*    */   }
/*    */   public final BlockStateProvider stateProvider;
/*    */   
/*    */   public BlockPileConfiguration(BlockStateProvider paramBlockStateProvider) {
/* 12 */     this.stateProvider = paramBlockStateProvider;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockPileConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */