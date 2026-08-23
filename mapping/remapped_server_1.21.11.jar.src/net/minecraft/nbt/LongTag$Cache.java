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
/*    */   private static final int HIGH = 1024;
/*    */   private static final int LOW = -128;
/* 18 */   static final LongTag[] cache = new LongTag[1153];
/*    */   
/*    */   static {
/* 21 */     for (byte b = 0; b < cache.length; b++)
/* 22 */       cache[b] = new LongTag((-128 + b)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\LongTag$Cache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */