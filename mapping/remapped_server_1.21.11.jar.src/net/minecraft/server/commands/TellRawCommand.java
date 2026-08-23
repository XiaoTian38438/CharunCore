/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.ComponentArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class TellRawCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 18 */     paramCommandDispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw")
/* 20 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 21 */         .then(
/* 22 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 23 */           .then(
/* 24 */             Commands.argument("message", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/* 25 */             .executes(paramCommandContext -> {
/*    */                 byte b = 0;
/*    */                 for (ServerPlayer serverPlayer : EntityArgument.getPlayers(paramCommandContext, "targets")) {
/*    */                   serverPlayer.sendSystemMessage(ComponentArgument.getResolvedComponent(paramCommandContext, "message", (Entity)serverPlayer), false);
/*    */                   b++;
/*    */                 } 
/*    */                 return b;
/*    */               }))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TellRawCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */