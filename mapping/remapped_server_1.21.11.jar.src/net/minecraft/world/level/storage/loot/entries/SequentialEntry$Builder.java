/*    */ package net.minecraft.world.level.storage.loot.entries;
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
/*    */ public class Builder
/*    */   extends LootPoolEntryContainer.Builder<SequentialEntry.Builder>
/*    */ {
/* 39 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(LootPoolEntryContainer.Builder<?>... paramVarArgs) {
/* 42 */     for (LootPoolEntryContainer.Builder<?> builder : paramVarArgs) {
/* 43 */       this.entries.add(builder.build());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 49 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Builder then(LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 54 */     this.entries.add(paramBuilder.build());
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryContainer build() {
/* 60 */     return new SequentialEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\SequentialEntry$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */