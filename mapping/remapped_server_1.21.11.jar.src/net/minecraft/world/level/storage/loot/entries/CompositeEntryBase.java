/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class CompositeEntryBase extends LootPoolEntryContainer {
/* 14 */   public static final ProblemReporter.Problem NO_CHILDREN_PROBLEM = new ProblemReporter.Problem()
/*    */     {
/*    */       public String description() {
/* 17 */         return "Empty children list";
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   protected final List<LootPoolEntryContainer> children;
/*    */   
/*    */   protected CompositeEntryBase(List<LootPoolEntryContainer> paramList, List<LootItemCondition> paramList1) {
/* 25 */     super(paramList1);
/* 26 */     this.children = paramList;
/* 27 */     this.composedChildren = compose((List)paramList);
/*    */   } private final ComposableEntryContainer composedChildren; @FunctionalInterface
/*    */   public static interface CompositeEntryConstructor<T extends CompositeEntryBase> {
/*    */     T create(List<LootPoolEntryContainer> param1List, List<LootItemCondition> param1List1); }
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 32 */     super.validate(paramValidationContext);
/*    */     
/* 34 */     if (this.children.isEmpty()) {
/* 35 */       paramValidationContext.reportProblem(NO_CHILDREN_PROBLEM);
/*    */     }
/*    */     
/* 38 */     for (byte b = 0; b < this.children.size(); b++) {
/* 39 */       ((LootPoolEntryContainer)this.children.get(b)).validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("children", b)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract ComposableEntryContainer compose(List<? extends ComposableEntryContainer> paramList);
/*    */   
/*    */   public final boolean expand(LootContext paramLootContext, Consumer<LootPoolEntry> paramConsumer) {
/* 47 */     if (!canRun(paramLootContext)) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     return this.composedChildren.expand(paramLootContext, paramConsumer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends CompositeEntryBase> MapCodec<T> createCodec(CompositeEntryConstructor<T> paramCompositeEntryConstructor) {
/* 60 */     return RecordCodecBuilder.mapCodec(paramInstance -> {
/*    */           Objects.requireNonNull(paramCompositeEntryConstructor);
/*    */           return paramInstance.group((App)LootPoolEntries.CODEC.listOf().optionalFieldOf("children", List.of()).forGetter(())).and(commonFields(paramInstance).t1()).apply((Applicative)paramInstance, paramCompositeEntryConstructor::create);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\CompositeEntryBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */