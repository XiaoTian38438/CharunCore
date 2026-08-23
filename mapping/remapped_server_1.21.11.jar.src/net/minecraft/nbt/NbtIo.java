/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UTFDataFormatException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.util.DelegateDataOutput;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NbtIo
/*     */ {
/*  31 */   private static final OpenOption[] SYNC_OUTPUT_OPTIONS = new OpenOption[] { StandardOpenOption.SYNC, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING };
/*     */ 
/*     */   
/*     */   public static CompoundTag readCompressed(Path paramPath, NbtAccounter paramNbtAccounter) throws IOException {
/*  35 */     InputStream inputStream = Files.newInputStream(paramPath, new OpenOption[0]); 
/*  36 */     try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(inputStream);
/*     */       
/*  38 */       try { CompoundTag compoundTag = readCompressed((InputStream)fastBufferedInputStream, paramNbtAccounter);
/*  39 */         fastBufferedInputStream.close(); if (inputStream != null) inputStream.close();  return compoundTag; } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (inputStream != null)
/*     */         try { inputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  43 */      } private static DataInputStream createDecompressorStream(InputStream paramInputStream) throws IOException { return new DataInputStream((InputStream)new FastBufferedInputStream(new GZIPInputStream(paramInputStream))); }
/*     */ 
/*     */   
/*     */   private static DataOutputStream createCompressorStream(OutputStream paramOutputStream) throws IOException {
/*  47 */     return new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(paramOutputStream)));
/*     */   }
/*     */   
/*     */   public static CompoundTag readCompressed(InputStream paramInputStream, NbtAccounter paramNbtAccounter) throws IOException {
/*  51 */     DataInputStream dataInputStream = createDecompressorStream(paramInputStream); 
/*  52 */     try { CompoundTag compoundTag = read(dataInputStream, paramNbtAccounter);
/*  53 */       if (dataInputStream != null) dataInputStream.close();  return compoundTag; }
/*     */     catch (Throwable throwable) { if (dataInputStream != null)
/*     */         try { dataInputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  58 */      } public static void parseCompressed(Path paramPath, StreamTagVisitor paramStreamTagVisitor, NbtAccounter paramNbtAccounter) throws IOException { InputStream inputStream = Files.newInputStream(paramPath, new OpenOption[0]); 
/*  59 */     try { FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(inputStream);
/*     */       
/*  61 */       try { parseCompressed((InputStream)fastBufferedInputStream, paramStreamTagVisitor, paramNbtAccounter);
/*  62 */         fastBufferedInputStream.close(); } catch (Throwable throwable) { try { fastBufferedInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null)
/*     */         try { inputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  66 */      } public static void parseCompressed(InputStream paramInputStream, StreamTagVisitor paramStreamTagVisitor, NbtAccounter paramNbtAccounter) throws IOException { DataInputStream dataInputStream = createDecompressorStream(paramInputStream); 
/*  67 */     try { parse(dataInputStream, paramStreamTagVisitor, paramNbtAccounter);
/*  68 */       if (dataInputStream != null) dataInputStream.close();  } catch (Throwable throwable) { if (dataInputStream != null)
/*     */         try { dataInputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  72 */      } public static void writeCompressed(CompoundTag paramCompoundTag, Path paramPath) throws IOException { OutputStream outputStream = Files.newOutputStream(paramPath, SYNC_OUTPUT_OPTIONS); 
/*  73 */     try { BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream); 
/*  74 */       try { writeCompressed(paramCompoundTag, bufferedOutputStream);
/*  75 */         bufferedOutputStream.close(); } catch (Throwable throwable) { try { bufferedOutputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (outputStream != null) outputStream.close();  } catch (Throwable throwable) { if (outputStream != null)
/*     */         try { outputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  79 */      } public static void writeCompressed(CompoundTag paramCompoundTag, OutputStream paramOutputStream) throws IOException { DataOutputStream dataOutputStream = createCompressorStream(paramOutputStream); 
/*  80 */     try { write(paramCompoundTag, dataOutputStream);
/*  81 */       if (dataOutputStream != null) dataOutputStream.close();  }
/*     */     catch (Throwable throwable) { if (dataOutputStream != null)
/*     */         try { dataOutputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  86 */      } public static void write(CompoundTag paramCompoundTag, Path paramPath) throws IOException { OutputStream outputStream = Files.newOutputStream(paramPath, SYNC_OUTPUT_OPTIONS); 
/*  87 */     try { BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream); 
/*  88 */       try { DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
/*     */         
/*  90 */         try { write(paramCompoundTag, dataOutputStream);
/*  91 */           dataOutputStream.close(); } catch (Throwable throwable) { try { dataOutputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  bufferedOutputStream.close(); } catch (Throwable throwable) { try { bufferedOutputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (outputStream != null) outputStream.close();  } catch (Throwable throwable) { if (outputStream != null)
/*     */         try { outputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  95 */      } public static CompoundTag read(Path paramPath) throws IOException { if (!Files.exists(paramPath, new java.nio.file.LinkOption[0])) {
/*  96 */       return null;
/*     */     }
/*     */     
/*  99 */     InputStream inputStream = Files.newInputStream(paramPath, new OpenOption[0]); 
/* 100 */     try { DataInputStream dataInputStream = new DataInputStream(inputStream);
/*     */       
/* 102 */       try { CompoundTag compoundTag = read(dataInputStream, NbtAccounter.unlimitedHeap());
/* 103 */         dataInputStream.close(); if (inputStream != null) inputStream.close();  return compoundTag; } catch (Throwable throwable) { try { dataInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (inputStream != null)
/*     */         try { inputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 107 */      } public static CompoundTag read(DataInput paramDataInput) throws IOException { return read(paramDataInput, NbtAccounter.unlimitedHeap()); }
/*     */ 
/*     */   
/*     */   public static CompoundTag read(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException {
/* 111 */     Tag tag = readUnnamedTag(paramDataInput, paramNbtAccounter);
/* 112 */     if (tag instanceof CompoundTag) {
/* 113 */       return (CompoundTag)tag;
/*     */     }
/* 115 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */   
/*     */   public static void write(CompoundTag paramCompoundTag, DataOutput paramDataOutput) throws IOException {
/* 119 */     writeUnnamedTagWithFallback(paramCompoundTag, paramDataOutput);
/*     */   }
/*     */   
/*     */   public static void parse(DataInput paramDataInput, StreamTagVisitor paramStreamTagVisitor, NbtAccounter paramNbtAccounter) throws IOException {
/* 123 */     TagType<?> tagType = TagTypes.getType(paramDataInput.readByte());
/* 124 */     if (tagType == EndTag.TYPE) {
/* 125 */       if (paramStreamTagVisitor.visitRootEntry(EndTag.TYPE) == StreamTagVisitor.ValueResult.CONTINUE) {
/* 126 */         paramStreamTagVisitor.visitEnd();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 131 */     switch (paramStreamTagVisitor.visitRootEntry(tagType)) {
/*     */ 
/*     */       
/*     */       case BREAK:
/* 135 */         StringTag.skipString(paramDataInput);
/* 136 */         tagType.skip(paramDataInput, paramNbtAccounter);
/*     */         break;
/*     */       case CONTINUE:
/* 139 */         StringTag.skipString(paramDataInput);
/* 140 */         tagType.parse(paramDataInput, paramStreamTagVisitor, paramNbtAccounter);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Tag readAnyTag(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException {
/* 146 */     byte b = paramDataInput.readByte();
/* 147 */     if (b == 0) {
/* 148 */       return EndTag.INSTANCE;
/*     */     }
/* 150 */     return readTagSafe(paramDataInput, paramNbtAccounter, b);
/*     */   }
/*     */   
/*     */   public static void writeAnyTag(Tag paramTag, DataOutput paramDataOutput) throws IOException {
/* 154 */     paramDataOutput.writeByte(paramTag.getId());
/* 155 */     if (paramTag.getId() == 0) {
/*     */       return;
/*     */     }
/* 158 */     paramTag.write(paramDataOutput);
/*     */   }
/*     */   
/*     */   public static void writeUnnamedTag(Tag paramTag, DataOutput paramDataOutput) throws IOException {
/* 162 */     paramDataOutput.writeByte(paramTag.getId());
/* 163 */     if (paramTag.getId() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 168 */     paramDataOutput.writeUTF("");
/*     */     
/* 170 */     paramTag.write(paramDataOutput);
/*     */   }
/*     */   
/*     */   public static void writeUnnamedTagWithFallback(Tag paramTag, DataOutput paramDataOutput) throws IOException {
/* 174 */     writeUnnamedTag(paramTag, (DataOutput)new StringFallbackDataOutput(paramDataOutput));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static Tag readUnnamedTag(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException {
/* 179 */     byte b = paramDataInput.readByte();
/* 180 */     if (b == 0) {
/* 181 */       return EndTag.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/* 185 */     StringTag.skipString(paramDataInput);
/*     */     
/* 187 */     return readTagSafe(paramDataInput, paramNbtAccounter, b);
/*     */   }
/*     */   
/*     */   private static Tag readTagSafe(DataInput paramDataInput, NbtAccounter paramNbtAccounter, byte paramByte) {
/*     */     try {
/* 192 */       return (Tag)TagTypes.getType(paramByte).load(paramDataInput, paramNbtAccounter);
/* 193 */     } catch (IOException iOException) {
/* 194 */       CrashReport crashReport = CrashReport.forThrowable(iOException, "Loading NBT data");
/* 195 */       CrashReportCategory crashReportCategory = crashReport.addCategory("NBT Tag");
/* 196 */       crashReportCategory.setDetail("Tag type", Byte.valueOf(paramByte));
/* 197 */       throw new ReportedNbtException(crashReport);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class StringFallbackDataOutput extends DelegateDataOutput {
/*     */     public StringFallbackDataOutput(DataOutput param1DataOutput) {
/* 203 */       super(param1DataOutput);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeUTF(String param1String) throws IOException {
/*     */       try {
/* 209 */         super.writeUTF(param1String);
/* 210 */       } catch (UTFDataFormatException uTFDataFormatException) {
/* 211 */         Util.logAndPauseIfInIde("Failed to write NBT String", uTFDataFormatException);
/*     */         
/* 213 */         super.writeUTF("");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\NbtIo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */