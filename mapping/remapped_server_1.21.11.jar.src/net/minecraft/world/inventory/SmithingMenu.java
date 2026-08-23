/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.RecipeAccess;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SmithingRecipe;
/*     */ import net.minecraft.world.item.crafting.SmithingRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SmithingMenu extends ItemCombinerMenu {
/*     */   public static final int TEMPLATE_SLOT = 0;
/*     */   public static final int BASE_SLOT = 1;
/*     */   public static final int ADDITIONAL_SLOT = 2;
/*     */   public static final int RESULT_SLOT = 3;
/*     */   public static final int TEMPLATE_SLOT_X_PLACEMENT = 8;
/*     */   public static final int BASE_SLOT_X_PLACEMENT = 26;
/*     */   public static final int ADDITIONAL_SLOT_X_PLACEMENT = 44;
/*     */   private static final int RESULT_SLOT_X_PLACEMENT = 98;
/*     */   public static final int SLOT_Y_PLACEMENT = 48;
/*     */   private final Level level;
/*     */   private final RecipePropertySet baseItemTest;
/*     */   private final RecipePropertySet templateItemTest;
/*     */   private final RecipePropertySet additionItemTest;
/*  38 */   private final DataSlot hasRecipeError = DataSlot.standalone();
/*     */   
/*     */   public SmithingMenu(int paramInt, Inventory paramInventory) {
/*  41 */     this(paramInt, paramInventory, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public SmithingMenu(int paramInt, Inventory paramInventory, ContainerLevelAccess paramContainerLevelAccess) {
/*  45 */     this(paramInt, paramInventory, paramContainerLevelAccess, paramInventory.player.level());
/*     */   }
/*     */   
/*     */   private SmithingMenu(int paramInt, Inventory paramInventory, ContainerLevelAccess paramContainerLevelAccess, Level paramLevel) {
/*  49 */     super(MenuType.SMITHING, paramInt, paramInventory, paramContainerLevelAccess, createInputSlotDefinitions(paramLevel.recipeAccess()));
/*  50 */     this.level = paramLevel;
/*     */     
/*  52 */     this.baseItemTest = paramLevel.recipeAccess().propertySet(RecipePropertySet.SMITHING_BASE);
/*  53 */     this.templateItemTest = paramLevel.recipeAccess().propertySet(RecipePropertySet.SMITHING_TEMPLATE);
/*  54 */     this.additionItemTest = paramLevel.recipeAccess().propertySet(RecipePropertySet.SMITHING_ADDITION);
/*     */     
/*  56 */     addDataSlot(this.hasRecipeError).set(0);
/*     */   }
/*     */   
/*     */   private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions(RecipeAccess paramRecipeAccess) {
/*  60 */     RecipePropertySet recipePropertySet1 = paramRecipeAccess.propertySet(RecipePropertySet.SMITHING_BASE);
/*  61 */     RecipePropertySet recipePropertySet2 = paramRecipeAccess.propertySet(RecipePropertySet.SMITHING_TEMPLATE);
/*  62 */     RecipePropertySet recipePropertySet3 = paramRecipeAccess.propertySet(RecipePropertySet.SMITHING_ADDITION);
/*     */ 
/*     */     
/*  65 */     Objects.requireNonNull(recipePropertySet2);
/*  66 */     Objects.requireNonNull(recipePropertySet1);
/*  67 */     Objects.requireNonNull(recipePropertySet3); return ItemCombinerMenuSlotDefinition.create().withSlot(0, 8, 48, recipePropertySet2::test).withSlot(1, 26, 48, recipePropertySet1::test).withSlot(2, 44, 48, recipePropertySet3::test)
/*  68 */       .withResultSlot(3, 98, 48)
/*  69 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidBlock(BlockState paramBlockState) {
/*  74 */     return paramBlockState.is(Blocks.SMITHING_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTake(Player paramPlayer, ItemStack paramItemStack) {
/*  79 */     paramItemStack.onCraftedBy(paramPlayer, paramItemStack.getCount());
/*  80 */     this.resultSlots.awardUsedRecipes(paramPlayer, getRelevantItems());
/*     */ 
/*     */     
/*  83 */     shrinkStackInSlot(0);
/*  84 */     shrinkStackInSlot(1);
/*  85 */     shrinkStackInSlot(2);
/*     */     
/*  87 */     this.access.execute((paramLevel, paramBlockPos) -> paramLevel.levelEvent(1044, paramBlockPos, 0));
/*     */   }
/*     */   
/*     */   private List<ItemStack> getRelevantItems() {
/*  91 */     return List.of(this.inputSlots
/*  92 */         .getItem(0), this.inputSlots
/*  93 */         .getItem(1), this.inputSlots
/*  94 */         .getItem(2));
/*     */   }
/*     */ 
/*     */   
/*     */   private SmithingRecipeInput createRecipeInput() {
/*  99 */     return new SmithingRecipeInput(this.inputSlots
/* 100 */         .getItem(0), this.inputSlots
/* 101 */         .getItem(1), this.inputSlots
/* 102 */         .getItem(2));
/*     */   }
/*     */ 
/*     */   
/*     */   private void shrinkStackInSlot(int paramInt) {
/* 107 */     ItemStack itemStack = this.inputSlots.getItem(paramInt);
/* 108 */     if (!itemStack.isEmpty()) {
/* 109 */       itemStack.shrink(1);
/* 110 */       this.inputSlots.setItem(paramInt, itemStack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container paramContainer) {
/* 116 */     super.slotsChanged(paramContainer);
/*     */     
/* 118 */     if (this.level instanceof ServerLevel) {
/*     */ 
/*     */ 
/*     */       
/* 122 */       boolean bool = (getSlot(0).hasItem() && getSlot(1).hasItem() && getSlot(2).hasItem() && !getSlot(getResultSlot()).hasItem()) ? true : false;
/* 123 */       this.hasRecipeError.set(bool ? 1 : 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createResult() {
/*     */     Optional<?> optional;
/* 129 */     SmithingRecipeInput smithingRecipeInput = createRecipeInput();
/*     */ 
/*     */     
/* 132 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 133 */       optional = serverLevel.recipeAccess().getRecipeFor(RecipeType.SMITHING, (RecipeInput)smithingRecipeInput, (Level)serverLevel); }
/*     */     
/*     */     else
/*     */     
/* 137 */     { optional = Optional.empty(); }
/*     */ 
/*     */     
/* 140 */     optional.ifPresentOrElse(paramRecipeHolder -> {
/*     */           ItemStack itemStack = ((SmithingRecipe)paramRecipeHolder.value()).assemble((RecipeInput)paramSmithingRecipeInput, (HolderLookup.Provider)this.level.registryAccess());
/*     */           this.resultSlots.setRecipeUsed(paramRecipeHolder);
/*     */           this.resultSlots.setItem(0, itemStack);
/*     */         }() -> {
/*     */           this.resultSlots.setRecipeUsed((RecipeHolder<?>)null);
/*     */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack paramItemStack, Slot paramSlot) {
/* 155 */     return (paramSlot.container != this.resultSlots && super.canTakeItemForPickAll(paramItemStack, paramSlot));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMoveIntoInputSlots(ItemStack paramItemStack) {
/* 160 */     if (this.templateItemTest.test(paramItemStack) && !getSlot(0).hasItem()) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (this.baseItemTest.test(paramItemStack) && !getSlot(1).hasItem()) {
/* 164 */       return true;
/*     */     }
/* 166 */     if (this.additionItemTest.test(paramItemStack) && !getSlot(2).hasItem()) {
/* 167 */       return true;
/*     */     }
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasRecipeError() {
/* 173 */     return (this.hasRecipeError.get() > 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\SmithingMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */