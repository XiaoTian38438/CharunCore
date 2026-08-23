/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class AmethystClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  29 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("height").forGetter(()), (App)Codec.FLOAT.fieldOf("width").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, AmethystClusterBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<AmethystClusterBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<AmethystClusterBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  41 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/*     */   
/*     */   private final float height;
/*     */   
/*     */   private final float width;
/*     */   private final Map<Direction, VoxelShape> shapes;
/*     */   
/*     */   public AmethystClusterBlock(float paramFloat1, float paramFloat2, BlockBehaviour.Properties paramProperties) {
/*  49 */     super(paramProperties);
/*  50 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.UP));
/*  51 */     this.shapes = Shapes.rotateAll(Block.boxZ(paramFloat2, (16.0F - paramFloat1), 16.0D));
/*     */     
/*  53 */     this.height = paramFloat1;
/*  54 */     this.width = paramFloat2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  59 */     return this.shapes.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  64 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*  65 */     BlockPos blockPos = paramBlockPos.relative(direction.getOpposite());
/*  66 */     return paramLevelReader.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramLevelReader, blockPos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  71 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  72 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  75 */     if (paramDirection == ((Direction)paramBlockState1.getValue((Property)FACING)).getOpposite() && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  76 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  79 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  84 */     Level level = paramBlockPlaceContext.getLevel();
/*  85 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*  86 */     return (BlockState)((BlockState)defaultBlockState()
/*  87 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((level.getFluidState(blockPos).getType() == Fluids.WATER))))
/*  88 */       .setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getClickedFace());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  93 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/*  98 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 103 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 104 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 106 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 111 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AmethystClusterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */