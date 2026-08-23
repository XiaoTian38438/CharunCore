/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
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
/*    */   extends LootItemConditionalFunction.Builder<FilteredFunction.Builder>
/*    */ {
/*    */   private final ItemPredicate itemPredicate;
/* 60 */   private Optional<LootItemFunction> onPass = Optional.empty();
/* 61 */   private Optional<LootItemFunction> onFail = Optional.empty();
/*    */   
/*    */   Builder(ItemPredicate paramItemPredicate) {
/* 64 */     this.itemPredicate = paramItemPredicate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 69 */     return this;
/*    */   }
/*    */   
/*    */   public Builder onPass(Optional<LootItemFunction> paramOptional) {
/* 73 */     this.onPass = paramOptional;
/* 74 */     return this;
/*    */   }
/*    */   
/*    */   public Builder onFail(Optional<LootItemFunction> paramOptional) {
/* 78 */     this.onFail = paramOptional;
/* 79 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 84 */     return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\FilteredFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */