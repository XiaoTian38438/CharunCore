/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.level.block.state.properties.WoodType;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class StandingSignBlock extends SignBlock {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)paramInstance, StandingSignBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<StandingSignBlock> CODEC;
/*    */   
/*    */   public MapCodec<StandingSignBlock> codec() {
/* 28 */     return CODEC;
/*    */   }
/*    */   
/* 31 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/*    */   public StandingSignBlock(WoodType paramWoodType, BlockBehaviour.Properties paramProperties) {
/* 34 */     super(paramWoodType, paramProperties.sound(paramWoodType.soundType()));
/* 35 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(0))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 40 */     return paramLevelReader.getBlockState(paramBlockPos.below()).isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 45 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 46 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(RotationSegment.convertToSegment(paramBlockPlaceContext.getRotation() + 180.0F)))).setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 51 */     if (paramDirection == Direction.DOWN && !canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/* 52 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 54 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getYRotationDegrees(BlockState paramBlockState) {
/* 59 */     return RotationSegment.convertToDegrees(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 64 */     return (BlockState)paramBlockState.setValue((Property)ROTATION, Integer.valueOf(paramRotation.rotate(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 69 */     return (BlockState)paramBlockState.setValue((Property)ROTATION, Integer.valueOf(paramMirror.mirror(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 74 */     paramBuilder.add(new Property[] { (Property)ROTATION, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StandingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */