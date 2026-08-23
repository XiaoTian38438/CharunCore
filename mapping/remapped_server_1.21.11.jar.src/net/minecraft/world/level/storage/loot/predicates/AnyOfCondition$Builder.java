/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.List;
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
/*    */   extends CompositeLootItemCondition.Builder
/*    */ {
/*    */   public Builder(LootItemCondition.Builder... paramVarArgs) {
/* 22 */     super(paramVarArgs);
/*    */   }
/*    */ 
/*    */   
/*    */   public Builder or(LootItemCondition.Builder paramBuilder) {
/* 27 */     addTerm(paramBuilder);
/* 28 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected LootItemCondition create(List<LootItemCondition> paramList) {
/* 33 */     return new AnyOfCondition(paramList);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\AnyOfCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */