/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class CrashReportCategory
/*     */ {
/*     */   private final String title;
/*  15 */   private final List<Entry> entries = Lists.newArrayList();
/*  16 */   private StackTraceElement[] stackTrace = new StackTraceElement[0];
/*     */   
/*     */   public CrashReportCategory(String paramString) {
/*  19 */     this.title = paramString;
/*     */   }
/*     */   
/*     */   public static String formatLocation(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  23 */     return String.format(Locale.ROOT, "%.2f,%.2f,%.2f", new Object[] { Double.valueOf(paramDouble1), Double.valueOf(paramDouble2), Double.valueOf(paramDouble3) });
/*     */   }
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor paramLevelHeightAccessor, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  27 */     return String.format(Locale.ROOT, "%.2f,%.2f,%.2f - %s", new Object[] { Double.valueOf(paramDouble1), Double.valueOf(paramDouble2), Double.valueOf(paramDouble3), formatLocation(paramLevelHeightAccessor, BlockPos.containing(paramDouble1, paramDouble2, paramDouble3)) });
/*     */   }
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor paramLevelHeightAccessor, BlockPos paramBlockPos) {
/*  31 */     return formatLocation(paramLevelHeightAccessor, paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   public static String formatLocation(LevelHeightAccessor paramLevelHeightAccessor, int paramInt1, int paramInt2, int paramInt3) {
/*  35 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/*     */     try {
/*  38 */       stringBuilder.append(String.format(Locale.ROOT, "World: (%d,%d,%d)", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }));
/*  39 */     } catch (Throwable throwable) {
/*  40 */       stringBuilder.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  43 */     stringBuilder.append(", ");
/*     */     
/*     */     try {
/*  46 */       int i = SectionPos.blockToSectionCoord(paramInt1);
/*  47 */       int j = SectionPos.blockToSectionCoord(paramInt2);
/*  48 */       int k = SectionPos.blockToSectionCoord(paramInt3);
/*  49 */       int m = paramInt1 & 0xF;
/*  50 */       int n = paramInt2 & 0xF;
/*  51 */       int i1 = paramInt3 & 0xF;
/*  52 */       int i2 = SectionPos.sectionToBlockCoord(i);
/*  53 */       int i3 = paramLevelHeightAccessor.getMinY();
/*  54 */       int i4 = SectionPos.sectionToBlockCoord(k);
/*  55 */       int i5 = SectionPos.sectionToBlockCoord(i + 1) - 1;
/*  56 */       int i6 = paramLevelHeightAccessor.getMaxY();
/*  57 */       int i7 = SectionPos.sectionToBlockCoord(k + 1) - 1;
/*  58 */       stringBuilder.append(String.format(Locale.ROOT, "Section: (at %d,%d,%d in %d,%d,%d; chunk contains blocks %d,%d,%d to %d,%d,%d)", new Object[] { Integer.valueOf(m), Integer.valueOf(n), Integer.valueOf(i1), Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7) }));
/*  59 */     } catch (Throwable throwable) {
/*  60 */       stringBuilder.append("(Error finding chunk loc)");
/*     */     } 
/*     */     
/*  63 */     stringBuilder.append(", ");
/*     */     
/*     */     try {
/*  66 */       int i = paramInt1 >> 9;
/*  67 */       int j = paramInt3 >> 9;
/*  68 */       int k = i << 5;
/*  69 */       int m = j << 5;
/*  70 */       int n = (i + 1 << 5) - 1;
/*  71 */       int i1 = (j + 1 << 5) - 1;
/*  72 */       int i2 = i << 9;
/*  73 */       int i3 = paramLevelHeightAccessor.getMinY();
/*  74 */       int i4 = j << 9;
/*  75 */       int i5 = (i + 1 << 9) - 1;
/*  76 */       int i6 = paramLevelHeightAccessor.getMaxY();
/*  77 */       int i7 = (j + 1 << 9) - 1;
/*  78 */       stringBuilder.append(String.format(Locale.ROOT, "Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,%d,%d to %d,%d,%d)", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k), Integer.valueOf(m), Integer.valueOf(n), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7) }));
/*  79 */     } catch (Throwable throwable) {
/*  80 */       stringBuilder.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  83 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String paramString, CrashReportDetail<String> paramCrashReportDetail) {
/*     */     try {
/*  88 */       setDetail(paramString, paramCrashReportDetail.call());
/*  89 */     } catch (Throwable throwable) {
/*  90 */       setDetailError(paramString, throwable);
/*     */     } 
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String paramString, Object paramObject) {
/*  96 */     this.entries.add(new Entry(paramString, paramObject));
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public void setDetailError(String paramString, Throwable paramThrowable) {
/* 101 */     setDetail(paramString, paramThrowable);
/*     */   }
/*     */   
/*     */   public int fillInStackTrace(int paramInt) {
/* 105 */     StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
/*     */ 
/*     */     
/* 108 */     if (arrayOfStackTraceElement.length <= 0) {
/* 109 */       return 0;
/*     */     }
/*     */     
/* 112 */     this.stackTrace = new StackTraceElement[arrayOfStackTraceElement.length - 3 - paramInt];
/* 113 */     System.arraycopy(arrayOfStackTraceElement, 3 + paramInt, this.stackTrace, 0, this.stackTrace.length);
/* 114 */     return this.stackTrace.length;
/*     */   }
/*     */   
/*     */   public boolean validateStackTrace(StackTraceElement paramStackTraceElement1, StackTraceElement paramStackTraceElement2) {
/* 118 */     if (this.stackTrace.length == 0 || paramStackTraceElement1 == null) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     StackTraceElement stackTraceElement = this.stackTrace[0];
/*     */ 
/*     */     
/* 125 */     if (stackTraceElement.isNativeMethod() != paramStackTraceElement1.isNativeMethod() || 
/* 126 */       !stackTraceElement.getClassName().equals(paramStackTraceElement1.getClassName()) || 
/* 127 */       !stackTraceElement.getFileName().equals(paramStackTraceElement1.getFileName()) || 
/* 128 */       !stackTraceElement.getMethodName().equals(paramStackTraceElement1.getMethodName()))
/*     */     {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     if (((paramStackTraceElement2 != null) ? true : false) != ((this.stackTrace.length > 1) ? true : false)) {
/* 134 */       return false;
/*     */     }
/* 136 */     if (paramStackTraceElement2 != null && !this.stackTrace[1].equals(paramStackTraceElement2)) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     this.stackTrace[0] = paramStackTraceElement1;
/*     */     
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   public void trimStacktrace(int paramInt) {
/* 146 */     StackTraceElement[] arrayOfStackTraceElement = new StackTraceElement[this.stackTrace.length - paramInt];
/* 147 */     System.arraycopy(this.stackTrace, 0, arrayOfStackTraceElement, 0, arrayOfStackTraceElement.length);
/* 148 */     this.stackTrace = arrayOfStackTraceElement;
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder paramStringBuilder) {
/* 152 */     paramStringBuilder.append("-- ").append(this.title).append(" --\n");
/* 153 */     paramStringBuilder.append("Details:");
/*     */     
/* 155 */     for (Entry entry : this.entries) {
/* 156 */       paramStringBuilder.append("\n\t");
/* 157 */       paramStringBuilder.append(entry.getKey());
/* 158 */       paramStringBuilder.append(": ");
/* 159 */       paramStringBuilder.append(entry.getValue());
/*     */     } 
/*     */     
/* 162 */     if (this.stackTrace != null && this.stackTrace.length > 0) {
/* 163 */       paramStringBuilder.append("\nStacktrace:");
/*     */       
/* 165 */       for (StackTraceElement stackTraceElement : this.stackTrace) {
/* 166 */         paramStringBuilder.append("\n\tat ");
/* 167 */         paramStringBuilder.append(stackTraceElement);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStacktrace() {
/* 173 */     return this.stackTrace;
/*     */   }
/*     */   
/*     */   public static void populateBlockDetails(CrashReportCategory paramCrashReportCategory, LevelHeightAccessor paramLevelHeightAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 177 */     Objects.requireNonNull(paramBlockState); paramCrashReportCategory.setDetail("Block", paramBlockState::toString);
/*     */     
/* 179 */     populateBlockLocationDetails(paramCrashReportCategory, paramLevelHeightAccessor, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static CrashReportCategory populateBlockLocationDetails(CrashReportCategory paramCrashReportCategory, LevelHeightAccessor paramLevelHeightAccessor, BlockPos paramBlockPos) {
/* 183 */     return paramCrashReportCategory.setDetail("Block location", () -> formatLocation(paramLevelHeightAccessor, paramBlockPos));
/*     */   }
/*     */   
/*     */   private static class Entry {
/*     */     private final String key;
/*     */     private final String value;
/*     */     
/*     */     public Entry(String param1String, Object param1Object) {
/* 191 */       this.key = param1String;
/*     */       
/* 193 */       if (param1Object == null)
/* 194 */       { this.value = "~~NULL~~"; }
/* 195 */       else if (param1Object instanceof Throwable) { Throwable throwable = (Throwable)param1Object;
/* 196 */         this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage(); }
/*     */       else
/* 198 */       { this.value = param1Object.toString(); }
/*     */     
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 203 */       return this.key;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 207 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\CrashReportCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */