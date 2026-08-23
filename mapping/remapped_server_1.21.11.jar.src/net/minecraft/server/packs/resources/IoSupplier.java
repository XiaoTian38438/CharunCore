/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface IoSupplier<T> {
/*    */   static IoSupplier<InputStream> create(Path paramPath) {
/* 13 */     return () -> Files.newInputStream(paramPath, new java.nio.file.OpenOption[0]);
/*    */   }
/*    */   
/*    */   static IoSupplier<InputStream> create(ZipFile paramZipFile, ZipEntry paramZipEntry) {
/* 17 */     return () -> paramZipFile.getInputStream(paramZipEntry);
/*    */   }
/*    */   
/*    */   T get() throws IOException;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\IoSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */