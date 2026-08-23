/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
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
/*    */ class null
/*    */   implements RegistryAccess.Frozen
/*    */ {
/*    */   public <T> Optional<Registry<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey) {
/* 85 */     Registry<? extends Registry<? extends T>> registry = registries;
/* 86 */     return (Optional)registry.getOptional(paramResourceKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<RegistryAccess.RegistryEntry<?>> registries() {
/* 91 */     return registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   public RegistryAccess.Frozen freeze() {
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistryAccess$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */