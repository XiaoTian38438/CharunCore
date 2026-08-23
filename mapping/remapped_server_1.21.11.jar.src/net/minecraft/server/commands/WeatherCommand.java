/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.TimeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class WeatherCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 19 */     paramCommandDispatcher.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("weather")
/* 21 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 22 */         .then((
/* 23 */           (LiteralArgumentBuilder)Commands.literal("clear")
/* 24 */           .executes(paramCommandContext -> setClear((CommandSourceStack)paramCommandContext.getSource(), -1)))
/* 25 */           .then(
/* 26 */             Commands.argument("duration", (ArgumentType)TimeArgument.time(1))
/* 27 */             .executes(paramCommandContext -> setClear((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "duration"))))))
/*    */ 
/*    */         
/* 30 */         .then((
/* 31 */           (LiteralArgumentBuilder)Commands.literal("rain")
/* 32 */           .executes(paramCommandContext -> setRain((CommandSourceStack)paramCommandContext.getSource(), -1)))
/* 33 */           .then(
/* 34 */             Commands.argument("duration", (ArgumentType)TimeArgument.time(1))
/* 35 */             .executes(paramCommandContext -> setRain((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "duration"))))))
/*    */ 
/*    */         
/* 38 */         .then((
/* 39 */           (LiteralArgumentBuilder)Commands.literal("thunder")
/* 40 */           .executes(paramCommandContext -> setThunder((CommandSourceStack)paramCommandContext.getSource(), -1)))
/* 41 */           .then(
/* 42 */             Commands.argument("duration", (ArgumentType)TimeArgument.time(1))
/* 43 */             .executes(paramCommandContext -> setThunder((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "duration"))))));
/*    */   }
/*    */ 
/*    */   
/*    */   private static final int DEFAULT_TIME = -1;
/*    */   
/*    */   private static int getDuration(CommandSourceStack paramCommandSourceStack, int paramInt, IntProvider paramIntProvider) {
/* 50 */     if (paramInt == -1) {
/* 51 */       return paramIntProvider.sample(paramCommandSourceStack.getServer().overworld().getRandom());
/*    */     }
/* 53 */     return paramInt;
/*    */   }
/*    */   
/*    */   private static int setClear(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 57 */     paramCommandSourceStack.getServer().overworld().setWeatherParameters(getDuration(paramCommandSourceStack, paramInt, ServerLevel.RAIN_DELAY), 0, false, false);
/* 58 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.weather.set.clear"), true);
/* 59 */     return paramInt;
/*    */   }
/*    */   
/*    */   private static int setRain(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 63 */     paramCommandSourceStack.getServer().overworld().setWeatherParameters(0, getDuration(paramCommandSourceStack, paramInt, ServerLevel.RAIN_DURATION), true, false);
/* 64 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.weather.set.rain"), true);
/* 65 */     return paramInt;
/*    */   }
/*    */   
/*    */   private static int setThunder(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 69 */     paramCommandSourceStack.getServer().overworld().setWeatherParameters(0, getDuration(paramCommandSourceStack, paramInt, ServerLevel.THUNDER_DURATION), true, true);
/* 70 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.weather.set.thunder"), true);
/* 71 */     return paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\WeatherCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */