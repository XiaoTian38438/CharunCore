/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WeightedList<E>
/*     */ {
/*     */   private static final int FLAT_THRESHOLD = 64;
/*     */   private final int totalWeight;
/*     */   private final List<Weighted<E>> items;
/*     */   private final Selector<E> selector;
/*     */   
/*     */   WeightedList(List<? extends Weighted<E>> paramList) {
/*  28 */     this.items = List.copyOf(paramList);
/*  29 */     this.totalWeight = WeightedRandom.getTotalWeight(paramList, Weighted::weight);
/*  30 */     if (this.totalWeight == 0) {
/*  31 */       this.selector = null;
/*  32 */     } else if (this.totalWeight < 64) {
/*  33 */       this.selector = new Flat<>(this.items, this.totalWeight);
/*     */     } else {
/*  35 */       this.selector = new Compact<>(this.items);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of() {
/*  40 */     return new WeightedList<>(List.of());
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(E paramE) {
/*  44 */     return new WeightedList<>(List.of(new Weighted<>(paramE, 1)));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> WeightedList<E> of(Weighted<E>... paramVarArgs) {
/*  49 */     return new WeightedList<>(List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(List<Weighted<E>> paramList) {
/*  53 */     return new WeightedList<>(paramList);
/*     */   }
/*     */   
/*     */   public static <E> Builder<E> builder() {
/*  57 */     return new Builder<>();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  61 */     return this.items.isEmpty();
/*     */   }
/*     */   
/*     */   public <T> WeightedList<T> map(Function<E, T> paramFunction) {
/*  65 */     return new WeightedList(Lists.transform(this.items, paramWeighted -> paramWeighted.map(paramFunction)));
/*     */   }
/*     */   
/*     */   public Optional<E> getRandom(RandomSource paramRandomSource) {
/*  69 */     if (this.selector == null) {
/*  70 */       return Optional.empty();
/*     */     }
/*  72 */     int i = paramRandomSource.nextInt(this.totalWeight);
/*  73 */     return Optional.of(this.selector.get(i));
/*     */   }
/*     */   
/*     */   public E getRandomOrThrow(RandomSource paramRandomSource) {
/*  77 */     if (this.selector == null) {
/*  78 */       throw new IllegalStateException("Weighted list has no elements");
/*     */     }
/*  80 */     int i = paramRandomSource.nextInt(this.totalWeight);
/*  81 */     return this.selector.get(i);
/*     */   }
/*     */   
/*     */   public List<Weighted<E>> unwrap() {
/*  85 */     return this.items;
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> codec(Codec<E> paramCodec) {
/*  89 */     return Weighted.<E>codec(paramCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> codec(MapCodec<E> paramMapCodec) {
/*  93 */     return Weighted.<E>codec(paramMapCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(Codec<E> paramCodec) {
/*  97 */     return ExtraCodecs.nonEmptyList(Weighted.<E>codec(paramCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(MapCodec<E> paramMapCodec) {
/* 101 */     return ExtraCodecs.nonEmptyList(Weighted.<E>codec(paramMapCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E, B extends io.netty.buffer.ByteBuf> StreamCodec<B, WeightedList<E>> streamCodec(StreamCodec<B, E> paramStreamCodec) {
/* 105 */     return Weighted.<B, T>streamCodec(paramStreamCodec).apply(ByteBufCodecs.list()).map(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public boolean contains(E paramE) {
/* 109 */     for (Weighted<T> weighted : this.items) {
/* 110 */       if (weighted.value().equals(paramE)) {
/* 111 */         return true;
/*     */       }
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 119 */     if (this == paramObject) {
/* 120 */       return true;
/*     */     }
/* 122 */     if (paramObject instanceof WeightedList) { WeightedList weightedList = (WeightedList)paramObject;
/* 123 */       return (this.totalWeight == weightedList.totalWeight && Objects.equals(this.items, weightedList.items)); }
/*     */     
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 130 */     int i = this.totalWeight;
/* 131 */     i = 31 * i + this.items.hashCode();
/* 132 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */   {
/* 143 */     private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();
/*     */     
/*     */     public Builder<E> add(E param1E) {
/* 146 */       return add(param1E, 1);
/*     */     }
/*     */     
/*     */     public Builder<E> add(E param1E, int param1Int) {
/* 150 */       this.result.add(new Weighted<>(param1E, param1Int));
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     public WeightedList<E> build() {
/* 155 */       return new WeightedList<>((List<? extends Weighted<E>>)this.result.build());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Flat<E> implements Selector<E> {
/*     */     private final Object[] entries;
/*     */     
/*     */     Flat(List<Weighted<E>> param1List, int param1Int) {
/* 163 */       this.entries = new Object[param1Int];
/* 164 */       int i = 0;
/* 165 */       for (Weighted<E> weighted : param1List) {
/* 166 */         int j = weighted.weight();
/* 167 */         Arrays.fill(this.entries, i, i + j, weighted.value());
/* 168 */         i += j;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E get(int param1Int) {
/* 175 */       return (E)this.entries[param1Int];
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Compact<E> implements Selector<E> {
/*     */     private final Weighted<?>[] entries;
/*     */     
/*     */     Compact(List<Weighted<E>> param1List) {
/* 183 */       this.entries = (Weighted<?>[])param1List.toArray(param1Int -> new Weighted[param1Int]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E get(int param1Int) {
/* 189 */       for (Weighted<?> weighted : this.entries) {
/* 190 */         param1Int -= weighted.weight();
/* 191 */         if (param1Int < 0) {
/* 192 */           return (E)weighted.value();
/*     */         }
/*     */       } 
/* 195 */       throw new IllegalStateException("" + param1Int + " exceeded total weight");
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface Selector<E> {
/*     */     E get(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\random\WeightedList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */