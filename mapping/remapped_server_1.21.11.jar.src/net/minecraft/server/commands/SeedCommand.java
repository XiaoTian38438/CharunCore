/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public class SeedCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, boolean paramBoolean) {
/* 13 */     paramCommandDispatcher.register(
/* 14 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("seed")
/* 15 */         .requires((Predicate)Commands.hasPermission(paramBoolean ? Commands.LEVEL_GAMEMASTERS : Commands.LEVEL_ALL)))
/* 16 */         .executes(paramCommandContext -> {
/*    */             long l = ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getSeed();
/*    */             MutableComponent mutableComponent = ComponentUtils.copyOnClickText(String.valueOf(l));
/*    */             ((CommandSourceStack)paramCommandContext.getSource()).sendSuccess((), false);
/*    */             return (int)l;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SeedCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */