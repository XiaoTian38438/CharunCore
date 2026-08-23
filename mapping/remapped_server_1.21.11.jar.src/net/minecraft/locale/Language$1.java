/*    */ package net.minecraft.locale;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.FormattedCharSink;
/*    */ import net.minecraft.util.StringDecomposer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Language
/*    */ {
/*    */   public String getOrDefault(String paramString1, String paramString2) {
/* 56 */     return (String)storage.getOrDefault(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean has(String paramString) {
/* 61 */     return storage.containsKey(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDefaultRightToLeft() {
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FormattedCharSequence getVisualOrder(FormattedText paramFormattedText) {
/* 72 */     return paramFormattedCharSink -> paramFormattedText.visit((), Style.EMPTY).isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\locale\Language$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */