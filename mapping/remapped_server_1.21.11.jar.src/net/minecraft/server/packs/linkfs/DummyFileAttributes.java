/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileTime;
/*    */ 
/*    */ abstract class DummyFileAttributes
/*    */   implements BasicFileAttributes
/*    */ {
/*  9 */   private static final FileTime EPOCH = FileTime.fromMillis(0L);
/*    */ 
/*    */   
/*    */   public FileTime lastModifiedTime() {
/* 13 */     return EPOCH;
/*    */   }
/*    */ 
/*    */   
/*    */   public FileTime lastAccessTime() {
/* 18 */     return EPOCH;
/*    */   }
/*    */ 
/*    */   
/*    */   public FileTime creationTime() {
/* 23 */     return EPOCH;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolicLink() {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOther() {
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public long size() {
/* 38 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object fileKey() {
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\linkfs\DummyFileAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */