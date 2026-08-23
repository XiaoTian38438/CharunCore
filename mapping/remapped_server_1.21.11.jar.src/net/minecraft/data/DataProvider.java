/*    */ package net.minecraft.data;
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.hash.HashingOutputStream;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface DataProvider {
/*    */   public static final ToIntFunction<String> FIXED_ORDER_FIELDS;
/*    */   public static final Comparator<String> KEY_COMPARATOR;
/*    */   
/*    */   static {
/* 31 */     FIXED_ORDER_FIELDS = (ToIntFunction<String>)Util.make(new Object2IntOpenHashMap(), paramObject2IntOpenHashMap -> {
/*    */           paramObject2IntOpenHashMap.put("type", 0);
/*    */           paramObject2IntOpenHashMap.put("parent", 1);
/*    */           paramObject2IntOpenHashMap.defaultReturnValue(2);
/*    */         });
/* 36 */     KEY_COMPARATOR = Comparator.<String>comparingInt(FIXED_ORDER_FIELDS).thenComparing(paramString -> paramString);
/*    */   }
/* 38 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> CompletableFuture<?> saveAll(CachedOutput paramCachedOutput, Codec<T> paramCodec, PackOutput.PathProvider paramPathProvider, Map<Identifier, T> paramMap) {
/* 45 */     Objects.requireNonNull(paramPathProvider); return saveAll(paramCachedOutput, paramCodec, paramPathProvider::json, paramMap);
/*    */   }
/*    */   
/*    */   static <T, E> CompletableFuture<?> saveAll(CachedOutput paramCachedOutput, Codec<E> paramCodec, Function<T, Path> paramFunction, Map<T, E> paramMap) {
/* 49 */     return saveAll(paramCachedOutput, paramObject -> (JsonElement)paramCodec.encodeStart((DynamicOps)JsonOps.INSTANCE, paramObject).getOrThrow(), paramFunction, paramMap);
/*    */   }
/*    */   
/*    */   static <T, E> CompletableFuture<?> saveAll(CachedOutput paramCachedOutput, Function<E, JsonElement> paramFunction, Function<T, Path> paramFunction1, Map<T, E> paramMap) {
/* 53 */     return CompletableFuture.allOf((CompletableFuture<?>[])paramMap.entrySet().stream()
/* 54 */         .map(paramEntry -> {
/*    */             Path path = (Path)paramFunction1.apply(paramEntry.getKey());
/*    */             
/*    */             JsonElement jsonElement = (JsonElement)paramFunction2.apply(paramEntry.getValue());
/*    */             return saveStable(paramCachedOutput, jsonElement, path);
/* 59 */           }).toArray(paramInt -> new CompletableFuture[paramInt]));
/*    */   }
/*    */   
/*    */   static <T> CompletableFuture<?> saveStable(CachedOutput paramCachedOutput, HolderLookup.Provider paramProvider, Codec<T> paramCodec, T paramT, Path paramPath) {
/* 63 */     RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/* 64 */     return saveStable(paramCachedOutput, (DynamicOps<JsonElement>)registryOps, paramCodec, paramT, paramPath);
/*    */   }
/*    */   
/*    */   static <T> CompletableFuture<?> saveStable(CachedOutput paramCachedOutput, Codec<T> paramCodec, T paramT, Path paramPath) {
/* 68 */     return saveStable(paramCachedOutput, (DynamicOps<JsonElement>)JsonOps.INSTANCE, paramCodec, paramT, paramPath);
/*    */   }
/*    */   
/*    */   private static <T> CompletableFuture<?> saveStable(CachedOutput paramCachedOutput, DynamicOps<JsonElement> paramDynamicOps, Codec<T> paramCodec, T paramT, Path paramPath) {
/* 72 */     JsonElement jsonElement = (JsonElement)paramCodec.encodeStart(paramDynamicOps, paramT).getOrThrow();
/* 73 */     return saveStable(paramCachedOutput, jsonElement, paramPath);
/*    */   }
/*    */   
/*    */   static CompletableFuture<?> saveStable(CachedOutput paramCachedOutput, JsonElement paramJsonElement, Path paramPath) {
/* 77 */     return CompletableFuture.runAsync(() -> {
/*    */           try {
/*    */             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); HashingOutputStream hashingOutputStream = new HashingOutputStream(Hashing.sha1(), byteArrayOutputStream); JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter((OutputStream)hashingOutputStream, StandardCharsets.UTF_8)); 
/*    */             try { jsonWriter.setSerializeNulls(false); jsonWriter.setIndent("  "); GsonHelper.writeValue(jsonWriter, paramJsonElement, KEY_COMPARATOR); jsonWriter.close(); }
/* 81 */             catch (Throwable throwable) { try { jsonWriter.close(); } catch (Throwable throwable1)
/*    */               { throwable.addSuppressed(throwable1); }
/*    */               
/*    */               throw throwable; }
/*    */             
/*    */             paramCachedOutput.writeIfNeeded(paramPath, byteArrayOutputStream.toByteArray(), hashingOutputStream.hash());
/* 87 */           } catch (IOException iOException) {
/*    */             LOGGER.error("Failed to save file to {}", paramPath, iOException);
/*    */           } 
/* 90 */         }Util.backgroundExecutor().forName("saveStable"));
/*    */   }
/*    */   
/*    */   CompletableFuture<?> run(CachedOutput paramCachedOutput);
/*    */   
/*    */   String getName();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends DataProvider> {
/*    */     T create(PackOutput param1PackOutput);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\DataProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */