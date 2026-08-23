/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ItemLore;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetLoreFunction extends LootItemConditionalFunction {
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)ComponentSerialization.CODEC.sizeLimitedListOf(256).fieldOf("lore").forGetter(()), (App)ListOperation.codec(256).forGetter(()), (App)LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(()))).apply((Applicative)paramInstance, SetLoreFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetLoreFunction> CODEC;
/*    */   
/*    */   private final List<Component> lore;
/*    */   private final ListOperation mode;
/*    */   private final Optional<LootContext.EntityTarget> resolutionContext;
/*    */   
/*    */   public SetLoreFunction(List<LootItemCondition> paramList, List<Component> paramList1, ListOperation paramListOperation, Optional<LootContext.EntityTarget> paramOptional) {
/* 33 */     super(paramList);
/* 34 */     this.lore = List.copyOf(paramList1);
/* 35 */     this.mode = paramListOperation;
/* 36 */     this.resolutionContext = paramOptional;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetLoreFunction> getType() {
/* 41 */     return LootItemFunctions.SET_LORE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 46 */     return this.resolutionContext.<Set<ContextKey<?>>>map(paramEntityTarget -> Set.of(paramEntityTarget.contextParam())).orElseGet(Set::of);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 51 */     paramItemStack.update(DataComponents.LORE, ItemLore.EMPTY, paramItemLore -> new ItemLore(updateLore(paramItemLore, paramLootContext)));
/* 52 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   private List<Component> updateLore(ItemLore paramItemLore, LootContext paramLootContext) {
/* 56 */     if (paramItemLore == null && this.lore.isEmpty()) {
/* 57 */       return List.of();
/*    */     }
/* 59 */     UnaryOperator<Component> unaryOperator = SetNameFunction.createResolver(paramLootContext, this.resolutionContext.orElse(null));
/* 60 */     List<Component> list = this.lore.stream().map(unaryOperator).toList();
/* 61 */     return this.mode.apply(paramItemLore.lines(), list, 256);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 65 */     private Optional<LootContext.EntityTarget> resolutionContext = Optional.empty();
/* 66 */     private final ImmutableList.Builder<Component> lore = ImmutableList.builder();
/* 67 */     private ListOperation mode = ListOperation.Append.INSTANCE;
/*    */     
/*    */     public Builder setMode(ListOperation param1ListOperation) {
/* 70 */       this.mode = param1ListOperation;
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setResolutionContext(LootContext.EntityTarget param1EntityTarget) {
/* 75 */       this.resolutionContext = Optional.of(param1EntityTarget);
/* 76 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addLine(Component param1Component) {
/* 80 */       this.lore.add(param1Component);
/* 81 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 86 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 91 */       return new SetLoreFunction(getConditions(), (List<Component>)this.lore.build(), this.mode, this.resolutionContext);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setLore() {
/* 96 */     return new Builder();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */