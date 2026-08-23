/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ public class Entry
/*    */ {
/*    */   String filename;
/* 54 */   private final List<String> jsonKeys = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void addJsonKey(String paramString) {
/* 60 */     this.jsonKeys.add(0, paramString);
/*    */   }
/*    */   
/*    */   public String getFilename() {
/* 64 */     return this.filename;
/*    */   }
/*    */   
/*    */   public String getJsonKeys() {
/* 68 */     return StringUtils.join(this.jsonKeys, "->");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     if (this.filename != null) {
/* 74 */       if (this.jsonKeys.isEmpty()) {
/* 75 */         return this.filename;
/*    */       }
/* 77 */       return this.filename + " " + this.filename;
/*    */     } 
/*    */     
/* 80 */     if (this.jsonKeys.isEmpty()) {
/* 81 */       return "(Unknown file)";
/*    */     }
/* 83 */     return "(Unknown file) " + getJsonKeys();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ChainedJsonException$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */