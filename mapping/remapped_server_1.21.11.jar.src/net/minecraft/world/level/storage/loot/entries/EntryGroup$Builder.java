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
/*    */ public class Builder
/*    */   extends LootPoolEntryContainer.Builder<EntryGroup.Builder>
/*    */ {
/* 45 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   
/*    */   public Builder(LootPoolEntryContainer.Builder<?>... paramVarArgs) {
/* 48 */     for (LootPoolEntryContainer.Builder<?> builder : paramVarArgs) {
/* 49 */       this.entries.add(builder.build());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Builder append(LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 60 */     this.entries.add(paramBuilder.build());
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryContainer build() {
/* 66 */     return new EntryGroup((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\EntryGroup$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */