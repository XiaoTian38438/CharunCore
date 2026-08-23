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
/*    */ public class RootsBlock extends VegetationBlock {
/* 12 */   public static final MapCodec<RootsBlock> CODEC = simpleCodec(RootsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<RootsBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/* 19 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */   
/*    */   protected RootsBlock(BlockBehaviour.Properties paramProperties) {
/* 22 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 27 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 32 */     return (paramBlockState.is(BlockTags.NYLIUM) || paramBlockState.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(paramBlockState, paramBlockGetter, paramBlockPos));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RootsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */