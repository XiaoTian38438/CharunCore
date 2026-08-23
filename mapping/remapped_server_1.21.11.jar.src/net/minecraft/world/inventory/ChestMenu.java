/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ChestMenu extends AbstractContainerMenu {
/*     */   private final Container container;
/*     */   
/*     */   private ChestMenu(MenuType<?> paramMenuType, int paramInt1, Inventory paramInventory, int paramInt2) {
/*  14 */     this(paramMenuType, paramInt1, paramInventory, (Container)new SimpleContainer(9 * paramInt2), paramInt2);
/*     */   }
/*     */   private final int containerRows;
/*     */   public static ChestMenu oneRow(int paramInt, Inventory paramInventory) {
/*  18 */     return new ChestMenu(MenuType.GENERIC_9x1, paramInt, paramInventory, 1);
/*     */   }
/*     */   
/*     */   public static ChestMenu twoRows(int paramInt, Inventory paramInventory) {
/*  22 */     return new ChestMenu(MenuType.GENERIC_9x2, paramInt, paramInventory, 2);
/*     */   }
/*     */   
/*     */   public static ChestMenu threeRows(int paramInt, Inventory paramInventory) {
/*  26 */     return new ChestMenu(MenuType.GENERIC_9x3, paramInt, paramInventory, 3);
/*     */   }
/*     */   
/*     */   public static ChestMenu fourRows(int paramInt, Inventory paramInventory) {
/*  30 */     return new ChestMenu(MenuType.GENERIC_9x4, paramInt, paramInventory, 4);
/*     */   }
/*     */   
/*     */   public static ChestMenu fiveRows(int paramInt, Inventory paramInventory) {
/*  34 */     return new ChestMenu(MenuType.GENERIC_9x5, paramInt, paramInventory, 5);
/*     */   }
/*     */   
/*     */   public static ChestMenu sixRows(int paramInt, Inventory paramInventory) {
/*  38 */     return new ChestMenu(MenuType.GENERIC_9x6, paramInt, paramInventory, 6);
/*     */   }
/*     */   
/*     */   public static ChestMenu threeRows(int paramInt, Inventory paramInventory, Container paramContainer) {
/*  42 */     return new ChestMenu(MenuType.GENERIC_9x3, paramInt, paramInventory, paramContainer, 3);
/*     */   }
/*     */   
/*     */   public static ChestMenu sixRows(int paramInt, Inventory paramInventory, Container paramContainer) {
/*  46 */     return new ChestMenu(MenuType.GENERIC_9x6, paramInt, paramInventory, paramContainer, 6);
/*     */   }
/*     */   
/*     */   public ChestMenu(MenuType<?> paramMenuType, int paramInt1, Inventory paramInventory, Container paramContainer, int paramInt2) {
/*  50 */     super(paramMenuType, paramInt1);
/*  51 */     checkContainerSize(paramContainer, paramInt2 * 9);
/*  52 */     this.container = paramContainer;
/*  53 */     this.containerRows = paramInt2;
/*  54 */     paramContainer.startOpen((ContainerUser)paramInventory.player);
/*     */     
/*  56 */     byte b = 18;
/*  57 */     addChestGrid(paramContainer, 8, 18);
/*     */     
/*  59 */     int i = 18 + this.containerRows * 18 + 13;
/*  60 */     addStandardInventorySlots((Container)paramInventory, 8, i);
/*     */   }
/*     */   
/*     */   private void addChestGrid(Container paramContainer, int paramInt1, int paramInt2) {
/*  64 */     for (byte b = 0; b < this.containerRows; b++) {
/*  65 */       for (byte b1 = 0; b1 < 9; b1++) {
/*  66 */         addSlot(new Slot(paramContainer, b1 + b * 9, paramInt1 + b1 * 18, paramInt2 + b * 18));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  73 */     return this.container.stillValid(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/*  78 */     ItemStack itemStack = ItemStack.EMPTY;
/*  79 */     Slot slot = (Slot)this.slots.get(paramInt);
/*  80 */     if (slot != null && slot.hasItem()) {
/*  81 */       ItemStack itemStack1 = slot.getItem();
/*  82 */       itemStack = itemStack1.copy();
/*     */       
/*  84 */       if (paramInt < this.containerRows * 9) {
/*  85 */         if (!moveItemStackTo(itemStack1, this.containerRows * 9, this.slots.size(), true)) {
/*  86 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/*  89 */       else if (!moveItemStackTo(itemStack1, 0, this.containerRows * 9, false)) {
/*  90 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/*  93 */       if (itemStack1.isEmpty()) {
/*  94 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/*  96 */         slot.setChanged();
/*     */       } 
/*     */     } 
/*  99 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player paramPlayer) {
/* 104 */     super.removed(paramPlayer);
/* 105 */     this.container.stopOpen((ContainerUser)paramPlayer);
/*     */   }
/*     */   
/*     */   public Container getContainer() {
/* 109 */     return this.container;
/*     */   }
/*     */   
/*     */   public int getRowCount() {
/* 113 */     return this.containerRows;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ChestMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */