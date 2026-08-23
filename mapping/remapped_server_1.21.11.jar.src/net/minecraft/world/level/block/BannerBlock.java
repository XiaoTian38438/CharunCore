/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BannerBlock extends AbstractBannerBlock {
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor), (App)propertiesCodec()).apply((Applicative)paramInstance, BannerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BannerBlock> CODEC;
/*    */   
/*    */   public MapCodec<BannerBlock> codec() {
/* 32 */     return CODEC;
/*    */   }
/*    */   
/* 35 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 37 */   private static final Map<DyeColor, Block> BY_COLOR = Maps.newHashMap();
/* 38 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*    */   
/*    */   public BannerBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/* 41 */     super(paramDyeColor, paramProperties);
/* 42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(0)));
/*    */     
/* 44 */     BY_COLOR.put(paramDyeColor, this);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 49 */     return paramLevelReader.getBlockState(paramBlockPos.below()).isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 54 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 59 */     return (BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(RotationSegment.convertToSegment(paramBlockPlaceContext.getRotation() + 180.0F)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 64 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 65 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 68 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 73 */     return (BlockState)paramBlockState.setValue((Property)ROTATION, Integer.valueOf(paramRotation.rotate(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 78 */     return (BlockState)paramBlockState.setValue((Property)ROTATION, Integer.valueOf(paramMirror.mirror(((Integer)paramBlockState.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 83 */     paramBuilder.add(new Property[] { (Property)ROTATION });
/*    */   }
/*    */   
/*    */   public static Block byColor(DyeColor paramDyeColor) {
/* 87 */     return BY_COLOR.getOrDefault(paramDyeColor, Blocks.WHITE_BANNER);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */