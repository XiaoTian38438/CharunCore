/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import net.minecraft.CharPredicate;
/*    */ 
/*    */ public class ParserUtils {
/*    */   public static String readWhile(StringReader paramStringReader, CharPredicate paramCharPredicate) {
/*  8 */     int i = paramStringReader.getCursor();
/*  9 */     while (paramStringReader.canRead() && paramCharPredicate.test(paramStringReader.peek())) {
/* 10 */       paramStringReader.skip();
/*    */     }
/* 12 */     return paramStringReader.getString().substring(i, paramStringReader.getCursor());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\ParserUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */