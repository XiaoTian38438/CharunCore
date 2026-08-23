/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements CommandBuildContext
/*    */ {
/*    */   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 16 */     return access.listRegistryKeys();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey) {
/* 21 */     return access.lookup(paramResourceKey).map(paramRegistryLookup -> paramRegistryLookup.filterFeatures(paramFeatureFlagSet));
/*    */   }
/*    */ 
/*    */   
/*    */   public FeatureFlagSet enabledFeatures() {
/* 26 */     return enabledFeatures;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\CommandBuildContext$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */