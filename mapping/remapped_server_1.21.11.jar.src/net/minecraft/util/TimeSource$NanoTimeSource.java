/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface NanoTimeSource
/*    */   extends TimeSource, LongSupplier
/*    */ {
/*    */   default long get(TimeUnit paramTimeUnit) {
/* 13 */     return paramTimeUnit.convert(getAsLong(), TimeUnit.NANOSECONDS);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\TimeSource$NanoTimeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */