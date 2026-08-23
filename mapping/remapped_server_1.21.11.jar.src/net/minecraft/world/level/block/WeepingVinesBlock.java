/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WeepingVinesBlock extends GrowingPlantHeadBlock {
/* 10 */   public static final MapCodec<WeepingVinesBlock> CODEC = simpleCodec(WeepingVinesBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WeepingVinesBlock> codec() {
/* 14 */     return CODEC;
/*    */   }
/*    */   
/* 17 */   private static final VoxelShape SHAPE = Block.column(8.0D, 9.0D, 16.0D);
/*    */   
/*    */   public WeepingVinesBlock(BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramProperties, Direction.DOWN, SHAPE, false, 0.1D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(RandomSource paramRandomSource) {
/* 25 */     return NetherVines.getBlocksToGrowWhenBonemealed(paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 30 */     return Blocks.WEEPING_VINES_PLANT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canGrowInto(BlockState paramBlockState) {
/* 35 */     return NetherVines.isValidGrowthState(paramBlockState);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeepingVinesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */