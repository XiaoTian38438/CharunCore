/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ 
/*    */ public interface LootItemFunction
/*    */   extends LootContextUser, BiFunction<ItemStack, LootContext, ItemStack>
/*    */ {
/*    */   LootItemFunctionType<? extends LootItemFunction> getType();
/*    */   
/*    */   static Consumer<ItemStack> decorate(BiFunction<ItemStack, LootContext, ItemStack> paramBiFunction, Consumer<ItemStack> paramConsumer, LootContext paramLootContext) {
/* 16 */     return paramItemStack -> paramConsumer.accept(paramBiFunction.apply(paramItemStack, paramLootContext));
/*    */   }
/*    */   
/*    */   public static interface Builder {
/*    */     LootItemFunction build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */