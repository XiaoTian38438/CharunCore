/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonMovingBlockEntity
/*     */   extends BlockEntity {
/*     */   private static final int TICKS_TO_EXTEND = 2;
/*     */   private static final double PUSH_OFFSET = 0.01D;
/*     */   public static final double TICK_MOVEMENT = 0.51D;
/*  37 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private static final float DEFAULT_PROGRESS = 0.0F;
/*     */   private static final boolean DEFAULT_EXTENDING = false;
/*     */   private static final boolean DEFAULT_SOURCE = false;
/*  42 */   private BlockState movedState = DEFAULT_BLOCK_STATE;
/*     */   
/*     */   private Direction direction;
/*     */   
/*     */   private boolean extending = false;
/*     */   private boolean isSourcePiston = false;
/*  48 */   private static final ThreadLocal<Direction> NOCLIP = ThreadLocal.withInitial(() -> null);
/*     */   
/*  50 */   private float progress = 0.0F;
/*  51 */   private float progressO = 0.0F;
/*     */   
/*     */   private long lastTicked;
/*     */   private int deathTicks;
/*     */   
/*     */   public PistonMovingBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  57 */     super(BlockEntityType.PISTON, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public PistonMovingBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection, boolean paramBoolean1, boolean paramBoolean2) {
/*  61 */     this(paramBlockPos, paramBlockState1);
/*  62 */     this.movedState = paramBlockState2;
/*  63 */     this.direction = paramDirection;
/*  64 */     this.extending = paramBoolean1;
/*  65 */     this.isSourcePiston = paramBoolean2;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/*  70 */     return saveCustomOnly(paramProvider);
/*     */   }
/*     */   
/*     */   public boolean isExtending() {
/*  74 */     return this.extending;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/*  78 */     return this.direction;
/*     */   }
/*     */   
/*     */   public boolean isSourcePiston() {
/*  82 */     return this.isSourcePiston;
/*     */   }
/*     */   
/*     */   public float getProgress(float paramFloat) {
/*  86 */     if (paramFloat > 1.0F) {
/*  87 */       paramFloat = 1.0F;
/*     */     }
/*  89 */     return Mth.lerp(paramFloat, this.progressO, this.progress);
/*     */   }
/*     */   
/*     */   public float getXOff(float paramFloat) {
/*  93 */     return this.direction.getStepX() * getExtendedProgress(getProgress(paramFloat));
/*     */   }
/*     */   
/*     */   public float getYOff(float paramFloat) {
/*  97 */     return this.direction.getStepY() * getExtendedProgress(getProgress(paramFloat));
/*     */   }
/*     */   
/*     */   public float getZOff(float paramFloat) {
/* 101 */     return this.direction.getStepZ() * getExtendedProgress(getProgress(paramFloat));
/*     */   }
/*     */   
/*     */   private float getExtendedProgress(float paramFloat) {
/* 105 */     return this.extending ? (paramFloat - 1.0F) : (1.0F - paramFloat);
/*     */   }
/*     */   
/*     */   private BlockState getCollisionRelatedBlockState() {
/* 109 */     if (!isExtending() && isSourcePiston() && this.movedState.getBlock() instanceof PistonBaseBlock) {
/* 110 */       return (BlockState)((BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState()
/* 111 */         .setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((this.progress > 0.25F))))
/* 112 */         .setValue((Property)PistonHeadBlock.TYPE, this.movedState.is(Blocks.STICKY_PISTON) ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT))
/* 113 */         .setValue((Property)PistonHeadBlock.FACING, this.movedState.getValue((Property)PistonBaseBlock.FACING));
/*     */     }
/* 115 */     return this.movedState;
/*     */   }
/*     */   
/*     */   private static void moveCollidedEntities(Level paramLevel, BlockPos paramBlockPos, float paramFloat, PistonMovingBlockEntity paramPistonMovingBlockEntity) {
/* 119 */     Direction direction = paramPistonMovingBlockEntity.getMovementDirection();
/*     */     
/* 121 */     double d = (paramFloat - paramPistonMovingBlockEntity.progress);
/*     */     
/* 123 */     VoxelShape voxelShape = paramPistonMovingBlockEntity.getCollisionRelatedBlockState().getCollisionShape((BlockGetter)paramLevel, paramBlockPos);
/* 124 */     if (voxelShape.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     AABB aABB = moveByPositionAndProgress(paramBlockPos, voxelShape.bounds(), paramPistonMovingBlockEntity);
/* 129 */     List list1 = paramLevel.getEntities(null, PistonMath.getMovementArea(aABB, direction, d).minmax(aABB));
/* 130 */     if (list1.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     List list2 = voxelShape.toAabbs();
/* 135 */     boolean bool = paramPistonMovingBlockEntity.movedState.is(Blocks.SLIME_BLOCK);
/* 136 */     for (Entity entity : list1) {
/* 137 */       if (entity.getPistonPushReaction() == PushReaction.IGNORE) {
/*     */         continue;
/*     */       }
/*     */       
/* 141 */       if (bool) {
/* 142 */         if (entity instanceof net.minecraft.server.level.ServerPlayer) {
/*     */           continue;
/*     */         }
/*     */         
/* 146 */         Vec3 vec3 = entity.getDeltaMovement();
/* 147 */         double d2 = vec3.x;
/* 148 */         double d3 = vec3.y;
/* 149 */         double d4 = vec3.z;
/* 150 */         switch (direction.getAxis()) {
/*     */           case EAST:
/* 152 */             d2 = direction.getStepX();
/*     */             break;
/*     */           case WEST:
/* 155 */             d3 = direction.getStepY();
/*     */             break;
/*     */           case UP:
/* 158 */             d4 = direction.getStepZ();
/*     */             break;
/*     */         } 
/*     */         
/* 162 */         entity.setDeltaMovement(d2, d3, d4);
/*     */       } 
/*     */       
/* 165 */       double d1 = 0.0D;
/* 166 */       for (AABB aABB1 : list2) {
/*     */ 
/*     */         
/* 169 */         AABB aABB2 = PistonMath.getMovementArea(moveByPositionAndProgress(paramBlockPos, aABB1, paramPistonMovingBlockEntity), direction, d);
/*     */         
/* 171 */         AABB aABB3 = entity.getBoundingBox();
/* 172 */         if (!aABB2.intersects(aABB3)) {
/*     */           continue;
/*     */         }
/*     */         
/* 176 */         d1 = Math.max(d1, getMovement(aABB2, direction, aABB3));
/*     */ 
/*     */         
/* 179 */         if (d1 >= d) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 184 */       if (d1 <= 0.0D) {
/*     */         continue;
/*     */       }
/*     */       
/* 188 */       d1 = Math.min(d1, d) + 0.01D;
/* 189 */       moveEntityByPiston(direction, entity, d1, direction);
/*     */       
/* 191 */       if (!paramPistonMovingBlockEntity.extending && paramPistonMovingBlockEntity.isSourcePiston) {
/* 192 */         fixEntityWithinPistonBase(paramBlockPos, entity, direction, d);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void moveEntityByPiston(Direction paramDirection1, Entity paramEntity, double paramDouble, Direction paramDirection2) {
/* 199 */     NOCLIP.set(paramDirection1);
/* 200 */     Vec3 vec3 = paramEntity.position();
/* 201 */     paramEntity.move(MoverType.PISTON, new Vec3(paramDouble * paramDirection2.getStepX(), paramDouble * paramDirection2.getStepY(), paramDouble * paramDirection2.getStepZ()));
/*     */     
/* 203 */     paramEntity.applyEffectsFromBlocks(vec3, paramEntity.position());
/*     */     
/* 205 */     paramEntity.removeLatestMovementRecording();
/* 206 */     NOCLIP.set(null);
/*     */   }
/*     */   
/*     */   private static void moveStuckEntities(Level paramLevel, BlockPos paramBlockPos, float paramFloat, PistonMovingBlockEntity paramPistonMovingBlockEntity) {
/* 210 */     if (!paramPistonMovingBlockEntity.isStickyForEntities()) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     Direction direction = paramPistonMovingBlockEntity.getMovementDirection();
/* 215 */     if (!direction.getAxis().isHorizontal()) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     double d1 = paramPistonMovingBlockEntity.movedState.getCollisionShape((BlockGetter)paramLevel, paramBlockPos).max(Direction.Axis.Y);
/* 220 */     AABB aABB = moveByPositionAndProgress(paramBlockPos, new AABB(0.0D, d1, 0.0D, 1.0D, 1.5000010000000001D, 1.0D), paramPistonMovingBlockEntity);
/*     */     
/* 222 */     double d2 = (paramFloat - paramPistonMovingBlockEntity.progress);
/*     */     
/* 224 */     List list = paramLevel.getEntities((Entity)null, aABB, paramEntity -> matchesStickyCritera(paramAABB, paramEntity, paramBlockPos));
/* 225 */     for (Entity entity : list) {
/* 226 */       moveEntityByPiston(direction, entity, d2, direction);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean matchesStickyCritera(AABB paramAABB, Entity paramEntity, BlockPos paramBlockPos) {
/* 231 */     return (paramEntity.getPistonPushReaction() == PushReaction.NORMAL && paramEntity
/* 232 */       .onGround() && (paramEntity
/* 233 */       .isSupportedBy(paramBlockPos) || (paramEntity
/* 234 */       .getX() >= paramAABB.minX && paramEntity
/* 235 */       .getX() <= paramAABB.maxX && paramEntity
/* 236 */       .getZ() >= paramAABB.minZ && paramEntity
/* 237 */       .getZ() <= paramAABB.maxZ)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isStickyForEntities() {
/* 242 */     return this.movedState.is(Blocks.HONEY_BLOCK);
/*     */   }
/*     */   
/*     */   public Direction getMovementDirection() {
/* 246 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   private static double getMovement(AABB paramAABB1, Direction paramDirection, AABB paramAABB2) {
/* 250 */     switch (paramDirection)
/*     */     { case EAST:
/* 252 */         return paramAABB1.maxX - paramAABB2.minX;
/*     */       case WEST:
/* 254 */         return paramAABB2.maxX - paramAABB1.minX;
/*     */       
/*     */       default:
/* 257 */         return paramAABB1.maxY - paramAABB2.minY;
/*     */       case DOWN:
/* 259 */         return paramAABB2.maxY - paramAABB1.minY;
/*     */       case SOUTH:
/* 261 */         return paramAABB1.maxZ - paramAABB2.minZ;
/*     */       case NORTH:
/* 263 */         break; }  return paramAABB2.maxZ - paramAABB1.minZ;
/*     */   }
/*     */ 
/*     */   
/*     */   private static AABB moveByPositionAndProgress(BlockPos paramBlockPos, AABB paramAABB, PistonMovingBlockEntity paramPistonMovingBlockEntity) {
/* 268 */     double d = paramPistonMovingBlockEntity.getExtendedProgress(paramPistonMovingBlockEntity.progress);
/* 269 */     return paramAABB.move(paramBlockPos
/* 270 */         .getX() + d * paramPistonMovingBlockEntity.direction.getStepX(), paramBlockPos
/* 271 */         .getY() + d * paramPistonMovingBlockEntity.direction.getStepY(), paramBlockPos
/* 272 */         .getZ() + d * paramPistonMovingBlockEntity.direction.getStepZ());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fixEntityWithinPistonBase(BlockPos paramBlockPos, Entity paramEntity, Direction paramDirection, double paramDouble) {
/* 277 */     AABB aABB1 = paramEntity.getBoundingBox();
/* 278 */     AABB aABB2 = Shapes.block().bounds().move(paramBlockPos);
/* 279 */     if (aABB1.intersects(aABB2)) {
/* 280 */       Direction direction = paramDirection.getOpposite();
/*     */ 
/*     */       
/* 283 */       double d1 = getMovement(aABB2, direction, aABB1) + 0.01D;
/* 284 */       double d2 = getMovement(aABB2, direction, aABB1.intersect(aABB2)) + 0.01D;
/*     */       
/* 286 */       if (Math.abs(d1 - d2) < 0.01D) {
/* 287 */         d1 = Math.min(d1, paramDouble) + 0.01D;
/* 288 */         moveEntityByPiston(paramDirection, paramEntity, d1, direction);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getMovedState() {
/* 294 */     return this.movedState;
/*     */   }
/*     */   
/*     */   public void finalTick() {
/* 298 */     if (this.level != null && (this.progressO < 1.0F || this.level.isClientSide())) {
/* 299 */       this.progress = 1.0F;
/* 300 */       this.progressO = this.progress;
/* 301 */       this.level.removeBlockEntity(this.worldPosition);
/* 302 */       setRemoved();
/* 303 */       if (this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
/*     */         BlockState blockState;
/* 305 */         if (this.isSourcePiston) {
/* 306 */           blockState = Blocks.AIR.defaultBlockState();
/*     */         } else {
/* 308 */           blockState = Block.updateFromNeighbourShapes(this.movedState, (LevelAccessor)this.level, this.worldPosition);
/*     */         } 
/* 310 */         this.level.setBlock(this.worldPosition, blockState, 3);
/* 311 */         this.level.neighborChanged(this.worldPosition, blockState.getBlock(), ExperimentalRedstoneUtils.initialOrientation(this.level, getPushDirection(), null));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 318 */     finalTick();
/*     */   }
/*     */   
/*     */   public Direction getPushDirection() {
/* 322 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   public static void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, PistonMovingBlockEntity paramPistonMovingBlockEntity) {
/* 326 */     paramPistonMovingBlockEntity.lastTicked = paramLevel.getGameTime();
/* 327 */     paramPistonMovingBlockEntity.progressO = paramPistonMovingBlockEntity.progress;
/*     */     
/* 329 */     if (paramPistonMovingBlockEntity.progressO >= 1.0F) {
/* 330 */       if (paramLevel.isClientSide() && paramPistonMovingBlockEntity.deathTicks < 5) {
/* 331 */         paramPistonMovingBlockEntity.deathTicks++;
/*     */         return;
/*     */       } 
/* 334 */       paramLevel.removeBlockEntity(paramBlockPos);
/* 335 */       paramPistonMovingBlockEntity.setRemoved();
/* 336 */       if (paramLevel.getBlockState(paramBlockPos).is(Blocks.MOVING_PISTON)) {
/* 337 */         BlockState blockState = Block.updateFromNeighbourShapes(paramPistonMovingBlockEntity.movedState, (LevelAccessor)paramLevel, paramBlockPos);
/* 338 */         if (blockState.isAir()) {
/* 339 */           paramLevel.setBlock(paramBlockPos, paramPistonMovingBlockEntity.movedState, 340);
/* 340 */           Block.updateOrDestroy(paramPistonMovingBlockEntity.movedState, blockState, (LevelAccessor)paramLevel, paramBlockPos, 3);
/*     */         } else {
/* 342 */           if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)blockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 343 */             blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false));
/*     */           }
/* 345 */           paramLevel.setBlock(paramBlockPos, blockState, 67);
/* 346 */           paramLevel.neighborChanged(paramBlockPos, blockState.getBlock(), ExperimentalRedstoneUtils.initialOrientation(paramLevel, paramPistonMovingBlockEntity.getPushDirection(), null));
/*     */         } 
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 352 */     float f = paramPistonMovingBlockEntity.progress + 0.5F;
/* 353 */     moveCollidedEntities(paramLevel, paramBlockPos, f, paramPistonMovingBlockEntity);
/* 354 */     moveStuckEntities(paramLevel, paramBlockPos, f, paramPistonMovingBlockEntity);
/* 355 */     paramPistonMovingBlockEntity.progress = f;
/* 356 */     if (paramPistonMovingBlockEntity.progress >= 1.0F) {
/* 357 */       paramPistonMovingBlockEntity.progress = 1.0F;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/* 363 */     super.loadAdditional(paramValueInput);
/*     */     
/* 365 */     this.movedState = paramValueInput.read("blockState", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE);
/* 366 */     this.direction = paramValueInput.read("facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN);
/* 367 */     this.progress = paramValueInput.getFloatOr("progress", 0.0F);
/* 368 */     this.progressO = this.progress;
/* 369 */     this.extending = paramValueInput.getBooleanOr("extending", false);
/* 370 */     this.isSourcePiston = paramValueInput.getBooleanOr("source", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 375 */     super.saveAdditional(paramValueOutput);
/*     */     
/* 377 */     paramValueOutput.store("blockState", BlockState.CODEC, this.movedState);
/* 378 */     paramValueOutput.store("facing", Direction.LEGACY_ID_CODEC, this.direction);
/* 379 */     paramValueOutput.putFloat("progress", this.progressO);
/* 380 */     paramValueOutput.putBoolean("extending", this.extending);
/* 381 */     paramValueOutput.putBoolean("source", this.isSourcePiston);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*     */     VoxelShape voxelShape;
/*     */     BlockState blockState;
/* 388 */     if (!this.extending && this.isSourcePiston && this.movedState.getBlock() instanceof PistonBaseBlock) {
/* 389 */       voxelShape = ((BlockState)this.movedState.setValue((Property)PistonBaseBlock.EXTENDED, Boolean.valueOf(true))).getCollisionShape(paramBlockGetter, paramBlockPos);
/*     */     } else {
/* 391 */       voxelShape = Shapes.empty();
/*     */     } 
/*     */     
/* 394 */     Direction direction = NOCLIP.get();
/* 395 */     if (this.progress < 1.0D && direction == getMovementDirection()) {
/* 396 */       return voxelShape;
/*     */     }
/*     */ 
/*     */     
/* 400 */     if (isSourcePiston()) {
/* 401 */       blockState = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)this.direction)).setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((this.extending != ((1.0F - this.progress < 0.25F)))));
/*     */     } else {
/* 403 */       blockState = this.movedState;
/*     */     } 
/* 405 */     float f = getExtendedProgress(this.progress);
/* 406 */     double d1 = (this.direction.getStepX() * f);
/* 407 */     double d2 = (this.direction.getStepY() * f);
/* 408 */     double d3 = (this.direction.getStepZ() * f);
/* 409 */     return Shapes.or(voxelShape, blockState.getCollisionShape(paramBlockGetter, paramBlockPos).move(d1, d2, d3));
/*     */   }
/*     */   
/*     */   public long getLastTicked() {
/* 413 */     return this.lastTicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLevel(Level paramLevel) {
/* 418 */     super.setLevel(paramLevel);
/*     */     
/* 420 */     if (paramLevel.holderLookup(Registries.BLOCK).get(this.movedState.getBlock().builtInRegistryHolder().key()).isEmpty())
/* 421 */       this.movedState = Blocks.AIR.defaultBlockState(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\piston\PistonMovingBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */