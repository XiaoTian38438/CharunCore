/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.entity.SlotProvider;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ 
/*    */ public class RangeSlotSource implements SlotSource {
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)LootContextArg.ENTITY_OR_BLOCK.fieldOf("source").forGetter(()), (App)SlotRanges.CODEC.fieldOf("slots").forGetter(())).apply((Applicative)paramInstance, RangeSlotSource::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<RangeSlotSource> MAP_CODEC;
/*    */   private final LootContextArg<Object> source;
/*    */   private final SlotRange slotRange;
/*    */   
/*    */   private RangeSlotSource(LootContextArg<Object> paramLootContextArg, SlotRange paramSlotRange) {
/* 24 */     this.source = paramLootContextArg;
/* 25 */     this.slotRange = paramSlotRange;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<RangeSlotSource> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 35 */     return Set.of(this.source.contextParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public final SlotCollection provide(LootContext paramLootContext) {
/* 40 */     Object object = this.source.get(paramLootContext);
/*    */     
/* 42 */     if (object instanceof SlotProvider) { SlotProvider slotProvider = (SlotProvider)object;
/* 43 */       return slotProvider.getSlotsFromRange(this.slotRange.slots()); }
/*    */     
/* 45 */     return SlotCollection.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\RangeSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */