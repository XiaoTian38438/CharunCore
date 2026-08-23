/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ 
/*    */ 
/*    */ public class TimeUtil
/*    */ {
/*  9 */   public static final long NANOSECONDS_PER_SECOND = TimeUnit.SECONDS.toNanos(1L);
/* 10 */   public static final long NANOSECONDS_PER_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1L);
/* 11 */   public static final long MILLISECONDS_PER_SECOND = TimeUnit.SECONDS.toMillis(1L);
/* 12 */   public static final long SECONDS_PER_HOUR = TimeUnit.HOURS.toSeconds(1L);
/* 13 */   public static final int SECONDS_PER_MINUTE = (int)TimeUnit.MINUTES.toSeconds(1L);
/*    */   
/*    */   public static UniformInt rangeOfSeconds(int paramInt1, int paramInt2) {
/* 16 */     return UniformInt.of(paramInt1 * 20, paramInt2 * 20);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\TimeUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */