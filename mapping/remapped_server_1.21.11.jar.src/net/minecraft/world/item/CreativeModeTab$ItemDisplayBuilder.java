/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ItemDisplayBuilder
/*     */   implements CreativeModeTab.Output
/*     */ {
/* 218 */   public final Collection<ItemStack> tabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/* 219 */   public final Set<ItemStack> searchTabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */   private final CreativeModeTab tab;
/*     */   private final FeatureFlagSet featureFlagSet;
/*     */   
/*     */   public ItemDisplayBuilder(CreativeModeTab paramCreativeModeTab, FeatureFlagSet paramFeatureFlagSet) {
/* 224 */     this.tab = paramCreativeModeTab;
/* 225 */     this.featureFlagSet = paramFeatureFlagSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(ItemStack paramItemStack, CreativeModeTab.TabVisibility paramTabVisibility) {
/* 230 */     if (paramItemStack.getCount() != 1) {
/* 231 */       throw new IllegalArgumentException("Stack size must be exactly 1");
/*     */     }
/*     */ 
/*     */     
/* 235 */     boolean bool = (this.tabContents.contains(paramItemStack) && paramTabVisibility != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY) ? true : false;
/*     */     
/* 237 */     if (bool) {
/* 238 */       throw new IllegalStateException("Accidentally adding the same item stack twice " + paramItemStack
/* 239 */           .getDisplayName().getString() + " to a Creative Mode Tab: " + this.tab
/*     */           
/* 241 */           .getDisplayName().getString());
/*     */     }
/*     */     
/* 244 */     if (paramItemStack.getItem().isEnabled(this.featureFlagSet))
/* 245 */       switch (paramTabVisibility.ordinal()) {
/*     */         case 0:
/* 247 */           this.tabContents.add(paramItemStack);
/* 248 */           this.searchTabContents.add(paramItemStack); break;
/*     */         case 1:
/* 250 */           this.tabContents.add(paramItemStack); break;
/* 251 */         case 2: this.searchTabContents.add(paramItemStack);
/*     */           break;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\CreativeModeTab$ItemDisplayBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */