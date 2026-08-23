/*     */ package net.minecraft.world.inventory;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.level.block.CrafterBlock;
/*     */ 
/*     */ public class CrafterMenu extends AbstractContainerMenu implements ContainerListener {
/*     */   protected static final int SLOT_COUNT = 9;
/*     */   private static final int INV_SLOT_START = 9;
/*  19 */   private final ResultContainer resultContainer = new ResultContainer(); private static final int INV_SLOT_END = 36; private static final int USE_ROW_SLOT_START = 36; private static final int USE_ROW_SLOT_END = 45;
/*     */   private final ContainerData containerData;
/*     */   private final Player player;
/*     */   private final CraftingContainer container;
/*     */   
/*     */   public CrafterMenu(int paramInt, Inventory paramInventory) {
/*  25 */     super(MenuType.CRAFTER_3x3, paramInt);
/*  26 */     this.player = paramInventory.player;
/*  27 */     this.containerData = new SimpleContainerData(10);
/*  28 */     this.container = new TransientCraftingContainer(this, 3, 3);
/*  29 */     addSlots(paramInventory);
/*     */   }
/*     */   
/*     */   public CrafterMenu(int paramInt, Inventory paramInventory, CraftingContainer paramCraftingContainer, ContainerData paramContainerData) {
/*  33 */     super(MenuType.CRAFTER_3x3, paramInt);
/*  34 */     this.player = paramInventory.player;
/*  35 */     this.containerData = paramContainerData;
/*  36 */     this.container = paramCraftingContainer;
/*  37 */     checkContainerSize(paramCraftingContainer, 9);
/*  38 */     paramCraftingContainer.startOpen((ContainerUser)paramInventory.player);
/*  39 */     addSlots(paramInventory);
/*  40 */     addSlotListener(this);
/*     */   }
/*     */   
/*     */   private void addSlots(Inventory paramInventory) {
/*  44 */     for (byte b = 0; b < 3; b++) {
/*  45 */       for (byte b1 = 0; b1 < 3; b1++) {
/*  46 */         int i = b1 + b * 3;
/*  47 */         addSlot(new CrafterSlot(this.container, i, 26 + b1 * 18, 17 + b * 18, this));
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     addStandardInventorySlots((Container)paramInventory, 8, 84);
/*     */     
/*  53 */     addSlot(new NonInteractiveResultSlot(this.resultContainer, 0, 134, 35));
/*  54 */     addDataSlots(this.containerData);
/*  55 */     refreshRecipeResult();
/*     */   }
/*     */   
/*     */   public void setSlotState(int paramInt, boolean paramBoolean) {
/*  59 */     CrafterSlot crafterSlot = (CrafterSlot)getSlot(paramInt);
/*  60 */     this.containerData.set(crafterSlot.index, paramBoolean ? 0 : 1);
/*  61 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public boolean isSlotDisabled(int paramInt) {
/*  65 */     if (paramInt > -1 && paramInt < 9) {
/*  66 */       return (this.containerData.get(paramInt) == 1);
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/*  72 */     return (this.containerData.get(9) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/*  77 */     ItemStack itemStack = ItemStack.EMPTY;
/*  78 */     Slot slot = (Slot)this.slots.get(paramInt);
/*  79 */     if (slot != null && slot.hasItem()) {
/*  80 */       ItemStack itemStack1 = slot.getItem();
/*  81 */       itemStack = itemStack1.copy();
/*     */       
/*  83 */       if (paramInt < 9) {
/*  84 */         if (!moveItemStackTo(itemStack1, 9, 45, true)) {
/*  85 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/*  88 */       else if (!moveItemStackTo(itemStack1, 0, 9, false)) {
/*  89 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/*  92 */       if (itemStack1.isEmpty()) {
/*  93 */         slot.set(ItemStack.EMPTY);
/*     */       } else {
/*  95 */         slot.setChanged();
/*     */       } 
/*  97 */       if (itemStack1.getCount() == itemStack.getCount())
/*     */       {
/*  99 */         return ItemStack.EMPTY;
/*     */       }
/* 101 */       slot.onTake(paramPlayer, itemStack1);
/*     */     } 
/*     */     
/* 104 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 109 */     return this.container.stillValid(paramPlayer);
/*     */   }
/*     */   
/*     */   private void refreshRecipeResult() {
/* 113 */     Player player = this.player; if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 114 */       ServerLevel serverLevel = serverPlayer.level();
/* 115 */       CraftingInput craftingInput = this.container.asCraftInput();
/*     */ 
/*     */       
/* 118 */       ItemStack itemStack = CrafterBlock.getPotentialResults(serverLevel, craftingInput).map(paramRecipeHolder -> ((CraftingRecipe)paramRecipeHolder.value()).assemble((RecipeInput)paramCraftingInput, (HolderLookup.Provider)paramServerLevel.registryAccess())).orElse(ItemStack.EMPTY);
/* 119 */       this.resultContainer.setItem(0, itemStack); }
/*     */   
/*     */   }
/*     */   
/*     */   public Container getContainer() {
/* 124 */     return this.container;
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt, ItemStack paramItemStack) {
/* 129 */     refreshRecipeResult();
/*     */   }
/*     */   
/*     */   public void dataChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2) {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\CrafterMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */