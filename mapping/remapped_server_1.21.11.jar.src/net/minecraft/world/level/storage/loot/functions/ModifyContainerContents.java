/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ModifyContainerContents extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.fieldOf("modifier").forGetter(()))).apply((Applicative)paramInstance, ModifyContainerContents::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ModifyContainerContents> CODEC;
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   private final LootItemFunction modifier;
/*    */   
/*    */   private ModifyContainerContents(List<LootItemCondition> paramList, ContainerComponentManipulator<?> paramContainerComponentManipulator, LootItemFunction paramLootItemFunction) {
/* 25 */     super(paramList);
/* 26 */     this.component = paramContainerComponentManipulator;
/* 27 */     this.modifier = paramLootItemFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<ModifyContainerContents> getType() {
/* 32 */     return LootItemFunctions.MODIFY_CONTENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 37 */     if (paramItemStack.isEmpty()) {
/* 38 */       return paramItemStack;
/*    */     }
/*    */     
/* 41 */     this.component.modifyItems(paramItemStack, paramItemStack -> this.modifier.apply(paramItemStack, paramLootContext));
/*    */     
/* 43 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 48 */     super.validate(paramValidationContext);
/* 49 */     this.modifier.validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("modifier")));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ModifyContainerContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */