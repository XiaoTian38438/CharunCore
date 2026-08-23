/*    */ package net.minecraft.network.codec;
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
/*    */   implements StreamCodec<B, V>
/*    */ {
/*    */   public V decode(B paramB) {
/* 57 */     return (V)instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(B paramB, V paramV) {
/* 62 */     if (!paramV.equals(instance))
/* 63 */       throw new IllegalStateException("Can't encode '" + String.valueOf(paramV) + "', expected '" + String.valueOf(instance) + "'"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\codec\StreamCodec$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */