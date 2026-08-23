/*    */ package net.minecraft.world.entity.animal.nautilus;
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
/*    */ public class ZombieNautilusVariants {
/* 17 */   public static final ResourceKey<ZombieNautilusVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
/* 18 */   public static final ResourceKey<ZombieNautilusVariant> WARM = createKey(TemperatureVariants.WARM);
/* 19 */   public static final ResourceKey<ZombieNautilusVariant> DEFAULT = TEMPERATE;
/*    */   
/*    */   private static ResourceKey<ZombieNautilusVariant> createKey(Identifier paramIdentifier) {
/* 22 */     return ResourceKey.create(Registries.ZOMBIE_NAUTILUS_VARIANT, paramIdentifier);
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ZombieNautilusVariant> paramBootstrapContext) {
/* 26 */     register(paramBootstrapContext, TEMPERATE, ZombieNautilusVariant.ModelType.NORMAL, "zombie_nautilus", SpawnPrioritySelectors.fallback(0));
/* 27 */     register(paramBootstrapContext, WARM, ZombieNautilusVariant.ModelType.WARM, "zombie_nautilus_coral", BiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ZombieNautilusVariant> paramBootstrapContext, ResourceKey<ZombieNautilusVariant> paramResourceKey, ZombieNautilusVariant.ModelType paramModelType, String paramString, TagKey<Biome> paramTagKey) {
/* 31 */     HolderSet.Named named = paramBootstrapContext.lookup(Registries.BIOME).getOrThrow(paramTagKey);
/* 32 */     register(paramBootstrapContext, paramResourceKey, paramModelType, paramString, SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck((HolderSet)named), 1));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ZombieNautilusVariant> paramBootstrapContext, ResourceKey<ZombieNautilusVariant> paramResourceKey, ZombieNautilusVariant.ModelType paramModelType, String paramString, SpawnPrioritySelectors paramSpawnPrioritySelectors) {
/* 36 */     Identifier identifier = Identifier.withDefaultNamespace("entity/nautilus/" + paramString);
/* 37 */     paramBootstrapContext.register(paramResourceKey, new ZombieNautilusVariant(new ModelAndTexture(paramModelType, identifier), paramSpawnPrioritySelectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */