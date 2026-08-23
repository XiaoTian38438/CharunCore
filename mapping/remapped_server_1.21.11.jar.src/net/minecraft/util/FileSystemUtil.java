/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.nio.file.FileSystemAlreadyExistsException;
/*    */ import java.nio.file.FileSystemNotFoundException;
/*    */ import java.nio.file.FileSystems;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Collections;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FileSystemUtil
/*    */ {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static Path safeGetPath(URI paramURI) throws IOException {
/*    */     try {
/* 20 */       return Paths.get(paramURI);
/* 21 */     } catch (FileSystemNotFoundException fileSystemNotFoundException) {
/*    */     
/* 23 */     } catch (Throwable throwable) {
/* 24 */       LOGGER.warn("Unable to get path for: {}", paramURI, throwable);
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 29 */       FileSystems.newFileSystem(paramURI, Collections.emptyMap());
/* 30 */     } catch (FileSystemAlreadyExistsException fileSystemAlreadyExistsException) {}
/*    */ 
/*    */ 
/*    */     
/* 34 */     return Paths.get(paramURI);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\FileSystemUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */