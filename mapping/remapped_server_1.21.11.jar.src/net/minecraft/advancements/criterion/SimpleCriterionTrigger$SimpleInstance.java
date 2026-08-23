/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SimpleInstance
/*    */   extends CriterionTriggerInstance
/*    */ {
/*    */   default void validate(CriterionValidator paramCriterionValidator) {
/* 79 */     paramCriterionValidator.validateEntity(player(), "player");
/*    */   }
/*    */   
/*    */   Optional<ContextAwarePredicate> player();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\SimpleCriterionTrigger$SimpleInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */