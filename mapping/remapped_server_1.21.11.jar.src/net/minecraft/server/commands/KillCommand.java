/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class KillCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 19 */     paramCommandDispatcher.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kill")
/* 21 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 22 */         .executes(paramCommandContext -> kill((CommandSourceStack)paramCommandContext.getSource(), (Collection<? extends Entity>)ImmutableList.of(((CommandSourceStack)paramCommandContext.getSource()).getEntityOrException()))))
/* 23 */         .then(
/* 24 */           Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 25 */           .executes(paramCommandContext -> kill((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int kill(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection) {
/* 31 */     for (Entity entity : paramCollection) {
/* 32 */       entity.kill(paramCommandSourceStack.getLevel());
/*    */     }
/*    */     
/* 35 */     if (paramCollection.size() == 1) {
/* 36 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.kill.success.single", new Object[] { ((Entity)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 38 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.kill.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */     
/* 41 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\KillCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */