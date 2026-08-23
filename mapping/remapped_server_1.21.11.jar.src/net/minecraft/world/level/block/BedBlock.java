/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.BedRule;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BedBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BedBlock extends HorizontalDirectionalBlock implements EntityBlock {
/*     */   static {
/*  52 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)DyeColor.CODEC.fieldOf("color").forGetter(BedBlock::getColor), (App)propertiesCodec()).apply((Applicative)paramInstance, BedBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<BedBlock> CODEC;
/*     */   
/*     */   public MapCodec<BedBlock> codec() {
/*  59 */     return CODEC;
/*     */   }
/*     */   
/*  62 */   public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
/*  63 */   public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED; private static final Map<Direction, VoxelShape> SHAPES; private final DyeColor color;
/*     */   static {
/*  65 */     SHAPES = (Map<Direction, VoxelShape>)Util.make(() -> {
/*     */           VoxelShape voxelShape1 = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D);
/*     */           VoxelShape voxelShape2 = Shapes.rotate(voxelShape1, OctahedralGroup.BLOCK_ROT_Y_90);
/*     */           return Shapes.rotateHorizontal(Shapes.or(Block.column(16.0D, 3.0D, 9.0D), new VoxelShape[] { voxelShape1, voxelShape2 }));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BedBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/*  79 */     super(paramProperties);
/*  80 */     this.color = paramDyeColor;
/*  81 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PART, (Comparable)BedPart.FOOT)).setValue((Property)OCCUPIED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public static Direction getBedOrientation(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  85 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos);
/*  86 */     return (blockState.getBlock() instanceof BedBlock) ? (Direction)blockState.getValue((Property)FACING) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  91 */     if (paramLevel.isClientSide()) {
/*  92 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     }
/*     */     
/*  95 */     if (paramBlockState.getValue((Property)PART) != BedPart.HEAD) {
/*     */       
/*  97 */       paramBlockPos = paramBlockPos.relative((Direction)paramBlockState.getValue((Property)FACING));
/*  98 */       paramBlockState = paramLevel.getBlockState(paramBlockPos);
/*  99 */       if (!paramBlockState.is(this)) {
/* 100 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/* 104 */     BedRule bedRule = (BedRule)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, paramBlockPos);
/* 105 */     if (bedRule.explodes()) {
/* 106 */       bedRule.errorMessage().ifPresent(paramComponent -> paramPlayer.displayClientMessage(paramComponent, true));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 111 */       paramLevel.removeBlock(paramBlockPos, false);
/*     */ 
/*     */       
/* 114 */       BlockPos blockPos = paramBlockPos.relative(((Direction)paramBlockState.getValue((Property)FACING)).getOpposite());
/* 115 */       if (paramLevel.getBlockState(blockPos).is(this)) {
/* 116 */         paramLevel.removeBlock(blockPos, false);
/*     */       }
/*     */       
/* 119 */       Vec3 vec3 = paramBlockPos.getCenter();
/* 120 */       paramLevel.explode(null, paramLevel.damageSources().badRespawnPointExplosion(vec3), null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
/* 121 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 124 */     if (((Boolean)paramBlockState.getValue((Property)OCCUPIED)).booleanValue()) {
/* 125 */       if (!kickVillagerOutOfBed(paramLevel, paramBlockPos)) {
/* 126 */         paramPlayer.displayClientMessage((Component)Component.translatable("block.minecraft.bed.occupied"), true);
/*     */       }
/* 128 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 131 */     paramPlayer.startSleepInBed(paramBlockPos)
/* 132 */       .ifLeft(paramBedSleepingProblem -> {
/*     */           if (paramBedSleepingProblem.message() != null) {
/*     */             paramPlayer.displayClientMessage(paramBedSleepingProblem.message(), true);
/*     */           }
/*     */         });
/* 137 */     return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean kickVillagerOutOfBed(Level paramLevel, BlockPos paramBlockPos) {
/* 144 */     List<Villager> list = paramLevel.getEntitiesOfClass(Villager.class, new AABB(paramBlockPos), LivingEntity::isSleeping);
/* 145 */     if (list.isEmpty()) {
/* 146 */       return false;
/*     */     }
/* 148 */     ((Villager)list.get(0)).stopSleeping();
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/* 154 */     super.fallOn(paramLevel, paramBlockState, paramBlockPos, paramEntity, paramDouble * 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEntityMovementAfterFallOn(BlockGetter paramBlockGetter, Entity paramEntity) {
/* 159 */     if (paramEntity.isSuppressingBounce()) {
/* 160 */       super.updateEntityMovementAfterFallOn(paramBlockGetter, paramEntity);
/*     */     } else {
/* 162 */       bounceUp(paramEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void bounceUp(Entity paramEntity) {
/* 167 */     Vec3 vec3 = paramEntity.getDeltaMovement();
/* 168 */     if (vec3.y < 0.0D) {
/*     */       
/* 170 */       double d = (paramEntity instanceof LivingEntity) ? 1.0D : 0.8D;
/* 171 */       paramEntity.setDeltaMovement(vec3.x, -vec3.y * 0.6600000262260437D * d, vec3.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 181 */     if (paramDirection == getNeighbourDirection((BedPart)paramBlockState1.getValue((Property)PART), (Direction)paramBlockState1.getValue((Property)FACING))) {
/* 182 */       if (paramBlockState2.is(this) && paramBlockState2.getValue((Property)PART) != paramBlockState1.getValue((Property)PART)) {
/* 183 */         return (BlockState)paramBlockState1.setValue((Property)OCCUPIED, paramBlockState2.getValue((Property)OCCUPIED));
/*     */       }
/* 185 */       return Blocks.AIR.defaultBlockState();
/*     */     } 
/*     */ 
/*     */     
/* 189 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   private static Direction getNeighbourDirection(BedPart paramBedPart, Direction paramDirection) {
/* 193 */     return (paramBedPart == BedPart.FOOT) ? paramDirection : paramDirection.getOpposite();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 198 */     if (!paramLevel.isClientSide() && paramPlayer.preventsBlockDrops()) {
/* 199 */       BedPart bedPart = (BedPart)paramBlockState.getValue((Property)PART);
/* 200 */       if (bedPart == BedPart.FOOT) {
/* 201 */         BlockPos blockPos = paramBlockPos.relative(getNeighbourDirection(bedPart, (Direction)paramBlockState.getValue((Property)FACING)));
/* 202 */         BlockState blockState = paramLevel.getBlockState(blockPos);
/* 203 */         if (blockState.is(this) && blockState.getValue((Property)PART) == BedPart.HEAD) {
/*     */           
/* 205 */           paramLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
/* 206 */           paramLevel.levelEvent((Entity)paramPlayer, 2001, blockPos, Block.getId(blockState));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 216 */     Direction direction = paramBlockPlaceContext.getHorizontalDirection();
/*     */     
/* 218 */     BlockPos blockPos1 = paramBlockPlaceContext.getClickedPos();
/* 219 */     BlockPos blockPos2 = blockPos1.relative(direction);
/* 220 */     Level level = paramBlockPlaceContext.getLevel();
/* 221 */     if (level.getBlockState(blockPos2).canBeReplaced(paramBlockPlaceContext) && level.getWorldBorder().isWithinBounds(blockPos2)) {
/* 222 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)direction);
/*     */     }
/*     */     
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 230 */     return SHAPES.get(getConnectedDirection(paramBlockState).getOpposite());
/*     */   }
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState paramBlockState) {
/* 234 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 235 */     return (paramBlockState.getValue((Property)PART) == BedPart.HEAD) ? direction.getOpposite() : direction;
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState paramBlockState) {
/* 239 */     BedPart bedPart = (BedPart)paramBlockState.getValue((Property)PART);
/* 240 */     if (bedPart == BedPart.HEAD) {
/* 241 */       return DoubleBlockCombiner.BlockType.FIRST;
/*     */     }
/* 243 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */   
/*     */   private static boolean isBunkBed(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 247 */     return paramBlockGetter.getBlockState(paramBlockPos.below()).getBlock() instanceof BedBlock;
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> findStandUpPosition(EntityType<?> paramEntityType, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos, Direction paramDirection, float paramFloat) {
/* 251 */     Direction direction1 = paramDirection.getClockWise();
/* 252 */     Direction direction2 = direction1.isFacingAngle(paramFloat) ? direction1.getOpposite() : direction1;
/*     */     
/* 254 */     if (isBunkBed((BlockGetter)paramCollisionGetter, paramBlockPos)) {
/* 255 */       return findBunkBedStandUpPosition(paramEntityType, paramCollisionGetter, paramBlockPos, paramDirection, direction2);
/*     */     }
/*     */     
/* 258 */     int[][] arrayOfInt = bedStandUpOffsets(paramDirection, direction2);
/*     */     
/* 260 */     Optional<Vec3> optional = findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, paramBlockPos, arrayOfInt, true);
/* 261 */     if (optional.isPresent()) {
/* 262 */       return optional;
/*     */     }
/* 264 */     return findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, paramBlockPos, arrayOfInt, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> paramEntityType, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos, Direction paramDirection1, Direction paramDirection2) {
/* 268 */     int[][] arrayOfInt1 = bedSurroundStandUpOffsets(paramDirection1, paramDirection2);
/*     */     
/* 270 */     Optional<Vec3> optional1 = findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, paramBlockPos, arrayOfInt1, true);
/* 271 */     if (optional1.isPresent()) {
/* 272 */       return optional1;
/*     */     }
/*     */     
/* 275 */     BlockPos blockPos = paramBlockPos.below();
/*     */     
/* 277 */     Optional<Vec3> optional2 = findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, blockPos, arrayOfInt1, true);
/* 278 */     if (optional2.isPresent()) {
/* 279 */       return optional2;
/*     */     }
/*     */     
/* 282 */     int[][] arrayOfInt2 = bedAboveStandUpOffsets(paramDirection1);
/*     */     
/* 284 */     Optional<Vec3> optional3 = findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, paramBlockPos, arrayOfInt2, true);
/* 285 */     if (optional3.isPresent()) {
/* 286 */       return optional3;
/*     */     }
/*     */     
/* 289 */     Optional<Vec3> optional4 = findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, paramBlockPos, arrayOfInt1, false);
/* 290 */     if (optional4.isPresent()) {
/* 291 */       return optional4;
/*     */     }
/*     */     
/* 294 */     Optional<Vec3> optional5 = findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, blockPos, arrayOfInt1, false);
/* 295 */     if (optional5.isPresent()) {
/* 296 */       return optional5;
/*     */     }
/*     */     
/* 299 */     return findStandUpPositionAtOffset(paramEntityType, paramCollisionGetter, paramBlockPos, arrayOfInt2, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> paramEntityType, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos, int[][] paramArrayOfint, boolean paramBoolean) {
/* 303 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 304 */     for (int[] arrayOfInt : paramArrayOfint) {
/* 305 */       mutableBlockPos.set(paramBlockPos.getX() + arrayOfInt[0], paramBlockPos.getY(), paramBlockPos.getZ() + arrayOfInt[1]);
/*     */       
/* 307 */       Vec3 vec3 = DismountHelper.findSafeDismountLocation(paramEntityType, paramCollisionGetter, (BlockPos)mutableBlockPos, paramBoolean);
/* 308 */       if (vec3 != null) {
/* 309 */         return Optional.of(vec3);
/*     */       }
/*     */     } 
/* 312 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 317 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)PART, (Property)OCCUPIED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 322 */     return (BlockEntity)new BedBlockEntity(paramBlockPos, paramBlockState, this.color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 327 */     super.setPlacedBy(paramLevel, paramBlockPos, paramBlockState, paramLivingEntity, paramItemStack);
/*     */ 
/*     */     
/* 330 */     if (!paramLevel.isClientSide()) {
/* 331 */       BlockPos blockPos = paramBlockPos.relative((Direction)paramBlockState.getValue((Property)FACING));
/* 332 */       paramLevel.setBlock(blockPos, (BlockState)paramBlockState.setValue((Property)PART, (Comparable)BedPart.HEAD), 3);
/*     */       
/* 334 */       paramLevel.updateNeighborsAt(paramBlockPos, Blocks.AIR);
/* 335 */       paramBlockState.updateNeighbourShapes((LevelAccessor)paramLevel, paramBlockPos, 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 340 */     return this.color;
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getSeed(BlockState paramBlockState, BlockPos paramBlockPos) {
/* 345 */     BlockPos blockPos = paramBlockPos.relative((Direction)paramBlockState.getValue((Property)FACING), (paramBlockState.getValue((Property)PART) == BedPart.HEAD) ? 0 : 1);
/* 346 */     return Mth.getSeed(blockPos.getX(), paramBlockPos.getY(), blockPos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedStandUpOffsets(Direction paramDirection1, Direction paramDirection2) {
/* 358 */     return (int[][])ArrayUtils.addAll((Object[])bedSurroundStandUpOffsets(paramDirection1, paramDirection2), (Object[])bedAboveStandUpOffsets(paramDirection1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedSurroundStandUpOffsets(Direction paramDirection1, Direction paramDirection2) {
/* 366 */     return new int[][] { { paramDirection2
/* 367 */           .getStepX(), paramDirection2.getStepZ() }, { paramDirection2
/* 368 */           .getStepX() - paramDirection1.getStepX(), paramDirection2.getStepZ() - paramDirection1.getStepZ() }, { paramDirection2
/* 369 */           .getStepX() - paramDirection1.getStepX() * 2, paramDirection2.getStepZ() - paramDirection1.getStepZ() * 2
/* 370 */         }, { -paramDirection1.getStepX() * 2, -paramDirection1.getStepZ() * 2
/* 371 */         }, { -paramDirection2.getStepX() - paramDirection1.getStepX() * 2, -paramDirection2.getStepZ() - paramDirection1.getStepZ() * 2
/* 372 */         }, { -paramDirection2.getStepX() - paramDirection1.getStepX(), -paramDirection2.getStepZ() - paramDirection1.getStepZ()
/* 373 */         }, { -paramDirection2.getStepX(), -paramDirection2.getStepZ()
/* 374 */         }, { -paramDirection2.getStepX() + paramDirection1.getStepX(), -paramDirection2.getStepZ() + paramDirection1.getStepZ() }, { paramDirection1
/* 375 */           .getStepX(), paramDirection1.getStepZ() }, { paramDirection2
/* 376 */           .getStepX() + paramDirection1.getStepX(), paramDirection2.getStepZ() + paramDirection1.getStepZ() } };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedAboveStandUpOffsets(Direction paramDirection) {
/* 382 */     return new int[][] { { 0, 0
/*     */         },
/* 384 */         { -paramDirection.getStepX(), -paramDirection.getStepZ() } };
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */