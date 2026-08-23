/*     */ package net.minecraft.util.profiling.jfr.serialize;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.LongSerializationPolicy;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.time.Duration;
/*     */ import java.util.DoubleSummaryStatistics;
/*     */ import java.util.IntSummaryStatistics;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.DoubleStream;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.jfr.Percentiles;
/*     */ import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FileIOStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FpsStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*     */ import net.minecraft.util.profiling.jfr.stats.PacketIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.StructureGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class JfrResultJsonSerializer
/*     */ {
/*     */   private static final String BYTES_PER_SECOND = "bytesPerSecond";
/*     */   private static final String COUNT = "count";
/*     */   private static final String DURATION_NANOS_TOTAL = "durationNanosTotal";
/*     */   private static final String TOTAL_BYTES = "totalBytes";
/*     */   private static final String COUNT_PER_SECOND = "countPerSecond";
/*  49 */   final Gson gson = (new GsonBuilder())
/*  50 */     .setPrettyPrinting()
/*  51 */     .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
/*  52 */     .create();
/*     */   
/*     */   private static void serializePacketId(PacketIdentification paramPacketIdentification, JsonObject paramJsonObject) {
/*  55 */     paramJsonObject.addProperty("protocolId", paramPacketIdentification.protocolId());
/*  56 */     paramJsonObject.addProperty("packetId", paramPacketIdentification.packetId());
/*     */   }
/*     */   
/*     */   private static void serializeChunkId(ChunkIdentification paramChunkIdentification, JsonObject paramJsonObject) {
/*  60 */     paramJsonObject.addProperty("level", paramChunkIdentification.level());
/*  61 */     paramJsonObject.addProperty("dimension", paramChunkIdentification.dimension());
/*  62 */     paramJsonObject.addProperty("x", Integer.valueOf(paramChunkIdentification.x()));
/*  63 */     paramJsonObject.addProperty("z", Integer.valueOf(paramChunkIdentification.z()));
/*     */   }
/*     */   
/*     */   public String format(JfrStatsResult paramJfrStatsResult) {
/*  67 */     JsonObject jsonObject = new JsonObject();
/*     */     
/*  69 */     jsonObject.addProperty("startedEpoch", Long.valueOf(paramJfrStatsResult.recordingStarted().toEpochMilli()));
/*  70 */     jsonObject.addProperty("endedEpoch", Long.valueOf(paramJfrStatsResult.recordingEnded().toEpochMilli()));
/*  71 */     jsonObject.addProperty("durationMs", Long.valueOf(paramJfrStatsResult.recordingDuration().toMillis()));
/*  72 */     Duration duration = paramJfrStatsResult.worldCreationDuration();
/*  73 */     if (duration != null) {
/*  74 */       jsonObject.addProperty("worldGenDurationMs", Long.valueOf(duration.toMillis()));
/*     */     }
/*  76 */     jsonObject.add("heap", heap(paramJfrStatsResult.heapSummary()));
/*  77 */     jsonObject.add("cpuPercent", cpu(paramJfrStatsResult.cpuLoadStats()));
/*  78 */     jsonObject.add("network", network(paramJfrStatsResult));
/*  79 */     jsonObject.add("fileIO", fileIO(paramJfrStatsResult));
/*  80 */     jsonObject.add("fps", fps(paramJfrStatsResult.fps()));
/*  81 */     jsonObject.add("serverTick", serverTicks(paramJfrStatsResult.serverTickTimes()));
/*  82 */     jsonObject.add("threadAllocation", threadAllocations(paramJfrStatsResult.threadAllocationSummary()));
/*  83 */     jsonObject.add("chunkGen", chunkGen(paramJfrStatsResult.chunkGenSummary()));
/*  84 */     jsonObject.add("structureGen", structureGen(paramJfrStatsResult.structureGenStats()));
/*     */     
/*  86 */     return this.gson.toJson((JsonElement)jsonObject);
/*     */   }
/*     */   
/*     */   private JsonElement heap(GcHeapStat.Summary paramSummary) {
/*  90 */     JsonObject jsonObject = new JsonObject();
/*  91 */     jsonObject.addProperty("allocationRateBytesPerSecond", Double.valueOf(paramSummary.allocationRateBytesPerSecond()));
/*  92 */     jsonObject.addProperty("gcCount", Integer.valueOf(paramSummary.totalGCs()));
/*  93 */     jsonObject.addProperty("gcOverHeadPercent", Float.valueOf(paramSummary.gcOverHead()));
/*  94 */     jsonObject.addProperty("gcTotalDurationMs", Long.valueOf(paramSummary.gcTotalDuration().toMillis()));
/*  95 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement structureGen(List<StructureGenStat> paramList) {
/*  99 */     JsonObject jsonObject = new JsonObject();
/* 100 */     Optional<TimedStatSummary> optional = TimedStatSummary.summary(paramList);
/* 101 */     if (optional.isEmpty()) {
/* 102 */       return (JsonElement)jsonObject;
/*     */     }
/* 104 */     TimedStatSummary timedStatSummary = optional.get();
/*     */     
/* 106 */     JsonArray jsonArray = new JsonArray();
/* 107 */     jsonObject.add("structure", (JsonElement)jsonArray);
/*     */     
/* 109 */     ((Map)paramList.stream()
/* 110 */       .collect(Collectors.groupingBy(StructureGenStat::structureName)))
/* 111 */       .forEach((paramString, paramList) -> {
/*     */           Optional<TimedStatSummary> optional = TimedStatSummary.summary(paramList);
/*     */ 
/*     */           
/*     */           if (optional.isEmpty()) {
/*     */             return;
/*     */           }
/*     */           
/*     */           TimedStatSummary timedStatSummary = optional.get();
/*     */           
/*     */           JsonObject jsonObject1 = new JsonObject();
/*     */           
/*     */           paramJsonArray.add((JsonElement)jsonObject1);
/*     */           
/*     */           jsonObject1.addProperty("name", paramString);
/*     */           
/*     */           jsonObject1.addProperty("count", Integer.valueOf(timedStatSummary.count()));
/*     */           
/*     */           jsonObject1.addProperty("durationNanosTotal", Long.valueOf(timedStatSummary.totalDuration().toNanos()));
/*     */           
/*     */           jsonObject1.addProperty("durationNanosAvg", Long.valueOf(timedStatSummary.totalDuration().toNanos() / timedStatSummary.count()));
/*     */           
/*     */           JsonObject jsonObject2 = (JsonObject)Util.make(new JsonObject(), ());
/*     */           
/*     */           timedStatSummary.percentilesNanos().forEach(());
/*     */           
/*     */           Function<StructureGenStat, JsonElement> function = ();
/*     */           
/*     */           paramJsonObject.add("fastest", function.apply((StructureGenStat)paramTimedStatSummary.fastest()));
/*     */           
/*     */           paramJsonObject.add("slowest", function.apply((StructureGenStat)paramTimedStatSummary.slowest()));
/*     */           
/*     */           paramJsonObject.add("secondSlowest", (paramTimedStatSummary.secondSlowest() != null) ? function.apply((StructureGenStat)paramTimedStatSummary.secondSlowest()) : (JsonElement)JsonNull.INSTANCE);
/*     */         });
/*     */     
/* 146 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement chunkGen(List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> paramList) {
/* 150 */     JsonObject jsonObject = new JsonObject();
/* 151 */     if (paramList.isEmpty()) {
/* 152 */       return (JsonElement)jsonObject;
/*     */     }
/* 154 */     jsonObject.addProperty("durationNanosTotal", Double.valueOf(paramList.stream().mapToDouble(paramPair -> ((TimedStatSummary)paramPair.getSecond()).totalDuration().toNanos()).sum())); JsonArray jsonArray = (JsonArray)Util.make(new JsonArray(), paramJsonArray -> paramJsonObject.add("status", (JsonElement)paramJsonArray));
/*     */     
/* 156 */     for (Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>> pair : paramList) {
/* 157 */       TimedStatSummary timedStatSummary = (TimedStatSummary)pair.getSecond();
/* 158 */       Objects.requireNonNull(jsonArray); JsonObject jsonObject1 = (JsonObject)Util.make(new JsonObject(), jsonArray::add);
/* 159 */       jsonObject1.addProperty("state", ((ChunkStatus)pair.getFirst()).toString());
/* 160 */       jsonObject1.addProperty("count", Integer.valueOf(timedStatSummary.count()));
/* 161 */       jsonObject1.addProperty("durationNanosTotal", Long.valueOf(timedStatSummary.totalDuration().toNanos()));
/* 162 */       jsonObject1.addProperty("durationNanosAvg", Long.valueOf(timedStatSummary.totalDuration().toNanos() / timedStatSummary.count()));
/* 163 */       JsonObject jsonObject2 = (JsonObject)Util.make(new JsonObject(), paramJsonObject2 -> paramJsonObject1.add("durationNanosPercentiles", (JsonElement)paramJsonObject2));
/* 164 */       timedStatSummary.percentilesNanos().forEach((paramInteger, paramDouble) -> paramJsonObject.addProperty("p" + paramInteger, paramDouble));
/*     */       
/* 166 */       Function<ChunkGenStat, JsonElement> function = paramChunkGenStat -> {
/*     */           JsonObject jsonObject = new JsonObject();
/*     */           jsonObject.addProperty("durationNanos", Long.valueOf(paramChunkGenStat.duration().toNanos()));
/*     */           jsonObject.addProperty("level", paramChunkGenStat.level());
/*     */           jsonObject.addProperty("chunkPosX", Integer.valueOf((paramChunkGenStat.chunkPos()).x));
/*     */           jsonObject.addProperty("chunkPosZ", Integer.valueOf((paramChunkGenStat.chunkPos()).z));
/*     */           jsonObject.addProperty("worldPosX", Integer.valueOf(paramChunkGenStat.worldPos().x()));
/*     */           jsonObject.addProperty("worldPosZ", Integer.valueOf(paramChunkGenStat.worldPos().z()));
/*     */           return (JsonElement)jsonObject;
/*     */         };
/* 176 */       jsonObject1.add("fastest", function.apply((ChunkGenStat)timedStatSummary.fastest()));
/* 177 */       jsonObject1.add("slowest", function.apply((ChunkGenStat)timedStatSummary.slowest()));
/* 178 */       jsonObject1.add("secondSlowest", (timedStatSummary.secondSlowest() != null) ? 
/* 179 */           function.apply((ChunkGenStat)timedStatSummary.secondSlowest()) : 
/* 180 */           (JsonElement)JsonNull.INSTANCE);
/*     */     } 
/*     */     
/* 183 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement threadAllocations(ThreadAllocationStat.Summary paramSummary) {
/* 187 */     JsonArray jsonArray = new JsonArray();
/* 188 */     paramSummary.allocationsPerSecondByThread().forEach((paramString, paramDouble) -> paramJsonArray.add((JsonElement)Util.make(new JsonObject(), ())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     return (JsonElement)jsonArray;
/*     */   }
/*     */   
/*     */   private JsonElement serverTicks(List<TickTimeStat> paramList) {
/* 198 */     if (paramList.isEmpty()) {
/* 199 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/* 201 */     JsonObject jsonObject = new JsonObject();
/* 202 */     double[] arrayOfDouble = paramList.stream().mapToDouble(paramTickTimeStat -> paramTickTimeStat.currentAverage().toNanos() / 1000000.0D).toArray();
/*     */     
/* 204 */     DoubleSummaryStatistics doubleSummaryStatistics = DoubleStream.of(arrayOfDouble).summaryStatistics();
/* 205 */     jsonObject.addProperty("minMs", Double.valueOf(doubleSummaryStatistics.getMin()));
/* 206 */     jsonObject.addProperty("averageMs", Double.valueOf(doubleSummaryStatistics.getAverage()));
/* 207 */     jsonObject.addProperty("maxMs", Double.valueOf(doubleSummaryStatistics.getMax()));
/* 208 */     Map map = Percentiles.evaluate(arrayOfDouble);
/* 209 */     map.forEach((paramInteger, paramDouble) -> paramJsonObject.addProperty("p" + paramInteger, paramDouble));
/* 210 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement fps(List<FpsStat> paramList) {
/* 214 */     if (paramList.isEmpty()) {
/* 215 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/* 217 */     JsonObject jsonObject = new JsonObject();
/* 218 */     int[] arrayOfInt = paramList.stream().mapToInt(FpsStat::fps).toArray();
/* 219 */     IntSummaryStatistics intSummaryStatistics = IntStream.of(arrayOfInt).summaryStatistics();
/* 220 */     jsonObject.addProperty("minFPS", Integer.valueOf(intSummaryStatistics.getMin()));
/* 221 */     jsonObject.addProperty("averageFPS", Double.valueOf(intSummaryStatistics.getAverage()));
/* 222 */     jsonObject.addProperty("maxFPS", Integer.valueOf(intSummaryStatistics.getMax()));
/* 223 */     Map map = Percentiles.evaluate(arrayOfInt);
/* 224 */     map.forEach((paramInteger, paramDouble) -> paramJsonObject.addProperty("p" + paramInteger, paramDouble));
/* 225 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement fileIO(JfrStatsResult paramJfrStatsResult) {
/* 229 */     JsonObject jsonObject = new JsonObject();
/* 230 */     jsonObject.add("write", fileIoSummary(paramJfrStatsResult.fileWrites()));
/* 231 */     jsonObject.add("read", fileIoSummary(paramJfrStatsResult.fileReads()));
/* 232 */     jsonObject.add("chunksRead", ioSummary(paramJfrStatsResult.readChunks(), JfrResultJsonSerializer::serializeChunkId));
/* 233 */     jsonObject.add("chunksWritten", ioSummary(paramJfrStatsResult.writtenChunks(), JfrResultJsonSerializer::serializeChunkId));
/* 234 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement fileIoSummary(FileIOStat.Summary paramSummary) {
/* 238 */     JsonObject jsonObject = new JsonObject();
/* 239 */     jsonObject.addProperty("totalBytes", Long.valueOf(paramSummary.totalBytes()));
/* 240 */     jsonObject.addProperty("count", Long.valueOf(paramSummary.counts()));
/* 241 */     jsonObject.addProperty("bytesPerSecond", Double.valueOf(paramSummary.bytesPerSecond()));
/* 242 */     jsonObject.addProperty("countPerSecond", Double.valueOf(paramSummary.countsPerSecond()));
/* 243 */     JsonArray jsonArray = new JsonArray();
/* 244 */     jsonObject.add("topContributors", (JsonElement)jsonArray);
/* 245 */     paramSummary.topTenContributorsByTotalBytes().forEach(paramPair -> {
/*     */           JsonObject jsonObject = new JsonObject();
/*     */           paramJsonArray.add((JsonElement)jsonObject);
/*     */           jsonObject.addProperty("path", (String)paramPair.getFirst());
/*     */           jsonObject.addProperty("totalBytes", (Number)paramPair.getSecond());
/*     */         });
/* 251 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement network(JfrStatsResult paramJfrStatsResult) {
/* 255 */     JsonObject jsonObject = new JsonObject();
/* 256 */     jsonObject.add("sent", ioSummary(paramJfrStatsResult.sentPacketsSummary(), JfrResultJsonSerializer::serializePacketId));
/* 257 */     jsonObject.add("received", ioSummary(paramJfrStatsResult.receivedPacketsSummary(), JfrResultJsonSerializer::serializePacketId));
/* 258 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private <T> JsonElement ioSummary(IoSummary<T> paramIoSummary, BiConsumer<T, JsonObject> paramBiConsumer) {
/* 262 */     JsonObject jsonObject = new JsonObject();
/* 263 */     jsonObject.addProperty("totalBytes", Long.valueOf(paramIoSummary.getTotalSize()));
/* 264 */     jsonObject.addProperty("count", Long.valueOf(paramIoSummary.getTotalCount()));
/* 265 */     jsonObject.addProperty("bytesPerSecond", Double.valueOf(paramIoSummary.getSizePerSecond()));
/* 266 */     jsonObject.addProperty("countPerSecond", Double.valueOf(paramIoSummary.getCountsPerSecond()));
/* 267 */     JsonArray jsonArray = new JsonArray();
/* 268 */     jsonObject.add("topContributors", (JsonElement)jsonArray);
/* 269 */     paramIoSummary.largestSizeContributors().forEach(paramPair -> {
/*     */           JsonObject jsonObject = new JsonObject();
/*     */           paramJsonArray.add((JsonElement)jsonObject);
/*     */           Object object = paramPair.getFirst();
/*     */           IoSummary.CountAndSize countAndSize = (IoSummary.CountAndSize)paramPair.getSecond();
/*     */           paramBiConsumer.accept(object, jsonObject);
/*     */           jsonObject.addProperty("totalBytes", Long.valueOf(countAndSize.totalSize()));
/*     */           jsonObject.addProperty("count", Long.valueOf(countAndSize.totalCount()));
/*     */           jsonObject.addProperty("averageSize", Float.valueOf(countAndSize.averageSize()));
/*     */         });
/* 279 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private JsonElement cpu(List<CpuLoadStat> paramList) {
/* 283 */     JsonObject jsonObject = new JsonObject();
/* 284 */     BiFunction<List<CpuLoadStat>, ToDoubleFunction, JsonElement> biFunction = (paramList, paramToDoubleFunction) -> {
/*     */         JsonObject jsonObject = new JsonObject();
/*     */         
/*     */         DoubleSummaryStatistics doubleSummaryStatistics = paramList.stream().mapToDouble(paramToDoubleFunction).summaryStatistics();
/*     */         jsonObject.addProperty("min", Double.valueOf(doubleSummaryStatistics.getMin()));
/*     */         jsonObject.addProperty("average", Double.valueOf(doubleSummaryStatistics.getAverage()));
/*     */         jsonObject.addProperty("max", Double.valueOf(doubleSummaryStatistics.getMax()));
/*     */         return jsonObject;
/*     */       };
/* 293 */     jsonObject.add("jvm", biFunction.apply(paramList, CpuLoadStat::jvm));
/* 294 */     jsonObject.add("userJvm", biFunction.apply(paramList, CpuLoadStat::userJvm));
/* 295 */     jsonObject.add("system", biFunction.apply(paramList, CpuLoadStat::system));
/*     */     
/* 297 */     return (JsonElement)jsonObject;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\serialize\JfrResultJsonSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */