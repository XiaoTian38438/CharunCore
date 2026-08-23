/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SnowyDirtBlock extends Block {
/* 17 */   public static final MapCodec<SnowyDirtBlock> CODEC = simpleCodec(SnowyDirtBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends SnowyDirtBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/* 24 */   public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;
/*    */   
/*    */   protected SnowyDirtBlock(BlockBehaviour.Properties paramProperties) {
/* 27 */     super(paramProperties);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SNOWY, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 33 */     if (paramDirection == Direction.UP) {
/* 34 */       return (BlockState)paramBlockState1.setValue((Property)SNOWY, Boolean.valueOf(isSnowySetting(paramBlockState2)));
/*    */     }
/* 36 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 41 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().above());
/* 42 */     return (BlockState)defaultBlockState().setValue((Property)SNOWY, Boolean.valueOf(isSnowySetting(blockState)));
/*    */   }
/*    */   
/*    */   protected static boolean isSnowySetting(BlockState paramBlockState) {
/* 46 */     return paramBlockState.is(BlockTags.SNOW);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 51 */     paramBuilder.add(new Property[] { (Property)SNOWY });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SnowyDirtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */