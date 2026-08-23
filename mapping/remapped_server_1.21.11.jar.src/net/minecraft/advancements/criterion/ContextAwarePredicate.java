/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ContextAwarePredicate {
/*    */   static {
/* 15 */     CODEC = LootItemCondition.DIRECT_CODEC.listOf().xmap(ContextAwarePredicate::new, paramContextAwarePredicate -> paramContextAwarePredicate.conditions);
/*    */   }
/*    */   
/*    */   public static final Codec<ContextAwarePredicate> CODEC;
/*    */   
/*    */   ContextAwarePredicate(List<LootItemCondition> paramList) {
/* 21 */     this.conditions = paramList;
/* 22 */     this.compositePredicates = Util.allOf(paramList);
/*    */   }
/*    */   private final List<LootItemCondition> conditions; private final Predicate<LootContext> compositePredicates;
/*    */   public static ContextAwarePredicate create(LootItemCondition... paramVarArgs) {
/* 26 */     return new ContextAwarePredicate(List.of(paramVarArgs));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext paramLootContext) {
/* 30 */     return this.compositePredicates.test(paramLootContext);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 34 */     for (byte b = 0; b < this.conditions.size(); b++) {
/* 35 */       LootItemCondition lootItemCondition = this.conditions.get(b);
/* 36 */       lootItemCondition.validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedPathElement(b)));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\ContextAwarePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */