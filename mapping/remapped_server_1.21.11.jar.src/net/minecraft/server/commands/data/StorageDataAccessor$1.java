/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.IdentifierArgument;
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
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> paramCommandContext) {
/* 31 */     return new StorageDataAccessor(StorageDataAccessor.getGlobalTags(paramCommandContext), IdentifierArgument.getId(paramCommandContext, arg));
/*    */   }
/*    */ 
/*    */   
/*    */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> paramFunction) {
/* 36 */     return paramArgumentBuilder.then(Commands.literal("storage").then(paramFunction.apply(Commands.argument(arg, (ArgumentType)IdentifierArgument.id()).suggests(StorageDataAccessor.SUGGEST_STORAGE))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\StorageDataAccessor$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */