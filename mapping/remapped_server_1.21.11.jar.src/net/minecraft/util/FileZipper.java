/*    */ package net.minecraft.util;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collector;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FileZipper implements Closeable {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path outputFile;
/*    */   private final Path tempFile;
/*    */   private final FileSystem fs;
/*    */   
/*    */   public FileZipper(Path paramPath) {
/* 26 */     this.outputFile = paramPath;
/* 27 */     this.tempFile = paramPath.resolveSibling(paramPath.getFileName().toString() + "_tmp");
/*    */     try {
/* 29 */       this.fs = Util.ZIP_FILE_SYSTEM_PROVIDER.newFileSystem(this.tempFile, (Map<String, ?>)ImmutableMap.of("create", "true"));
/* 30 */     } catch (IOException iOException) {
/* 31 */       throw new UncheckedIOException(iOException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Path paramPath, String paramString) {
/*    */     try {
/* 37 */       Path path1 = this.fs.getPath(File.separator, new String[0]);
/* 38 */       Path path2 = path1.resolve(paramPath.toString());
/*    */       
/* 40 */       Files.createDirectories(path2.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 41 */       Files.write(path2, paramString.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
/* 42 */     } catch (IOException iOException) {
/* 43 */       throw new UncheckedIOException(iOException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Path paramPath, File paramFile) {
/*    */     try {
/* 49 */       Path path1 = this.fs.getPath(File.separator, new String[0]);
/* 50 */       Path path2 = path1.resolve(paramPath.toString());
/*    */       
/* 52 */       Files.createDirectories(path2.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 53 */       Files.copy(paramFile.toPath(), path2, new java.nio.file.CopyOption[0]);
/* 54 */     } catch (IOException iOException) {
/* 55 */       throw new UncheckedIOException(iOException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Path paramPath) {
/*    */     
/* 61 */     try { Path path = this.fs.getPath(File.separator, new String[0]);
/*    */       
/* 63 */       if (Files.isRegularFile(paramPath, new java.nio.file.LinkOption[0])) {
/* 64 */         Path path1 = path.resolve(paramPath.getParent().relativize(paramPath).toString());
/* 65 */         Files.copy(path1, paramPath, new java.nio.file.CopyOption[0]);
/*    */         
/*    */         return;
/*    */       } 
/* 69 */       Stream<Path> stream = Files.find(paramPath, 2147483647, (paramPath, paramBasicFileAttributes) -> paramBasicFileAttributes.isRegularFile(), new java.nio.file.FileVisitOption[0]); 
/* 70 */       try { for (Path path1 : stream.collect((Collector)Collectors.toList())) {
/* 71 */           Path path2 = path.resolve(paramPath.relativize(path1).toString());
/* 72 */           Files.createDirectories(path2.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 73 */           Files.copy(path1, path2, new java.nio.file.CopyOption[0]);
/*    */         } 
/* 75 */         if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null)
/* 76 */           try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException)
/* 77 */     { throw new UncheckedIOException(iOException); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 84 */       this.fs.close();
/* 85 */       Files.move(this.tempFile, this.outputFile, new java.nio.file.CopyOption[0]);
/* 86 */       LOGGER.info("Compressed to {}", this.outputFile);
/* 87 */     } catch (IOException iOException) {
/* 88 */       throw new UncheckedIOException(iOException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\FileZipper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */