/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.DispenserMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class DispenserBlockEntity extends RandomizableContainerBlockEntity {
/*     */   public static final int CONTAINER_SIZE = 9;
/*  18 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.dispenser");
/*     */   
/*  20 */   private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
/*     */   
/*     */   protected DispenserBlockEntity(BlockEntityType<?> paramBlockEntityType, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  23 */     super(paramBlockEntityType, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public DispenserBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  27 */     this(BlockEntityType.DISPENSER, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  32 */     return 9;
/*     */   }
/*     */   
/*     */   public int getRandomSlot(RandomSource paramRandomSource) {
/*  36 */     unpackLootTable(null);
/*  37 */     byte b = -1;
/*  38 */     byte b1 = 1;
/*     */     
/*  40 */     for (byte b2 = 0; b2 < this.items.size(); b2++) {
/*  41 */       if (!((ItemStack)this.items.get(b2)).isEmpty() && paramRandomSource.nextInt(b1++) == 0) {
/*  42 */         b = b2;
/*     */       }
/*     */     } 
/*     */     
/*  46 */     return b;
/*     */   }
/*     */   
/*     */   public ItemStack insertItem(ItemStack paramItemStack) {
/*  50 */     int i = getMaxStackSize(paramItemStack);
/*  51 */     for (byte b = 0; b < this.items.size(); b++) {
/*  52 */       ItemStack itemStack = (ItemStack)this.items.get(b);
/*  53 */       if (itemStack.isEmpty() || ItemStack.isSameItemSameComponents(paramItemStack, itemStack)) {
/*     */ 
/*     */         
/*  56 */         int j = Math.min(paramItemStack.getCount(), i - itemStack.getCount());
/*  57 */         if (j > 0) {
/*  58 */           if (itemStack.isEmpty()) {
/*  59 */             setItem(b, paramItemStack.split(j));
/*     */           } else {
/*  61 */             paramItemStack.shrink(j);
/*  62 */             itemStack.grow(j);
/*     */           } 
/*     */         }
/*  65 */         if (paramItemStack.isEmpty())
/*     */           break; 
/*     */       } 
/*     */     } 
/*  69 */     return paramItemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/*  74 */     return DEFAULT_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  79 */     super.loadAdditional(paramValueInput);
/*     */     
/*  81 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  82 */     if (!tryLoadLootTable(paramValueInput)) {
/*  83 */       ContainerHelper.loadAllItems(paramValueInput, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  89 */     super.saveAdditional(paramValueOutput);
/*     */     
/*  91 */     if (!trySaveLootTable(paramValueOutput)) {
/*  92 */       ContainerHelper.saveAllItems(paramValueOutput, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/*  98 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> paramNonNullList) {
/* 103 */     this.items = paramNonNullList;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory) {
/* 108 */     return (AbstractContainerMenu)new DispenserMenu(paramInt, paramInventory, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\DispenserBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */