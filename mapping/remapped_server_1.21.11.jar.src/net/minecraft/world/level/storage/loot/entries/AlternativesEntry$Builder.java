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
/*    */   extends LootPoolEntryContainer.Builder<AlternativesEntry.Builder>
/*    */ {
/* 62 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(LootPoolEntryContainer.Builder<?>... paramVarArgs) {
/* 65 */     for (LootPoolEntryContainer.Builder<?> builder : paramVarArgs) {
/* 66 */       this.entries.add(builder.build());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Builder otherwise(LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 77 */     this.entries.add(paramBuilder.build());
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryContainer build() {
/* 83 */     return new AlternativesEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */