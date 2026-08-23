/*    */ package net.minecraft.nbt;
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
/*    */ class Cache
/*    */ {
/* 16 */   static final ByteTag[] cache = new ByteTag[256];
/*    */   
/*    */   static {
/* 19 */     for (byte b = 0; b < cache.length; b++)
/* 20 */       cache[b] = new ByteTag((byte)(b - 128)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\ByteTag$Cache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */