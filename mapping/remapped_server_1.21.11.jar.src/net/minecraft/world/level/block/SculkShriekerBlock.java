/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkShriekerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  33 */   public static final MapCodec<SculkShriekerBlock> CODEC = simpleCodec(SculkShriekerBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<SculkShriekerBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty SHRIEKING = BlockStateProperties.SHRIEKING;
/*  41 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  42 */   public static final BooleanProperty CAN_SUMMON = BlockStateProperties.CAN_SUMMON;
/*     */   
/*  44 */   private static final VoxelShape SHAPE_COLLISION = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*  46 */   public static final double TOP_Y = SHAPE_COLLISION.max(Direction.Axis.Y);
/*     */   
/*     */   public SculkShriekerBlock(BlockBehaviour.Properties paramProperties) {
/*  49 */     super(paramProperties);
/*     */     
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHRIEKING, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)CAN_SUMMON, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  56 */     paramBuilder.add(new Property[] { (Property)SHRIEKING });
/*  57 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED });
/*  58 */     paramBuilder.add(new Property[] { (Property)CAN_SUMMON });
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity) {
/*  63 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  64 */       ServerPlayer serverPlayer = SculkShriekerBlockEntity.tryGetPlayer(paramEntity);
/*  65 */       if (serverPlayer != null) {
/*  66 */         serverLevel.getBlockEntity(paramBlockPos, BlockEntityType.SCULK_SHRIEKER).ifPresent(paramSculkShriekerBlockEntity -> paramSculkShriekerBlockEntity.tryShriek(paramServerLevel, paramServerPlayer));
/*     */       } }
/*     */ 
/*     */     
/*  70 */     super.stepOn(paramLevel, paramBlockPos, paramBlockState, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  75 */     if (((Boolean)paramBlockState.getValue((Property)SHRIEKING)).booleanValue()) {
/*  76 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)SHRIEKING, Boolean.valueOf(false)), 3);
/*     */       
/*  78 */       paramServerLevel.getBlockEntity(paramBlockPos, BlockEntityType.SCULK_SHRIEKER).ifPresent(paramSculkShriekerBlockEntity -> paramSculkShriekerBlockEntity.tryRespond(paramServerLevel));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  84 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState paramBlockState) {
/*  89 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  99 */     return (BlockEntity)new SculkShriekerBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 104 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 105 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 107 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 112 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos()).getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 117 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 118 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 120 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 125 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/* 126 */     if (paramBoolean) {
/* 127 */       tryDropExperience(paramServerLevel, paramBlockPos, paramItemStack, (IntProvider)ConstantInt.of(5));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 133 */     if (!paramLevel.isClientSide()) {
/* 134 */       return BaseEntityBlock.createTickerHelper(paramBlockEntityType, BlockEntityType.SCULK_SHRIEKER, (paramLevel, paramBlockPos, paramBlockState, paramSculkShriekerBlockEntity) -> VibrationSystem.Ticker.tick(paramLevel, paramSculkShriekerBlockEntity.getVibrationData(), paramSculkShriekerBlockEntity.getVibrationUser()));
/*     */     }
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkShriekerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */