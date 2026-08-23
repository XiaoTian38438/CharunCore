/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OldMinecartBehavior
/*     */   extends MinecartBehavior
/*     */ {
/*     */   private static final double MINECART_RIDABLE_THRESHOLD = 0.01D;
/*     */   private static final double MAX_SPEED_IN_WATER = 0.2D;
/*     */   private static final double MAX_SPEED_ON_LAND = 0.4D;
/*     */   private static final double ABSOLUTE_MAX_SPEED = 0.4D;
/*     */   private final InterpolationHandler interpolation;
/*  38 */   private Vec3 targetDeltaMovement = Vec3.ZERO;
/*     */   
/*     */   public OldMinecartBehavior(AbstractMinecart paramAbstractMinecart) {
/*  41 */     super(paramAbstractMinecart);
/*  42 */     this.interpolation = new InterpolationHandler((Entity)paramAbstractMinecart, this::onInterpolation);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/*  47 */     return this.interpolation;
/*     */   }
/*     */   
/*     */   public void onInterpolation(InterpolationHandler paramInterpolationHandler) {
/*  51 */     setDeltaMovement(this.targetDeltaMovement);
/*     */   }
/*     */ 
/*     */   
/*     */   public void lerpMotion(Vec3 paramVec3) {
/*  56 */     this.targetDeltaMovement = paramVec3;
/*  57 */     setDeltaMovement(this.targetDeltaMovement);
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     ServerLevel serverLevel;
/*  62 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  63 */     else { if (this.interpolation.hasActiveInterpolation()) {
/*  64 */         this.interpolation.interpolate();
/*     */       } else {
/*  66 */         this.minecart.reapplyPosition();
/*  67 */         setXRot(getXRot() % 360.0F);
/*  68 */         setYRot(getYRot() % 360.0F);
/*     */       } 
/*     */       
/*     */       return; }
/*     */ 
/*     */     
/*  74 */     this.minecart.applyGravity();
/*     */     
/*  76 */     BlockPos blockPos = this.minecart.getCurrentBlockPosOrRailBelow();
/*  77 */     BlockState blockState = level().getBlockState(blockPos);
/*  78 */     boolean bool = BaseRailBlock.isRail(blockState);
/*  79 */     this.minecart.setOnRails(bool);
/*  80 */     if (bool) {
/*  81 */       moveAlongTrack(serverLevel);
/*     */       
/*  83 */       if (blockState.is(Blocks.ACTIVATOR_RAIL)) {
/*  84 */         this.minecart.activateMinecart(serverLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((Boolean)blockState.getValue((Property)PoweredRailBlock.POWERED)).booleanValue());
/*     */       }
/*     */     } else {
/*  87 */       this.minecart.comeOffTrack(serverLevel);
/*     */     } 
/*     */     
/*  90 */     this.minecart.applyEffectsFromBlocks();
/*     */     
/*  92 */     setXRot(0.0F);
/*  93 */     double d1 = this.minecart.xo - getX();
/*  94 */     double d2 = this.minecart.zo - getZ();
/*  95 */     if (d1 * d1 + d2 * d2 > 0.001D) {
/*  96 */       setYRot((float)(Mth.atan2(d2, d1) * 180.0D / Math.PI));
/*  97 */       if (this.minecart.isFlipped()) {
/*  98 */         setYRot(getYRot() + 180.0F);
/*     */       }
/*     */     } 
/*     */     
/* 102 */     double d3 = Mth.wrapDegrees(getYRot() - this.minecart.yRotO);
/* 103 */     if (d3 < -170.0D || d3 >= 170.0D) {
/* 104 */       setYRot(getYRot() + 180.0F);
/* 105 */       this.minecart.setFlipped(!this.minecart.isFlipped());
/*     */     } 
/*     */     
/* 108 */     setXRot(getXRot() % 360.0F);
/* 109 */     setYRot(getYRot() % 360.0F);
/*     */     
/* 111 */     pushAndPickupEntities();
/*     */   }
/*     */   public void moveAlongTrack(ServerLevel paramServerLevel) {
/*     */     Vec3 vec33;
/*     */     double d14;
/* 116 */     BlockPos blockPos = this.minecart.getCurrentBlockPosOrRailBelow();
/* 117 */     BlockState blockState = level().getBlockState(blockPos);
/* 118 */     this.minecart.resetFallDistance();
/*     */     
/* 120 */     double d1 = this.minecart.getX();
/* 121 */     double d2 = this.minecart.getY();
/* 122 */     double d3 = this.minecart.getZ();
/* 123 */     Vec3 vec31 = getPos(d1, d2, d3);
/* 124 */     d2 = blockPos.getY();
/*     */     
/* 126 */     boolean bool = false;
/* 127 */     boolean bool1 = false;
/*     */     
/* 129 */     if (blockState.is(Blocks.POWERED_RAIL)) {
/* 130 */       bool = ((Boolean)blockState.getValue((Property)PoweredRailBlock.POWERED)).booleanValue();
/* 131 */       bool1 = !bool ? true : false;
/*     */     } 
/*     */     
/* 134 */     double d4 = 0.0078125D;
/* 135 */     if (this.minecart.isInWater()) {
/* 136 */       d4 *= 0.2D;
/*     */     }
/* 138 */     Vec3 vec32 = getDeltaMovement();
/* 139 */     RailShape railShape = (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty());
/* 140 */     switch (railShape) {
/*     */       case ASCENDING_EAST:
/* 142 */         setDeltaMovement(vec32.add(-d4, 0.0D, 0.0D));
/* 143 */         d2++;
/*     */         break;
/*     */       case ASCENDING_WEST:
/* 146 */         setDeltaMovement(vec32.add(d4, 0.0D, 0.0D));
/* 147 */         d2++;
/*     */         break;
/*     */       case ASCENDING_NORTH:
/* 150 */         setDeltaMovement(vec32.add(0.0D, 0.0D, d4));
/* 151 */         d2++;
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/* 154 */         setDeltaMovement(vec32.add(0.0D, 0.0D, -d4));
/* 155 */         d2++;
/*     */         break;
/*     */     } 
/*     */     
/* 159 */     vec32 = getDeltaMovement();
/*     */     
/* 161 */     Pair<Vec3i, Vec3i> pair = AbstractMinecart.exits(railShape);
/* 162 */     Vec3i vec3i1 = (Vec3i)pair.getFirst();
/* 163 */     Vec3i vec3i2 = (Vec3i)pair.getSecond();
/*     */     
/* 165 */     double d5 = (vec3i2.getX() - vec3i1.getX());
/* 166 */     double d6 = (vec3i2.getZ() - vec3i1.getZ());
/* 167 */     double d7 = Math.sqrt(d5 * d5 + d6 * d6);
/*     */     
/* 169 */     double d8 = vec32.x * d5 + vec32.z * d6;
/* 170 */     if (d8 < 0.0D) {
/* 171 */       d5 = -d5;
/* 172 */       d6 = -d6;
/*     */     } 
/*     */     
/* 175 */     double d9 = Math.min(2.0D, vec32.horizontalDistance());
/*     */     
/* 177 */     vec32 = new Vec3(d9 * d5 / d7, vec32.y, d9 * d6 / d7);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     setDeltaMovement(vec32);
/*     */     
/* 184 */     Entity entity1 = this.minecart.getFirstPassenger();
/*     */     
/* 186 */     Entity entity2 = this.minecart.getFirstPassenger(); if (entity2 instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity2;
/* 187 */       vec33 = serverPlayer.getLastClientMoveIntent(); }
/*     */     else
/* 189 */     { vec33 = Vec3.ZERO; }
/*     */ 
/*     */     
/* 192 */     if (entity1 instanceof net.minecraft.world.entity.player.Player && vec33.lengthSqr() > 0.0D) {
/* 193 */       Vec3 vec3 = vec33.normalize();
/* 194 */       double d = getDeltaMovement().horizontalDistanceSqr();
/* 195 */       if (vec3.lengthSqr() > 0.0D && d < 0.01D) {
/* 196 */         setDeltaMovement(getDeltaMovement().add(vec33.x * 0.001D, 0.0D, vec33.z * 0.001D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 202 */         bool1 = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 207 */     if (bool1) {
/* 208 */       double d = getDeltaMovement().horizontalDistance();
/* 209 */       if (d < 0.03D) {
/* 210 */         setDeltaMovement(Vec3.ZERO);
/*     */       } else {
/* 212 */         setDeltaMovement(getDeltaMovement().multiply(0.5D, 0.0D, 0.5D));
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     double d10 = blockPos.getX() + 0.5D + vec3i1.getX() * 0.5D;
/* 217 */     double d11 = blockPos.getZ() + 0.5D + vec3i1.getZ() * 0.5D;
/* 218 */     double d12 = blockPos.getX() + 0.5D + vec3i2.getX() * 0.5D;
/* 219 */     double d13 = blockPos.getZ() + 0.5D + vec3i2.getZ() * 0.5D;
/*     */     
/* 221 */     d5 = d12 - d10;
/* 222 */     d6 = d13 - d11;
/*     */ 
/*     */     
/* 225 */     if (d5 == 0.0D) {
/* 226 */       d14 = d3 - blockPos.getZ();
/* 227 */     } else if (d6 == 0.0D) {
/* 228 */       d14 = d1 - blockPos.getX();
/*     */     } else {
/* 230 */       double d17 = d1 - d10;
/* 231 */       double d18 = d3 - d11;
/*     */       
/* 233 */       d14 = (d17 * d5 + d18 * d6) * 2.0D;
/*     */     } 
/*     */     
/* 236 */     d1 = d10 + d5 * d14;
/* 237 */     d3 = d11 + d6 * d14;
/*     */     
/* 239 */     setPos(d1, d2, d3);
/*     */     
/* 241 */     double d15 = this.minecart.isVehicle() ? 0.75D : 1.0D;
/* 242 */     double d16 = this.minecart.getMaxSpeed(paramServerLevel);
/*     */     
/* 244 */     vec32 = getDeltaMovement();
/* 245 */     this.minecart.move(MoverType.SELF, new Vec3(
/* 246 */           Mth.clamp(d15 * vec32.x, -d16, d16), 0.0D, 
/*     */           
/* 248 */           Mth.clamp(d15 * vec32.z, -d16, d16)));
/*     */ 
/*     */     
/* 251 */     if (vec3i1.getY() != 0 && Mth.floor(this.minecart.getX()) - blockPos.getX() == vec3i1.getX() && Mth.floor(this.minecart.getZ()) - blockPos.getZ() == vec3i1.getZ()) {
/* 252 */       setPos(this.minecart.getX(), this.minecart.getY() + vec3i1.getY(), this.minecart.getZ());
/* 253 */     } else if (vec3i2.getY() != 0 && Mth.floor(this.minecart.getX()) - blockPos.getX() == vec3i2.getX() && Mth.floor(this.minecart.getZ()) - blockPos.getZ() == vec3i2.getZ()) {
/* 254 */       setPos(this.minecart.getX(), this.minecart.getY() + vec3i2.getY(), this.minecart.getZ());
/*     */     } 
/*     */     
/* 257 */     setDeltaMovement(this.minecart.applyNaturalSlowdown(getDeltaMovement()));
/*     */     
/* 259 */     Vec3 vec34 = getPos(this.minecart.getX(), this.minecart.getY(), this.minecart.getZ());
/* 260 */     if (vec34 != null && vec31 != null) {
/* 261 */       double d17 = (vec31.y - vec34.y) * 0.05D;
/*     */       
/* 263 */       Vec3 vec3 = getDeltaMovement();
/* 264 */       double d18 = vec3.horizontalDistance();
/* 265 */       if (d18 > 0.0D) {
/* 266 */         setDeltaMovement(vec3.multiply((d18 + d17) / d18, 1.0D, (d18 + d17) / d18));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 272 */       setPos(this.minecart.getX(), vec34.y, this.minecart.getZ());
/*     */     } 
/*     */     
/* 275 */     int i = Mth.floor(this.minecart.getX());
/* 276 */     int j = Mth.floor(this.minecart.getZ());
/* 277 */     if (i != blockPos.getX() || j != blockPos.getZ()) {
/* 278 */       Vec3 vec3 = getDeltaMovement();
/* 279 */       double d = vec3.horizontalDistance();
/* 280 */       setDeltaMovement(d * (i - blockPos
/* 281 */           .getX()), vec3.y, d * (j - blockPos
/*     */           
/* 283 */           .getZ()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 288 */     if (bool) {
/* 289 */       Vec3 vec3 = getDeltaMovement();
/* 290 */       double d = vec3.horizontalDistance();
/* 291 */       if (d > 0.01D) {
/* 292 */         double d17 = 0.06D;
/* 293 */         setDeltaMovement(vec3.add(vec3.x / d * 0.06D, 0.0D, vec3.z / d * 0.06D));
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 300 */         Vec3 vec35 = getDeltaMovement();
/* 301 */         double d17 = vec35.x;
/* 302 */         double d18 = vec35.z;
/* 303 */         if (railShape == RailShape.EAST_WEST) {
/* 304 */           if (this.minecart.isRedstoneConductor(blockPos.west())) {
/* 305 */             d17 = 0.02D;
/* 306 */           } else if (this.minecart.isRedstoneConductor(blockPos.east())) {
/* 307 */             d17 = -0.02D;
/*     */           } 
/* 309 */         } else if (railShape == RailShape.NORTH_SOUTH) {
/* 310 */           if (this.minecart.isRedstoneConductor(blockPos.north())) {
/* 311 */             d18 = 0.02D;
/* 312 */           } else if (this.minecart.isRedstoneConductor(blockPos.south())) {
/* 313 */             d18 = -0.02D;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/* 318 */         setDeltaMovement(d17, vec35.y, d18);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3 getPosOffs(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 324 */     int i = Mth.floor(paramDouble1);
/* 325 */     int j = Mth.floor(paramDouble2);
/* 326 */     int k = Mth.floor(paramDouble3);
/* 327 */     if (level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
/* 328 */       j--;
/*     */     }
/*     */     
/* 331 */     BlockState blockState = level().getBlockState(new BlockPos(i, j, k));
/* 332 */     if (BaseRailBlock.isRail(blockState)) {
/* 333 */       RailShape railShape = (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty());
/* 334 */       paramDouble2 = j;
/* 335 */       if (railShape.isSlope()) {
/* 336 */         paramDouble2 = (j + 1);
/*     */       }
/*     */       
/* 339 */       Pair<Vec3i, Vec3i> pair = AbstractMinecart.exits(railShape);
/* 340 */       Vec3i vec3i1 = (Vec3i)pair.getFirst();
/* 341 */       Vec3i vec3i2 = (Vec3i)pair.getSecond();
/*     */       
/* 343 */       double d1 = (vec3i2.getX() - vec3i1.getX());
/* 344 */       double d2 = (vec3i2.getZ() - vec3i1.getZ());
/* 345 */       double d3 = Math.sqrt(d1 * d1 + d2 * d2);
/* 346 */       d1 /= d3;
/* 347 */       d2 /= d3;
/*     */       
/* 349 */       paramDouble1 += d1 * paramDouble4;
/* 350 */       paramDouble3 += d2 * paramDouble4;
/*     */       
/* 352 */       if (vec3i1.getY() != 0 && Mth.floor(paramDouble1) - i == vec3i1.getX() && Mth.floor(paramDouble3) - k == vec3i1.getZ()) {
/* 353 */         paramDouble2 += vec3i1.getY();
/* 354 */       } else if (vec3i2.getY() != 0 && Mth.floor(paramDouble1) - i == vec3i2.getX() && Mth.floor(paramDouble3) - k == vec3i2.getZ()) {
/* 355 */         paramDouble2 += vec3i2.getY();
/*     */       } 
/*     */       
/* 358 */       return getPos(paramDouble1, paramDouble2, paramDouble3);
/*     */     } 
/* 360 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3 getPos(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 364 */     int i = Mth.floor(paramDouble1);
/* 365 */     int j = Mth.floor(paramDouble2);
/* 366 */     int k = Mth.floor(paramDouble3);
/* 367 */     if (level().getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS)) {
/* 368 */       j--;
/*     */     }
/*     */     
/* 371 */     BlockState blockState = level().getBlockState(new BlockPos(i, j, k));
/* 372 */     if (BaseRailBlock.isRail(blockState)) {
/* 373 */       double d10; RailShape railShape = (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty());
/*     */       
/* 375 */       Pair<Vec3i, Vec3i> pair = AbstractMinecart.exits(railShape);
/* 376 */       Vec3i vec3i1 = (Vec3i)pair.getFirst();
/* 377 */       Vec3i vec3i2 = (Vec3i)pair.getSecond();
/*     */       
/* 379 */       double d1 = i + 0.5D + vec3i1.getX() * 0.5D;
/* 380 */       double d2 = j + 0.0625D + vec3i1.getY() * 0.5D;
/* 381 */       double d3 = k + 0.5D + vec3i1.getZ() * 0.5D;
/* 382 */       double d4 = i + 0.5D + vec3i2.getX() * 0.5D;
/* 383 */       double d5 = j + 0.0625D + vec3i2.getY() * 0.5D;
/* 384 */       double d6 = k + 0.5D + vec3i2.getZ() * 0.5D;
/*     */       
/* 386 */       double d7 = d4 - d1;
/* 387 */       double d8 = (d5 - d2) * 2.0D;
/* 388 */       double d9 = d6 - d3;
/*     */ 
/*     */       
/* 391 */       if (d7 == 0.0D) {
/* 392 */         d10 = paramDouble3 - k;
/* 393 */       } else if (d9 == 0.0D) {
/* 394 */         d10 = paramDouble1 - i;
/*     */       } else {
/* 396 */         double d11 = paramDouble1 - d1;
/* 397 */         double d12 = paramDouble3 - d3;
/*     */         
/* 399 */         d10 = (d11 * d7 + d12 * d9) * 2.0D;
/*     */       } 
/*     */       
/* 402 */       paramDouble1 = d1 + d7 * d10;
/* 403 */       paramDouble2 = d2 + d8 * d10;
/* 404 */       paramDouble3 = d3 + d9 * d10;
/* 405 */       if (d8 < 0.0D) {
/* 406 */         paramDouble2++;
/* 407 */       } else if (d8 > 0.0D) {
/* 408 */         paramDouble2 += 0.5D;
/*     */       } 
/* 410 */       return new Vec3(paramDouble1, paramDouble2, paramDouble3);
/*     */     } 
/* 412 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public double stepAlongTrack(BlockPos paramBlockPos, RailShape paramRailShape, double paramDouble) {
/* 417 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean pushAndPickupEntities() {
/* 422 */     AABB aABB = this.minecart.getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D);
/* 423 */     if (this.minecart.isRideable() && getDeltaMovement().horizontalDistanceSqr() >= 0.01D) {
/* 424 */       List list = level().getEntities((Entity)this.minecart, aABB, EntitySelector.pushableBy((Entity)this.minecart));
/* 425 */       if (!list.isEmpty()) {
/* 426 */         for (Entity entity : list) {
/* 427 */           if (entity instanceof net.minecraft.world.entity.player.Player || entity instanceof net.minecraft.world.entity.animal.golem.IronGolem || entity instanceof AbstractMinecart || this.minecart.isVehicle() || entity.isPassenger()) {
/* 428 */             entity.push((Entity)this.minecart); continue;
/*     */           } 
/* 430 */           entity.startRiding((Entity)this.minecart);
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 435 */       for (Entity entity : level().getEntities((Entity)this.minecart, aABB)) {
/* 436 */         if (!this.minecart.hasPassenger(entity) && entity.isPushable() && entity instanceof AbstractMinecart) {
/* 437 */           entity.push((Entity)this.minecart);
/*     */         }
/*     */       } 
/*     */     } 
/* 441 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getMotionDirection() {
/* 446 */     return this.minecart.isFlipped() ? this.minecart.getDirection().getOpposite().getClockWise() : this.minecart.getDirection().getClockWise();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getKnownMovement(Vec3 paramVec3) {
/* 453 */     if (Double.isNaN(paramVec3.x) || Double.isNaN(paramVec3.y) || Double.isNaN(paramVec3.z)) {
/* 454 */       return Vec3.ZERO;
/*     */     }
/* 456 */     return new Vec3(
/* 457 */         Mth.clamp(paramVec3.x, -0.4D, 0.4D), paramVec3.y, 
/*     */         
/* 459 */         Mth.clamp(paramVec3.z, -0.4D, 0.4D));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaxSpeed(ServerLevel paramServerLevel) {
/* 465 */     return this.minecart.isInWater() ? 0.2D : 0.4D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getSlowdownFactor() {
/* 470 */     return this.minecart.isVehicle() ? 0.997D : 0.96D;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\OldMinecartBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */