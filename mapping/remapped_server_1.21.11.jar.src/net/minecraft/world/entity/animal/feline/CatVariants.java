/*    */ package net.minecraft.world.entity.animal.feline;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.StructureTags;
/*    */ import net.minecraft.world.entity.variant.MoonBrightnessCheck;
/*    */ import net.minecraft.world.entity.variant.PriorityProvider;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.entity.variant.StructureCheck;
/*    */ 
/*    */ public interface CatVariants
/*    */ {
/* 20 */   public static final ResourceKey<CatVariant> TABBY = createKey("tabby");
/* 21 */   public static final ResourceKey<CatVariant> BLACK = createKey("black");
/* 22 */   public static final ResourceKey<CatVariant> RED = createKey("red");
/* 23 */   public static final ResourceKey<CatVariant> SIAMESE = createKey("siamese");
/* 24 */   public static final ResourceKey<CatVariant> BRITISH_SHORTHAIR = createKey("british_shorthair");
/* 25 */   public static final ResourceKey<CatVariant> CALICO = createKey("calico");
/* 26 */   public static final ResourceKey<CatVariant> PERSIAN = createKey("persian");
/* 27 */   public static final ResourceKey<CatVariant> RAGDOLL = createKey("ragdoll");
/* 28 */   public static final ResourceKey<CatVariant> WHITE = createKey("white");
/* 29 */   public static final ResourceKey<CatVariant> JELLIE = createKey("jellie");
/* 30 */   public static final ResourceKey<CatVariant> ALL_BLACK = createKey("all_black");
/*    */   
/*    */   private static ResourceKey<CatVariant> createKey(String paramString) {
/* 33 */     return ResourceKey.create(Registries.CAT_VARIANT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<CatVariant> paramBootstrapContext) {
/* 37 */     HolderGetter holderGetter = paramBootstrapContext.lookup(Registries.STRUCTURE);
/*    */     
/* 39 */     registerForAnyConditions(paramBootstrapContext, TABBY, "entity/cat/tabby");
/* 40 */     registerForAnyConditions(paramBootstrapContext, BLACK, "entity/cat/black");
/* 41 */     registerForAnyConditions(paramBootstrapContext, RED, "entity/cat/red");
/* 42 */     registerForAnyConditions(paramBootstrapContext, SIAMESE, "entity/cat/siamese");
/* 43 */     registerForAnyConditions(paramBootstrapContext, BRITISH_SHORTHAIR, "entity/cat/british_shorthair");
/* 44 */     registerForAnyConditions(paramBootstrapContext, CALICO, "entity/cat/calico");
/* 45 */     registerForAnyConditions(paramBootstrapContext, PERSIAN, "entity/cat/persian");
/* 46 */     registerForAnyConditions(paramBootstrapContext, RAGDOLL, "entity/cat/ragdoll");
/* 47 */     registerForAnyConditions(paramBootstrapContext, WHITE, "entity/cat/white");
/* 48 */     registerForAnyConditions(paramBootstrapContext, JELLIE, "entity/cat/jellie");
/*    */     
/* 50 */     register(paramBootstrapContext, ALL_BLACK, "entity/cat/all_black", new SpawnPrioritySelectors(
/* 51 */           List.of(new PriorityProvider.Selector((PriorityProvider.SelectorCondition)new StructureCheck((HolderSet)holderGetter
/*    */                 
/* 53 */                 .getOrThrow(StructureTags.CATS_SPAWN_AS_BLACK)), 1), new PriorityProvider.Selector((PriorityProvider.SelectorCondition)new MoonBrightnessCheck(
/*    */                 
/* 55 */                 MinMaxBounds.Doubles.atLeast(0.9D)), 0))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static void registerForAnyConditions(BootstrapContext<CatVariant> paramBootstrapContext, ResourceKey<CatVariant> paramResourceKey, String paramString) {
/* 61 */     register(paramBootstrapContext, paramResourceKey, paramString, SpawnPrioritySelectors.fallback(0));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CatVariant> paramBootstrapContext, ResourceKey<CatVariant> paramResourceKey, String paramString, SpawnPrioritySelectors paramSpawnPrioritySelectors) {
/* 65 */     paramBootstrapContext.register(paramResourceKey, new CatVariant(new ClientAsset.ResourceTexture(
/* 66 */             Identifier.withDefaultNamespace(paramString)), paramSpawnPrioritySelectors));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\feline\CatVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */