/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.timers.FunctionCallback;
/*     */ import net.minecraft.world.level.timers.FunctionTagCallback;
/*     */ import net.minecraft.world.level.timers.TimerCallback;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ public class ScheduleCommand {
/*     */   private static final DynamicCommandExceptionType ERROR_CANT_REMOVE;
/*  35 */   private static final SimpleCommandExceptionType ERROR_SAME_TICK = new SimpleCommandExceptionType((Message)Component.translatable("commands.schedule.same_tick")); static {
/*  36 */     ERROR_CANT_REMOVE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.schedule.cleared.failure", new Object[] { paramObject }));
/*  37 */   } private static final SimpleCommandExceptionType ERROR_MACRO = new SimpleCommandExceptionType((Message)Component.translatableEscape("commands.schedule.macro", new Object[0])); private static final SuggestionProvider<CommandSourceStack> SUGGEST_SCHEDULE;
/*     */   static {
/*  39 */     SUGGEST_SCHEDULE = ((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)paramCommandContext.getSource()).getServer().getWorldData().overworldData().getScheduledEvents().getEventsIds(), paramSuggestionsBuilder));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  42 */     paramCommandDispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("schedule")
/*  44 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  45 */         .then(
/*  46 */           Commands.literal("function")
/*  47 */           .then(
/*  48 */             Commands.argument("function", (ArgumentType)FunctionArgument.functions())
/*  49 */             .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  50 */             .then((
/*  51 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("time", (ArgumentType)TimeArgument.time())
/*  52 */               .executes(paramCommandContext -> schedule((CommandSourceStack)paramCommandContext.getSource(), FunctionArgument.getFunctionOrTag(paramCommandContext, "function"), IntegerArgumentType.getInteger(paramCommandContext, "time"), true)))
/*  53 */               .then(
/*  54 */                 Commands.literal("append")
/*  55 */                 .executes(paramCommandContext -> schedule((CommandSourceStack)paramCommandContext.getSource(), FunctionArgument.getFunctionOrTag(paramCommandContext, "function"), IntegerArgumentType.getInteger(paramCommandContext, "time"), false))))
/*     */               
/*  57 */               .then(
/*  58 */                 Commands.literal("replace")
/*  59 */                 .executes(paramCommandContext -> schedule((CommandSourceStack)paramCommandContext.getSource(), FunctionArgument.getFunctionOrTag(paramCommandContext, "function"), IntegerArgumentType.getInteger(paramCommandContext, "time"), true)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  64 */         .then(
/*  65 */           Commands.literal("clear")
/*  66 */           .then(
/*  67 */             Commands.argument("function", (ArgumentType)StringArgumentType.greedyString())
/*  68 */             .suggests(SUGGEST_SCHEDULE)
/*  69 */             .executes(paramCommandContext -> remove((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "function"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int schedule(CommandSourceStack paramCommandSourceStack, Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> paramPair, int paramInt, boolean paramBoolean) throws CommandSyntaxException {
/*  76 */     if (paramInt == 0) {
/*  77 */       throw ERROR_SAME_TICK.create();
/*     */     }
/*     */     
/*  80 */     long l = paramCommandSourceStack.getLevel().getGameTime() + paramInt;
/*     */     
/*  82 */     Identifier identifier = (Identifier)paramPair.getFirst();
/*  83 */     TimerQueue timerQueue = paramCommandSourceStack.getServer().getWorldData().overworldData().getScheduledEvents();
/*  84 */     Optional optional = ((Either)paramPair.getSecond()).left();
/*  85 */     if (optional.isPresent()) {
/*  86 */       if (optional.get() instanceof net.minecraft.commands.functions.MacroFunction) {
/*  87 */         throw ERROR_MACRO.create();
/*     */       }
/*  89 */       String str = identifier.toString();
/*  90 */       if (paramBoolean) {
/*  91 */         timerQueue.remove(str);
/*     */       }
/*  93 */       timerQueue.schedule(str, l, (TimerCallback)new FunctionCallback(identifier));
/*  94 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.schedule.created.function", new Object[] { Component.translationArg(paramIdentifier), Integer.valueOf(paramInt), Long.valueOf(paramLong) }), true);
/*     */     } else {
/*  96 */       String str = "#" + String.valueOf(identifier);
/*  97 */       if (paramBoolean) {
/*  98 */         timerQueue.remove(str);
/*     */       }
/* 100 */       timerQueue.schedule(str, l, (TimerCallback)new FunctionTagCallback(identifier));
/* 101 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.schedule.created.tag", new Object[] { Component.translationArg(paramIdentifier), Integer.valueOf(paramInt), Long.valueOf(paramLong) }), true);
/*     */     } 
/*     */     
/* 104 */     return Math.floorMod(l, 2147483647);
/*     */   }
/*     */   
/*     */   private static int remove(CommandSourceStack paramCommandSourceStack, String paramString) throws CommandSyntaxException {
/* 108 */     int i = paramCommandSourceStack.getServer().getWorldData().overworldData().getScheduledEvents().remove(paramString);
/* 109 */     if (i == 0) {
/* 110 */       throw ERROR_CANT_REMOVE.create(paramString);
/*     */     }
/* 112 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.schedule.cleared.success", new Object[] { Integer.valueOf(paramInt), paramString }), true);
/* 113 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ScheduleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */