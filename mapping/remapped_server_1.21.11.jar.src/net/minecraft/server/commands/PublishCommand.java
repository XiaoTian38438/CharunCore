/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.util.HttpUtil;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class PublishCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED;
/* 26 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.publish.failed")); static {
/* 27 */     ERROR_ALREADY_PUBLISHED = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.publish.alreadyPublished", new Object[] { paramObject }));
/*    */   }
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 30 */     paramCommandDispatcher.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("publish")
/* 32 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 33 */         .executes(paramCommandContext -> publish((CommandSourceStack)paramCommandContext.getSource(), HttpUtil.getAvailablePort(), false, null)))
/* 34 */         .then((
/* 35 */           (RequiredArgumentBuilder)Commands.argument("allowCommands", (ArgumentType)BoolArgumentType.bool())
/* 36 */           .executes(paramCommandContext -> publish((CommandSourceStack)paramCommandContext.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(paramCommandContext, "allowCommands"), null)))
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)Commands.argument("gamemode", (ArgumentType)GameModeArgument.gameMode())
/* 39 */             .executes(paramCommandContext -> publish((CommandSourceStack)paramCommandContext.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(paramCommandContext, "allowCommands"), GameModeArgument.getGameMode(paramCommandContext, "gamemode"))))
/* 40 */             .then(
/* 41 */               Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(0, 65535))
/* 42 */               .executes(paramCommandContext -> publish((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "port"), BoolArgumentType.getBool(paramCommandContext, "allowCommands"), GameModeArgument.getGameMode(paramCommandContext, "gamemode")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int publish(CommandSourceStack paramCommandSourceStack, int paramInt, boolean paramBoolean, GameType paramGameType) throws CommandSyntaxException {
/* 50 */     if (paramCommandSourceStack.getServer().isPublished()) {
/* 51 */       throw ERROR_ALREADY_PUBLISHED.create(Integer.valueOf(paramCommandSourceStack.getServer().getPort()));
/*    */     }
/* 53 */     if (!paramCommandSourceStack.getServer().publishServer(paramGameType, paramBoolean, paramInt)) {
/* 54 */       throw ERROR_FAILED.create();
/*    */     }
/* 56 */     paramCommandSourceStack.sendSuccess(() -> getSuccessMessage(paramInt), true);
/* 57 */     return paramInt;
/*    */   }
/*    */   
/*    */   public static MutableComponent getSuccessMessage(int paramInt) {
/* 61 */     MutableComponent mutableComponent = ComponentUtils.copyOnClickText(String.valueOf(paramInt));
/* 62 */     return Component.translatable("commands.publish.started", new Object[] { mutableComponent });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\PublishCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */