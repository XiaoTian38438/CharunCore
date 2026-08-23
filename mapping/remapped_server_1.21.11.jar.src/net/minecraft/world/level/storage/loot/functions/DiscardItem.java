/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DiscardItem extends LootItemConditionalFunction {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).apply((Applicative)paramInstance, DiscardItem::new));
/*    */   }
/*    */   public static final MapCodec<DiscardItem> CODEC;
/*    */   
/*    */   protected DiscardItem(List<LootItemCondition> paramList) {
/* 17 */     super(paramList);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<DiscardItem> getType() {
/* 22 */     return LootItemFunctions.DISCARD;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 27 */     return ItemStack.EMPTY;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> discardItem() {
/* 31 */     return simpleBuilder(DiscardItem::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\DiscardItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */