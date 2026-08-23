/*    */ package net.minecraft.world.entity.animal.chicken;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.animal.TemperatureVariants;
/*    */ import net.minecraft.world.entity.variant.BiomeCheck;
/*    */ import net.minecraft.world.entity.variant.ModelAndTexture;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class ChickenVariants {
/* 17 */   public static final ResourceKey<ChickenVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<ChickenVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<ChickenVariant> COLD = createKey(TemperatureVariants.COLD);
/* 20 */   public static final ResourceKey<ChickenVariant> DEFAULT = TEMPERATE;
/*    */   
/*    */   private static ResourceKey<ChickenVariant> createKey(Identifier paramIdentifier) {
/* 23 */     return ResourceKey.create(Registries.CHICKEN_VARIANT, paramIdentifier);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ChickenVariant> paramBootstrapContext) {
/* 27 */     register(paramBootstrapContext, TEMPERATE, ChickenVariant.ModelType.NORMAL, "temperate_chicken", SpawnPrioritySelectors.fallback(0));
/* 28 */     register(paramBootstrapContext, WARM, ChickenVariant.ModelType.NORMAL, "warm_chicken", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 29 */     register(paramBootstrapContext, COLD, ChickenVariant.ModelType.COLD, "cold_chicken", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenVariant> paramBootstrapContext, ResourceKey<ChickenVariant> paramResourceKey, ChickenVariant.ModelType paramModelType, String paramString, TagKey<Biome> paramTagKey) {
/* 33 */     HolderSet.Named named = paramBootstrapContext.lookup(Registries.BIOME).getOrThrow(paramTagKey);
/* 34 */     register(paramBootstrapContext, paramResourceKey, paramModelType, paramString, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenVariant> paramBootstrapContext, ResourceKey<ChickenVariant> paramResourceKey, ChickenVariant.ModelType paramModelType, String paramString, SpawnPrioritySelectors paramSpawnPrioritySelectors) {
/* 38 */     Identifier identifier = Identifier.withDefaultNamespace("entity/chicken/" + paramString);
/* 39 */     paramBootstrapContext.register(paramResourceKey, new ChickenVariant(new ModelAndTexture(paramModelType, identifier), paramSpawnPrioritySelectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\chicken\ChickenVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */