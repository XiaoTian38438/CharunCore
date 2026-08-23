/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HangingMossBlock extends Block implements BonemealableBlock {
/*  23 */   public static final MapCodec<HangingMossBlock> CODEC = simpleCodec(HangingMossBlock::new);
/*     */   
/*  25 */   private static final VoxelShape SHAPE_BASE = Block.column(14.0D, 0.0D, 16.0D);
/*  26 */   private static final VoxelShape SHAPE_TIP = Block.column(14.0D, 2.0D, 16.0D);
/*     */ 
/*     */   
/*     */   public MapCodec<HangingMossBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final BooleanProperty TIP = BlockStateProperties.TIP;
/*     */   
/*     */   public HangingMossBlock(BlockBehaviour.Properties paramProperties) {
/*  36 */     super(paramProperties);
/*  37 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)TIP, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  42 */     return ((Boolean)paramBlockState.getValue((Property)TIP)).booleanValue() ? SHAPE_TIP : SHAPE_BASE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  47 */     if (paramRandomSource.nextInt(500) == 0) {
/*  48 */       BlockState blockState = paramLevel.getBlockState(paramBlockPos.above());
/*  49 */       if (blockState.is(BlockTags.PALE_OAK_LOGS) || blockState.is(Blocks.PALE_OAK_LEAVES)) {
/*  50 */         paramLevel.playLocalSound(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), SoundEvents.PALE_HANGING_MOSS_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  57 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  62 */     return canStayAtPosition((BlockGetter)paramLevelReader, paramBlockPos);
/*     */   }
/*     */   
/*     */   private boolean canStayAtPosition(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  66 */     BlockPos blockPos = paramBlockPos.relative(Direction.UP);
/*  67 */     BlockState blockState = paramBlockGetter.getBlockState(blockPos);
/*  68 */     return (MultifaceBlock.canAttachTo(paramBlockGetter, Direction.UP, blockPos, blockState) || blockState.is(Blocks.PALE_HANGING_MOSS));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  73 */     if (!canStayAtPosition((BlockGetter)paramLevelReader, paramBlockPos1)) {
/*  74 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*  76 */     return (BlockState)paramBlockState1.setValue((Property)TIP, Boolean.valueOf(!paramLevelReader.getBlockState(paramBlockPos1.below()).is(this)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  81 */     if (!canStayAtPosition((BlockGetter)paramServerLevel, paramBlockPos)) {
/*  82 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  88 */     paramBuilder.add(new Property[] { (Property)TIP });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  93 */     return canGrowInto(paramLevelReader.getBlockState(getTip((BlockGetter)paramLevelReader, paramBlockPos).below()));
/*     */   }
/*     */   
/*     */   private boolean canGrowInto(BlockState paramBlockState) {
/*  97 */     return paramBlockState.isAir();
/*     */   }
/*     */   public BlockPos getTip(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*     */     BlockState blockState;
/* 101 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */     
/*     */     do {
/* 104 */       mutableBlockPos.move(Direction.DOWN);
/* 105 */       blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 106 */     } while (blockState.is(this));
/*     */     
/* 108 */     return mutableBlockPos.relative(Direction.UP).immutable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 118 */     BlockPos blockPos = getTip((BlockGetter)paramServerLevel, paramBlockPos).below();
/*     */     
/* 120 */     if (!canGrowInto(paramServerLevel.getBlockState(blockPos))) {
/*     */       return;
/*     */     }
/* 123 */     paramServerLevel.setBlockAndUpdate(blockPos, (BlockState)paramBlockState.setValue((Property)TIP, Boolean.valueOf(true)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HangingMossBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */