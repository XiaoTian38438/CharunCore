/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetItemFunction extends LootItemConditionalFunction {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)Item.CODEC.fieldOf("item").forGetter(())).apply((Applicative)paramInstance, SetItemFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetItemFunction> CODEC;
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private SetItemFunction(List<LootItemCondition> paramList, Holder<Item> paramHolder) {
/* 21 */     super(paramList);
/* 22 */     this.item = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetItemFunction> getType() {
/* 27 */     return LootItemFunctions.SET_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 32 */     return paramItemStack.transmuteCopy((ItemLike)this.item.value());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */