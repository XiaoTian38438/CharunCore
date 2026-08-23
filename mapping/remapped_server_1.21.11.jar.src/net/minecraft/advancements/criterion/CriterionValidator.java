/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*    */ 
/*    */ public class CriterionValidator
/*    */ {
/*    */   private final ProblemReporter reporter;
/*    */   private final HolderGetter.Provider lootData;
/*    */   
/*    */   public CriterionValidator(ProblemReporter paramProblemReporter, HolderGetter.Provider paramProvider) {
/* 17 */     this.reporter = paramProblemReporter;
/* 18 */     this.lootData = paramProvider;
/*    */   }
/*    */   
/*    */   public void validateEntity(Optional<ContextAwarePredicate> paramOptional, String paramString) {
/* 22 */     paramOptional.ifPresent(paramContextAwarePredicate -> validateEntity(paramContextAwarePredicate, paramString));
/*    */   }
/*    */   
/*    */   public void validateEntities(List<ContextAwarePredicate> paramList, String paramString) {
/* 26 */     validate(paramList, LootContextParamSets.ADVANCEMENT_ENTITY, paramString);
/*    */   }
/*    */   
/*    */   public void validateEntity(ContextAwarePredicate paramContextAwarePredicate, String paramString) {
/* 30 */     validate(paramContextAwarePredicate, LootContextParamSets.ADVANCEMENT_ENTITY, paramString);
/*    */   }
/*    */   
/*    */   public void validate(ContextAwarePredicate paramContextAwarePredicate, ContextKeySet paramContextKeySet, String paramString) {
/* 34 */     paramContextAwarePredicate.validate(new ValidationContext(this.reporter.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement(paramString)), paramContextKeySet, this.lootData));
/*    */   }
/*    */   
/*    */   public void validate(List<ContextAwarePredicate> paramList, ContextKeySet paramContextKeySet, String paramString) {
/* 38 */     for (byte b = 0; b < paramList.size(); b++) {
/* 39 */       ContextAwarePredicate contextAwarePredicate = paramList.get(b);
/* 40 */       contextAwarePredicate.validate(new ValidationContext(this.reporter.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement(paramString, b)), paramContextKeySet, this.lootData));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\CriterionValidator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */