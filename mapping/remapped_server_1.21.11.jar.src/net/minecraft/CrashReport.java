/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CrashReport
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  26 */   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
/*     */   
/*     */   private final String title;
/*     */   private final Throwable exception;
/*  30 */   private final List<CrashReportCategory> details = Lists.newArrayList();
/*     */   private Path saveFile;
/*     */   private boolean trackingStackTrace = true;
/*  33 */   private StackTraceElement[] uncategorizedStackTrace = new StackTraceElement[0];
/*     */   
/*  35 */   private final SystemReport systemReport = new SystemReport();
/*     */   
/*     */   public CrashReport(String paramString, Throwable paramThrowable) {
/*  38 */     this.title = paramString;
/*  39 */     this.exception = paramThrowable;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  43 */     return this.title;
/*     */   }
/*     */   
/*     */   public Throwable getException() {
/*  47 */     return this.exception;
/*     */   }
/*     */   
/*     */   public String getDetails() {
/*  51 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/*  53 */     getDetails(stringBuilder);
/*     */     
/*  55 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder paramStringBuilder) {
/*  59 */     if ((this.uncategorizedStackTrace == null || this.uncategorizedStackTrace.length <= 0) && !this.details.isEmpty()) {
/*  60 */       this.uncategorizedStackTrace = (StackTraceElement[])ArrayUtils.subarray((Object[])((CrashReportCategory)this.details.get(0)).getStacktrace(), 0, 1);
/*     */     }
/*     */     
/*  63 */     if (this.uncategorizedStackTrace != null && this.uncategorizedStackTrace.length > 0) {
/*  64 */       paramStringBuilder.append("-- Head --\n");
/*  65 */       paramStringBuilder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
/*  66 */       paramStringBuilder.append("Stacktrace:\n");
/*     */       
/*  68 */       for (StackTraceElement stackTraceElement : this.uncategorizedStackTrace) {
/*  69 */         paramStringBuilder.append("\t").append("at ").append(stackTraceElement);
/*  70 */         paramStringBuilder.append("\n");
/*     */       } 
/*  72 */       paramStringBuilder.append("\n");
/*     */     } 
/*     */     
/*  75 */     for (CrashReportCategory crashReportCategory : this.details) {
/*  76 */       crashReportCategory.getDetails(paramStringBuilder);
/*  77 */       paramStringBuilder.append("\n\n");
/*     */     } 
/*     */     
/*  80 */     this.systemReport.appendToCrashReportString(paramStringBuilder);
/*     */   }
/*     */   
/*     */   public String getExceptionMessage() {
/*  84 */     StringWriter stringWriter = null;
/*  85 */     PrintWriter printWriter = null;
/*  86 */     Throwable throwable = this.exception;
/*     */     
/*  88 */     if (throwable.getMessage() == null) {
/*     */       
/*  90 */       if (throwable instanceof NullPointerException) {
/*  91 */         throwable = new NullPointerException(this.title);
/*  92 */       } else if (throwable instanceof StackOverflowError) {
/*  93 */         throwable = new StackOverflowError(this.title);
/*  94 */       } else if (throwable instanceof OutOfMemoryError) {
/*  95 */         throwable = new OutOfMemoryError(this.title);
/*     */       } 
/*     */       
/*  98 */       throwable.setStackTrace(this.exception.getStackTrace());
/*     */     } 
/*     */     
/*     */     try {
/* 102 */       stringWriter = new StringWriter();
/* 103 */       printWriter = new PrintWriter(stringWriter);
/* 104 */       throwable.printStackTrace(printWriter);
/* 105 */       return stringWriter.toString();
/*     */     } finally {
/* 107 */       IOUtils.closeQuietly(stringWriter);
/* 108 */       IOUtils.closeQuietly(printWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getFriendlyReport(ReportType paramReportType, List<String> paramList) {
/* 113 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/* 115 */     paramReportType.appendHeader(stringBuilder, paramList);
/*     */     
/* 117 */     stringBuilder.append("Time: ");
/* 118 */     stringBuilder.append(DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
/* 119 */     stringBuilder.append("\n");
/*     */     
/* 121 */     stringBuilder.append("Description: ");
/* 122 */     stringBuilder.append(this.title);
/* 123 */     stringBuilder.append("\n\n");
/*     */     
/* 125 */     stringBuilder.append(getExceptionMessage());
/* 126 */     stringBuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
/*     */     
/* 128 */     for (byte b = 0; b < 87; b++) {
/* 129 */       stringBuilder.append("-");
/*     */     }
/* 131 */     stringBuilder.append("\n\n");
/* 132 */     getDetails(stringBuilder);
/*     */     
/* 134 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public String getFriendlyReport(ReportType paramReportType) {
/* 138 */     return getFriendlyReport(paramReportType, List.of());
/*     */   }
/*     */   
/*     */   public Path getSaveFile() {
/* 142 */     return this.saveFile;
/*     */   }
/*     */   
/*     */   public boolean saveToFile(Path paramPath, ReportType paramReportType, List<String> paramList) {
/* 146 */     if (this.saveFile != null) {
/* 147 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 151 */       if (paramPath.getParent() != null) {
/* 152 */         FileUtil.createDirectoriesSafe(paramPath.getParent());
/*     */       }
/*     */       
/* 155 */       BufferedWriter bufferedWriter = Files.newBufferedWriter(paramPath, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/* 156 */       try { bufferedWriter.write(getFriendlyReport(paramReportType, paramList));
/* 157 */         if (bufferedWriter != null) bufferedWriter.close();  } catch (Throwable throwable) { if (bufferedWriter != null)
/*     */           try { bufferedWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 159 */        this.saveFile = paramPath;
/* 160 */       return true;
/* 161 */     } catch (Throwable throwable) {
/* 162 */       LOGGER.error("Could not save crash report to {}", paramPath, throwable);
/* 163 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean saveToFile(Path paramPath, ReportType paramReportType) {
/* 168 */     return saveToFile(paramPath, paramReportType, List.of());
/*     */   }
/*     */   
/*     */   public SystemReport getSystemReport() {
/* 172 */     return this.systemReport;
/*     */   }
/*     */   
/*     */   public CrashReportCategory addCategory(String paramString) {
/* 176 */     return addCategory(paramString, 1);
/*     */   }
/*     */   
/*     */   public CrashReportCategory addCategory(String paramString, int paramInt) {
/* 180 */     CrashReportCategory crashReportCategory = new CrashReportCategory(paramString);
/*     */     
/* 182 */     if (this.trackingStackTrace) {
/* 183 */       int i = crashReportCategory.fillInStackTrace(paramInt);
/* 184 */       StackTraceElement[] arrayOfStackTraceElement = this.exception.getStackTrace();
/* 185 */       StackTraceElement stackTraceElement1 = null;
/* 186 */       StackTraceElement stackTraceElement2 = null;
/*     */       
/* 188 */       int j = arrayOfStackTraceElement.length - i;
/* 189 */       if (j < 0) {
/* 190 */         LOGGER.error("Negative index in crash report handler ({}/{})", Integer.valueOf(arrayOfStackTraceElement.length), Integer.valueOf(i));
/*     */       }
/*     */       
/* 193 */       if (arrayOfStackTraceElement != null && 0 <= j && j < arrayOfStackTraceElement.length) {
/* 194 */         stackTraceElement1 = arrayOfStackTraceElement[j];
/*     */         
/* 196 */         if (arrayOfStackTraceElement.length + 1 - i < arrayOfStackTraceElement.length) {
/* 197 */           stackTraceElement2 = arrayOfStackTraceElement[arrayOfStackTraceElement.length + 1 - i];
/*     */         }
/*     */       } 
/*     */       
/* 201 */       this.trackingStackTrace = crashReportCategory.validateStackTrace(stackTraceElement1, stackTraceElement2);
/*     */       
/* 203 */       if (arrayOfStackTraceElement != null && arrayOfStackTraceElement.length >= i && 0 <= j && j < arrayOfStackTraceElement.length) {
/* 204 */         this.uncategorizedStackTrace = new StackTraceElement[j];
/* 205 */         System.arraycopy(arrayOfStackTraceElement, 0, this.uncategorizedStackTrace, 0, this.uncategorizedStackTrace.length);
/*     */       } else {
/* 207 */         this.trackingStackTrace = false;
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     this.details.add(crashReportCategory);
/* 212 */     return crashReportCategory;
/*     */   }
/*     */ 
/*     */   
/*     */   public static CrashReport forThrowable(Throwable paramThrowable, String paramString) {
/*     */     CrashReport crashReport;
/* 218 */     while (paramThrowable instanceof java.util.concurrent.CompletionException && paramThrowable.getCause() != null) {
/* 219 */       paramThrowable = paramThrowable.getCause();
/*     */     }
/*     */     
/* 222 */     if (paramThrowable instanceof ReportedException) { ReportedException reportedException = (ReportedException)paramThrowable;
/* 223 */       crashReport = reportedException.getReport(); }
/*     */     else
/* 225 */     { crashReport = new CrashReport(paramString, paramThrowable); }
/*     */ 
/*     */     
/* 228 */     return crashReport;
/*     */   }
/*     */   
/*     */   public static void preload() {
/* 232 */     MemoryReserve.allocate();
/* 233 */     (new CrashReport("Don't panic!", new Throwable())).getFriendlyReport(ReportType.CRASH);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\CrashReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */