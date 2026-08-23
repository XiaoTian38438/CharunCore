/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ 
/*    */ public interface ResourceSuggestion
/*    */   extends SuggestionSupplier<StringReader>
/*    */ {
/*    */   Stream<Identifier> possibleResources();
/*    */   
/*    */   default Stream<String> possibleValues(ParseState<StringReader> paramParseState) {
/* 15 */     return possibleResources().map(Identifier::toString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\commands\ResourceSuggestion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */