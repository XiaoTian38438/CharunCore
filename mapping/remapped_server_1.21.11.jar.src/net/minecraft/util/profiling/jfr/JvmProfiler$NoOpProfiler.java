/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.file.Path;
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
/*     */ 
/*     */ public class NoOpProfiler
/*     */   implements JvmProfiler
/*     */ {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean start(Environment paramEnvironment) {
/*  59 */     LOGGER.warn("Attempted to start Flight Recorder, but it's not supported on this JVM");
/*  60 */     return false;
/*     */   } static final ProfiledDuration noOpCommit = paramBoolean -> {
/*     */     
/*     */     };
/*     */   public Path stop() {
/*  65 */     throw new IllegalStateException("Attempted to stop Flight Recorder, but it's not supported on this JVM");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAvailable() {
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPacketReceived(ConnectionProtocol paramConnectionProtocol, PacketType<?> paramPacketType, SocketAddress paramSocketAddress, int paramInt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPacketSent(ConnectionProtocol paramConnectionProtocol, PacketType<?> paramPacketType, SocketAddress paramSocketAddress, int paramInt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRegionFileRead(RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos, RegionFileVersion paramRegionFileVersion, int paramInt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRegionFileWrite(RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos, RegionFileVersion paramRegionFileVersion, int paramInt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onServerTick(float paramFloat) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClientTick(int paramInt) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ProfiledDuration onWorldLoadedStarted() {
/* 110 */     return noOpCommit;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfiledDuration onChunkGenerate(ChunkPos paramChunkPos, ResourceKey<Level> paramResourceKey, String paramString) {
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfiledDuration onStructureGenerate(ChunkPos paramChunkPos, ResourceKey<Level> paramResourceKey, Holder<Structure> paramHolder) {
/* 120 */     return noOpCommit;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\JvmProfiler$NoOpProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */