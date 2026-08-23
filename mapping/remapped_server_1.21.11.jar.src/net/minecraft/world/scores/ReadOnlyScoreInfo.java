/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ 
/*    */ public interface ReadOnlyScoreInfo
/*    */ {
/*    */   int value();
/*    */   
/*    */   boolean isLocked();
/*    */   
/*    */   NumberFormat numberFormat();
/*    */   
/*    */   default MutableComponent formatValue(NumberFormat paramNumberFormat) {
/* 17 */     return ((NumberFormat)Objects.<NumberFormat>requireNonNullElse(numberFormat(), paramNumberFormat)).format(value());
/*    */   }
/*    */   
/*    */   static MutableComponent safeFormatValue(ReadOnlyScoreInfo paramReadOnlyScoreInfo, NumberFormat paramNumberFormat) {
/* 21 */     return (paramReadOnlyScoreInfo != null) ? paramReadOnlyScoreInfo.formatValue(paramNumberFormat) : paramNumberFormat.format(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\scores\ReadOnlyScoreInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */