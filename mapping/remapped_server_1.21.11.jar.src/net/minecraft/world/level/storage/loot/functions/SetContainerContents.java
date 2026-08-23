/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerContents extends LootItemConditionalFunction {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(()), (App)LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter(()))).apply((Applicative)paramInstance, SetContainerContents::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetContainerContents> CODEC;
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   private final List<LootPoolEntryContainer> entries;
/*    */   
/*    */   SetContainerContents(List<LootItemCondition> paramList, ContainerComponentManipulator<?> paramContainerComponentManipulator, List<LootPoolEntryContainer> paramList1) {
/* 30 */     super(paramList);
/* 31 */     this.component = paramContainerComponentManipulator;
/* 32 */     this.entries = List.copyOf(paramList1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetContainerContents> getType() {
/* 37 */     return LootItemFunctions.SET_CONTENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 42 */     if (paramItemStack.isEmpty()) {
/* 43 */       return paramItemStack;
/*    */     }
/*    */     
/* 46 */     Stream.Builder<?> builder = Stream.builder();
/* 47 */     this.entries.forEach(paramLootPoolEntryContainer -> paramLootPoolEntryContainer.expand(paramLootContext, ()));
/* 48 */     this.component.setContents(paramItemStack, builder.build());
/*    */     
/* 50 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 55 */     super.validate(paramValidationContext);
/*    */     
/* 57 */     for (byte b = 0; b < this.entries.size(); b++)
/* 58 */       ((LootPoolEntryContainer)this.entries.get(b)).validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("entries", b))); 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootItemConditionalFunction.Builder<Builder> {
/* 63 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     private final ContainerComponentManipulator<?> component;
/*    */     
/*    */     public Builder(ContainerComponentManipulator<?> param1ContainerComponentManipulator) {
/* 67 */       this.component = param1ContainerComponentManipulator;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 72 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEntry(LootPoolEntryContainer.Builder<?> param1Builder) {
/* 76 */       this.entries.add(param1Builder.build());
/* 77 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 82 */       return new SetContainerContents(getConditions(), this.component, (List<LootPoolEntryContainer>)this.entries.build());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setContents(ContainerComponentManipulator<?> paramContainerComponentManipulator) {
/* 87 */     return new Builder(paramContainerComponentManipulator);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */