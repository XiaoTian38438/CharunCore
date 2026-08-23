/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class StructureVoidBlock
/*    */   extends Block
/*    */ {
/* 15 */   public static final MapCodec<StructureVoidBlock> CODEC = simpleCodec(StructureVoidBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<StructureVoidBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.cube(6.0D);
/*    */   
/*    */   protected StructureVoidBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 30 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 41 */     return SHAPE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 47 */     return 1.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StructureVoidBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */