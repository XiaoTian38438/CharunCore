/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CandleBlock extends AbstractCandleBlock implements SimpleWaterloggedBlock {
/*  37 */   public static final MapCodec<CandleBlock> CODEC = simpleCodec(CandleBlock::new); public static final int MIN_CANDLES = 1;
/*     */   public static final int MAX_CANDLES = 4;
/*     */   
/*     */   public MapCodec<CandleBlock> codec() {
/*  41 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
/*  48 */   public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
/*  49 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED; public static final ToIntFunction<BlockState> LIGHT_EMISSION; private static final Int2ObjectMap<List<Vec3>> PARTICLE_OFFSETS;
/*     */   static {
/*  51 */     LIGHT_EMISSION = (paramBlockState -> ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() ? (3 * ((Integer)paramBlockState.getValue((Property)CANDLES)).intValue()) : 0);
/*     */     
/*  53 */     PARTICLE_OFFSETS = (Int2ObjectMap<List<Vec3>>)Util.make(new Int2ObjectOpenHashMap(4), paramInt2ObjectOpenHashMap -> {
/*     */           float f = 0.0625F;
/*     */           paramInt2ObjectOpenHashMap.put(1, List.of((new Vec3(8.0D, 8.0D, 8.0D)).scale(0.0625D)));
/*     */           paramInt2ObjectOpenHashMap.put(2, List.of((new Vec3(6.0D, 7.0D, 8.0D)).scale(0.0625D), (new Vec3(10.0D, 8.0D, 7.0D)).scale(0.0625D)));
/*     */           paramInt2ObjectOpenHashMap.put(3, List.of((new Vec3(8.0D, 5.0D, 10.0D)).scale(0.0625D), (new Vec3(6.0D, 7.0D, 8.0D)).scale(0.0625D), (new Vec3(9.0D, 8.0D, 7.0D)).scale(0.0625D)));
/*     */           paramInt2ObjectOpenHashMap.put(4, List.of((new Vec3(7.0D, 5.0D, 9.0D)).scale(0.0625D), (new Vec3(10.0D, 7.0D, 9.0D)).scale(0.0625D), (new Vec3(6.0D, 7.0D, 6.0D)).scale(0.0625D), (new Vec3(9.0D, 8.0D, 6.0D)).scale(0.0625D)));
/*     */         });
/*     */   }
/*  61 */   private static final VoxelShape[] SHAPES = new VoxelShape[] {
/*  62 */       Block.column(2.0D, 0.0D, 6.0D), 
/*  63 */       Block.box(5.0D, 0.0D, 6.0D, 11.0D, 6.0D, 9.0D), 
/*  64 */       Block.box(5.0D, 0.0D, 6.0D, 10.0D, 6.0D, 11.0D), 
/*  65 */       Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 10.0D)
/*     */     };
/*     */   
/*     */   public CandleBlock(BlockBehaviour.Properties paramProperties) {
/*  69 */     super(paramProperties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)CANDLES, Integer.valueOf(1))).setValue((Property)LIT, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  75 */     if (paramItemStack.isEmpty() && (paramPlayer.getAbilities()).mayBuild && ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*  76 */       extinguish(paramPlayer, paramBlockState, (LevelAccessor)paramLevel, paramBlockPos);
/*  77 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  80 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/*  85 */     if (!paramBlockPlaceContext.isSecondaryUseActive() && paramBlockPlaceContext.getItemInHand().getItem() == asItem() && ((Integer)paramBlockState.getValue((Property)CANDLES)).intValue() < 4) {
/*  86 */       return true;
/*     */     }
/*  88 */     return super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  93 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/*  94 */     if (blockState.is(this)) {
/*  95 */       return (BlockState)blockState.cycle((Property)CANDLES);
/*     */     }
/*     */     
/*  98 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  99 */     boolean bool = (fluidState.getType() == Fluids.WATER) ? true : false;
/* 100 */     return (BlockState)super.getStateForPlacement(paramBlockPlaceContext).setValue((Property)WATERLOGGED, Boolean.valueOf(bool));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 105 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 106 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 109 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 114 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 115 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/* 118 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 123 */     return SHAPES[((Integer)paramBlockState.getValue((Property)CANDLES)).intValue() - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 128 */     paramBuilder.add(new Property[] { (Property)CANDLES, (Property)LIT, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 133 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue() || paramFluidState.getType() != Fluids.WATER) {
/* 134 */       return false;
/*     */     }
/*     */     
/* 137 */     BlockState blockState = (BlockState)paramBlockState.setValue((Property)WATERLOGGED, Boolean.valueOf(true));
/* 138 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/* 139 */       extinguish((Player)null, blockState, paramLevelAccessor, paramBlockPos);
/*     */     } else {
/* 141 */       paramLevelAccessor.setBlock(paramBlockPos, blockState, 3);
/*     */     } 
/*     */     
/* 144 */     paramLevelAccessor.scheduleTick(paramBlockPos, paramFluidState.getType(), paramFluidState.getType().getTickDelay((LevelReader)paramLevelAccessor));
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState paramBlockState) {
/* 149 */     return (paramBlockState.is(BlockTags.CANDLES, paramBlockStateBase -> (paramBlockStateBase.hasProperty((Property)LIT) && paramBlockStateBase.hasProperty((Property)WATERLOGGED))) && !((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() && !((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterable<Vec3> getParticleOffsets(BlockState paramBlockState) {
/* 155 */     return (Iterable<Vec3>)PARTICLE_OFFSETS.get(((Integer)paramBlockState.getValue((Property)CANDLES)).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeLit(BlockState paramBlockState) {
/* 160 */     return (!((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue() && super.canBeLit(paramBlockState));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 165 */     return Block.canSupportCenter(paramLevelReader, paramBlockPos.below(), Direction.UP);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CandleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */