/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class PotatoBlock extends CropBlock {
/* 13 */   public static final MapCodec<PotatoBlock> CODEC = simpleCodec(PotatoBlock::new);
/*    */   private static final VoxelShape[] SHAPES;
/*    */   
/*    */   public MapCodec<PotatoBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   static {
/* 20 */     SHAPES = Block.boxes(7, paramInt -> Block.column(16.0D, 0.0D, (2 + paramInt)));
/*    */   }
/*    */   public PotatoBlock(BlockBehaviour.Properties paramProperties) {
/* 23 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemLike getBaseSeedId() {
/* 28 */     return (ItemLike)Items.POTATO;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 33 */     return SHAPES[getAge(paramBlockState)];
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PotatoBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */