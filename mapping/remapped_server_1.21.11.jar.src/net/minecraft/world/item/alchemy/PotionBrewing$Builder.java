/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 226 */   private final List<Ingredient> containers = new ArrayList<>();
/* 227 */   private final List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>();
/* 228 */   private final List<PotionBrewing.Mix<Item>> containerMixes = new ArrayList<>();
/*     */   
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   
/*     */   public Builder(FeatureFlagSet paramFeatureFlagSet) {
/* 233 */     this.enabledFeatures = paramFeatureFlagSet;
/*     */   }
/*     */   
/*     */   private static void expectPotion(Item paramItem) {
/* 237 */     if (!(paramItem instanceof net.minecraft.world.item.PotionItem)) {
/* 238 */       throw new IllegalArgumentException("Expected a potion, got: " + String.valueOf(BuiltInRegistries.ITEM.getKey(paramItem)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addContainerRecipe(Item paramItem1, Item paramItem2, Item paramItem3) {
/* 243 */     if (!paramItem1.isEnabled(this.enabledFeatures) || !paramItem2.isEnabled(this.enabledFeatures) || !paramItem3.isEnabled(this.enabledFeatures)) {
/*     */       return;
/*     */     }
/*     */     
/* 247 */     expectPotion(paramItem1);
/* 248 */     expectPotion(paramItem3);
/* 249 */     this.containerMixes.add(new PotionBrewing.Mix<>((Holder<Item>)paramItem1.builtInRegistryHolder(), Ingredient.of((ItemLike)paramItem2), (Holder<Item>)paramItem3.builtInRegistryHolder()));
/*     */   }
/*     */   
/*     */   public void addContainer(Item paramItem) {
/* 253 */     if (!paramItem.isEnabled(this.enabledFeatures)) {
/*     */       return;
/*     */     }
/* 256 */     expectPotion(paramItem);
/* 257 */     this.containers.add(Ingredient.of((ItemLike)paramItem));
/*     */   }
/*     */   
/*     */   public void addMix(Holder<Potion> paramHolder1, Item paramItem, Holder<Potion> paramHolder2) {
/* 261 */     if (((Potion)paramHolder1.value()).isEnabled(this.enabledFeatures) && paramItem.isEnabled(this.enabledFeatures) && ((Potion)paramHolder2.value()).isEnabled(this.enabledFeatures)) {
/* 262 */       this.potionMixes.add(new PotionBrewing.Mix<>(paramHolder1, Ingredient.of((ItemLike)paramItem), paramHolder2));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addStartMix(Item paramItem, Holder<Potion> paramHolder) {
/* 267 */     if (((Potion)paramHolder.value()).isEnabled(this.enabledFeatures)) {
/* 268 */       addMix(Potions.WATER, paramItem, Potions.MUNDANE);
/* 269 */       addMix(Potions.AWKWARD, paramItem, paramHolder);
/*     */     } 
/*     */   }
/*     */   
/*     */   public PotionBrewing build() {
/* 274 */     return new PotionBrewing(
/* 275 */         List.copyOf(this.containers), 
/* 276 */         List.copyOf(this.potionMixes), 
/* 277 */         List.copyOf(this.containerMixes));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\alchemy\PotionBrewing$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */