/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import it.unimi.dsi.fastutil.chars.CharList;
/*    */ import java.util.stream.Collectors;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TerminalCharacters
/*    */   implements Term<StringReader>
/*    */ {
/*    */   private final DelayedException<CommandSyntaxException> error;
/*    */   private final SuggestionSupplier<StringReader> suggestions;
/*    */   
/*    */   public TerminalCharacters(CharList paramCharList) {
/* 55 */     String str = paramCharList.intStream().<CharSequence>mapToObj(Character::toString).collect(Collectors.joining("|"));
/* 56 */     this.error = DelayedException.create(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), str);
/* 57 */     this.suggestions = (paramParseState -> paramCharList.intStream().mapToObj(Character::toString));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean parse(ParseState<StringReader> paramParseState, Scope paramScope, Control paramControl) {
/* 62 */     ((StringReader)paramParseState.input()).skipWhitespace();
/* 63 */     int i = paramParseState.mark();
/* 64 */     if (!((StringReader)paramParseState.input()).canRead() || !isAccepted(((StringReader)paramParseState.input()).read())) {
/* 65 */       paramParseState.errorCollector().store(i, this.suggestions, this.error);
/* 66 */       return false;
/*    */     } 
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract boolean isAccepted(char paramChar);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderTerms$TerminalCharacters.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */