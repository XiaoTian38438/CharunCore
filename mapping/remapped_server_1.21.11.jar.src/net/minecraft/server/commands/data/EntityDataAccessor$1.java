/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
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
/*    */ 
/*    */ class null
/*    */   implements DataCommands.DataProvider
/*    */ {
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException {
/* 38 */     return new EntityDataAccessor(EntityArgument.getEntity(paramCommandContext, arg));
/*    */   }
/*    */ 
/*    */   
/*    */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> paramFunction) {
/* 43 */     return paramArgumentBuilder.then(Commands.literal("entity").then(paramFunction.apply(Commands.argument(arg, (ArgumentType)EntityArgument.entity()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\EntityDataAccessor$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */