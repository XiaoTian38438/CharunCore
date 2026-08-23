/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherWartBlock extends VegetationBlock {
/* 19 */   public static final MapCodec<NetherWartBlock> CODEC = simpleCodec(NetherWartBlock::new);
/*    */   public static final int MAX_AGE = 3;
/*    */   
/*    */   public MapCodec<NetherWartBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3; private static final VoxelShape[] SHAPES;
/*    */   static {
/* 29 */     SHAPES = Block.boxes(3, paramInt -> Block.column(16.0D, 0.0D, (5 + paramInt * 3)));
/*    */   }
/*    */   protected NetherWartBlock(BlockBehaviour.Properties paramProperties) {
/* 32 */     super(paramProperties);
/* 33 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 38 */     return SHAPES[((Integer)paramBlockState.getValue((Property)AGE)).intValue()];
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 43 */     return paramBlockState.is(Blocks.SOUL_SAND);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 48 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 53 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/* 54 */     if (i < 3 && paramRandomSource.nextInt(10) == 0) {
/* 55 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1));
/* 56 */       paramServerLevel.setBlock(paramBlockPos, paramBlockState, 2);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 62 */     return new ItemStack((ItemLike)Items.NETHER_WART);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 67 */     paramBuilder.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\NetherWartBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */