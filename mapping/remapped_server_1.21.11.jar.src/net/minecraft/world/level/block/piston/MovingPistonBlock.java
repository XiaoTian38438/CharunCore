/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collections;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseEntityBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MovingPistonBlock extends BaseEntityBlock {
/*  38 */   public static final MapCodec<MovingPistonBlock> CODEC = simpleCodec(MovingPistonBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<MovingPistonBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final EnumProperty<Direction> FACING = PistonHeadBlock.FACING;
/*  46 */   public static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;
/*     */   
/*     */   public MovingPistonBlock(BlockBehaviour.Properties paramProperties) {
/*  49 */     super(paramProperties);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockEntity newMovingBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection, boolean paramBoolean1, boolean paramBoolean2) {
/*  60 */     return new PistonMovingBlockEntity(paramBlockPos, paramBlockState1, paramBlockState2, paramDirection, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/*  65 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.PISTON, PistonMovingBlockEntity::tick);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  71 */     BlockPos blockPos = paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite());
/*  72 */     BlockState blockState = paramLevelAccessor.getBlockState(blockPos);
/*  73 */     if (blockState.getBlock() instanceof PistonBaseBlock && ((Boolean)blockState.getValue((Property)PistonBaseBlock.EXTENDED)).booleanValue()) {
/*  74 */       paramLevelAccessor.removeBlock(blockPos, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  81 */     if (!paramLevel.isClientSide() && paramLevel.getBlockEntity(paramBlockPos) == null) {
/*     */       
/*  83 */       paramLevel.removeBlock(paramBlockPos, false);
/*  84 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*  86 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState paramBlockState, LootParams.Builder paramBuilder) {
/*  92 */     PistonMovingBlockEntity pistonMovingBlockEntity = getBlockEntity((BlockGetter)paramBuilder.getLevel(), BlockPos.containing((Position)paramBuilder.getParameter(LootContextParams.ORIGIN)));
/*  93 */     if (pistonMovingBlockEntity == null) {
/*  94 */       return Collections.emptyList();
/*     */     }
/*     */     
/*  97 */     return pistonMovingBlockEntity.getMovedState().getDrops(paramBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 103 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 108 */     PistonMovingBlockEntity pistonMovingBlockEntity = getBlockEntity(paramBlockGetter, paramBlockPos);
/* 109 */     if (pistonMovingBlockEntity != null) {
/* 110 */       return pistonMovingBlockEntity.getCollisionShape(paramBlockGetter, paramBlockPos);
/*     */     }
/* 112 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   private PistonMovingBlockEntity getBlockEntity(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 116 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/* 117 */     if (blockEntity instanceof PistonMovingBlockEntity) {
/* 118 */       return (PistonMovingBlockEntity)blockEntity;
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 125 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 130 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 135 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 140 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 145 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)TYPE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\piston\MovingPistonBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */