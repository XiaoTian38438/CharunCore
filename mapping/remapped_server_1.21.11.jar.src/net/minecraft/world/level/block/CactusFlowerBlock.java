/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CactusFlowerBlock extends VegetationBlock {
/* 12 */   public static final MapCodec<CactusFlowerBlock> CODEC = simpleCodec(CactusFlowerBlock::new);
/* 13 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 12.0D);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends CactusFlowerBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public CactusFlowerBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 26 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 31 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos);
/* 32 */     return (blockState.is(Blocks.CACTUS) || blockState.is(Blocks.FARMLAND) || blockState.isFaceSturdy(paramBlockGetter, paramBlockPos, Direction.UP, SupportType.CENTER));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CactusFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */