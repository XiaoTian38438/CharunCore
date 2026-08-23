/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.ItemStackWithSlot;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityEquipment;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class Inventory
/*     */   implements Container, Nameable
/*     */ {
/*     */   public static final int POP_TIME_DURATION = 5;
/*     */   public static final int INVENTORY_SIZE = 36;
/*     */   public static final int SELECTION_SIZE = 9;
/*  40 */   public static final Int2ObjectMap<EquipmentSlot> EQUIPMENT_SLOT_MAPPING = (Int2ObjectMap<EquipmentSlot>)new Int2ObjectArrayMap(Map.of(
/*  41 */         Integer.valueOf(EquipmentSlot.FEET.getIndex(36)), EquipmentSlot.FEET, 
/*  42 */         Integer.valueOf(EquipmentSlot.LEGS.getIndex(36)), EquipmentSlot.LEGS, 
/*  43 */         Integer.valueOf(EquipmentSlot.CHEST.getIndex(36)), EquipmentSlot.CHEST, 
/*  44 */         Integer.valueOf(EquipmentSlot.HEAD.getIndex(36)), EquipmentSlot.HEAD, 
/*  45 */         Integer.valueOf(40), EquipmentSlot.OFFHAND, 
/*  46 */         Integer.valueOf(41), EquipmentSlot.BODY, 
/*  47 */         Integer.valueOf(42), EquipmentSlot.SADDLE)); public static final int SLOT_OFFHAND = 40; public static final int SLOT_BODY_ARMOR = 41;
/*     */   public static final int SLOT_SADDLE = 42;
/*     */   public static final int NOT_FOUND_INDEX = -1;
/*  50 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.inventory");
/*     */   
/*  52 */   private final NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
/*     */   
/*     */   private int selected;
/*     */   public final Player player;
/*     */   private final EntityEquipment equipment;
/*     */   private int timesChanged;
/*     */   
/*     */   public Inventory(Player paramPlayer, EntityEquipment paramEntityEquipment) {
/*  60 */     this.player = paramPlayer;
/*  61 */     this.equipment = paramEntityEquipment;
/*     */   }
/*     */   
/*     */   public int getSelectedSlot() {
/*  65 */     return this.selected;
/*     */   }
/*     */   
/*     */   public void setSelectedSlot(int paramInt) {
/*  69 */     if (!isHotbarSlot(paramInt)) {
/*  70 */       throw new IllegalArgumentException("Invalid selected slot");
/*     */     }
/*  72 */     this.selected = paramInt;
/*     */   }
/*     */   
/*     */   public ItemStack getSelectedItem() {
/*  76 */     return (ItemStack)this.items.get(this.selected);
/*     */   }
/*     */   
/*     */   public ItemStack setSelectedItem(ItemStack paramItemStack) {
/*  80 */     return (ItemStack)this.items.set(this.selected, paramItemStack);
/*     */   }
/*     */   
/*     */   public static int getSelectionSize() {
/*  84 */     return 9;
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getNonEquipmentItems() {
/*  88 */     return this.items;
/*     */   }
/*     */   
/*     */   private boolean hasRemainingSpaceForItem(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/*  92 */     return (!paramItemStack1.isEmpty() && 
/*  93 */       ItemStack.isSameItemSameComponents(paramItemStack1, paramItemStack2) && paramItemStack1
/*  94 */       .isStackable() && paramItemStack1
/*  95 */       .getCount() < getMaxStackSize(paramItemStack1));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFreeSlot() {
/* 100 */     for (byte b = 0; b < this.items.size(); b++) {
/* 101 */       if (((ItemStack)this.items.get(b)).isEmpty()) {
/* 102 */         return b;
/*     */       }
/*     */     } 
/* 105 */     return -1;
/*     */   }
/*     */   
/*     */   public void addAndPickItem(ItemStack paramItemStack) {
/* 109 */     setSelectedSlot(getSuitableHotbarSlot());
/*     */ 
/*     */     
/* 112 */     if (!((ItemStack)this.items.get(this.selected)).isEmpty()) {
/* 113 */       int i = getFreeSlot();
/* 114 */       if (i != -1) {
/* 115 */         this.items.set(i, this.items.get(this.selected));
/*     */       }
/*     */     } 
/*     */     
/* 119 */     this.items.set(this.selected, paramItemStack);
/*     */   }
/*     */   
/*     */   public void pickSlot(int paramInt) {
/* 123 */     setSelectedSlot(getSuitableHotbarSlot());
/*     */ 
/*     */     
/* 126 */     ItemStack itemStack = (ItemStack)this.items.get(this.selected);
/* 127 */     this.items.set(this.selected, this.items.get(paramInt));
/* 128 */     this.items.set(paramInt, itemStack);
/*     */   }
/*     */   
/*     */   public static boolean isHotbarSlot(int paramInt) {
/* 132 */     return (paramInt >= 0 && paramInt < 9);
/*     */   }
/*     */   
/*     */   public int findSlotMatchingItem(ItemStack paramItemStack) {
/* 136 */     for (byte b = 0; b < this.items.size(); b++) {
/* 137 */       if (!((ItemStack)this.items.get(b)).isEmpty() && ItemStack.isSameItemSameComponents(paramItemStack, (ItemStack)this.items.get(b))) {
/* 138 */         return b;
/*     */       }
/*     */     } 
/* 141 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isUsableForCrafting(ItemStack paramItemStack) {
/* 145 */     return (!paramItemStack.isDamaged() && 
/* 146 */       !paramItemStack.isEnchanted() && 
/* 147 */       !paramItemStack.has(DataComponents.CUSTOM_NAME));
/*     */   }
/*     */   
/*     */   public int findSlotMatchingCraftingIngredient(Holder<Item> paramHolder, ItemStack paramItemStack) {
/* 151 */     for (byte b = 0; b < this.items.size(); b++) {
/* 152 */       ItemStack itemStack = (ItemStack)this.items.get(b);
/* 153 */       if (!itemStack.isEmpty() && itemStack
/* 154 */         .is(paramHolder) && 
/* 155 */         isUsableForCrafting(itemStack) && (paramItemStack
/* 156 */         .isEmpty() || ItemStack.isSameItemSameComponents(paramItemStack, itemStack)))
/*     */       {
/* 158 */         return b;
/*     */       }
/*     */     } 
/* 161 */     return -1;
/*     */   }
/*     */   
/*     */   public int getSuitableHotbarSlot() {
/*     */     byte b;
/* 166 */     for (b = 0; b < 9; b++) {
/* 167 */       int i = (this.selected + b) % 9;
/*     */       
/* 169 */       if (((ItemStack)this.items.get(i)).isEmpty()) {
/* 170 */         return i;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 175 */     for (b = 0; b < 9; b++) {
/* 176 */       int i = (this.selected + b) % 9;
/*     */       
/* 178 */       if (!((ItemStack)this.items.get(i)).isEnchanted()) {
/* 179 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 183 */     return this.selected;
/*     */   }
/*     */   
/*     */   public int clearOrCountMatchingItems(Predicate<ItemStack> paramPredicate, int paramInt, Container paramContainer) {
/* 187 */     int i = 0;
/* 188 */     boolean bool = (paramInt == 0) ? true : false;
/*     */     
/* 190 */     i += ContainerHelper.clearOrCountMatchingItems(this, paramPredicate, paramInt - i, bool);
/* 191 */     i += ContainerHelper.clearOrCountMatchingItems(paramContainer, paramPredicate, paramInt - i, bool);
/*     */     
/* 193 */     ItemStack itemStack = this.player.containerMenu.getCarried();
/* 194 */     i += ContainerHelper.clearOrCountMatchingItems(itemStack, paramPredicate, paramInt - i, bool);
/* 195 */     if (itemStack.isEmpty()) {
/* 196 */       this.player.containerMenu.setCarried(ItemStack.EMPTY);
/*     */     }
/* 198 */     return i;
/*     */   }
/*     */   
/*     */   private int addResource(ItemStack paramItemStack) {
/* 202 */     int i = getSlotWithRemainingSpace(paramItemStack);
/* 203 */     if (i == -1) {
/* 204 */       i = getFreeSlot();
/*     */     }
/* 206 */     if (i == -1) {
/* 207 */       return paramItemStack.getCount();
/*     */     }
/* 209 */     return addResource(i, paramItemStack);
/*     */   }
/*     */   
/*     */   private int addResource(int paramInt, ItemStack paramItemStack) {
/* 213 */     int i = paramItemStack.getCount();
/*     */     
/* 215 */     ItemStack itemStack = getItem(paramInt);
/* 216 */     if (itemStack.isEmpty()) {
/* 217 */       itemStack = paramItemStack.copyWithCount(0);
/* 218 */       setItem(paramInt, itemStack);
/*     */     } 
/*     */     
/* 221 */     int j = getMaxStackSize(itemStack) - itemStack.getCount();
/* 222 */     int k = Math.min(i, j);
/* 223 */     if (k == 0) {
/* 224 */       return i;
/*     */     }
/*     */     
/* 227 */     i -= k;
/* 228 */     itemStack.grow(k);
/* 229 */     itemStack.setPopTime(5);
/*     */     
/* 231 */     return i;
/*     */   }
/*     */   
/*     */   public int getSlotWithRemainingSpace(ItemStack paramItemStack) {
/* 235 */     if (hasRemainingSpaceForItem(getItem(this.selected), paramItemStack)) {
/* 236 */       return this.selected;
/*     */     }
/* 238 */     if (hasRemainingSpaceForItem(getItem(40), paramItemStack)) {
/* 239 */       return 40;
/*     */     }
/* 241 */     for (byte b = 0; b < this.items.size(); b++) {
/* 242 */       if (hasRemainingSpaceForItem((ItemStack)this.items.get(b), paramItemStack)) {
/* 243 */         return b;
/*     */       }
/*     */     } 
/* 246 */     return -1;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 250 */     for (byte b = 0; b < this.items.size(); b++) {
/* 251 */       ItemStack itemStack = getItem(b);
/* 252 */       if (!itemStack.isEmpty()) {
/* 253 */         itemStack.inventoryTick(this.player.level(), (Entity)this.player, (b == this.selected) ? EquipmentSlot.MAINHAND : null);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean add(ItemStack paramItemStack) {
/* 259 */     return add(-1, paramItemStack);
/*     */   }
/*     */   
/*     */   public boolean add(int paramInt, ItemStack paramItemStack) {
/* 263 */     if (paramItemStack.isEmpty()) {
/* 264 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 268 */       if (!paramItemStack.isDamaged()) {
/*     */         int i;
/*     */         do {
/* 271 */           i = paramItemStack.getCount();
/* 272 */           if (paramInt == -1) {
/* 273 */             paramItemStack.setCount(addResource(paramItemStack));
/*     */           } else {
/* 275 */             paramItemStack.setCount(addResource(paramInt, paramItemStack));
/*     */           } 
/* 277 */         } while (!paramItemStack.isEmpty() && paramItemStack.getCount() < i);
/* 278 */         if (paramItemStack.getCount() == i && this.player.hasInfiniteMaterials()) {
/*     */           
/* 280 */           paramItemStack.setCount(0);
/* 281 */           return true;
/*     */         } 
/* 283 */         return (paramItemStack.getCount() < i);
/*     */       } 
/*     */       
/* 286 */       if (paramInt == -1) {
/* 287 */         paramInt = getFreeSlot();
/*     */       }
/* 289 */       if (paramInt >= 0) {
/* 290 */         this.items.set(paramInt, paramItemStack.copyAndClear());
/* 291 */         ((ItemStack)this.items.get(paramInt)).setPopTime(5);
/* 292 */         return true;
/* 293 */       }  if (this.player.hasInfiniteMaterials()) {
/*     */         
/* 295 */         paramItemStack.setCount(0);
/* 296 */         return true;
/*     */       } 
/* 298 */       return false;
/* 299 */     } catch (Throwable throwable) {
/* 300 */       CrashReport crashReport = CrashReport.forThrowable(throwable, "Adding item to inventory");
/* 301 */       CrashReportCategory crashReportCategory = crashReport.addCategory("Item being added");
/*     */       
/* 303 */       crashReportCategory.setDetail("Item ID", Integer.valueOf(Item.getId(paramItemStack.getItem())));
/* 304 */       crashReportCategory.setDetail("Item data", Integer.valueOf(paramItemStack.getDamageValue()));
/* 305 */       crashReportCategory.setDetail("Item name", () -> paramItemStack.getHoverName().getString());
/*     */       
/* 307 */       throw new ReportedException(crashReport);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void placeItemBackInInventory(ItemStack paramItemStack) {
/* 312 */     placeItemBackInInventory(paramItemStack, true);
/*     */   }
/*     */   
/*     */   public void placeItemBackInInventory(ItemStack paramItemStack, boolean paramBoolean) {
/* 316 */     while (!paramItemStack.isEmpty()) {
/* 317 */       int i = getSlotWithRemainingSpace(paramItemStack);
/* 318 */       if (i == -1) {
/* 319 */         i = getFreeSlot();
/*     */       }
/*     */       
/* 322 */       if (i == -1) {
/* 323 */         this.player.drop(paramItemStack, false);
/*     */         
/*     */         break;
/*     */       } 
/* 327 */       int j = paramItemStack.getMaxStackSize() - getItem(i).getCount();
/*     */       
/* 329 */       if (add(i, paramItemStack.split(j)) && paramBoolean) { Player player = this.player; if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 330 */           serverPlayer.connection.send((Packet)createInventoryUpdatePacket(i)); }
/*     */          }
/*     */     
/*     */     } 
/*     */   }
/*     */   public ClientboundSetPlayerInventoryPacket createInventoryUpdatePacket(int paramInt) {
/* 336 */     return new ClientboundSetPlayerInventoryPacket(paramInt, getItem(paramInt).copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/* 341 */     if (paramInt1 < this.items.size()) {
/* 342 */       return ContainerHelper.removeItem((List)this.items, paramInt1, paramInt2);
/*     */     }
/* 344 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(paramInt1);
/* 345 */     if (equipmentSlot != null) {
/* 346 */       ItemStack itemStack = this.equipment.get(equipmentSlot);
/* 347 */       if (!itemStack.isEmpty()) {
/* 348 */         return itemStack.split(paramInt2);
/*     */       }
/*     */     } 
/* 351 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeItem(ItemStack paramItemStack) {
/* 358 */     for (byte b = 0; b < this.items.size(); b++) {
/* 359 */       if (this.items.get(b) == paramItemStack) {
/* 360 */         this.items.set(b, ItemStack.EMPTY);
/*     */         return;
/*     */       } 
/*     */     } 
/* 364 */     for (ObjectIterator<EquipmentSlot> objectIterator = EQUIPMENT_SLOT_MAPPING.values().iterator(); objectIterator.hasNext(); ) { EquipmentSlot equipmentSlot = objectIterator.next();
/* 365 */       ItemStack itemStack = this.equipment.get(equipmentSlot);
/* 366 */       if (itemStack == paramItemStack) {
/* 367 */         this.equipment.set(equipmentSlot, ItemStack.EMPTY);
/*     */         return;
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/* 375 */     if (paramInt < this.items.size()) {
/* 376 */       ItemStack itemStack = (ItemStack)this.items.get(paramInt);
/* 377 */       this.items.set(paramInt, ItemStack.EMPTY);
/* 378 */       return itemStack;
/*     */     } 
/* 380 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(paramInt);
/* 381 */     if (equipmentSlot != null) {
/* 382 */       return this.equipment.set(equipmentSlot, ItemStack.EMPTY);
/*     */     }
/* 384 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/* 389 */     if (paramInt < this.items.size()) {
/* 390 */       this.items.set(paramInt, paramItemStack);
/*     */     }
/* 392 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(paramInt);
/* 393 */     if (equipmentSlot != null) {
/* 394 */       this.equipment.set(equipmentSlot, paramItemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void save(ValueOutput.TypedOutputList<ItemStackWithSlot> paramTypedOutputList) {
/* 399 */     for (byte b = 0; b < this.items.size(); b++) {
/* 400 */       ItemStack itemStack = (ItemStack)this.items.get(b);
/* 401 */       if (!itemStack.isEmpty()) {
/* 402 */         paramTypedOutputList.add(new ItemStackWithSlot(b, itemStack));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void load(ValueInput.TypedInputList<ItemStackWithSlot> paramTypedInputList) {
/* 408 */     this.items.clear();
/* 409 */     for (ItemStackWithSlot itemStackWithSlot : paramTypedInputList) {
/* 410 */       if (itemStackWithSlot.isValidInContainer(this.items.size())) {
/* 411 */         setItem(itemStackWithSlot.slot(), itemStackWithSlot.stack());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 418 */     return this.items.size() + EQUIPMENT_SLOT_MAPPING.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 423 */     for (ItemStack itemStack : this.items) {
/* 424 */       if (!itemStack.isEmpty()) {
/* 425 */         return false;
/*     */       }
/*     */     } 
/* 428 */     for (ObjectIterator<EquipmentSlot> objectIterator = EQUIPMENT_SLOT_MAPPING.values().iterator(); objectIterator.hasNext(); ) { EquipmentSlot equipmentSlot = objectIterator.next();
/* 429 */       if (!this.equipment.get(equipmentSlot).isEmpty()) {
/* 430 */         return false;
/*     */       } }
/*     */     
/* 433 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/* 438 */     if (paramInt < this.items.size()) {
/* 439 */       return (ItemStack)this.items.get(paramInt);
/*     */     }
/* 441 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(paramInt);
/* 442 */     if (equipmentSlot != null) {
/* 443 */       return this.equipment.get(equipmentSlot);
/*     */     }
/* 445 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 450 */     return DEFAULT_NAME;
/*     */   }
/*     */   
/*     */   public void dropAll() {
/* 454 */     for (byte b = 0; b < this.items.size(); b++) {
/* 455 */       ItemStack itemStack = (ItemStack)this.items.get(b);
/* 456 */       if (!itemStack.isEmpty()) {
/* 457 */         this.player.drop(itemStack, true, false);
/* 458 */         this.items.set(b, ItemStack.EMPTY);
/*     */       } 
/*     */     } 
/* 461 */     this.equipment.dropAll((LivingEntity)this.player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 466 */     this.timesChanged++;
/*     */   }
/*     */   
/*     */   public int getTimesChanged() {
/* 470 */     return this.timesChanged;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 475 */     return true;
/*     */   }
/*     */   
/*     */   public boolean contains(ItemStack paramItemStack) {
/* 479 */     for (ItemStack itemStack : this) {
/* 480 */       if (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, paramItemStack)) {
/* 481 */         return true;
/*     */       }
/*     */     } 
/* 484 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(TagKey<Item> paramTagKey) {
/* 488 */     for (ItemStack itemStack : this) {
/* 489 */       if (!itemStack.isEmpty() && itemStack.is(paramTagKey)) {
/* 490 */         return true;
/*     */       }
/*     */     } 
/* 493 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Predicate<ItemStack> paramPredicate) {
/* 497 */     for (ItemStack itemStack : this) {
/* 498 */       if (paramPredicate.test(itemStack)) {
/* 499 */         return true;
/*     */       }
/*     */     } 
/* 502 */     return false;
/*     */   }
/*     */   
/*     */   public void replaceWith(Inventory paramInventory) {
/* 506 */     for (byte b = 0; b < getContainerSize(); b++) {
/* 507 */       setItem(b, paramInventory.getItem(b));
/*     */     }
/* 509 */     setSelectedSlot(paramInventory.getSelectedSlot());
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 514 */     this.items.clear();
/* 515 */     this.equipment.clear();
/*     */   }
/*     */   
/*     */   public void fillStackedContents(StackedItemContents paramStackedItemContents) {
/* 519 */     for (ItemStack itemStack : this.items) {
/* 520 */       paramStackedItemContents.accountSimpleStack(itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemStack removeFromSelected(boolean paramBoolean) {
/* 525 */     ItemStack itemStack = getSelectedItem();
/* 526 */     if (itemStack.isEmpty()) {
/* 527 */       return ItemStack.EMPTY;
/*     */     }
/* 529 */     return removeItem(this.selected, paramBoolean ? itemStack.getCount() : 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\player\Inventory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */