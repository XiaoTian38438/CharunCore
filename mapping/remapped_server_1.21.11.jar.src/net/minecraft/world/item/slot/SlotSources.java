/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public interface SlotSources {
/*    */   static {
/* 15 */     TYPED_CODEC = BuiltInRegistries.SLOT_SOURCE_TYPE.byNameCodec().dispatch(SlotSource::codec, paramMapCodec -> paramMapCodec);
/* 16 */   } public static final Codec<SlotSource> CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, GroupSlotSource.INLINE_CODEC)); public static final Codec<SlotSource> TYPED_CODEC;
/*    */   
/*    */   static MapCodec<? extends SlotSource> bootstrap(Registry<MapCodec<? extends SlotSource>> paramRegistry) {
/* 19 */     Registry.register(paramRegistry, "group", GroupSlotSource.MAP_CODEC);
/* 20 */     Registry.register(paramRegistry, "filtered", FilteredSlotSource.MAP_CODEC);
/* 21 */     Registry.register(paramRegistry, "limit_slots", LimitSlotSource.MAP_CODEC);
/* 22 */     Registry.register(paramRegistry, "slot_range", RangeSlotSource.MAP_CODEC);
/* 23 */     Registry.register(paramRegistry, "contents", ContentsSlotSource.MAP_CODEC);
/* 24 */     return (MapCodec<? extends SlotSource>)Registry.register(paramRegistry, "empty", EmptySlotSource.MAP_CODEC);
/*    */   }
/*    */   static Function<LootContext, SlotCollection> group(Collection<? extends SlotSource> paramCollection) {
/*    */     SlotSource slotSource1, slotSource2;
/* 28 */     List<SlotSource> list = List.copyOf(paramCollection);
/* 29 */     switch (list.size()) { case 0: 
/*    */       case 1:
/* 31 */         Objects.requireNonNull(list.getFirst());
/*    */       case 2:
/* 33 */         slotSource1 = list.get(0);
/* 34 */         slotSource2 = list.get(1); }
/*    */     
/*    */     return paramLootContext -> {
/*    */         ArrayList<SlotCollection> arrayList = new ArrayList();
/*    */         for (SlotSource slotSource : paramList)
/*    */           arrayList.add(slotSource.provide(paramLootContext)); 
/*    */         return SlotCollection.concat(arrayList);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\SlotSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */