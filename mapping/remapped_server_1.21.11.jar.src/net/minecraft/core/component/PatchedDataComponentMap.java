/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatchedDataComponentMap
/*     */   implements DataComponentMap
/*     */ {
/*     */   private final DataComponentMap prototype;
/*     */   private Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch;
/*     */   private boolean copyOnWrite;
/*     */   
/*     */   public PatchedDataComponentMap(DataComponentMap paramDataComponentMap) {
/*  33 */     this(paramDataComponentMap, Reference2ObjectMaps.emptyMap(), true);
/*     */   }
/*     */   
/*     */   private PatchedDataComponentMap(DataComponentMap paramDataComponentMap, Reference2ObjectMap<DataComponentType<?>, Optional<?>> paramReference2ObjectMap, boolean paramBoolean) {
/*  37 */     this.prototype = paramDataComponentMap;
/*  38 */     this.patch = paramReference2ObjectMap;
/*  39 */     this.copyOnWrite = paramBoolean;
/*     */   }
/*     */   
/*     */   public static PatchedDataComponentMap fromPatch(DataComponentMap paramDataComponentMap, DataComponentPatch paramDataComponentPatch) {
/*  43 */     if (isPatchSanitized(paramDataComponentMap, paramDataComponentPatch.map))
/*     */     {
/*  45 */       return new PatchedDataComponentMap(paramDataComponentMap, paramDataComponentPatch.map, true);
/*     */     }
/*     */     
/*  48 */     PatchedDataComponentMap patchedDataComponentMap = new PatchedDataComponentMap(paramDataComponentMap);
/*  49 */     patchedDataComponentMap.applyPatch(paramDataComponentPatch);
/*  50 */     return patchedDataComponentMap;
/*     */   }
/*     */   
/*     */   private static boolean isPatchSanitized(DataComponentMap paramDataComponentMap, Reference2ObjectMap<DataComponentType<?>, Optional<?>> paramReference2ObjectMap) {
/*  54 */     for (ObjectIterator<Map.Entry> objectIterator = Reference2ObjectMaps.fastIterable(paramReference2ObjectMap).iterator(); objectIterator.hasNext(); ) { Map.Entry entry = objectIterator.next();
/*  55 */       Object object = paramDataComponentMap.get((DataComponentType<? extends Object>)entry.getKey());
/*  56 */       Optional<T> optional = (Optional)entry.getValue();
/*  57 */       if (optional.isPresent() && optional.get().equals(object))
/*  58 */         return false; 
/*  59 */       if (optional.isEmpty() && object == null) {
/*  60 */         return false;
/*     */       } }
/*     */     
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/*  72 */     Optional<T> optional = (Optional)this.patch.get(paramDataComponentType);
/*  73 */     if (optional != null) {
/*  74 */       return optional.orElse(null);
/*     */     }
/*  76 */     return this.prototype.get(paramDataComponentType);
/*     */   }
/*     */   
/*     */   public boolean hasNonDefault(DataComponentType<?> paramDataComponentType) {
/*  80 */     return this.patch.containsKey(paramDataComponentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T set(DataComponentType<T> paramDataComponentType, T paramT) {
/*     */     Optional<T> optional;
/*  97 */     ensureMapOwnership();
/*  98 */     T t = (T)this.prototype.get((DataComponentType)paramDataComponentType);
/*     */     
/* 100 */     if (Objects.equals(paramT, t)) {
/* 101 */       optional = (Optional)this.patch.remove(paramDataComponentType);
/*     */     } else {
/* 103 */       optional = (Optional)this.patch.put(paramDataComponentType, Optional.ofNullable(paramT));
/*     */     } 
/* 105 */     if (optional != null) {
/* 106 */       return optional.orElse(t);
/*     */     }
/* 108 */     return t;
/*     */   }
/*     */   
/*     */   public <T> T set(TypedDataComponent<T> paramTypedDataComponent) {
/* 112 */     return set(paramTypedDataComponent.type(), paramTypedDataComponent.value());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T remove(DataComponentType<? extends T> paramDataComponentType) {
/*     */     Optional<T> optional;
/* 124 */     ensureMapOwnership();
/* 125 */     T t = (T)this.prototype.get((DataComponentType)paramDataComponentType);
/*     */     
/* 127 */     if (t != null) {
/* 128 */       optional = (Optional)this.patch.put(paramDataComponentType, Optional.empty());
/*     */     } else {
/* 130 */       optional = (Optional)this.patch.remove(paramDataComponentType);
/*     */     } 
/* 132 */     if (optional != null) {
/* 133 */       return optional.orElse(null);
/*     */     }
/* 135 */     return t;
/*     */   }
/*     */   
/*     */   public void applyPatch(DataComponentPatch paramDataComponentPatch) {
/* 139 */     ensureMapOwnership();
/* 140 */     for (ObjectIterator<Map.Entry> objectIterator = Reference2ObjectMaps.fastIterable(paramDataComponentPatch.map).iterator(); objectIterator.hasNext(); ) { Map.Entry entry = objectIterator.next();
/* 141 */       applyPatch((DataComponentType)entry.getKey(), (Optional)entry.getValue()); }
/*     */   
/*     */   }
/*     */   
/*     */   private void applyPatch(DataComponentType<?> paramDataComponentType, Optional<?> paramOptional) {
/* 146 */     Object object = this.prototype.get((DataComponentType)paramDataComponentType);
/* 147 */     if (paramOptional.isPresent()) {
/* 148 */       if (paramOptional.get().equals(object)) {
/* 149 */         this.patch.remove(paramDataComponentType);
/*     */       } else {
/* 151 */         this.patch.put(paramDataComponentType, paramOptional);
/*     */       }
/*     */     
/* 154 */     } else if (object != null) {
/* 155 */       this.patch.put(paramDataComponentType, Optional.empty());
/*     */     } else {
/* 157 */       this.patch.remove(paramDataComponentType);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void restorePatch(DataComponentPatch paramDataComponentPatch) {
/* 163 */     ensureMapOwnership();
/* 164 */     this.patch.clear();
/* 165 */     this.patch.putAll((Map)paramDataComponentPatch.map);
/*     */   }
/*     */   
/*     */   public void clearPatch() {
/* 169 */     ensureMapOwnership();
/* 170 */     this.patch.clear();
/*     */   }
/*     */   
/*     */   public void setAll(DataComponentMap paramDataComponentMap) {
/* 174 */     for (TypedDataComponent typedDataComponent : paramDataComponentMap) {
/* 175 */       typedDataComponent.applyTo(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureMapOwnership() {
/* 180 */     if (this.copyOnWrite) {
/* 181 */       this.patch = (Reference2ObjectMap<DataComponentType<?>, Optional<?>>)new Reference2ObjectArrayMap(this.patch);
/* 182 */       this.copyOnWrite = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<DataComponentType<?>> keySet() {
/* 191 */     if (this.patch.isEmpty()) {
/* 192 */       return this.prototype.keySet();
/*     */     }
/* 194 */     ReferenceArraySet<DataComponentType> referenceArraySet = new ReferenceArraySet(this.prototype.keySet());
/* 195 */     for (ObjectIterator<Reference2ObjectMap.Entry> objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry entry = objectIterator.next();
/* 196 */       Optional optional = (Optional)entry.getValue();
/* 197 */       if (optional.isPresent()) {
/* 198 */         referenceArraySet.add((DataComponentType)entry.getKey()); continue;
/*     */       } 
/* 200 */       referenceArraySet.remove(entry.getKey()); }
/*     */ 
/*     */     
/* 203 */     return (Set)referenceArraySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<TypedDataComponent<?>> iterator() {
/* 208 */     if (this.patch.isEmpty()) {
/* 209 */       return this.prototype.iterator();
/*     */     }
/* 211 */     ArrayList<TypedDataComponent> arrayList = new ArrayList(this.patch.size() + this.prototype.size());
/* 212 */     for (ObjectIterator<Reference2ObjectMap.Entry> objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry entry = objectIterator.next();
/* 213 */       if (((Optional)entry.getValue()).isPresent()) {
/* 214 */         arrayList.add(TypedDataComponent.createUnchecked((DataComponentType)entry.getKey(), ((Optional)entry.getValue()).get()));
/*     */       } }
/*     */     
/* 217 */     for (TypedDataComponent typedDataComponent : this.prototype) {
/* 218 */       if (!this.patch.containsKey(typedDataComponent.type())) {
/* 219 */         arrayList.add(typedDataComponent);
/*     */       }
/*     */     } 
/* 222 */     return (Iterator)arrayList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 227 */     int i = this.prototype.size();
/* 228 */     for (ObjectIterator<Reference2ObjectMap.Entry> objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry entry = objectIterator.next();
/* 229 */       boolean bool1 = ((Optional)entry.getValue()).isPresent();
/* 230 */       boolean bool2 = this.prototype.has((DataComponentType)entry.getKey());
/* 231 */       if (bool1 != bool2) {
/* 232 */         i += bool1 ? 1 : -1;
/*     */       } }
/*     */     
/* 235 */     return i;
/*     */   }
/*     */   
/*     */   public DataComponentPatch asPatch() {
/* 239 */     if (this.patch.isEmpty()) {
/* 240 */       return DataComponentPatch.EMPTY;
/*     */     }
/* 242 */     this.copyOnWrite = true;
/* 243 */     return new DataComponentPatch(this.patch);
/*     */   }
/*     */   
/*     */   public PatchedDataComponentMap copy() {
/* 247 */     this.copyOnWrite = true;
/* 248 */     return new PatchedDataComponentMap(this.prototype, this.patch, true);
/*     */   }
/*     */   
/*     */   public DataComponentMap toImmutableMap() {
/* 252 */     if (this.patch.isEmpty()) {
/* 253 */       return this.prototype;
/*     */     }
/* 255 */     return copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 260 */     if (this == paramObject) {
/* 261 */       return true;
/*     */     }
/* 263 */     if (paramObject instanceof PatchedDataComponentMap) { PatchedDataComponentMap patchedDataComponentMap = (PatchedDataComponentMap)paramObject; if (this.prototype.equals(patchedDataComponentMap.prototype) && this.patch.equals(patchedDataComponentMap.patch)); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 268 */     return this.prototype.hashCode() + this.patch.hashCode() * 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 273 */     return "{" + (String)stream().<CharSequence>map(TypedDataComponent::toString).collect(Collectors.joining(", ")) + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\PatchedDataComponentMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */