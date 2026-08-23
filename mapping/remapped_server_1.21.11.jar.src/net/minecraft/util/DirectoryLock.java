/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileLock;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.AccessDeniedException;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ 
/*    */ public class DirectoryLock
/*    */   implements AutoCloseable {
/*    */   public static final String LOCK_FILE = "session.lock";
/*    */   private final FileChannel lockFile;
/*    */   private final FileLock lock;
/*    */   private static final ByteBuffer DUMMY;
/*    */   
/*    */   static {
/* 22 */     byte[] arrayOfByte = "☃".getBytes(StandardCharsets.UTF_8);
/* 23 */     DUMMY = ByteBuffer.allocateDirect(arrayOfByte.length);
/* 24 */     DUMMY.put(arrayOfByte);
/* 25 */     DUMMY.flip();
/*    */   }
/*    */   
/*    */   public static DirectoryLock create(Path paramPath) throws IOException {
/* 29 */     Path path = paramPath.resolve("session.lock");
/*    */     
/* 31 */     FileUtil.createDirectoriesSafe(paramPath);
/* 32 */     FileChannel fileChannel = FileChannel.open(path, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
/*    */     
/*    */     try {
/* 35 */       fileChannel.write(DUMMY.duplicate());
/* 36 */       fileChannel.force(true);
/* 37 */       FileLock fileLock = fileChannel.tryLock();
/* 38 */       if (fileLock == null) {
/* 39 */         throw LockException.alreadyLocked(path);
/*    */       }
/* 41 */       return new DirectoryLock(fileChannel, fileLock);
/* 42 */     } catch (IOException iOException) {
/*    */       try {
/* 44 */         fileChannel.close();
/* 45 */       } catch (IOException iOException1) {
/* 46 */         iOException.addSuppressed(iOException1);
/*    */       } 
/* 48 */       throw iOException;
/*    */     } 
/*    */   }
/*    */   
/*    */   private DirectoryLock(FileChannel paramFileChannel, FileLock paramFileLock) {
/* 53 */     this.lockFile = paramFileChannel;
/* 54 */     this.lock = paramFileLock;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 60 */       if (this.lock.isValid()) {
/* 61 */         this.lock.release();
/*    */       }
/*    */     } finally {
/* 64 */       if (this.lockFile.isOpen()) {
/* 65 */         this.lockFile.close();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 71 */     return this.lock.isValid();
/*    */   }
/*    */   
/*    */   public static boolean isLocked(Path paramPath) throws IOException {
/* 75 */     Path path = paramPath.resolve("session.lock");
/*    */     
/* 77 */     try { FileChannel fileChannel = FileChannel.open(path, new OpenOption[] { StandardOpenOption.WRITE }); 
/* 78 */       try { FileLock fileLock = fileChannel.tryLock(); 
/* 79 */         try { boolean bool = (fileLock == null) ? true : false;
/* 80 */           if (fileLock != null) fileLock.close();  if (fileChannel != null) fileChannel.close();  return bool; } catch (Throwable throwable) { if (fileLock != null) try { fileLock.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (fileChannel != null) try { fileChannel.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (AccessDeniedException accessDeniedException)
/* 81 */     { return true; }
/* 82 */     catch (NoSuchFileException noSuchFileException)
/* 83 */     { return false; }
/*    */   
/*    */   }
/*    */   
/*    */   public static class LockException extends IOException {
/*    */     private LockException(Path param1Path, String param1String) {
/* 89 */       super(String.valueOf(param1Path.toAbsolutePath()) + ": " + String.valueOf(param1Path.toAbsolutePath()));
/*    */     }
/*    */     
/*    */     public static LockException alreadyLocked(Path param1Path) {
/* 93 */       return new LockException(param1Path, "already locked (possibly by other Minecraft instance?)");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\DirectoryLock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */