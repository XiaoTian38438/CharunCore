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
/*    */ class null
/*    */   implements StreamCodec<B, V>
/*    */ {
/*    */   public V decode(B paramB) {
/* 26 */     return decoder.decode(paramB);
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(B paramB, V paramV) {
/* 31 */     encoder.encode(paramB, paramV);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\codec\StreamCodec$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */