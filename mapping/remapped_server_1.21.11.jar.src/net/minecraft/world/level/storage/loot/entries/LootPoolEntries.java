/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LootPoolEntries {
/* 10 */   public static final Codec<LootPoolEntryContainer> CODEC = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.byNameCodec()
/* 11 */     .dispatch(LootPoolEntryContainer::getType, LootPoolEntryType::codec);
/*    */   
/* 13 */   public static final LootPoolEntryType EMPTY = register("empty", (MapCodec)EmptyLootItem.CODEC);
/* 14 */   public static final LootPoolEntryType ITEM = register("item", (MapCodec)LootItem.CODEC);
/* 15 */   public static final LootPoolEntryType LOOT_TABLE = register("loot_table", (MapCodec)NestedLootTable.CODEC);
/* 16 */   public static final LootPoolEntryType DYNAMIC = register("dynamic", (MapCodec)DynamicLoot.CODEC);
/* 17 */   public static final LootPoolEntryType TAG = register("tag", (MapCodec)TagEntry.CODEC);
/* 18 */   public static final LootPoolEntryType SLOTS = register("slots", (MapCodec)SlotLoot.CODEC);
/*    */   
/* 20 */   public static final LootPoolEntryType ALTERNATIVES = register("alternatives", (MapCodec)AlternativesEntry.CODEC);
/* 21 */   public static final LootPoolEntryType SEQUENCE = register("sequence", (MapCodec)SequentialEntry.CODEC);
/* 22 */   public static final LootPoolEntryType GROUP = register("group", (MapCodec)EntryGroup.CODEC);
/*    */   
/*    */   private static LootPoolEntryType register(String paramString, MapCodec<? extends LootPoolEntryContainer> paramMapCodec) {
/* 25 */     return (LootPoolEntryType)Registry.register(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, Identifier.withDefaultNamespace(paramString), new LootPoolEntryType(paramMapCodec));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntries.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */