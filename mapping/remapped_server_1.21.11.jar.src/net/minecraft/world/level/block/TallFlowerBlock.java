/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TallFlowerBlock extends DoublePlantBlock implements BonemealableBlock {
/* 13 */   public static final MapCodec<TallFlowerBlock> CODEC = simpleCodec(TallFlowerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TallFlowerBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public TallFlowerBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 31 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 36 */     popResource((Level)paramServerLevel, paramBlockPos, new ItemStack(this));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TallFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */