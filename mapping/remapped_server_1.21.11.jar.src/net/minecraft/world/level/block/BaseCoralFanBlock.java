/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BaseCoralFanBlock extends BaseCoralPlantTypeBlock {
/* 11 */   public static final MapCodec<BaseCoralFanBlock> CODEC = simpleCodec(BaseCoralFanBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends BaseCoralFanBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/* 18 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 4.0D);
/*    */   
/*    */   protected BaseCoralFanBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 26 */     return SHAPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BaseCoralFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */