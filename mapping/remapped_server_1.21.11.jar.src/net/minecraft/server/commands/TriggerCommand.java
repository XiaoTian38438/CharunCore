/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreAccess;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public class TriggerCommand {
/*  31 */   private static final SimpleCommandExceptionType ERROR_NOT_PRIMED = new SimpleCommandExceptionType((Message)Component.translatable("commands.trigger.failed.unprimed"));
/*  32 */   private static final SimpleCommandExceptionType ERROR_INVALID_OBJECTIVE = new SimpleCommandExceptionType((Message)Component.translatable("commands.trigger.failed.invalid"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  35 */     paramCommandDispatcher.register(
/*  36 */         (LiteralArgumentBuilder)Commands.literal("trigger")
/*  37 */         .then((
/*  38 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/*  39 */           .suggests((paramCommandContext, paramSuggestionsBuilder) -> suggestObjectives((CommandSourceStack)paramCommandContext.getSource(), paramSuggestionsBuilder))
/*  40 */           .executes(paramCommandContext -> simpleTrigger((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(paramCommandContext, "objective"))))
/*  41 */           .then(
/*  42 */             Commands.literal("add")
/*  43 */             .then(
/*  44 */               Commands.argument("value", (ArgumentType)IntegerArgumentType.integer())
/*  45 */               .executes(paramCommandContext -> addValue((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(paramCommandContext, "objective"), IntegerArgumentType.getInteger(paramCommandContext, "value"))))))
/*     */ 
/*     */           
/*  48 */           .then(
/*  49 */             Commands.literal("set")
/*  50 */             .then(
/*  51 */               Commands.argument("value", (ArgumentType)IntegerArgumentType.integer())
/*  52 */               .executes(paramCommandContext -> setValue((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(paramCommandContext, "objective"), IntegerArgumentType.getInteger(paramCommandContext, "value")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Suggestions> suggestObjectives(CommandSourceStack paramCommandSourceStack, SuggestionsBuilder paramSuggestionsBuilder) {
/*  60 */     Entity entity = paramCommandSourceStack.getEntity();
/*  61 */     ArrayList<String> arrayList = Lists.newArrayList();
/*     */     
/*  63 */     if (entity != null) {
/*  64 */       ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */       
/*  66 */       for (Objective objective : serverScoreboard.getObjectives()) {
/*  67 */         if (objective.getCriteria() == ObjectiveCriteria.TRIGGER) {
/*  68 */           ReadOnlyScoreInfo readOnlyScoreInfo = serverScoreboard.getPlayerScoreInfo((ScoreHolder)entity, objective);
/*  69 */           if (readOnlyScoreInfo != null && !readOnlyScoreInfo.isLocked()) {
/*  70 */             arrayList.add(objective.getName());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  76 */     return SharedSuggestionProvider.suggest(arrayList, paramSuggestionsBuilder);
/*     */   }
/*     */   
/*     */   private static int addValue(CommandSourceStack paramCommandSourceStack, ServerPlayer paramServerPlayer, Objective paramObjective, int paramInt) throws CommandSyntaxException {
/*  80 */     ScoreAccess scoreAccess = getScore((Scoreboard)paramCommandSourceStack.getServer().getScoreboard(), (ScoreHolder)paramServerPlayer, paramObjective);
/*  81 */     int i = scoreAccess.add(paramInt);
/*  82 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.trigger.add.success", new Object[] { paramObjective.getFormattedDisplayName(), Integer.valueOf(paramInt) }), true);
/*  83 */     return i;
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack paramCommandSourceStack, ServerPlayer paramServerPlayer, Objective paramObjective, int paramInt) throws CommandSyntaxException {
/*  87 */     ScoreAccess scoreAccess = getScore((Scoreboard)paramCommandSourceStack.getServer().getScoreboard(), (ScoreHolder)paramServerPlayer, paramObjective);
/*  88 */     scoreAccess.set(paramInt);
/*  89 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.trigger.set.success", new Object[] { paramObjective.getFormattedDisplayName(), Integer.valueOf(paramInt) }), true);
/*  90 */     return paramInt;
/*     */   }
/*     */   
/*     */   private static int simpleTrigger(CommandSourceStack paramCommandSourceStack, ServerPlayer paramServerPlayer, Objective paramObjective) throws CommandSyntaxException {
/*  94 */     ScoreAccess scoreAccess = getScore((Scoreboard)paramCommandSourceStack.getServer().getScoreboard(), (ScoreHolder)paramServerPlayer, paramObjective);
/*  95 */     int i = scoreAccess.add(1);
/*  96 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.trigger.simple.success", new Object[] { paramObjective.getFormattedDisplayName() }), true);
/*  97 */     return i;
/*     */   }
/*     */   
/*     */   private static ScoreAccess getScore(Scoreboard paramScoreboard, ScoreHolder paramScoreHolder, Objective paramObjective) throws CommandSyntaxException {
/* 101 */     if (paramObjective.getCriteria() != ObjectiveCriteria.TRIGGER) {
/* 102 */       throw ERROR_INVALID_OBJECTIVE.create();
/*     */     }
/*     */     
/* 105 */     ReadOnlyScoreInfo readOnlyScoreInfo = paramScoreboard.getPlayerScoreInfo(paramScoreHolder, paramObjective);
/*     */     
/* 107 */     if (readOnlyScoreInfo == null || readOnlyScoreInfo.isLocked()) {
/* 108 */       throw ERROR_NOT_PRIMED.create();
/*     */     }
/*     */     
/* 111 */     ScoreAccess scoreAccess = paramScoreboard.getOrCreatePlayerScore(paramScoreHolder, paramObjective);
/* 112 */     scoreAccess.lock();
/* 113 */     return scoreAccess;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TriggerCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */