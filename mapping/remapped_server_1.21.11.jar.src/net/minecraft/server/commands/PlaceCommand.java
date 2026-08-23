/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.commands.arguments.TemplateMirrorArgument;
/*     */ import net.minecraft.commands.arguments.TemplateRotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlaceCommand
/*     */ {
/*  60 */   private static final SimpleCommandExceptionType ERROR_FEATURE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.place.feature.failed"));
/*  61 */   private static final SimpleCommandExceptionType ERROR_JIGSAW_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.place.jigsaw.failed")); private static final DynamicCommandExceptionType ERROR_TEMPLATE_INVALID;
/*  62 */   private static final SimpleCommandExceptionType ERROR_STRUCTURE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.place.structure.failed")); static {
/*  63 */     ERROR_TEMPLATE_INVALID = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.place.template.invalid", new Object[] { paramObject }));
/*  64 */   } private static final SimpleCommandExceptionType ERROR_TEMPLATE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.place.template.failed")); private static final SuggestionProvider<CommandSourceStack> SUGGEST_TEMPLATES;
/*     */   static {
/*  66 */     SUGGEST_TEMPLATES = ((paramCommandContext, paramSuggestionsBuilder) -> {
/*     */         StructureTemplateManager structureTemplateManager = ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getStructureManager();
/*     */         return SharedSuggestionProvider.suggestResource(structureTemplateManager.listTemplates(), paramSuggestionsBuilder);
/*     */       });
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  72 */     paramCommandDispatcher.register(
/*  73 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("place")
/*  74 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  75 */         .then(
/*  76 */           Commands.literal("feature")
/*  77 */           .then((
/*  78 */             (RequiredArgumentBuilder)Commands.argument("feature", (ArgumentType)ResourceKeyArgument.key(Registries.CONFIGURED_FEATURE))
/*  79 */             .executes(paramCommandContext -> placeFeature((CommandSourceStack)paramCommandContext.getSource(), ResourceKeyArgument.getConfiguredFeature(paramCommandContext, "feature"), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()))))
/*  80 */             .then(
/*  81 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  82 */               .executes(paramCommandContext -> placeFeature((CommandSourceStack)paramCommandContext.getSource(), ResourceKeyArgument.getConfiguredFeature(paramCommandContext, "feature"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos")))))))
/*     */ 
/*     */ 
/*     */         
/*  86 */         .then(
/*  87 */           Commands.literal("jigsaw")
/*  88 */           .then(
/*  89 */             Commands.argument("pool", (ArgumentType)ResourceKeyArgument.key(Registries.TEMPLATE_POOL))
/*  90 */             .then(
/*  91 */               Commands.argument("target", (ArgumentType)IdentifierArgument.id())
/*  92 */               .then((
/*  93 */                 (RequiredArgumentBuilder)Commands.argument("max_depth", (ArgumentType)IntegerArgumentType.integer(1, 20))
/*  94 */                 .executes(paramCommandContext -> placeJigsaw((CommandSourceStack)paramCommandContext.getSource(), (Holder<StructureTemplatePool>)ResourceKeyArgument.getStructureTemplatePool(paramCommandContext, "pool"), IdentifierArgument.getId(paramCommandContext, "target"), IntegerArgumentType.getInteger(paramCommandContext, "max_depth"), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()))))
/*  95 */                 .then(
/*  96 */                   Commands.argument("position", (ArgumentType)BlockPosArgument.blockPos())
/*  97 */                   .executes(paramCommandContext -> placeJigsaw((CommandSourceStack)paramCommandContext.getSource(), (Holder<StructureTemplatePool>)ResourceKeyArgument.getStructureTemplatePool(paramCommandContext, "pool"), IdentifierArgument.getId(paramCommandContext, "target"), IntegerArgumentType.getInteger(paramCommandContext, "max_depth"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "position")))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 103 */         .then(
/* 104 */           Commands.literal("structure")
/* 105 */           .then((
/* 106 */             (RequiredArgumentBuilder)Commands.argument("structure", (ArgumentType)ResourceKeyArgument.key(Registries.STRUCTURE))
/* 107 */             .executes(paramCommandContext -> placeStructure((CommandSourceStack)paramCommandContext.getSource(), ResourceKeyArgument.getStructure(paramCommandContext, "structure"), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()))))
/* 108 */             .then(
/* 109 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 110 */               .executes(paramCommandContext -> placeStructure((CommandSourceStack)paramCommandContext.getSource(), ResourceKeyArgument.getStructure(paramCommandContext, "structure"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos")))))))
/*     */ 
/*     */ 
/*     */         
/* 114 */         .then(
/* 115 */           Commands.literal("template")
/* 116 */           .then((
/* 117 */             (RequiredArgumentBuilder)Commands.argument("template", (ArgumentType)IdentifierArgument.id())
/* 118 */             .suggests(SUGGEST_TEMPLATES)
/* 119 */             .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()), Rotation.NONE, Mirror.NONE, 1.0F, 0, false)))
/* 120 */             .then((
/* 121 */               (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 122 */               .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), Rotation.NONE, Mirror.NONE, 1.0F, 0, false)))
/* 123 */               .then((
/* 124 */                 (RequiredArgumentBuilder)Commands.argument("rotation", (ArgumentType)TemplateRotationArgument.templateRotation())
/* 125 */                 .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), TemplateRotationArgument.getRotation(paramCommandContext, "rotation"), Mirror.NONE, 1.0F, 0, false)))
/* 126 */                 .then((
/* 127 */                   (RequiredArgumentBuilder)Commands.argument("mirror", (ArgumentType)TemplateMirrorArgument.templateMirror())
/* 128 */                   .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), TemplateRotationArgument.getRotation(paramCommandContext, "rotation"), TemplateMirrorArgument.getMirror(paramCommandContext, "mirror"), 1.0F, 0, false)))
/* 129 */                   .then((
/* 130 */                     (RequiredArgumentBuilder)Commands.argument("integrity", (ArgumentType)FloatArgumentType.floatArg(0.0F, 1.0F))
/* 131 */                     .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), TemplateRotationArgument.getRotation(paramCommandContext, "rotation"), TemplateMirrorArgument.getMirror(paramCommandContext, "mirror"), FloatArgumentType.getFloat(paramCommandContext, "integrity"), 0, false)))
/* 132 */                     .then((
/* 133 */                       (RequiredArgumentBuilder)Commands.argument("seed", (ArgumentType)IntegerArgumentType.integer())
/* 134 */                       .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), TemplateRotationArgument.getRotation(paramCommandContext, "rotation"), TemplateMirrorArgument.getMirror(paramCommandContext, "mirror"), FloatArgumentType.getFloat(paramCommandContext, "integrity"), IntegerArgumentType.getInteger(paramCommandContext, "seed"), false)))
/* 135 */                       .then(
/* 136 */                         Commands.literal("strict")
/* 137 */                         .executes(paramCommandContext -> placeTemplate((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "template"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), TemplateRotationArgument.getRotation(paramCommandContext, "rotation"), TemplateMirrorArgument.getMirror(paramCommandContext, "mirror"), FloatArgumentType.getFloat(paramCommandContext, "integrity"), IntegerArgumentType.getInteger(paramCommandContext, "seed"), true)))))))))));
/*     */   }
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
/*     */   public static int placeFeature(CommandSourceStack paramCommandSourceStack, Holder.Reference<ConfiguredFeature<?, ?>> paramReference, BlockPos paramBlockPos) throws CommandSyntaxException {
/* 150 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 151 */     ConfiguredFeature configuredFeature = (ConfiguredFeature)paramReference.value();
/*     */     
/* 153 */     ChunkPos chunkPos = new ChunkPos(paramBlockPos);
/* 154 */     checkLoaded(serverLevel, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1), new ChunkPos(chunkPos.x + 1, chunkPos.z + 1));
/*     */     
/* 156 */     if (!configuredFeature.place((WorldGenLevel)serverLevel, serverLevel.getChunkSource().getGenerator(), serverLevel.getRandom(), paramBlockPos)) {
/* 157 */       throw ERROR_FEATURE_FAILED.create();
/*     */     }
/* 159 */     String str = paramReference.key().identifier().toString();
/* 160 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.place.feature.success", new Object[] { paramString, Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()) }), true);
/* 161 */     return 1;
/*     */   }
/*     */   
/*     */   public static int placeJigsaw(CommandSourceStack paramCommandSourceStack, Holder<StructureTemplatePool> paramHolder, Identifier paramIdentifier, int paramInt, BlockPos paramBlockPos) throws CommandSyntaxException {
/* 165 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 166 */     ChunkPos chunkPos = new ChunkPos(paramBlockPos);
/* 167 */     checkLoaded(serverLevel, chunkPos, chunkPos);
/* 168 */     if (!JigsawPlacement.generateJigsaw(serverLevel, paramHolder, paramIdentifier, paramInt, paramBlockPos, false)) {
/* 169 */       throw ERROR_JIGSAW_FAILED.create();
/*     */     }
/* 171 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.place.jigsaw.success", new Object[] { Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()) }), true);
/* 172 */     return 1;
/*     */   }
/*     */   
/*     */   public static int placeStructure(CommandSourceStack paramCommandSourceStack, Holder.Reference<Structure> paramReference, BlockPos paramBlockPos) throws CommandSyntaxException {
/* 176 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 177 */     Structure structure = (Structure)paramReference.value();
/* 178 */     ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
/*     */     
/* 180 */     StructureStart structureStart = structure.generate((Holder)paramReference, serverLevel.dimension(), paramCommandSourceStack.registryAccess(), chunkGenerator, chunkGenerator.getBiomeSource(), serverLevel.getChunkSource().randomState(), serverLevel.getStructureManager(), serverLevel.getSeed(), new ChunkPos(paramBlockPos), 0, (LevelHeightAccessor)serverLevel, paramHolder -> true);
/* 181 */     if (!structureStart.isValid()) {
/* 182 */       throw ERROR_STRUCTURE_FAILED.create();
/*     */     }
/* 184 */     BoundingBox boundingBox = structureStart.getBoundingBox();
/* 185 */     ChunkPos chunkPos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingBox.minX()), SectionPos.blockToSectionCoord(boundingBox.minZ()));
/* 186 */     ChunkPos chunkPos2 = new ChunkPos(SectionPos.blockToSectionCoord(boundingBox.maxX()), SectionPos.blockToSectionCoord(boundingBox.maxZ()));
/*     */     
/* 188 */     checkLoaded(serverLevel, chunkPos1, chunkPos2);
/* 189 */     ChunkPos.rangeClosed(chunkPos1, chunkPos2).forEach(paramChunkPos -> paramStructureStart.placeInChunk((WorldGenLevel)paramServerLevel, paramServerLevel.structureManager(), paramChunkGenerator, paramServerLevel.getRandom(), new BoundingBox(paramChunkPos.getMinBlockX(), paramServerLevel.getMinY(), paramChunkPos.getMinBlockZ(), paramChunkPos.getMaxBlockX(), paramServerLevel.getMaxY() + 1, paramChunkPos.getMaxBlockZ()), paramChunkPos));
/*     */ 
/*     */ 
/*     */     
/* 193 */     String str = paramReference.key().identifier().toString();
/* 194 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.place.structure.success", new Object[] { paramString, Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()) }), true);
/* 195 */     return 1;
/*     */   }
/*     */   public static int placeTemplate(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier, BlockPos paramBlockPos, Rotation paramRotation, Mirror paramMirror, float paramFloat, int paramInt, boolean paramBoolean) throws CommandSyntaxException {
/*     */     Optional<StructureTemplate> optional;
/* 199 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 200 */     StructureTemplateManager structureTemplateManager = serverLevel.getStructureManager();
/*     */     
/*     */     try {
/* 203 */       optional = structureTemplateManager.get(paramIdentifier);
/* 204 */     } catch (IdentifierException identifierException) {
/* 205 */       throw ERROR_TEMPLATE_INVALID.create(paramIdentifier);
/*     */     } 
/* 207 */     if (optional.isEmpty()) {
/* 208 */       throw ERROR_TEMPLATE_INVALID.create(paramIdentifier);
/*     */     }
/* 210 */     StructureTemplate structureTemplate = optional.get();
/* 211 */     checkLoaded(serverLevel, new ChunkPos(paramBlockPos), new ChunkPos(paramBlockPos.offset(structureTemplate.getSize())));
/*     */     
/* 213 */     StructurePlaceSettings structurePlaceSettings = (new StructurePlaceSettings()).setMirror(paramMirror).setRotation(paramRotation).setKnownShape(paramBoolean);
/* 214 */     if (paramFloat < 1.0F) {
/* 215 */       structurePlaceSettings.clearProcessors().addProcessor((StructureProcessor)new BlockRotProcessor(paramFloat)).setRandom(StructureBlockEntity.createRandom(paramInt));
/*     */     }
/*     */     
/* 218 */     boolean bool = structureTemplate.placeInWorld((ServerLevelAccessor)serverLevel, paramBlockPos, paramBlockPos, structurePlaceSettings, StructureBlockEntity.createRandom(paramInt), 0x2 | (paramBoolean ? 816 : 0));
/* 219 */     if (!bool) {
/* 220 */       throw ERROR_TEMPLATE_FAILED.create();
/*     */     }
/* 222 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.place.template.success", new Object[] { Component.translationArg(paramIdentifier), Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()) }), true);
/* 223 */     return 1;
/*     */   }
/*     */   
/*     */   private static void checkLoaded(ServerLevel paramServerLevel, ChunkPos paramChunkPos1, ChunkPos paramChunkPos2) throws CommandSyntaxException {
/* 227 */     if (ChunkPos.rangeClosed(paramChunkPos1, paramChunkPos2).filter(paramChunkPos -> !paramServerLevel.isLoaded(paramChunkPos.getWorldPosition())).findAny().isPresent())
/* 228 */       throw BlockPosArgument.ERROR_NOT_LOADED.create(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\PlaceCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */