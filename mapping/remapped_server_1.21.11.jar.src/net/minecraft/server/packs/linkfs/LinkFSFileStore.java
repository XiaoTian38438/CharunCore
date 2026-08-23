/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileStore;
/*    */ import java.nio.file.attribute.BasicFileAttributeView;
/*    */ import java.nio.file.attribute.FileAttributeView;
/*    */ 
/*    */ 
/*    */ class LinkFSFileStore
/*    */   extends FileStore
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public LinkFSFileStore(String paramString) {
/* 15 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 20 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String type() {
/* 25 */     return "index";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadOnly() {
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTotalSpace() {
/* 35 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getUsableSpace() {
/* 40 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getUnallocatedSpace() {
/* 45 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> paramClass) {
/* 50 */     return (paramClass == BasicFileAttributeView.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsFileAttributeView(String paramString) {
/* 55 */     return "basic".equals(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> paramClass) {
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAttribute(String paramString) throws IOException {
/* 65 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\linkfs\LinkFSFileStore.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */