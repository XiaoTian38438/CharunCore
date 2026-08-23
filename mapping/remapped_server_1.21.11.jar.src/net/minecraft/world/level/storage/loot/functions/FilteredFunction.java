/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FilteredFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)ItemPredicate.CODEC.fieldOf("item_filter").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.optionalFieldOf("on_pass").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.optionalFieldOf("on_fail").forGetter(()))).apply((Applicative)paramInstance, FilteredFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FilteredFunction> CODEC;
/*    */   
/*    */   private final ItemPredicate filter;
/*    */   private final Optional<LootItemFunction> onPass;
/*    */   private final Optional<LootItemFunction> onFail;
/*    */   
/*    */   FilteredFunction(List<LootItemCondition> paramList, ItemPredicate paramItemPredicate, Optional<LootItemFunction> paramOptional1, Optional<LootItemFunction> paramOptional2) {
/* 27 */     super(paramList);
/* 28 */     this.filter = paramItemPredicate;
/* 29 */     this.onPass = paramOptional1;
/* 30 */     this.onFail = paramOptional2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<FilteredFunction> getType() {
/* 35 */     return LootItemFunctions.FILTERED;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 40 */     Optional<LootItemFunction> optional = this.filter.test(paramItemStack) ? this.onPass : this.onFail;
/* 41 */     if (optional.isPresent()) {
/* 42 */       return ((LootItemFunction)optional.get()).apply(paramItemStack, paramLootContext);
/*    */     }
/* 44 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 49 */     super.validate(paramValidationContext);
/* 50 */     this.onPass.ifPresent(paramLootItemFunction -> paramLootItemFunction.validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("on_pass"))));
/* 51 */     this.onFail.ifPresent(paramLootItemFunction -> paramLootItemFunction.validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("on_fail"))));
/*    */   }
/*    */   
/*    */   public static Builder filtered(ItemPredicate paramItemPredicate) {
/* 55 */     return new Builder(paramItemPredicate);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final ItemPredicate itemPredicate;
/* 60 */     private Optional<LootItemFunction> onPass = Optional.empty();
/* 61 */     private Optional<LootItemFunction> onFail = Optional.empty();
/*    */     
/*    */     Builder(ItemPredicate param1ItemPredicate) {
/* 64 */       this.itemPredicate = param1ItemPredicate;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 69 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onPass(Optional<LootItemFunction> param1Optional) {
/* 73 */       this.onPass = param1Optional;
/* 74 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onFail(Optional<LootItemFunction> param1Optional) {
/* 78 */       this.onFail = param1Optional;
/* 79 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 84 */       return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\FilteredFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */