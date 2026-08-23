/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
/*     */ import net.minecraft.commands.arguments.OperationArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.ScoreboardSlotArgument;
/*     */ import net.minecraft.commands.arguments.StyleArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.numbers.BlankFormat;
/*     */ import net.minecraft.network.chat.numbers.FixedFormat;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.StyledFormat;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.DisplaySlot;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreAccess;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
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
/*     */ public class ScoreboardCommand
/*     */ {
/*  68 */   private static final SimpleCommandExceptionType ERROR_OBJECTIVE_ALREADY_EXISTS = new SimpleCommandExceptionType((Message)Component.translatable("commands.scoreboard.objectives.add.duplicate"));
/*  69 */   private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_EMPTY = new SimpleCommandExceptionType((Message)Component.translatable("commands.scoreboard.objectives.display.alreadyEmpty"));
/*  70 */   private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_SET = new SimpleCommandExceptionType((Message)Component.translatable("commands.scoreboard.objectives.display.alreadySet"));
/*  71 */   private static final SimpleCommandExceptionType ERROR_TRIGGER_ALREADY_ENABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.scoreboard.players.enable.failed")); private static final Dynamic2CommandExceptionType ERROR_NO_VALUE;
/*  72 */   private static final SimpleCommandExceptionType ERROR_NOT_TRIGGER = new SimpleCommandExceptionType((Message)Component.translatable("commands.scoreboard.players.enable.invalid")); static {
/*  73 */     ERROR_NO_VALUE = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.scoreboard.players.get.null", new Object[] { paramObject1, paramObject2 }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  76 */     paramCommandDispatcher.register(
/*  77 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("scoreboard")
/*  78 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  79 */         .then((
/*  80 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("objectives")
/*  81 */           .then(
/*  82 */             Commands.literal("list")
/*  83 */             .executes(paramCommandContext -> listObjectives((CommandSourceStack)paramCommandContext.getSource()))))
/*     */           
/*  85 */           .then(
/*  86 */             Commands.literal("add")
/*  87 */             .then(
/*  88 */               Commands.argument("objective", (ArgumentType)StringArgumentType.word())
/*  89 */               .then((
/*  90 */                 (RequiredArgumentBuilder)Commands.argument("criteria", (ArgumentType)ObjectiveCriteriaArgument.criteria())
/*  91 */                 .executes(paramCommandContext -> addObjective((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "objective"), ObjectiveCriteriaArgument.getCriteria(paramCommandContext, "criteria"), (Component)Component.literal(StringArgumentType.getString(paramCommandContext, "objective")))))
/*  92 */                 .then(
/*  93 */                   Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  94 */                   .executes(paramCommandContext -> addObjective((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "objective"), ObjectiveCriteriaArgument.getCriteria(paramCommandContext, "criteria"), ComponentArgument.getResolvedComponent(paramCommandContext, "displayName"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  99 */           .then(
/* 100 */             Commands.literal("modify")
/* 101 */             .then((
/* 102 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 103 */               .then(
/* 104 */                 Commands.literal("displayname")
/* 105 */                 .then(
/* 106 */                   Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 107 */                   .executes(paramCommandContext -> setDisplayName((CommandSourceStack)paramCommandContext.getSource(), ObjectiveArgument.getObjective(paramCommandContext, "objective"), ComponentArgument.getResolvedComponent(paramCommandContext, "displayName"))))))
/*     */               
/* 109 */               .then((ArgumentBuilder)createRenderTypeModify()))
/* 110 */               .then(
/* 111 */                 Commands.literal("displayautoupdate")
/* 112 */                 .then(
/* 113 */                   Commands.argument("value", (ArgumentType)BoolArgumentType.bool())
/* 114 */                   .executes(paramCommandContext -> setDisplayAutoUpdate((CommandSourceStack)paramCommandContext.getSource(), ObjectiveArgument.getObjective(paramCommandContext, "objective"), BoolArgumentType.getBool(paramCommandContext, "value"))))))
/*     */ 
/*     */               
/* 117 */               .then(
/* 118 */                 addNumberFormats(paramCommandBuildContext, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("numberformat"), (paramCommandContext, paramNumberFormat) -> setObjectiveFormat((CommandSourceStack)paramCommandContext.getSource(), ObjectiveArgument.getObjective(paramCommandContext, "objective"), paramNumberFormat))))))
/*     */ 
/*     */ 
/*     */           
/* 122 */           .then(
/* 123 */             Commands.literal("remove")
/* 124 */             .then(
/* 125 */               Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 126 */               .executes(paramCommandContext -> removeObjective((CommandSourceStack)paramCommandContext.getSource(), ObjectiveArgument.getObjective(paramCommandContext, "objective"))))))
/*     */ 
/*     */           
/* 129 */           .then(
/* 130 */             Commands.literal("setdisplay")
/* 131 */             .then((
/* 132 */               (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)ScoreboardSlotArgument.displaySlot())
/* 133 */               .executes(paramCommandContext -> clearDisplaySlot((CommandSourceStack)paramCommandContext.getSource(), ScoreboardSlotArgument.getDisplaySlot(paramCommandContext, "slot"))))
/* 134 */               .then(
/* 135 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 136 */                 .executes(paramCommandContext -> setDisplaySlot((CommandSourceStack)paramCommandContext.getSource(), ScoreboardSlotArgument.getDisplaySlot(paramCommandContext, "slot"), ObjectiveArgument.getObjective(paramCommandContext, "objective"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 141 */         .then((
/* 142 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("players")
/* 143 */           .then((
/* 144 */             (LiteralArgumentBuilder)Commands.literal("list")
/* 145 */             .executes(paramCommandContext -> listTrackedPlayers((CommandSourceStack)paramCommandContext.getSource())))
/* 146 */             .then(
/* 147 */               Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 148 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 149 */               .executes(paramCommandContext -> listTrackedPlayerScores((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getName(paramCommandContext, "target"))))))
/*     */ 
/*     */           
/* 152 */           .then(
/* 153 */             Commands.literal("set")
/* 154 */             .then(
/* 155 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 156 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 157 */               .then(
/* 158 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 159 */                 .then(
/* 160 */                   Commands.argument("score", (ArgumentType)IntegerArgumentType.integer())
/* 161 */                   .executes(paramCommandContext -> setScore((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getWritableObjective(paramCommandContext, "objective"), IntegerArgumentType.getInteger(paramCommandContext, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 166 */           .then(
/* 167 */             Commands.literal("get")
/* 168 */             .then(
/* 169 */               Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 170 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 171 */               .then(
/* 172 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 173 */                 .executes(paramCommandContext -> getScore((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getName(paramCommandContext, "target"), ObjectiveArgument.getObjective(paramCommandContext, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 177 */           .then(
/* 178 */             Commands.literal("add")
/* 179 */             .then(
/* 180 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 181 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 182 */               .then(
/* 183 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 184 */                 .then(
/* 185 */                   Commands.argument("score", (ArgumentType)IntegerArgumentType.integer(0))
/* 186 */                   .executes(paramCommandContext -> addScore((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getWritableObjective(paramCommandContext, "objective"), IntegerArgumentType.getInteger(paramCommandContext, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 191 */           .then(
/* 192 */             Commands.literal("remove")
/* 193 */             .then(
/* 194 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 195 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 196 */               .then(
/* 197 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 198 */                 .then(
/* 199 */                   Commands.argument("score", (ArgumentType)IntegerArgumentType.integer(0))
/* 200 */                   .executes(paramCommandContext -> removeScore((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getWritableObjective(paramCommandContext, "objective"), IntegerArgumentType.getInteger(paramCommandContext, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 205 */           .then(
/* 206 */             Commands.literal("reset")
/* 207 */             .then((
/* 208 */               (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 209 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 210 */               .executes(paramCommandContext -> resetScores((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"))))
/* 211 */               .then(
/* 212 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 213 */                 .executes(paramCommandContext -> resetScore((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getObjective(paramCommandContext, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 217 */           .then(
/* 218 */             Commands.literal("enable")
/* 219 */             .then(
/* 220 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 221 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 222 */               .then(
/* 223 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 224 */                 .suggests((paramCommandContext, paramSuggestionsBuilder) -> suggestTriggers((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), paramSuggestionsBuilder))
/* 225 */                 .executes(paramCommandContext -> enableTrigger((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getObjective(paramCommandContext, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 229 */           .then((
/* 230 */             (LiteralArgumentBuilder)Commands.literal("display")
/* 231 */             .then(
/* 232 */               Commands.literal("name")
/* 233 */               .then(
/* 234 */                 Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 235 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 236 */                 .then((
/* 237 */                   (RequiredArgumentBuilder)Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 238 */                   .then(
/* 239 */                     Commands.argument("name", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 240 */                     .executes(paramCommandContext -> setScoreDisplay((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getObjective(paramCommandContext, "objective"), ComponentArgument.getResolvedComponent(paramCommandContext, "name")))))
/*     */                   
/* 242 */                   .executes(paramCommandContext -> setScoreDisplay((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getObjective(paramCommandContext, "objective"), null))))))
/*     */ 
/*     */ 
/*     */             
/* 246 */             .then(
/* 247 */               Commands.literal("numberformat")
/* 248 */               .then(
/* 249 */                 Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 250 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 251 */                 .then(
/* 252 */                   addNumberFormats(paramCommandBuildContext, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective()), (paramCommandContext, paramNumberFormat) -> setScoreNumberFormat((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getObjective(paramCommandContext, "objective"), paramNumberFormat)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 259 */           .then(
/* 260 */             Commands.literal("operation")
/* 261 */             .then(
/* 262 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 263 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 264 */               .then(
/* 265 */                 Commands.argument("targetObjective", (ArgumentType)ObjectiveArgument.objective())
/* 266 */                 .then(
/* 267 */                   Commands.argument("operation", (ArgumentType)OperationArgument.operation())
/* 268 */                   .then(
/* 269 */                     Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 270 */                     .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 271 */                     .then(
/* 272 */                       Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective())
/* 273 */                       .executes(paramCommandContext -> performOperation((CommandSourceStack)paramCommandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "targets"), ObjectiveArgument.getWritableObjective(paramCommandContext, "targetObjective"), OperationArgument.getOperation(paramCommandContext, "operation"), ScoreHolderArgument.getNamesWithDefaultWildcard(paramCommandContext, "source"), ObjectiveArgument.getObjective(paramCommandContext, "sourceObjective")))))))))));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> addNumberFormats(CommandBuildContext paramCommandBuildContext, ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, NumberFormatCommandExecutor paramNumberFormatCommandExecutor) {
/* 290 */     return paramArgumentBuilder
/* 291 */       .then(
/* 292 */         Commands.literal("blank")
/* 293 */         .executes(paramCommandContext -> paramNumberFormatCommandExecutor.run(paramCommandContext, (NumberFormat)BlankFormat.INSTANCE)))
/*     */       
/* 295 */       .then(
/* 296 */         Commands.literal("fixed")
/* 297 */         .then(
/* 298 */           Commands.argument("contents", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 299 */           .executes(paramCommandContext -> {
/*     */               Component component = ComponentArgument.getResolvedComponent(paramCommandContext, "contents");
/*     */ 
/*     */ 
/*     */               
/*     */               return paramNumberFormatCommandExecutor.run(paramCommandContext, (NumberFormat)new FixedFormat(component));
/* 305 */             }))).then(
/* 306 */         Commands.literal("styled")
/* 307 */         .then(
/* 308 */           Commands.argument("style", (ArgumentType)StyleArgument.style(paramCommandBuildContext))
/* 309 */           .executes(paramCommandContext -> {
/*     */               Style style = StyleArgument.getStyle(paramCommandContext, "style");
/*     */ 
/*     */ 
/*     */               
/*     */               return paramNumberFormatCommandExecutor.run(paramCommandContext, (NumberFormat)new StyledFormat(style));
/* 315 */             }))).executes(paramCommandContext -> paramNumberFormatCommandExecutor.run(paramCommandContext, null));
/*     */   }
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> createRenderTypeModify() {
/* 319 */     LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = Commands.literal("rendertype");
/*     */     
/* 321 */     for (ObjectiveCriteria.RenderType renderType : ObjectiveCriteria.RenderType.values()) {
/* 322 */       literalArgumentBuilder.then(Commands.literal(renderType.getId())
/* 323 */           .executes(paramCommandContext -> setRenderType((CommandSourceStack)paramCommandContext.getSource(), ObjectiveArgument.getObjective(paramCommandContext, "objective"), paramRenderType)));
/*     */     }
/*     */     
/* 326 */     return literalArgumentBuilder;
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestTriggers(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, SuggestionsBuilder paramSuggestionsBuilder) {
/* 330 */     ArrayList<String> arrayList = Lists.newArrayList();
/* 331 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 333 */     for (Objective objective : serverScoreboard.getObjectives()) {
/* 334 */       if (objective.getCriteria() == ObjectiveCriteria.TRIGGER) {
/* 335 */         boolean bool = false;
/* 336 */         for (ScoreHolder scoreHolder : paramCollection) {
/* 337 */           ReadOnlyScoreInfo readOnlyScoreInfo = serverScoreboard.getPlayerScoreInfo(scoreHolder, objective);
/*     */           
/* 339 */           if (readOnlyScoreInfo == null || readOnlyScoreInfo.isLocked()) {
/* 340 */             bool = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 344 */         if (bool) {
/* 345 */           arrayList.add(objective.getName());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 350 */     return SharedSuggestionProvider.suggest(arrayList, paramSuggestionsBuilder);
/*     */   }
/*     */   
/*     */   private static int getScore(CommandSourceStack paramCommandSourceStack, ScoreHolder paramScoreHolder, Objective paramObjective) throws CommandSyntaxException {
/* 354 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 356 */     ReadOnlyScoreInfo readOnlyScoreInfo = serverScoreboard.getPlayerScoreInfo(paramScoreHolder, paramObjective);
/* 357 */     if (readOnlyScoreInfo == null) {
/* 358 */       throw ERROR_NO_VALUE.create(paramObjective.getName(), paramScoreHolder.getFeedbackDisplayName());
/*     */     }
/*     */     
/* 361 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.get.success", new Object[] { paramScoreHolder.getFeedbackDisplayName(), Integer.valueOf(paramReadOnlyScoreInfo.value()), paramObjective.getFormattedDisplayName() }), false);
/*     */     
/* 363 */     return readOnlyScoreInfo.value();
/*     */   }
/*     */   
/*     */   private static Component getFirstTargetName(Collection<ScoreHolder> paramCollection) {
/* 367 */     return ((ScoreHolder)paramCollection.iterator().next()).getFeedbackDisplayName();
/*     */   }
/*     */   
/*     */   private static int performOperation(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection1, Objective paramObjective1, OperationArgument.Operation paramOperation, Collection<ScoreHolder> paramCollection2, Objective paramObjective2) throws CommandSyntaxException {
/* 371 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 372 */     int i = 0;
/*     */     
/* 374 */     for (ScoreHolder scoreHolder : paramCollection1) {
/* 375 */       ScoreAccess scoreAccess = serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective1);
/* 376 */       for (ScoreHolder scoreHolder1 : paramCollection2) {
/* 377 */         ScoreAccess scoreAccess1 = serverScoreboard.getOrCreatePlayerScore(scoreHolder1, paramObjective2);
/* 378 */         paramOperation.apply(scoreAccess, scoreAccess1);
/*     */       } 
/* 380 */       i += scoreAccess.get();
/*     */     } 
/*     */     
/* 383 */     if (paramCollection1.size() == 1) {
/* 384 */       int j = i;
/* 385 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.operation.success.single", new Object[] { paramObjective.getFormattedDisplayName(), getFirstTargetName(paramCollection), Integer.valueOf(paramInt) }), true);
/*     */     } else {
/* 387 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.operation.success.multiple", new Object[] { paramObjective.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 390 */     return i;
/*     */   }
/*     */   
/*     */   private static int enableTrigger(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective) throws CommandSyntaxException {
/* 394 */     if (paramObjective.getCriteria() != ObjectiveCriteria.TRIGGER) {
/* 395 */       throw ERROR_NOT_TRIGGER.create();
/*     */     }
/* 397 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 399 */     byte b = 0;
/*     */     
/* 401 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 402 */       ScoreAccess scoreAccess = serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective);
/* 403 */       if (scoreAccess.locked()) {
/* 404 */         scoreAccess.unlock();
/* 405 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 409 */     if (b == 0) {
/* 410 */       throw ERROR_TRIGGER_ALREADY_ENABLED.create();
/*     */     }
/*     */     
/* 413 */     if (paramCollection.size() == 1) {
/* 414 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.enable.success.single", new Object[] { paramObjective.getFormattedDisplayName(), getFirstTargetName(paramCollection) }), true);
/*     */     } else {
/* 416 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.enable.success.multiple", new Object[] { paramObjective.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 419 */     return b;
/*     */   }
/*     */   
/*     */   private static int resetScores(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection) {
/* 423 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 425 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 426 */       serverScoreboard.resetAllPlayerScores(scoreHolder);
/*     */     }
/*     */     
/* 429 */     if (paramCollection.size() == 1) {
/* 430 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.all.single", new Object[] { getFirstTargetName(paramCollection) }), true);
/*     */     } else {
/* 432 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.all.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 435 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int resetScore(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective) {
/* 439 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 441 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 442 */       serverScoreboard.resetSinglePlayerScore(scoreHolder, paramObjective);
/*     */     }
/*     */     
/* 445 */     if (paramCollection.size() == 1) {
/* 446 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.specific.single", new Object[] { paramObjective.getFormattedDisplayName(), getFirstTargetName(paramCollection) }), true);
/*     */     } else {
/* 448 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.reset.specific.multiple", new Object[] { paramObjective.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 451 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int setScore(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective, int paramInt) {
/* 455 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 457 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 458 */       serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective).set(paramInt);
/*     */     }
/*     */     
/* 461 */     if (paramCollection.size() == 1) {
/* 462 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.set.success.single", new Object[] { paramObjective.getFormattedDisplayName(), getFirstTargetName(paramCollection), Integer.valueOf(paramInt) }), true);
/*     */     } else {
/* 464 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.set.success.multiple", new Object[] { paramObjective.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()), Integer.valueOf(paramInt) }), true);
/*     */     } 
/*     */     
/* 467 */     return paramInt * paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int setScoreDisplay(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective, Component paramComponent) {
/* 471 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 473 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 474 */       serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective).display(paramComponent);
/*     */     }
/*     */     
/* 477 */     if (paramComponent == null) {
/* 478 */       if (paramCollection.size() == 1) {
/* 479 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.clear.success.single", new Object[] { getFirstTargetName(paramCollection), paramObjective.getFormattedDisplayName() }), true);
/*     */       } else {
/* 481 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.clear.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()), paramObjective.getFormattedDisplayName() }), true);
/*     */       }
/*     */     
/* 484 */     } else if (paramCollection.size() == 1) {
/* 485 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.set.success.single", new Object[] { paramComponent, getFirstTargetName(paramCollection), paramObjective.getFormattedDisplayName() }), true);
/*     */     } else {
/* 487 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.name.set.success.multiple", new Object[] { paramComponent, Integer.valueOf(paramCollection.size()), paramObjective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */ 
/*     */     
/* 491 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int setScoreNumberFormat(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective, NumberFormat paramNumberFormat) {
/* 495 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 497 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 498 */       serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective).numberFormatOverride(paramNumberFormat);
/*     */     }
/*     */     
/* 501 */     if (paramNumberFormat == null) {
/* 502 */       if (paramCollection.size() == 1) {
/* 503 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.clear.success.single", new Object[] { getFirstTargetName(paramCollection), paramObjective.getFormattedDisplayName() }), true);
/*     */       } else {
/* 505 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.clear.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()), paramObjective.getFormattedDisplayName() }), true);
/*     */       }
/*     */     
/* 508 */     } else if (paramCollection.size() == 1) {
/* 509 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.set.success.single", new Object[] { getFirstTargetName(paramCollection), paramObjective.getFormattedDisplayName() }), true);
/*     */     } else {
/* 511 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.display.numberFormat.set.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()), paramObjective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */ 
/*     */     
/* 515 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int addScore(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective, int paramInt) {
/* 519 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 520 */     int i = 0;
/*     */     
/* 522 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 523 */       ScoreAccess scoreAccess = serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective);
/* 524 */       scoreAccess.set(scoreAccess.get() + paramInt);
/* 525 */       i += scoreAccess.get();
/*     */     } 
/*     */     
/* 528 */     if (paramCollection.size() == 1) {
/* 529 */       int j = i;
/* 530 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.add.success.single", new Object[] { Integer.valueOf(paramInt1), paramObjective.getFormattedDisplayName(), getFirstTargetName(paramCollection), Integer.valueOf(paramInt2) }), true);
/*     */     } else {
/* 532 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.add.success.multiple", new Object[] { Integer.valueOf(paramInt), paramObjective.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 535 */     return i;
/*     */   }
/*     */   
/*     */   private static int removeScore(CommandSourceStack paramCommandSourceStack, Collection<ScoreHolder> paramCollection, Objective paramObjective, int paramInt) {
/* 539 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 540 */     int i = 0;
/*     */     
/* 542 */     for (ScoreHolder scoreHolder : paramCollection) {
/* 543 */       ScoreAccess scoreAccess = serverScoreboard.getOrCreatePlayerScore(scoreHolder, paramObjective);
/* 544 */       scoreAccess.set(scoreAccess.get() - paramInt);
/* 545 */       i += scoreAccess.get();
/*     */     } 
/*     */     
/* 548 */     if (paramCollection.size() == 1) {
/* 549 */       int j = i;
/* 550 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.remove.success.single", new Object[] { Integer.valueOf(paramInt1), paramObjective.getFormattedDisplayName(), getFirstTargetName(paramCollection), Integer.valueOf(paramInt2) }), true);
/*     */     } else {
/* 552 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.remove.success.multiple", new Object[] { Integer.valueOf(paramInt), paramObjective.getFormattedDisplayName(), Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 555 */     return i;
/*     */   }
/*     */   
/*     */   private static int listTrackedPlayers(CommandSourceStack paramCommandSourceStack) {
/* 559 */     Collection collection = paramCommandSourceStack.getServer().getScoreboard().getTrackedPlayers();
/*     */     
/* 561 */     if (collection.isEmpty()) {
/* 562 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.empty"), false);
/*     */     } else {
/* 564 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.success", new Object[] { Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection, ScoreHolder::getFeedbackDisplayName) }), false);
/*     */     } 
/*     */     
/* 567 */     return collection.size();
/*     */   }
/*     */   
/*     */   private static int listTrackedPlayerScores(CommandSourceStack paramCommandSourceStack, ScoreHolder paramScoreHolder) {
/* 571 */     Object2IntMap object2IntMap = paramCommandSourceStack.getServer().getScoreboard().listPlayerScores(paramScoreHolder);
/*     */     
/* 573 */     if (object2IntMap.isEmpty()) {
/* 574 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.empty", new Object[] { paramScoreHolder.getFeedbackDisplayName() }), false);
/*     */     } else {
/* 576 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.players.list.entity.success", new Object[] { paramScoreHolder.getFeedbackDisplayName(), Integer.valueOf(paramObject2IntMap.size()) }), false);
/*     */       
/* 578 */       Object2IntMaps.fastForEach(object2IntMap, paramEntry -> paramCommandSourceStack.sendSuccess((), false));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 583 */     return object2IntMap.size();
/*     */   }
/*     */   
/*     */   private static int clearDisplaySlot(CommandSourceStack paramCommandSourceStack, DisplaySlot paramDisplaySlot) throws CommandSyntaxException {
/* 587 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 589 */     if (serverScoreboard.getDisplayObjective(paramDisplaySlot) == null) {
/* 590 */       throw ERROR_DISPLAY_SLOT_ALREADY_EMPTY.create();
/*     */     }
/*     */     
/* 593 */     serverScoreboard.setDisplayObjective(paramDisplaySlot, null);
/* 594 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.display.cleared", new Object[] { paramDisplaySlot.getSerializedName() }), true);
/*     */     
/* 596 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplaySlot(CommandSourceStack paramCommandSourceStack, DisplaySlot paramDisplaySlot, Objective paramObjective) throws CommandSyntaxException {
/* 600 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 602 */     if (serverScoreboard.getDisplayObjective(paramDisplaySlot) == paramObjective) {
/* 603 */       throw ERROR_DISPLAY_SLOT_ALREADY_SET.create();
/*     */     }
/*     */     
/* 606 */     serverScoreboard.setDisplayObjective(paramDisplaySlot, paramObjective);
/* 607 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.display.set", new Object[] { paramDisplaySlot.getSerializedName(), paramObjective.getDisplayName() }), true);
/*     */     
/* 609 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack paramCommandSourceStack, Objective paramObjective, Component paramComponent) {
/* 613 */     if (!paramObjective.getDisplayName().equals(paramComponent)) {
/* 614 */       paramObjective.setDisplayName(paramComponent);
/* 615 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayname", new Object[] { paramObjective.getName(), paramObjective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 618 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayAutoUpdate(CommandSourceStack paramCommandSourceStack, Objective paramObjective, boolean paramBoolean) {
/* 622 */     if (paramObjective.displayAutoUpdate() != paramBoolean) {
/* 623 */       paramObjective.setDisplayAutoUpdate(paramBoolean);
/* 624 */       if (paramBoolean) {
/* 625 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayAutoUpdate.enable", new Object[] { paramObjective.getName(), paramObjective.getFormattedDisplayName() }), true);
/*     */       } else {
/* 627 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.displayAutoUpdate.disable", new Object[] { paramObjective.getName(), paramObjective.getFormattedDisplayName() }), true);
/*     */       } 
/*     */     } 
/*     */     
/* 631 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setObjectiveFormat(CommandSourceStack paramCommandSourceStack, Objective paramObjective, NumberFormat paramNumberFormat) {
/* 635 */     paramObjective.setNumberFormat(paramNumberFormat);
/* 636 */     if (paramNumberFormat != null) {
/* 637 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.objectiveFormat.set", new Object[] { paramObjective.getName() }), true);
/*     */     } else {
/* 639 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.objectiveFormat.clear", new Object[] { paramObjective.getName() }), true);
/*     */     } 
/* 641 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setRenderType(CommandSourceStack paramCommandSourceStack, Objective paramObjective, ObjectiveCriteria.RenderType paramRenderType) {
/* 645 */     if (paramObjective.getRenderType() != paramRenderType) {
/* 646 */       paramObjective.setRenderType(paramRenderType);
/* 647 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.modify.rendertype", new Object[] { paramObjective.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 650 */     return 0;
/*     */   }
/*     */   
/*     */   private static int removeObjective(CommandSourceStack paramCommandSourceStack, Objective paramObjective) {
/* 654 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/* 655 */     serverScoreboard.removeObjective(paramObjective);
/* 656 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.remove.success", new Object[] { paramObjective.getFormattedDisplayName() }), true);
/* 657 */     return serverScoreboard.getObjectives().size();
/*     */   }
/*     */   
/*     */   private static int addObjective(CommandSourceStack paramCommandSourceStack, String paramString, ObjectiveCriteria paramObjectiveCriteria, Component paramComponent) throws CommandSyntaxException {
/* 661 */     ServerScoreboard serverScoreboard = paramCommandSourceStack.getServer().getScoreboard();
/*     */     
/* 663 */     if (serverScoreboard.getObjective(paramString) != null) {
/* 664 */       throw ERROR_OBJECTIVE_ALREADY_EXISTS.create();
/*     */     }
/*     */     
/* 667 */     serverScoreboard.addObjective(paramString, paramObjectiveCriteria, paramComponent, paramObjectiveCriteria.getDefaultRenderType(), false, null);
/* 668 */     Objective objective = serverScoreboard.getObjective(paramString);
/*     */     
/* 670 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.add.success", new Object[] { paramObjective.getFormattedDisplayName() }), true);
/*     */     
/* 672 */     return serverScoreboard.getObjectives().size();
/*     */   }
/*     */   
/*     */   private static int listObjectives(CommandSourceStack paramCommandSourceStack) {
/* 676 */     Collection collection = paramCommandSourceStack.getServer().getScoreboard().getObjectives();
/*     */     
/* 678 */     if (collection.isEmpty()) {
/* 679 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.list.empty"), false);
/*     */     } else {
/* 681 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.scoreboard.objectives.list.success", new Object[] { Integer.valueOf(paramCollection.size()), ComponentUtils.formatList(paramCollection, Objective::getFormattedDisplayName) }), false);
/*     */     } 
/*     */     
/* 684 */     return collection.size();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface NumberFormatCommandExecutor {
/*     */     int run(CommandContext<CommandSourceStack> param1CommandContext, NumberFormat param1NumberFormat) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ScoreboardCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */