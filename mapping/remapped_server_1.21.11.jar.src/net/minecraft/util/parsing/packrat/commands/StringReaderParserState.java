/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.util.parsing.packrat.CachedParseState;
/*    */ import net.minecraft.util.parsing.packrat.ErrorCollector;
/*    */ 
/*    */ public class StringReaderParserState extends CachedParseState<StringReader> {
/*    */   private final StringReader input;
/*    */   
/*    */   public StringReaderParserState(ErrorCollector<StringReader> paramErrorCollector, StringReader paramStringReader) {
/* 11 */     super(paramErrorCollector);
/* 12 */     this.input = paramStringReader;
/*    */   }
/*    */ 
/*    */   
/*    */   public StringReader input() {
/* 17 */     return this.input;
/*    */   }
/*    */ 
/*    */   
/*    */   public int mark() {
/* 22 */     return this.input.getCursor();
/*    */   }
/*    */ 
/*    */   
/*    */   public void restore(int paramInt) {
/* 27 */     this.input.setCursor(paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\commands\StringReaderParserState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */