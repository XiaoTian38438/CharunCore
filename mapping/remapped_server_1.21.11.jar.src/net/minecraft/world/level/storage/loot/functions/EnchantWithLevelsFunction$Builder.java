/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<EnchantWithLevelsFunction.Builder>
/*    */ {
/*    */   private final NumberProvider levels;
/* 59 */   private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*    */   
/*    */   public Builder(NumberProvider paramNumberProvider) {
/* 62 */     this.levels = paramNumberProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public Builder fromOptions(HolderSet<Enchantment> paramHolderSet) {
/* 71 */     this.options = Optional.of(paramHolderSet);
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 77 */     return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */