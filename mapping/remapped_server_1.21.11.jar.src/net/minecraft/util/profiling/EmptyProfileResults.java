/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class EmptyProfileResults implements ProfileResults {
/*  8 */   public static final EmptyProfileResults EMPTY = new EmptyProfileResults();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ResultField> getTimes(String paramString) {
/* 15 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean saveResults(Path paramPath) {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getStartTimeNano() {
/* 25 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStartTimeTicks() {
/* 30 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getEndTimeNano() {
/* 35 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEndTimeTicks() {
/* 40 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getProfilerResults() {
/* 45 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\EmptyProfileResults.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */