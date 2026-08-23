/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class GameRuleCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 19 */     final LiteralArgumentBuilder base = (LiteralArgumentBuilder)Commands.literal("gamerule").requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*    */     
/* 21 */     (new GameRules(paramCommandBuildContext.enabledFeatures())).visitGameRuleTypes(new GameRuleTypeVisitor()
/*    */         {
/*    */           public <T> void visit(GameRule<T> param1GameRule) {
/* 24 */             LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder1 = Commands.literal(param1GameRule.id());
/* 25 */             LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = Commands.literal(param1GameRule.getIdentifier().toString());
/* 26 */             ((LiteralArgumentBuilder)base
/* 27 */               .then((ArgumentBuilder)GameRuleCommand.buildRuleArguments(param1GameRule, literalArgumentBuilder1)))
/* 28 */               .then((ArgumentBuilder)GameRuleCommand.buildRuleArguments(param1GameRule, literalArgumentBuilder2));
/*    */           }
/*    */         });
/*    */     
/* 32 */     paramCommandDispatcher.register(literalArgumentBuilder);
/*    */   }
/*    */   
/*    */   static <T> LiteralArgumentBuilder<CommandSourceStack> buildRuleArguments(GameRule<T> paramGameRule, LiteralArgumentBuilder<CommandSourceStack> paramLiteralArgumentBuilder) {
/* 36 */     return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)paramLiteralArgumentBuilder
/* 37 */       .executes(paramCommandContext -> queryRule((CommandSourceStack)paramCommandContext.getSource(), paramGameRule)))
/* 38 */       .then(
/* 39 */         Commands.argument("value", paramGameRule.argument())
/* 40 */         .executes(paramCommandContext -> setRule(paramCommandContext, paramGameRule)));
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> int setRule(CommandContext<CommandSourceStack> paramCommandContext, GameRule<T> paramGameRule) {
/* 45 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 46 */     Object object = paramCommandContext.getArgument("value", paramGameRule.valueClass());
/* 47 */     commandSourceStack.getLevel().getGameRules().set(paramGameRule, object, ((CommandSourceStack)paramCommandContext.getSource()).getServer());
/* 48 */     commandSourceStack.sendSuccess(() -> Component.translatable("commands.gamerule.set", new Object[] { paramGameRule.id(), paramGameRule.serialize(paramObject) }), true);
/* 49 */     return paramGameRule.getCommandResult(object);
/*    */   }
/*    */   
/*    */   private static <T> int queryRule(CommandSourceStack paramCommandSourceStack, GameRule<T> paramGameRule) {
/* 53 */     Object object = paramCommandSourceStack.getLevel().getGameRules().get(paramGameRule);
/* 54 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.gamerule.query", new Object[] { paramGameRule.id(), paramGameRule.serialize(paramObject) }), false);
/* 55 */     return paramGameRule.getCommandResult(object);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\GameRuleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */