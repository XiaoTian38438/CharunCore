/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public class DifficultyCommand {
/*    */   static {
/* 16 */     ERROR_ALREADY_DIFFICULT = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.difficulty.failure", new Object[] { paramObject }));
/*    */   } private static final DynamicCommandExceptionType ERROR_ALREADY_DIFFICULT;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 19 */     LiteralArgumentBuilder literalArgumentBuilder = Commands.literal("difficulty");
/*    */     
/* 21 */     for (Difficulty difficulty : Difficulty.values()) {
/* 22 */       literalArgumentBuilder.then(Commands.literal(difficulty.getKey()).executes(paramCommandContext -> setDifficulty((CommandSourceStack)paramCommandContext.getSource(), paramDifficulty)));
/*    */     }
/*    */     
/* 25 */     paramCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)literalArgumentBuilder
/*    */         
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .executes(paramCommandContext -> {
/*    */             Difficulty difficulty = ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getDifficulty();
/*    */             ((CommandSourceStack)paramCommandContext.getSource()).sendSuccess((), false);
/*    */             return difficulty.getId();
/*    */           }));
/*    */   }
/*    */ 
/*    */   
/*    */   public static int setDifficulty(CommandSourceStack paramCommandSourceStack, Difficulty paramDifficulty) throws CommandSyntaxException {
/* 37 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/* 38 */     if (minecraftServer.getWorldData().getDifficulty() == paramDifficulty) {
/* 39 */       throw ERROR_ALREADY_DIFFICULT.create(paramDifficulty.getKey());
/*    */     }
/*    */     
/* 42 */     minecraftServer.setDifficulty(paramDifficulty, true);
/* 43 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.difficulty.success", new Object[] { paramDifficulty.getDisplayName() }), true);
/*    */     
/* 45 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DifficultyCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */