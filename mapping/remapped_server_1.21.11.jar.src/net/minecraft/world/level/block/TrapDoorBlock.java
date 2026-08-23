/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  41 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, TrapDoorBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<TrapDoorBlock> CODEC;
/*     */   
/*     */   public MapCodec<? extends TrapDoorBlock> codec() {
/*  48 */     return CODEC;
/*     */   }
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  52 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  53 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  54 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  56 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected TrapDoorBlock(BlockSetType paramBlockSetType, BlockBehaviour.Properties paramProperties) {
/*  61 */     super(paramProperties.sound(paramBlockSetType.soundType()));
/*  62 */     this.type = paramBlockSetType;
/*  63 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  68 */     return SHAPES.get(((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() ? 
/*  69 */         paramBlockState.getValue((Property)FACING) : (
/*  70 */         (paramBlockState.getValue((Property)HALF) == Half.TOP) ? Direction.DOWN : Direction.UP));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  76 */     switch (paramPathComputationType) {
/*     */       case LAND:
/*  78 */         return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/*     */       case WATER:
/*  80 */         return ((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue();
/*     */       case AIR:
/*  82 */         return ((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue();
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  90 */     if (!this.type.canOpenByHand()) {
/*  91 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  94 */     toggle(paramBlockState, paramLevel, paramBlockPos, paramPlayer);
/*  95 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 100 */     if (paramExplosion.canTriggerBlocks() && this.type.canOpenByWindCharge() && !((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 101 */       toggle(paramBlockState, (Level)paramServerLevel, paramBlockPos, (Player)null);
/*     */     }
/* 103 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*     */   }
/*     */   
/*     */   private void toggle(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 107 */     BlockState blockState = (BlockState)paramBlockState.cycle((Property)OPEN);
/* 108 */     paramLevel.setBlock(paramBlockPos, blockState, 2);
/*     */     
/* 110 */     if (((Boolean)blockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 111 */       paramLevel.scheduleTick(paramBlockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)paramLevel));
/*     */     }
/*     */     
/* 114 */     playSound(paramPlayer, paramLevel, paramBlockPos, ((Boolean)blockState.getValue((Property)OPEN)).booleanValue());
/*     */   }
/*     */   
/*     */   protected void playSound(Player paramPlayer, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 118 */     paramLevel.playSound((Entity)paramPlayer, paramBlockPos, paramBoolean ? this.type.trapdoorOpen() : this.type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, paramLevel.getRandom().nextFloat() * 0.1F + 0.9F);
/* 119 */     paramLevel.gameEvent((Entity)paramPlayer, paramBoolean ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 124 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     boolean bool = paramLevel.hasNeighborSignal(paramBlockPos);
/* 129 */     if (bool != ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 130 */       if (((Boolean)paramBlockState.getValue((Property)OPEN)).booleanValue() != bool) {
/* 131 */         paramBlockState = (BlockState)paramBlockState.setValue((Property)OPEN, Boolean.valueOf(bool));
/* 132 */         playSound((Player)null, paramLevel, paramBlockPos, bool);
/*     */       } 
/* 134 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool)), 2);
/*     */       
/* 136 */       if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 137 */         paramLevel.scheduleTick(paramBlockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)paramLevel));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 144 */     BlockState blockState = defaultBlockState();
/* 145 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */     
/* 147 */     Direction direction = paramBlockPlaceContext.getClickedFace();
/* 148 */     if (paramBlockPlaceContext.replacingClickedOnBlock() || !direction.getAxis().isHorizontal()) {
/* 149 */       blockState = (BlockState)((BlockState)blockState.setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite())).setValue((Property)HALF, (direction == Direction.UP) ? (Comparable)Half.BOTTOM : (Comparable)Half.TOP);
/*     */     } else {
/* 151 */       blockState = (BlockState)((BlockState)blockState.setValue((Property)FACING, (Comparable)direction)).setValue((Property)HALF, ((paramBlockPlaceContext.getClickLocation()).y - paramBlockPlaceContext.getClickedPos().getY() > 0.5D) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM);
/*     */     } 
/* 153 */     if (paramBlockPlaceContext.getLevel().hasNeighborSignal(paramBlockPlaceContext.getClickedPos())) {
/* 154 */       blockState = (BlockState)((BlockState)blockState.setValue((Property)OPEN, Boolean.valueOf(true))).setValue((Property)POWERED, Boolean.valueOf(true));
/*     */     }
/* 156 */     return (BlockState)blockState.setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 161 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)HALF, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 166 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 167 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 169 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 174 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 175 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 178 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   protected BlockSetType getType() {
/* 182 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TrapDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */