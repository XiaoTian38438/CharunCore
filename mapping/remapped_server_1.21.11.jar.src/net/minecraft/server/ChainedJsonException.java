/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ public class ChainedJsonException
/*    */   extends IOException
/*    */ {
/* 12 */   private final List<Entry> entries = Lists.newArrayList();
/*    */   private final String message;
/*    */   
/*    */   public ChainedJsonException(String paramString) {
/* 16 */     this.entries.add(new Entry());
/* 17 */     this.message = paramString;
/*    */   }
/*    */   
/*    */   public ChainedJsonException(String paramString, Throwable paramThrowable) {
/* 21 */     super(paramThrowable);
/* 22 */     this.entries.add(new Entry());
/* 23 */     this.message = paramString;
/*    */   }
/*    */   
/*    */   public void prependJsonKey(String paramString) {
/* 27 */     ((Entry)this.entries.get(0)).addJsonKey(paramString);
/*    */   }
/*    */   
/*    */   public void setFilenameAndFlush(String paramString) {
/* 31 */     ((Entry)this.entries.get(0)).filename = paramString;
/* 32 */     this.entries.add(0, new Entry());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 37 */     return "Invalid " + String.valueOf(this.entries.get(this.entries.size() - 1)) + ": " + this.message;
/*    */   }
/*    */   
/*    */   public static ChainedJsonException forException(Exception paramException) {
/* 41 */     if (paramException instanceof ChainedJsonException) {
/* 42 */       return (ChainedJsonException)paramException;
/*    */     }
/* 44 */     String str = paramException.getMessage();
/* 45 */     if (paramException instanceof java.io.FileNotFoundException) {
/* 46 */       str = "File not found";
/*    */     }
/* 48 */     return new ChainedJsonException(str, paramException);
/*    */   }
/*    */   
/*    */   public static class Entry
/*    */   {
/*    */     String filename;
/* 54 */     private final List<String> jsonKeys = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     void addJsonKey(String param1String) {
/* 60 */       this.jsonKeys.add(0, param1String);
/*    */     }
/*    */     
/*    */     public String getFilename() {
/* 64 */       return this.filename;
/*    */     }
/*    */     
/*    */     public String getJsonKeys() {
/* 68 */       return StringUtils.join(this.jsonKeys, "->");
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 73 */       if (this.filename != null) {
/* 74 */         if (this.jsonKeys.isEmpty()) {
/* 75 */           return this.filename;
/*    */         }
/* 77 */         return this.filename + " " + this.filename;
/*    */       } 
/*    */       
/* 80 */       if (this.jsonKeys.isEmpty()) {
/* 81 */         return "(Unknown file)";
/*    */       }
/* 83 */       return "(Unknown file) " + getJsonKeys();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ChainedJsonException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */