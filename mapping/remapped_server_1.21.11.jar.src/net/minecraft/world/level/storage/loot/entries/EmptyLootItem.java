/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EmptyLootItem extends LootPoolSingletonContainer {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> singletonFields(paramInstance).apply((Applicative)paramInstance, EmptyLootItem::new));
/*    */   } public static final MapCodec<EmptyLootItem> CODEC;
/*    */   private EmptyLootItem(int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1) {
/* 17 */     super(paramInt1, paramInt2, paramList, paramList1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 22 */     return LootPoolEntries.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext) {}
/*    */ 
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> emptyItem() {
/* 30 */     return simpleBuilder(EmptyLootItem::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\EmptyLootItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */