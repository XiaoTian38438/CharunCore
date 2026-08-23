/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.file.Path;
/*     */ import jdk.jfr.FlightRecorder;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface JvmProfiler
/*     */ {
/*  25 */   public static final JvmProfiler INSTANCE = (Runtime.class.getModule().getLayer().findModule("jdk.jfr").isPresent() && FlightRecorder.isAvailable()) ? JfrProfiler.getInstance() : new NoOpProfiler();
/*     */   
/*     */   boolean start(Environment paramEnvironment);
/*     */   
/*     */   Path stop();
/*     */   
/*     */   boolean isRunning();
/*     */   
/*     */   boolean isAvailable();
/*     */   
/*     */   void onServerTick(float paramFloat);
/*     */   
/*     */   void onClientTick(int paramInt);
/*     */   
/*     */   void onPacketReceived(ConnectionProtocol paramConnectionProtocol, PacketType<?> paramPacketType, SocketAddress paramSocketAddress, int paramInt);
/*     */   
/*     */   void onPacketSent(ConnectionProtocol paramConnectionProtocol, PacketType<?> paramPacketType, SocketAddress paramSocketAddress, int paramInt);
/*     */   
/*     */   void onRegionFileRead(RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos, RegionFileVersion paramRegionFileVersion, int paramInt);
/*     */   
/*     */   void onRegionFileWrite(RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos, RegionFileVersion paramRegionFileVersion, int paramInt);
/*     */   
/*     */   ProfiledDuration onWorldLoadedStarted();
/*     */   
/*     */   ProfiledDuration onChunkGenerate(ChunkPos paramChunkPos, ResourceKey<Level> paramResourceKey, String paramString);
/*     */   
/*     */   ProfiledDuration onStructureGenerate(ChunkPos paramChunkPos, ResourceKey<Level> paramResourceKey, Holder<Structure> paramHolder);
/*     */   
/*     */   public static class NoOpProfiler implements JvmProfiler {
/*  54 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean start(Environment param1Environment) {
/*  59 */       LOGGER.warn("Attempted to start Flight Recorder, but it's not supported on this JVM");
/*  60 */       return false;
/*     */     } static final ProfiledDuration noOpCommit = param1Boolean -> {
/*     */       
/*     */       };
/*     */     public Path stop() {
/*  65 */       throw new IllegalStateException("Attempted to stop Flight Recorder, but it's not supported on this JVM");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRunning() {
/*  70 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAvailable() {
/*  75 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPacketReceived(ConnectionProtocol param1ConnectionProtocol, PacketType<?> param1PacketType, SocketAddress param1SocketAddress, int param1Int) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onPacketSent(ConnectionProtocol param1ConnectionProtocol, PacketType<?> param1PacketType, SocketAddress param1SocketAddress, int param1Int) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onRegionFileRead(RegionStorageInfo param1RegionStorageInfo, ChunkPos param1ChunkPos, RegionFileVersion param1RegionFileVersion, int param1Int) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onRegionFileWrite(RegionStorageInfo param1RegionStorageInfo, ChunkPos param1ChunkPos, RegionFileVersion param1RegionFileVersion, int param1Int) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onServerTick(float param1Float) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onClientTick(int param1Int) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ProfiledDuration onWorldLoadedStarted() {
/* 110 */       return noOpCommit;
/*     */     }
/*     */ 
/*     */     
/*     */     public ProfiledDuration onChunkGenerate(ChunkPos param1ChunkPos, ResourceKey<Level> param1ResourceKey, String param1String) {
/* 115 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ProfiledDuration onStructureGenerate(ChunkPos param1ChunkPos, ResourceKey<Level> param1ResourceKey, Holder<Structure> param1Holder) {
/* 120 */       return noOpCommit;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\JvmProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */