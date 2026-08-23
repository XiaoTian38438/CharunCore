/*    */ package net.minecraft.server.commands;
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
/*    */ 
/*    */ public class TimeCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 17 */     paramCommandDispatcher.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("time")
/* 19 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 20 */         .then((
/* 21 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set")
/* 22 */           .then(
/* 23 */             Commands.literal("day")
/* 24 */             .executes(paramCommandContext -> setTime((CommandSourceStack)paramCommandContext.getSource(), 1000))))
/* 25 */           .then(
/* 26 */             Commands.literal("noon")
/* 27 */             .executes(paramCommandContext -> setTime((CommandSourceStack)paramCommandContext.getSource(), 6000))))
/* 28 */           .then(
/* 29 */             Commands.literal("night")
/* 30 */             .executes(paramCommandContext -> setTime((CommandSourceStack)paramCommandContext.getSource(), 13000))))
/* 31 */           .then(
/* 32 */             Commands.literal("midnight")
/* 33 */             .executes(paramCommandContext -> setTime((CommandSourceStack)paramCommandContext.getSource(), 18000))))
/* 34 */           .then(
/* 35 */             Commands.argument("time", (ArgumentType)TimeArgument.time())
/* 36 */             .executes(paramCommandContext -> setTime((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "time"))))))
/*    */ 
/*    */         
/* 39 */         .then(
/* 40 */           Commands.literal("add")
/* 41 */           .then(
/* 42 */             Commands.argument("time", (ArgumentType)TimeArgument.time())
/* 43 */             .executes(paramCommandContext -> addTime((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "time"))))))
/*    */ 
/*    */         
/* 46 */         .then((
/* 47 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("query")
/* 48 */           .then(
/* 49 */             Commands.literal("daytime")
/* 50 */             .executes(paramCommandContext -> queryTime((CommandSourceStack)paramCommandContext.getSource(), getDayTime(((CommandSourceStack)paramCommandContext.getSource()).getLevel())))))
/*    */           
/* 52 */           .then(
/* 53 */             Commands.literal("gametime")
/* 54 */             .executes(paramCommandContext -> queryTime((CommandSourceStack)paramCommandContext.getSource(), (int)(((CommandSourceStack)paramCommandContext.getSource()).getLevel().getGameTime() % 2147483647L)))))
/*    */           
/* 56 */           .then(
/* 57 */             Commands.literal("day")
/* 58 */             .executes(paramCommandContext -> queryTime((CommandSourceStack)paramCommandContext.getSource(), (int)(((CommandSourceStack)paramCommandContext.getSource()).getLevel().getDayCount() % 2147483647L))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int getDayTime(ServerLevel paramServerLevel) {
/* 65 */     return (int)(paramServerLevel.getDayTime() % 24000L);
/*    */   }
/*    */   
/*    */   private static int queryTime(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 69 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.time.query", new Object[] { Integer.valueOf(paramInt) }), false);
/* 70 */     return paramInt;
/*    */   }
/*    */   
/*    */   public static int setTime(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 74 */     for (ServerLevel serverLevel : paramCommandSourceStack.getServer().getAllLevels()) {
/* 75 */       serverLevel.setDayTime(paramInt);
/*    */     }
/* 77 */     paramCommandSourceStack.getServer().forceTimeSynchronization();
/* 78 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.time.set", new Object[] { Integer.valueOf(paramInt) }), true);
/* 79 */     return getDayTime(paramCommandSourceStack.getLevel());
/*    */   }
/*    */   
/*    */   public static int addTime(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 83 */     for (ServerLevel serverLevel : paramCommandSourceStack.getServer().getAllLevels()) {
/* 84 */       serverLevel.setDayTime(serverLevel.getDayTime() + paramInt);
/*    */     }
/* 86 */     paramCommandSourceStack.getServer().forceTimeSynchronization();
/* 87 */     int i = getDayTime(paramCommandSourceStack.getLevel());
/* 88 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.time.set", new Object[] { Integer.valueOf(paramInt) }), true);
/* 89 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TimeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */