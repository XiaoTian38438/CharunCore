/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TransparentBlock extends HalfTransparentBlock {
/* 12 */   public static final MapCodec<TransparentBlock> CODEC = simpleCodec(TransparentBlock::new);
/*    */   protected TransparentBlock(BlockBehaviour.Properties paramProperties) {
/* 14 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends TransparentBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 24 */     return Shapes.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 29 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 34 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TransparentBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */