/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
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
/*     */ final class HolderLookupAdapter
/*     */   implements RegistryOps.RegistryInfoLookup
/*     */ {
/*     */   private final HolderLookup.Provider lookupProvider;
/* 113 */   private final Map<ResourceKey<? extends Registry<?>>, Optional<? extends RegistryOps.RegistryInfo<?>>> lookups = new ConcurrentHashMap<>();
/*     */   
/*     */   public HolderLookupAdapter(HolderLookup.Provider paramProvider) {
/* 116 */     this.lookupProvider = paramProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> Optional<RegistryOps.RegistryInfo<E>> lookup(ResourceKey<? extends Registry<? extends E>> paramResourceKey) {
/* 122 */     return (Optional<RegistryOps.RegistryInfo<E>>)this.lookups.computeIfAbsent(paramResourceKey, this::createLookup);
/*     */   }
/*     */   
/*     */   private Optional<RegistryOps.RegistryInfo<Object>> createLookup(ResourceKey<? extends Registry<?>> paramResourceKey) {
/* 126 */     return this.lookupProvider.lookup(paramResourceKey).map(RegistryOps.RegistryInfo::fromRegistryLookup);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 131 */     if (this == paramObject) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (paramObject instanceof HolderLookupAdapter) { HolderLookupAdapter holderLookupAdapter = (HolderLookupAdapter)paramObject; if (this.lookupProvider.equals(holderLookupAdapter.lookupProvider)); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 139 */     return this.lookupProvider.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\resources\RegistryOps$HolderLookupAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */