/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class SimpleCommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Message message;
/*    */   
/*    */   public SimpleCommandExceptionType(Message paramMessage) {
/* 13 */     this.message = paramMessage;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create() {
/* 17 */     return new CommandSyntaxException(this, this.message);
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader paramImmutableStringReader) {
/* 21 */     return new CommandSyntaxException(this, this.message, paramImmutableStringReader.getString(), paramImmutableStringReader.getCursor());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return this.message.getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\SimpleCommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */