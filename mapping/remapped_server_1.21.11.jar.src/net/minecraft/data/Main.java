/*     */ package net.minecraft.data;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import joptsimple.AbstractOptionSpec;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.data.advancements.packs.VanillaAdvancementProvider;
/*     */ import net.minecraft.data.loot.packs.TradeRebalanceLootTableProvider;
/*     */ import net.minecraft.data.loot.packs.VanillaLootTableProvider;
/*     */ import net.minecraft.data.metadata.PackMetadataGenerator;
/*     */ import net.minecraft.data.registries.TradeRebalanceRegistries;
/*     */ import net.minecraft.data.registries.VanillaRegistries;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.data.structures.SnbtToNbt;
/*     */ import net.minecraft.data.structures.StructureUpdater;
/*     */ import net.minecraft.data.tags.TagsProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.obfuscate.DontObfuscate;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   @SuppressForbidden(a = "System.out needed before bootstrap")
/*     */   @DontObfuscate
/*     */   public static void main(String[] paramArrayOfString) throws IOException {
/*  70 */     SharedConstants.tryDetectVersion();
/*     */     
/*  72 */     OptionParser optionParser = new OptionParser();
/*  73 */     AbstractOptionSpec abstractOptionSpec = optionParser.accepts("help", "Show the help menu").forHelp();
/*  74 */     OptionSpecBuilder optionSpecBuilder1 = optionParser.accepts("server", "Include server generators");
/*  75 */     OptionSpecBuilder optionSpecBuilder2 = optionParser.accepts("dev", "Include development tools");
/*  76 */     OptionSpecBuilder optionSpecBuilder3 = optionParser.accepts("reports", "Include data reports");
/*  77 */     optionParser.accepts("validate", "Validate inputs");
/*  78 */     OptionSpecBuilder optionSpecBuilder4 = optionParser.accepts("all", "Include all generators");
/*  79 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = optionParser.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", (Object[])new String[0]);
/*  80 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = optionParser.accepts("input", "Input folder").withRequiredArg();
/*  81 */     OptionSet optionSet = optionParser.parse(paramArrayOfString);
/*     */     
/*  83 */     if (optionSet.has((OptionSpec)abstractOptionSpec) || !optionSet.hasOptions()) {
/*  84 */       optionParser.printHelpOn(System.out);
/*     */       
/*     */       return;
/*     */     } 
/*  88 */     Path path = Paths.get((String)argumentAcceptingOptionSpec1.value(optionSet), new String[0]);
/*  89 */     boolean bool = optionSet.has((OptionSpec)optionSpecBuilder4);
/*  90 */     boolean bool1 = (bool || optionSet.has((OptionSpec)optionSpecBuilder1)) ? true : false;
/*  91 */     boolean bool2 = (bool || optionSet.has((OptionSpec)optionSpecBuilder2)) ? true : false;
/*  92 */     boolean bool3 = (bool || optionSet.has((OptionSpec)optionSpecBuilder3)) ? true : false;
/*  93 */     List<Path> list = optionSet.valuesOf((OptionSpec)argumentAcceptingOptionSpec2).stream().map(paramString -> Paths.get(paramString, new String[0])).toList();
/*  94 */     DataGenerator dataGenerator = new DataGenerator(path, SharedConstants.getCurrentVersion(), true);
/*  95 */     addServerProviders(dataGenerator, list, bool1, bool2, bool3);
/*  96 */     dataGenerator.run();
/*  97 */     Util.shutdownExecutors();
/*     */   }
/*     */   
/*     */   private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> paramBiFunction, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 101 */     return paramPackOutput -> (DataProvider)paramBiFunction.apply(paramPackOutput, paramCompletableFuture);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addServerProviders(DataGenerator paramDataGenerator, Collection<Path> paramCollection, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 106 */     DataGenerator.PackGenerator packGenerator1 = paramDataGenerator.getVanillaPack(paramBoolean1);
/* 107 */     packGenerator1.addProvider(paramPackOutput -> (new SnbtToNbt(paramPackOutput, paramCollection)).addFilter((SnbtToNbt.Filter)new StructureUpdater()));
/*     */ 
/*     */     
/* 110 */     CompletableFuture<?> completableFuture = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, (Executor)Util.backgroundExecutor());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     DataGenerator.PackGenerator packGenerator3 = paramDataGenerator.getVanillaPack(paramBoolean1);
/*     */     
/* 120 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.registries.RegistriesDatapackGenerator::new, (CompletableFuture)completableFuture));
/*     */ 
/*     */     
/* 123 */     packGenerator3.addProvider(bindRegistries(VanillaAdvancementProvider::create, (CompletableFuture)completableFuture));
/* 124 */     packGenerator3.addProvider(bindRegistries(VanillaLootTableProvider::create, (CompletableFuture)completableFuture));
/* 125 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.recipes.packs.VanillaRecipeProvider.Runner::new, (CompletableFuture)completableFuture));
/*     */ 
/*     */     
/* 128 */     TagsProvider tagsProvider1 = packGenerator3.<TagsProvider>addProvider(bindRegistries(net.minecraft.data.tags.VanillaBlockTagsProvider::new, (CompletableFuture)completableFuture));
/* 129 */     TagsProvider tagsProvider2 = packGenerator3.<TagsProvider>addProvider(bindRegistries(net.minecraft.data.tags.VanillaItemTagsProvider::new, (CompletableFuture)completableFuture));
/* 130 */     TagsProvider tagsProvider3 = packGenerator3.<TagsProvider>addProvider(bindRegistries(net.minecraft.data.tags.BiomeTagsProvider::new, (CompletableFuture)completableFuture));
/* 131 */     TagsProvider tagsProvider4 = packGenerator3.<TagsProvider>addProvider(bindRegistries(net.minecraft.data.tags.BannerPatternTagsProvider::new, (CompletableFuture)completableFuture));
/* 132 */     TagsProvider tagsProvider5 = packGenerator3.<TagsProvider>addProvider(bindRegistries(net.minecraft.data.tags.StructureTagsProvider::new, (CompletableFuture)completableFuture));
/*     */     
/* 134 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.DamageTypeTagsProvider::new, (CompletableFuture)completableFuture));
/* 135 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.DialogTagsProvider::new, (CompletableFuture)completableFuture));
/* 136 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.EntityTypeTagsProvider::new, (CompletableFuture)completableFuture));
/* 137 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.FlatLevelGeneratorPresetTagsProvider::new, (CompletableFuture)completableFuture));
/* 138 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.FluidTagsProvider::new, (CompletableFuture)completableFuture));
/* 139 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.GameEventTagsProvider::new, (CompletableFuture)completableFuture));
/* 140 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.InstrumentTagsProvider::new, (CompletableFuture)completableFuture));
/* 141 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.PaintingVariantTagsProvider::new, (CompletableFuture)completableFuture));
/* 142 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.PoiTypeTagsProvider::new, (CompletableFuture)completableFuture));
/* 143 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.WorldPresetTagsProvider::new, (CompletableFuture)completableFuture));
/* 144 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.VanillaEnchantmentTagsProvider::new, (CompletableFuture)completableFuture));
/* 145 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.tags.TimelineTagsProvider::new, (CompletableFuture)completableFuture));
/*     */ 
/*     */ 
/*     */     
/* 149 */     packGenerator3 = paramDataGenerator.getVanillaPack(paramBoolean2);
/* 150 */     packGenerator3.addProvider(paramPackOutput -> new NbtToSnbt(paramPackOutput, paramCollection));
/*     */ 
/*     */ 
/*     */     
/* 154 */     packGenerator3 = paramDataGenerator.getVanillaPack(paramBoolean3);
/* 155 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.info.BiomeParametersDumpReport::new, (CompletableFuture)completableFuture));
/* 156 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.info.ItemListReport::new, (CompletableFuture)completableFuture));
/* 157 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.info.BlockListReport::new, (CompletableFuture)completableFuture));
/* 158 */     packGenerator3.addProvider(bindRegistries(net.minecraft.data.info.CommandsReport::new, (CompletableFuture)completableFuture));
/* 159 */     packGenerator3.addProvider(net.minecraft.data.info.RegistryDumpReport::new);
/* 160 */     packGenerator3.addProvider(net.minecraft.data.info.PacketReport::new);
/* 161 */     packGenerator3.addProvider(net.minecraft.data.info.DatapackStructureReport::new);
/* 162 */     packGenerator3.addProvider(net.minecraft.server.jsonrpc.dataprovider.JsonRpcApiSchema::new);
/*     */ 
/*     */ 
/*     */     
/* 166 */     CompletableFuture completableFuture1 = TradeRebalanceRegistries.createLookup(completableFuture);
/* 167 */     CompletableFuture<HolderLookup.Provider> completableFuture2 = completableFuture1.thenApply(RegistrySetBuilder.PatchedRegistries::patches);
/*     */     
/* 169 */     DataGenerator.PackGenerator packGenerator4 = paramDataGenerator.getBuiltinDatapack(paramBoolean1, "trade_rebalance");
/* 170 */     packGenerator4.addProvider(bindRegistries(net.minecraft.data.registries.RegistriesDatapackGenerator::new, completableFuture2));
/* 171 */     packGenerator4.addProvider(paramPackOutput -> PackMetadataGenerator.forFeaturePack(paramPackOutput, (Component)Component.translatable("dataPack.trade_rebalance.description"), FeatureFlagSet.of(FeatureFlags.TRADE_REBALANCE)));
/* 172 */     packGenerator4.addProvider(bindRegistries(TradeRebalanceLootTableProvider::create, (CompletableFuture)completableFuture));
/* 173 */     packGenerator4.addProvider(bindRegistries(net.minecraft.data.tags.TradeRebalanceEnchantmentTagsProvider::new, (CompletableFuture)completableFuture));
/*     */ 
/*     */ 
/*     */     
/* 177 */     DataGenerator.PackGenerator packGenerator2 = paramDataGenerator.getBuiltinDatapack(paramBoolean1, "redstone_experiments");
/* 178 */     packGenerator2.addProvider(paramPackOutput -> PackMetadataGenerator.forFeaturePack(paramPackOutput, (Component)Component.translatable("dataPack.redstone_experiments.description"), FeatureFlagSet.of(FeatureFlags.REDSTONE_EXPERIMENTS)));
/*     */ 
/*     */ 
/*     */     
/* 182 */     packGenerator2 = paramDataGenerator.getBuiltinDatapack(paramBoolean1, "minecart_improvements");
/* 183 */     packGenerator2.addProvider(paramPackOutput -> PackMetadataGenerator.forFeaturePack(paramPackOutput, (Component)Component.translatable("dataPack.minecart_improvements.description"), FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */