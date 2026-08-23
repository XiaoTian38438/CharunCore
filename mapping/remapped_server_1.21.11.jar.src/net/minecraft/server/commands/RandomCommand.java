/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.RangeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.RandomSequence;
/*     */ import net.minecraft.world.RandomSequences;
/*     */ 
/*     */ 
/*     */ public class RandomCommand
/*     */ {
/*  38 */   private static final SimpleCommandExceptionType ERROR_RANGE_TOO_LARGE = new SimpleCommandExceptionType((Message)Component.translatable("commands.random.error.range_too_large"));
/*  39 */   private static final SimpleCommandExceptionType ERROR_RANGE_TOO_SMALL = new SimpleCommandExceptionType((Message)Component.translatable("commands.random.error.range_too_small"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  42 */     paramCommandDispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("random")
/*  44 */         .then(
/*  45 */           (ArgumentBuilder)drawRandomValueTree("value", false)))
/*     */         
/*  47 */         .then(
/*  48 */           (ArgumentBuilder)drawRandomValueTree("roll", true)))
/*     */         
/*  50 */         .then((
/*  51 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reset")
/*  52 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  53 */           .then((
/*  54 */             (LiteralArgumentBuilder)Commands.literal("*")
/*  55 */             .executes(paramCommandContext -> resetAllSequences((CommandSourceStack)paramCommandContext.getSource())))
/*  56 */             .then((
/*  57 */               (RequiredArgumentBuilder)Commands.argument("seed", (ArgumentType)IntegerArgumentType.integer())
/*  58 */               .executes(paramCommandContext -> resetAllSequencesAndSetNewDefaults((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "seed"), true, true)))
/*     */ 
/*     */               
/*  61 */               .then((
/*  62 */                 (RequiredArgumentBuilder)Commands.argument("includeWorldSeed", (ArgumentType)BoolArgumentType.bool())
/*  63 */                 .executes(paramCommandContext -> resetAllSequencesAndSetNewDefaults((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "seed"), BoolArgumentType.getBool(paramCommandContext, "includeWorldSeed"), true)))
/*     */ 
/*     */                 
/*  66 */                 .then(
/*  67 */                   Commands.argument("includeSequenceId", (ArgumentType)BoolArgumentType.bool())
/*  68 */                   .executes(paramCommandContext -> resetAllSequencesAndSetNewDefaults((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "seed"), BoolArgumentType.getBool(paramCommandContext, "includeWorldSeed"), BoolArgumentType.getBool(paramCommandContext, "includeSequenceId"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  75 */           .then((
/*  76 */             (RequiredArgumentBuilder)Commands.argument("sequence", (ArgumentType)IdentifierArgument.id())
/*  77 */             .suggests(RandomCommand::suggestRandomSequence)
/*  78 */             .executes(paramCommandContext -> resetSequence((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "sequence"))))
/*  79 */             .then((
/*  80 */               (RequiredArgumentBuilder)Commands.argument("seed", (ArgumentType)IntegerArgumentType.integer())
/*  81 */               .executes(paramCommandContext -> resetSequence((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "sequence"), IntegerArgumentType.getInteger(paramCommandContext, "seed"), true, true)))
/*     */ 
/*     */               
/*  84 */               .then((
/*  85 */                 (RequiredArgumentBuilder)Commands.argument("includeWorldSeed", (ArgumentType)BoolArgumentType.bool())
/*  86 */                 .executes(paramCommandContext -> resetSequence((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "sequence"), IntegerArgumentType.getInteger(paramCommandContext, "seed"), BoolArgumentType.getBool(paramCommandContext, "includeWorldSeed"), true)))
/*     */ 
/*     */                 
/*  89 */                 .then(
/*  90 */                   Commands.argument("includeSequenceId", (ArgumentType)BoolArgumentType.bool())
/*  91 */                   .executes(paramCommandContext -> resetSequence((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "sequence"), IntegerArgumentType.getInteger(paramCommandContext, "seed"), BoolArgumentType.getBool(paramCommandContext, "includeWorldSeed"), BoolArgumentType.getBool(paramCommandContext, "includeSequenceId")))))))));
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
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> drawRandomValueTree(String paramString, boolean paramBoolean) {
/* 103 */     return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal(paramString)
/* 104 */       .then((
/* 105 */         (RequiredArgumentBuilder)Commands.argument("range", (ArgumentType)RangeArgument.intRange())
/* 106 */         .executes(paramCommandContext -> randomSample((CommandSourceStack)paramCommandContext.getSource(), RangeArgument.Ints.getRange(paramCommandContext, "range"), null, paramBoolean)))
/* 107 */         .then((
/* 108 */           (RequiredArgumentBuilder)Commands.argument("sequence", (ArgumentType)IdentifierArgument.id())
/* 109 */           .suggests(RandomCommand::suggestRandomSequence)
/* 110 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 111 */           .executes(paramCommandContext -> randomSample((CommandSourceStack)paramCommandContext.getSource(), RangeArgument.Ints.getRange(paramCommandContext, "range"), IdentifierArgument.getId(paramCommandContext, "sequence"), paramBoolean))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestRandomSequence(CommandContext<CommandSourceStack> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) {
/* 117 */     ArrayList arrayList = Lists.newArrayList();
/* 118 */     ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getRandomSequences().forAllSequences((paramIdentifier, paramRandomSequence) -> paramList.add(paramIdentifier.toString()));
/* 119 */     return SharedSuggestionProvider.suggest(arrayList, paramSuggestionsBuilder);
/*     */   }
/*     */   
/*     */   private static int randomSample(CommandSourceStack paramCommandSourceStack, MinMaxBounds.Ints paramInts, Identifier paramIdentifier, boolean paramBoolean) throws CommandSyntaxException {
/*     */     RandomSource randomSource;
/* 124 */     if (paramIdentifier != null) {
/* 125 */       randomSource = paramCommandSourceStack.getLevel().getRandomSequence(paramIdentifier);
/*     */     } else {
/* 127 */       randomSource = paramCommandSourceStack.getLevel().getRandom();
/*     */     } 
/*     */     
/* 130 */     int i = ((Integer)paramInts.min().orElse(Integer.valueOf(-2147483648))).intValue();
/* 131 */     int j = ((Integer)paramInts.max().orElse(Integer.valueOf(2147483647))).intValue();
/* 132 */     long l = j - i;
/* 133 */     if (l == 0L) {
/* 134 */       throw ERROR_RANGE_TOO_SMALL.create();
/*     */     }
/* 136 */     if (l >= 2147483647L) {
/* 137 */       throw ERROR_RANGE_TOO_LARGE.create();
/*     */     }
/* 139 */     int k = Mth.randomBetweenInclusive(randomSource, i, j);
/* 140 */     if (paramBoolean) {
/* 141 */       paramCommandSourceStack.getServer().getPlayerList().broadcastSystemMessage((Component)Component.translatable("commands.random.roll", new Object[] { paramCommandSourceStack.getDisplayName(), Integer.valueOf(k), Integer.valueOf(i), Integer.valueOf(j) }), false);
/*     */     } else {
/* 143 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.random.sample.success", new Object[] { Integer.valueOf(paramInt) }), false);
/*     */     } 
/*     */     
/* 146 */     return k;
/*     */   }
/*     */   
/*     */   private static int resetSequence(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier) throws CommandSyntaxException {
/* 150 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 151 */     serverLevel.getRandomSequences().reset(paramIdentifier, serverLevel.getSeed());
/* 152 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.success", new Object[] { Component.translationArg(paramIdentifier) }), false);
/* 153 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resetSequence(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier, int paramInt, boolean paramBoolean1, boolean paramBoolean2) throws CommandSyntaxException {
/* 157 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 158 */     serverLevel.getRandomSequences().reset(paramIdentifier, serverLevel.getSeed(), paramInt, paramBoolean1, paramBoolean2);
/* 159 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.success", new Object[] { Component.translationArg(paramIdentifier) }), false);
/* 160 */     return 1;
/*     */   }
/*     */   
/*     */   private static int resetAllSequences(CommandSourceStack paramCommandSourceStack) {
/* 164 */     int i = paramCommandSourceStack.getLevel().getRandomSequences().clear();
/* 165 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.all.success", new Object[] { Integer.valueOf(paramInt) }), false);
/* 166 */     return i;
/*     */   }
/*     */   
/*     */   private static int resetAllSequencesAndSetNewDefaults(CommandSourceStack paramCommandSourceStack, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
/* 170 */     RandomSequences randomSequences = paramCommandSourceStack.getLevel().getRandomSequences();
/* 171 */     randomSequences.setSeedDefaults(paramInt, paramBoolean1, paramBoolean2);
/* 172 */     int i = randomSequences.clear();
/* 173 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.all.success", new Object[] { Integer.valueOf(paramInt) }), false);
/* 174 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\RandomCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */