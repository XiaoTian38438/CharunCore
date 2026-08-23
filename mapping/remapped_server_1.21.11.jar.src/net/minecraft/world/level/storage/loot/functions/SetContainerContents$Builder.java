/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
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
/*    */   extends LootItemConditionalFunction.Builder<SetContainerContents.Builder>
/*    */ {
/* 63 */   private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   public Builder(ContainerComponentManipulator<?> paramContainerComponentManipulator) {
/* 67 */     this.component = paramContainerComponentManipulator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEntry(LootPoolEntryContainer.Builder<?> paramBuilder) {
/* 76 */     this.entries.add(paramBuilder.build());
/* 77 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 82 */     return new SetContainerContents(getConditions(), this.component, (List<LootPoolEntryContainer>)this.entries.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */