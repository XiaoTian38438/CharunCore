/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ButtonBlock extends FaceAttachedHorizontalDirectionalBlock {
/*     */   static {
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)Codec.intRange(1, 1024).fieldOf("ticks_to_stay_pressed").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, ButtonBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<ButtonBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<ButtonBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   private final int ticksToStayPressed;
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected ButtonBlock(BlockSetType paramBlockSetType, int paramInt, BlockBehaviour.Properties paramProperties) {
/*  63 */     super(paramProperties.sound(paramBlockSetType.soundType()));
/*     */     
/*  65 */     this.type = paramBlockSetType;
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*  67 */     this.ticksToStayPressed = paramInt;
/*  68 */     this.shapes = makeShapes();
/*     */   }
/*     */ 
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  73 */     VoxelShape voxelShape1 = Block.cube(14.0D);
/*  74 */     VoxelShape voxelShape2 = Block.cube(12.0D);
/*     */ 
/*     */     
/*  77 */     Map map = Shapes.rotateAttachFace(Block.boxZ(6.0D, 4.0D, 8.0D, 16.0D));
/*     */     
/*  79 */     return getShapeForEachState(paramBlockState -> Shapes.join((VoxelShape)((Map)paramMap.get(paramBlockState.getValue((Property)FACE))).get(paramBlockState.getValue((Property)FACING)), ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? paramVoxelShape1 : paramVoxelShape2, BooleanOp.ONLY_FIRST));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  88 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  93 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*  94 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/*  96 */     press(paramBlockState, paramLevel, paramBlockPos, paramPlayer);
/*  97 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 102 */     if (paramExplosion.canTriggerBlocks() && !((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 103 */       press(paramBlockState, (Level)paramServerLevel, paramBlockPos, (Player)null);
/*     */     }
/* 105 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */   
/*     */   public void press(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 109 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true)), 3);
/* 110 */     updateNeighbours(paramBlockState, paramLevel, paramBlockPos);
/* 111 */     paramLevel.scheduleTick(paramBlockPos, this, this.ticksToStayPressed);
/* 112 */     playSound(paramPlayer, (LevelAccessor)paramLevel, paramBlockPos, true);
/* 113 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_ACTIVATE, paramBlockPos);
/*     */   }
/*     */   
/*     */   protected void playSound(Player paramPlayer, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, boolean paramBoolean) {
/* 117 */     paramLevelAccessor.playSound(paramBoolean ? (Entity)paramPlayer : null, paramBlockPos, getSound(paramBoolean), SoundSource.BLOCKS);
/*     */   }
/*     */   
/*     */   protected SoundEvent getSound(boolean paramBoolean) {
/* 121 */     return paramBoolean ? this.type.buttonClickOn() : this.type.buttonClickOff();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 126 */     if (!paramBoolean && ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 127 */       updateNeighbours(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 133 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 138 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() && getConnectedDirection(paramBlockState) == paramDirection) {
/* 139 */       return 15;
/*     */     }
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 151 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     checkPressed(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 160 */     if (paramLevel.isClientSide() || !this.type.canButtonBeActivatedByArrows() || ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 164 */     checkPressed(paramBlockState, paramLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   protected void checkPressed(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 168 */     AbstractArrow abstractArrow = this.type.canButtonBeActivatedByArrows() ? paramLevel.getEntitiesOfClass(AbstractArrow.class, paramBlockState.getShape((BlockGetter)paramLevel, paramBlockPos).bounds().move(paramBlockPos)).stream().findFirst().orElse(null) : null;
/*     */     
/* 170 */     boolean bool1 = (abstractArrow != null);
/* 171 */     boolean bool2 = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/*     */     
/* 173 */     if (bool1 != bool2) {
/* 174 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool1)), 3);
/* 175 */       updateNeighbours(paramBlockState, paramLevel, paramBlockPos);
/* 176 */       playSound((Player)null, (LevelAccessor)paramLevel, paramBlockPos, bool1);
/* 177 */       paramLevel.gameEvent((Entity)abstractArrow, bool1 ? (Holder)GameEvent.BLOCK_ACTIVATE : (Holder)GameEvent.BLOCK_DEACTIVATE, paramBlockPos);
/*     */     } 
/*     */     
/* 180 */     if (bool1) {
/* 181 */       paramLevel.scheduleTick(new BlockPos((Vec3i)paramBlockPos), this, this.ticksToStayPressed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 187 */     Direction direction = getConnectedDirection(paramBlockState).getOpposite();
/* 188 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, direction, direction.getAxis().isHorizontal() ? Direction.UP : (Direction)paramBlockState.getValue((Property)FACING));
/* 189 */     paramLevel.updateNeighborsAt(paramBlockPos, this, orientation);
/* 190 */     paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 195 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)FACE });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ButtonBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */