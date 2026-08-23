/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class CompoundContainer implements Container {
/*     */   private final Container container1;
/*     */   private final Container container2;
/*     */   
/*     */   public CompoundContainer(Container paramContainer1, Container paramContainer2) {
/*  12 */     this.container1 = paramContainer1;
/*  13 */     this.container2 = paramContainer2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  18 */     return this.container1.getContainerSize() + this.container2.getContainerSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  23 */     return (this.container1.isEmpty() && this.container2.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean contains(Container paramContainer) {
/*  27 */     return (this.container1 == paramContainer || this.container2 == paramContainer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/*  32 */     if (paramInt >= this.container1.getContainerSize()) {
/*  33 */       return this.container2.getItem(paramInt - this.container1.getContainerSize());
/*     */     }
/*  35 */     return this.container1.getItem(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  41 */     if (paramInt1 >= this.container1.getContainerSize()) {
/*  42 */       return this.container2.removeItem(paramInt1 - this.container1.getContainerSize(), paramInt2);
/*     */     }
/*  44 */     return this.container1.removeItem(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/*  50 */     if (paramInt >= this.container1.getContainerSize()) {
/*  51 */       return this.container2.removeItemNoUpdate(paramInt - this.container1.getContainerSize());
/*     */     }
/*  53 */     return this.container1.removeItemNoUpdate(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/*  59 */     if (paramInt >= this.container1.getContainerSize()) {
/*  60 */       this.container2.setItem(paramInt - this.container1.getContainerSize(), paramItemStack);
/*     */     } else {
/*  62 */       this.container1.setItem(paramInt, paramItemStack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/*  68 */     return this.container1.getMaxStackSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  73 */     this.container1.setChanged();
/*  74 */     this.container2.setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  79 */     return (this.container1.stillValid(paramPlayer) && this.container2.stillValid(paramPlayer));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(ContainerUser paramContainerUser) {
/*  84 */     this.container1.startOpen(paramContainerUser);
/*  85 */     this.container2.startOpen(paramContainerUser);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser paramContainerUser) {
/*  90 */     this.container1.stopOpen(paramContainerUser);
/*  91 */     this.container2.stopOpen(paramContainerUser);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int paramInt, ItemStack paramItemStack) {
/*  96 */     if (paramInt >= this.container1.getContainerSize()) {
/*  97 */       return this.container2.canPlaceItem(paramInt - this.container1.getContainerSize(), paramItemStack);
/*     */     }
/*  99 */     return this.container1.canPlaceItem(paramInt, paramItemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 105 */     this.container1.clearContent();
/* 106 */     this.container2.clearContent();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\CompoundContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */