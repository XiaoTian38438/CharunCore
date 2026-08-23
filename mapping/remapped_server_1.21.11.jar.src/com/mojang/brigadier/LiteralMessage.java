/*    */ package com.mojang.brigadier;
/*    */ 
/*    */ 
/*    */ public class LiteralMessage
/*    */   implements Message
/*    */ {
/*    */   private final String string;
/*    */   
/*    */   public LiteralMessage(String paramString) {
/* 10 */     this.string = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 15 */     return this.string;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return this.string;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\LiteralMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */