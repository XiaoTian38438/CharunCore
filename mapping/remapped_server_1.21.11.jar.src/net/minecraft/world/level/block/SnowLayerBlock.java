/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SnowLayerBlock extends Block {
/*  24 */   public static final MapCodec<SnowLayerBlock> CODEC = simpleCodec(SnowLayerBlock::new);
/*     */   public static final int MAX_HEIGHT = 8;
/*     */   
/*     */   public MapCodec<SnowLayerBlock> codec() {
/*  28 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  32 */   public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS; private static final VoxelShape[] SHAPES; public static final int HEIGHT_IMPASSABLE = 5;
/*     */   static {
/*  34 */     SHAPES = Block.boxes(8, paramInt -> Block.column(16.0D, 0.0D, (paramInt * 2)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SnowLayerBlock(BlockBehaviour.Properties paramProperties) {
/*  39 */     super(paramProperties);
/*  40 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LAYERS, Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  45 */     if (paramPathComputationType == PathComputationType.LAND) {
/*  46 */       return (((Integer)paramBlockState.getValue((Property)LAYERS)).intValue() < 5);
/*     */     }
/*  48 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  53 */     return SHAPES[((Integer)paramBlockState.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  58 */     return SHAPES[((Integer)paramBlockState.getValue((Property)LAYERS)).intValue() - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  63 */     return SHAPES[((Integer)paramBlockState.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  68 */     return SHAPES[((Integer)paramBlockState.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  78 */     return (((Integer)paramBlockState.getValue((Property)LAYERS)).intValue() == 8) ? 0.2F : 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  83 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/*     */     
/*  85 */     if (blockState.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
/*  86 */       return false;
/*     */     }
/*  88 */     if (blockState.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)) {
/*  89 */       return true;
/*     */     }
/*     */     
/*  92 */     return (Block.isFaceFull(blockState.getCollisionShape((BlockGetter)paramLevelReader, paramBlockPos.below()), Direction.UP) || (blockState.is(this) && ((Integer)blockState.getValue((Property)LAYERS)).intValue() == 8));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  97 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  98 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 100 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 105 */     if (paramServerLevel.getBrightness(LightLayer.BLOCK, paramBlockPos) > 11) {
/* 106 */       dropResources(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/* 107 */       paramServerLevel.removeBlock(paramBlockPos, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/* 113 */     int i = ((Integer)paramBlockState.getValue((Property)LAYERS)).intValue();
/*     */     
/* 115 */     if (paramBlockPlaceContext.getItemInHand().is(asItem()) && i < 8) {
/* 116 */       if (paramBlockPlaceContext.replacingClickedOnBlock()) {
/* 117 */         return (paramBlockPlaceContext.getClickedFace() == Direction.UP);
/*     */       }
/* 119 */       return true;
/*     */     } 
/*     */     
/* 122 */     return (i == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 127 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/* 128 */     if (blockState.is(this)) {
/* 129 */       int i = ((Integer)blockState.getValue((Property)LAYERS)).intValue();
/* 130 */       return (BlockState)blockState.setValue((Property)LAYERS, Integer.valueOf(Math.min(8, i + 1)));
/*     */     } 
/*     */     
/* 133 */     return super.getStateForPlacement(paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 138 */     paramBuilder.add(new Property[] { (Property)LAYERS });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SnowLayerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */