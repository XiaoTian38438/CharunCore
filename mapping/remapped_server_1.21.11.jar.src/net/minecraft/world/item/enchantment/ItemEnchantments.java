/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.component.TooltipProvider;
/*     */ 
/*     */ public class ItemEnchantments implements TooltipProvider {
/*  31 */   public static final ItemEnchantments EMPTY = new ItemEnchantments(new Object2IntOpenHashMap());
/*     */   
/*  33 */   private static final Codec<Integer> LEVEL_CODEC = Codec.intRange(1, 255); public static final Codec<ItemEnchantments> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemEnchantments> STREAM_CODEC;
/*     */   final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;
/*     */   
/*     */   static {
/*  38 */     CODEC = Codec.unboundedMap(Enchantment.CODEC, LEVEL_CODEC).xmap(paramMap -> new ItemEnchantments(new Object2IntOpenHashMap(paramMap)), paramItemEnchantments -> paramItemEnchantments.enchantments);
/*     */     
/*  40 */     STREAM_CODEC = StreamCodec.composite(
/*  41 */         ByteBufCodecs.map(Object2IntOpenHashMap::new, Enchantment.STREAM_CODEC, ByteBufCodecs.VAR_INT), paramItemEnchantments -> paramItemEnchantments.enchantments, ItemEnchantments::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ItemEnchantments(Object2IntOpenHashMap<Holder<Enchantment>> paramObject2IntOpenHashMap) {
/*  48 */     this.enchantments = paramObject2IntOpenHashMap;
/*     */     
/*  50 */     for (ObjectIterator<Object2IntMap.Entry> objectIterator = paramObject2IntOpenHashMap.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry entry = objectIterator.next();
/*  51 */       int i = entry.getIntValue();
/*  52 */       if (i < 0 || i > 255) {
/*  53 */         throw new IllegalArgumentException("Enchantment " + String.valueOf(entry.getKey()) + " has invalid level " + i);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public int getLevel(Holder<Enchantment> paramHolder) {
/*  59 */     return this.enchantments.getInt(paramHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag, DataComponentGetter paramDataComponentGetter) {
/*  64 */     HolderLookup.Provider provider = paramTooltipContext.registries();
/*  65 */     HolderSet<?> holderSet = getTagOrEmpty(provider, Registries.ENCHANTMENT, EnchantmentTags.TOOLTIP_ORDER);
/*  66 */     for (Holder<Enchantment> holder : holderSet) {
/*  67 */       int i = this.enchantments.getInt(holder);
/*  68 */       if (i > 0) {
/*  69 */         paramConsumer.accept(Enchantment.getFullname(holder, i));
/*     */       }
/*     */     } 
/*  72 */     for (ObjectIterator<Object2IntMap.Entry> objectIterator = this.enchantments.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry entry = objectIterator.next();
/*  73 */       Holder holder = (Holder)entry.getKey();
/*  74 */       if (!holderSet.contains(holder)) {
/*  75 */         paramConsumer.accept(Enchantment.getFullname((Holder<Enchantment>)entry.getKey(), entry.getIntValue()));
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private static <T> HolderSet<T> getTagOrEmpty(HolderLookup.Provider paramProvider, ResourceKey<Registry<T>> paramResourceKey, TagKey<T> paramTagKey) {
/*  81 */     if (paramProvider != null) {
/*  82 */       Optional<HolderSet<T>> optional = paramProvider.lookupOrThrow(paramResourceKey).get(paramTagKey);
/*  83 */       if (optional.isPresent()) {
/*  84 */         return optional.get();
/*     */       }
/*     */     } 
/*  87 */     return (HolderSet<T>)HolderSet.direct(new Holder[0]);
/*     */   }
/*     */   
/*     */   public Set<Holder<Enchantment>> keySet() {
/*  91 */     return Collections.unmodifiableSet((Set<? extends Holder<Enchantment>>)this.enchantments.keySet());
/*     */   }
/*     */   
/*     */   public Set<Object2IntMap.Entry<Holder<Enchantment>>> entrySet() {
/*  95 */     return Collections.unmodifiableSet((Set<? extends Object2IntMap.Entry<Holder<Enchantment>>>)this.enchantments.object2IntEntrySet());
/*     */   }
/*     */   
/*     */   public int size() {
/*  99 */     return this.enchantments.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 103 */     return this.enchantments.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 108 */     if (this == paramObject) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (paramObject instanceof ItemEnchantments) { ItemEnchantments itemEnchantments = (ItemEnchantments)paramObject;
/* 112 */       return this.enchantments.equals(itemEnchantments.enchantments); }
/*     */     
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return this.enchantments.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return "ItemEnchantments{enchantments=" + String.valueOf(this.enchantments) + "}";
/*     */   }
/*     */   
/*     */   public static class Mutable {
/* 128 */     private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments = new Object2IntOpenHashMap();
/*     */     
/*     */     public Mutable(ItemEnchantments param1ItemEnchantments) {
/* 131 */       this.enchantments.putAll((Map)param1ItemEnchantments.enchantments);
/*     */     }
/*     */     
/*     */     public void set(Holder<Enchantment> param1Holder, int param1Int) {
/* 135 */       if (param1Int <= 0) {
/* 136 */         this.enchantments.removeInt(param1Holder);
/*     */       } else {
/* 138 */         this.enchantments.put(param1Holder, Math.min(param1Int, 255));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void upgrade(Holder<Enchantment> param1Holder, int param1Int) {
/* 143 */       if (param1Int > 0) {
/* 144 */         this.enchantments.merge(param1Holder, Math.min(param1Int, 255), Integer::max);
/*     */       }
/*     */     }
/*     */     
/*     */     public void removeIf(Predicate<Holder<Enchantment>> param1Predicate) {
/* 149 */       this.enchantments.keySet().removeIf(param1Predicate);
/*     */     }
/*     */     
/*     */     public int getLevel(Holder<Enchantment> param1Holder) {
/* 153 */       return this.enchantments.getOrDefault(param1Holder, 0);
/*     */     }
/*     */     
/*     */     public Set<Holder<Enchantment>> keySet() {
/* 157 */       return (Set<Holder<Enchantment>>)this.enchantments.keySet();
/*     */     }
/*     */     
/*     */     public ItemEnchantments toImmutable() {
/* 161 */       return new ItemEnchantments(this.enchantments);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\ItemEnchantments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */