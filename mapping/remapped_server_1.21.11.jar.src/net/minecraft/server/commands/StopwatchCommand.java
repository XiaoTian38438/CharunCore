/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.Stopwatch;
/*     */ import net.minecraft.world.Stopwatches;
/*     */ 
/*     */ public class StopwatchCommand {
/*     */   private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS;
/*     */   
/*     */   static {
/*  24 */     ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.stopwatch.already_exists", new Object[] { paramObject }));
/*  25 */     ERROR_DOES_NOT_EXIST = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.stopwatch.does_not_exist", new Object[] { paramObject }));
/*  26 */     SUGGEST_STOPWATCHES = ((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack)paramCommandContext.getSource()).getServer().getStopwatches().ids(), paramSuggestionsBuilder));
/*     */   }
/*     */   public static final DynamicCommandExceptionType ERROR_DOES_NOT_EXIST; public static final SuggestionProvider<CommandSourceStack> SUGGEST_STOPWATCHES;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  30 */     paramCommandDispatcher.register(
/*  31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stopwatch")
/*  32 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  33 */         .then(
/*  34 */           Commands.literal("create")
/*  35 */           .then(
/*  36 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  37 */             .executes(paramCommandContext -> createStopwatch((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "id"))))))
/*     */ 
/*     */         
/*  40 */         .then(
/*  41 */           Commands.literal("query")
/*  42 */           .then((
/*  43 */             (RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  44 */             .suggests(SUGGEST_STOPWATCHES)
/*  45 */             .then(
/*  46 */               Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/*  47 */               .executes(paramCommandContext -> queryStopwatch((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "id"), DoubleArgumentType.getDouble(paramCommandContext, "scale")))))
/*     */             
/*  49 */             .executes(paramCommandContext -> queryStopwatch((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "id"), 1.0D)))))
/*     */ 
/*     */         
/*  52 */         .then(
/*  53 */           Commands.literal("restart")
/*  54 */           .then(
/*  55 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  56 */             .suggests(SUGGEST_STOPWATCHES)
/*  57 */             .executes(paramCommandContext -> restartStopwatch((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "id"))))))
/*     */ 
/*     */         
/*  60 */         .then(
/*  61 */           Commands.literal("remove")
/*  62 */           .then(
/*  63 */             Commands.argument("id", (ArgumentType)IdentifierArgument.id())
/*  64 */             .suggests(SUGGEST_STOPWATCHES)
/*  65 */             .executes(paramCommandContext -> removeStopwatch((CommandSourceStack)paramCommandContext.getSource(), IdentifierArgument.getId(paramCommandContext, "id"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int createStopwatch(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier) throws CommandSyntaxException {
/*  72 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/*  73 */     Stopwatches stopwatches = minecraftServer.getStopwatches();
/*  74 */     Stopwatch stopwatch = new Stopwatch(Stopwatches.currentTime());
/*  75 */     if (!stopwatches.add(paramIdentifier, stopwatch)) {
/*  76 */       throw ERROR_ALREADY_EXISTS.create(paramIdentifier);
/*     */     }
/*  78 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopwatch.create.success", new Object[] { Component.translationArg(paramIdentifier) }), true);
/*  79 */     return 1;
/*     */   }
/*     */   
/*     */   private static int queryStopwatch(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier, double paramDouble) throws CommandSyntaxException {
/*  83 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/*  84 */     Stopwatches stopwatches = minecraftServer.getStopwatches();
/*  85 */     Stopwatch stopwatch = stopwatches.get(paramIdentifier);
/*  86 */     if (stopwatch == null) {
/*  87 */       throw ERROR_DOES_NOT_EXIST.create(paramIdentifier);
/*     */     }
/*  89 */     long l = Stopwatches.currentTime();
/*  90 */     double d = stopwatch.elapsedSeconds(l);
/*  91 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopwatch.query", new Object[] { Component.translationArg(paramIdentifier), Double.valueOf(paramDouble) }), true);
/*  92 */     return (int)(d * paramDouble);
/*     */   }
/*     */   
/*     */   private static int restartStopwatch(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier) throws CommandSyntaxException {
/*  96 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/*  97 */     Stopwatches stopwatches = minecraftServer.getStopwatches();
/*  98 */     if (!stopwatches.update(paramIdentifier, paramStopwatch -> new Stopwatch(Stopwatches.currentTime()))) {
/*  99 */       throw ERROR_DOES_NOT_EXIST.create(paramIdentifier);
/*     */     }
/* 101 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopwatch.restart.success", new Object[] { Component.translationArg(paramIdentifier) }), true);
/* 102 */     return 1;
/*     */   }
/*     */   
/*     */   private static int removeStopwatch(CommandSourceStack paramCommandSourceStack, Identifier paramIdentifier) throws CommandSyntaxException {
/* 106 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/* 107 */     Stopwatches stopwatches = minecraftServer.getStopwatches();
/* 108 */     if (!stopwatches.remove(paramIdentifier)) {
/* 109 */       throw ERROR_DOES_NOT_EXIST.create(paramIdentifier);
/*     */     }
/* 111 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopwatch.remove.success", new Object[] { Component.translationArg(paramIdentifier) }), true);
/* 112 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\StopwatchCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */