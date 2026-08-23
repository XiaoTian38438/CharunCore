/*     */ package net.minecraft.server.commands;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DataPackCommand {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final DynamicCommandExceptionType ERROR_UNKNOWN_PACK; private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_ENABLED; private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_DISABLED; private static final DynamicCommandExceptionType ERROR_CANNOT_DISABLE_FEATURE; private static final Dynamic2CommandExceptionType ERROR_PACK_FEATURES_NOT_ENABLED; private static final DynamicCommandExceptionType ERROR_PACK_INVALID_NAME;
/*     */   static {
/*  56 */     ERROR_UNKNOWN_PACK = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.unknown", new Object[] { paramObject }));
/*  57 */     ERROR_PACK_ALREADY_ENABLED = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.enable.failed", new Object[] { paramObject }));
/*  58 */     ERROR_PACK_ALREADY_DISABLED = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.disable.failed", new Object[] { paramObject }));
/*  59 */     ERROR_CANNOT_DISABLE_FEATURE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.disable.failed.feature", new Object[] { paramObject }));
/*  60 */     ERROR_PACK_FEATURES_NOT_ENABLED = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.datapack.enable.failed.no_flags", new Object[] { paramObject1, paramObject2 }));
/*     */     
/*  62 */     ERROR_PACK_INVALID_NAME = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.create.invalid_name", new Object[] { paramObject }));
/*  63 */     ERROR_PACK_INVALID_FULL_NAME = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.create.invalid_full_name", new Object[] { paramObject }));
/*  64 */     ERROR_PACK_ALREADY_EXISTS = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.create.already_exists", new Object[] { paramObject }));
/*  65 */     ERROR_PACK_METADATA_ENCODE_FAILURE = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.datapack.create.metadata_encode_failure", new Object[] { paramObject1, paramObject2 }));
/*  66 */     ERROR_PACK_IO_FAILURE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.datapack.create.io_failure", new Object[] { paramObject }));
/*     */     
/*  68 */     SELECTED_PACKS = ((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)paramCommandContext.getSource()).getServer().getPackRepository().getSelectedIds().stream().map(StringArgumentType::escapeIfRequired), paramSuggestionsBuilder));
/*  69 */     UNSELECTED_PACKS = ((paramCommandContext, paramSuggestionsBuilder) -> {
/*     */         PackRepository packRepository = ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPackRepository();
/*     */         Collection collection = packRepository.getSelectedIds();
/*     */         FeatureFlagSet featureFlagSet = ((CommandSourceStack)paramCommandContext.getSource()).enabledFeatures();
/*     */         return SharedSuggestionProvider.suggest(packRepository.getAvailablePacks().stream().filter(()).map(Pack::getId).filter(()).map(StringArgumentType::escapeIfRequired), paramSuggestionsBuilder);
/*     */       });
/*     */   }
/*     */   
/*     */   private static final DynamicCommandExceptionType ERROR_PACK_INVALID_FULL_NAME;
/*     */   private static final DynamicCommandExceptionType ERROR_PACK_ALREADY_EXISTS;
/*     */   private static final Dynamic2CommandExceptionType ERROR_PACK_METADATA_ENCODE_FAILURE;
/*     */   private static final DynamicCommandExceptionType ERROR_PACK_IO_FAILURE;
/*     */   private static final SuggestionProvider<CommandSourceStack> SELECTED_PACKS;
/*     */   private static final SuggestionProvider<CommandSourceStack> UNSELECTED_PACKS;
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  85 */     paramCommandDispatcher.register(
/*  86 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("datapack")
/*  87 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  88 */         .then(
/*  89 */           Commands.literal("enable")
/*  90 */           .then((
/*  91 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", (ArgumentType)StringArgumentType.string())
/*  92 */             .suggests(UNSELECTED_PACKS)
/*  93 */             .executes(paramCommandContext -> enablePack((CommandSourceStack)paramCommandContext.getSource(), getPack(paramCommandContext, "name", true), ())))
/*  94 */             .then(
/*  95 */               Commands.literal("after")
/*  96 */               .then(
/*  97 */                 Commands.argument("existing", (ArgumentType)StringArgumentType.string())
/*  98 */                 .suggests(SELECTED_PACKS)
/*  99 */                 .executes(paramCommandContext -> enablePack((CommandSourceStack)paramCommandContext.getSource(), getPack(paramCommandContext, "name", true), ())))))
/*     */ 
/*     */             
/* 102 */             .then(
/* 103 */               Commands.literal("before")
/* 104 */               .then(
/* 105 */                 Commands.argument("existing", (ArgumentType)StringArgumentType.string())
/* 106 */                 .suggests(SELECTED_PACKS)
/* 107 */                 .executes(paramCommandContext -> enablePack((CommandSourceStack)paramCommandContext.getSource(), getPack(paramCommandContext, "name", true), ())))))
/*     */ 
/*     */             
/* 110 */             .then(
/* 111 */               Commands.literal("last")
/* 112 */               .executes(paramCommandContext -> enablePack((CommandSourceStack)paramCommandContext.getSource(), getPack(paramCommandContext, "name", true), List::add))))
/*     */             
/* 114 */             .then(
/* 115 */               Commands.literal("first")
/* 116 */               .executes(paramCommandContext -> enablePack((CommandSourceStack)paramCommandContext.getSource(), getPack(paramCommandContext, "name", true), ()))))))
/*     */ 
/*     */ 
/*     */         
/* 120 */         .then(
/* 121 */           Commands.literal("disable")
/* 122 */           .then(
/* 123 */             Commands.argument("name", (ArgumentType)StringArgumentType.string())
/* 124 */             .suggests(SELECTED_PACKS)
/* 125 */             .executes(paramCommandContext -> disablePack((CommandSourceStack)paramCommandContext.getSource(), getPack(paramCommandContext, "name", false))))))
/*     */ 
/*     */         
/* 128 */         .then((
/* 129 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list")
/* 130 */           .executes(paramCommandContext -> listPacks((CommandSourceStack)paramCommandContext.getSource())))
/* 131 */           .then(
/* 132 */             Commands.literal("available")
/* 133 */             .executes(paramCommandContext -> listAvailablePacks((CommandSourceStack)paramCommandContext.getSource()))))
/*     */           
/* 135 */           .then(
/* 136 */             Commands.literal("enabled")
/* 137 */             .executes(paramCommandContext -> listEnabledPacks((CommandSourceStack)paramCommandContext.getSource())))))
/*     */ 
/*     */         
/* 140 */         .then((
/* 141 */           (LiteralArgumentBuilder)Commands.literal("create")
/* 142 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 143 */           .then(
/* 144 */             Commands.argument("id", (ArgumentType)StringArgumentType.string())
/* 145 */             .then(
/* 146 */               Commands.argument("description", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 147 */               .executes(paramCommandContext -> createPack((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "id"), ComponentArgument.getResolvedComponent(paramCommandContext, "description")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int createPack(CommandSourceStack paramCommandSourceStack, String paramString, Component paramComponent) throws CommandSyntaxException {
/* 155 */     Path path1 = paramCommandSourceStack.getServer().getWorldPath(LevelResource.DATAPACK_DIR);
/* 156 */     if (!FileUtil.isValidPathSegment(paramString)) {
/* 157 */       throw ERROR_PACK_INVALID_NAME.create(paramString);
/*     */     }
/* 159 */     if (!FileUtil.isPathPartPortable(paramString)) {
/* 160 */       throw ERROR_PACK_INVALID_FULL_NAME.create(paramString);
/*     */     }
/* 162 */     Path path2 = path1.resolve(paramString);
/* 163 */     if (Files.exists(path2, new java.nio.file.LinkOption[0])) {
/* 164 */       throw ERROR_PACK_ALREADY_EXISTS.create(paramString);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 169 */     PackMetadataSection packMetadataSection = new PackMetadataSection(paramComponent, SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).minorRange());
/*     */ 
/*     */     
/* 172 */     DataResult dataResult = PackMetadataSection.SERVER_TYPE.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, packMetadataSection);
/* 173 */     Optional optional = dataResult.error();
/* 174 */     if (optional.isPresent()) {
/* 175 */       throw ERROR_PACK_METADATA_ENCODE_FAILURE.create(paramString, ((DataResult.Error)optional.get()).message());
/*     */     }
/* 177 */     JsonObject jsonObject = new JsonObject();
/* 178 */     jsonObject.add(PackMetadataSection.SERVER_TYPE.name(), (JsonElement)dataResult.getOrThrow());
/*     */ 
/*     */     
/* 181 */     try { Files.createDirectory(path2, (FileAttribute<?>[])new FileAttribute[0]);
/* 182 */       Files.createDirectory(path2.resolve(PackType.SERVER_DATA.getDirectory()), (FileAttribute<?>[])new FileAttribute[0]);
/*     */ 
/*     */       
/* 185 */       BufferedWriter bufferedWriter = Files.newBufferedWriter(path2.resolve("pack.mcmeta"), StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/* 186 */       try { JsonWriter jsonWriter = new JsonWriter(bufferedWriter);
/*     */         
/* 188 */         try { jsonWriter.setSerializeNulls(false);
/* 189 */           jsonWriter.setIndent("  ");
/* 190 */           GsonHelper.writeValue(jsonWriter, (JsonElement)jsonObject, null);
/* 191 */           jsonWriter.close(); } catch (Throwable throwable) { try { jsonWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (bufferedWriter != null) bufferedWriter.close();  } catch (Throwable throwable) { if (bufferedWriter != null)
/* 192 */           try { bufferedWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException)
/* 193 */     { LOGGER.warn("Failed to create pack at {}", path1.toAbsolutePath(), iOException);
/* 194 */       throw ERROR_PACK_IO_FAILURE.create(paramString); }
/*     */ 
/*     */     
/* 197 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.create.success", new Object[] { paramString }), true);
/* 198 */     return 1;
/*     */   }
/*     */   
/*     */   private static int enablePack(CommandSourceStack paramCommandSourceStack, Pack paramPack, Inserter paramInserter) throws CommandSyntaxException {
/* 202 */     PackRepository packRepository = paramCommandSourceStack.getServer().getPackRepository();
/*     */     
/* 204 */     ArrayList<Pack> arrayList = Lists.newArrayList(packRepository.getSelectedPacks());
/* 205 */     paramInserter.apply(arrayList, paramPack);
/*     */     
/* 207 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.modify.enable", new Object[] { paramPack.getChatLink(true) }), true);
/* 208 */     ReloadCommand.reloadPacks((Collection<String>)arrayList.stream().map(Pack::getId).collect(Collectors.toList()), paramCommandSourceStack);
/* 209 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static int disablePack(CommandSourceStack paramCommandSourceStack, Pack paramPack) {
/* 213 */     PackRepository packRepository = paramCommandSourceStack.getServer().getPackRepository();
/*     */     
/* 215 */     ArrayList arrayList = Lists.newArrayList(packRepository.getSelectedPacks());
/* 216 */     arrayList.remove(paramPack);
/*     */     
/* 218 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.modify.disable", new Object[] { paramPack.getChatLink(true) }), true);
/* 219 */     ReloadCommand.reloadPacks((Collection<String>)arrayList.stream().map(Pack::getId).collect(Collectors.toList()), paramCommandSourceStack);
/* 220 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static int listPacks(CommandSourceStack paramCommandSourceStack) {
/* 224 */     return listEnabledPacks(paramCommandSourceStack) + listAvailablePacks(paramCommandSourceStack);
/*     */   }
/*     */   
/*     */   private static int listAvailablePacks(CommandSourceStack paramCommandSourceStack) {
/* 228 */     PackRepository packRepository = paramCommandSourceStack.getServer().getPackRepository();
/* 229 */     packRepository.reload();
/*     */     
/* 231 */     Collection collection1 = packRepository.getSelectedPacks();
/* 232 */     Collection collection2 = packRepository.getAvailablePacks();
/* 233 */     FeatureFlagSet featureFlagSet = paramCommandSourceStack.enabledFeatures();
/* 234 */     List list = collection2.stream().filter(paramPack -> (!paramCollection.contains(paramPack) && paramPack.getRequestedFeatures().isSubsetOf(paramFeatureFlagSet))).toList();
/* 235 */     if (list.isEmpty()) {
/* 236 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.list.available.none"), false);
/*     */     } else {
/* 238 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.list.available.success", new Object[] { Integer.valueOf(paramList.size()), ComponentUtils.formatList(paramList, ()) }), false);
/*     */     } 
/*     */     
/* 241 */     return list.size();
/*     */   }
/*     */   
/*     */   private static int listEnabledPacks(CommandSourceStack paramCommandSourceStack) {
/* 245 */     PackRepository packRepository = paramCommandSourceStack.getServer().getPackRepository();
/* 246 */     packRepository.reload();
/*     */     
/* 248 */     Collection collection = packRepository.getSelectedPacks();
/* 249 */     if (collection.isEmpty()) {
/* 250 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.list.enabled.none"), false);
/*     */     } else {
/* 252 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.datapack.list.enabled.success", new Object[] { Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection, ()) }), false);
/*     */     } 
/*     */     
/* 255 */     return collection.size();
/*     */   }
/*     */   
/*     */   private static Pack getPack(CommandContext<CommandSourceStack> paramCommandContext, String paramString, boolean paramBoolean) throws CommandSyntaxException {
/* 259 */     String str = StringArgumentType.getString(paramCommandContext, paramString);
/* 260 */     PackRepository packRepository = ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPackRepository();
/* 261 */     Pack pack = packRepository.getPack(str);
/* 262 */     if (pack == null) {
/* 263 */       throw ERROR_UNKNOWN_PACK.create(str);
/*     */     }
/* 265 */     boolean bool = packRepository.getSelectedPacks().contains(pack);
/* 266 */     if (paramBoolean && bool) {
/* 267 */       throw ERROR_PACK_ALREADY_ENABLED.create(str);
/*     */     }
/* 269 */     if (!paramBoolean && !bool) {
/* 270 */       throw ERROR_PACK_ALREADY_DISABLED.create(str);
/*     */     }
/* 272 */     FeatureFlagSet featureFlagSet1 = ((CommandSourceStack)paramCommandContext.getSource()).enabledFeatures();
/* 273 */     FeatureFlagSet featureFlagSet2 = pack.getRequestedFeatures();
/* 274 */     if (!paramBoolean && !featureFlagSet2.isEmpty() && pack.getPackSource() == PackSource.FEATURE) {
/* 275 */       throw ERROR_CANNOT_DISABLE_FEATURE.create(str);
/*     */     }
/* 277 */     if (!featureFlagSet2.isSubsetOf(featureFlagSet1)) {
/* 278 */       throw ERROR_PACK_FEATURES_NOT_ENABLED.create(str, FeatureFlags.printMissingFlags(featureFlagSet1, featureFlagSet2));
/*     */     }
/* 280 */     return pack;
/*     */   }
/*     */   
/*     */   private static interface Inserter {
/*     */     void apply(List<Pack> param1List, Pack param1Pack) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DataPackCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */