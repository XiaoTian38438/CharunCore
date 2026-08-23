/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CandleCakeBlock extends AbstractCandleBlock {
/*     */   static {
/*  34 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("candle").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, CandleCakeBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CandleCakeBlock> CODEC;
/*     */   
/*     */   public MapCodec<CandleCakeBlock> codec() {
/*  41 */     return CODEC;
/*     */   }
/*     */   
/*  44 */   public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
/*     */   
/*  46 */   private static final VoxelShape SHAPE = Shapes.or(
/*  47 */       Block.column(2.0D, 8.0D, 14.0D), 
/*  48 */       Block.column(14.0D, 0.0D, 8.0D));
/*     */ 
/*     */   
/*  51 */   private static final Map<CandleBlock, CandleCakeBlock> BY_CANDLE = Maps.newHashMap();
/*     */   
/*  53 */   private static final Iterable<Vec3> PARTICLE_OFFSETS = List.of((new Vec3(8.0D, 16.0D, 8.0D)).scale(0.0625D));
/*     */   
/*     */   private final CandleBlock candleBlock;
/*     */   
/*     */   protected CandleCakeBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/*  58 */     super(paramProperties); CandleBlock candleBlock;
/*  59 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(false)));
/*     */     
/*  61 */     if (paramBlock instanceof CandleBlock) { candleBlock = (CandleBlock)paramBlock; }
/*  62 */     else { throw new IllegalArgumentException("Expected block to be of " + String.valueOf(CandleBlock.class) + " was " + String.valueOf(paramBlock.getClass())); }
/*     */ 
/*     */     
/*  65 */     BY_CANDLE.put(candleBlock, this);
/*  66 */     this.candleBlock = candleBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Iterable<Vec3> getParticleOffsets(BlockState paramBlockState) {
/*  71 */     return PARTICLE_OFFSETS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  76 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  81 */     if (paramItemStack.is(Items.FLINT_AND_STEEL) || paramItemStack.is(Items.FIRE_CHARGE)) {
/*  82 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  85 */     if (candleHit(paramBlockHitResult) && paramItemStack.isEmpty() && ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*  86 */       extinguish(paramPlayer, paramBlockState, (LevelAccessor)paramLevel, paramBlockPos);
/*  87 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  90 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  95 */     InteractionResult interactionResult = CakeBlock.eat((LevelAccessor)paramLevel, paramBlockPos, Blocks.CAKE.defaultBlockState(), paramPlayer);
/*  96 */     if (interactionResult.consumesAction()) {
/*  97 */       dropResources(paramBlockState, paramLevel, paramBlockPos);
/*     */     }
/*  99 */     return interactionResult;
/*     */   }
/*     */   
/*     */   private static boolean candleHit(BlockHitResult paramBlockHitResult) {
/* 103 */     return ((paramBlockHitResult.getLocation()).y - paramBlockHitResult.getBlockPos().getY() > 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 108 */     paramBuilder.add(new Property[] { (Property)LIT });
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 113 */     return new ItemStack(Blocks.CAKE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 118 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 119 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 122 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 127 */     return paramLevelReader.getBlockState(paramBlockPos.below()).isSolid();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 132 */     return CakeBlock.FULL_CAKE_SIGNAL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   public static BlockState byCandle(CandleBlock paramCandleBlock) {
/* 146 */     return ((CandleCakeBlock)BY_CANDLE.get(paramCandleBlock)).defaultBlockState();
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState paramBlockState) {
/* 150 */     return paramBlockState.is(BlockTags.CANDLE_CAKES, paramBlockStateBase -> (paramBlockStateBase.hasProperty((Property)LIT) && !((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CandleCakeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */