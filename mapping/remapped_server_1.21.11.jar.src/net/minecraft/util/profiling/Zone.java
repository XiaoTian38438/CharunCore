/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Zone
/*    */   implements AutoCloseable
/*    */ {
/*  8 */   public static final Zone INACTIVE = new Zone(null);
/*    */   
/*    */   private final ProfilerFiller profiler;
/*    */   
/*    */   Zone(ProfilerFiller paramProfilerFiller) {
/* 13 */     this.profiler = paramProfilerFiller;
/*    */   }
/*    */   
/*    */   public Zone addText(String paramString) {
/* 17 */     if (this.profiler != null) {
/* 18 */       this.profiler.addZoneText(paramString);
/*    */     }
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   public Zone addText(Supplier<String> paramSupplier) {
/* 24 */     if (this.profiler != null) {
/* 25 */       this.profiler.addZoneText(paramSupplier.get());
/*    */     }
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public Zone addValue(long paramLong) {
/* 31 */     if (this.profiler != null) {
/* 32 */       this.profiler.addZoneValue(paramLong);
/*    */     }
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Zone setColor(int paramInt) {
/* 38 */     if (this.profiler != null) {
/* 39 */       this.profiler.setZoneColor(paramInt);
/*    */     }
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 46 */     if (this.profiler != null)
/* 47 */       this.profiler.pop(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\Zone.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */