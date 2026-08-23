/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LootItem extends LootPoolSingletonContainer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Item.CODEC.fieldOf("name").forGetter(())).and(singletonFields(paramInstance)).apply((Applicative)paramInstance, LootItem::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<LootItem> CODEC;
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private LootItem(Holder<Item> paramHolder, int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1) {
/* 24 */     super(paramInt1, paramInt2, paramList, paramList1);
/* 25 */     this.item = paramHolder;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 30 */     return LootPoolEntries.ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext) {
/* 35 */     paramConsumer.accept(new ItemStack(this.item));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike paramItemLike) {
/* 39 */     return simpleBuilder((paramInt1, paramInt2, paramList1, paramList2) -> new LootItem((Holder<Item>)paramItemLike.asItem().builtInRegistryHolder(), paramInt1, paramInt2, paramList1, paramList2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\LootItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */