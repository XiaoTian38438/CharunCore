/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ 
/*    */ public class FilteredSlotSource extends TransformedSlotSource {
/*    */   static {
/*  8 */     MAP_CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)ItemPredicate.CODEC.fieldOf("item_filter").forGetter(())).apply((Applicative)paramInstance, FilteredSlotSource::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<FilteredSlotSource> MAP_CODEC;
/*    */   private final ItemPredicate filter;
/*    */   
/*    */   private FilteredSlotSource(SlotSource paramSlotSource, ItemPredicate paramItemPredicate) {
/* 15 */     super(paramSlotSource);
/* 16 */     this.filter = paramItemPredicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<FilteredSlotSource> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SlotCollection transform(SlotCollection paramSlotCollection) {
/* 26 */     return paramSlotCollection.filter((Predicate<ItemStack>)this.filter);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\FilteredSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */