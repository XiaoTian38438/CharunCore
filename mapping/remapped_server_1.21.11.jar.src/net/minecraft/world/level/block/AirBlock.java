/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AirBlock extends Block {
/* 12 */   public static final MapCodec<AirBlock> CODEC = simpleCodec(AirBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<AirBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   public AirBlock(BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 25 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 30 */     return Shapes.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AirBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */