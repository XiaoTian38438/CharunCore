/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.ItemLike;
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
/*     */ public class Builder
/*     */ {
/*     */   private final HolderLookup<Item> items;
/*     */   private final FeatureFlagSet enabledFeatures;
/* 123 */   private final Object2IntSortedMap<Item> values = (Object2IntSortedMap<Item>)new Object2IntLinkedOpenHashMap();
/*     */   
/*     */   public Builder(HolderLookup.Provider paramProvider, FeatureFlagSet paramFeatureFlagSet) {
/* 126 */     this.items = (HolderLookup<Item>)paramProvider.lookupOrThrow(Registries.ITEM);
/* 127 */     this.enabledFeatures = paramFeatureFlagSet;
/*     */   }
/*     */   
/*     */   public FuelValues build() {
/* 131 */     return new FuelValues(this.values);
/*     */   }
/*     */   
/*     */   public Builder remove(TagKey<Item> paramTagKey) {
/* 135 */     this.values.keySet().removeIf(paramItem -> paramItem.builtInRegistryHolder().is(paramTagKey));
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(TagKey<Item> paramTagKey, int paramInt) {
/* 140 */     this.items.get(paramTagKey).ifPresent(paramNamed -> {
/*     */           for (Holder holder : paramNamed) {
/*     */             putInternal(paramInt, (Item)holder.value());
/*     */           }
/*     */         });
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder add(ItemLike paramItemLike, int paramInt) {
/* 149 */     Item item = paramItemLike.asItem();
/* 150 */     putInternal(paramInt, item);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   private void putInternal(int paramInt, Item paramItem) {
/* 155 */     if (paramItem.isEnabled(this.enabledFeatures))
/* 156 */       this.values.put(paramItem, paramInt); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\FuelValues$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */