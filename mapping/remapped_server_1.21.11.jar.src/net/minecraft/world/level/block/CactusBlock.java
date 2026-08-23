/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ public class CactusBlock extends Block {
/*  25 */   public static final MapCodec<CactusBlock> CODEC = simpleCodec(CactusBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<CactusBlock> codec() {
/*  29 */     return CODEC;
/*     */   }
/*     */   
/*  32 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*     */   public static final int MAX_AGE = 15;
/*  35 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*  37 */   private static final VoxelShape SHAPE_COLLISION = Block.column(14.0D, 0.0D, 15.0D);
/*     */   private static final int MAX_CACTUS_GROWING_HEIGHT = 3;
/*     */   private static final int ATTEMPT_GROW_CACTUS_FLOWER_AGE = 8;
/*     */   private static final double ATTEMPT_GROW_CACTUS_FLOWER_SMALL_CACTUS_CHANCE = 0.1D;
/*     */   private static final double ATTEMPT_GROW_CACTUS_FLOWER_TALL_CACTUS_CHANCE = 0.25D;
/*     */   
/*     */   protected CactusBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  50 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/*  51 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  57 */     BlockPos blockPos = paramBlockPos.above();
/*  58 */     if (!paramServerLevel.isEmptyBlock(blockPos)) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     byte b = 1;
/*  63 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  64 */     while (paramServerLevel.getBlockState(paramBlockPos.below(b)).is(this)) {
/*  65 */       b++;
/*  66 */       if (b == 3 && i == 15) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*  71 */     if (i == 8 && canSurvive(defaultBlockState(), (LevelReader)paramServerLevel, paramBlockPos.above())) {
/*  72 */       double d = (b >= 3) ? 0.25D : 0.1D;
/*  73 */       if (paramRandomSource.nextDouble() <= d) {
/*  74 */         paramServerLevel.setBlockAndUpdate(blockPos, Blocks.CACTUS_FLOWER.defaultBlockState());
/*     */       }
/*  76 */     } else if (i == 15 && b < 3) {
/*  77 */       paramServerLevel.setBlockAndUpdate(blockPos, defaultBlockState());
/*  78 */       BlockState blockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(0));
/*  79 */       paramServerLevel.setBlock(paramBlockPos, blockState, 260);
/*  80 */       paramServerLevel.neighborChanged(blockState, blockPos, this, null, false);
/*     */     } 
/*     */     
/*  83 */     if (i < 15) {
/*  84 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1)), 260);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  90 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  95 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 100 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 101 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*     */     
/* 104 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 109 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 110 */       BlockState blockState1 = paramLevelReader.getBlockState(paramBlockPos.relative(direction));
/*     */       
/* 112 */       if (blockState1.isSolid() || paramLevelReader.getFluidState(paramBlockPos.relative(direction)).is(FluidTags.LAVA)) {
/* 113 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/* 118 */     return ((blockState.is(Blocks.CACTUS) || blockState.is(BlockTags.SAND)) && !paramLevelReader.getBlockState(paramBlockPos.above()).liquid());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 123 */     paramEntity.hurt(paramLevel.damageSources().cactus(), 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 128 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 133 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CactusBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */