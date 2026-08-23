/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WallSide;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MossyCarpetBlock extends Block implements BonemealableBlock {
/*  34 */   public static final MapCodec<MossyCarpetBlock> CODEC = simpleCodec(MossyCarpetBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<MossyCarpetBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  41 */   public static final BooleanProperty BASE = BlockStateProperties.BOTTOM;
/*  42 */   public static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
/*  43 */   public static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
/*  44 */   public static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
/*  45 */   public static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
/*     */   
/*  47 */   public static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = (Map<Direction, EnumProperty<WallSide>>)ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */ 
/*     */ 
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MossyCarpetBlock(BlockBehaviour.Properties paramProperties) {
/*  57 */     super(paramProperties);
/*  58 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BASE, Boolean.valueOf(true))).setValue((Property)NORTH, (Comparable)WallSide.NONE)).setValue((Property)EAST, (Comparable)WallSide.NONE)).setValue((Property)SOUTH, (Comparable)WallSide.NONE)).setValue((Property)WEST, (Comparable)WallSide.NONE));
/*     */     
/*  60 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   public Function<BlockState, VoxelShape> makeShapes() {
/*  64 */     Map map1 = Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 10.0D, 0.0D, 1.0D));
/*  65 */     Map map2 = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  67 */     return getShapeForEachState(paramBlockState -> {
/*     */           VoxelShape voxelShape = ((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue() ? (VoxelShape)paramMap1.get(Direction.DOWN) : Shapes.empty();
/*     */           for (Map.Entry<Direction, EnumProperty<WallSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*     */             switch ((WallSide)paramBlockState.getValue((Property)entry.getValue())) {
/*     */               case LOW:
/*     */                 voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap2.get(entry.getKey()));
/*     */               case TALL:
/*     */                 voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap1.get(entry.getKey()));
/*     */             } 
/*     */           } 
/*     */           return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  83 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  88 */     return ((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue() ? this.shapes.apply(defaultBlockState()) : Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  98 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/*     */     
/* 100 */     if (((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue()) {
/* 101 */       return !blockState.isAir();
/*     */     }
/*     */     
/* 104 */     return (blockState.is(this) && ((Boolean)blockState.getValue((Property)BASE)).booleanValue());
/*     */   }
/*     */   
/*     */   private static boolean hasFaces(BlockState paramBlockState) {
/* 108 */     if (((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue()) {
/* 109 */       return true;
/*     */     }
/* 111 */     for (EnumProperty<WallSide> enumProperty : PROPERTY_BY_DIRECTION.values()) {
/* 112 */       if (paramBlockState.getValue((Property)enumProperty) != WallSide.NONE) {
/* 113 */         return true;
/*     */       }
/*     */     } 
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean canSupportAtFace(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 120 */     if (paramDirection == Direction.UP) {
/* 121 */       return false;
/*     */     }
/* 123 */     return MultifaceBlock.canAttachTo(paramBlockGetter, paramBlockPos, paramDirection);
/*     */   }
/*     */   
/*     */   private static BlockState getUpdatedState(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, boolean paramBoolean) {
/* 127 */     BlockState blockState1 = null;
/* 128 */     BlockState blockState2 = null;
/*     */ 
/*     */     
/* 131 */     paramBoolean |= ((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue();
/*     */     
/* 133 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 134 */       EnumProperty<WallSide> enumProperty = getPropertyForFace(direction);
/* 135 */       WallSide wallSide = canSupportAtFace(paramBlockGetter, paramBlockPos, direction) ? (paramBoolean ? WallSide.LOW : (WallSide)paramBlockState.getValue((Property)enumProperty)) : WallSide.NONE;
/* 136 */       if (wallSide == WallSide.LOW) {
/* 137 */         if (blockState1 == null) {
/* 138 */           blockState1 = paramBlockGetter.getBlockState(paramBlockPos.above());
/*     */         }
/* 140 */         if (blockState1.is(Blocks.PALE_MOSS_CARPET) && blockState1.getValue((Property)enumProperty) != WallSide.NONE && !((Boolean)blockState1.getValue((Property)BASE)).booleanValue()) {
/* 141 */           wallSide = WallSide.TALL;
/*     */         }
/* 143 */         if (!((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue()) {
/* 144 */           if (blockState2 == null) {
/* 145 */             blockState2 = paramBlockGetter.getBlockState(paramBlockPos.below());
/*     */           }
/* 147 */           if (blockState2.is(Blocks.PALE_MOSS_CARPET) && blockState2.getValue((Property)enumProperty) == WallSide.NONE) {
/* 148 */             wallSide = WallSide.NONE;
/*     */           }
/*     */         } 
/*     */       } 
/* 152 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)enumProperty, (Comparable)wallSide);
/*     */     } 
/* 154 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 159 */     return getUpdatedState(defaultBlockState(), (BlockGetter)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos(), true);
/*     */   }
/*     */   
/*     */   public static void placeAt(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, @UpdateFlags int paramInt) {
/* 163 */     BlockState blockState1 = Blocks.PALE_MOSS_CARPET.defaultBlockState();
/* 164 */     BlockState blockState2 = getUpdatedState(blockState1, (BlockGetter)paramLevelAccessor, paramBlockPos, true);
/* 165 */     paramLevelAccessor.setBlock(paramBlockPos, blockState2, paramInt);
/* 166 */     Objects.requireNonNull(paramRandomSource); BlockState blockState3 = createTopperWithSideChance((BlockGetter)paramLevelAccessor, paramBlockPos, paramRandomSource::nextBoolean);
/* 167 */     if (!blockState3.isAir()) {
/* 168 */       paramLevelAccessor.setBlock(paramBlockPos.above(), blockState3, paramInt);
/* 169 */       BlockState blockState = getUpdatedState(blockState2, (BlockGetter)paramLevelAccessor, paramBlockPos, true);
/* 170 */       paramLevelAccessor.setBlock(paramBlockPos, blockState, paramInt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 177 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     RandomSource randomSource = paramLevel.getRandom();
/* 182 */     Objects.requireNonNull(randomSource); BlockState blockState = createTopperWithSideChance((BlockGetter)paramLevel, paramBlockPos, randomSource::nextBoolean);
/* 183 */     if (!blockState.isAir()) {
/* 184 */       paramLevel.setBlock(paramBlockPos.above(), blockState, 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private static BlockState createTopperWithSideChance(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BooleanSupplier paramBooleanSupplier) {
/* 189 */     BlockPos blockPos = paramBlockPos.above();
/* 190 */     BlockState blockState1 = paramBlockGetter.getBlockState(blockPos);
/* 191 */     boolean bool = blockState1.is(Blocks.PALE_MOSS_CARPET);
/* 192 */     if ((bool && ((Boolean)blockState1.getValue((Property)BASE)).booleanValue()) || (!bool && !blockState1.canBeReplaced())) {
/* 193 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 195 */     BlockState blockState2 = (BlockState)Blocks.PALE_MOSS_CARPET.defaultBlockState().setValue((Property)BASE, Boolean.valueOf(false));
/* 196 */     BlockState blockState3 = getUpdatedState(blockState2, paramBlockGetter, paramBlockPos.above(), true);
/* 197 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 198 */       EnumProperty<WallSide> enumProperty = getPropertyForFace(direction);
/* 199 */       if (blockState3.getValue((Property)enumProperty) != WallSide.NONE && !paramBooleanSupplier.getAsBoolean()) {
/* 200 */         blockState3 = (BlockState)blockState3.setValue((Property)enumProperty, (Comparable)WallSide.NONE);
/*     */       }
/*     */     } 
/* 203 */     if (hasFaces(blockState3) && blockState3 != blockState1) {
/* 204 */       return blockState3;
/*     */     }
/* 206 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 211 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 212 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 215 */     BlockState blockState = getUpdatedState(paramBlockState1, (BlockGetter)paramLevelReader, paramBlockPos1, false);
/*     */     
/* 217 */     if (!hasFaces(blockState)) {
/* 218 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 221 */     return blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 226 */     paramBuilder.add(new Property[] { (Property)BASE, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 231 */     switch (paramRotation) { case NONE: case LOW: case TALL:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 238 */       paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 244 */     switch (paramMirror) { case NONE: case LOW:  }  return 
/*     */ 
/*     */       
/* 247 */       super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumProperty<WallSide> getPropertyForFace(Direction paramDirection) {
/* 252 */     return PROPERTY_BY_DIRECTION.get(paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 257 */     return (((Boolean)paramBlockState.getValue((Property)BASE)).booleanValue() && !createTopperWithSideChance((BlockGetter)paramLevelReader, paramBlockPos, () -> true).isAir());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 262 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 267 */     BlockState blockState = createTopperWithSideChance((BlockGetter)paramServerLevel, paramBlockPos, () -> true);
/* 268 */     if (!blockState.isAir())
/* 269 */       paramServerLevel.setBlock(paramBlockPos.above(), blockState, 3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MossyCarpetBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */