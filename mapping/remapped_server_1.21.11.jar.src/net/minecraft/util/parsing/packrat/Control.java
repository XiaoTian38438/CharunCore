/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public interface Control {
/*  4 */   public static final Control UNBOUND = new Control()
/*    */     {
/*    */       public void cut() {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean hasCut() {
/* 11 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   void cut();
/*    */   
/*    */   boolean hasCut();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\Control.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */