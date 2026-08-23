/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class AlternativesEntry extends CompositeEntryBase {
/* 14 */   public static final MapCodec<AlternativesEntry> CODEC = createCodec(AlternativesEntry::new);
/*    */   
/* 16 */   public static final ProblemReporter.Problem UNREACHABLE_PROBLEM = new ProblemReporter.Problem()
/*    */     {
/*    */       public String description() {
/* 19 */         return "Unreachable entry!";
/*    */       }
/*    */     };
/*    */   
/*    */   AlternativesEntry(List<LootPoolEntryContainer> paramList, List<LootItemCondition> paramList1) {
/* 24 */     super(paramList, paramList1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 29 */     return LootPoolEntries.ALTERNATIVES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> paramList) {
/* 34 */     switch (paramList.size()) { case 0: case 1: case 2:  }  return (paramLootContext, paramConsumer) -> {
/*    */         for (ComposableEntryContainer composableEntryContainer : paramList) {
/*    */           if (composableEntryContainer.expand(paramLootContext, paramConsumer)) {
/*    */             return true;
/*    */           }
/*    */         } 
/*    */         return false;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 51 */     super.validate(paramValidationContext);
/*    */     
/* 53 */     for (byte b = 0; b < this.children.size() - 1; b++) {
/*    */       
/* 55 */       if (((LootPoolEntryContainer)this.children.get(b)).conditions.isEmpty())
/* 56 */         paramValidationContext.reportProblem(UNREACHABLE_PROBLEM); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder> {
/* 62 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... param1VarArgs) {
/* 65 */       for (LootPoolEntryContainer.Builder<?> builder : param1VarArgs) {
/* 66 */         this.entries.add(builder.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 72 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder otherwise(LootPoolEntryContainer.Builder<?> param1Builder) {
/* 77 */       this.entries.add(param1Builder.build());
/* 78 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 83 */       return new AlternativesEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder alternatives(LootPoolEntryContainer.Builder<?>... paramVarArgs) {
/* 88 */     return new Builder(paramVarArgs);
/*    */   }
/*    */   
/*    */   public static <E> Builder alternatives(Collection<E> paramCollection, Function<E, LootPoolEntryContainer.Builder<?>> paramFunction) {
/* 92 */     Objects.requireNonNull(paramFunction); return new Builder((LootPoolEntryContainer.Builder<?>[])paramCollection.stream().map(paramFunction::apply).toArray(paramInt -> new LootPoolEntryContainer.Builder[paramInt]));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */