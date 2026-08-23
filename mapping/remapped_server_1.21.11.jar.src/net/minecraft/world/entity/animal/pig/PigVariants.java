/*    */ package net.minecraft.world.entity.animal.pig;
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
/*    */ public class PigVariants {
/* 17 */   public static final ResourceKey<PigVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<PigVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<PigVariant> COLD = createKey(TemperatureVariants.COLD);
/* 20 */   public static final ResourceKey<PigVariant> DEFAULT = TEMPERATE;
/*    */   
/*    */   private static ResourceKey<PigVariant> createKey(Identifier paramIdentifier) {
/* 23 */     return ResourceKey.create(Registries.PIG_VARIANT, paramIdentifier);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PigVariant> paramBootstrapContext) {
/* 27 */     register(paramBootstrapContext, TEMPERATE, PigVariant.ModelType.NORMAL, "temperate_pig", SpawnPrioritySelectors.fallback(0));
/* 28 */     register(paramBootstrapContext, WARM, PigVariant.ModelType.NORMAL, "warm_pig", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
/* 29 */     register(paramBootstrapContext, COLD, PigVariant.ModelType.COLD, "cold_pig", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigVariant> paramBootstrapContext, ResourceKey<PigVariant> paramResourceKey, PigVariant.ModelType paramModelType, String paramString, TagKey<Biome> paramTagKey) {
/* 33 */     HolderSet.Named named = paramBootstrapContext.lookup(Registries.BIOME).getOrThrow(paramTagKey);
/* 34 */     register(paramBootstrapContext, paramResourceKey, paramModelType, paramString, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigVariant> paramBootstrapContext, ResourceKey<PigVariant> paramResourceKey, PigVariant.ModelType paramModelType, String paramString, SpawnPrioritySelectors paramSpawnPrioritySelectors) {
/* 38 */     Identifier identifier = Identifier.withDefaultNamespace("entity/pig/" + paramString);
/* 39 */     paramBootstrapContext.register(paramResourceKey, new PigVariant(new ModelAndTexture(paramModelType, identifier), paramSpawnPrioritySelectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\pig\PigVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */