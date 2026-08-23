/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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
/*    */ final class DummyBuilder
/*    */   extends LootItemConditionalFunction.Builder<LootItemConditionalFunction.DummyBuilder>
/*    */ {
/*    */   private final Function<List<LootItemCondition>, LootItemFunction> constructor;
/*    */   
/*    */   public DummyBuilder(Function<List<LootItemCondition>, LootItemFunction> paramFunction) {
/* 77 */     this.constructor = paramFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   protected DummyBuilder getThis() {
/* 82 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 87 */     return this.constructor.apply(getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction$DummyBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */