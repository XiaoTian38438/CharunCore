/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import net.jpountz.lz4.LZ4BlockInputStream;
/*     */ import net.jpountz.lz4.LZ4BlockOutputStream;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class RegionFileVersion
/*     */ {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  25 */   private static final Int2ObjectMap<RegionFileVersion> VERSIONS = (Int2ObjectMap<RegionFileVersion>)new Int2ObjectOpenHashMap();
/*  26 */   private static final Object2ObjectMap<String, RegionFileVersion> VERSIONS_BY_NAME = (Object2ObjectMap<String, RegionFileVersion>)new Object2ObjectOpenHashMap();
/*     */   static {
/*  28 */     VERSION_GZIP = register(new RegionFileVersion(1, null, paramInputStream -> new FastBufferedInputStream(new GZIPInputStream(paramInputStream)), paramOutputStream -> new BufferedOutputStream(new GZIPOutputStream(paramOutputStream))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  34 */     VERSION_DEFLATE = register(new RegionFileVersion(2, "deflate", paramInputStream -> new FastBufferedInputStream(new InflaterInputStream(paramInputStream)), paramOutputStream -> new BufferedOutputStream(new DeflaterOutputStream(paramOutputStream))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final RegionFileVersion VERSION_GZIP;
/*     */   public static final RegionFileVersion VERSION_DEFLATE;
/*  40 */   public static final RegionFileVersion VERSION_NONE = register(new RegionFileVersion(3, "none", FastBufferedInputStream::new, BufferedOutputStream::new));
/*     */   
/*     */   public static final RegionFileVersion VERSION_LZ4;
/*     */ 
/*     */   
/*     */   static {
/*  46 */     VERSION_LZ4 = register(new RegionFileVersion(4, "lz4", paramInputStream -> new FastBufferedInputStream((InputStream)new LZ4BlockInputStream(paramInputStream)), paramOutputStream -> new BufferedOutputStream((OutputStream)new LZ4BlockOutputStream(paramOutputStream))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final RegionFileVersion VERSION_CUSTOM = register(new RegionFileVersion(127, null, paramInputStream -> {
/*     */           throw new UnsupportedOperationException();
/*     */         }paramOutputStream -> {
/*     */           throw new UnsupportedOperationException();
/*     */         }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final RegionFileVersion DEFAULT = VERSION_DEFLATE;
/*  63 */   private static volatile RegionFileVersion selected = DEFAULT;
/*     */   
/*     */   private final int id;
/*     */   private final String optionName;
/*     */   private final StreamWrapper<InputStream> inputWrapper;
/*     */   private final StreamWrapper<OutputStream> outputWrapper;
/*     */   
/*     */   private RegionFileVersion(int paramInt, String paramString, StreamWrapper<InputStream> paramStreamWrapper, StreamWrapper<OutputStream> paramStreamWrapper1) {
/*  71 */     this.id = paramInt;
/*  72 */     this.optionName = paramString;
/*  73 */     this.inputWrapper = paramStreamWrapper;
/*  74 */     this.outputWrapper = paramStreamWrapper1;
/*     */   }
/*     */   
/*     */   private static RegionFileVersion register(RegionFileVersion paramRegionFileVersion) {
/*  78 */     VERSIONS.put(paramRegionFileVersion.id, paramRegionFileVersion);
/*  79 */     if (paramRegionFileVersion.optionName != null) {
/*  80 */       VERSIONS_BY_NAME.put(paramRegionFileVersion.optionName, paramRegionFileVersion);
/*     */     }
/*  82 */     return paramRegionFileVersion;
/*     */   }
/*     */   
/*     */   public static RegionFileVersion fromId(int paramInt) {
/*  86 */     return (RegionFileVersion)VERSIONS.get(paramInt);
/*     */   }
/*     */   
/*     */   public static void configure(String paramString) {
/*  90 */     RegionFileVersion regionFileVersion = (RegionFileVersion)VERSIONS_BY_NAME.get(paramString);
/*  91 */     if (regionFileVersion != null) {
/*  92 */       selected = regionFileVersion;
/*     */     } else {
/*  94 */       LOGGER.error("Invalid `region-file-compression` value `{}` in server.properties. Please use one of: {}", paramString, String.join(", ", (Iterable<? extends CharSequence>)VERSIONS_BY_NAME.keySet()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static RegionFileVersion getSelected() {
/*  99 */     return selected;
/*     */   }
/*     */   
/*     */   public static boolean isValidVersion(int paramInt) {
/* 103 */     return VERSIONS.containsKey(paramInt);
/*     */   }
/*     */   
/*     */   public int getId() {
/* 107 */     return this.id;
/*     */   }
/*     */   
/*     */   public OutputStream wrap(OutputStream paramOutputStream) throws IOException {
/* 111 */     return this.outputWrapper.wrap(paramOutputStream);
/*     */   }
/*     */   
/*     */   public InputStream wrap(InputStream paramInputStream) throws IOException {
/* 115 */     return this.inputWrapper.wrap(paramInputStream);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface StreamWrapper<O> {
/*     */     O wrap(O param1O) throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\RegionFileVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */