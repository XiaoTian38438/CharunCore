/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.parsing.packrat.Control;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.Scope;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ import net.minecraft.util.parsing.packrat.Term;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TerminalWord
/*    */   implements Term<StringReader>
/*    */ {
/*    */   private final String value;
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   private final SuggestionSupplier<StringReader> suggestions;
/*    */   
/*    */   public TerminalWord(String paramString) {
/* 27 */     this.value = paramString;
/* 28 */     this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), paramString);
/* 29 */     this.suggestions = (paramParseState -> Stream.of(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean parse(ParseState<StringReader> paramParseState, Scope paramScope, Control paramControl) {
/* 34 */     ((StringReader)paramParseState.input()).skipWhitespace();
/* 35 */     int i = paramParseState.mark();
/* 36 */     String str = ((StringReader)paramParseState.input()).readUnquotedString();
/* 37 */     if (!str.equals(this.value)) {
/* 38 */       paramParseState.errorCollector().store(i, this.suggestions, this.error);
/* 39 */       return false;
/*    */     } 
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "terminal[" + this.value + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderTerms$TerminalWord.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */