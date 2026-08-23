/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.entity.vehicle.VehicleEntity;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractMinecart extends VehicleEntity {
/*  51 */   private static final Vec3 LOWERED_PASSENGER_ATTACHMENT = new Vec3(0.0D, 0.0D, 0.0D);
/*     */   
/*  53 */   private static final EntityDataAccessor<Optional<BlockState>> DATA_ID_CUSTOM_DISPLAY_BLOCK = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
/*  54 */   private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*  55 */   private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, 
/*  56 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.CROUCHING, 
/*  57 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.SWIMMING, 
/*  58 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1)));
/*     */   protected static final float WATER_SLOWDOWN_FACTOR = 0.95F;
/*     */   private static final boolean DEFAULT_FLIPPED_ROTATION = false;
/*     */   private boolean onRails;
/*     */   private boolean flipped = false;
/*     */   private final MinecartBehavior behavior;
/*     */   private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS;
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> paramEntityType, Level paramLevel) {
/*  67 */     super(paramEntityType, paramLevel);
/*  68 */     this.blocksBuilding = true;
/*  69 */     if (useExperimentalMovement(paramLevel)) {
/*  70 */       this.behavior = new NewMinecartBehavior(this);
/*     */     } else {
/*  72 */       this.behavior = new OldMinecartBehavior(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> paramEntityType, Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  77 */     this(paramEntityType, paramLevel);
/*  78 */     setInitialPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */   
/*     */   public void setInitialPos(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  82 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */ 
/*     */     
/*  85 */     this.xo = paramDouble1;
/*  86 */     this.yo = paramDouble2;
/*  87 */     this.zo = paramDouble3;
/*     */   }
/*     */   
/*     */   public static <T extends AbstractMinecart> T createMinecart(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, EntityType<T> paramEntityType, EntitySpawnReason paramEntitySpawnReason, ItemStack paramItemStack, Player paramPlayer) {
/*  91 */     AbstractMinecart abstractMinecart = (AbstractMinecart)paramEntityType.create(paramLevel, paramEntitySpawnReason);
/*  92 */     if (abstractMinecart != null) {
/*  93 */       abstractMinecart.setInitialPos(paramDouble1, paramDouble2, paramDouble3);
/*  94 */       EntityType.createDefaultStackConfig(paramLevel, paramItemStack, (LivingEntity)paramPlayer).accept(abstractMinecart);
/*     */       
/*  96 */       MinecartBehavior minecartBehavior = abstractMinecart.getBehavior(); if (minecartBehavior instanceof NewMinecartBehavior) { NewMinecartBehavior newMinecartBehavior = (NewMinecartBehavior)minecartBehavior;
/*  97 */         BlockPos blockPos = abstractMinecart.getCurrentBlockPosOrRailBelow();
/*  98 */         BlockState blockState = paramLevel.getBlockState(blockPos);
/*  99 */         newMinecartBehavior.adjustToRails(blockPos, blockState, true); }
/*     */     
/*     */     } 
/* 102 */     return (T)abstractMinecart;
/*     */   }
/*     */   
/*     */   public MinecartBehavior getBehavior() {
/* 106 */     return this.behavior;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 111 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 116 */     super.defineSynchedData(paramBuilder);
/* 117 */     paramBuilder.define(DATA_ID_CUSTOM_DISPLAY_BLOCK, Optional.empty());
/* 118 */     paramBuilder.define(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(getDefaultDisplayOffset()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideWith(Entity paramEntity) {
/* 123 */     return AbstractBoat.canVehicleCollide((Entity)this, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRelativePortalPosition(Direction.Axis paramAxis, BlockUtil.FoundRectangle paramFoundRectangle) {
/* 133 */     return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(paramAxis, paramFoundRectangle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity paramEntity, EntityDimensions paramEntityDimensions, float paramFloat) {
/* 140 */     boolean bool = (paramEntity instanceof net.minecraft.world.entity.npc.villager.Villager || paramEntity instanceof net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader) ? true : false;
/* 141 */     if (bool) {
/* 142 */       return LOWERED_PASSENGER_ATTACHMENT;
/*     */     }
/* 144 */     return super.getPassengerAttachmentPoint(paramEntity, paramEntityDimensions, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity paramLivingEntity) {
/* 149 */     Direction direction = getMotionDirection();
/* 150 */     if (direction.getAxis() == Direction.Axis.Y) {
/* 151 */       return super.getDismountLocationForPassenger(paramLivingEntity);
/*     */     }
/*     */     
/* 154 */     int[][] arrayOfInt = DismountHelper.offsetsForDirection(direction);
/* 155 */     BlockPos blockPos = blockPosition();
/* 156 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 158 */     ImmutableList immutableList = paramLivingEntity.getDismountPoses();
/*     */     
/* 160 */     for (UnmodifiableIterator<Pose> unmodifiableIterator1 = immutableList.iterator(); unmodifiableIterator1.hasNext(); ) { Pose pose = unmodifiableIterator1.next();
/* 161 */       EntityDimensions entityDimensions = paramLivingEntity.getDimensions(pose);
/*     */ 
/*     */       
/* 164 */       float f = Math.min(entityDimensions.width(), 1.0F) / 2.0F;
/*     */       
/* 166 */       for (UnmodifiableIterator<Integer> unmodifiableIterator = ((ImmutableList)POSE_DISMOUNT_HEIGHTS.get(pose)).iterator(); unmodifiableIterator.hasNext(); ) { int i = ((Integer)unmodifiableIterator.next()).intValue();
/* 167 */         for (int[] arrayOfInt1 : arrayOfInt) {
/* 168 */           mutableBlockPos.set(blockPos.getX() + arrayOfInt1[0], blockPos.getY() + i, blockPos.getZ() + arrayOfInt1[1]);
/*     */           
/* 170 */           double d1 = level().getBlockFloorHeight(DismountHelper.nonClimbableShape((BlockGetter)level(), (BlockPos)mutableBlockPos), () -> DismountHelper.nonClimbableShape((BlockGetter)level(), paramMutableBlockPos.below()));
/* 171 */           if (DismountHelper.isBlockFloorValid(d1)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 177 */             AABB aABB = new AABB(-f, 0.0D, -f, f, entityDimensions.height(), f);
/*     */ 
/*     */             
/* 180 */             Vec3 vec3 = Vec3.upFromBottomCenterOf((Vec3i)mutableBlockPos, d1);
/* 181 */             if (DismountHelper.canDismountTo((CollisionGetter)level(), paramLivingEntity, aABB.move(vec3))) {
/* 182 */               paramLivingEntity.setPose(pose);
/* 183 */               return vec3;
/*     */             } 
/*     */           } 
/*     */         }  }
/*     */        }
/*     */     
/* 189 */     double d = (getBoundingBox()).maxY;
/* 190 */     mutableBlockPos.set(blockPos.getX(), d, blockPos.getZ());
/*     */     
/* 192 */     for (UnmodifiableIterator<Pose> unmodifiableIterator2 = immutableList.iterator(); unmodifiableIterator2.hasNext(); ) { Pose pose = unmodifiableIterator2.next();
/* 193 */       double d1 = paramLivingEntity.getDimensions(pose).height();
/* 194 */       int i = Mth.ceil(d - mutableBlockPos.getY() + d1);
/* 195 */       double d2 = DismountHelper.findCeilingFrom((BlockPos)mutableBlockPos, i, paramBlockPos -> level().getBlockState(paramBlockPos).getCollisionShape((BlockGetter)level(), paramBlockPos));
/*     */       
/* 197 */       if (d + d1 <= d2) {
/* 198 */         paramLivingEntity.setPose(pose);
/*     */         
/*     */         break;
/*     */       }  }
/*     */     
/* 203 */     return super.getDismountLocationForPassenger(paramLivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getBlockSpeedFactor() {
/* 208 */     BlockState blockState = level().getBlockState(blockPosition());
/* 209 */     if (blockState.is(BlockTags.RAILS)) {
/* 210 */       return 1.0F;
/*     */     }
/* 212 */     return super.getBlockSpeedFactor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateHurt(float paramFloat) {
/* 217 */     setHurtDir(-getHurtDir());
/* 218 */     setHurtTime(10);
/* 219 */     setDamage(getDamage() + getDamage() * 10.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 224 */     return !isRemoved();
/*     */   }
/*     */   static {
/* 227 */     EXITS = Maps.newEnumMap((Map)Util.make(() -> {
/*     */             Vec3i vec3i1 = Direction.WEST.getUnitVec3i();
/*     */             Vec3i vec3i2 = Direction.EAST.getUnitVec3i();
/*     */             Vec3i vec3i3 = Direction.NORTH.getUnitVec3i();
/*     */             Vec3i vec3i4 = Direction.SOUTH.getUnitVec3i();
/*     */             Vec3i vec3i5 = vec3i1.below();
/*     */             Vec3i vec3i6 = vec3i2.below();
/*     */             Vec3i vec3i7 = vec3i3.below();
/*     */             Vec3i vec3i8 = vec3i4.below();
/*     */             return ImmutableMap.of(RailShape.NORTH_SOUTH, Pair.of(vec3i3, vec3i4), RailShape.EAST_WEST, Pair.of(vec3i1, vec3i2), RailShape.ASCENDING_EAST, Pair.of(vec3i5, vec3i2), RailShape.ASCENDING_WEST, Pair.of(vec3i1, vec3i6), RailShape.ASCENDING_NORTH, Pair.of(vec3i3, vec3i8), RailShape.ASCENDING_SOUTH, Pair.of(vec3i7, vec3i4), RailShape.SOUTH_EAST, Pair.of(vec3i4, vec3i2), RailShape.SOUTH_WEST, Pair.of(vec3i4, vec3i1), RailShape.NORTH_WEST, Pair.of(vec3i3, vec3i1), RailShape.NORTH_EAST, Pair.of(vec3i3, vec3i2));
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pair<Vec3i, Vec3i> exits(RailShape paramRailShape) {
/* 253 */     return EXITS.get(paramRailShape);
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getMotionDirection() {
/* 258 */     return this.behavior.getMotionDirection();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 263 */     return isInWater() ? 0.005D : 0.04D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 268 */     if (getHurtTime() > 0) {
/* 269 */       setHurtTime(getHurtTime() - 1);
/*     */     }
/* 271 */     if (getDamage() > 0.0F) {
/* 272 */       setDamage(getDamage() - 1.0F);
/*     */     }
/* 274 */     checkBelowWorld();
/* 275 */     computeSpeed();
/*     */     
/* 277 */     handlePortal();
/*     */     
/* 279 */     this.behavior.tick();
/*     */     
/* 281 */     updateInWaterStateAndDoFluidPushing();
/*     */     
/* 283 */     if (isInLava()) {
/* 284 */       lavaIgnite();
/* 285 */       lavaHurt();
/* 286 */       this.fallDistance *= 0.5D;
/*     */     } 
/*     */     
/* 289 */     this.firstTick = false;
/*     */   }
/*     */   
/*     */   public boolean isFirstTick() {
/* 293 */     return this.firstTick;
/*     */   }
/*     */   
/*     */   public BlockPos getCurrentBlockPosOrRailBelow() {
/* 297 */     int i = Mth.floor(getX());
/* 298 */     int j = Mth.floor(getY());
/* 299 */     int k = Mth.floor(getZ());
/*     */     
/* 301 */     if (useExperimentalMovement(level())) {
/* 302 */       double d = getY() - 0.1D - 9.999999747378752E-6D;
/* 303 */       if (level().getBlockState(BlockPos.containing(i, d, k)).is(BlockTags.RAILS)) {
/* 304 */         j = Mth.floor(d);
/*     */       }
/* 306 */     } else if (level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
/* 307 */       j--;
/*     */     } 
/*     */     
/* 310 */     return new BlockPos(i, j, k);
/*     */   }
/*     */   
/*     */   protected double getMaxSpeed(ServerLevel paramServerLevel) {
/* 314 */     return this.behavior.getMaxSpeed(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel paramServerLevel, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {}
/*     */ 
/*     */   
/*     */   public void lerpPositionAndRotationStep(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/* 322 */     super.lerpPositionAndRotationStep(paramInt, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyGravity() {
/* 327 */     super.applyGravity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reapplyPosition() {
/* 332 */     super.reapplyPosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateInWaterStateAndDoFluidPushing() {
/* 337 */     return super.updateInWaterStateAndDoFluidPushing();
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getKnownMovement() {
/* 342 */     return this.behavior.getKnownMovement(super.getKnownMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 347 */     return this.behavior.getInterpolation();
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 352 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 353 */     this.behavior.lerpMotion(getDeltaMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void lerpMotion(Vec3 paramVec3) {
/* 358 */     this.behavior.lerpMotion(paramVec3);
/*     */   }
/*     */   
/*     */   protected void moveAlongTrack(ServerLevel paramServerLevel) {
/* 362 */     this.behavior.moveAlongTrack(paramServerLevel);
/*     */   }
/*     */   
/*     */   protected void comeOffTrack(ServerLevel paramServerLevel) {
/* 366 */     double d = getMaxSpeed(paramServerLevel);
/* 367 */     Vec3 vec3 = getDeltaMovement();
/* 368 */     setDeltaMovement(
/* 369 */         Mth.clamp(vec3.x, -d, d), vec3.y, 
/*     */         
/* 371 */         Mth.clamp(vec3.z, -d, d));
/*     */     
/* 373 */     if (onGround()) {
/* 374 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*     */     }
/* 376 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 378 */     if (!onGround()) {
/* 379 */       setDeltaMovement(getDeltaMovement().scale(0.95D));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double makeStepAlongTrack(BlockPos paramBlockPos, RailShape paramRailShape, double paramDouble) {
/* 387 */     return this.behavior.stepAlongTrack(paramBlockPos, paramRailShape, paramDouble);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType paramMoverType, Vec3 paramVec3) {
/* 392 */     if (useExperimentalMovement(level())) {
/* 393 */       Vec3 vec3 = position().add(paramVec3);
/* 394 */       super.move(paramMoverType, paramVec3);
/* 395 */       boolean bool = this.behavior.pushAndPickupEntities();
/* 396 */       if (bool)
/*     */       {
/* 398 */         super.move(paramMoverType, vec3.subtract(position()));
/*     */       }
/* 400 */       if (paramMoverType.equals(MoverType.PISTON)) {
/* 401 */         this.onRails = false;
/*     */       }
/*     */     } else {
/* 404 */       super.move(paramMoverType, paramVec3);
/*     */       
/* 406 */       applyEffectsFromBlocks();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyEffectsFromBlocks() {
/* 412 */     if (useExperimentalMovement(level())) {
/* 413 */       super.applyEffectsFromBlocks();
/*     */     } else {
/*     */       
/* 416 */       applyEffectsFromBlocks(position(), position());
/* 417 */       clearMovementThisTick();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnRails() {
/* 423 */     return this.onRails;
/*     */   }
/*     */   
/*     */   public void setOnRails(boolean paramBoolean) {
/* 427 */     this.onRails = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isFlipped() {
/* 431 */     return this.flipped;
/*     */   }
/*     */   
/*     */   public void setFlipped(boolean paramBoolean) {
/* 435 */     this.flipped = paramBoolean;
/*     */   }
/*     */   
/*     */   public Vec3 getRedstoneDirection(BlockPos paramBlockPos) {
/* 439 */     BlockState blockState = level().getBlockState(paramBlockPos);
/* 440 */     if (!blockState.is(Blocks.POWERED_RAIL) || !((Boolean)blockState.getValue((Property)PoweredRailBlock.POWERED)).booleanValue()) {
/* 441 */       return Vec3.ZERO;
/*     */     }
/*     */     
/* 444 */     RailShape railShape = (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty());
/*     */     
/* 446 */     if (railShape == RailShape.EAST_WEST) {
/* 447 */       if (isRedstoneConductor(paramBlockPos.west()))
/* 448 */         return new Vec3(1.0D, 0.0D, 0.0D); 
/* 449 */       if (isRedstoneConductor(paramBlockPos.east())) {
/* 450 */         return new Vec3(-1.0D, 0.0D, 0.0D);
/*     */       }
/* 452 */     } else if (railShape == RailShape.NORTH_SOUTH) {
/* 453 */       if (isRedstoneConductor(paramBlockPos.north()))
/* 454 */         return new Vec3(0.0D, 0.0D, 1.0D); 
/* 455 */       if (isRedstoneConductor(paramBlockPos.south())) {
/* 456 */         return new Vec3(0.0D, 0.0D, -1.0D);
/*     */       }
/*     */     } 
/*     */     
/* 460 */     return Vec3.ZERO;
/*     */   }
/*     */   
/*     */   public boolean isRedstoneConductor(BlockPos paramBlockPos) {
/* 464 */     return level().getBlockState(paramBlockPos).isRedstoneConductor((BlockGetter)level(), paramBlockPos);
/*     */   }
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 paramVec3) {
/* 468 */     double d = this.behavior.getSlowdownFactor();
/* 469 */     Vec3 vec3 = paramVec3.multiply(d, 0.0D, d);
/*     */     
/* 471 */     if (isInWater()) {
/* 472 */       vec3 = vec3.scale(0.949999988079071D);
/*     */     }
/* 474 */     return vec3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 479 */     setCustomDisplayBlockState(paramValueInput.read("DisplayState", BlockState.CODEC));
/* 480 */     setDisplayOffset(paramValueInput.getIntOr("DisplayOffset", getDefaultDisplayOffset()));
/*     */     
/* 482 */     this.flipped = paramValueInput.getBooleanOr("FlippedRotation", false);
/* 483 */     this.firstTick = paramValueInput.getBooleanOr("HasTicked", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 488 */     getCustomDisplayBlockState().ifPresent(paramBlockState -> paramValueOutput.store("DisplayState", BlockState.CODEC, paramBlockState));
/* 489 */     int i = getDisplayOffset();
/* 490 */     if (i != getDefaultDisplayOffset()) {
/* 491 */       paramValueOutput.putInt("DisplayOffset", i);
/*     */     }
/* 493 */     paramValueOutput.putBoolean("FlippedRotation", this.flipped);
/* 494 */     paramValueOutput.putBoolean("HasTicked", this.firstTick);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity paramEntity) {
/* 499 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/* 502 */     if (paramEntity.noPhysics || this.noPhysics) {
/*     */       return;
/*     */     }
/*     */     
/* 506 */     if (hasPassenger(paramEntity)) {
/*     */       return;
/*     */     }
/*     */     
/* 510 */     double d1 = paramEntity.getX() - getX();
/* 511 */     double d2 = paramEntity.getZ() - getZ();
/*     */     
/* 513 */     double d3 = d1 * d1 + d2 * d2;
/* 514 */     if (d3 >= 9.999999747378752E-5D) {
/* 515 */       d3 = Math.sqrt(d3);
/* 516 */       d1 /= d3;
/* 517 */       d2 /= d3;
/* 518 */       double d = 1.0D / d3;
/* 519 */       if (d > 1.0D) {
/* 520 */         d = 1.0D;
/*     */       }
/* 522 */       d1 *= d;
/* 523 */       d2 *= d;
/* 524 */       d1 *= 0.10000000149011612D;
/* 525 */       d2 *= 0.10000000149011612D;
/*     */       
/* 527 */       d1 *= 0.5D;
/* 528 */       d2 *= 0.5D;
/*     */       
/* 530 */       if (paramEntity instanceof AbstractMinecart) { AbstractMinecart abstractMinecart = (AbstractMinecart)paramEntity;
/* 531 */         pushOtherMinecart(abstractMinecart, d1, d2); }
/*     */       else
/* 533 */       { push(-d1, 0.0D, -d2);
/* 534 */         paramEntity.push(d1 / 4.0D, 0.0D, d2 / 4.0D); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void pushOtherMinecart(AbstractMinecart paramAbstractMinecart, double paramDouble1, double paramDouble2) {
/*     */     double d1, d2;
/* 542 */     if (useExperimentalMovement(level())) {
/* 543 */       d1 = (getDeltaMovement()).x;
/* 544 */       d2 = (getDeltaMovement()).z;
/*     */     } else {
/* 546 */       d1 = paramAbstractMinecart.getX() - getX();
/* 547 */       d2 = paramAbstractMinecart.getZ() - getZ();
/*     */     } 
/*     */     
/* 550 */     Vec3 vec31 = (new Vec3(d1, 0.0D, d2)).normalize();
/* 551 */     Vec3 vec32 = (new Vec3(Mth.cos((getYRot() * 0.017453292F)), 0.0D, Mth.sin((getYRot() * 0.017453292F)))).normalize();
/*     */     
/* 553 */     double d3 = Math.abs(vec31.dot(vec32));
/*     */     
/* 555 */     if (d3 < 0.800000011920929D && !useExperimentalMovement(level())) {
/*     */       return;
/*     */     }
/*     */     
/* 559 */     Vec3 vec33 = getDeltaMovement();
/* 560 */     Vec3 vec34 = paramAbstractMinecart.getDeltaMovement();
/*     */     
/* 562 */     if (paramAbstractMinecart.isFurnace() && !isFurnace()) {
/* 563 */       setDeltaMovement(vec33.multiply(0.2D, 1.0D, 0.2D));
/* 564 */       push(vec34.x - paramDouble1, 0.0D, vec34.z - paramDouble2);
/* 565 */       paramAbstractMinecart.setDeltaMovement(vec34.multiply(0.95D, 1.0D, 0.95D));
/* 566 */     } else if (!paramAbstractMinecart.isFurnace() && isFurnace()) {
/* 567 */       paramAbstractMinecart.setDeltaMovement(vec34.multiply(0.2D, 1.0D, 0.2D));
/* 568 */       paramAbstractMinecart.push(vec33.x + paramDouble1, 0.0D, vec33.z + paramDouble2);
/* 569 */       setDeltaMovement(vec33.multiply(0.95D, 1.0D, 0.95D));
/*     */     } else {
/* 571 */       double d4 = (vec34.x + vec33.x) / 2.0D;
/* 572 */       double d5 = (vec34.z + vec33.z) / 2.0D;
/*     */       
/* 574 */       setDeltaMovement(vec33.multiply(0.2D, 1.0D, 0.2D));
/* 575 */       push(d4 - paramDouble1, 0.0D, d5 - paramDouble2);
/* 576 */       paramAbstractMinecart.setDeltaMovement(vec34.multiply(0.2D, 1.0D, 0.2D));
/* 577 */       paramAbstractMinecart.push(d4 + paramDouble1, 0.0D, d5 + paramDouble2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getDisplayBlockState() {
/* 582 */     return getCustomDisplayBlockState().orElseGet(this::getDefaultDisplayBlockState);
/*     */   }
/*     */   
/*     */   private Optional<BlockState> getCustomDisplayBlockState() {
/* 586 */     return (Optional<BlockState>)getEntityData().get(DATA_ID_CUSTOM_DISPLAY_BLOCK);
/*     */   }
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/* 590 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public int getDisplayOffset() {
/* 594 */     return ((Integer)getEntityData().get(DATA_ID_DISPLAY_OFFSET)).intValue();
/*     */   }
/*     */   
/*     */   public int getDefaultDisplayOffset() {
/* 598 */     return 6;
/*     */   }
/*     */   
/*     */   public void setCustomDisplayBlockState(Optional<BlockState> paramOptional) {
/* 602 */     getEntityData().set(DATA_ID_CUSTOM_DISPLAY_BLOCK, paramOptional);
/*     */   }
/*     */   
/*     */   public void setDisplayOffset(int paramInt) {
/* 606 */     getEntityData().set(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public static boolean useExperimentalMovement(Level paramLevel) {
/* 610 */     return paramLevel.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRideable() {
/* 617 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFurnace() {
/* 624 */     return false;
/*     */   }
/*     */   
/*     */   public abstract ItemStack getPickResult();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\AbstractMinecart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */