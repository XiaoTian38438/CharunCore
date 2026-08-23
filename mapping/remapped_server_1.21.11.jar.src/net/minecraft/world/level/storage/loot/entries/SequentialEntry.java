/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SequentialEntry extends CompositeEntryBase {
/* 10 */   public static final MapCodec<SequentialEntry> CODEC = createCodec(SequentialEntry::new);
/*    */   
/*    */   SequentialEntry(List<LootPoolEntryContainer> paramList, List<LootItemCondition> paramList1) {
/* 13 */     super(paramList, paramList1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 18 */     return LootPoolEntries.SEQUENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> paramList) {
/* 23 */     switch (paramList.size()) { case 0: case 1: case 2:  }  return (paramLootContext, paramConsumer) -> {
/*    */         for (ComposableEntryContainer composableEntryContainer : paramList) {
/*    */           if (!composableEntryContainer.expand(paramLootContext, paramConsumer)) {
/*    */             return false;
/*    */           }
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder>
/*    */   {
/* 39 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... param1VarArgs) {
/* 42 */       for (LootPoolEntryContainer.Builder<?> builder : param1VarArgs) {
/* 43 */         this.entries.add(builder.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 49 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder then(LootPoolEntryContainer.Builder<?> param1Builder) {
/* 54 */       this.entries.add(param1Builder.build());
/* 55 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 60 */       return new SequentialEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder sequential(LootPoolEntryContainer.Builder<?>... paramVarArgs) {
/* 65 */     return new Builder(paramVarArgs);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\SequentialEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */