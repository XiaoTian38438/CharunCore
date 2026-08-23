/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class RepeaterBlock extends DiodeBlock {
/*  23 */   public static final MapCodec<RepeaterBlock> CODEC = simpleCodec(RepeaterBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<RepeaterBlock> codec() {
/*  27 */     return CODEC;
/*     */   }
/*     */   
/*  30 */   public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
/*  31 */   public static final IntegerProperty DELAY = BlockStateProperties.DELAY;
/*     */   
/*     */   protected RepeaterBlock(BlockBehaviour.Properties paramProperties) {
/*  34 */     super(paramProperties);
/*  35 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)DELAY, Integer.valueOf(1))).setValue((Property)LOCKED, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  40 */     if (!(paramPlayer.getAbilities()).mayBuild) {
/*  41 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  44 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)DELAY), 3);
/*  45 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDelay(BlockState paramBlockState) {
/*  50 */     return ((Integer)paramBlockState.getValue((Property)DELAY)).intValue() * 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  55 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/*  56 */     return (BlockState)blockState.setValue((Property)LOCKED, Boolean.valueOf(isLocked((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos(), blockState)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  61 */     if (paramDirection == Direction.DOWN && !canSurviveOn(paramLevelReader, paramBlockPos2, paramBlockState2)) {
/*  62 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  65 */     if (!paramLevelReader.isClientSide() && paramDirection.getAxis() != ((Direction)paramBlockState1.getValue((Property)FACING)).getAxis()) {
/*  66 */       return (BlockState)paramBlockState1.setValue((Property)LOCKED, Boolean.valueOf(isLocked(paramLevelReader, paramBlockPos1, paramBlockState1)));
/*     */     }
/*  68 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocked(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  73 */     return (getAlternateSignal((SignalGetter)paramLevelReader, paramBlockPos, paramBlockState) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean sideInputDiodesOnly() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  83 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*  86 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*     */     
/*  88 */     double d1 = paramBlockPos.getX() + 0.5D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D;
/*  89 */     double d2 = paramBlockPos.getY() + 0.4D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D;
/*  90 */     double d3 = paramBlockPos.getZ() + 0.5D + (paramRandomSource.nextDouble() - 0.5D) * 0.2D;
/*     */     
/*  92 */     float f = -5.0F;
/*  93 */     if (paramRandomSource.nextBoolean()) {
/*  94 */       f = (((Integer)paramBlockState.getValue((Property)DELAY)).intValue() * 2 - 1);
/*     */     }
/*  96 */     f /= 16.0F;
/*     */     
/*  98 */     double d4 = (f * direction.getStepX());
/*  99 */     double d5 = (f * direction.getStepZ());
/*     */     
/* 101 */     paramLevel.addParticle((ParticleOptions)DustParticleOptions.REDSTONE, d1 + d4, d2, d3 + d5, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 106 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)DELAY, (Property)LOCKED, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RepeaterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */