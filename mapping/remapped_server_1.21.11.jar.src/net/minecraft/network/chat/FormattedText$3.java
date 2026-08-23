/*    */ package net.minecraft.network.chat;
/*    */ 
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
/*    */ class null
/*    */   implements FormattedText
/*    */ {
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> paramContentConsumer) {
/* 46 */     return paramContentConsumer.accept(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle) {
/* 51 */     return paramStyledContentConsumer.accept(style.applyTo(paramStyle), text);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\FormattedText$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */