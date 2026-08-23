/*   */ package net.minecraft.util.parsing.packrat;
/*   */ 
/*   */ import java.util.stream.Stream;
/*   */ 
/*   */ public interface SuggestionSupplier<S> {
/*   */   Stream<String> possibleValues(ParseState<S> paramParseState);
/*   */   
/*   */   static <S> SuggestionSupplier<S> empty() {
/* 9 */     return paramParseState -> Stream.empty();
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\SuggestionSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */