/*    */ package net.minecraft;
/*    */ 
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ public class IdentifierException extends RuntimeException {
/*    */   public IdentifierException(String paramString) {
/*  7 */     super(StringEscapeUtils.escapeJava(paramString));
/*    */   }
/*    */   
/*    */   public IdentifierException(String paramString, Throwable paramThrowable) {
/* 11 */     super(StringEscapeUtils.escapeJava(paramString), paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\IdentifierException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */