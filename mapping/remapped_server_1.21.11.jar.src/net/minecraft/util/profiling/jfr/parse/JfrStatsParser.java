/*     */ package net.minecraft.util.profiling.jfr.parse;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Spliterators;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import jdk.jfr.consumer.RecordedEvent;
/*     */ import jdk.jfr.consumer.RecordingFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JfrStatsParser
/*     */ {
/*  47 */   private Instant recordingStarted = Instant.EPOCH;
/*  48 */   private Instant recordingEnded = Instant.EPOCH;
/*     */   
/*  50 */   private final List<ChunkGenStat> chunkGenStats = new ArrayList<>();
/*  51 */   private final List<StructureGenStat> structureGenStats = new ArrayList<>();
/*  52 */   private final List<CpuLoadStat> cpuLoadStat = new ArrayList<>();
/*  53 */   private final Map<PacketIdentification, MutableCountAndSize> receivedPackets = new HashMap<>();
/*  54 */   private final Map<PacketIdentification, MutableCountAndSize> sentPackets = new HashMap<>();
/*  55 */   private final Map<ChunkIdentification, MutableCountAndSize> readChunks = new HashMap<>();
/*  56 */   private final Map<ChunkIdentification, MutableCountAndSize> writtenChunks = new HashMap<>();
/*  57 */   private final List<FileIOStat> fileWrites = new ArrayList<>();
/*  58 */   private final List<FileIOStat> fileReads = new ArrayList<>();
/*     */   private int garbageCollections;
/*  60 */   private Duration gcTotalDuration = Duration.ZERO;
/*  61 */   private final List<GcHeapStat> gcHeapStats = new ArrayList<>();
/*  62 */   private final List<ThreadAllocationStat> threadAllocationStats = new ArrayList<>();
/*     */   
/*  64 */   private final List<FpsStat> fps = new ArrayList<>();
/*  65 */   private final List<TickTimeStat> serverTickTimes = new ArrayList<>();
/*     */   
/*  67 */   private Duration worldCreationDuration = null;
/*     */   
/*     */   private JfrStatsParser(Stream<RecordedEvent> paramStream) {
/*  70 */     capture(paramStream);
/*     */   }
/*     */   public static JfrStatsResult parse(Path paramPath) {
/*     */     
/*  74 */     try { final RecordingFile recordingFile = new RecordingFile(paramPath); 
/*  75 */       try { Iterator<RecordedEvent> iterator = new Iterator<RecordedEvent>()
/*     */           {
/*     */             public boolean hasNext() {
/*  78 */               return recordingFile.hasMoreEvents();
/*     */             }
/*     */ 
/*     */             
/*     */             public RecordedEvent next() {
/*  83 */               if (!hasNext()) {
/*  84 */                 throw new NoSuchElementException();
/*     */               }
/*     */               try {
/*  87 */                 return recordingFile.readEvent();
/*  88 */               } catch (IOException iOException) {
/*  89 */                 throw new UncheckedIOException(iOException);
/*     */               } 
/*     */             }
/*     */           };
/*  93 */         Stream<?> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 1297), false);
/*  94 */         JfrStatsResult jfrStatsResult = (new JfrStatsParser((Stream)stream)).results();
/*  95 */         recordingFile.close(); return jfrStatsResult; } catch (Throwable throwable) { try { recordingFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/*  96 */     { throw new UncheckedIOException(iOException); }
/*     */   
/*     */   }
/*     */   
/*     */   private JfrStatsResult results() {
/* 101 */     Duration duration = Duration.between(this.recordingStarted, this.recordingEnded);
/* 102 */     return new JfrStatsResult(this.recordingStarted, this.recordingEnded, duration, this.worldCreationDuration, this.fps, this.serverTickTimes, this.cpuLoadStat, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         GcHeapStat.summary(duration, this.gcHeapStats, this.gcTotalDuration, this.garbageCollections), 
/* 111 */         ThreadAllocationStat.summary(this.threadAllocationStats), 
/* 112 */         collectIoStats(duration, this.receivedPackets), 
/* 113 */         collectIoStats(duration, this.sentPackets), 
/* 114 */         collectIoStats(duration, this.writtenChunks), 
/* 115 */         collectIoStats(duration, this.readChunks), 
/* 116 */         FileIOStat.summary(duration, this.fileWrites), 
/* 117 */         FileIOStat.summary(duration, this.fileReads), this.chunkGenStats, this.structureGenStats);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void capture(Stream<RecordedEvent> paramStream) {
/* 124 */     paramStream.forEach(paramRecordedEvent -> { if (paramRecordedEvent.getEndTime().isAfter(this.recordingEnded) || this.recordingEnded.equals(Instant.EPOCH))
/*     */             this.recordingEnded = paramRecordedEvent.getEndTime();  if (paramRecordedEvent.getStartTime().isBefore(this.recordingStarted) || this.recordingStarted.equals(Instant.EPOCH))
/*     */             this.recordingStarted = paramRecordedEvent.getStartTime();  switch (paramRecordedEvent.getEventType().getName()) {
/*     */             case "minecraft.ChunkGeneration":
/*     */               this.chunkGenStats.add(ChunkGenStat.from(paramRecordedEvent)); break;
/*     */             case "minecraft.StructureGeneration":
/*     */               this.structureGenStats.add(StructureGenStat.from(paramRecordedEvent)); break;
/*     */             case "minecraft.LoadWorld":
/*     */               this.worldCreationDuration = paramRecordedEvent.getDuration(); break;
/*     */             case "minecraft.ClientFps":
/*     */               this.fps.add(FpsStat.from(paramRecordedEvent, "fps")); break;
/*     */             case "minecraft.ServerTickTime":
/*     */               this.serverTickTimes.add(TickTimeStat.from(paramRecordedEvent)); break;
/*     */             case "minecraft.PacketReceived":
/*     */               incrementPacket(paramRecordedEvent, paramRecordedEvent.getInt("bytes"), this.receivedPackets); break;
/*     */             case "minecraft.PacketSent":
/*     */               incrementPacket(paramRecordedEvent, paramRecordedEvent.getInt("bytes"), this.sentPackets); break;
/*     */             case "minecraft.ChunkRegionRead":
/*     */               incrementChunk(paramRecordedEvent, paramRecordedEvent.getInt("bytes"), this.readChunks); break;
/*     */             case "minecraft.ChunkRegionWrite":
/*     */               incrementChunk(paramRecordedEvent, paramRecordedEvent.getInt("bytes"), this.writtenChunks); break;
/*     */             case "jdk.ThreadAllocationStatistics":
/*     */               this.threadAllocationStats.add(ThreadAllocationStat.from(paramRecordedEvent)); break;
/*     */             case "jdk.GCHeapSummary":
/*     */               this.gcHeapStats.add(GcHeapStat.from(paramRecordedEvent)); break;
/*     */             case "jdk.CPULoad":
/*     */               this.cpuLoadStat.add(CpuLoadStat.from(paramRecordedEvent)); break;
/*     */             case "jdk.FileWrite":
/*     */               appendFileIO(paramRecordedEvent, this.fileWrites, "bytesWritten"); break;
/*     */             case "jdk.FileRead":
/*     */               appendFileIO(paramRecordedEvent, this.fileReads, "bytesRead"); break;
/*     */             case "jdk.GarbageCollection":
/*     */               this.garbageCollections++; this.gcTotalDuration = this.gcTotalDuration.plus(paramRecordedEvent.getDuration()); break;
/*     */           } 
/* 158 */         }); } private void incrementPacket(RecordedEvent paramRecordedEvent, int paramInt, Map<PacketIdentification, MutableCountAndSize> paramMap) { ((MutableCountAndSize)paramMap.computeIfAbsent(PacketIdentification.from(paramRecordedEvent), paramPacketIdentification -> new MutableCountAndSize())).increment(paramInt); }
/*     */ 
/*     */   
/*     */   private void incrementChunk(RecordedEvent paramRecordedEvent, int paramInt, Map<ChunkIdentification, MutableCountAndSize> paramMap) {
/* 162 */     ((MutableCountAndSize)paramMap.computeIfAbsent(ChunkIdentification.from(paramRecordedEvent), paramChunkIdentification -> new MutableCountAndSize())).increment(paramInt);
/*     */   }
/*     */   
/*     */   private void appendFileIO(RecordedEvent paramRecordedEvent, List<FileIOStat> paramList, String paramString) {
/* 166 */     paramList.add(new FileIOStat(paramRecordedEvent.getDuration(), paramRecordedEvent.getString("path"), paramRecordedEvent.getLong(paramString)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> IoSummary<T> collectIoStats(Duration paramDuration, Map<T, MutableCountAndSize> paramMap) {
/* 172 */     List list = paramMap.entrySet().stream().map(paramEntry -> Pair.of(paramEntry.getKey(), ((MutableCountAndSize)paramEntry.getValue()).toCountAndSize())).toList();
/* 173 */     return new IoSummary(paramDuration, list);
/*     */   }
/*     */   
/*     */   public static final class MutableCountAndSize {
/*     */     private long count;
/*     */     private long totalSize;
/*     */     
/*     */     public void increment(int param1Int) {
/* 181 */       this.totalSize += param1Int;
/* 182 */       this.count++;
/*     */     }
/*     */     
/*     */     public IoSummary.CountAndSize toCountAndSize() {
/* 186 */       return new IoSummary.CountAndSize(this.count, this.totalSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */