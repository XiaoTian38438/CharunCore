/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
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
/*    */ public abstract class Builder
/*    */   implements LootItemCondition.Builder
/*    */ {
/* 50 */   private final ImmutableList.Builder<LootItemCondition> terms = ImmutableList.builder();
/*    */   
/*    */   protected Builder(LootItemCondition.Builder... paramVarArgs) {
/* 53 */     for (LootItemCondition.Builder builder : paramVarArgs) {
/* 54 */       this.terms.add(builder.build());
/*    */     }
/*    */   }
/*    */   
/*    */   public void addTerm(LootItemCondition.Builder paramBuilder) {
/* 59 */     this.terms.add(paramBuilder.build());
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemCondition build() {
/* 64 */     return create((List<LootItemCondition>)this.terms.build());
/*    */   }
/*    */   
/*    */   protected abstract LootItemCondition create(List<LootItemCondition> paramList);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\CompositeLootItemCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */