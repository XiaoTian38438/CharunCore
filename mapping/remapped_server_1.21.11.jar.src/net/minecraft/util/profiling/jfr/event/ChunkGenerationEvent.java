/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Enabled;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ @Name("minecraft.ChunkGeneration")
/*    */ @Label("Chunk Generation")
/*    */ @Category({"Minecraft", "World Generation"})
/*    */ @StackTrace(false)
/*    */ @Enabled(false)
/*    */ @DontObfuscate
/*    */ public class ChunkGenerationEvent
/*    */   extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.ChunkGeneration";
/* 24 */   public static final EventType TYPE = EventType.getEventType((Class)ChunkGenerationEvent.class);
/*    */   
/*    */   @Name("worldPosX")
/*    */   @Label("First Block X World Position")
/*    */   public final int worldPosX;
/*    */   
/*    */   @Name("worldPosZ")
/*    */   @Label("First Block Z World Position")
/*    */   public final int worldPosZ;
/*    */   
/*    */   @Name("chunkPosX")
/*    */   @Label("Chunk X Position")
/*    */   public final int chunkPosX;
/*    */   
/*    */   @Name("chunkPosZ")
/*    */   @Label("Chunk Z Position")
/*    */   public final int chunkPosZ;
/*    */   
/*    */   @Name("status")
/*    */   @Label("Status")
/*    */   public final String targetStatus;
/*    */   
/*    */   @Name("level")
/*    */   @Label("Level")
/*    */   public final String level;
/*    */   
/*    */   public ChunkGenerationEvent(ChunkPos paramChunkPos, ResourceKey<Level> paramResourceKey, String paramString) {
/* 51 */     this.targetStatus = paramString;
/* 52 */     this.level = paramResourceKey.identifier().toString();
/* 53 */     this.chunkPosX = paramChunkPos.x;
/* 54 */     this.chunkPosZ = paramChunkPos.z;
/* 55 */     this.worldPosX = paramChunkPos.getMinBlockX();
/* 56 */     this.worldPosZ = paramChunkPos.getMinBlockZ();
/*    */   }
/*    */   
/*    */   public static class Fields {
/*    */     public static final String WORLD_POS_X = "worldPosX";
/*    */     public static final String WORLD_POS_Z = "worldPosZ";
/*    */     public static final String CHUNK_POS_X = "chunkPosX";
/*    */     public static final String CHUNK_POS_Z = "chunkPosZ";
/*    */     public static final String STATUS = "status";
/*    */     public static final String LEVEL = "level";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\ChunkGenerationEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */