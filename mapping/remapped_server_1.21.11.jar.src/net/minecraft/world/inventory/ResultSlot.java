/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ResultSlot extends Slot {
/*     */   private final CraftingContainer craftSlots;
/*     */   
/*     */   public ResultSlot(Player paramPlayer, CraftingContainer paramCraftingContainer, Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/*  19 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/*  20 */     this.player = paramPlayer;
/*  21 */     this.craftSlots = paramCraftingContainer;
/*     */   }
/*     */   private final Player player; private int removeCount;
/*     */   
/*     */   public boolean mayPlace(ItemStack paramItemStack) {
/*  26 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack remove(int paramInt) {
/*  31 */     if (hasItem()) {
/*  32 */       this.removeCount += Math.min(paramInt, getItem().getCount());
/*     */     }
/*  34 */     return super.remove(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onQuickCraft(ItemStack paramItemStack, int paramInt) {
/*  39 */     this.removeCount += paramInt;
/*  40 */     checkTakeAchievements(paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onSwapCraft(int paramInt) {
/*  45 */     this.removeCount += paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack paramItemStack) {
/*  50 */     if (this.removeCount > 0) {
/*  51 */       paramItemStack.onCraftedBy(this.player, this.removeCount);
/*     */     }
/*  53 */     Container container = this.container; if (container instanceof RecipeCraftingHolder) { RecipeCraftingHolder recipeCraftingHolder = (RecipeCraftingHolder)container;
/*  54 */       recipeCraftingHolder.awardUsedRecipes(this.player, this.craftSlots.getItems()); }
/*     */     
/*  56 */     this.removeCount = 0;
/*     */   }
/*     */   
/*     */   private static NonNullList<ItemStack> copyAllInputItems(CraftingInput paramCraftingInput) {
/*  60 */     NonNullList<ItemStack> nonNullList = NonNullList.withSize(paramCraftingInput.size(), ItemStack.EMPTY);
/*  61 */     for (byte b = 0; b < nonNullList.size(); b++) {
/*  62 */       nonNullList.set(b, paramCraftingInput.getItem(b));
/*     */     }
/*  64 */     return nonNullList;
/*     */   }
/*     */   
/*     */   private NonNullList<ItemStack> getRemainingItems(CraftingInput paramCraftingInput, Level paramLevel) {
/*  68 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*     */       
/*  70 */       return serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, (RecipeInput)paramCraftingInput, (Level)serverLevel)
/*  71 */         .map(paramRecipeHolder -> ((CraftingRecipe)paramRecipeHolder.value()).getRemainingItems(paramCraftingInput))
/*  72 */         .orElseGet(() -> copyAllInputItems(paramCraftingInput)); }
/*     */ 
/*     */ 
/*     */     
/*  76 */     return CraftingRecipe.defaultCraftingReminder(paramCraftingInput);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTake(Player paramPlayer, ItemStack paramItemStack) {
/*  82 */     checkTakeAchievements(paramItemStack);
/*     */     
/*  84 */     CraftingInput.Positioned positioned = this.craftSlots.asPositionedCraftInput();
/*  85 */     CraftingInput craftingInput = positioned.input();
/*  86 */     int i = positioned.left();
/*  87 */     int j = positioned.top();
/*     */     
/*  89 */     NonNullList<ItemStack> nonNullList = getRemainingItems(craftingInput, paramPlayer.level());
/*     */     
/*  91 */     for (byte b = 0; b < craftingInput.height(); b++) {
/*  92 */       for (byte b1 = 0; b1 < craftingInput.width(); b1++) {
/*  93 */         int k = b1 + i + (b + j) * this.craftSlots.getWidth();
/*  94 */         ItemStack itemStack1 = this.craftSlots.getItem(k);
/*     */         
/*  96 */         ItemStack itemStack2 = (ItemStack)nonNullList.get(b1 + b * craftingInput.width());
/*     */         
/*  98 */         if (!itemStack1.isEmpty()) {
/*  99 */           this.craftSlots.removeItem(k, 1);
/* 100 */           itemStack1 = this.craftSlots.getItem(k);
/*     */         } 
/*     */         
/* 103 */         if (!itemStack2.isEmpty()) {
/* 104 */           if (itemStack1.isEmpty()) {
/*     */             
/* 106 */             this.craftSlots.setItem(k, itemStack2);
/* 107 */           } else if (ItemStack.isSameItemSameComponents(itemStack1, itemStack2)) {
/* 108 */             itemStack2.grow(itemStack1.getCount());
/* 109 */             this.craftSlots.setItem(k, itemStack2);
/* 110 */           } else if (!this.player.getInventory().add(itemStack2)) {
/*     */             
/* 112 */             this.player.drop(itemStack2, false);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFake() {
/* 121 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */