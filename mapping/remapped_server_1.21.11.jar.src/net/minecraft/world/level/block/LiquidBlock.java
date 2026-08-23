/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FlowingFluid;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LiquidBlock extends Block implements BucketPickup {
/*     */   static {
/*  42 */     FLOWING_FLUID = BuiltInRegistries.FLUID.byNameCodec().comapFlatMap(paramFluid -> { FlowingFluid flowingFluid = (FlowingFluid)paramFluid; return (Function)((paramFluid instanceof FlowingFluid) ? DataResult.success(flowingFluid) : DataResult.error(()));
/*     */         }paramFlowingFluid -> paramFlowingFluid);
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)FLOWING_FLUID.fieldOf("fluid").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, LiquidBlock::new));
/*     */   }
/*     */   
/*     */   private static final Codec<FlowingFluid> FLOWING_FLUID;
/*     */   public static final MapCodec<LiquidBlock> CODEC;
/*     */   
/*     */   public MapCodec<LiquidBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
/*     */   
/*     */   protected final FlowingFluid fluid;
/*     */   
/*     */   private final List<FluidState> stateCache;
/*  59 */   public static final VoxelShape SHAPE_STABLE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*  61 */   public static final ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST);
/*     */   
/*     */   protected LiquidBlock(FlowingFluid paramFlowingFluid, BlockBehaviour.Properties paramProperties) {
/*  64 */     super(paramProperties);
/*  65 */     this.fluid = paramFlowingFluid;
/*  66 */     this.stateCache = Lists.newArrayList();
/*  67 */     this.stateCache.add(paramFlowingFluid.getSource(false));
/*  68 */     for (byte b = 1; b < 8; b++) {
/*  69 */       this.stateCache.add(paramFlowingFluid.getFlowing(8 - b, false));
/*     */     }
/*  71 */     this.stateCache.add(paramFlowingFluid.getFlowing(8, true));
/*  72 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  77 */     if (paramCollisionContext.alwaysCollideWithFluid()) {
/*  78 */       return Shapes.block();
/*     */     }
/*     */     
/*  81 */     if (paramCollisionContext.isAbove(SHAPE_STABLE, paramBlockPos, true) && ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() == 0 && paramCollisionContext.canStandOnFluid(paramBlockGetter.getFluidState(paramBlockPos.above()), paramBlockState.getFluidState())) {
/*  82 */       return SHAPE_STABLE;
/*     */     }
/*  84 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  89 */     return paramBlockState.getFluidState().isRandomlyTicking();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  94 */     paramBlockState.getFluidState().randomTick(paramServerLevel, paramBlockPos, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 104 */     return !this.fluid.is(FluidTags.LAVA);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 109 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/* 110 */     return this.stateCache.get(Math.min(i, 8));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skipRendering(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/* 115 */     return paramBlockState2.getFluidState().getType().isSame((Fluid)this.fluid);
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 120 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState paramBlockState, LootParams.Builder paramBuilder) {
/* 125 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 130 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 135 */     if (shouldSpreadLiquid(paramLevel, paramBlockPos, paramBlockState1)) {
/* 136 */       paramLevel.scheduleTick(paramBlockPos, paramBlockState1.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)paramLevel));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 142 */     if (paramBlockState1.getFluidState().isSource() || paramBlockState2.getFluidState().isSource()) {
/* 143 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, paramBlockState1.getFluidState().getType(), this.fluid.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 146 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 151 */     if (shouldSpreadLiquid(paramLevel, paramBlockPos, paramBlockState)) {
/* 152 */       paramLevel.scheduleTick(paramBlockPos, paramBlockState.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)paramLevel));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldSpreadLiquid(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 157 */     if (this.fluid.is(FluidTags.LAVA)) {
/* 158 */       boolean bool = paramLevel.getBlockState(paramBlockPos.below()).is(Blocks.SOUL_SOIL);
/*     */       
/* 160 */       for (UnmodifiableIterator<Direction> unmodifiableIterator = POSSIBLE_FLOW_DIRECTIONS.iterator(); unmodifiableIterator.hasNext(); ) { Direction direction = unmodifiableIterator.next();
/* 161 */         BlockPos blockPos = paramBlockPos.relative(direction.getOpposite());
/*     */         
/* 163 */         if (paramLevel.getFluidState(blockPos).is(FluidTags.WATER)) {
/* 164 */           Block block = paramLevel.getFluidState(paramBlockPos).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
/* 165 */           paramLevel.setBlockAndUpdate(paramBlockPos, block.defaultBlockState());
/* 166 */           fizz((LevelAccessor)paramLevel, paramBlockPos);
/* 167 */           return false;
/*     */         } 
/*     */         
/* 170 */         if (bool && paramLevel.getBlockState(blockPos).is(Blocks.BLUE_ICE)) {
/* 171 */           paramLevel.setBlockAndUpdate(paramBlockPos, Blocks.BASALT.defaultBlockState());
/* 172 */           fizz((LevelAccessor)paramLevel, paramBlockPos);
/* 173 */           return false;
/*     */         }  }
/*     */     
/*     */     } 
/* 177 */     return true;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 181 */     paramLevelAccessor.levelEvent(1501, paramBlockPos, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 186 */     paramBuilder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 191 */     if (((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() == 0) {
/* 192 */       paramLevelAccessor.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 11);
/* 193 */       return new ItemStack((ItemLike)this.fluid.getBucket());
/*     */     } 
/* 195 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 200 */     return this.fluid.getPickupSound();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LiquidBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */