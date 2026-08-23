/*    */ package net.minecraft.data.registries;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.Cloner;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistrySetBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RegistryPatchGenerator {
/*    */   public static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(CompletableFuture<HolderLookup.Provider> paramCompletableFuture, RegistrySetBuilder paramRegistrySetBuilder) {
/* 19 */     return paramCompletableFuture.thenApply(paramProvider -> {
/*    */           RegistryAccess.Frozen frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */           Cloner.Factory factory = new Cloner.Factory();
/*    */           RegistryDataLoader.WORLDGEN_REGISTRIES.forEach(());
/*    */           RegistrySetBuilder.PatchedRegistries patchedRegistries = paramRegistrySetBuilder.buildPatch((RegistryAccess)frozen, paramProvider, factory);
/*    */           HolderLookup.Provider provider = patchedRegistries.full();
/*    */           Optional optional1 = provider.lookup(Registries.BIOME);
/*    */           Optional optional2 = provider.lookup(Registries.PLACED_FEATURE);
/*    */           if (optional1.isPresent() || optional2.isPresent())
/*    */             VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter((HolderGetter<PlacedFeature>)DataFixUtils.orElseGet(optional2, ()), (HolderLookup<Biome>)DataFixUtils.orElseGet(optional1, ())); 
/*    */           return patchedRegistries;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\registries\RegistryPatchGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */