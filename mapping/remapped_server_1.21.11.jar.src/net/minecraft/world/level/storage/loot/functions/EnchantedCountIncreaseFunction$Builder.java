/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<EnchantedCountIncreaseFunction.Builder>
/*     */ {
/*     */   private final Holder<Enchantment> enchantment;
/*     */   private final NumberProvider count;
/*  84 */   private int limit = 0;
/*     */   
/*     */   public Builder(Holder<Enchantment> paramHolder, NumberProvider paramNumberProvider) {
/*  87 */     this.enchantment = paramHolder;
/*  88 */     this.count = paramNumberProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Builder getThis() {
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLimit(int paramInt) {
/*  97 */     this.limit = paramInt;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunction build() {
/* 103 */     return new EnchantedCountIncreaseFunction(getConditions(), this.enchantment, this.count, this.limit);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\EnchantedCountIncreaseFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */