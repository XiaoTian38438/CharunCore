/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonHeadBlock extends DirectionalBlock {
/*  36 */   public static final MapCodec<PistonHeadBlock> CODEC = simpleCodec(PistonHeadBlock::new);
/*     */ 
/*     */   
/*     */   protected MapCodec<PistonHeadBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;
/*  44 */   public static final BooleanProperty SHORT = BlockStateProperties.SHORT;
/*     */   
/*     */   public static final int PLATFORM_THICKNESS = 4;
/*     */   
/*  48 */   private static final VoxelShape SHAPE_PLATFORM = Block.boxZ(16.0D, 0.0D, 4.0D);
/*  49 */   private static final Map<Direction, VoxelShape> SHAPES_SHORT = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, 
/*     */         
/*  51 */         Block.boxZ(4.0D, 4.0D, 16.0D)));
/*     */ 
/*     */   
/*  54 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, 
/*     */         
/*  56 */         Block.boxZ(4.0D, 4.0D, 20.0D)));
/*     */ 
/*     */   
/*     */   public PistonHeadBlock(BlockBehaviour.Properties paramProperties) {
/*  60 */     super(paramProperties);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT)).setValue((Property)SHORT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  71 */     return (((Boolean)paramBlockState.getValue((Property)SHORT)).booleanValue() ? SHAPES_SHORT : SHAPES).get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   private boolean isFittingBase(BlockState paramBlockState1, BlockState paramBlockState2) {
/*  75 */     Block block = (paramBlockState1.getValue((Property)TYPE) == PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON;
/*  76 */     return (paramBlockState2.is(block) && ((Boolean)paramBlockState2.getValue((Property)PistonBaseBlock.EXTENDED)).booleanValue() && paramBlockState2.getValue((Property)FACING) == paramBlockState1.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/*  81 */     if (!paramLevel.isClientSide() && paramPlayer.preventsBlockDrops()) {
/*  82 */       BlockPos blockPos = paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite());
/*  83 */       if (isFittingBase(paramBlockState, paramLevel.getBlockState(blockPos))) {
/*  84 */         paramLevel.destroyBlock(blockPos, false);
/*     */       }
/*     */     } 
/*  87 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  93 */     BlockPos blockPos = paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite());
/*  94 */     if (isFittingBase(paramBlockState, paramServerLevel.getBlockState(blockPos))) {
/*  95 */       paramServerLevel.destroyBlock(blockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 101 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && 
/* 102 */       !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 103 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 106 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 111 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite()));
/*     */     
/* 113 */     return (isFittingBase(paramBlockState, blockState) || (blockState.is(Blocks.MOVING_PISTON) && blockState.getValue((Property)FACING) == paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 118 */     if (paramBlockState.canSurvive((LevelReader)paramLevel, paramBlockPos)) {
/* 119 */       paramLevel.neighborChanged(paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite()), paramBlock, ExperimentalRedstoneUtils.withFront(paramOrientation, ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 125 */     return new ItemStack((paramBlockState.getValue((Property)TYPE) == PistonType.STICKY) ? (ItemLike)Blocks.STICKY_PISTON : (ItemLike)Blocks.PISTON);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 130 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 135 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 140 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)TYPE, (Property)SHORT });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\piston\PistonHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */