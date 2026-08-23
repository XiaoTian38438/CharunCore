/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import jdk.jfr.Category;
/*    */ import jdk.jfr.Event;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ import jdk.jfr.Period;
/*    */ import jdk.jfr.StackTrace;
/*    */ import net.minecraft.obfuscate.DontObfuscate;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Name("minecraft.ClientFps")
/*    */ @Label("Client fps")
/*    */ @Category({"Minecraft", "Ticking"})
/*    */ @StackTrace(false)
/*    */ @Period("1 s")
/*    */ @DontObfuscate
/*    */ public class ClientFpsEvent
/*    */   extends Event
/*    */ {
/*    */   public static final String EVENT_NAME = "minecraft.ClientFps";
/* 24 */   public static final EventType TYPE = EventType.getEventType((Class)ClientFpsEvent.class);
/*    */   
/*    */   @Name("fps")
/*    */   @Label("Client fps")
/*    */   public final int fps;
/*    */   
/*    */   public ClientFpsEvent(int paramInt) {
/* 31 */     this.fps = paramInt;
/*    */   }
/*    */   
/*    */   public static class Fields {
/*    */     public static final String FPS = "fps";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\event\ClientFpsEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */