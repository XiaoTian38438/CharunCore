/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public class WolfVariants {
/* 17 */   public static final ResourceKey<WolfVariant> PALE = createKey("pale");
/* 18 */   public static final ResourceKey<WolfVariant> SPOTTED = createKey("spotted");
/* 19 */   public static final ResourceKey<WolfVariant> SNOWY = createKey("snowy");
/* 20 */   public static final ResourceKey<WolfVariant> BLACK = createKey("black");
/* 21 */   public static final ResourceKey<WolfVariant> ASHEN = createKey("ashen");
/* 22 */   public static final ResourceKey<WolfVariant> RUSTY = createKey("rusty");
/* 23 */   public static final ResourceKey<WolfVariant> WOODS = createKey("woods");
/* 24 */   public static final ResourceKey<WolfVariant> CHESTNUT = createKey("chestnut");
/* 25 */   public static final ResourceKey<WolfVariant> STRIPED = createKey("striped");
/*    */   
/* 27 */   public static final ResourceKey<WolfVariant> DEFAULT = PALE;
/*    */   
/*    */   private static ResourceKey<WolfVariant> createKey(String paramString) {
/* 30 */     return ResourceKey.create(Registries.WOLF_VARIANT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> paramBootstrapContext, ResourceKey<WolfVariant> paramResourceKey, String paramString, ResourceKey<Biome> paramResourceKey1) {
/* 34 */     register(paramBootstrapContext, paramResourceKey, paramString, highPrioBiome((HolderSet<Biome>)HolderSet.direct(new Holder[] { (Holder)paramBootstrapContext.lookup(Registries.BIOME).getOrThrow(paramResourceKey1) })));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> paramBootstrapContext, ResourceKey<WolfVariant> paramResourceKey, String paramString, TagKey<Biome> paramTagKey) {
/* 38 */     register(paramBootstrapContext, paramResourceKey, paramString, highPrioBiome((HolderSet<Biome>)paramBootstrapContext.lookup(Registries.BIOME).getOrThrow(paramTagKey)));
/*    */   }
/*    */   
/*    */   private static SpawnPrioritySelectors highPrioBiome(HolderSet<Biome> paramHolderSet) {
/* 42 */     return SpawnPrioritySelectors.single((SpawnCondition)new BiomeCheck(paramHolderSet), 1);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> paramBootstrapContext, ResourceKey<WolfVariant> paramResourceKey, String paramString, SpawnPrioritySelectors paramSpawnPrioritySelectors) {
/* 46 */     Identifier identifier1 = Identifier.withDefaultNamespace("entity/wolf/" + paramString);
/* 47 */     Identifier identifier2 = Identifier.withDefaultNamespace("entity/wolf/" + paramString + "_tame");
/* 48 */     Identifier identifier3 = Identifier.withDefaultNamespace("entity/wolf/" + paramString + "_angry");
/* 49 */     paramBootstrapContext.register(paramResourceKey, new WolfVariant(new WolfVariant.AssetInfo(new ClientAsset.ResourceTexture(identifier1), new ClientAsset.ResourceTexture(identifier2), new ClientAsset.ResourceTexture(identifier3)), paramSpawnPrioritySelectors));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<WolfVariant> paramBootstrapContext) {
/* 60 */     register(paramBootstrapContext, PALE, "wolf", SpawnPrioritySelectors.fallback(0));
/* 61 */     register(paramBootstrapContext, SPOTTED, "wolf_spotted", BiomeTags.IS_SAVANNA);
/* 62 */     register(paramBootstrapContext, SNOWY, "wolf_snowy", Biomes.GROVE);
/* 63 */     register(paramBootstrapContext, BLACK, "wolf_black", Biomes.OLD_GROWTH_PINE_TAIGA);
/* 64 */     register(paramBootstrapContext, ASHEN, "wolf_ashen", Biomes.SNOWY_TAIGA);
/* 65 */     register(paramBootstrapContext, RUSTY, "wolf_rusty", BiomeTags.IS_JUNGLE);
/* 66 */     register(paramBootstrapContext, WOODS, "wolf_woods", Biomes.FOREST);
/* 67 */     register(paramBootstrapContext, CHESTNUT, "wolf_chestnut", Biomes.OLD_GROWTH_SPRUCE_TAIGA);
/* 68 */     register(paramBootstrapContext, STRIPED, "wolf_striped", BiomeTags.IS_BADLANDS);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\wolf\WolfVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */