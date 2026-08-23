/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TorchflowerCropBlock extends CropBlock {
/* 19 */   public static final MapCodec<TorchflowerCropBlock> CODEC = simpleCodec(TorchflowerCropBlock::new);
/*    */   public static final int MAX_AGE = 1;
/*    */   
/*    */   public MapCodec<TorchflowerCropBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1; private static final VoxelShape[] SHAPES; private static final int BONEMEAL_INCREASE = 1;
/*    */   static {
/* 29 */     SHAPES = Block.boxes(1, paramInt -> Block.column(6.0D, 0.0D, (6 + paramInt * 4)));
/*    */   }
/*    */ 
/*    */   
/*    */   public TorchflowerCropBlock(BlockBehaviour.Properties paramProperties) {
/* 34 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 39 */     paramBuilder.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 44 */     return SHAPES[getAge(paramBlockState)];
/*    */   }
/*    */ 
/*    */   
/*    */   protected IntegerProperty getAgeProperty() {
/* 49 */     return AGE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMaxAge() {
/* 55 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemLike getBaseSeedId() {
/* 60 */     return (ItemLike)Items.TORCHFLOWER_SEEDS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForAge(int paramInt) {
/* 66 */     if (paramInt == 2) {
/* 67 */       return Blocks.TORCHFLOWER.defaultBlockState();
/*    */     }
/* 69 */     return super.getStateForAge(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 74 */     if (paramRandomSource.nextInt(3) != 0) {
/* 75 */       super.randomTick(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBonemealAgeIncrease(Level paramLevel) {
/* 81 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TorchflowerCropBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */