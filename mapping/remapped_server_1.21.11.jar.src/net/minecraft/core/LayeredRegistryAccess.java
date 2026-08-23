/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayeredRegistryAccess<T>
/*     */ {
/*     */   private final List<T> keys;
/*     */   private final List<RegistryAccess.Frozen> values;
/*     */   private final RegistryAccess.Frozen composite;
/*     */   
/*     */   public LayeredRegistryAccess(List<T> paramList) {
/*  23 */     this(paramList, 
/*     */         
/*  25 */         (List<RegistryAccess.Frozen>)Util.make(() -> {
/*     */             RegistryAccess.Frozen[] arrayOfFrozen = new RegistryAccess.Frozen[paramList.size()];
/*     */             Arrays.fill((Object[])arrayOfFrozen, RegistryAccess.EMPTY);
/*     */             return Arrays.asList(arrayOfFrozen);
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   private LayeredRegistryAccess(List<T> paramList, List<RegistryAccess.Frozen> paramList1) {
/*  34 */     this.keys = List.copyOf(paramList);
/*  35 */     this.values = List.copyOf(paramList1);
/*  36 */     this.composite = (new RegistryAccess.ImmutableRegistryAccess(collectRegistries(paramList1.stream()))).freeze();
/*     */   }
/*     */   
/*     */   private int getLayerIndexOrThrow(T paramT) {
/*  40 */     int i = this.keys.indexOf(paramT);
/*  41 */     if (i == -1) {
/*  42 */       throw new IllegalStateException("Can't find " + String.valueOf(paramT) + " inside " + String.valueOf(this.keys));
/*     */     }
/*  44 */     return i;
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getLayer(T paramT) {
/*  48 */     int i = getLayerIndexOrThrow(paramT);
/*  49 */     return this.values.get(i);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessForLoading(T paramT) {
/*  53 */     int i = getLayerIndexOrThrow(paramT);
/*  54 */     return getCompositeAccessForLayers(0, i);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessFrom(T paramT) {
/*  58 */     int i = getLayerIndexOrThrow(paramT);
/*  59 */     return getCompositeAccessForLayers(i, this.values.size());
/*     */   }
/*     */   
/*     */   private RegistryAccess.Frozen getCompositeAccessForLayers(int paramInt1, int paramInt2) {
/*  63 */     return (new RegistryAccess.ImmutableRegistryAccess(collectRegistries(this.values.subList(paramInt1, paramInt2).stream()))).freeze();
/*     */   }
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T paramT, RegistryAccess.Frozen... paramVarArgs) {
/*  67 */     return replaceFrom(paramT, Arrays.asList(paramVarArgs));
/*     */   }
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T paramT, List<RegistryAccess.Frozen> paramList) {
/*  71 */     int i = getLayerIndexOrThrow(paramT);
/*     */     
/*  73 */     if (paramList.size() > this.values.size() - i) {
/*  74 */       throw new IllegalStateException("Too many values to replace");
/*     */     }
/*     */     
/*  77 */     ArrayList<RegistryAccess.Frozen> arrayList = new ArrayList();
/*     */     
/*  79 */     for (byte b = 0; b < i; b++) {
/*  80 */       arrayList.add(this.values.get(b));
/*     */     }
/*     */     
/*  83 */     arrayList.addAll(paramList);
/*     */     
/*  85 */     while (arrayList.size() < this.values.size()) {
/*  86 */       arrayList.add(RegistryAccess.EMPTY);
/*     */     }
/*  88 */     return new LayeredRegistryAccess(this.keys, arrayList);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen compositeAccess() {
/*  92 */     return this.composite;
/*     */   }
/*     */   
/*     */   private static Map<ResourceKey<? extends Registry<?>>, Registry<?>> collectRegistries(Stream<? extends RegistryAccess> paramStream) {
/*  96 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  98 */     paramStream.forEach(paramRegistryAccess -> paramRegistryAccess.registries().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     return (Map)hashMap;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\LayeredRegistryAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */