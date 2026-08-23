/*    */ package net.minecraft.stats;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface StatFormatter {
/*  9 */   public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.00", DecimalFormatSymbols.getInstance(Locale.ROOT)); public static final StatFormatter DIVIDE_BY_TEN; public static final StatFormatter DISTANCE;
/*    */   public static final StatFormatter TIME;
/* 11 */   public static final StatFormatter DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format; static { Objects.requireNonNull(NumberFormat.getIntegerInstance(Locale.US)); } static {
/* 12 */     DIVIDE_BY_TEN = (paramInt -> DECIMAL_FORMAT.format(paramInt * 0.1D));
/* 13 */     DISTANCE = (paramInt -> {
/*    */         double d1 = paramInt / 100.0D;
/*    */ 
/*    */         
/*    */         double d2 = d1 / 1000.0D;
/*    */ 
/*    */         
/*    */         return (d2 > 0.5D) ? (DECIMAL_FORMAT.format(d2) + " km") : ((d1 > 0.5D) ? (DECIMAL_FORMAT.format(d1) + " m") : ("" + paramInt + " cm"));
/*    */       });
/*    */ 
/*    */     
/* 24 */     TIME = (paramInt -> {
/*    */         double d1 = paramInt / 20.0D;
/*    */         double d2 = d1 / 60.0D;
/*    */         double d3 = d2 / 60.0D;
/*    */         double d4 = d3 / 24.0D;
/*    */         double d5 = d4 / 365.0D;
/*    */         return (d5 > 0.5D) ? (DECIMAL_FORMAT.format(d5) + " y") : ((d4 > 0.5D) ? (DECIMAL_FORMAT.format(d4) + " d") : ((d3 > 0.5D) ? (DECIMAL_FORMAT.format(d3) + " h") : ((d2 > 0.5D) ? (DECIMAL_FORMAT.format(d2) + " min") : ("" + d1 + " s"))));
/*    */       });
/*    */   }
/*    */   
/*    */   String format(int paramInt);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\StatFormatter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */