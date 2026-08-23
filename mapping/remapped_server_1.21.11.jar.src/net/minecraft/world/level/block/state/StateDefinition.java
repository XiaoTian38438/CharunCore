/*     */ package net.minecraft.world.level.block.state;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSortedMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class StateDefinition<O, S extends StateHolder<O, S>> {
/*  28 */   static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
/*     */   
/*     */   private final O owner;
/*     */   private final ImmutableSortedMap<String, Property<?>> propertiesByName;
/*     */   private final ImmutableList<S> states;
/*     */   
/*     */   protected StateDefinition(Function<O, S> paramFunction, O paramO, Factory<O, S> paramFactory, Map<String, Property<?>> paramMap) {
/*  35 */     this.owner = paramO;
/*  36 */     this.propertiesByName = ImmutableSortedMap.copyOf(paramMap);
/*     */     
/*  38 */     Supplier<StateHolder> supplier = () -> (StateHolder)paramFunction.apply(paramObject);
/*  39 */     MapCodec<StateHolder> mapCodec1 = MapCodec.of(Encoder.empty(), Decoder.unit(supplier));
/*  40 */     for (UnmodifiableIterator<Map.Entry> unmodifiableIterator = this.propertiesByName.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry entry = unmodifiableIterator.next();
/*  41 */       mapCodec1 = appendPropertyCodec(mapCodec1, supplier, (String)entry.getKey(), (Property<Comparable>)entry.getValue()); }
/*     */ 
/*     */     
/*  44 */     MapCodec<StateHolder> mapCodec2 = mapCodec1;
/*     */ 
/*     */     
/*  47 */     LinkedHashMap linkedHashMap = Maps.newLinkedHashMap();
/*  48 */     ArrayList arrayList = Lists.newArrayList();
/*     */     
/*  50 */     Stream<?> stream = Stream.of(Collections.emptyList());
/*  51 */     for (UnmodifiableIterator<Property> unmodifiableIterator1 = this.propertiesByName.values().iterator(); unmodifiableIterator1.hasNext(); ) { Property property = unmodifiableIterator1.next();
/*  52 */       stream = stream.flatMap(paramList -> paramProperty.getPossibleValues().stream().map(())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     stream.forEach(paramList2 -> {
/*     */           Reference2ObjectArrayMap<Property<?>, Comparable<?>> reference2ObjectArrayMap = new Reference2ObjectArrayMap(paramList2.size());
/*     */           
/*     */           for (Pair pair : paramList2) {
/*     */             reference2ObjectArrayMap.put(pair.getFirst(), pair.getSecond());
/*     */           }
/*     */           
/*     */           StateHolder stateHolder = paramFactory.create(paramObject, reference2ObjectArrayMap, paramMapCodec);
/*     */           paramMap.put(reference2ObjectArrayMap, stateHolder);
/*     */           paramList1.add(stateHolder);
/*     */         });
/*  70 */     for (StateHolder stateHolder : arrayList) {
/*  71 */       stateHolder.populateNeighbours(linkedHashMap);
/*     */     }
/*     */     
/*  74 */     this.states = ImmutableList.copyOf(arrayList);
/*     */   }
/*     */   
/*     */   private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(MapCodec<S> paramMapCodec, Supplier<S> paramSupplier, String paramString, Property<T> paramProperty) {
/*  78 */     return Codec.mapPair(paramMapCodec, paramProperty
/*     */         
/*  80 */         .valueCodec().fieldOf(paramString).orElseGet(paramString -> { 
/*  81 */           }() -> paramProperty.value(paramSupplier.get()))).xmap(paramPair -> (StateHolder)((StateHolder)paramPair.getFirst()).setValue(paramProperty, ((Property.Value)paramPair.getSecond()).value()), paramStateHolder -> Pair.of(paramStateHolder, paramProperty.value(paramStateHolder)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<S> getPossibleStates() {
/*  88 */     return this.states;
/*     */   }
/*     */   
/*     */   public S any() {
/*  92 */     return (S)this.states.get(0);
/*     */   }
/*     */   
/*     */   public O getOwner() {
/*  96 */     return this.owner;
/*     */   }
/*     */   
/*     */   public Collection<Property<?>> getProperties() {
/* 100 */     return (Collection<Property<?>>)this.propertiesByName.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return MoreObjects.toStringHelper(this)
/* 106 */       .add("block", this.owner)
/* 107 */       .add("properties", this.propertiesByName.values().stream().map(Property::getName).collect(Collectors.toList()))
/* 108 */       .toString();
/*     */   }
/*     */   
/*     */   public Property<?> getProperty(String paramString) {
/* 112 */     return (Property)this.propertiesByName.get(paramString);
/*     */   }
/*     */   
/*     */   public static interface Factory<O, S> {
/*     */     S create(O param1O, Reference2ObjectArrayMap<Property<?>, Comparable<?>> param1Reference2ObjectArrayMap, MapCodec<S> param1MapCodec);
/*     */   }
/*     */   
/*     */   public static class Builder<O, S extends StateHolder<O, S>> {
/*     */     private final O owner;
/* 121 */     private final Map<String, Property<?>> properties = Maps.newHashMap();
/*     */     
/*     */     public Builder(O param1O) {
/* 124 */       this.owner = param1O;
/*     */     }
/*     */     
/*     */     public Builder<O, S> add(Property<?>... param1VarArgs) {
/* 128 */       for (Property<?> property : param1VarArgs) {
/* 129 */         validateProperty(property);
/* 130 */         this.properties.put(property.getName(), property);
/*     */       } 
/* 132 */       return this;
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> void validateProperty(Property<T> param1Property) {
/* 136 */       String str = param1Property.getName();
/* 137 */       if (!StateDefinition.NAME_PATTERN.matcher(str).matches()) {
/* 138 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + String.valueOf(this.owner));
/*     */       }
/*     */       
/* 141 */       List list = param1Property.getPossibleValues();
/* 142 */       if (list.size() <= 1) {
/* 143 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + String.valueOf(this.owner) + " with <= 1 possible values");
/*     */       }
/*     */       
/* 146 */       for (Comparable comparable : list) {
/* 147 */         String str1 = param1Property.getName(comparable);
/* 148 */         if (!StateDefinition.NAME_PATTERN.matcher(str1).matches()) {
/* 149 */           throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + String.valueOf(this.owner) + " with invalidly named value: " + str);
/*     */         }
/*     */       } 
/*     */       
/* 153 */       if (this.properties.containsKey(str)) {
/* 154 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + String.valueOf(this.owner));
/*     */       }
/*     */     }
/*     */     
/*     */     public StateDefinition<O, S> create(Function<O, S> param1Function, StateDefinition.Factory<O, S> param1Factory) {
/* 159 */       return new StateDefinition<>(param1Function, this.owner, param1Factory, this.properties);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\StateDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */