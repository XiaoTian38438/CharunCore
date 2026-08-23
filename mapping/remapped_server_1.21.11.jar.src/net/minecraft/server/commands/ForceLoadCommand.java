/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ColumnPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class ForceLoadCommand {
/*     */   private static final int MAX_CHUNK_LIMIT = 256;
/*     */   
/*     */   static {
/*  28 */     ERROR_TOO_MANY_CHUNKS = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.forceload.toobig", new Object[] { paramObject1, paramObject2 }));
/*  29 */     ERROR_NOT_TICKING = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.forceload.query.failure", new Object[] { paramObject1, paramObject2 }));
/*  30 */   } private static final Dynamic2CommandExceptionType ERROR_TOO_MANY_CHUNKS; private static final Dynamic2CommandExceptionType ERROR_NOT_TICKING; private static final SimpleCommandExceptionType ERROR_ALL_ADDED = new SimpleCommandExceptionType((Message)Component.translatable("commands.forceload.added.failure"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_NONE_REMOVED = new SimpleCommandExceptionType((Message)Component.translatable("commands.forceload.removed.failure"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  34 */     paramCommandDispatcher.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("forceload")
/*  36 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  37 */         .then(
/*  38 */           Commands.literal("add")
/*  39 */           .then((
/*  40 */             (RequiredArgumentBuilder)Commands.argument("from", (ArgumentType)ColumnPosArgument.columnPos())
/*  41 */             .executes(paramCommandContext -> changeForceLoad((CommandSourceStack)paramCommandContext.getSource(), ColumnPosArgument.getColumnPos(paramCommandContext, "from"), ColumnPosArgument.getColumnPos(paramCommandContext, "from"), true)))
/*  42 */             .then(
/*  43 */               Commands.argument("to", (ArgumentType)ColumnPosArgument.columnPos())
/*  44 */               .executes(paramCommandContext -> changeForceLoad((CommandSourceStack)paramCommandContext.getSource(), ColumnPosArgument.getColumnPos(paramCommandContext, "from"), ColumnPosArgument.getColumnPos(paramCommandContext, "to"), true))))))
/*     */         
/*  46 */         .then((
/*  47 */           (LiteralArgumentBuilder)Commands.literal("remove")
/*  48 */           .then((
/*  49 */             (RequiredArgumentBuilder)Commands.argument("from", (ArgumentType)ColumnPosArgument.columnPos())
/*  50 */             .executes(paramCommandContext -> changeForceLoad((CommandSourceStack)paramCommandContext.getSource(), ColumnPosArgument.getColumnPos(paramCommandContext, "from"), ColumnPosArgument.getColumnPos(paramCommandContext, "from"), false)))
/*  51 */             .then(
/*  52 */               Commands.argument("to", (ArgumentType)ColumnPosArgument.columnPos())
/*  53 */               .executes(paramCommandContext -> changeForceLoad((CommandSourceStack)paramCommandContext.getSource(), ColumnPosArgument.getColumnPos(paramCommandContext, "from"), ColumnPosArgument.getColumnPos(paramCommandContext, "to"), false)))))
/*  54 */           .then(
/*  55 */             Commands.literal("all")
/*  56 */             .executes(paramCommandContext -> removeAll((CommandSourceStack)paramCommandContext.getSource())))))
/*     */ 
/*     */         
/*  59 */         .then((
/*  60 */           (LiteralArgumentBuilder)Commands.literal("query")
/*  61 */           .executes(paramCommandContext -> listForceLoad((CommandSourceStack)paramCommandContext.getSource())))
/*  62 */           .then(
/*  63 */             Commands.argument("pos", (ArgumentType)ColumnPosArgument.columnPos())
/*  64 */             .executes(paramCommandContext -> queryForceLoad((CommandSourceStack)paramCommandContext.getSource(), ColumnPosArgument.getColumnPos(paramCommandContext, "pos"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int queryForceLoad(CommandSourceStack paramCommandSourceStack, ColumnPos paramColumnPos) throws CommandSyntaxException {
/*  71 */     ChunkPos chunkPos = paramColumnPos.toChunkPos();
/*  72 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/*  73 */     ResourceKey resourceKey = serverLevel.dimension();
/*  74 */     boolean bool = serverLevel.getForceLoadedChunks().contains(chunkPos.toLong());
/*     */     
/*  76 */     if (bool) {
/*  77 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.forceload.query.success", new Object[] { Component.translationArg(paramChunkPos), Component.translationArg(paramResourceKey.identifier()) }), false);
/*  78 */       return 1;
/*     */     } 
/*  80 */     throw ERROR_NOT_TICKING.create(chunkPos, resourceKey.identifier());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int listForceLoad(CommandSourceStack paramCommandSourceStack) {
/*  85 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/*  86 */     ResourceKey resourceKey = serverLevel.dimension();
/*  87 */     LongSet longSet = serverLevel.getForceLoadedChunks();
/*  88 */     int i = longSet.size();
/*     */     
/*  90 */     if (i > 0) {
/*  91 */       String str = Joiner.on(", ").join(longSet.stream().sorted().map(ChunkPos::new).map(ChunkPos::toString).iterator());
/*     */       
/*  93 */       if (i == 1) {
/*  94 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.forceload.list.single", new Object[] { Component.translationArg(paramResourceKey.identifier()), paramString }), false);
/*     */       } else {
/*  96 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.forceload.list.multiple", new Object[] { Integer.valueOf(paramInt), Component.translationArg(paramResourceKey.identifier()), paramString }), false);
/*     */       } 
/*     */     } else {
/*  99 */       paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.forceload.added.none", new Object[] { Component.translationArg(resourceKey.identifier()) }));
/*     */     } 
/* 101 */     return i;
/*     */   }
/*     */   
/*     */   private static int removeAll(CommandSourceStack paramCommandSourceStack) {
/* 105 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 106 */     ResourceKey resourceKey = serverLevel.dimension();
/* 107 */     LongSet longSet = serverLevel.getForceLoadedChunks();
/* 108 */     longSet.forEach(paramLong -> paramServerLevel.setChunkForced(ChunkPos.getX(paramLong), ChunkPos.getZ(paramLong), false));
/* 109 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.forceload.removed.all", new Object[] { Component.translationArg(paramResourceKey.identifier()) }), true);
/* 110 */     return 0;
/*     */   }
/*     */   
/*     */   private static int changeForceLoad(CommandSourceStack paramCommandSourceStack, ColumnPos paramColumnPos1, ColumnPos paramColumnPos2, boolean paramBoolean) throws CommandSyntaxException {
/* 114 */     int i = Math.min(paramColumnPos1.x(), paramColumnPos2.x());
/* 115 */     int j = Math.min(paramColumnPos1.z(), paramColumnPos2.z());
/* 116 */     int k = Math.max(paramColumnPos1.x(), paramColumnPos2.x());
/* 117 */     int m = Math.max(paramColumnPos1.z(), paramColumnPos2.z());
/*     */     
/* 119 */     if (i < -30000000 || j < -30000000 || k >= 30000000 || m >= 30000000)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 124 */       throw BlockPosArgument.ERROR_OUT_OF_WORLD.create();
/*     */     }
/*     */     
/* 127 */     int n = SectionPos.blockToSectionCoord(i);
/* 128 */     int i1 = SectionPos.blockToSectionCoord(j);
/* 129 */     int i2 = SectionPos.blockToSectionCoord(k);
/* 130 */     int i3 = SectionPos.blockToSectionCoord(m);
/*     */     
/* 132 */     long l = ((i2 - n) + 1L) * ((i3 - i1) + 1L);
/*     */     
/* 134 */     if (l > 256L) {
/* 135 */       throw ERROR_TOO_MANY_CHUNKS.create(Integer.valueOf(256), Long.valueOf(l));
/*     */     }
/*     */     
/* 138 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 139 */     ResourceKey resourceKey = serverLevel.dimension();
/*     */     
/* 141 */     ChunkPos chunkPos1 = null;
/* 142 */     byte b1 = 0;
/* 143 */     for (int i4 = n; i4 <= i2; i4++) {
/* 144 */       for (int i5 = i1; i5 <= i3; i5++) {
/* 145 */         boolean bool = serverLevel.setChunkForced(i4, i5, paramBoolean);
/* 146 */         if (bool) {
/* 147 */           b1++;
/* 148 */           if (chunkPos1 == null) {
/* 149 */             chunkPos1 = new ChunkPos(i4, i5);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     ChunkPos chunkPos2 = chunkPos1;
/* 156 */     byte b2 = b1;
/* 157 */     if (b2 == 0)
/* 158 */       throw (paramBoolean ? ERROR_ALL_ADDED : ERROR_NONE_REMOVED).create(); 
/* 159 */     if (b2 == 1) {
/* 160 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.forceload." + (paramBoolean ? "added" : "removed") + ".single", new Object[] { Component.translationArg(paramChunkPos), Component.translationArg(paramResourceKey.identifier()) }), true);
/*     */     } else {
/* 162 */       ChunkPos chunkPos3 = new ChunkPos(n, i1);
/* 163 */       ChunkPos chunkPos4 = new ChunkPos(i2, i3);
/* 164 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.forceload." + (paramBoolean ? "added" : "removed") + ".multiple", new Object[] { Integer.valueOf(paramInt), Component.translationArg(paramResourceKey.identifier()), Component.translationArg(paramChunkPos1), Component.translationArg(paramChunkPos2) }), true);
/*     */     } 
/*     */     
/* 167 */     return b2;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ForceLoadCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */