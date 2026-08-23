/*     */ package net.minecraft.server.commands;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LocateCommand {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND; private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID;
/*     */   static {
/*  47 */     ERROR_STRUCTURE_NOT_FOUND = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.locate.structure.not_found", new Object[] { paramObject }));
/*  48 */     ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.locate.structure.invalid", new Object[] { paramObject }));
/*     */     
/*  50 */     ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.locate.biome.not_found", new Object[] { paramObject }));
/*     */     
/*  52 */     ERROR_POI_NOT_FOUND = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.locate.poi.not_found", new Object[] { paramObject }));
/*     */   }
/*     */   private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND;
/*     */   private static final DynamicCommandExceptionType ERROR_POI_NOT_FOUND;
/*     */   private static final int MAX_STRUCTURE_SEARCH_RADIUS = 100;
/*     */   private static final int MAX_BIOME_SEARCH_RADIUS = 6400;
/*     */   private static final int BIOME_SAMPLE_RESOLUTION_HORIZONTAL = 32;
/*     */   private static final int BIOME_SAMPLE_RESOLUTION_VERTICAL = 64;
/*     */   private static final int POI_SEARCH_RADIUS = 256;
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  63 */     paramCommandDispatcher.register(
/*  64 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("locate")
/*  65 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  66 */         .then(
/*  67 */           Commands.literal("structure")
/*  68 */           .then(
/*  69 */             Commands.argument("structure", (ArgumentType)ResourceOrTagKeyArgument.resourceOrTagKey(Registries.STRUCTURE))
/*  70 */             .executes(paramCommandContext -> locateStructure((CommandSourceStack)paramCommandContext.getSource(), ResourceOrTagKeyArgument.getResourceOrTagKey(paramCommandContext, "structure", Registries.STRUCTURE, ERROR_STRUCTURE_INVALID))))))
/*     */         
/*  72 */         .then(
/*  73 */           Commands.literal("biome")
/*  74 */           .then(
/*  75 */             Commands.argument("biome", (ArgumentType)ResourceOrTagArgument.resourceOrTag(paramCommandBuildContext, Registries.BIOME))
/*  76 */             .executes(paramCommandContext -> locateBiome((CommandSourceStack)paramCommandContext.getSource(), ResourceOrTagArgument.getResourceOrTag(paramCommandContext, "biome", Registries.BIOME))))))
/*     */         
/*  78 */         .then(
/*  79 */           Commands.literal("poi")
/*  80 */           .then(
/*  81 */             Commands.argument("poi", (ArgumentType)ResourceOrTagArgument.resourceOrTag(paramCommandBuildContext, Registries.POINT_OF_INTEREST_TYPE))
/*  82 */             .executes(paramCommandContext -> locatePoi((CommandSourceStack)paramCommandContext.getSource(), ResourceOrTagArgument.getResourceOrTag(paramCommandContext, "poi", Registries.POINT_OF_INTEREST_TYPE))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<? extends HolderSet.ListBacked<Structure>> getHolders(ResourceOrTagKeyArgument.Result<Structure> paramResult, Registry<Structure> paramRegistry) {
/*  90 */     Objects.requireNonNull(paramRegistry); return (Optional<? extends HolderSet.ListBacked<Structure>>)paramResult.unwrap().map(paramResourceKey -> paramRegistry.get(paramResourceKey).map(()), paramRegistry::get);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int locateStructure(CommandSourceStack paramCommandSourceStack, ResourceOrTagKeyArgument.Result<Structure> paramResult) throws CommandSyntaxException {
/*  95 */     Registry<Structure> registry = paramCommandSourceStack.getLevel().registryAccess().lookupOrThrow(Registries.STRUCTURE);
/*     */     
/*  97 */     HolderSet holderSet = (HolderSet)getHolders(paramResult, registry).<Throwable>orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(paramResult.asPrintable()));
/*     */     
/*  99 */     BlockPos blockPos = BlockPos.containing((Position)paramCommandSourceStack.getPosition());
/* 100 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 101 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/* 102 */     Pair<BlockPos, ? extends Holder<?>> pair = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, holderSet, blockPos, 100, false);
/* 103 */     stopwatch.stop();
/* 104 */     if (pair == null) {
/* 105 */       throw ERROR_STRUCTURE_NOT_FOUND.create(paramResult.asPrintable());
/*     */     }
/*     */     
/* 108 */     return showLocateResult(paramCommandSourceStack, paramResult, blockPos, pair, "commands.locate.structure.success", false, stopwatch.elapsed());
/*     */   }
/*     */   
/*     */   private static int locateBiome(CommandSourceStack paramCommandSourceStack, ResourceOrTagArgument.Result<Biome> paramResult) throws CommandSyntaxException {
/* 112 */     BlockPos blockPos = BlockPos.containing((Position)paramCommandSourceStack.getPosition());
/* 113 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/* 114 */     Pair<BlockPos, ? extends Holder<?>> pair = paramCommandSourceStack.getLevel().findClosestBiome3d((Predicate)paramResult, blockPos, 6400, 32, 64);
/* 115 */     stopwatch.stop();
/* 116 */     if (pair == null) {
/* 117 */       throw ERROR_BIOME_NOT_FOUND.create(paramResult.asPrintable());
/*     */     }
/* 119 */     return showLocateResult(paramCommandSourceStack, paramResult, blockPos, pair, "commands.locate.biome.success", true, stopwatch.elapsed());
/*     */   }
/*     */   
/*     */   private static int locatePoi(CommandSourceStack paramCommandSourceStack, ResourceOrTagArgument.Result<PoiType> paramResult) throws CommandSyntaxException {
/* 123 */     BlockPos blockPos = BlockPos.containing((Position)paramCommandSourceStack.getPosition());
/* 124 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 125 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/* 126 */     Optional<Pair> optional = serverLevel.getPoiManager().findClosestWithType((Predicate)paramResult, blockPos, 256, PoiManager.Occupancy.ANY);
/* 127 */     stopwatch.stop();
/*     */     
/* 129 */     if (optional.isEmpty()) {
/* 130 */       throw ERROR_POI_NOT_FOUND.create(paramResult.asPrintable());
/*     */     }
/*     */     
/* 133 */     return showLocateResult(paramCommandSourceStack, paramResult, blockPos, ((Pair)optional.get()).swap(), "commands.locate.poi.success", false, stopwatch.elapsed());
/*     */   }
/*     */   
/*     */   public static int showLocateResult(CommandSourceStack paramCommandSourceStack, ResourceOrTagArgument.Result<?> paramResult, BlockPos paramBlockPos, Pair<BlockPos, ? extends Holder<?>> paramPair, String paramString, boolean paramBoolean, Duration paramDuration) {
/* 137 */     String str = (String)paramResult.unwrap().map(paramReference -> paramResult.asPrintable(), paramNamed -> paramResult.asPrintable() + " (" + paramResult.asPrintable() + ")");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     return showLocateResult(paramCommandSourceStack, paramBlockPos, paramPair, paramString, paramBoolean, str, paramDuration);
/*     */   }
/*     */   
/*     */   public static int showLocateResult(CommandSourceStack paramCommandSourceStack, ResourceOrTagKeyArgument.Result<?> paramResult, BlockPos paramBlockPos, Pair<BlockPos, ? extends Holder<?>> paramPair, String paramString, boolean paramBoolean, Duration paramDuration) {
/* 146 */     String str = (String)paramResult.unwrap().map(paramResourceKey -> paramResourceKey.identifier().toString(), paramTagKey -> "#" + String.valueOf(paramTagKey.location()) + " (" + ((Holder)paramPair.getSecond()).getRegisteredName() + ")");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     return showLocateResult(paramCommandSourceStack, paramBlockPos, paramPair, paramString, paramBoolean, str, paramDuration);
/*     */   }
/*     */   
/*     */   private static int showLocateResult(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, Pair<BlockPos, ? extends Holder<?>> paramPair, String paramString1, boolean paramBoolean, String paramString2, Duration paramDuration) {
/* 155 */     BlockPos blockPos = (BlockPos)paramPair.getFirst();
/*     */ 
/*     */ 
/*     */     
/* 159 */     int i = paramBoolean ? Mth.floor(Mth.sqrt((float)paramBlockPos.distSqr((Vec3i)blockPos))) : Mth.floor(dist(paramBlockPos.getX(), paramBlockPos.getZ(), blockPos.getX(), blockPos.getZ()));
/* 160 */     String str = paramBoolean ? String.valueOf(blockPos.getY()) : "~";
/* 161 */     MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets((Component)Component.translatable("chat.coordinates", new Object[] { Integer.valueOf(blockPos.getX()), str, Integer.valueOf(blockPos.getZ()) })).withStyle(paramStyle -> paramStyle.withColor(ChatFormatting.GREEN).withClickEvent((ClickEvent)new ClickEvent.SuggestCommand("/tp @s " + paramBlockPos.getX() + " " + paramString + " " + paramBlockPos.getZ())).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat.coordinates.tooltip"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable(paramString1, new Object[] { paramString2, paramComponent, Integer.valueOf(paramInt) }), false);
/* 168 */     LOGGER.info("Locating element {} took {} ms", paramString2, Long.valueOf(paramDuration.toMillis()));
/* 169 */     return i;
/*     */   }
/*     */   
/*     */   private static float dist(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 173 */     int i = paramInt3 - paramInt1;
/* 174 */     int j = paramInt4 - paramInt2;
/* 175 */     return Mth.sqrt((i * i + j * j));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\LocateCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */