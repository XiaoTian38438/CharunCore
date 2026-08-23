/*     */ package net.minecraft.world.level.block.entity;
/*     */ import java.util.List;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.WorldlyContainerHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HopperBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class HopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {
/*     */   public static final int MOVE_ITEM_SPEED = 8;
/*     */   public static final int HOPPER_CONTAINER_SIZE = 5;
/*  35 */   private static final int[][] CACHED_SLOTS = new int[54][];
/*     */   private static final int NO_COOLDOWN_TIME = -1;
/*  37 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.hopper");
/*     */   
/*  39 */   private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
/*  40 */   private int cooldownTime = -1;
/*     */   private long tickedGameTime;
/*     */   private Direction facing;
/*     */   
/*     */   public HopperBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  45 */     super(BlockEntityType.HOPPER, paramBlockPos, paramBlockState);
/*  46 */     this.facing = (Direction)paramBlockState.getValue((Property)HopperBlock.FACING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  51 */     super.loadAdditional(paramValueInput);
/*     */     
/*  53 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  54 */     if (!tryLoadLootTable(paramValueInput)) {
/*  55 */       ContainerHelper.loadAllItems(paramValueInput, this.items);
/*     */     }
/*  57 */     this.cooldownTime = paramValueInput.getIntOr("TransferCooldown", -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  62 */     super.saveAdditional(paramValueOutput);
/*     */     
/*  64 */     if (!trySaveLootTable(paramValueOutput)) {
/*  65 */       ContainerHelper.saveAllItems(paramValueOutput, this.items);
/*     */     }
/*     */     
/*  68 */     paramValueOutput.putInt("TransferCooldown", this.cooldownTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  73 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  78 */     unpackLootTable(null);
/*     */ 
/*     */     
/*  81 */     return ContainerHelper.removeItem((List)getItems(), paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/*  86 */     unpackLootTable(null);
/*  87 */     getItems().set(paramInt, paramItemStack);
/*  88 */     paramItemStack.limitSize(getMaxStackSize(paramItemStack));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockState(BlockState paramBlockState) {
/*  94 */     super.setBlockState(paramBlockState);
/*  95 */     this.facing = (Direction)paramBlockState.getValue((Property)HopperBlock.FACING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/* 100 */     return DEFAULT_NAME;
/*     */   }
/*     */   
/*     */   public static void pushItemsTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, HopperBlockEntity paramHopperBlockEntity) {
/* 104 */     paramHopperBlockEntity.cooldownTime--;
/* 105 */     paramHopperBlockEntity.tickedGameTime = paramLevel.getGameTime();
/*     */     
/* 107 */     if (!paramHopperBlockEntity.isOnCooldown()) {
/* 108 */       paramHopperBlockEntity.setCooldown(0);
/* 109 */       tryMoveItems(paramLevel, paramBlockPos, paramBlockState, paramHopperBlockEntity, () -> suckInItems(paramLevel, paramHopperBlockEntity));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean tryMoveItems(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, HopperBlockEntity paramHopperBlockEntity, BooleanSupplier paramBooleanSupplier) {
/* 114 */     if (paramLevel.isClientSide()) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (!paramHopperBlockEntity.isOnCooldown() && ((Boolean)paramBlockState.getValue((Property)HopperBlock.ENABLED)).booleanValue()) {
/* 119 */       boolean bool = false;
/*     */       
/* 121 */       if (!paramHopperBlockEntity.isEmpty()) {
/* 122 */         bool = ejectItems(paramLevel, paramBlockPos, paramHopperBlockEntity);
/*     */       }
/* 124 */       if (!paramHopperBlockEntity.inventoryFull()) {
/* 125 */         bool |= paramBooleanSupplier.getAsBoolean();
/*     */       }
/*     */       
/* 128 */       if (bool) {
/* 129 */         paramHopperBlockEntity.setCooldown(8);
/* 130 */         setChanged(paramLevel, paramBlockPos, paramBlockState);
/* 131 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   private boolean inventoryFull() {
/* 139 */     for (ItemStack itemStack : this.items) {
/* 140 */       if (itemStack.isEmpty() || itemStack.getCount() != itemStack.getMaxStackSize()) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean ejectItems(Level paramLevel, BlockPos paramBlockPos, HopperBlockEntity paramHopperBlockEntity) {
/* 149 */     Container container = getAttachedContainer(paramLevel, paramBlockPos, paramHopperBlockEntity);
/* 150 */     if (container == null) {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     Direction direction = paramHopperBlockEntity.facing.getOpposite();
/* 155 */     if (isFullContainer(container, direction)) {
/* 156 */       return false;
/*     */     }
/*     */     
/* 159 */     for (byte b = 0; b < paramHopperBlockEntity.getContainerSize(); b++) {
/* 160 */       ItemStack itemStack = paramHopperBlockEntity.getItem(b);
/* 161 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 165 */         int i = itemStack.getCount();
/* 166 */         ItemStack itemStack1 = addItem(paramHopperBlockEntity, container, paramHopperBlockEntity.removeItem(b, 1), direction);
/*     */         
/* 168 */         if (itemStack1.isEmpty()) {
/* 169 */           container.setChanged();
/* 170 */           return true;
/*     */         } 
/* 172 */         itemStack.setCount(i);
/* 173 */         if (i == 1) {
/* 174 */           paramHopperBlockEntity.setItem(b, itemStack);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   private static int[] getSlots(Container paramContainer, Direction paramDirection) {
/* 183 */     if (paramContainer instanceof WorldlyContainer) { WorldlyContainer worldlyContainer = (WorldlyContainer)paramContainer;
/* 184 */       return worldlyContainer.getSlotsForFace(paramDirection); }
/*     */     
/* 186 */     int i = paramContainer.getContainerSize();
/* 187 */     if (i < CACHED_SLOTS.length) {
/* 188 */       int[] arrayOfInt1 = CACHED_SLOTS[i];
/* 189 */       if (arrayOfInt1 != null) {
/* 190 */         return arrayOfInt1;
/*     */       }
/* 192 */       int[] arrayOfInt2 = createFlatSlots(i);
/* 193 */       CACHED_SLOTS[i] = arrayOfInt2;
/* 194 */       return arrayOfInt2;
/*     */     } 
/* 196 */     return createFlatSlots(i);
/*     */   }
/*     */   
/*     */   private static int[] createFlatSlots(int paramInt) {
/* 200 */     int[] arrayOfInt = new int[paramInt];
/* 201 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 202 */       arrayOfInt[b] = b;
/*     */     }
/* 204 */     return arrayOfInt;
/*     */   }
/*     */   
/*     */   private static boolean isFullContainer(Container paramContainer, Direction paramDirection) {
/* 208 */     int[] arrayOfInt = getSlots(paramContainer, paramDirection);
/* 209 */     for (int i : arrayOfInt) {
/* 210 */       ItemStack itemStack = paramContainer.getItem(i);
/* 211 */       if (itemStack.getCount() < itemStack.getMaxStackSize()) {
/* 212 */         return false;
/*     */       }
/*     */     } 
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean suckInItems(Level paramLevel, Hopper paramHopper) {
/* 219 */     BlockPos blockPos = BlockPos.containing(paramHopper.getLevelX(), paramHopper.getLevelY() + 1.0D, paramHopper.getLevelZ());
/* 220 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/* 221 */     Container container = getSourceContainer(paramLevel, paramHopper, blockPos, blockState);
/*     */     
/* 223 */     if (container != null) {
/* 224 */       Direction direction = Direction.DOWN;
/* 225 */       for (int i : getSlots(container, direction)) {
/* 226 */         if (tryTakeInItemFromSlot(paramHopper, container, i, direction)) {
/* 227 */           return true;
/*     */         }
/*     */       } 
/* 230 */       return false;
/*     */     } 
/* 232 */     boolean bool = (paramHopper.isGridAligned() && blockState.isCollisionShapeFullBlock((BlockGetter)paramLevel, blockPos) && !blockState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS)) ? true : false;
/* 233 */     if (!bool) {
/* 234 */       for (ItemEntity itemEntity : getItemsAtAndAbove(paramLevel, paramHopper)) {
/* 235 */         if (addItem(paramHopper, itemEntity)) {
/* 236 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 242 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean tryTakeInItemFromSlot(Hopper paramHopper, Container paramContainer, int paramInt, Direction paramDirection) {
/* 246 */     ItemStack itemStack = paramContainer.getItem(paramInt);
/*     */     
/* 248 */     if (!itemStack.isEmpty() && canTakeItemFromContainer(paramHopper, paramContainer, itemStack, paramInt, paramDirection)) {
/* 249 */       int i = itemStack.getCount();
/* 250 */       ItemStack itemStack1 = addItem(paramContainer, paramHopper, paramContainer.removeItem(paramInt, 1), (Direction)null);
/*     */       
/* 252 */       if (itemStack1.isEmpty()) {
/* 253 */         paramContainer.setChanged();
/* 254 */         return true;
/*     */       } 
/* 256 */       itemStack.setCount(i);
/* 257 */       if (i == 1) {
/* 258 */         paramContainer.setItem(paramInt, itemStack);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 263 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean addItem(Container paramContainer, ItemEntity paramItemEntity) {
/* 267 */     boolean bool = false;
/*     */     
/* 269 */     ItemStack itemStack1 = paramItemEntity.getItem().copy();
/* 270 */     ItemStack itemStack2 = addItem((Container)null, paramContainer, itemStack1, (Direction)null);
/*     */     
/* 272 */     if (itemStack2.isEmpty()) {
/* 273 */       bool = true;
/*     */       
/* 275 */       paramItemEntity.setItem(ItemStack.EMPTY);
/* 276 */       paramItemEntity.discard();
/*     */     } else {
/* 278 */       paramItemEntity.setItem(itemStack2);
/*     */     } 
/*     */     
/* 281 */     return bool;
/*     */   }
/*     */   
/*     */   public static ItemStack addItem(Container paramContainer1, Container paramContainer2, ItemStack paramItemStack, Direction paramDirection) {
/* 285 */     if (paramContainer2 instanceof WorldlyContainer) { WorldlyContainer worldlyContainer = (WorldlyContainer)paramContainer2; if (paramDirection != null)
/* 286 */       { int[] arrayOfInt = worldlyContainer.getSlotsForFace(paramDirection);
/*     */         
/* 288 */         for (byte b1 = 0; b1 < arrayOfInt.length && !paramItemStack.isEmpty(); b1++) {
/* 289 */           paramItemStack = tryMoveInItem(paramContainer1, paramContainer2, paramItemStack, arrayOfInt[b1], paramDirection);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 298 */         return paramItemStack; }  }  int i = paramContainer2.getContainerSize(); for (byte b = 0; b < i && !paramItemStack.isEmpty(); b++) paramItemStack = tryMoveInItem(paramContainer1, paramContainer2, paramItemStack, b, paramDirection);  return paramItemStack;
/*     */   }
/*     */   
/*     */   private static boolean canPlaceItemInContainer(Container paramContainer, ItemStack paramItemStack, int paramInt, Direction paramDirection) {
/* 302 */     if (!paramContainer.canPlaceItem(paramInt, paramItemStack)) {
/* 303 */       return false;
/*     */     }
/* 305 */     if (paramContainer instanceof WorldlyContainer) { WorldlyContainer worldlyContainer = (WorldlyContainer)paramContainer; if (worldlyContainer.canPlaceItemThroughFace(paramInt, paramItemStack, paramDirection)); return false; }
/*     */   
/*     */   }
/*     */   private static boolean canTakeItemFromContainer(Container paramContainer1, Container paramContainer2, ItemStack paramItemStack, int paramInt, Direction paramDirection) {
/* 309 */     if (!paramContainer2.canTakeItem(paramContainer1, paramInt, paramItemStack)) {
/* 310 */       return false;
/*     */     }
/* 312 */     if (paramContainer2 instanceof WorldlyContainer) { WorldlyContainer worldlyContainer = (WorldlyContainer)paramContainer2; if (worldlyContainer.canTakeItemThroughFace(paramInt, paramItemStack, paramDirection)); return false; }
/*     */   
/*     */   }
/*     */   private static ItemStack tryMoveInItem(Container paramContainer1, Container paramContainer2, ItemStack paramItemStack, int paramInt, Direction paramDirection) {
/* 316 */     ItemStack itemStack = paramContainer2.getItem(paramInt);
/*     */     
/* 318 */     if (canPlaceItemInContainer(paramContainer2, paramItemStack, paramInt, paramDirection)) {
/* 319 */       boolean bool = false;
/* 320 */       boolean bool1 = paramContainer2.isEmpty();
/* 321 */       if (itemStack.isEmpty()) {
/* 322 */         paramContainer2.setItem(paramInt, paramItemStack);
/* 323 */         paramItemStack = ItemStack.EMPTY;
/* 324 */         bool = true;
/* 325 */       } else if (canMergeItems(itemStack, paramItemStack)) {
/* 326 */         int i = paramItemStack.getMaxStackSize() - itemStack.getCount();
/* 327 */         int j = Math.min(paramItemStack.getCount(), i);
/*     */         
/* 329 */         paramItemStack.shrink(j);
/* 330 */         itemStack.grow(j);
/* 331 */         bool = (j > 0) ? true : false;
/*     */       } 
/* 333 */       if (bool) {
/* 334 */         if (bool1 && paramContainer2 instanceof HopperBlockEntity) { HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)paramContainer2;
/* 335 */           if (!hopperBlockEntity.isOnCustomCooldown()) {
/* 336 */             byte b = 0;
/* 337 */             if (paramContainer1 instanceof HopperBlockEntity) { HopperBlockEntity hopperBlockEntity1 = (HopperBlockEntity)paramContainer1;
/* 338 */               if (hopperBlockEntity.tickedGameTime >= hopperBlockEntity1.tickedGameTime)
/*     */               {
/* 340 */                 b = 1;
/*     */               } }
/*     */             
/* 343 */             hopperBlockEntity.setCooldown(8 - b);
/*     */           }  }
/*     */         
/* 346 */         paramContainer2.setChanged();
/*     */       } 
/*     */     } 
/* 349 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   private static Container getAttachedContainer(Level paramLevel, BlockPos paramBlockPos, HopperBlockEntity paramHopperBlockEntity) {
/* 353 */     return getContainerAt(paramLevel, paramBlockPos.relative(paramHopperBlockEntity.facing));
/*     */   }
/*     */   
/*     */   private static Container getSourceContainer(Level paramLevel, Hopper paramHopper, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 357 */     return getContainerAt(paramLevel, paramBlockPos, paramBlockState, paramHopper.getLevelX(), paramHopper.getLevelY() + 1.0D, paramHopper.getLevelZ());
/*     */   }
/*     */   
/*     */   public static List<ItemEntity> getItemsAtAndAbove(Level paramLevel, Hopper paramHopper) {
/* 361 */     AABB aABB = paramHopper.getSuckAabb().move(paramHopper.getLevelX() - 0.5D, paramHopper.getLevelY() - 0.5D, paramHopper.getLevelZ() - 0.5D);
/* 362 */     return paramLevel.getEntitiesOfClass(ItemEntity.class, aABB, EntitySelector.ENTITY_STILL_ALIVE);
/*     */   }
/*     */   
/*     */   public static Container getContainerAt(Level paramLevel, BlockPos paramBlockPos) {
/* 366 */     return getContainerAt(paramLevel, paramBlockPos, paramLevel.getBlockState(paramBlockPos), paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   private static Container getContainerAt(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 370 */     Container container = getBlockContainer(paramLevel, paramBlockPos, paramBlockState);
/* 371 */     if (container == null) {
/* 372 */       container = getEntityContainer(paramLevel, paramDouble1, paramDouble2, paramDouble3);
/*     */     }
/* 374 */     return container;
/*     */   }
/*     */   
/*     */   private static Container getBlockContainer(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 378 */     Block block = paramBlockState.getBlock();
/* 379 */     if (block instanceof WorldlyContainerHolder)
/* 380 */       return (Container)((WorldlyContainerHolder)block).getContainer(paramBlockState, (LevelAccessor)paramLevel, paramBlockPos); 
/* 381 */     if (paramBlockState.hasBlockEntity()) {
/* 382 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 383 */       if (blockEntity instanceof Container) { Container container = (Container)blockEntity;
/*     */ 
/*     */ 
/*     */         
/* 387 */         if (container instanceof ChestBlockEntity && 
/* 388 */           block instanceof ChestBlock) {
/* 389 */           container = ChestBlock.getContainer((ChestBlock)block, paramBlockState, paramLevel, paramBlockPos, true);
/*     */         }
/*     */         
/* 392 */         return container; }
/*     */     
/*     */     } 
/* 395 */     return null;
/*     */   }
/*     */   
/*     */   private static Container getEntityContainer(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 399 */     List<Container> list = paramLevel.getEntities((Entity)null, new AABB(paramDouble1 - 0.5D, paramDouble2 - 0.5D, paramDouble3 - 0.5D, paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
/*     */     
/* 401 */     if (!list.isEmpty()) {
/* 402 */       return list.get(paramLevel.random.nextInt(list.size()));
/*     */     }
/* 404 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean canMergeItems(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 408 */     return (paramItemStack1.getCount() <= paramItemStack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(paramItemStack1, paramItemStack2));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelX() {
/* 413 */     return this.worldPosition.getX() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelY() {
/* 418 */     return this.worldPosition.getY() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelZ() {
/* 423 */     return this.worldPosition.getZ() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGridAligned() {
/* 428 */     return true;
/*     */   }
/*     */   
/*     */   private void setCooldown(int paramInt) {
/* 432 */     this.cooldownTime = paramInt;
/*     */   }
/*     */   
/*     */   private boolean isOnCooldown() {
/* 436 */     return (this.cooldownTime > 0);
/*     */   }
/*     */   
/*     */   private boolean isOnCustomCooldown() {
/* 440 */     return (this.cooldownTime > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/* 445 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> paramNonNullList) {
/* 450 */     this.items = paramNonNullList;
/*     */   }
/*     */   
/*     */   public static void entityInside(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity, HopperBlockEntity paramHopperBlockEntity) {
/* 454 */     if (paramEntity instanceof ItemEntity) { ItemEntity itemEntity = (ItemEntity)paramEntity; if (!itemEntity.getItem().isEmpty() && 
/* 455 */         paramEntity.getBoundingBox().move(-paramBlockPos.getX(), -paramBlockPos.getY(), -paramBlockPos.getZ()).intersects(paramHopperBlockEntity.getSuckAabb())) {
/* 456 */         tryMoveItems(paramLevel, paramBlockPos, paramBlockState, paramHopperBlockEntity, () -> addItem(paramHopperBlockEntity, paramItemEntity));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory) {
/* 463 */     return (AbstractContainerMenu)new HopperMenu(paramInt, paramInventory, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\HopperBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */