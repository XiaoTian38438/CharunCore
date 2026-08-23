/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.hash.HashingOutputStream;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.util.FastBufferedInputStream;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class NbtToSnbt implements DataProvider {
/* 30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Iterable<Path> inputFolders;
/*    */   private final PackOutput output;
/*    */   
/*    */   public NbtToSnbt(PackOutput paramPackOutput, Collection<Path> paramCollection) {
/* 36 */     this.inputFolders = paramCollection;
/* 37 */     this.output = paramPackOutput;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 42 */     Path path = this.output.getOutputFolder();
/*    */     
/* 44 */     ArrayList arrayList = new ArrayList();
/*    */     
/* 46 */     for (Iterator<Path> iterator = this.inputFolders.iterator(); iterator.hasNext(); ) { Path path1 = iterator.next();
/* 47 */       arrayList.add(CompletableFuture.supplyAsync(() -> { try { Stream<Path> stream = Files.walk(paramPath1, new java.nio.file.FileVisitOption[0]); try { CompletableFuture<Void> completableFuture = CompletableFuture.allOf((CompletableFuture<?>[])stream.filter(()).map(()).toArray(())); if (stream != null)
/* 48 */                     stream.close();  return completableFuture; } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1)
/*    */                     { throwable.addSuppressed(throwable1); }
/*    */                      
/*    */                   throw throwable; }
/*    */                  }
/* 53 */               catch (IOException iOException)
/*    */               { LOGGER.error("Failed to read structure input directory", iOException);
/*    */                 return CompletableFuture.completedFuture(null); }
/*    */             
/* 57 */             }Util.backgroundExecutor().forName("NbtToSnbt")).thenCompose(paramCompletableFuture -> paramCompletableFuture)); }
/*    */ 
/*    */     
/* 60 */     return CompletableFuture.allOf((CompletableFuture<?>[])arrayList.toArray(paramInt -> new CompletableFuture[paramInt]));
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 65 */     return "NBT -> SNBT";
/*    */   }
/*    */   
/*    */   private static String getName(Path paramPath1, Path paramPath2) {
/* 69 */     String str = paramPath1.relativize(paramPath2).toString().replaceAll("\\\\", "/");
/* 70 */     return str.substring(0, str.length() - ".nbt".length());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Path convertStructure(CachedOutput paramCachedOutput, Path paramPath1, String paramString, Path paramPath2) {
/*    */     
/* 79 */     try { InputStream inputStream = Files.newInputStream(paramPath1, new java.nio.file.OpenOption[0]); 
/* 80 */       try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(inputStream);
/*    */         
/* 82 */         try { Path path1 = paramPath2.resolve(paramString + ".snbt");
/* 83 */           writeSnbt(paramCachedOutput, path1, NbtUtils.structureToSnbt(NbtIo.readCompressed((InputStream)fastBufferedInputStream, NbtAccounter.unlimitedHeap())));
/* 84 */           LOGGER.info("Converted {} from NBT to SNBT", paramString);
/* 85 */           Path path2 = path1;
/* 86 */           fastBufferedInputStream.close(); if (inputStream != null) inputStream.close();  return path2; } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException)
/* 87 */     { LOGGER.error("Couldn't convert {} from NBT to SNBT at {}", new Object[] { paramString, paramPath1, iOException });
/* 88 */       return null; }
/*    */   
/*    */   }
/*    */   
/*    */   public static void writeSnbt(CachedOutput paramCachedOutput, Path paramPath, String paramString) throws IOException {
/* 93 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 94 */     HashingOutputStream hashingOutputStream = new HashingOutputStream(Hashing.sha1(), byteArrayOutputStream);
/* 95 */     hashingOutputStream.write(paramString.getBytes(StandardCharsets.UTF_8));
/* 96 */     hashingOutputStream.write(10);
/* 97 */     paramCachedOutput.writeIfNeeded(paramPath, byteArrayOutputStream.toByteArray(), hashingOutputStream.hash());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\structures\NbtToSnbt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */