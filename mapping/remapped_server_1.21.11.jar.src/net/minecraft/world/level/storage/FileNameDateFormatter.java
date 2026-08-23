/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.format.DateTimeFormatterBuilder;
/*    */ import java.time.format.SignStyle;
/*    */ import java.time.temporal.ChronoField;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class FileNameDateFormatter {
/* 10 */   public static final DateTimeFormatter FORMATTER = (new DateTimeFormatterBuilder())
/* 11 */     .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
/* 12 */     .appendLiteral('-')
/* 13 */     .appendValue(ChronoField.MONTH_OF_YEAR, 2)
/* 14 */     .appendLiteral('-')
/* 15 */     .appendValue(ChronoField.DAY_OF_MONTH, 2)
/* 16 */     .appendLiteral('_')
/* 17 */     .appendValue(ChronoField.HOUR_OF_DAY, 2)
/* 18 */     .appendLiteral('-')
/* 19 */     .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
/* 20 */     .appendLiteral('-')
/* 21 */     .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
/* 22 */     .toFormatter(Locale.ROOT);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\FileNameDateFormatter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */