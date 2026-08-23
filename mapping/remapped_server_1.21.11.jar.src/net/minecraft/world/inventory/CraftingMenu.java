/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class CraftingMenu extends AbstractCraftingMenu {
/*     */   private static final int CRAFTING_GRID_WIDTH = 3;
/*     */   private static final int CRAFTING_GRID_HEIGHT = 3;
/*     */   public static final int RESULT_SLOT = 0;
/*     */   private static final int CRAFT_SLOT_START = 1;
/*     */   private static final int CRAFT_SLOT_COUNT = 9;
/*     */   private static final int CRAFT_SLOT_END = 10;
/*     */   private static final int INV_SLOT_START = 10;
/*     */   private static final int INV_SLOT_END = 37;
/*     */   private static final int USE_ROW_SLOT_START = 37;
/*     */   private static final int USE_ROW_SLOT_END = 46;
/*     */   private final ContainerLevelAccess access;
/*     */   private final Player player;
/*     */   private boolean placingRecipe;
/*     */   
/*     */   public CraftingMenu(int paramInt, Inventory paramInventory) {
/*  39 */     this(paramInt, paramInventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public CraftingMenu(int paramInt, Inventory paramInventory, ContainerLevelAccess paramContainerLevelAccess) {
/*  43 */     super(MenuType.CRAFTING, paramInt, 3, 3);
/*  44 */     this.access = paramContainerLevelAccess;
/*  45 */     this.player = paramInventory.player;
/*     */     
/*  47 */     addResultSlot(this.player, 124, 35);
/*  48 */     addCraftingGridSlots(30, 17);
/*     */     
/*  50 */     addStandardInventorySlots((Container)paramInventory, 8, 84);
/*     */   }
/*     */   
/*     */   protected static void slotChangedCraftingGrid(AbstractContainerMenu paramAbstractContainerMenu, ServerLevel paramServerLevel, Player paramPlayer, CraftingContainer paramCraftingContainer, ResultContainer paramResultContainer, RecipeHolder<CraftingRecipe> paramRecipeHolder) {
/*  54 */     CraftingInput craftingInput = paramCraftingContainer.asCraftInput();
/*  55 */     ServerPlayer serverPlayer = (ServerPlayer)paramPlayer;
/*  56 */     ItemStack itemStack = ItemStack.EMPTY;
/*  57 */     Optional<RecipeHolder> optional = paramServerLevel.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, (RecipeInput)craftingInput, (Level)paramServerLevel, paramRecipeHolder);
/*  58 */     if (optional.isPresent()) {
/*  59 */       RecipeHolder<?> recipeHolder = optional.get();
/*  60 */       CraftingRecipe craftingRecipe = (CraftingRecipe)recipeHolder.value();
/*  61 */       if (paramResultContainer.setRecipeUsed(serverPlayer, recipeHolder)) {
/*  62 */         ItemStack itemStack1 = craftingRecipe.assemble((RecipeInput)craftingInput, (HolderLookup.Provider)paramServerLevel.registryAccess());
/*  63 */         if (itemStack1.isItemEnabled(paramServerLevel.enabledFeatures())) {
/*  64 */           itemStack = itemStack1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     paramResultContainer.setItem(0, itemStack);
/*  70 */     paramAbstractContainerMenu.setRemoteSlot(0, itemStack);
/*  71 */     serverPlayer.connection.send((Packet)new ClientboundContainerSetSlotPacket(paramAbstractContainerMenu.containerId, paramAbstractContainerMenu.incrementStateId(), 0, itemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container paramContainer) {
/*  76 */     if (!this.placingRecipe) {
/*  77 */       this.access.execute((paramLevel, paramBlockPos) -> {
/*     */             if (paramLevel instanceof ServerLevel) {
/*     */               ServerLevel serverLevel = (ServerLevel)paramLevel;
/*     */               slotChangedCraftingGrid(this, serverLevel, this.player, this.craftSlots, this.resultSlots, (RecipeHolder<CraftingRecipe>)null);
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public void beginPlacingRecipe() {
/*  87 */     this.placingRecipe = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void finishPlacingRecipe(ServerLevel paramServerLevel, RecipeHolder<CraftingRecipe> paramRecipeHolder) {
/*  92 */     this.placingRecipe = false;
/*  93 */     slotChangedCraftingGrid(this, paramServerLevel, this.player, this.craftSlots, this.resultSlots, paramRecipeHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player paramPlayer) {
/*  98 */     super.removed(paramPlayer);
/*  99 */     this.access.execute((paramLevel, paramBlockPos) -> clearContainer(paramPlayer, this.craftSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 104 */     return stillValid(this.access, paramPlayer, Blocks.CRAFTING_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/* 109 */     ItemStack itemStack = ItemStack.EMPTY;
/* 110 */     Slot slot = (Slot)this.slots.get(paramInt);
/* 111 */     if (slot != null && slot.hasItem()) {
/* 112 */       ItemStack itemStack1 = slot.getItem();
/* 113 */       itemStack = itemStack1.copy();
/*     */       
/* 115 */       if (paramInt == 0) {
/*     */         
/* 117 */         itemStack1.getItem().onCraftedBy(itemStack1, paramPlayer);
/* 118 */         if (!moveItemStackTo(itemStack1, 10, 46, true)) {
/* 119 */           return ItemStack.EMPTY;
/*     */         }
/* 121 */         slot.onQuickCraft(itemStack1, itemStack);
/* 122 */       } else if (paramInt >= 10 && paramInt < 46) {
/* 123 */         if (!moveItemStackTo(itemStack1, 1, 10, false)) {
/* 124 */           if (paramInt < 37) {
/* 125 */             if (!moveItemStackTo(itemStack1, 37, 46, false)) {
/* 126 */               return ItemStack.EMPTY;
/*     */             }
/*     */           }
/* 129 */           else if (!moveItemStackTo(itemStack1, 10, 37, false)) {
/* 130 */             return ItemStack.EMPTY;
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 135 */       else if (!moveItemStackTo(itemStack1, 10, 46, false)) {
/* 136 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 139 */       if (itemStack1.isEmpty()) {
/* 140 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 142 */         slot.setChanged();
/*     */       } 
/* 144 */       if (itemStack1.getCount() == itemStack.getCount())
/*     */       {
/* 146 */         return ItemStack.EMPTY;
/*     */       }
/* 148 */       slot.onTake(paramPlayer, itemStack1);
/* 149 */       if (paramInt == 0) {
/* 150 */         paramPlayer.drop(itemStack1, false);
/*     */       }
/*     */     } 
/*     */     
/* 154 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack paramItemStack, Slot paramSlot) {
/* 159 */     return (paramSlot.container != this.resultSlots && super.canTakeItemForPickAll(paramItemStack, paramSlot));
/*     */   }
/*     */ 
/*     */   
/*     */   public Slot getResultSlot() {
/* 164 */     return (Slot)this.slots.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Slot> getInputGridSlots() {
/* 169 */     return this.slots.subList(1, 10);
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeBookType getRecipeBookType() {
/* 174 */     return RecipeBookType.CRAFTING;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Player owner() {
/* 179 */     return this.player;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\CraftingMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */