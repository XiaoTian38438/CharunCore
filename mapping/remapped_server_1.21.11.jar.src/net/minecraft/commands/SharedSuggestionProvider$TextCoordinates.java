/*    */ package net.minecraft.commands;
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
/*    */ public class TextCoordinates
/*    */ {
/* 35 */   public static final TextCoordinates DEFAULT_LOCAL = new TextCoordinates("^", "^", "^");
/*    */   
/* 37 */   public static final TextCoordinates DEFAULT_GLOBAL = new TextCoordinates("~", "~", "~");
/*    */   
/*    */   public final String x;
/*    */   
/*    */   public final String y;
/*    */   
/*    */   public final String z;
/*    */   
/*    */   public TextCoordinates(String paramString1, String paramString2, String paramString3) {
/* 46 */     this.x = paramString1;
/* 47 */     this.y = paramString2;
/* 48 */     this.z = paramString3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\SharedSuggestionProvider$TextCoordinates.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */