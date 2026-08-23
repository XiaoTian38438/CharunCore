/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnderChestBlock extends AbstractChestBlock<EnderChestBlockEntity> implements SimpleWaterloggedBlock {
/*  46 */   public static final MapCodec<EnderChestBlock> CODEC = simpleCodec(EnderChestBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<EnderChestBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  53 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  54 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  56 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 14.0D);
/*  57 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.enderchest");
/*     */   
/*     */   protected EnderChestBlock(BlockBehaviour.Properties paramProperties) {
/*  60 */     super(paramProperties, () -> BlockEntityType.ENDER_CHEST);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  66 */     return DoubleBlockCombiner.Combiner::acceptNone;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  71 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  76 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  77 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite())).setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*     */     EnderChestBlockEntity enderChestBlockEntity;
/*  82 */     PlayerEnderChestContainer playerEnderChestContainer = paramPlayer.getEnderChestInventory();
/*  83 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  84 */     if (playerEnderChestContainer != null && blockEntity instanceof EnderChestBlockEntity) { enderChestBlockEntity = (EnderChestBlockEntity)blockEntity; }
/*  85 */     else { return (InteractionResult)InteractionResult.SUCCESS; }
/*     */ 
/*     */     
/*  88 */     BlockPos blockPos = paramBlockPos.above();
/*  89 */     if (paramLevel.getBlockState(blockPos).isRedstoneConductor((BlockGetter)paramLevel, blockPos)) {
/*  90 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  93 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  94 */       playerEnderChestContainer.setActiveChest(enderChestBlockEntity);
/*     */       
/*  96 */       paramPlayer.openMenu((MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> ChestMenu.threeRows(paramInt, paramInventory, (Container)paramPlayerEnderChestContainer), CONTAINER_TITLE));
/*  97 */       paramPlayer.awardStat(Stats.OPEN_ENDERCHEST);
/*  98 */       PiglinAi.angerNearbyPiglins(serverLevel, paramPlayer, true); }
/*     */     
/* 100 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 105 */     return (BlockEntity)new EnderChestBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 110 */     return paramLevel.isClientSide() ? createTickerHelper(paramBlockEntityType, BlockEntityType.ENDER_CHEST, EnderChestBlockEntity::lidAnimateTick) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 115 */     for (byte b = 0; b < 3; b++) {
/* 116 */       int i = paramRandomSource.nextInt(2) * 2 - 1;
/* 117 */       int j = paramRandomSource.nextInt(2) * 2 - 1;
/*     */       
/* 119 */       double d1 = paramBlockPos.getX() + 0.5D + 0.25D * i;
/* 120 */       double d2 = (paramBlockPos.getY() + paramRandomSource.nextFloat());
/* 121 */       double d3 = paramBlockPos.getZ() + 0.5D + 0.25D * j;
/* 122 */       double d4 = (paramRandomSource.nextFloat() * i);
/* 123 */       double d5 = (paramRandomSource.nextFloat() - 0.5D) * 0.125D;
/* 124 */       double d6 = (paramRandomSource.nextFloat() * j);
/*     */       
/* 126 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.PORTAL, d1, d2, d3, d4, d5, d6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 132 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 137 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 142 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 147 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 148 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 150 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 155 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 156 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 158 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 168 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos);
/*     */     
/* 170 */     if (blockEntity instanceof EnderChestBlockEntity)
/* 171 */       ((EnderChestBlockEntity)blockEntity).recheckOpen(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EnderChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */