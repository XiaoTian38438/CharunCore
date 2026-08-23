/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Enabled;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ 
/*    */ @Name("minecraft.StructureGeneration")
/*    */ @Label("Structure Generation")
/*    */ @Category({"Minecraft", "World Generation"})
/*    */ @StackTrace(false)
/*    */ @Enabled(false)
/*    */ @DontObfuscate
/*    */ public class StructureGenerationEvent
/*    */   extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.StructureGeneration";
/* 26 */   public static final EventType TYPE = EventType.getEventType((Class)StructureGenerationEvent.class);
/*    */   
/*    */   @Name("chunkPosX")
/*    */   @Label("Chunk X Position")
/*    */   public final int chunkPosX;
/*    */   
/*    */   @Name("chunkPosZ")
/*    */   @Label("Chunk Z Position")
/*    */   public final int chunkPosZ;
/*    */   
/*    */   @Name("structure")
/*    */   @Label("Structure")
/*    */   public final String structure;
/*    */   
/*    */   @Name("level")
/*    */   @Label("Level")
/*    */   public final String level;
/*    */   
/*    */   @Name("success")
/*    */   @Label("Success")
/*    */   public boolean success;
/*    */   
/*    */   public StructureGenerationEvent(ChunkPos paramChunkPos, Holder<Structure> paramHolder, ResourceKey<Level> paramResourceKey) {
/* 49 */     this.chunkPosX = paramChunkPos.x;
/* 50 */     this.chunkPosZ = paramChunkPos.z;
/* 51 */     this.structure = paramHolder.getRegisteredName();
/* 52 */     this.level = paramResourceKey.identifier().toString();
/*    */   }
/*    */   
/*    */   public static interface Fields {
/*    */     public static final String CHUNK_POS_X = "chunkPosX";
/*    */     public static final String CHUNK_POS_Z = "chunkPosZ";
/*    */     public static final String STRUCTURE = "structure";
/*    */     public static final String LEVEL = "level";
/*    */     public static final String SUCCESS = "success";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\StructureGenerationEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */