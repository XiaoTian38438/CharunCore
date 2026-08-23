/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public class PackRepository {
/*     */   private final Set<RepositorySource> sources;
/*  23 */   private Map<String, Pack> available = (Map<String, Pack>)ImmutableMap.of();
/*  24 */   private List<Pack> selected = (List<Pack>)ImmutableList.of();
/*     */   
/*     */   public PackRepository(RepositorySource... paramVarArgs) {
/*  27 */     this.sources = (Set<RepositorySource>)ImmutableSet.copyOf((Object[])paramVarArgs);
/*     */   }
/*     */   
/*     */   public static String displayPackList(Collection<Pack> paramCollection) {
/*  31 */     return paramCollection.stream().map(paramPack -> paramPack.getId() + paramPack.getId()).collect(Collectors.joining(", "));
/*     */   }
/*     */   
/*     */   public void reload() {
/*  35 */     List<String> list = (List)this.selected.stream().map(Pack::getId).collect(ImmutableList.toImmutableList());
/*  36 */     this.available = discoverAvailable();
/*  37 */     this.selected = rebuildSelected(list);
/*     */   }
/*     */   
/*     */   private Map<String, Pack> discoverAvailable() {
/*  41 */     TreeMap treeMap = Maps.newTreeMap();
/*  42 */     for (RepositorySource repositorySource : this.sources) {
/*  43 */       repositorySource.loadPacks(paramPack -> paramMap.put(paramPack.getId(), paramPack));
/*     */     }
/*  45 */     return (Map<String, Pack>)ImmutableMap.copyOf(treeMap);
/*     */   }
/*     */   
/*     */   public boolean isAbleToClearAnyPack() {
/*  49 */     List<Pack> list = rebuildSelected(List.of());
/*  50 */     return !this.selected.equals(list);
/*     */   }
/*     */   
/*     */   public void setSelected(Collection<String> paramCollection) {
/*  54 */     this.selected = rebuildSelected(paramCollection);
/*     */   }
/*     */   
/*     */   public boolean addPack(String paramString) {
/*  58 */     Pack pack = this.available.get(paramString);
/*  59 */     if (pack != null && !this.selected.contains(pack)) {
/*  60 */       ArrayList<Pack> arrayList = Lists.newArrayList(this.selected);
/*  61 */       arrayList.add(pack);
/*  62 */       this.selected = arrayList;
/*  63 */       return true;
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removePack(String paramString) {
/*  69 */     Pack pack = this.available.get(paramString);
/*  70 */     if (pack != null && this.selected.contains(pack)) {
/*  71 */       ArrayList<Pack> arrayList = Lists.newArrayList(this.selected);
/*  72 */       arrayList.remove(pack);
/*  73 */       this.selected = arrayList;
/*  74 */       return true;
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   private List<Pack> rebuildSelected(Collection<String> paramCollection) {
/*  80 */     List<Pack> list = getAvailablePacks(paramCollection).collect(Util.toMutableList());
/*     */     
/*  82 */     for (Pack pack : this.available.values()) {
/*     */       
/*  84 */       if (pack.isRequired() && !list.contains(pack)) {
/*  85 */         pack.getDefaultPosition().insert(list, pack, Pack::selectionConfig, false);
/*     */       }
/*     */     } 
/*  88 */     return (List<Pack>)ImmutableList.copyOf(list);
/*     */   }
/*     */   
/*     */   private Stream<Pack> getAvailablePacks(Collection<String> paramCollection) {
/*  92 */     Objects.requireNonNull(this.available); return paramCollection.stream().map(this.available::get).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   public Collection<String> getAvailableIds() {
/*  96 */     return this.available.keySet();
/*     */   }
/*     */   
/*     */   public Collection<Pack> getAvailablePacks() {
/* 100 */     return this.available.values();
/*     */   }
/*     */   
/*     */   public Collection<String> getSelectedIds() {
/* 104 */     return (Collection<String>)this.selected.stream().map(Pack::getId).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*     */   
/*     */   public FeatureFlagSet getRequestedFeatureFlags() {
/* 108 */     return getSelectedPacks().stream().map(Pack::getRequestedFeatures).reduce(FeatureFlagSet::join).orElse(FeatureFlagSet.of());
/*     */   }
/*     */   
/*     */   public Collection<Pack> getSelectedPacks() {
/* 112 */     return this.selected;
/*     */   }
/*     */   
/*     */   public Pack getPack(String paramString) {
/* 116 */     return this.available.get(paramString);
/*     */   }
/*     */   
/*     */   public boolean isAvailable(String paramString) {
/* 120 */     return this.available.containsKey(paramString);
/*     */   }
/*     */   
/*     */   public List<PackResources> openAllSelected() {
/* 124 */     return (List<PackResources>)this.selected.stream().map(Pack::open).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\repository\PackRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */