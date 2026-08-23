/*     */ package net.minecraft.util.profiling;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import java.io.BufferedWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FilledProfileResults implements ProfileResults {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  29 */   private static final ProfilerPathEntry EMPTY = new ProfilerPathEntry()
/*     */     {
/*     */       public long getDuration() {
/*  32 */         return 0L;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getMaxDuration() {
/*  37 */         return 0L;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getCount() {
/*  42 */         return 0L;
/*     */       }
/*     */ 
/*     */       
/*     */       public Object2LongMap<String> getCounters() {
/*  47 */         return Object2LongMaps.emptyMap();
/*     */       }
/*     */     };
/*     */   private static final Comparator<Map.Entry<String, CounterCollector>> COUNTER_ENTRY_COMPARATOR;
/*  51 */   private static final Splitter SPLITTER = Splitter.on('\036'); static {
/*  52 */     COUNTER_ENTRY_COMPARATOR = Map.Entry.<String, CounterCollector>comparingByValue(Comparator.comparingLong(paramCounterCollector -> paramCounterCollector.totalValue)).reversed();
/*     */   }
/*     */   private final Map<String, ? extends ProfilerPathEntry> entries;
/*     */   private final long startTimeNano;
/*     */   private final int startTimeTicks;
/*     */   private final long endTimeNano;
/*     */   private final int endTimeTicks;
/*     */   private final int tickDuration;
/*     */   
/*     */   public FilledProfileResults(Map<String, ? extends ProfilerPathEntry> paramMap, long paramLong1, int paramInt1, long paramLong2, int paramInt2) {
/*  62 */     this.entries = paramMap;
/*  63 */     this.startTimeNano = paramLong1;
/*  64 */     this.startTimeTicks = paramInt1;
/*  65 */     this.endTimeNano = paramLong2;
/*  66 */     this.endTimeTicks = paramInt2;
/*  67 */     this.tickDuration = paramInt2 - paramInt1;
/*     */   }
/*     */ 
/*     */   
/*     */   private ProfilerPathEntry getEntry(String paramString) {
/*  72 */     ProfilerPathEntry profilerPathEntry = this.entries.get(paramString);
/*  73 */     return (profilerPathEntry != null) ? profilerPathEntry : EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResultField> getTimes(String paramString) {
/*  78 */     String str = paramString;
/*  79 */     ProfilerPathEntry profilerPathEntry1 = getEntry("root");
/*  80 */     long l1 = profilerPathEntry1.getDuration();
/*  81 */     ProfilerPathEntry profilerPathEntry2 = getEntry(paramString);
/*  82 */     long l2 = profilerPathEntry2.getDuration();
/*  83 */     long l3 = profilerPathEntry2.getCount();
/*     */     
/*  85 */     ArrayList<ResultField> arrayList = Lists.newArrayList();
/*     */     
/*  87 */     if (!paramString.isEmpty()) {
/*  88 */       paramString = paramString + "\036";
/*     */     }
/*  90 */     long l4 = 0L;
/*     */     
/*  92 */     for (String str1 : this.entries.keySet()) {
/*  93 */       if (isDirectChild(paramString, str1)) {
/*  94 */         l4 += getEntry(str1).getDuration();
/*     */       }
/*     */     } 
/*     */     
/*  98 */     float f = (float)l4;
/*  99 */     if (l4 < l2) {
/* 100 */       l4 = l2;
/*     */     }
/* 102 */     if (l1 < l4) {
/* 103 */       l1 = l4;
/*     */     }
/*     */     
/* 106 */     for (String str1 : this.entries.keySet()) {
/* 107 */       if (isDirectChild(paramString, str1)) {
/* 108 */         ProfilerPathEntry profilerPathEntry = getEntry(str1);
/* 109 */         long l = profilerPathEntry.getDuration();
/* 110 */         double d1 = l * 100.0D / l4;
/* 111 */         double d2 = l * 100.0D / l1;
/* 112 */         String str2 = str1.substring(paramString.length());
/* 113 */         arrayList.add(new ResultField(str2, d1, d2, profilerPathEntry.getCount()));
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     if ((float)l4 > f) {
/* 118 */       arrayList.add(new ResultField("unspecified", ((float)l4 - f) * 100.0D / l4, ((float)l4 - f) * 100.0D / l1, l3));
/*     */     }
/*     */     
/* 121 */     Collections.sort(arrayList);
/* 122 */     arrayList.add(0, new ResultField(str, 100.0D, l4 * 100.0D / l1, l3));
/* 123 */     return arrayList;
/*     */   }
/*     */   
/*     */   private static boolean isDirectChild(String paramString1, String paramString2) {
/* 127 */     return (paramString2.length() > paramString1.length() && paramString2.startsWith(paramString1) && paramString2.indexOf('\036', paramString1.length() + 1) < 0);
/*     */   }
/*     */   
/*     */   private Map<String, CounterCollector> getCounterValues() {
/* 131 */     TreeMap<String, CounterCollector> treeMap = Maps.newTreeMap();
/* 132 */     this.entries.forEach((paramString, paramProfilerPathEntry) -> {
/*     */           Object2LongMap<String> object2LongMap = paramProfilerPathEntry.getCounters();
/*     */           
/*     */           if (!object2LongMap.isEmpty()) {
/*     */             List list = SPLITTER.splitToList(paramString);
/*     */             
/*     */             object2LongMap.forEach(());
/*     */           } 
/*     */         });
/* 141 */     return treeMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getStartTimeNano() {
/* 146 */     return this.startTimeNano;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartTimeTicks() {
/* 151 */     return this.startTimeTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getEndTimeNano() {
/* 156 */     return this.endTimeNano;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndTimeTicks() {
/* 161 */     return this.endTimeTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveResults(Path paramPath) {
/* 166 */     BufferedWriter bufferedWriter = null;
/*     */     try {
/* 168 */       Files.createDirectories(paramPath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 169 */       bufferedWriter = Files.newBufferedWriter(paramPath, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/* 170 */       bufferedWriter.write(getProfilerResults(getNanoDuration(), getTickDuration()));
/* 171 */       return true;
/* 172 */     } catch (Throwable throwable) {
/* 173 */       LOGGER.error("Could not save profiler results to {}", paramPath, throwable);
/* 174 */       return false;
/*     */     } finally {
/* 176 */       IOUtils.closeQuietly(bufferedWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getProfilerResults(long paramLong, int paramInt) {
/* 181 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/* 183 */     ReportType.PROFILE.appendHeader(stringBuilder, List.of());
/*     */     
/* 185 */     stringBuilder.append("Version: ").append(SharedConstants.getCurrentVersion().id()).append('\n');
/* 186 */     stringBuilder.append("Time span: ").append(paramLong / 1000000L).append(" ms\n");
/* 187 */     stringBuilder.append("Tick span: ").append(paramInt).append(" ticks\n");
/* 188 */     stringBuilder.append("// This is approximately ").append(String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(paramInt / (float)paramLong / 1.0E9F) })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
/*     */     
/* 190 */     stringBuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
/*     */     
/* 192 */     appendProfilerResults(0, "root", stringBuilder);
/*     */     
/* 194 */     stringBuilder.append("--- END PROFILE DUMP ---\n\n");
/*     */     
/* 196 */     Map<String, CounterCollector> map = getCounterValues();
/*     */     
/* 198 */     if (!map.isEmpty()) {
/* 199 */       stringBuilder.append("--- BEGIN COUNTER DUMP ---\n\n");
/* 200 */       appendCounters(map, stringBuilder, paramInt);
/* 201 */       stringBuilder.append("--- END COUNTER DUMP ---\n\n");
/*     */     } 
/*     */     
/* 204 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProfilerResults() {
/* 209 */     StringBuilder stringBuilder = new StringBuilder();
/* 210 */     appendProfilerResults(0, "root", stringBuilder);
/* 211 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private static StringBuilder indentLine(StringBuilder paramStringBuilder, int paramInt) {
/* 215 */     paramStringBuilder.append(String.format(Locale.ROOT, "[%02d] ", new Object[] { Integer.valueOf(paramInt) }));
/* 216 */     for (byte b = 0; b < paramInt; b++) {
/* 217 */       paramStringBuilder.append("|   ");
/*     */     }
/* 219 */     return paramStringBuilder;
/*     */   }
/*     */   
/*     */   private void appendProfilerResults(int paramInt, String paramString, StringBuilder paramStringBuilder) {
/* 223 */     List<ResultField> list = getTimes(paramString);
/*     */     
/* 225 */     Object2LongMap<String> object2LongMap = ((ProfilerPathEntry)ObjectUtils.firstNonNull((Object[])new ProfilerPathEntry[] { this.entries.get(paramString), EMPTY })).getCounters();
/* 226 */     object2LongMap.forEach((paramString, paramLong) -> indentLine(paramStringBuilder, paramInt).append('#').append(paramString).append(' ').append(paramLong).append('/').append(paramLong / this.tickDuration).append('\n'));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (list.size() < 3) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 242 */     for (byte b = 1; b < list.size(); b++) {
/* 243 */       ResultField resultField = list.get(b);
/*     */       
/* 245 */       indentLine(paramStringBuilder, paramInt)
/* 246 */         .append(resultField.name)
/* 247 */         .append('(')
/* 248 */         .append(resultField.count)
/* 249 */         .append('/')
/* 250 */         .append(String.format(Locale.ROOT, "%.0f", new Object[] { Float.valueOf((float)resultField.count / this.tickDuration)
/* 251 */             })).append(')')
/* 252 */         .append(" - ")
/* 253 */         .append(String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(resultField.percentage) })).append("%/")
/* 254 */         .append(String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(resultField.globalPercentage) })).append("%\n");
/*     */       
/* 256 */       if (!"unspecified".equals(resultField.name)) {
/*     */         try {
/* 258 */           appendProfilerResults(paramInt + 1, paramString + "\036" + paramString, paramStringBuilder);
/* 259 */         } catch (Exception exception) {
/* 260 */           paramStringBuilder.append("[[ EXCEPTION ").append(exception).append(" ]]");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendCounterResults(int paramInt1, String paramString, CounterCollector paramCounterCollector, int paramInt2, StringBuilder paramStringBuilder) {
/* 267 */     indentLine(paramStringBuilder, paramInt1)
/* 268 */       .append(paramString).append(" total:")
/* 269 */       .append(paramCounterCollector.selfValue).append('/')
/* 270 */       .append(paramCounterCollector.totalValue).append(" average: ")
/* 271 */       .append(paramCounterCollector.selfValue / paramInt2)
/* 272 */       .append('/')
/* 273 */       .append(paramCounterCollector.totalValue / paramInt2)
/* 274 */       .append('\n');
/* 275 */     paramCounterCollector.children.entrySet().stream().sorted(COUNTER_ENTRY_COMPARATOR).forEach(paramEntry -> appendCounterResults(paramInt1 + 1, (String)paramEntry.getKey(), (CounterCollector)paramEntry.getValue(), paramInt2, paramStringBuilder));
/*     */   }
/*     */   
/*     */   private void appendCounters(Map<String, CounterCollector> paramMap, StringBuilder paramStringBuilder, int paramInt) {
/* 279 */     paramMap.forEach((paramString, paramCounterCollector) -> {
/*     */           paramStringBuilder.append("-- Counter: ").append(paramString).append(" --\n");
/*     */           appendCounterResults(0, "root", paramCounterCollector.children.get("root"), paramInt, paramStringBuilder);
/*     */           paramStringBuilder.append("\n\n");
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDuration() {
/* 288 */     return this.tickDuration;
/*     */   }
/*     */   
/*     */   private static class CounterCollector {
/*     */     long selfValue;
/*     */     long totalValue;
/* 294 */     final Map<String, CounterCollector> children = Maps.newHashMap();
/*     */     
/*     */     public void addValue(Iterator<String> param1Iterator, long param1Long) {
/* 297 */       this.totalValue += param1Long;
/* 298 */       if (!param1Iterator.hasNext()) {
/* 299 */         this.selfValue += param1Long;
/*     */       } else {
/* 301 */         ((CounterCollector)this.children.computeIfAbsent(param1Iterator.next(), param1String -> new CounterCollector())).addValue(param1Iterator, param1Long);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\FilledProfileResults.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */