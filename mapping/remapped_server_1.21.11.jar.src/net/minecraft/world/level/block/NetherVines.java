/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class NetherVines {
/*    */   private static final double BONEMEAL_GROW_PROBABILITY_DECREASE_RATE = 0.826D;
/*    */   public static final double GROW_PER_TICK_PROBABILITY = 0.1D;
/*    */   
/*    */   public static boolean isValidGrowthState(BlockState paramBlockState) {
/* 11 */     return paramBlockState.isAir();
/*    */   }
/*    */   
/*    */   public static int getBlocksToGrowWhenBonemealed(RandomSource paramRandomSource) {
/* 15 */     double d = 1.0D;
/* 16 */     byte b = 0;
/* 17 */     while (paramRandomSource.nextDouble() < d) {
/* 18 */       d *= 0.826D;
/* 19 */       b++;
/*    */     } 
/* 21 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\NetherVines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */