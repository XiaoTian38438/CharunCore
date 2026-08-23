/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.StackTrace;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ 
/*    */ @Name("minecraft.LoadWorld")
/*    */ @Label("Create/Load World")
/*    */ @Category({"Minecraft", "World Generation"})
/*    */ @StackTrace(false)
/*    */ @DontObfuscate
/*    */ public class WorldLoadFinishedEvent
/*    */   extends Event {
/*    */   public static final String EVENT_NAME = "minecraft.LoadWorld";
/* 19 */   public static final EventType TYPE = EventType.getEventType((Class)WorldLoadFinishedEvent.class);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\WorldLoadFinishedEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */