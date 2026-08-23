/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface ProfileResults {
/*    */   public static final char PATH_SEPARATOR = '\036';
/*    */   
/*    */   List<ResultField> getTimes(String paramString);
/*    */   
/*    */   boolean saveResults(Path paramPath);
/*    */   
/*    */   long getStartTimeNano();
/*    */   
/*    */   int getStartTimeTicks();
/*    */   
/*    */   long getEndTimeNano();
/*    */   
/*    */   int getEndTimeTicks();
/*    */   
/*    */   default long getNanoDuration() {
/* 22 */     return getEndTimeNano() - getStartTimeNano();
/*    */   }
/*    */   
/*    */   default int getTickDuration() {
/* 26 */     return getEndTimeTicks() - getStartTimeTicks();
/*    */   }
/*    */   
/*    */   String getProfilerResults();
/*    */   
/*    */   static String demanglePath(String paramString) {
/* 32 */     return paramString.replace('\036', '.');
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ProfileResults.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */