/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EntryGroup extends CompositeEntryBase {
/* 10 */   public static final MapCodec<EntryGroup> CODEC = createCodec(EntryGroup::new);
/*    */   
/*    */   EntryGroup(List<LootPoolEntryContainer> paramList, List<LootItemCondition> paramList1) {
/* 13 */     super(paramList, paramList1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 18 */     return LootPoolEntries.GROUP;
/*    */   }
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> paramList) {
/*    */     ComposableEntryContainer composableEntryContainer1;
/*    */     ComposableEntryContainer composableEntryContainer2;
/* 23 */     switch (paramList.size()) { case 0: 
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 27 */         composableEntryContainer1 = paramList.get(0);
/* 28 */         composableEntryContainer2 = paramList.get(1); }
/*    */     
/*    */     return (paramLootContext, paramConsumer) -> {
/*    */         for (ComposableEntryContainer composableEntryContainer : paramList) {
/*    */           composableEntryContainer.expand(paramLootContext, paramConsumer);
/*    */         }
/*    */         return true;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder>
/*    */   {
/* 45 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... param1VarArgs) {
/* 48 */       for (LootPoolEntryContainer.Builder<?> builder : param1VarArgs) {
/* 49 */         this.entries.add(builder.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 55 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder append(LootPoolEntryContainer.Builder<?> param1Builder) {
/* 60 */       this.entries.add(param1Builder.build());
/* 61 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 66 */       return new EntryGroup((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder list(LootPoolEntryContainer.Builder<?>... paramVarArgs) {
/* 71 */     return new Builder(paramVarArgs);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\EntryGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */