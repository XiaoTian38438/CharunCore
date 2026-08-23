/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.ChiseledBookShelfBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChiseledBookShelfBlockEntity extends BlockEntity implements ListBackedContainer {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MAX_BOOKS_IN_STORAGE = 6;
/*     */   private static final int DEFAULT_LAST_INTERACTED_SLOT = -1;
/*  33 */   private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
/*     */   
/*  35 */   private int lastInteractedSlot = -1;
/*     */   
/*     */   public ChiseledBookShelfBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  38 */     super(BlockEntityType.CHISELED_BOOKSHELF, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   private void updateState(int paramInt) {
/*  42 */     if (paramInt < 0 || paramInt >= 6) {
/*  43 */       LOGGER.error("Expected slot 0-5, got {}", Integer.valueOf(paramInt));
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     this.lastInteractedSlot = paramInt;
/*  48 */     BlockState blockState = getBlockState();
/*  49 */     for (byte b = 0; b < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); b++) {
/*  50 */       boolean bool = !getItem(b).isEmpty() ? true : false;
/*  51 */       BooleanProperty booleanProperty = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(b);
/*     */       
/*  53 */       blockState = (BlockState)blockState.setValue((Property)booleanProperty, Boolean.valueOf(bool));
/*     */     } 
/*     */     
/*  56 */     ((Level)Objects.<Level>requireNonNull(this.level)).setBlock(this.worldPosition, blockState, 3);
/*     */     
/*  58 */     this.level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(blockState));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  63 */     super.loadAdditional(paramValueInput);
/*     */     
/*  65 */     this.items.clear();
/*  66 */     ContainerHelper.loadAllItems(paramValueInput, this.items);
/*  67 */     this.lastInteractedSlot = paramValueInput.getIntOr("last_interacted_slot", -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  72 */     super.saveAdditional(paramValueOutput);
/*     */     
/*  74 */     ContainerHelper.saveAllItems(paramValueOutput, this.items, true);
/*  75 */     paramValueOutput.putInt("last_interacted_slot", this.lastInteractedSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/*  80 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsItemType(ItemStack paramItemStack) {
/*  85 */     return paramItemStack.is(ItemTags.BOOKSHELF_BOOKS);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  90 */     ItemStack itemStack = Objects.<ItemStack>requireNonNullElse((ItemStack)getItems().get(paramInt1), ItemStack.EMPTY);
/*  91 */     getItems().set(paramInt1, ItemStack.EMPTY);
/*     */     
/*  93 */     if (!itemStack.isEmpty()) {
/*  94 */       updateState(paramInt1);
/*     */     }
/*     */     
/*  97 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/* 102 */     if (acceptsItemType(paramItemStack)) {
/* 103 */       getItems().set(paramInt, paramItemStack);
/* 104 */       updateState(paramInt);
/* 105 */     } else if (paramItemStack.isEmpty()) {
/* 106 */       removeItem(paramInt, getMaxStackSize());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(Container paramContainer, int paramInt, ItemStack paramItemStack) {
/* 112 */     return paramContainer.hasAnyMatching(paramItemStack2 -> paramItemStack2.isEmpty() ? true : (
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 117 */         (ItemStack.isSameItemSameComponents(paramItemStack1, paramItemStack2) && paramItemStack2.getCount() + paramItemStack1.getCount() <= paramContainer.getMaxStackSize(paramItemStack2))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 123 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 128 */     return Container.stillValidBlockEntity(this, paramPlayer);
/*     */   }
/*     */   
/*     */   public int getLastInteractedSlot() {
/* 132 */     return this.lastInteractedSlot;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 137 */     super.applyImplicitComponents(paramDataComponentGetter);
/* 138 */     ((ItemContainerContents)paramDataComponentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 143 */     super.collectImplicitComponents(paramBuilder);
/* 144 */     paramBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems((List)this.items));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 149 */     paramValueOutput.discard("Items");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\ChiseledBookShelfBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */