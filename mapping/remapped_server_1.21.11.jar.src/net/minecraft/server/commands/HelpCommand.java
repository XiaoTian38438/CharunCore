/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ParsedCommandNode;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Map;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class HelpCommand {
/* 20 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.help.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 23 */     paramCommandDispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("help")
/* 25 */         .executes(paramCommandContext -> {
/*    */             Map map = paramCommandDispatcher.getSmartUsage((CommandNode)paramCommandDispatcher.getRoot(), paramCommandContext.getSource());
/*    */             
/*    */             for (String str : map.values()) {
/*    */               ((CommandSourceStack)paramCommandContext.getSource()).sendSuccess((), false);
/*    */             }
/*    */             return map.size();
/* 32 */           })).then(
/* 33 */           Commands.argument("command", (ArgumentType)StringArgumentType.greedyString())
/* 34 */           .executes(paramCommandContext -> {
/*    */               ParseResults parseResults = paramCommandDispatcher.parse(StringArgumentType.getString(paramCommandContext, "command"), paramCommandContext.getSource());
/*    */               if (parseResults.getContext().getNodes().isEmpty())
/*    */                 throw ERROR_FAILED.create(); 
/*    */               Map map = paramCommandDispatcher.getSmartUsage(((ParsedCommandNode)Iterables.getLast(parseResults.getContext().getNodes())).getNode(), paramCommandContext.getSource());
/*    */               for (String str : map.values())
/*    */                 ((CommandSourceStack)paramCommandContext.getSource()).sendSuccess((), false); 
/*    */               return map.size();
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\HelpCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */