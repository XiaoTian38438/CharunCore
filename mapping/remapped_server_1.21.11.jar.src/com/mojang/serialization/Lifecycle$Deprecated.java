/*    */ package com.mojang.serialization;
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
/*    */ public final class Deprecated
/*    */   extends Lifecycle
/*    */ {
/*    */   private final int since;
/*    */   
/*    */   public Deprecated(int paramInt) {
/* 26 */     this.since = paramInt;
/*    */   }
/*    */   
/*    */   public int since() {
/* 30 */     return this.since;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Lifecycle$Deprecated.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */