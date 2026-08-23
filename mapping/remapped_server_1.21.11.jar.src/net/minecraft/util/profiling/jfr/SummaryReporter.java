/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ import net.minecraft.util.profiling.jfr.parse.JfrStatsParser;
/*    */ import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SummaryReporter
/*    */ {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Runnable onDeregistration;
/*    */   
/*    */   protected SummaryReporter(Runnable paramRunnable) {
/* 23 */     this.onDeregistration = paramRunnable;
/*    */   }
/*    */   public void recordingStopped(Path paramPath) {
/*    */     JfrStatsResult jfrStatsResult;
/* 27 */     if (paramPath == null) {
/*    */       return;
/*    */     }
/* 30 */     this.onDeregistration.run();
/*    */     
/* 32 */     infoWithFallback(() -> "Dumped flight recorder profiling to " + String.valueOf(paramPath));
/*    */ 
/*    */     
/*    */     try {
/* 36 */       jfrStatsResult = JfrStatsParser.parse(paramPath);
/* 37 */     } catch (Throwable throwable) {
/* 38 */       warnWithFallback(() -> "Failed to parse JFR recording", throwable);
/*    */       
/*    */       return;
/*    */     } 
/*    */     try {
/* 43 */       Objects.requireNonNull(jfrStatsResult); infoWithFallback(jfrStatsResult::asJson);
/* 44 */       Path path = paramPath.resolveSibling("jfr-report-" + StringUtils.substringBefore(paramPath.getFileName().toString(), ".jfr") + ".json");
/* 45 */       Files.writeString(path, jfrStatsResult.asJson(), new OpenOption[] { StandardOpenOption.CREATE });
/* 46 */       infoWithFallback(() -> "Dumped recording summary to " + String.valueOf(paramPath));
/* 47 */     } catch (Throwable throwable) {
/* 48 */       warnWithFallback(() -> "Failed to output JFR report", throwable);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void infoWithFallback(Supplier<String> paramSupplier) {
/* 58 */     if (LogUtils.isLoggerActive()) {
/* 59 */       LOGGER.info(paramSupplier.get());
/*    */     } else {
/* 61 */       Bootstrap.realStdoutPrintln(paramSupplier.get());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void warnWithFallback(Supplier<String> paramSupplier, Throwable paramThrowable) {
/* 72 */     if (LogUtils.isLoggerActive()) {
/* 73 */       LOGGER.warn(paramSupplier.get(), paramThrowable);
/*    */     } else {
/* 75 */       Bootstrap.realStdoutPrintln(paramSupplier.get());
/* 76 */       paramThrowable.printStackTrace(Bootstrap.STDOUT);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\SummaryReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */