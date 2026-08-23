/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
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
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CakeBlock extends Block {
/*  33 */   public static final MapCodec<CakeBlock> CODEC = simpleCodec(CakeBlock::new);
/*     */   public static final int MAX_BITES = 6;
/*     */   
/*     */   public MapCodec<CakeBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  41 */   public static final IntegerProperty BITES = BlockStateProperties.BITES;
/*     */   
/*  43 */   public static final int FULL_CAKE_SIGNAL = getOutputSignal(0); private static final VoxelShape[] SHAPES;
/*     */   static {
/*  45 */     SHAPES = Block.boxes(6, paramInt -> Block.box((1 + paramInt * 2), 0.0D, 1.0D, 15.0D, 8.0D, 15.0D));
/*     */   }
/*     */   protected CakeBlock(BlockBehaviour.Properties paramProperties) {
/*  48 */     super(paramProperties);
/*  49 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BITES, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  54 */     return SHAPES[((Integer)paramBlockState.getValue((Property)BITES)).intValue()];
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*     */     CandleBlock candleBlock;
/*  59 */     Item item = paramItemStack.getItem();
/*  60 */     if (paramItemStack.is(ItemTags.CANDLES) && ((Integer)paramBlockState.getValue((Property)BITES)).intValue() == 0) { Block block = Block.byItem(item); if (block instanceof CandleBlock) { candleBlock = (CandleBlock)block; }
/*  61 */       else { return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; }  } else { return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; }
/*     */ 
/*     */     
/*  64 */     paramItemStack.consume(1, (LivingEntity)paramPlayer);
/*  65 */     paramLevel.playSound(null, paramBlockPos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  66 */     paramLevel.setBlockAndUpdate(paramBlockPos, CandleCakeBlock.byCandle(candleBlock));
/*  67 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*  68 */     paramPlayer.awardStat(Stats.ITEM_USED.get(item));
/*  69 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  74 */     if (paramLevel.isClientSide()) {
/*  75 */       if (eat((LevelAccessor)paramLevel, paramBlockPos, paramBlockState, paramPlayer).consumesAction())
/*  76 */         return (InteractionResult)InteractionResult.SUCCESS; 
/*  77 */       if (paramPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
/*  78 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/*  82 */     return eat((LevelAccessor)paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */   
/*     */   protected static InteractionResult eat(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/*  86 */     if (!paramPlayer.canEat(false)) {
/*  87 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*  89 */     paramPlayer.awardStat(Stats.EAT_CAKE_SLICE);
/*     */     
/*  91 */     paramPlayer.getFoodData().eat(2, 0.1F);
/*  92 */     int i = ((Integer)paramBlockState.getValue((Property)BITES)).intValue();
/*     */     
/*  94 */     paramLevelAccessor.gameEvent((Entity)paramPlayer, (Holder)GameEvent.EAT, paramBlockPos);
/*     */     
/*  96 */     if (i < 6) {
/*  97 */       paramLevelAccessor.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)BITES, Integer.valueOf(i + 1)), 3);
/*     */     } else {
/*  99 */       paramLevelAccessor.removeBlock(paramBlockPos, false);
/* 100 */       paramLevelAccessor.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_DESTROY, paramBlockPos);
/*     */     } 
/*     */     
/* 103 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 108 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 109 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 112 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 117 */     return paramLevelReader.getBlockState(paramBlockPos.below()).isSolid();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 122 */     paramBuilder.add(new Property[] { (Property)BITES });
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 127 */     return getOutputSignal(((Integer)paramBlockState.getValue((Property)BITES)).intValue());
/*     */   }
/*     */   
/*     */   public static int getOutputSignal(int paramInt) {
/* 131 */     return (7 - paramInt) * 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 141 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CakeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */