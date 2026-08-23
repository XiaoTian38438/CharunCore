/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TimeSource {
/*    */   long get(TimeUnit paramTimeUnit);
/*    */   
/*    */   public static interface NanoTimeSource
/*    */     extends TimeSource, LongSupplier {
/*    */     default long get(TimeUnit param1TimeUnit) {
/* 13 */       return param1TimeUnit.convert(getAsLong(), TimeUnit.NANOSECONDS);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\TimeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */