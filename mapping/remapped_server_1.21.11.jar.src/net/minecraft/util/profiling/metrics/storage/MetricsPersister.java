/*     */ package net.minecraft.util.profiling.metrics.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.time.ZoneId;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.CsvOutput;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MetricsPersister {
/*  30 */   public static final Path PROFILING_RESULTS_DIR = Paths.get("debug/profiling", new String[0]);
/*     */   public static final String METRICS_DIR_NAME = "metrics";
/*     */   public static final String DEVIATIONS_DIR_NAME = "deviations";
/*     */   public static final String PROFILING_RESULT_FILENAME = "profiling.txt";
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final String rootFolderName;
/*     */   
/*     */   public MetricsPersister(String paramString) {
/*  39 */     this.rootFolderName = paramString;
/*     */   }
/*     */   
/*     */   public Path saveReports(Set<MetricSampler> paramSet, Map<MetricSampler, List<RecordedDeviation>> paramMap, ProfileResults paramProfileResults) {
/*     */     try {
/*  44 */       Files.createDirectories(PROFILING_RESULTS_DIR, (FileAttribute<?>[])new FileAttribute[0]);
/*  45 */     } catch (IOException iOException) {
/*  46 */       throw new UncheckedIOException(iOException);
/*     */     } 
/*     */     
/*     */     try {
/*  50 */       Path path1 = Files.createTempDirectory("minecraft-profiling", (FileAttribute<?>[])new FileAttribute[0]);
/*  51 */       path1.toFile().deleteOnExit();
/*     */       
/*  53 */       Files.createDirectories(PROFILING_RESULTS_DIR, (FileAttribute<?>[])new FileAttribute[0]);
/*  54 */       Path path2 = path1.resolve(this.rootFolderName);
/*  55 */       Path path3 = path2.resolve("metrics");
/*     */       
/*  57 */       saveMetrics(paramSet, path3);
/*     */       
/*  59 */       if (!paramMap.isEmpty()) {
/*  60 */         saveDeviations(paramMap, path2.resolve("deviations"));
/*     */       }
/*     */       
/*  63 */       saveProfilingTaskExecutionResult(paramProfileResults, path2);
/*  64 */       return path1;
/*  65 */     } catch (IOException iOException) {
/*  66 */       throw new UncheckedIOException(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveMetrics(Set<MetricSampler> paramSet, Path paramPath) {
/*  71 */     if (paramSet.isEmpty()) {
/*  72 */       throw new IllegalArgumentException("Expected at least one sampler to persist");
/*     */     }
/*     */     
/*  75 */     Map map = (Map)paramSet.stream().collect(Collectors.groupingBy(MetricSampler::getCategory));
/*  76 */     map.forEach((paramMetricCategory, paramList) -> saveCategory(paramMetricCategory, paramList, paramPath));
/*     */   }
/*     */   
/*     */   private void saveCategory(MetricCategory paramMetricCategory, List<MetricSampler> paramList, Path paramPath) {
/*  80 */     Path path = paramPath.resolve(Util.sanitizeName(paramMetricCategory.getDescription(), Identifier::validPathChar) + ".csv");
/*     */     
/*  82 */     BufferedWriter bufferedWriter = null;
/*     */     try {
/*  84 */       Files.createDirectories(path.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/*  85 */       bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/*     */       
/*  87 */       CsvOutput.Builder builder = CsvOutput.builder();
/*  88 */       builder.addColumn("@tick");
/*  89 */       for (MetricSampler metricSampler : paramList) {
/*  90 */         builder.addColumn(metricSampler.getName());
/*     */       }
/*  92 */       CsvOutput csvOutput = builder.build(bufferedWriter);
/*     */ 
/*     */ 
/*     */       
/*  96 */       List list = (List)paramList.stream().map(MetricSampler::result).collect(Collectors.toList());
/*     */       
/*  98 */       int i = list.stream().mapToInt(MetricSampler.SamplerResult::getFirstTick).summaryStatistics().getMin();
/*  99 */       int j = list.stream().mapToInt(MetricSampler.SamplerResult::getLastTick).summaryStatistics().getMax();
/*     */       
/* 101 */       for (int k = i; k <= j; k++) {
/* 102 */         int m = k;
/*     */         
/* 104 */         Stream<?> stream = list.stream().map(paramSamplerResult -> String.valueOf(paramSamplerResult.valueAtTick(paramInt)));
/*     */         
/* 106 */         Object[] arrayOfObject = Stream.concat(Stream.of(String.valueOf(k)), stream).toArray(paramInt -> new String[paramInt]);
/* 107 */         csvOutput.writeRow(arrayOfObject);
/*     */       } 
/*     */       
/* 110 */       LOGGER.info("Flushed metrics to {}", path);
/* 111 */     } catch (Exception exception) {
/* 112 */       LOGGER.error("Could not save profiler results to {}", path, exception);
/*     */     } finally {
/* 114 */       IOUtils.closeQuietly(bufferedWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveDeviations(Map<MetricSampler, List<RecordedDeviation>> paramMap, Path paramPath) {
/* 119 */     DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss.SSS", Locale.UK).withZone(ZoneId.systemDefault());
/* 120 */     paramMap.forEach((paramMetricSampler, paramList) -> paramList.forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveProfilingTaskExecutionResult(ProfileResults paramProfileResults, Path paramPath) {
/* 130 */     paramProfileResults.saveResults(paramPath.resolve("profiling.txt"));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\storage\MetricsPersister.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */