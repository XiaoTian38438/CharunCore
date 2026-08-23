/*     */ package net.minecraft.world.entity.item;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.DirectionalPlaceContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Fallable;
/*     */ import net.minecraft.world.level.block.FallingBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FallingBlockEntity extends Entity {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  61 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.SAND.defaultBlockState();
/*     */   
/*     */   private static final int DEFAULT_TIME = 0;
/*     */   private static final float DEFAULT_FALL_DAMAGE_PER_DISTANCE = 0.0F;
/*     */   private static final int DEFAULT_MAX_FALL_DAMAGE = 40;
/*     */   private static final boolean DEFAULT_DROP_ITEM = true;
/*     */   private static final boolean DEFAULT_CANCEL_DROP = false;
/*  68 */   private BlockState blockState = DEFAULT_BLOCK_STATE;
/*  69 */   public int time = 0;
/*     */   public boolean dropItem = true;
/*     */   private boolean cancelDrop = false;
/*     */   private boolean hurtEntities;
/*  73 */   private int fallDamageMax = 40;
/*  74 */   private float fallDamagePerDistance = 0.0F;
/*     */   
/*     */   public CompoundTag blockData;
/*     */   public boolean forceTickAfterTeleportToDuplicate;
/*  78 */   protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
/*     */   
/*     */   public FallingBlockEntity(EntityType<? extends FallingBlockEntity> paramEntityType, Level paramLevel) {
/*  81 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   private FallingBlockEntity(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, BlockState paramBlockState) {
/*  85 */     this(EntityType.FALLING_BLOCK, paramLevel);
/*  86 */     this.blockState = paramBlockState;
/*  87 */     this.blocksBuilding = true;
/*     */     
/*  89 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */     
/*  91 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/*  93 */     this.xo = paramDouble1;
/*  94 */     this.yo = paramDouble2;
/*  95 */     this.zo = paramDouble3;
/*     */     
/*  97 */     setStartPos(blockPosition());
/*     */   }
/*     */   
/*     */   public static FallingBlockEntity fall(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 101 */     FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(paramLevel, paramBlockPos.getX() + 0.5D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.5D, paramBlockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) ? (BlockState)paramBlockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : paramBlockState);
/*     */     
/* 103 */     paramLevel.setBlock(paramBlockPos, paramBlockState.getFluidState().createLegacyBlock(), 3);
/* 104 */     paramLevel.addFreshEntity(fallingBlockEntity);
/* 105 */     return fallingBlockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 115 */     if (!isInvulnerableToBase(paramDamageSource)) {
/* 116 */       markHurt();
/*     */     }
/*     */     
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   public void setStartPos(BlockPos paramBlockPos) {
/* 123 */     this.entityData.set(DATA_START_POS, paramBlockPos);
/*     */   }
/*     */   
/*     */   public BlockPos getStartPos() {
/* 127 */     return (BlockPos)this.entityData.get(DATA_START_POS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 132 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 137 */     paramBuilder.define(DATA_START_POS, BlockPos.ZERO);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 142 */     return !isRemoved();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 147 */     return 0.04D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 152 */     if (this.blockState.isAir()) {
/* 153 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/* 157 */     Block block = this.blockState.getBlock();
/* 158 */     this.time++;
/*     */     
/* 160 */     applyGravity();
/* 161 */     move(MoverType.SELF, getDeltaMovement());
/* 162 */     applyEffectsFromBlocks();
/* 163 */     handlePortal();
/*     */     
/* 165 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (isAlive() || this.forceTickAfterTeleportToDuplicate) {
/* 166 */         BlockPos blockPos = blockPosition();
/*     */         
/* 168 */         boolean bool = this.blockState.getBlock() instanceof net.minecraft.world.level.block.ConcretePowderBlock;
/* 169 */         boolean bool1 = (bool && level().getFluidState(blockPos).is(FluidTags.WATER)) ? true : false;
/* 170 */         double d = getDeltaMovement().lengthSqr();
/*     */         
/* 172 */         if (bool && d > 1.0D) {
/*     */ 
/*     */           
/* 175 */           BlockHitResult blockHitResult = level().clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
/* 176 */           if (blockHitResult.getType() != HitResult.Type.MISS && level().getFluidState(blockHitResult.getBlockPos()).is(FluidTags.WATER)) {
/*     */             
/* 178 */             blockPos = blockHitResult.getBlockPos();
/* 179 */             bool1 = true;
/*     */           } 
/*     */         } 
/*     */         
/* 183 */         if (onGround() || bool1) {
/* 184 */           BlockState blockState = level().getBlockState(blockPos);
/*     */ 
/*     */           
/* 187 */           setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/*     */           
/* 189 */           if (!blockState.is(Blocks.MOVING_PISTON)) {
/* 190 */             if (!this.cancelDrop) {
/* 191 */               boolean bool2 = blockState.canBeReplaced((BlockPlaceContext)new DirectionalPlaceContext(level(), blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
/*     */               
/* 193 */               boolean bool3 = (FallingBlock.isFree(level().getBlockState(blockPos.below())) && (!bool || !bool1)) ? true : false;
/* 194 */               boolean bool4 = (this.blockState.canSurvive((LevelReader)level(), blockPos) && !bool3) ? true : false;
/* 195 */               if (bool2 && bool4) {
/* 196 */                 if (this.blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && level().getFluidState(blockPos).getType() == Fluids.WATER) {
/* 197 */                   this.blockState = (BlockState)this.blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */                 }
/* 199 */                 if (level().setBlock(blockPos, this.blockState, 3)) {
/*     */                   
/* 201 */                   (serverLevel.getChunkSource()).chunkMap.sendToTrackingPlayers(this, (Packet)new ClientboundBlockUpdatePacket(blockPos, level().getBlockState(blockPos)));
/* 202 */                   discard();
/* 203 */                   if (block instanceof Fallable) { Fallable fallable = (Fallable)block;
/* 204 */                     fallable.onLand(level(), blockPos, this.blockState, blockState, this); }
/*     */                   
/* 206 */                   if (this.blockData != null && this.blockState.hasBlockEntity()) {
/* 207 */                     BlockEntity blockEntity = level().getBlockEntity(blockPos);
/*     */                     
/* 209 */                     if (blockEntity != null) {
/*     */                       try {
/* 211 */                         ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER); 
/* 212 */                         try { RegistryAccess registryAccess = level().registryAccess();
/*     */                           
/* 214 */                           TagValueOutput tagValueOutput = TagValueOutput.createWithContext((ProblemReporter)scopedCollector, (HolderLookup.Provider)registryAccess);
/* 215 */                           blockEntity.saveWithoutMetadata((ValueOutput)tagValueOutput);
/* 216 */                           CompoundTag compoundTag = tagValueOutput.buildResult();
/*     */                           
/* 218 */                           this.blockData.forEach((paramString, paramTag) -> paramCompoundTag.put(paramString, paramTag.copy()));
/*     */                           
/* 220 */                           blockEntity.loadWithComponents(TagValueInput.create((ProblemReporter)scopedCollector, (HolderLookup.Provider)registryAccess, compoundTag));
/* 221 */                           scopedCollector.close(); } catch (Throwable throwable) { try { scopedCollector.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/* 222 */                       } catch (Exception exception) {
/* 223 */                         LOGGER.error("Failed to load block entity from falling block", exception);
/*     */                       } 
/* 225 */                       blockEntity.setChanged();
/*     */                     } 
/*     */                   } 
/* 228 */                 } else if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 229 */                   discard();
/* 230 */                   callOnBrokenAfterFall(block, blockPos);
/* 231 */                   spawnAtLocation(serverLevel, (ItemLike)block);
/*     */                 } 
/*     */               } else {
/* 234 */                 discard();
/* 235 */                 if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 236 */                   callOnBrokenAfterFall(block, blockPos);
/* 237 */                   spawnAtLocation(serverLevel, (ItemLike)block);
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 241 */               discard();
/* 242 */               callOnBrokenAfterFall(block, blockPos);
/*     */             } 
/*     */           }
/* 245 */         } else if ((this.time > 100 && (blockPos.getY() <= level().getMinY() || blockPos.getY() > level().getMaxY())) || this.time > 600) {
/* 246 */           if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 247 */             spawnAtLocation(serverLevel, (ItemLike)block);
/*     */           }
/* 249 */           discard();
/*     */         } 
/*     */       }  }
/* 252 */      setDeltaMovement(getDeltaMovement().scale(0.98D));
/*     */   }
/*     */   
/*     */   public void callOnBrokenAfterFall(Block paramBlock, BlockPos paramBlockPos) {
/* 256 */     if (paramBlock instanceof Fallable) {
/* 257 */       ((Fallable)paramBlock).onBrokenAfterFall(level(), paramBlockPos, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double paramDouble, float paramFloat, DamageSource paramDamageSource) {
/* 263 */     if (!this.hurtEntities) {
/* 264 */       return false;
/*     */     }
/*     */     
/* 267 */     int i = Mth.ceil(paramDouble - 1.0D);
/* 268 */     if (i < 0) {
/* 269 */       return false;
/*     */     }
/*     */     
/* 272 */     Predicate predicate = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
/* 273 */     Block block = this.blockState.getBlock(); Fallable fallable = (Fallable)block;
/*     */     
/* 275 */     DamageSource damageSource = (block instanceof Fallable) ? fallable.getFallDamageSource(this) : damageSources().fallingBlock(this);
/*     */     
/* 277 */     float f = Math.min(Mth.floor(i * this.fallDamagePerDistance), this.fallDamageMax);
/* 278 */     level().getEntities(this, getBoundingBox(), predicate).forEach(paramEntity -> paramEntity.hurt(paramDamageSource, paramFloat));
/*     */ 
/*     */ 
/*     */     
/* 282 */     boolean bool = this.blockState.is(BlockTags.ANVIL);
/* 283 */     if (bool && f > 0.0F && this.random.nextFloat() < 0.05F + i * 0.05F) {
/* 284 */       BlockState blockState = AnvilBlock.damage(this.blockState);
/* 285 */       if (blockState == null) {
/* 286 */         this.cancelDrop = true;
/*     */       } else {
/* 288 */         this.blockState = blockState;
/*     */       } 
/*     */     } 
/* 291 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 296 */     paramValueOutput.store("BlockState", BlockState.CODEC, this.blockState);
/* 297 */     paramValueOutput.putInt("Time", this.time);
/* 298 */     paramValueOutput.putBoolean("DropItem", this.dropItem);
/* 299 */     paramValueOutput.putBoolean("HurtEntities", this.hurtEntities);
/* 300 */     paramValueOutput.putFloat("FallHurtAmount", this.fallDamagePerDistance);
/* 301 */     paramValueOutput.putInt("FallHurtMax", this.fallDamageMax);
/* 302 */     if (this.blockData != null) {
/* 303 */       paramValueOutput.store("TileEntityData", CompoundTag.CODEC, this.blockData);
/*     */     }
/* 305 */     paramValueOutput.putBoolean("CancelDrop", this.cancelDrop);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 310 */     this.blockState = paramValueInput.read("BlockState", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE);
/*     */     
/* 312 */     this.time = paramValueInput.getIntOr("Time", 0);
/*     */     
/* 314 */     boolean bool = this.blockState.is(BlockTags.ANVIL);
/* 315 */     this.hurtEntities = paramValueInput.getBooleanOr("HurtEntities", bool);
/* 316 */     this.fallDamagePerDistance = paramValueInput.getFloatOr("FallHurtAmount", 0.0F);
/* 317 */     this.fallDamageMax = paramValueInput.getIntOr("FallHurtMax", 40);
/*     */     
/* 319 */     this.dropItem = paramValueInput.getBooleanOr("DropItem", true);
/*     */     
/* 321 */     this.blockData = paramValueInput.read("TileEntityData", CompoundTag.CODEC).orElse(null);
/*     */     
/* 323 */     this.cancelDrop = paramValueInput.getBooleanOr("CancelDrop", false);
/*     */   }
/*     */   
/*     */   public void setHurtsEntities(float paramFloat, int paramInt) {
/* 327 */     this.hurtEntities = true;
/* 328 */     this.fallDamagePerDistance = paramFloat;
/* 329 */     this.fallDamageMax = paramInt;
/*     */   }
/*     */   
/*     */   public void disableDrop() {
/* 333 */     this.cancelDrop = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean displayFireAnimation() {
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory paramCrashReportCategory) {
/* 343 */     super.fillCrashReportCategory(paramCrashReportCategory);
/* 344 */     paramCrashReportCategory.setDetail("Immitating BlockState", this.blockState.toString());
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 348 */     return this.blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getTypeName() {
/* 353 */     return (Component)Component.translatable("entity.minecraft.falling_block_type", new Object[] { this.blockState.getBlock().getName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 358 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, paramServerEntity, Block.getId(getBlockState()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 363 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 364 */     this.blockState = Block.stateById(paramClientboundAddEntityPacket.getData());
/* 365 */     this.blocksBuilding = true;
/*     */     
/* 367 */     double d1 = paramClientboundAddEntityPacket.getX();
/* 368 */     double d2 = paramClientboundAddEntityPacket.getY();
/* 369 */     double d3 = paramClientboundAddEntityPacket.getZ();
/*     */     
/* 371 */     setPos(d1, d2, d3);
/* 372 */     setStartPos(blockPosition());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition paramTeleportTransition) {
/* 381 */     ResourceKey resourceKey1 = paramTeleportTransition.newLevel().dimension();
/* 382 */     ResourceKey resourceKey2 = level().dimension();
/* 383 */     boolean bool = ((resourceKey2 == Level.END || resourceKey1 == Level.END) && resourceKey2 != resourceKey1) ? true : false;
/*     */     
/* 385 */     Entity entity = super.teleport(paramTeleportTransition);
/* 386 */     this.forceTickAfterTeleportToDuplicate = (entity != null && bool);
/* 387 */     return entity;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\item\FallingBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */