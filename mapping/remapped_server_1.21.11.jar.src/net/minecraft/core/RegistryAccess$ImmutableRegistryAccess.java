/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImmutableRegistryAccess
/*    */   implements RegistryAccess
/*    */ {
/*    */   private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries;
/*    */   
/*    */   public ImmutableRegistryAccess(List<? extends Registry<?>> paramList) {
/* 57 */     this.registries = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>)paramList.stream().collect(Collectors.toUnmodifiableMap(Registry::key, paramRegistry -> paramRegistry));
/*    */   }
/*    */   
/*    */   public ImmutableRegistryAccess(Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> paramMap) {
/* 61 */     this.registries = Map.copyOf(paramMap);
/*    */   }
/*    */   
/*    */   public ImmutableRegistryAccess(Stream<RegistryAccess.RegistryEntry<?>> paramStream) {
/* 65 */     this.registries = paramStream.collect(ImmutableMap.toImmutableMap(RegistryAccess.RegistryEntry::key, RegistryAccess.RegistryEntry::value));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <E> Optional<Registry<E>> lookup(ResourceKey<? extends Registry<? extends E>> paramResourceKey) {
/* 71 */     return Optional.<Registry>ofNullable(this.registries.get(paramResourceKey)).map(paramRegistry -> paramRegistry);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<RegistryAccess.RegistryEntry<?>> registries() {
/* 76 */     return this.registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistryAccess$ImmutableRegistryAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */