/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.PrimitiveSink;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.time.Instant;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalLong;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class HttpUtil {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Path downloadFile(Path paramPath, URL paramURL, Map<String, String> paramMap, HashFunction paramHashFunction, HashCode paramHashCode, int paramInt, Proxy paramProxy, DownloadProgressListener paramDownloadProgressListener) {
/*     */     Path path;
/*  48 */     HttpURLConnection httpURLConnection = null;
/*  49 */     InputStream inputStream = null;
/*     */     
/*  51 */     paramDownloadProgressListener.requestStart();
/*     */ 
/*     */     
/*  54 */     if (paramHashCode != null) {
/*  55 */       path = cachedFilePath(paramPath, paramHashCode);
/*     */       try {
/*  57 */         if (checkExistingFile(path, paramHashFunction, paramHashCode)) {
/*  58 */           LOGGER.info("Returning cached file since actual hash matches requested");
/*  59 */           paramDownloadProgressListener.requestFinished(true);
/*     */           
/*  61 */           updateModificationTime(path);
/*  62 */           return path;
/*     */         } 
/*  64 */       } catch (IOException iOException) {
/*  65 */         LOGGER.warn("Failed to check cached file {}", path, iOException);
/*     */       } 
/*     */       try {
/*  68 */         LOGGER.warn("Existing file {} not found or had mismatched hash", path);
/*  69 */         Files.deleteIfExists(path);
/*  70 */       } catch (IOException iOException) {
/*  71 */         paramDownloadProgressListener.requestFinished(false);
/*  72 */         throw new UncheckedIOException("Failed to remove existing file " + String.valueOf(path), iOException);
/*     */       } 
/*     */     } else {
/*  75 */       path = null;
/*     */     } 
/*     */     
/*     */     try {
/*  79 */       httpURLConnection = (HttpURLConnection)paramURL.openConnection(paramProxy);
/*  80 */       httpURLConnection.setInstanceFollowRedirects(true);
/*     */       
/*  82 */       Objects.requireNonNull(httpURLConnection); paramMap.forEach(httpURLConnection::setRequestProperty);
/*     */       
/*  84 */       inputStream = httpURLConnection.getInputStream();
/*  85 */       long l = httpURLConnection.getContentLengthLong();
/*  86 */       OptionalLong optionalLong = (l != -1L) ? OptionalLong.of(l) : OptionalLong.empty();
/*     */       
/*  88 */       FileUtil.createDirectoriesSafe(paramPath);
/*     */       
/*  90 */       paramDownloadProgressListener.downloadStart(optionalLong);
/*     */       
/*  92 */       if (optionalLong.isPresent() && 
/*  93 */         optionalLong.getAsLong() > paramInt) {
/*  94 */         throw new IOException("Filesize is bigger than maximum allowed (file is " + String.valueOf(optionalLong) + ", limit is " + paramInt + ")");
/*     */       }
/*     */ 
/*     */       
/*  98 */       if (path != null) {
/*  99 */         HashCode hashCode = downloadAndHash(paramHashFunction, paramInt, paramDownloadProgressListener, inputStream, path);
/* 100 */         if (!hashCode.equals(paramHashCode)) {
/* 101 */           throw new IOException("Hash of downloaded file (" + String.valueOf(hashCode) + ") did not match requested (" + String.valueOf(paramHashCode) + ")");
/*     */         }
/* 103 */         paramDownloadProgressListener.requestFinished(true);
/* 104 */         return path;
/*     */       } 
/* 106 */       Path path1 = Files.createTempFile(paramPath, "download", ".tmp", (FileAttribute<?>[])new FileAttribute[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 124 */     catch (Throwable throwable) {
/* 125 */       if (httpURLConnection != null) {
/* 126 */         InputStream inputStream1 = httpURLConnection.getErrorStream();
/* 127 */         if (inputStream1 != null) {
/*     */           try {
/* 129 */             LOGGER.error("HTTP response error: {}", IOUtils.toString(inputStream1, StandardCharsets.UTF_8));
/* 130 */           } catch (Exception exception) {
/* 131 */             LOGGER.error("Failed to read response from server");
/*     */           } 
/*     */         }
/*     */       } 
/* 135 */       paramDownloadProgressListener.requestFinished(false);
/* 136 */       throw new IllegalStateException("Failed to download file " + String.valueOf(paramURL), throwable);
/*     */     } finally {
/* 138 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void updateModificationTime(Path paramPath) {
/*     */     try {
/* 144 */       Files.setLastModifiedTime(paramPath, FileTime.from(Instant.now()));
/* 145 */     } catch (IOException iOException) {
/* 146 */       LOGGER.warn("Failed to update modification time of {}", paramPath, iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static HashCode hashFile(Path paramPath, HashFunction paramHashFunction) throws IOException {
/* 151 */     Hasher hasher = paramHashFunction.newHasher();
/* 152 */     OutputStream outputStream = Funnels.asOutputStream((PrimitiveSink)hasher); 
/* 153 */     try { InputStream inputStream = Files.newInputStream(paramPath, new OpenOption[0]);
/*     */       
/* 155 */       try { inputStream.transferTo(outputStream);
/* 156 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (outputStream != null) outputStream.close();  } catch (Throwable throwable) { if (outputStream != null)
/* 157 */         try { outputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return hasher.hash();
/*     */   }
/*     */   
/*     */   private static boolean checkExistingFile(Path paramPath, HashFunction paramHashFunction, HashCode paramHashCode) throws IOException {
/* 161 */     if (Files.exists(paramPath, new java.nio.file.LinkOption[0])) {
/* 162 */       HashCode hashCode = hashFile(paramPath, paramHashFunction);
/* 163 */       if (hashCode.equals(paramHashCode)) {
/* 164 */         return true;
/*     */       }
/* 166 */       LOGGER.warn("Mismatched hash of file {}, expected {} but found {}", new Object[] { paramPath, paramHashCode, hashCode });
/*     */     } 
/*     */     
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   private static Path cachedFilePath(Path paramPath, HashCode paramHashCode) {
/* 173 */     return paramPath.resolve(paramHashCode.toString());
/*     */   }
/*     */   
/*     */   private static HashCode downloadAndHash(HashFunction paramHashFunction, int paramInt, DownloadProgressListener paramDownloadProgressListener, InputStream paramInputStream, Path paramPath) throws IOException {
/* 177 */     OutputStream outputStream = Files.newOutputStream(paramPath, new OpenOption[] { StandardOpenOption.CREATE }); 
/* 178 */     try { Hasher hasher = paramHashFunction.newHasher();
/*     */       
/* 180 */       byte[] arrayOfByte = new byte[8196];
/*     */       
/* 182 */       long l = 0L; int i;
/* 183 */       while ((i = paramInputStream.read(arrayOfByte)) >= 0) {
/* 184 */         l += i;
/* 185 */         paramDownloadProgressListener.downloadedBytes(l);
/*     */         
/* 187 */         if (l > paramInt) {
/* 188 */           throw new IOException("Filesize was bigger than maximum allowed (got >= " + l + ", limit was " + paramInt + ")");
/*     */         }
/*     */         
/* 191 */         if (Thread.interrupted()) {
/* 192 */           LOGGER.error("INTERRUPTED");
/* 193 */           throw new IOException("Download interrupted");
/*     */         } 
/*     */         
/* 196 */         outputStream.write(arrayOfByte, 0, i);
/* 197 */         hasher.putBytes(arrayOfByte, 0, i);
/*     */       } 
/* 199 */       HashCode hashCode = hasher.hash();
/* 200 */       if (outputStream != null) outputStream.close();  return hashCode; } catch (Throwable throwable) { if (outputStream != null)
/*     */         try { outputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 204 */      } public static int getAvailablePort() { try { ServerSocket serverSocket = new ServerSocket(0); 
/* 205 */       try { int i = serverSocket.getLocalPort();
/* 206 */         serverSocket.close(); return i; } catch (Throwable throwable) { try { serverSocket.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/* 207 */     { return 25564; }
/*     */      }
/*     */ 
/*     */   
/*     */   public static boolean isPortAvailable(int paramInt) {
/* 212 */     if (paramInt < 0 || paramInt > 65535)
/* 213 */       return false; 
/*     */     
/* 215 */     try { ServerSocket serverSocket = new ServerSocket(paramInt); 
/* 216 */       try { boolean bool = (serverSocket.getLocalPort() == paramInt) ? true : false;
/* 217 */         serverSocket.close(); return bool; } catch (Throwable throwable) { try { serverSocket.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/* 218 */     { return false; }
/*     */   
/*     */   }
/*     */   
/*     */   public static interface DownloadProgressListener {
/*     */     void requestStart();
/*     */     
/*     */     void downloadStart(OptionalLong param1OptionalLong);
/*     */     
/*     */     void downloadedBytes(long param1Long);
/*     */     
/*     */     void requestFinished(boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\HttpUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */