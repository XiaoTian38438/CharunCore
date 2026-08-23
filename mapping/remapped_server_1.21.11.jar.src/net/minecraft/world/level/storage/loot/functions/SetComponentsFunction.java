/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetComponentsFunction extends LootItemConditionalFunction {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)DataComponentPatch.CODEC.fieldOf("components").forGetter(())).apply((Applicative)paramInstance, SetComponentsFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetComponentsFunction> CODEC;
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   private SetComponentsFunction(List<LootItemCondition> paramList, DataComponentPatch paramDataComponentPatch) {
/* 21 */     super(paramList);
/* 22 */     this.components = paramDataComponentPatch;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetComponentsFunction> getType() {
/* 27 */     return LootItemFunctions.SET_COMPONENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 32 */     paramItemStack.applyComponentsAndValidate(this.components);
/* 33 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public static <T> LootItemConditionalFunction.Builder<?> setComponent(DataComponentType<T> paramDataComponentType, T paramT) {
/* 37 */     return simpleBuilder(paramList -> new SetComponentsFunction(paramList, DataComponentPatch.builder().set(paramDataComponentType, paramObject).build()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetComponentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */