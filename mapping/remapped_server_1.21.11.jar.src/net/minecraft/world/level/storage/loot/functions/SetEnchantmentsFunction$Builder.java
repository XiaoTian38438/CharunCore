/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<SetEnchantmentsFunction.Builder>
/*    */ {
/* 66 */   private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments = ImmutableMap.builder();
/*    */   private final boolean add;
/*    */   
/*    */   public Builder() {
/* 70 */     this(false);
/*    */   }
/*    */   
/*    */   public Builder(boolean paramBoolean) {
/* 74 */     this.add = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEnchantment(Holder<Enchantment> paramHolder, NumberProvider paramNumberProvider) {
/* 83 */     this.enchantments.put(paramHolder, paramNumberProvider);
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 89 */     return new SetEnchantmentsFunction(getConditions(), (Map<Holder<Enchantment>, NumberProvider>)this.enchantments.build(), this.add);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetEnchantmentsFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */