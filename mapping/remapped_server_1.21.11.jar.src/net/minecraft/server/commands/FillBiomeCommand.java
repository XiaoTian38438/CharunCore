/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class FillBiomeCommand {
/*     */   private static final Dynamic2CommandExceptionType ERROR_VOLUME_TOO_LARGE;
/*  43 */   public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType((Message)Component.translatable("argument.pos.unloaded")); static {
/*  44 */     ERROR_VOLUME_TOO_LARGE = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.fillbiome.toobig", new Object[] { paramObject1, paramObject2 }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  47 */     paramCommandDispatcher.register(
/*  48 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fillbiome")
/*  49 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  50 */         .then(
/*  51 */           Commands.argument("from", (ArgumentType)BlockPosArgument.blockPos())
/*  52 */           .then(
/*  53 */             Commands.argument("to", (ArgumentType)BlockPosArgument.blockPos())
/*  54 */             .then((
/*  55 */               (RequiredArgumentBuilder)Commands.argument("biome", (ArgumentType)ResourceArgument.resource(paramCommandBuildContext, Registries.BIOME))
/*  56 */               .executes(paramCommandContext -> fill((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "from"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "to"), ResourceArgument.getResource(paramCommandContext, "biome", Registries.BIOME), ())))
/*  57 */               .then(Commands.literal("replace")
/*  58 */                 .then(
/*  59 */                   Commands.argument("filter", (ArgumentType)ResourceOrTagArgument.resourceOrTag(paramCommandBuildContext, Registries.BIOME))
/*  60 */                   .executes(paramCommandContext -> fill((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "from"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "to"), ResourceArgument.getResource(paramCommandContext, "biome", Registries.BIOME), (Predicate<Holder<Biome>>)ResourceOrTagArgument.getResourceOrTag(paramCommandContext, "filter", Registries.BIOME)))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int quantize(int paramInt) {
/*  70 */     return QuartPos.toBlock(QuartPos.fromBlock(paramInt));
/*     */   }
/*     */   
/*     */   private static BlockPos quantize(BlockPos paramBlockPos) {
/*  74 */     return new BlockPos(quantize(paramBlockPos.getX()), quantize(paramBlockPos.getY()), quantize(paramBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   private static BiomeResolver makeResolver(MutableInt paramMutableInt, ChunkAccess paramChunkAccess, BoundingBox paramBoundingBox, Holder<Biome> paramHolder, Predicate<Holder<Biome>> paramPredicate) {
/*  78 */     return (paramInt1, paramInt2, paramInt3, paramSampler) -> {
/*     */         int i = QuartPos.toBlock(paramInt1);
/*     */         int j = QuartPos.toBlock(paramInt2);
/*     */         int k = QuartPos.toBlock(paramInt3);
/*     */         Holder holder = paramChunkAccess.getNoiseBiome(paramInt1, paramInt2, paramInt3);
/*     */         if (paramBoundingBox.isInside(i, j, k) && paramPredicate.test(holder)) {
/*     */           paramMutableInt.increment();
/*     */           return paramHolder;
/*     */         } 
/*     */         return holder;
/*     */       };
/*     */   }
/*     */   
/*     */   public static Either<Integer, CommandSyntaxException> fill(ServerLevel paramServerLevel, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Holder<Biome> paramHolder) {
/*  92 */     return fill(paramServerLevel, paramBlockPos1, paramBlockPos2, paramHolder, paramHolder -> true, paramSupplier -> {
/*     */         
/*     */         });
/*     */   } public static Either<Integer, CommandSyntaxException> fill(ServerLevel paramServerLevel, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Holder<Biome> paramHolder, Predicate<Holder<Biome>> paramPredicate, Consumer<Supplier<Component>> paramConsumer) {
/*  96 */     BlockPos blockPos1 = quantize(paramBlockPos1);
/*  97 */     BlockPos blockPos2 = quantize(paramBlockPos2);
/*  98 */     BoundingBox boundingBox = BoundingBox.fromCorners((Vec3i)blockPos1, (Vec3i)blockPos2);
/*  99 */     int i = boundingBox.getXSpan() * boundingBox.getYSpan() * boundingBox.getZSpan();
/* 100 */     int j = ((Integer)paramServerLevel.getGameRules().get(GameRules.MAX_BLOCK_MODIFICATIONS)).intValue();
/* 101 */     if (i > j) {
/* 102 */       return Either.right(ERROR_VOLUME_TOO_LARGE.create(Integer.valueOf(j), Integer.valueOf(i)));
/*     */     }
/*     */     
/* 105 */     ArrayList<ChunkAccess> arrayList = new ArrayList();
/* 106 */     for (int k = SectionPos.blockToSectionCoord(boundingBox.minZ()); k <= SectionPos.blockToSectionCoord(boundingBox.maxZ()); k++) {
/* 107 */       for (int m = SectionPos.blockToSectionCoord(boundingBox.minX()); m <= SectionPos.blockToSectionCoord(boundingBox.maxX()); m++) {
/* 108 */         ChunkAccess chunkAccess = paramServerLevel.getChunk(m, k, ChunkStatus.FULL, false);
/* 109 */         if (chunkAccess == null) {
/* 110 */           return Either.right(ERROR_NOT_LOADED.create());
/*     */         }
/* 112 */         arrayList.add(chunkAccess);
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     MutableInt mutableInt = new MutableInt(0);
/* 117 */     for (ChunkAccess chunkAccess : arrayList) {
/* 118 */       chunkAccess.fillBiomesFromNoise(makeResolver(mutableInt, chunkAccess, boundingBox, paramHolder, paramPredicate), paramServerLevel.getChunkSource().randomState().sampler());
/* 119 */       chunkAccess.markUnsaved();
/*     */     } 
/* 121 */     (paramServerLevel.getChunkSource()).chunkMap.resendBiomesForChunks(arrayList);
/*     */     
/* 123 */     paramConsumer.accept(() -> Component.translatable("commands.fillbiome.success.count", new Object[] { Integer.valueOf(paramMutableInt.intValue()), Integer.valueOf(paramBoundingBox.minX()), Integer.valueOf(paramBoundingBox.minY()), Integer.valueOf(paramBoundingBox.minZ()), Integer.valueOf(paramBoundingBox.maxX()), Integer.valueOf(paramBoundingBox.maxY()), Integer.valueOf(paramBoundingBox.maxZ()) }));
/* 124 */     return Either.left(Integer.valueOf(mutableInt.intValue()));
/*     */   }
/*     */   
/*     */   private static int fill(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Holder.Reference<Biome> paramReference, Predicate<Holder<Biome>> paramPredicate) throws CommandSyntaxException {
/* 128 */     Either<Integer, CommandSyntaxException> either = fill(paramCommandSourceStack.getLevel(), paramBlockPos1, paramBlockPos2, (Holder<Biome>)paramReference, paramPredicate, paramSupplier -> paramCommandSourceStack.sendSuccess(paramSupplier, true));
/* 129 */     Optional optional = either.right();
/* 130 */     if (optional.isPresent()) {
/* 131 */       throw (CommandSyntaxException)optional.get();
/*     */     }
/* 133 */     return ((Integer)either.left().get()).intValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\FillBiomeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */