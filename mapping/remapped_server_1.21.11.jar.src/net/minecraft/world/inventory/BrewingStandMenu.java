/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ 
/*     */ 
/*     */ public class BrewingStandMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*  23 */   static final Identifier EMPTY_SLOT_FUEL = Identifier.withDefaultNamespace("container/slot/brewing_fuel");
/*  24 */   static final Identifier EMPTY_SLOT_POTION = Identifier.withDefaultNamespace("container/slot/potion");
/*     */   
/*     */   private static final int BOTTLE_SLOT_START = 0;
/*     */   
/*     */   private static final int BOTTLE_SLOT_END = 2;
/*     */   
/*     */   private static final int INGREDIENT_SLOT = 3;
/*     */   
/*     */   private static final int FUEL_SLOT = 4;
/*     */   private static final int SLOT_COUNT = 5;
/*     */   private static final int DATA_COUNT = 2;
/*     */   private static final int INV_SLOT_START = 5;
/*     */   private static final int INV_SLOT_END = 32;
/*     */   private static final int USE_ROW_SLOT_START = 32;
/*     */   private static final int USE_ROW_SLOT_END = 41;
/*     */   private final Container brewingStand;
/*     */   private final ContainerData brewingStandData;
/*     */   private final Slot ingredientSlot;
/*     */   
/*     */   public BrewingStandMenu(int paramInt, Inventory paramInventory) {
/*  44 */     this(paramInt, paramInventory, (Container)new SimpleContainer(5), new SimpleContainerData(2));
/*     */   }
/*     */   
/*     */   public BrewingStandMenu(int paramInt, Inventory paramInventory, Container paramContainer, ContainerData paramContainerData) {
/*  48 */     super(MenuType.BREWING_STAND, paramInt);
/*  49 */     checkContainerSize(paramContainer, 5);
/*  50 */     checkContainerDataCount(paramContainerData, 2);
/*  51 */     this.brewingStand = paramContainer;
/*  52 */     this.brewingStandData = paramContainerData;
/*     */     
/*  54 */     PotionBrewing potionBrewing = paramInventory.player.level().potionBrewing();
/*     */     
/*  56 */     addSlot(new PotionSlot(paramContainer, 0, 56, 51));
/*  57 */     addSlot(new PotionSlot(paramContainer, 1, 79, 58));
/*  58 */     addSlot(new PotionSlot(paramContainer, 2, 102, 51));
/*  59 */     this.ingredientSlot = addSlot(new IngredientsSlot(potionBrewing, paramContainer, 3, 79, 17));
/*  60 */     addSlot(new FuelSlot(paramContainer, 4, 17, 17));
/*     */     
/*  62 */     addDataSlots(paramContainerData);
/*     */     
/*  64 */     addStandardInventorySlots((Container)paramInventory, 8, 84);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  69 */     return this.brewingStand.stillValid(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/*  74 */     ItemStack itemStack = ItemStack.EMPTY;
/*  75 */     Slot slot = (Slot)this.slots.get(paramInt);
/*  76 */     if (slot != null && slot.hasItem()) {
/*  77 */       ItemStack itemStack1 = slot.getItem();
/*  78 */       itemStack = itemStack1.copy();
/*     */       
/*  80 */       if ((paramInt >= 0 && paramInt <= 2) || paramInt == 3 || paramInt == 4) {
/*  81 */         if (!moveItemStackTo(itemStack1, 5, 41, true)) {
/*  82 */           return ItemStack.EMPTY;
/*     */         }
/*  84 */         slot.onQuickCraft(itemStack1, itemStack);
/*  85 */       } else if (FuelSlot.mayPlaceItem(itemStack)) {
/*  86 */         if (moveItemStackTo(itemStack1, 4, 5, false) || (this.ingredientSlot.mayPlace(itemStack1) && !moveItemStackTo(itemStack1, 3, 4, false))) {
/*  87 */           return ItemStack.EMPTY;
/*     */         }
/*  89 */       } else if (this.ingredientSlot.mayPlace(itemStack1)) {
/*  90 */         if (!moveItemStackTo(itemStack1, 3, 4, false)) {
/*  91 */           return ItemStack.EMPTY;
/*     */         }
/*  93 */       } else if (PotionSlot.mayPlaceItem(itemStack)) {
/*  94 */         if (!moveItemStackTo(itemStack1, 0, 3, false)) {
/*  95 */           return ItemStack.EMPTY;
/*     */         }
/*  97 */       } else if (paramInt >= 5 && paramInt < 32) {
/*  98 */         if (!moveItemStackTo(itemStack1, 32, 41, false)) {
/*  99 */           return ItemStack.EMPTY;
/*     */         }
/* 101 */       } else if (paramInt >= 32 && paramInt < 41) {
/* 102 */         if (!moveItemStackTo(itemStack1, 5, 32, false)) {
/* 103 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 106 */       else if (!moveItemStackTo(itemStack1, 5, 41, false)) {
/* 107 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 110 */       if (itemStack1.isEmpty()) {
/* 111 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 113 */         slot.setChanged();
/*     */       } 
/* 115 */       if (itemStack1.getCount() == itemStack.getCount()) {
/* 116 */         return ItemStack.EMPTY;
/*     */       }
/* 118 */       slot.onTake(paramPlayer, itemStack);
/*     */     } 
/*     */     
/* 121 */     return itemStack;
/*     */   }
/*     */   
/*     */   public int getFuel() {
/* 125 */     return this.brewingStandData.get(1);
/*     */   }
/*     */   
/*     */   public int getBrewingTicks() {
/* 129 */     return this.brewingStandData.get(0);
/*     */   }
/*     */   
/*     */   private static class PotionSlot extends Slot {
/*     */     public PotionSlot(Container param1Container, int param1Int1, int param1Int2, int param1Int3) {
/* 134 */       super(param1Container, param1Int1, param1Int2, param1Int3);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack param1ItemStack) {
/* 139 */       return mayPlaceItem(param1ItemStack);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 144 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTake(Player param1Player, ItemStack param1ItemStack) {
/* 149 */       Optional<Holder> optional = ((PotionContents)param1ItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/* 150 */       if (optional.isPresent() && param1Player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)param1Player;
/* 151 */         CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, optional.get()); }
/*     */       
/* 153 */       super.onTake(param1Player, param1ItemStack);
/*     */     }
/*     */     
/*     */     public static boolean mayPlaceItem(ItemStack param1ItemStack) {
/* 157 */       return (param1ItemStack.is(Items.POTION) || param1ItemStack.is(Items.SPLASH_POTION) || param1ItemStack.is(Items.LINGERING_POTION) || param1ItemStack.is(Items.GLASS_BOTTLE));
/*     */     }
/*     */ 
/*     */     
/*     */     public Identifier getNoItemIcon() {
/* 162 */       return BrewingStandMenu.EMPTY_SLOT_POTION;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IngredientsSlot extends Slot {
/*     */     private final PotionBrewing potionBrewing;
/*     */     
/*     */     public IngredientsSlot(PotionBrewing param1PotionBrewing, Container param1Container, int param1Int1, int param1Int2, int param1Int3) {
/* 170 */       super(param1Container, param1Int1, param1Int2, param1Int3);
/* 171 */       this.potionBrewing = param1PotionBrewing;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack param1ItemStack) {
/* 176 */       return this.potionBrewing.isIngredient(param1ItemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FuelSlot extends Slot {
/*     */     public FuelSlot(Container param1Container, int param1Int1, int param1Int2, int param1Int3) {
/* 182 */       super(param1Container, param1Int1, param1Int2, param1Int3);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack param1ItemStack) {
/* 187 */       return mayPlaceItem(param1ItemStack);
/*     */     }
/*     */     
/*     */     public static boolean mayPlaceItem(ItemStack param1ItemStack) {
/* 191 */       return param1ItemStack.is(ItemTags.BREWING_FUEL);
/*     */     }
/*     */ 
/*     */     
/*     */     public Identifier getNoItemIcon() {
/* 196 */       return BrewingStandMenu.EMPTY_SLOT_FUEL;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\BrewingStandMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */