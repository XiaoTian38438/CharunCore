/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class SaveAllCommand {
/* 14 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.save.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 17 */     paramCommandDispatcher.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-all")
/* 19 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 20 */         .executes(paramCommandContext -> saveAll((CommandSourceStack)paramCommandContext.getSource(), false)))
/* 21 */         .then(
/* 22 */           Commands.literal("flush")
/* 23 */           .executes(paramCommandContext -> saveAll((CommandSourceStack)paramCommandContext.getSource(), true))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int saveAll(CommandSourceStack paramCommandSourceStack, boolean paramBoolean) throws CommandSyntaxException {
/* 29 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.save.saving"), false);
/*    */     
/* 31 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/* 32 */     boolean bool = minecraftServer.saveEverything(true, paramBoolean, true);
/*    */     
/* 34 */     if (!bool) {
/* 35 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 38 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.save.success"), true);
/*    */     
/* 40 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SaveAllCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */