/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class MudBlock extends Block {
/* 13 */   public static final MapCodec<MudBlock> CODEC = simpleCodec(MudBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<MudBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/* 20 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 14.0D);
/*    */   
/*    */   public MudBlock(BlockBehaviour.Properties paramProperties) {
/* 23 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 28 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 33 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 38 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 48 */     return 0.2F;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MudBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */