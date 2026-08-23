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
/* 28 */     super(paramVarArgs);
/*    */   }
/*    */ 
/*    */   
/*    */   public Builder and(LootItemCondition.Builder paramBuilder) {
/* 33 */     addTerm(paramBuilder);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected LootItemCondition create(List<LootItemCondition> paramList) {
/* 39 */     return new AllOfCondition(paramList);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\AllOfCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */