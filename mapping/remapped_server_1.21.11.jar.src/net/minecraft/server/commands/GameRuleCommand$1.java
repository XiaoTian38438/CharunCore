/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements GameRuleTypeVisitor
/*    */ {
/*    */   public <T> void visit(GameRule<T> paramGameRule) {
/* 24 */     LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder1 = Commands.literal(paramGameRule.id());
/* 25 */     LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = Commands.literal(paramGameRule.getIdentifier().toString());
/* 26 */     ((LiteralArgumentBuilder)base
/* 27 */       .then((ArgumentBuilder)GameRuleCommand.buildRuleArguments(paramGameRule, literalArgumentBuilder1)))
/* 28 */       .then((ArgumentBuilder)GameRuleCommand.buildRuleArguments(paramGameRule, literalArgumentBuilder2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\GameRuleCommand$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */