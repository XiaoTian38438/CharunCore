/*    */ package net.minecraft.util.profiling.jfr.parse;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ import jdk.jfr.consumer.RecordingFile;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements Iterator<RecordedEvent>
/*    */ {
/*    */   public boolean hasNext() {
/* 78 */     return recordingFile.hasMoreEvents();
/*    */   }
/*    */ 
/*    */   
/*    */   public RecordedEvent next() {
/* 83 */     if (!hasNext()) {
/* 84 */       throw new NoSuchElementException();
/*    */     }
/*    */     try {
/* 87 */       return recordingFile.readEvent();
/* 88 */     } catch (IOException iOException) {
/* 89 */       throw new UncheckedIOException(iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsParser$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */