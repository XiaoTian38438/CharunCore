/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 45 */   private Optional<HolderSet<Item>> items = Optional.empty();
/* 46 */   private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
/* 47 */   private DataComponentMatchers components = DataComponentMatchers.ANY;
/*    */   
/*    */   public static Builder item() {
/* 50 */     return new Builder();
/*    */   }
/*    */ 
/*    */   
/*    */   public Builder of(HolderGetter<Item> paramHolderGetter, ItemLike... paramVarArgs) {
/* 55 */     this.items = Optional.of(HolderSet.direct(paramItemLike -> paramItemLike.asItem().builtInRegistryHolder(), (Object[])paramVarArgs));
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public Builder of(HolderGetter<Item> paramHolderGetter, TagKey<Item> paramTagKey) {
/* 60 */     this.items = Optional.of(paramHolderGetter.getOrThrow(paramTagKey));
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withCount(MinMaxBounds.Ints paramInts) {
/* 65 */     this.count = paramInts;
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withComponents(DataComponentMatchers paramDataComponentMatchers) {
/* 70 */     this.components = paramDataComponentMatchers;
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public ItemPredicate build() {
/* 75 */     return new ItemPredicate(this.items, this.count, this.components);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\ItemPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */