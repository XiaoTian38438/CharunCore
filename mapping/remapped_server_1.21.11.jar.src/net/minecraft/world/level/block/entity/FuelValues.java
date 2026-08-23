/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
/*     */ import java.util.Collections;
/*     */ import java.util.SequencedSet;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class FuelValues {
/*     */   private final Object2IntSortedMap<Item> values;
/*     */   
/*     */   FuelValues(Object2IntSortedMap<Item> paramObject2IntSortedMap) {
/*  24 */     this.values = paramObject2IntSortedMap;
/*     */   }
/*     */   
/*     */   public boolean isFuel(ItemStack paramItemStack) {
/*  28 */     return this.values.containsKey(paramItemStack.getItem());
/*     */   }
/*     */   
/*     */   public SequencedSet<Item> fuelItems() {
/*  32 */     return Collections.unmodifiableSequencedSet((SequencedSet<? extends Item>)this.values.keySet());
/*     */   }
/*     */   
/*     */   public int burnDuration(ItemStack paramItemStack) {
/*  36 */     if (paramItemStack.isEmpty()) {
/*  37 */       return 0;
/*     */     }
/*     */     
/*  40 */     return this.values.getInt(paramItemStack.getItem());
/*     */   }
/*     */   
/*     */   public static FuelValues vanillaBurnTimes(HolderLookup.Provider paramProvider, FeatureFlagSet paramFeatureFlagSet) {
/*  44 */     return vanillaBurnTimes(paramProvider, paramFeatureFlagSet, 200);
/*     */   }
/*     */   
/*     */   public static FuelValues vanillaBurnTimes(HolderLookup.Provider paramProvider, FeatureFlagSet paramFeatureFlagSet, int paramInt) {
/*  48 */     return (new Builder(paramProvider, paramFeatureFlagSet))
/*  49 */       .add((ItemLike)Items.LAVA_BUCKET, paramInt * 100)
/*  50 */       .add((ItemLike)Blocks.COAL_BLOCK, paramInt * 8 * 10)
/*  51 */       .add((ItemLike)Items.BLAZE_ROD, paramInt * 12)
/*  52 */       .add((ItemLike)Items.COAL, paramInt * 8)
/*  53 */       .add((ItemLike)Items.CHARCOAL, paramInt * 8)
/*  54 */       .add(ItemTags.LOGS, paramInt * 3 / 2)
/*  55 */       .add(ItemTags.BAMBOO_BLOCKS, paramInt * 3 / 2)
/*  56 */       .add(ItemTags.PLANKS, paramInt * 3 / 2)
/*  57 */       .add((ItemLike)Blocks.BAMBOO_MOSAIC, paramInt * 3 / 2)
/*  58 */       .add(ItemTags.WOODEN_STAIRS, paramInt * 3 / 2)
/*  59 */       .add((ItemLike)Blocks.BAMBOO_MOSAIC_STAIRS, paramInt * 3 / 2)
/*  60 */       .add(ItemTags.WOODEN_SLABS, paramInt * 3 / 4)
/*  61 */       .add((ItemLike)Blocks.BAMBOO_MOSAIC_SLAB, paramInt * 3 / 4)
/*  62 */       .add(ItemTags.WOODEN_TRAPDOORS, paramInt * 3 / 2)
/*  63 */       .add(ItemTags.WOODEN_PRESSURE_PLATES, paramInt * 3 / 2)
/*  64 */       .add(ItemTags.WOODEN_SHELVES, paramInt * 3 / 2)
/*  65 */       .add(ItemTags.WOODEN_FENCES, paramInt * 3 / 2)
/*  66 */       .add(ItemTags.FENCE_GATES, paramInt * 3 / 2)
/*  67 */       .add((ItemLike)Blocks.NOTE_BLOCK, paramInt * 3 / 2)
/*  68 */       .add((ItemLike)Blocks.BOOKSHELF, paramInt * 3 / 2)
/*  69 */       .add((ItemLike)Blocks.CHISELED_BOOKSHELF, paramInt * 3 / 2)
/*  70 */       .add((ItemLike)Blocks.LECTERN, paramInt * 3 / 2)
/*  71 */       .add((ItemLike)Blocks.JUKEBOX, paramInt * 3 / 2)
/*  72 */       .add((ItemLike)Blocks.CHEST, paramInt * 3 / 2)
/*  73 */       .add((ItemLike)Blocks.TRAPPED_CHEST, paramInt * 3 / 2)
/*  74 */       .add((ItemLike)Blocks.CRAFTING_TABLE, paramInt * 3 / 2)
/*  75 */       .add((ItemLike)Blocks.DAYLIGHT_DETECTOR, paramInt * 3 / 2)
/*  76 */       .add(ItemTags.BANNERS, paramInt * 3 / 2)
/*  77 */       .add((ItemLike)Items.BOW, paramInt * 3 / 2)
/*  78 */       .add((ItemLike)Items.FISHING_ROD, paramInt * 3 / 2)
/*  79 */       .add((ItemLike)Blocks.LADDER, paramInt * 3 / 2)
/*  80 */       .add(ItemTags.SIGNS, paramInt)
/*  81 */       .add(ItemTags.HANGING_SIGNS, paramInt * 4)
/*  82 */       .add((ItemLike)Items.WOODEN_SHOVEL, paramInt)
/*  83 */       .add((ItemLike)Items.WOODEN_SWORD, paramInt)
/*  84 */       .add((ItemLike)Items.WOODEN_SPEAR, paramInt)
/*  85 */       .add((ItemLike)Items.WOODEN_HOE, paramInt)
/*  86 */       .add((ItemLike)Items.WOODEN_AXE, paramInt)
/*  87 */       .add((ItemLike)Items.WOODEN_PICKAXE, paramInt)
/*  88 */       .add(ItemTags.WOODEN_DOORS, paramInt)
/*  89 */       .add(ItemTags.BOATS, paramInt * 6)
/*  90 */       .add(ItemTags.WOOL, paramInt / 2)
/*  91 */       .add(ItemTags.WOODEN_BUTTONS, paramInt / 2)
/*  92 */       .add((ItemLike)Items.STICK, paramInt / 2)
/*  93 */       .add(ItemTags.SAPLINGS, paramInt / 2)
/*  94 */       .add((ItemLike)Items.BOWL, paramInt / 2)
/*  95 */       .add(ItemTags.WOOL_CARPETS, 1 + paramInt / 3)
/*  96 */       .add((ItemLike)Blocks.DRIED_KELP_BLOCK, 1 + paramInt * 20)
/*  97 */       .add((ItemLike)Items.CROSSBOW, paramInt * 3 / 2)
/*  98 */       .add((ItemLike)Blocks.BAMBOO, paramInt / 4)
/*  99 */       .add((ItemLike)Blocks.DEAD_BUSH, paramInt / 2)
/* 100 */       .add((ItemLike)Blocks.SHORT_DRY_GRASS, paramInt / 2)
/* 101 */       .add((ItemLike)Blocks.TALL_DRY_GRASS, paramInt / 2)
/* 102 */       .add((ItemLike)Blocks.SCAFFOLDING, paramInt / 4)
/* 103 */       .add((ItemLike)Blocks.LOOM, paramInt * 3 / 2)
/* 104 */       .add((ItemLike)Blocks.BARREL, paramInt * 3 / 2)
/* 105 */       .add((ItemLike)Blocks.CARTOGRAPHY_TABLE, paramInt * 3 / 2)
/* 106 */       .add((ItemLike)Blocks.FLETCHING_TABLE, paramInt * 3 / 2)
/* 107 */       .add((ItemLike)Blocks.SMITHING_TABLE, paramInt * 3 / 2)
/* 108 */       .add((ItemLike)Blocks.COMPOSTER, paramInt * 3 / 2)
/* 109 */       .add((ItemLike)Blocks.AZALEA, paramInt / 2)
/* 110 */       .add((ItemLike)Blocks.FLOWERING_AZALEA, paramInt / 2)
/* 111 */       .add((ItemLike)Blocks.MANGROVE_ROOTS, paramInt * 3 / 2)
/* 112 */       .add((ItemLike)Blocks.LEAF_LITTER, paramInt / 2)
/*     */       
/* 114 */       .remove(ItemTags.NON_FLAMMABLE_WOOD)
/* 115 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final HolderLookup<Item> items;
/*     */     private final FeatureFlagSet enabledFeatures;
/* 123 */     private final Object2IntSortedMap<Item> values = (Object2IntSortedMap<Item>)new Object2IntLinkedOpenHashMap();
/*     */     
/*     */     public Builder(HolderLookup.Provider param1Provider, FeatureFlagSet param1FeatureFlagSet) {
/* 126 */       this.items = (HolderLookup<Item>)param1Provider.lookupOrThrow(Registries.ITEM);
/* 127 */       this.enabledFeatures = param1FeatureFlagSet;
/*     */     }
/*     */     
/*     */     public FuelValues build() {
/* 131 */       return new FuelValues(this.values);
/*     */     }
/*     */     
/*     */     public Builder remove(TagKey<Item> param1TagKey) {
/* 135 */       this.values.keySet().removeIf(param1Item -> param1Item.builtInRegistryHolder().is(param1TagKey));
/* 136 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(TagKey<Item> param1TagKey, int param1Int) {
/* 140 */       this.items.get(param1TagKey).ifPresent(param1Named -> {
/*     */             for (Holder holder : param1Named) {
/*     */               putInternal(param1Int, (Item)holder.value());
/*     */             }
/*     */           });
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(ItemLike param1ItemLike, int param1Int) {
/* 149 */       Item item = param1ItemLike.asItem();
/* 150 */       putInternal(param1Int, item);
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     private void putInternal(int param1Int, Item param1Item) {
/* 155 */       if (param1Item.isEnabled(this.enabledFeatures))
/* 156 */         this.values.put(param1Item, param1Int); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\FuelValues.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */