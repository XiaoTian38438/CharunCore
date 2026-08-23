/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
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
/*    */   implements FormattedText
/*    */ {
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> paramContentConsumer) {
/* 64 */     for (FormattedText formattedText : parts) {
/* 65 */       Optional<T> optional = formattedText.visit(paramContentConsumer);
/* 66 */       if (optional.isPresent()) {
/* 67 */         return optional;
/*    */       }
/*    */     } 
/*    */     
/* 71 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle) {
/* 76 */     for (FormattedText formattedText : parts) {
/* 77 */       Optional<T> optional = formattedText.visit(paramStyledContentConsumer, paramStyle);
/* 78 */       if (optional.isPresent()) {
/* 79 */         return optional;
/*    */       }
/*    */     } 
/*    */     
/* 83 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\FormattedText$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */