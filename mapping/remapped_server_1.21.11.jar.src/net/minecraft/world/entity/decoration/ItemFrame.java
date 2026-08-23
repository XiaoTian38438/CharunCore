/*     */ package net.minecraft.world.entity.decoration;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ItemFrame extends HangingEntity {
/*  46 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.ITEM_STACK);
/*  47 */   private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int NUM_ROTATIONS = 8;
/*     */   
/*     */   private static final float DEPTH = 0.0625F;
/*     */   private static final float WIDTH = 0.75F;
/*     */   private static final float HEIGHT = 0.75F;
/*     */   private static final byte DEFAULT_ROTATION = 0;
/*     */   private static final float DEFAULT_DROP_CHANCE = 1.0F;
/*     */   private static final boolean DEFAULT_INVISIBLE = false;
/*     */   private static final boolean DEFAULT_FIXED = false;
/*  58 */   private float dropChance = 1.0F;
/*     */   private boolean fixed = false;
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> paramEntityType, Level paramLevel) {
/*  62 */     super((EntityType)paramEntityType, paramLevel);
/*  63 */     setInvisible(false);
/*     */   }
/*     */   
/*     */   public ItemFrame(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  67 */     this(EntityType.ITEM_FRAME, paramLevel, paramBlockPos, paramDirection);
/*     */   }
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> paramEntityType, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/*  71 */     super((EntityType)paramEntityType, paramLevel, paramBlockPos);
/*  72 */     setDirection(paramDirection);
/*  73 */     setInvisible(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  78 */     super.defineSynchedData(paramBuilder);
/*  79 */     paramBuilder.define(DATA_ITEM, ItemStack.EMPTY);
/*  80 */     paramBuilder.define(DATA_ROTATION, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDirection(Direction paramDirection) {
/*  85 */     Objects.requireNonNull(paramDirection);
/*     */     
/*  87 */     setDirectionRaw(paramDirection);
/*  88 */     if (paramDirection.getAxis().isHorizontal()) {
/*  89 */       setXRot(0.0F);
/*  90 */       setYRot((paramDirection.get2DDataValue() * 90));
/*     */     } else {
/*  92 */       setXRot((-90 * paramDirection.getAxisDirection().getStep()));
/*  93 */       setYRot(0.0F);
/*     */     } 
/*  95 */     this.xRotO = getXRot();
/*  96 */     this.yRotO = getYRot();
/*     */     
/*  98 */     recalculateBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void recalculateBoundingBox() {
/* 103 */     super.recalculateBoundingBox();
/* 104 */     syncPacketPositionCodec(getX(), getY(), getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB calculateBoundingBox(BlockPos paramBlockPos, Direction paramDirection) {
/* 109 */     return createBoundingBox(paramBlockPos, paramDirection, hasFramedMap());
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB getPopBox() {
/* 114 */     return createBoundingBox(this.pos, getDirection(), false);
/*     */   }
/*     */   
/*     */   private AABB createBoundingBox(BlockPos paramBlockPos, Direction paramDirection, boolean paramBoolean) {
/* 118 */     float f1 = 0.46875F;
/* 119 */     Vec3 vec3 = Vec3.atCenterOf((Vec3i)paramBlockPos).relative(paramDirection, -0.46875D);
/*     */     
/* 121 */     float f2 = paramBoolean ? 1.0F : 0.75F;
/* 122 */     float f3 = paramBoolean ? 1.0F : 0.75F;
/* 123 */     Direction.Axis axis = paramDirection.getAxis();
/* 124 */     double d1 = (axis == Direction.Axis.X) ? 0.0625D : f2;
/* 125 */     double d2 = (axis == Direction.Axis.Y) ? 0.0625D : f3;
/* 126 */     double d3 = (axis == Direction.Axis.Z) ? 0.0625D : f2;
/* 127 */     return AABB.ofSize(vec3, d1, d2, d3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean survives() {
/* 132 */     if (this.fixed) {
/* 133 */       return true;
/*     */     }
/*     */     
/* 136 */     if (hasLevelCollision(getPopBox())) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     BlockState blockState = level().getBlockState(this.pos.relative(getDirection().getOpposite()));
/* 141 */     if (!blockState.isSolid() && (!getDirection().getAxis().isHorizontal() || !DiodeBlock.isDiode(blockState))) {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     return canCoexist(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType paramMoverType, Vec3 paramVec3) {
/* 150 */     if (!this.fixed) {
/* 151 */       super.move(paramMoverType, paramVec3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 157 */     if (!this.fixed) {
/* 158 */       super.push(paramDouble1, paramDouble2, paramDouble3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel paramServerLevel) {
/* 164 */     removeFramedMap(getItem());
/* 165 */     super.kill(paramServerLevel);
/*     */   }
/*     */   
/*     */   private boolean shouldDamageDropItem(DamageSource paramDamageSource) {
/* 169 */     return (!paramDamageSource.is(DamageTypeTags.IS_EXPLOSION) && !getItem().isEmpty());
/*     */   }
/*     */   
/*     */   private static boolean canHurtWhenFixed(DamageSource paramDamageSource) {
/* 173 */     return (paramDamageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || paramDamageSource.isCreativePlayer());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource paramDamageSource) {
/* 178 */     if (this.fixed && !canHurtWhenFixed(paramDamageSource)) {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     return !isInvulnerableToBase(paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 187 */     if (this.fixed) {
/* 188 */       return (canHurtWhenFixed(paramDamageSource) && super.hurtServer(paramServerLevel, paramDamageSource, paramFloat));
/*     */     }
/*     */     
/* 191 */     if (isInvulnerableToBase(paramDamageSource)) {
/* 192 */       return false;
/*     */     }
/*     */     
/* 195 */     if (shouldDamageDropItem(paramDamageSource)) {
/* 196 */       dropItem(paramServerLevel, paramDamageSource.getEntity(), false);
/* 197 */       gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramDamageSource.getEntity());
/* 198 */       playSound(getRemoveItemSound(), 1.0F, 1.0F);
/* 199 */       return true;
/*     */     } 
/* 201 */     return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */   
/*     */   public SoundEvent getRemoveItemSound() {
/* 205 */     return SoundEvents.ITEM_FRAME_REMOVE_ITEM;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 210 */     double d = 16.0D;
/* 211 */     d *= 64.0D * getViewScale();
/* 212 */     return (paramDouble < d * d);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(ServerLevel paramServerLevel, Entity paramEntity) {
/* 217 */     playSound(getBreakSound(), 1.0F, 1.0F);
/* 218 */     dropItem(paramServerLevel, paramEntity, true);
/* 219 */     gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramEntity);
/*     */   }
/*     */   
/*     */   public SoundEvent getBreakSound() {
/* 223 */     return SoundEvents.ITEM_FRAME_BREAK;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playPlacementSound() {
/* 228 */     playSound(getPlaceSound(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public SoundEvent getPlaceSound() {
/* 232 */     return SoundEvents.ITEM_FRAME_PLACE;
/*     */   }
/*     */   
/*     */   private void dropItem(ServerLevel paramServerLevel, Entity paramEntity, boolean paramBoolean) {
/* 236 */     if (this.fixed) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     ItemStack itemStack = getItem();
/* 241 */     setItem(ItemStack.EMPTY);
/*     */     
/* 243 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 244 */       if (paramEntity == null) {
/* 245 */         removeFramedMap(itemStack);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 250 */     if (paramEntity instanceof Player) { Player player = (Player)paramEntity;
/* 251 */       if (player.hasInfiniteMaterials()) {
/* 252 */         removeFramedMap(itemStack);
/*     */         
/*     */         return;
/*     */       }  }
/*     */     
/* 257 */     if (paramBoolean) {
/* 258 */       spawnAtLocation(paramServerLevel, getFrameItemStack());
/*     */     }
/* 260 */     if (!itemStack.isEmpty()) {
/* 261 */       itemStack = itemStack.copy();
/* 262 */       removeFramedMap(itemStack);
/* 263 */       if (this.random.nextFloat() < this.dropChance) {
/* 264 */         spawnAtLocation(paramServerLevel, itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFramedMap(ItemStack paramItemStack) {
/* 270 */     MapId mapId = getFramedMapId(paramItemStack);
/* 271 */     if (mapId != null) {
/* 272 */       MapItemSavedData mapItemSavedData = MapItem.getSavedData(mapId, level());
/* 273 */       if (mapItemSavedData != null) {
/* 274 */         mapItemSavedData.removedFromFrame(this.pos, getId());
/*     */       }
/*     */     } 
/* 277 */     paramItemStack.setEntityRepresentation(null);
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 281 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   public MapId getFramedMapId(ItemStack paramItemStack) {
/* 285 */     return (MapId)paramItemStack.get(DataComponents.MAP_ID);
/*     */   }
/*     */   
/*     */   public boolean hasFramedMap() {
/* 289 */     return getItem().has(DataComponents.MAP_ID);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack paramItemStack) {
/* 293 */     setItem(paramItemStack, true);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack paramItemStack, boolean paramBoolean) {
/* 297 */     if (!paramItemStack.isEmpty()) {
/* 298 */       paramItemStack = paramItemStack.copyWithCount(1);
/*     */     }
/*     */     
/* 301 */     onItemChanged(paramItemStack);
/* 302 */     getEntityData().set(DATA_ITEM, paramItemStack);
/* 303 */     if (!paramItemStack.isEmpty()) {
/* 304 */       playSound(getAddItemSound(), 1.0F, 1.0F);
/*     */     }
/*     */     
/* 307 */     if (paramBoolean && this.pos != null) {
/* 308 */       level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
/*     */     }
/*     */   }
/*     */   
/*     */   public SoundEvent getAddItemSound() {
/* 313 */     return SoundEvents.ITEM_FRAME_ADD_ITEM;
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/* 318 */     if (paramInt == 0) {
/* 319 */       return SlotAccess.of(this::getItem, this::setItem);
/*     */     }
/* 321 */     return super.getSlot(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 326 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/* 327 */     if (paramEntityDataAccessor.equals(DATA_ITEM)) {
/* 328 */       onItemChanged(getItem());
/*     */     }
/*     */   }
/*     */   
/*     */   private void onItemChanged(ItemStack paramItemStack) {
/* 333 */     if (!paramItemStack.isEmpty() && paramItemStack.getFrame() != this) {
/* 334 */       paramItemStack.setEntityRepresentation(this);
/*     */     }
/* 336 */     recalculateBoundingBox();
/*     */   }
/*     */   
/*     */   public int getRotation() {
/* 340 */     return ((Integer)getEntityData().get(DATA_ROTATION)).intValue();
/*     */   }
/*     */   
/*     */   public void setRotation(int paramInt) {
/* 344 */     setRotation(paramInt, true);
/*     */   }
/*     */   
/*     */   private void setRotation(int paramInt, boolean paramBoolean) {
/* 348 */     getEntityData().set(DATA_ROTATION, Integer.valueOf(paramInt % 8));
/*     */     
/* 350 */     if (paramBoolean && this.pos != null) {
/* 351 */       level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 357 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 359 */     ItemStack itemStack = getItem();
/* 360 */     if (!itemStack.isEmpty()) {
/* 361 */       paramValueOutput.store("Item", ItemStack.CODEC, itemStack);
/*     */     }
/*     */     
/* 364 */     paramValueOutput.putByte("ItemRotation", (byte)getRotation());
/* 365 */     paramValueOutput.putFloat("ItemDropChance", this.dropChance);
/* 366 */     paramValueOutput.store("Facing", Direction.LEGACY_ID_CODEC, getDirection());
/* 367 */     paramValueOutput.putBoolean("Invisible", isInvisible());
/* 368 */     paramValueOutput.putBoolean("Fixed", this.fixed);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 373 */     super.readAdditionalSaveData(paramValueInput);
/* 374 */     ItemStack itemStack1 = paramValueInput.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*     */ 
/*     */     
/* 377 */     ItemStack itemStack2 = getItem();
/* 378 */     if (!itemStack2.isEmpty() && 
/* 379 */       !ItemStack.matches(itemStack1, itemStack2)) {
/* 380 */       removeFramedMap(itemStack2);
/*     */     }
/*     */ 
/*     */     
/* 384 */     setItem(itemStack1, false);
/*     */     
/* 386 */     setRotation(paramValueInput.getByteOr("ItemRotation", (byte)0), false);
/* 387 */     this.dropChance = paramValueInput.getFloatOr("ItemDropChance", 1.0F);
/* 388 */     setDirection(paramValueInput.read("Facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN));
/* 389 */     setInvisible(paramValueInput.getBooleanOr("Invisible", false));
/* 390 */     this.fixed = paramValueInput.getBooleanOr("Fixed", false);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 395 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 396 */     boolean bool1 = !getItem().isEmpty() ? true : false;
/* 397 */     boolean bool2 = !itemStack.isEmpty() ? true : false;
/*     */ 
/*     */     
/* 400 */     if (this.fixed) {
/* 401 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 404 */     if (paramPlayer.level().isClientSide()) {
/* 405 */       return (bool1 || bool2) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 408 */     if (!bool1) {
/* 409 */       if (bool2 && !isRemoved()) {
/* 410 */         MapItemSavedData mapItemSavedData = MapItem.getSavedData(itemStack, level());
/* 411 */         if (mapItemSavedData != null && mapItemSavedData.isTrackedCountOverLimit(256)) {
/* 412 */           return (InteractionResult)InteractionResult.FAIL;
/*     */         }
/* 414 */         setItem(itemStack);
/* 415 */         gameEvent((Holder)GameEvent.BLOCK_CHANGE, (Entity)paramPlayer);
/* 416 */         itemStack.consume(1, (LivingEntity)paramPlayer);
/* 417 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/* 419 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/*     */     
/* 422 */     playSound(getRotateItemSound(), 1.0F, 1.0F);
/* 423 */     setRotation(getRotation() + 1);
/* 424 */     gameEvent((Holder)GameEvent.BLOCK_CHANGE, (Entity)paramPlayer);
/* 425 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getRotateItemSound() {
/* 430 */     return SoundEvents.ITEM_FRAME_ROTATE_ITEM;
/*     */   }
/*     */   
/*     */   public int getAnalogOutput() {
/* 434 */     if (getItem().isEmpty()) {
/* 435 */       return 0;
/*     */     }
/*     */     
/* 438 */     return getRotation() % 8 + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 443 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, getDirection().get3DDataValue(), getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket paramClientboundAddEntityPacket) {
/* 448 */     super.recreateFromPacket(paramClientboundAddEntityPacket);
/* 449 */     setDirection(Direction.from3DDataValue(paramClientboundAddEntityPacket.getData()));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 454 */     ItemStack itemStack = getItem();
/* 455 */     if (itemStack.isEmpty()) {
/* 456 */       return getFrameItemStack();
/*     */     }
/* 458 */     return itemStack.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getFrameItemStack() {
/* 463 */     return new ItemStack((ItemLike)Items.ITEM_FRAME);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVisualRotationYInDegrees() {
/* 468 */     Direction direction = getDirection();
/* 469 */     byte b = direction.getAxis().isVertical() ? (90 * direction.getAxisDirection().getStep()) : 0;
/* 470 */     return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + getRotation() * 45 + b);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\ItemFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */