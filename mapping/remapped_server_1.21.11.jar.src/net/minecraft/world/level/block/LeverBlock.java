/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LeverBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  38 */   public static final MapCodec<LeverBlock> CODEC = simpleCodec(LeverBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<LeverBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected LeverBlock(BlockBehaviour.Properties paramProperties) {
/*  50 */     super(paramProperties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*     */     
/*  53 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  57 */     Map map = Shapes.rotateAttachFace(Block.boxZ(6.0D, 8.0D, 10.0D, 16.0D));
/*     */     
/*  59 */     return getShapeForEachState(paramBlockState -> (VoxelShape)((Map)paramMap.get(paramBlockState.getValue((Property)FACE))).get(paramBlockState.getValue((Property)FACING)), (Property<?>[])new Property[] { (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  64 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  70 */     if (paramLevel.isClientSide()) {
/*  71 */       BlockState blockState = (BlockState)paramBlockState.cycle((Property)POWERED);
/*  72 */       if (((Boolean)blockState.getValue((Property)POWERED)).booleanValue()) {
/*  73 */         makeParticle(blockState, (LevelAccessor)paramLevel, paramBlockPos, 1.0F);
/*     */       }
/*     */     } else {
/*  76 */       pull(paramBlockState, paramLevel, paramBlockPos, (Player)null);
/*     */     } 
/*  78 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/*  83 */     if (paramExplosion.canTriggerBlocks()) {
/*  84 */       pull(paramBlockState, (Level)paramServerLevel, paramBlockPos, (Player)null);
/*     */     }
/*  86 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */   
/*     */   public void pull(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/*  90 */     paramBlockState = (BlockState)paramBlockState.cycle((Property)POWERED);
/*  91 */     paramLevel.setBlock(paramBlockPos, paramBlockState, 3);
/*  92 */     updateNeighbours(paramBlockState, paramLevel, paramBlockPos);
/*  93 */     playSound(paramPlayer, (LevelAccessor)paramLevel, paramBlockPos, paramBlockState);
/*  94 */     paramLevel.gameEvent((Entity)paramPlayer, ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? (Holder)GameEvent.BLOCK_ACTIVATE : (Holder)GameEvent.BLOCK_DEACTIVATE, paramBlockPos);
/*     */   }
/*     */   
/*     */   protected static void playSound(Player paramPlayer, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  98 */     float f = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 0.6F : 0.5F;
/*  99 */     paramLevelAccessor.playSound((Entity)paramPlayer, paramBlockPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
/*     */   }
/*     */   
/*     */   private static void makeParticle(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, float paramFloat) {
/* 103 */     Direction direction1 = ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite();
/* 104 */     Direction direction2 = getConnectedDirection(paramBlockState).getOpposite();
/* 105 */     double d1 = paramBlockPos.getX() + 0.5D + 0.1D * direction1.getStepX() + 0.2D * direction2.getStepX();
/* 106 */     double d2 = paramBlockPos.getY() + 0.5D + 0.1D * direction1.getStepY() + 0.2D * direction2.getStepY();
/* 107 */     double d3 = paramBlockPos.getZ() + 0.5D + 0.1D * direction1.getStepZ() + 0.2D * direction2.getStepZ();
/*     */     
/* 109 */     paramLevelAccessor.addParticle((ParticleOptions)new DustParticleOptions(16711680, paramFloat), d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 114 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() && paramRandomSource.nextFloat() < 0.25F) {
/* 115 */       makeParticle(paramBlockState, (LevelAccessor)paramLevel, paramBlockPos, 0.5F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 121 */     if (!paramBoolean && ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 122 */       updateNeighbours(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 128 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 133 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() && getConnectedDirection(paramBlockState) == paramDirection) {
/* 134 */       return 15;
/*     */     }
/* 136 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 146 */     Direction direction = getConnectedDirection(paramBlockState).getOpposite();
/* 147 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, direction, direction.getAxis().isHorizontal() ? Direction.UP : (Direction)paramBlockState.getValue((Property)FACING));
/* 148 */     paramLevel.updateNeighborsAt(paramBlockPos, this, orientation);
/* 149 */     paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 154 */     paramBuilder.add(new Property[] { (Property)FACE, (Property)FACING, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LeverBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */