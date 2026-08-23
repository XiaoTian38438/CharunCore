/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.ServerTickRateManager;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ 
/*     */ public class TickCommand {
/*     */   private static final float MAX_TICKRATE = 10000.0F;
/*  25 */   private static final String DEFAULT_TICKRATE = String.valueOf(20);
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  28 */     paramCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tick")
/*  29 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  30 */         .then(Commands.literal("query")
/*  31 */           .executes(paramCommandContext -> tickQuery((CommandSourceStack)paramCommandContext.getSource()))))
/*  32 */         .then(Commands.literal("rate")
/*  33 */           .then(Commands.argument("rate", (ArgumentType)FloatArgumentType.floatArg(1.0F, 10000.0F))
/*  34 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(new String[] { DEFAULT_TICKRATE
/*  35 */                 }, paramSuggestionsBuilder)).executes(paramCommandContext -> setTickingRate((CommandSourceStack)paramCommandContext.getSource(), FloatArgumentType.getFloat(paramCommandContext, "rate"))))))
/*  36 */         .then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("step")
/*  37 */           .executes(paramCommandContext -> step((CommandSourceStack)paramCommandContext.getSource(), 1)))
/*  38 */           .then(Commands.literal("stop")
/*  39 */             .executes(paramCommandContext -> stopStepping((CommandSourceStack)paramCommandContext.getSource()))))
/*  40 */           .then(Commands.argument("time", (ArgumentType)TimeArgument.time(1))
/*  41 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(new String[] { "1t", "1s"
/*  42 */                 }, paramSuggestionsBuilder)).executes(paramCommandContext -> step((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "time"))))))
/*  43 */         .then(((LiteralArgumentBuilder)Commands.literal("sprint")
/*  44 */           .then(Commands.literal("stop")
/*  45 */             .executes(paramCommandContext -> stopSprinting((CommandSourceStack)paramCommandContext.getSource()))))
/*  46 */           .then(Commands.argument("time", (ArgumentType)TimeArgument.time(1))
/*  47 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(new String[] { "60s", "1d", "3d"
/*  48 */                 }, paramSuggestionsBuilder)).executes(paramCommandContext -> sprint((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "time"))))))
/*  49 */         .then(Commands.literal("unfreeze").executes(paramCommandContext -> setFreeze((CommandSourceStack)paramCommandContext.getSource(), false))))
/*  50 */         .then(Commands.literal("freeze").executes(paramCommandContext -> setFreeze((CommandSourceStack)paramCommandContext.getSource(), true))));
/*     */   }
/*     */   
/*     */   private static String nanosToMilisString(long paramLong) {
/*  54 */     return String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf((float)paramLong / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND) });
/*     */   }
/*     */   
/*     */   private static int setTickingRate(CommandSourceStack paramCommandSourceStack, float paramFloat) {
/*  58 */     ServerTickRateManager serverTickRateManager = paramCommandSourceStack.getServer().tickRateManager();
/*  59 */     serverTickRateManager.setTickRate(paramFloat);
/*  60 */     String str = String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf(paramFloat) });
/*  61 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.rate.success", new Object[] { paramString }), true);
/*  62 */     return (int)paramFloat;
/*     */   }
/*     */   
/*     */   private static int tickQuery(CommandSourceStack paramCommandSourceStack) {
/*  66 */     ServerTickRateManager serverTickRateManager = paramCommandSourceStack.getServer().tickRateManager();
/*  67 */     String str1 = nanosToMilisString(paramCommandSourceStack.getServer().getAverageTickTimeNanos());
/*     */     
/*  69 */     float f = serverTickRateManager.tickrate();
/*  70 */     String str2 = String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf(f) });
/*  71 */     if (serverTickRateManager.isSprinting()) {
/*  72 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.sprinting"), false);
/*  73 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.query.rate.sprinting", new Object[] { paramString1, paramString2 }), false);
/*     */     } else {
/*  75 */       if (serverTickRateManager.isFrozen()) {
/*  76 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.frozen"), false);
/*     */       }
/*  78 */       else if (serverTickRateManager.nanosecondsPerTick() < paramCommandSourceStack.getServer().getAverageTickTimeNanos()) {
/*  79 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.lagging"), false);
/*     */       } else {
/*  81 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.running"), false);
/*     */       } 
/*     */       
/*  84 */       String str = nanosToMilisString(serverTickRateManager.nanosecondsPerTick());
/*  85 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.query.rate.running", new Object[] { paramString1, paramString2, paramString3 }), false);
/*     */     } 
/*     */     
/*  88 */     long[] arrayOfLong = Arrays.copyOf(paramCommandSourceStack.getServer().getTickTimesNanos(), (paramCommandSourceStack.getServer().getTickTimesNanos()).length);
/*  89 */     Arrays.sort(arrayOfLong);
/*  90 */     String str3 = nanosToMilisString(arrayOfLong[arrayOfLong.length / 2]);
/*  91 */     String str4 = nanosToMilisString(arrayOfLong[(int)(arrayOfLong.length * 0.95D)]);
/*  92 */     String str5 = nanosToMilisString(arrayOfLong[(int)(arrayOfLong.length * 0.99D)]);
/*     */     
/*  94 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.query.percentiles", new Object[] { paramString1, paramString2, paramString3, Integer.valueOf(paramArrayOflong.length) }), false);
/*  95 */     return (int)f;
/*     */   }
/*     */   
/*     */   private static int sprint(CommandSourceStack paramCommandSourceStack, int paramInt) {
/*  99 */     boolean bool = paramCommandSourceStack.getServer().tickRateManager().requestGameToSprint(paramInt);
/* 100 */     if (bool) {
/* 101 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.sprint.stop.success"), true);
/*     */     }
/* 103 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.sprinting"), true);
/* 104 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setFreeze(CommandSourceStack paramCommandSourceStack, boolean paramBoolean) {
/* 108 */     ServerTickRateManager serverTickRateManager = paramCommandSourceStack.getServer().tickRateManager();
/* 109 */     if (paramBoolean) {
/* 110 */       if (serverTickRateManager.isSprinting()) {
/* 111 */         serverTickRateManager.stopSprinting();
/*     */       }
/* 113 */       if (serverTickRateManager.isSteppingForward()) {
/* 114 */         serverTickRateManager.stopStepping();
/*     */       }
/*     */     } 
/* 117 */     serverTickRateManager.setFrozen(paramBoolean);
/* 118 */     if (paramBoolean) {
/* 119 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.frozen"), true);
/*     */     } else {
/* 121 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.status.running"), true);
/*     */     } 
/* 123 */     return paramBoolean ? 1 : 0;
/*     */   }
/*     */   
/*     */   private static int step(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 127 */     ServerTickRateManager serverTickRateManager = paramCommandSourceStack.getServer().tickRateManager();
/* 128 */     boolean bool = serverTickRateManager.stepGameIfPaused(paramInt);
/* 129 */     if (bool) {
/* 130 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.step.success", new Object[] { Integer.valueOf(paramInt) }), true);
/*     */     } else {
/* 132 */       paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.tick.step.fail"));
/*     */     } 
/* 134 */     return 1;
/*     */   }
/*     */   
/*     */   private static int stopStepping(CommandSourceStack paramCommandSourceStack) {
/* 138 */     ServerTickRateManager serverTickRateManager = paramCommandSourceStack.getServer().tickRateManager();
/* 139 */     boolean bool = serverTickRateManager.stopStepping();
/* 140 */     if (bool) {
/* 141 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.step.stop.success"), true);
/* 142 */       return 1;
/*     */     } 
/* 144 */     paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.tick.step.stop.fail"));
/* 145 */     return 0;
/*     */   }
/*     */   
/*     */   private static int stopSprinting(CommandSourceStack paramCommandSourceStack) {
/* 149 */     ServerTickRateManager serverTickRateManager = paramCommandSourceStack.getServer().tickRateManager();
/* 150 */     boolean bool = serverTickRateManager.stopSprinting();
/* 151 */     if (bool) {
/* 152 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tick.sprint.stop.success"), true);
/* 153 */       return 1;
/*     */     } 
/* 155 */     paramCommandSourceStack.sendFailure((Component)Component.translatable("commands.tick.sprint.stop.fail"));
/* 156 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TickCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */