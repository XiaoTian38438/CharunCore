/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ProvidesTrimMaterial;
/*    */ 
/*    */ public class TrimMaterials
/*    */ {
/* 20 */   public static final ResourceKey<TrimMaterial> QUARTZ = registryKey("quartz");
/* 21 */   public static final ResourceKey<TrimMaterial> IRON = registryKey("iron");
/* 22 */   public static final ResourceKey<TrimMaterial> NETHERITE = registryKey("netherite");
/* 23 */   public static final ResourceKey<TrimMaterial> REDSTONE = registryKey("redstone");
/* 24 */   public static final ResourceKey<TrimMaterial> COPPER = registryKey("copper");
/* 25 */   public static final ResourceKey<TrimMaterial> GOLD = registryKey("gold");
/* 26 */   public static final ResourceKey<TrimMaterial> EMERALD = registryKey("emerald");
/* 27 */   public static final ResourceKey<TrimMaterial> DIAMOND = registryKey("diamond");
/* 28 */   public static final ResourceKey<TrimMaterial> LAPIS = registryKey("lapis");
/* 29 */   public static final ResourceKey<TrimMaterial> AMETHYST = registryKey("amethyst");
/* 30 */   public static final ResourceKey<TrimMaterial> RESIN = registryKey("resin");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<TrimMaterial> paramBootstrapContext) {
/* 33 */     register(paramBootstrapContext, QUARTZ, Style.EMPTY.withColor(14931140), MaterialAssetGroup.QUARTZ);
/* 34 */     register(paramBootstrapContext, IRON, Style.EMPTY.withColor(15527148), MaterialAssetGroup.IRON);
/* 35 */     register(paramBootstrapContext, NETHERITE, Style.EMPTY.withColor(6445145), MaterialAssetGroup.NETHERITE);
/* 36 */     register(paramBootstrapContext, REDSTONE, Style.EMPTY.withColor(9901575), MaterialAssetGroup.REDSTONE);
/* 37 */     register(paramBootstrapContext, COPPER, Style.EMPTY.withColor(11823181), MaterialAssetGroup.COPPER);
/* 38 */     register(paramBootstrapContext, GOLD, Style.EMPTY.withColor(14594349), MaterialAssetGroup.GOLD);
/* 39 */     register(paramBootstrapContext, EMERALD, Style.EMPTY.withColor(1155126), MaterialAssetGroup.EMERALD);
/* 40 */     register(paramBootstrapContext, DIAMOND, Style.EMPTY.withColor(7269586), MaterialAssetGroup.DIAMOND);
/* 41 */     register(paramBootstrapContext, LAPIS, Style.EMPTY.withColor(4288151), MaterialAssetGroup.LAPIS);
/* 42 */     register(paramBootstrapContext, AMETHYST, Style.EMPTY.withColor(10116294), MaterialAssetGroup.AMETHYST);
/* 43 */     register(paramBootstrapContext, RESIN, Style.EMPTY.withColor(16545810), MaterialAssetGroup.RESIN);
/*    */   }
/*    */   
/*    */   public static Optional<Holder<TrimMaterial>> getFromIngredient(HolderLookup.Provider paramProvider, ItemStack paramItemStack) {
/* 47 */     ProvidesTrimMaterial providesTrimMaterial = (ProvidesTrimMaterial)paramItemStack.get(DataComponents.PROVIDES_TRIM_MATERIAL);
/* 48 */     return (providesTrimMaterial != null) ? providesTrimMaterial.unwrap(paramProvider) : Optional.<Holder<TrimMaterial>>empty();
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<TrimMaterial> paramBootstrapContext, ResourceKey<TrimMaterial> paramResourceKey, Style paramStyle, MaterialAssetGroup paramMaterialAssetGroup) {
/* 52 */     MutableComponent mutableComponent = Component.translatable(Util.makeDescriptionId("trim_material", paramResourceKey.identifier())).withStyle(paramStyle);
/* 53 */     paramBootstrapContext.register(paramResourceKey, new TrimMaterial(paramMaterialAssetGroup, (Component)mutableComponent));
/*    */   }
/*    */   
/*    */   private static ResourceKey<TrimMaterial> registryKey(String paramString) {
/* 57 */     return ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\equipment\trim\TrimMaterials.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */