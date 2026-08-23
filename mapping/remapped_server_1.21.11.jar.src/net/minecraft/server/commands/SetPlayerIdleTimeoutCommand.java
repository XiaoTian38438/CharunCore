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
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SetPlayerIdleTimeoutCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 15 */     paramCommandDispatcher.register(
/* 16 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setidletimeout")
/* 17 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 18 */         .then(
/* 19 */           Commands.argument("minutes", (ArgumentType)IntegerArgumentType.integer(0))
/* 20 */           .executes(paramCommandContext -> setIdleTimeout((CommandSourceStack)paramCommandContext.getSource(), IntegerArgumentType.getInteger(paramCommandContext, "minutes")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setIdleTimeout(CommandSourceStack paramCommandSourceStack, int paramInt) {
/* 26 */     paramCommandSourceStack.getServer().setPlayerIdleTimeout(paramInt);
/* 27 */     if (paramInt > 0) {
/* 28 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.setidletimeout.success", new Object[] { Integer.valueOf(paramInt) }), true);
/*    */     } else {
/* 30 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.setidletimeout.success.disabled"), true);
/*    */     } 
/* 32 */     return paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SetPlayerIdleTimeoutCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */