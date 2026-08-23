/*    */ package net.minecraft.world.entity.animal.frog;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.animal.TemperatureVariants;
/*    */ import net.minecraft.world.entity.variant.BiomeCheck;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public interface FrogVariants {
/* 17 */   public static final ResourceKey<FrogVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<FrogVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<FrogVariant> COLD = createKey(TemperatureVariants.COLD);
/*    */   
/*    */   private static ResourceKey<FrogVariant> createKey(Identifier paramIdentifier) {
/* 22 */     return ResourceKey.create(Registries.FROG_VARIANT, paramIdentifier);
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<FrogVariant> paramBootstrapContext) {
/* 26 */     register(paramBootstrapContext, TEMPERATE, "entity/frog/temperate_frog", SpawnPrioritySelectors.fallback(0));
/* 27 */     register(paramBootstrapContext, WARM, "entity/frog/warm_frog", BiomeTags.SPAWNS_WARM_VARIANT_FROGS);
/* 28 */     register(paramBootstrapContext, COLD, "entity/frog/cold_frog", BiomeTags.SPAWNS_COLD_VARIANT_FROGS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<FrogVariant> paramBootstrapContext, ResourceKey<FrogVariant> paramResourceKey, String paramString, TagKey<Biome> paramTagKey) {
/* 32 */     HolderSet.Named named = paramBootstrapContext.lookup(Registries.BIOME).getOrThrow(paramTagKey);
/* 33 */     register(paramBootstrapContext, paramResourceKey, paramString, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<FrogVariant> paramBootstrapContext, ResourceKey<FrogVariant> paramResourceKey, String paramString, SpawnPrioritySelectors paramSpawnPrioritySelectors) {
/* 37 */     paramBootstrapContext.register(paramResourceKey, new FrogVariant(new ClientAsset.ResourceTexture(
/* 38 */             Identifier.withDefaultNamespace(paramString)), paramSpawnPrioritySelectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\frog\FrogVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */