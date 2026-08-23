/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class ExceptionCollector<T extends Throwable>
/*    */ {
/*    */   private T result;
/*    */   
/*    */   public void add(T paramT) {
/*  9 */     if (this.result == null) {
/* 10 */       this.result = paramT;
/*    */     } else {
/* 12 */       this.result.addSuppressed((Throwable)paramT);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void throwIfPresent() throws T {
/* 17 */     if (this.result != null)
/* 18 */       throw this.result; 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ExceptionCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */