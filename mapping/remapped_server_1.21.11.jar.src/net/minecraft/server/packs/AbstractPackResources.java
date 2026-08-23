/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class AbstractPackResources implements PackResources {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final PackLocationInfo location;
/*    */   
/*    */   protected AbstractPackResources(PackLocationInfo paramPackLocationInfo) {
/* 23 */     this.location = paramPackLocationInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getMetadataSection(MetadataSectionType<T> paramMetadataSectionType) throws IOException {
/* 28 */     IoSupplier<InputStream> ioSupplier = getRootResource(new String[] { "pack.mcmeta" });
/* 29 */     if (ioSupplier == null) {
/* 30 */       return null;
/*    */     }
/* 32 */     InputStream inputStream = (InputStream)ioSupplier.get(); 
/* 33 */     try { T t = (T)getMetadataFromStream((MetadataSectionType)paramMetadataSectionType, inputStream, this.location);
/* 34 */       if (inputStream != null) inputStream.close();  return t; }
/*    */     catch (Throwable throwable) { if (inputStream != null)
/*    */         try { inputStream.close(); }
/*    */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 39 */      } public static <T> T getMetadataFromStream(MetadataSectionType<T> paramMetadataSectionType, InputStream paramInputStream, PackLocationInfo paramPackLocationInfo) { JsonObject jsonObject; try { BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, StandardCharsets.UTF_8)); 
/* 40 */       try { jsonObject = GsonHelper.parse(bufferedReader);
/* 41 */         bufferedReader.close(); } catch (Throwable throwable) { try { bufferedReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception exception)
/* 42 */     { LOGGER.error("Couldn't load {} {} metadata: {}", new Object[] { paramPackLocationInfo.id(), paramMetadataSectionType.name(), exception.getMessage() });
/* 43 */       return null; }
/*    */ 
/*    */     
/* 46 */     if (!jsonObject.has(paramMetadataSectionType.name())) {
/* 47 */       return null;
/*    */     }
/*    */     
/* 50 */     return paramMetadataSectionType.codec().parse((DynamicOps)JsonOps.INSTANCE, jsonObject.get(paramMetadataSectionType.name()))
/* 51 */       .ifError(paramError -> LOGGER.error("Couldn't load {} {} metadata: {}", new Object[] { paramPackLocationInfo.id(), paramMetadataSectionType.name(), paramError.message()
/* 52 */           })).result().orElse(null); }
/*    */ 
/*    */ 
/*    */   
/*    */   public PackLocationInfo location() {
/* 57 */     return this.location;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\AbstractPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */