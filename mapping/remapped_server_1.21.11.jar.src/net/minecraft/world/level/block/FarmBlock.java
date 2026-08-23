/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FarmBlock extends Block {
/*  31 */   public static final MapCodec<FarmBlock> CODEC = simpleCodec(FarmBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<FarmBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;
/*     */   
/*  40 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 15.0D);
/*     */   
/*     */   public static final int MAX_MOISTURE = 7;
/*     */   
/*     */   protected FarmBlock(BlockBehaviour.Properties paramProperties) {
/*  45 */     super(paramProperties);
/*  46 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)MOISTURE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  51 */     if (paramDirection == Direction.UP && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  52 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*  54 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  59 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.above());
/*  60 */     return (!blockState.isSolid() || blockState.getBlock() instanceof FenceGateBlock || blockState.getBlock() instanceof net.minecraft.world.level.block.piston.MovingPistonBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  65 */     if (!defaultBlockState().canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/*  66 */       return Blocks.DIRT.defaultBlockState();
/*     */     }
/*  68 */     return super.getStateForPlacement(paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  78 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  83 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/*  84 */       turnToDirt((Entity)null, paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  90 */     int i = ((Integer)paramBlockState.getValue((Property)MOISTURE)).intValue();
/*  91 */     if (isNearWater((LevelReader)paramServerLevel, paramBlockPos) || paramServerLevel.isRainingAt(paramBlockPos.above())) {
/*  92 */       if (i < 7) {
/*  93 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)MOISTURE, Integer.valueOf(7)), 2);
/*     */       }
/*  95 */     } else if (i > 0) {
/*  96 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)MOISTURE, Integer.valueOf(i - 1)), 2);
/*  97 */     } else if (!shouldMaintainFarmland((BlockGetter)paramServerLevel, paramBlockPos)) {
/*  98 */       turnToDirt((Entity)null, paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/* 104 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramLevel.random.nextFloat() < paramDouble - 0.5D && paramEntity instanceof net.minecraft.world.entity.LivingEntity && (
/* 105 */         paramEntity instanceof net.minecraft.world.entity.player.Player || ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()))
/*     */       {
/* 107 */         if (paramEntity.getBbWidth() * paramEntity.getBbWidth() * paramEntity.getBbHeight() > 0.512F) {
/* 108 */           turnToDirt(paramEntity, paramBlockState, paramLevel, paramBlockPos);
/*     */         }
/*     */       } }
/*     */     
/* 112 */     super.fallOn(paramLevel, paramBlockState, paramBlockPos, paramEntity, paramDouble);
/*     */   }
/*     */   
/*     */   public static void turnToDirt(Entity paramEntity, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 116 */     BlockState blockState = pushEntitiesUp(paramBlockState, Blocks.DIRT.defaultBlockState(), (LevelAccessor)paramLevel, paramBlockPos);
/* 117 */     paramLevel.setBlockAndUpdate(paramBlockPos, blockState);
/* 118 */     paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramEntity, blockState));
/*     */   }
/*     */   
/*     */   private static boolean shouldMaintainFarmland(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 122 */     return paramBlockGetter.getBlockState(paramBlockPos.above()).is(BlockTags.MAINTAINS_FARMLAND);
/*     */   }
/*     */   
/*     */   private static boolean isNearWater(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 126 */     for (BlockPos blockPos : BlockPos.betweenClosed(paramBlockPos.offset(-4, 0, -4), paramBlockPos.offset(4, 1, 4))) {
/* 127 */       if (paramLevelReader.getFluidState(blockPos).is(FluidTags.WATER)) {
/* 128 */         return true;
/*     */       }
/*     */     } 
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 136 */     paramBuilder.add(new Property[] { (Property)MOISTURE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 141 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FarmBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */