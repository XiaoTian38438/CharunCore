/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class CommandSyntaxException
/*    */   extends Exception
/*    */ {
/*    */   public static final int CONTEXT_AMOUNT = 10;
/*    */   public static boolean ENABLE_COMMAND_STACK_TRACES = true;
/* 11 */   public static BuiltInExceptionProvider BUILT_IN_EXCEPTIONS = new BuiltInExceptions();
/*    */   
/*    */   private final CommandExceptionType type;
/*    */   private final Message message;
/*    */   private final String input;
/*    */   private final int cursor;
/*    */   
/*    */   public CommandSyntaxException(CommandExceptionType paramCommandExceptionType, Message paramMessage) {
/* 19 */     super(paramMessage.getString(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
/* 20 */     this.type = paramCommandExceptionType;
/* 21 */     this.message = paramMessage;
/* 22 */     this.input = null;
/* 23 */     this.cursor = -1;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException(CommandExceptionType paramCommandExceptionType, Message paramMessage, String paramString, int paramInt) {
/* 27 */     super(paramMessage.getString(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
/* 28 */     this.type = paramCommandExceptionType;
/* 29 */     this.message = paramMessage;
/* 30 */     this.input = paramString;
/* 31 */     this.cursor = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 36 */     String str1 = this.message.getString();
/* 37 */     String str2 = getContext();
/* 38 */     if (str2 != null) {
/* 39 */       str1 = str1 + " at position " + this.cursor + ": " + str2;
/*    */     }
/* 41 */     return str1;
/*    */   }
/*    */   
/*    */   public Message getRawMessage() {
/* 45 */     return this.message;
/*    */   }
/*    */   
/*    */   public String getContext() {
/* 49 */     if (this.input == null || this.cursor < 0) {
/* 50 */       return null;
/*    */     }
/* 52 */     StringBuilder stringBuilder = new StringBuilder();
/* 53 */     int i = Math.min(this.input.length(), this.cursor);
/*    */     
/* 55 */     if (i > 10) {
/* 56 */       stringBuilder.append("...");
/*    */     }
/*    */     
/* 59 */     stringBuilder.append(this.input.substring(Math.max(0, i - 10), i));
/* 60 */     stringBuilder.append("<--[HERE]");
/*    */     
/* 62 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public CommandExceptionType getType() {
/* 66 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getInput() {
/* 70 */     return this.input;
/*    */   }
/*    */   
/*    */   public int getCursor() {
/* 74 */     return this.cursor;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\CommandSyntaxException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */