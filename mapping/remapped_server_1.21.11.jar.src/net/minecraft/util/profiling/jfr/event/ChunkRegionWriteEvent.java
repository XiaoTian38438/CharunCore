/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*    */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*    */ 
/*    */ @Name("minecraft.ChunkRegionWrite")
/*    */ @Label("Region File Write")
/*    */ @DontObfuscate
/*    */ public class ChunkRegionWriteEvent extends ChunkRegionIoEvent {
/*    */   public static final String EVENT_NAME = "minecraft.ChunkRegionWrite";
/* 16 */   public static final EventType TYPE = EventType.getEventType((Class)ChunkRegionWriteEvent.class);
/*    */   
/*    */   public ChunkRegionWriteEvent(RegionStorageInfo paramRegionStorageInfo, ChunkPos paramChunkPos, RegionFileVersion paramRegionFileVersion, int paramInt) {
/* 19 */     super(paramRegionStorageInfo, paramChunkPos, paramRegionFileVersion, paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\ChunkRegionWriteEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */