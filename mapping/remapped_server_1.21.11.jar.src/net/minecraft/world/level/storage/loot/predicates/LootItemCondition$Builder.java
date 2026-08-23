/*    */ package net.minecraft.world.level.storage.loot.predicates;
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
/*    */ @FunctionalInterface
/*    */ public interface Builder
/*    */ {
/*    */   LootItemCondition build();
/*    */   
/*    */   default Builder invert() {
/* 26 */     return InvertedLootItemCondition.invert(this);
/*    */   }
/*    */   
/*    */   default AnyOfCondition.Builder or(Builder paramBuilder) {
/* 30 */     return AnyOfCondition.anyOf(new Builder[] { this, paramBuilder });
/*    */   }
/*    */   
/*    */   default AllOfCondition.Builder and(Builder paramBuilder) {
/* 34 */     return AllOfCondition.allOf(new Builder[] { this, paramBuilder });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */