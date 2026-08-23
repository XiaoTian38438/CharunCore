/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceBlock extends CrossCollisionBlock {
/*  28 */   public static final MapCodec<FenceBlock> CODEC = simpleCodec(FenceBlock::new);
/*     */   private final Function<BlockState, VoxelShape> occlusionShapes;
/*     */   
/*     */   public MapCodec<FenceBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FenceBlock(BlockBehaviour.Properties paramProperties) {
/*  38 */     super(4.0F, 16.0F, 4.0F, 16.0F, 24.0F, paramProperties);
/*  39 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */     
/*  41 */     this.occlusionShapes = makeShapes(4.0F, 16.0F, 2.0F, 6.0F, 15.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState paramBlockState) {
/*  46 */     return this.occlusionShapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  51 */     return getShape(paramBlockState, paramBlockGetter, paramBlockPos, paramCollisionContext);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   public boolean connectsTo(BlockState paramBlockState, boolean paramBoolean, Direction paramDirection) {
/*  60 */     Block block = paramBlockState.getBlock();
/*     */     
/*  62 */     boolean bool = isSameFence(paramBlockState);
/*  63 */     boolean bool1 = (block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(paramBlockState, paramDirection)) ? true : false;
/*  64 */     return ((!isExceptionForConnection(paramBlockState) && paramBoolean) || bool || bool1);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isSameFence(BlockState paramBlockState) {
/*  69 */     return (paramBlockState.is(BlockTags.FENCES) && paramBlockState.is(BlockTags.WOODEN_FENCES) == defaultBlockState().is(BlockTags.WOODEN_FENCES));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  74 */     return !paramLevel.isClientSide() ? LeadItem.bindPlayerMobs(paramPlayer, paramLevel, paramBlockPos) : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  79 */     Level level = paramBlockPlaceContext.getLevel();
/*  80 */     BlockPos blockPos1 = paramBlockPlaceContext.getClickedPos();
/*  81 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */ 
/*     */     
/*  84 */     BlockPos blockPos2 = blockPos1.north();
/*  85 */     BlockPos blockPos3 = blockPos1.east();
/*  86 */     BlockPos blockPos4 = blockPos1.south();
/*  87 */     BlockPos blockPos5 = blockPos1.west();
/*     */     
/*  89 */     BlockState blockState1 = level.getBlockState(blockPos2);
/*  90 */     BlockState blockState2 = level.getBlockState(blockPos3);
/*  91 */     BlockState blockState3 = level.getBlockState(blockPos4);
/*  92 */     BlockState blockState4 = level.getBlockState(blockPos5);
/*     */     
/*  94 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(paramBlockPlaceContext)
/*  95 */       .setValue((Property)NORTH, Boolean.valueOf(connectsTo(blockState1, blockState1.isFaceSturdy((BlockGetter)level, blockPos2, Direction.SOUTH), Direction.SOUTH))))
/*  96 */       .setValue((Property)EAST, Boolean.valueOf(connectsTo(blockState2, blockState2.isFaceSturdy((BlockGetter)level, blockPos3, Direction.WEST), Direction.WEST))))
/*  97 */       .setValue((Property)SOUTH, Boolean.valueOf(connectsTo(blockState3, blockState3.isFaceSturdy((BlockGetter)level, blockPos4, Direction.NORTH), Direction.NORTH))))
/*  98 */       .setValue((Property)WEST, Boolean.valueOf(connectsTo(blockState4, blockState4.isFaceSturdy((BlockGetter)level, blockPos5, Direction.EAST), Direction.EAST))))
/*  99 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 104 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 105 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 107 */     if (paramDirection.getAxis().isHorizontal()) {
/* 108 */       return (BlockState)paramBlockState1.setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), Boolean.valueOf(connectsTo(paramBlockState2, paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, paramDirection.getOpposite()), paramDirection.getOpposite())));
/*     */     }
/* 110 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 115 */     paramBuilder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FenceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */