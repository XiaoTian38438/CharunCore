/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public abstract class ParserBasedArgument<T>
/*    */   implements ArgumentType<T> {
/*    */   private final CommandArgumentParser<T> parser;
/*    */   
/*    */   public ParserBasedArgument(CommandArgumentParser<T> paramCommandArgumentParser) {
/* 16 */     this.parser = paramCommandArgumentParser;
/*    */   }
/*    */ 
/*    */   
/*    */   public T parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 21 */     return this.parser.parseForCommands(paramStringReader);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) {
/* 26 */     return this.parser.parseForSuggestions(paramSuggestionsBuilder);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\commands\ParserBasedArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */