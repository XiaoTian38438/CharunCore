/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class ConsoleInput {
/*    */   public final String msg;
/*    */   public final CommandSourceStack source;
/*    */   
/*    */   public ConsoleInput(String paramString, CommandSourceStack paramCommandSourceStack) {
/* 10 */     this.msg = paramString;
/* 11 */     this.source = paramCommandSourceStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ConsoleInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */