/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<EnchantRandomlyFunction.Builder>
/*     */ {
/*  82 */   private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */   
/*     */   private boolean onlyCompatible = true;
/*     */   
/*     */   protected Builder getThis() {
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEnchantment(Holder<Enchantment> paramHolder) {
/*  91 */     this.options = Optional.of(HolderSet.direct(new Holder[] { paramHolder }));
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOneOf(HolderSet<Enchantment> paramHolderSet) {
/*  96 */     this.options = Optional.of(paramHolderSet);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public Builder allowingIncompatibleEnchantments() {
/* 101 */     this.onlyCompatible = false;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunction build() {
/* 107 */     return new EnchantRandomlyFunction(getConditions(), this.options, this.onlyCompatible);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */