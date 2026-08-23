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
/*    */ class null
/*    */   implements StreamCodec<B, V>
/*    */ {
/*    */   public V decode(B paramB) {
/* 43 */     return decoder.decode(paramB);
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(B paramB, V paramV) {
/* 48 */     encoder.encode(paramV, paramB);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\codec\StreamCodec$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */