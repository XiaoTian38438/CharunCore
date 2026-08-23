/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Enabled;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*    */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Category({"Minecraft", "Storage"})
/*    */ @StackTrace(false)
/*    */ @Enabled(false)
/*    */ public abstract class ChunkRegionIoEvent
/*    */   extends Event
/*    */ {
/*    */   @Name("regionPosX")
/*    */   @Label("Region X Position")
/*    */   public final int regionPosX;
/*    */   @Name("regionPosZ")
/*    */   @Label("Region Z Position")
/*    */   public final int regionPosZ;
/*    */   @Name("localPosX")
/*    */   @Label("Local X Position")
/*    */   public final int localChunkPosX;
/*    */   @Name("localPosZ")
/*    */   @Label("Local Z Position")
/*    */   public final int localChunkPosZ;
/*    */   @Name("chunkPosX")
/*    */   @Label("Chunk X Position")
/*    */   public final int chunkPosX;
/*    */   @Name("chunkPosZ")
/*    */   @Label("Chunk Z Position")
/*    */   public final int chunkPosZ;
/*    */   @Name("level")
/*    */   @Label("Level Id")
/*    */   public final String levelId;
/*    */   @Name("dimension")
/*    */   @Label("Dimension")
/*    */   public final String dimension;
/*    */   @Name("type")
/*    */   @Label("Type")
/*    */   public final String type;
/*    */   @Name("compression")
/*    */   @Label("Compression")
/*    */   public final String compression;
/*    */   @Name("bytes")
/*    */   @Label("Bytes")
/*    */   public final int bytes;
/*    */   
/*    */   public ChunkRegionIoEvent(RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos, RegionFileVersion paramRegionFileVersion, int paramInt) {
/* 63 */     this.regionPosX = paramChunkPos.getRegionX();
/* 64 */     this.regionPosZ = paramChunkPos.getRegionZ();
/* 65 */     this.localChunkPosX = paramChunkPos.getRegionLocalX();
/* 66 */     this.localChunkPosZ = paramChunkPos.getRegionLocalZ();
/* 67 */     this.chunkPosX = paramChunkPos.x;
/* 68 */     this.chunkPosZ = paramChunkPos.z;
/* 69 */     this.levelId = paramRegionStorageInfo.level();
/* 70 */     this.dimension = paramRegionStorageInfo.dimension().identifier().toString();
/* 71 */     this.type = paramRegionStorageInfo.type();
/* 72 */     this.compression = "standard:" + paramRegionFileVersion.getId();
/* 73 */     this.bytes = paramInt;
/*    */   }
/*    */   
/*    */   public static class Fields {
/*    */     public static final String REGION_POS_X = "regionPosX";
/*    */     public static final String REGION_POS_Z = "regionPosZ";
/*    */     public static final String LOCAL_POS_X = "localPosX";
/*    */     public static final String LOCAL_POS_Z = "localPosZ";
/*    */     public static final String CHUNK_POS_X = "chunkPosX";
/*    */     public static final String CHUNK_POS_Z = "chunkPosZ";
/*    */     public static final String LEVEL = "level";
/*    */     public static final String DIMENSION = "dimension";
/*    */     public static final String TYPE = "type";
/*    */     public static final String COMPRESSION = "compression";
/*    */     public static final String BYTES = "bytes";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\ChunkRegionIoEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */