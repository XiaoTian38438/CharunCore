/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SaveOffCommand {
/* 12 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OFF = new SimpleCommandExceptionType((Message)Component.translatable("commands.save.alreadyOff"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 15 */     paramCommandDispatcher.register(
/* 16 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-off")
/* 17 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 18 */         .executes(paramCommandContext -> {
/*    */             CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*    */             boolean bool = commandSourceStack.getServer().setAutoSave(false);
/*    */             if (!bool)
/*    */               throw ERROR_ALREADY_OFF.create(); 
/*    */             commandSourceStack.sendSuccess((), true);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SaveOffCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */