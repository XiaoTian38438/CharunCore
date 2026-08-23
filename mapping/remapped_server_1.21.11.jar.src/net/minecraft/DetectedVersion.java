/*    */ package net.minecraft;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.Date;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.DataVersion;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class DetectedVersion
/*    */ {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 22 */   public static final WorldVersion BUILT_IN = createBuiltIn(
/* 23 */       UUID.randomUUID().toString().replaceAll("-", ""), "Development Version");
/*    */ 
/*    */ 
/*    */   
/*    */   public static WorldVersion createBuiltIn(String paramString1, String paramString2) {
/* 28 */     return createBuiltIn(paramString1, paramString2, true);
/*    */   }
/*    */   
/*    */   public static WorldVersion createBuiltIn(String paramString1, String paramString2, boolean paramBoolean) {
/* 32 */     return new WorldVersion.Simple(paramString1, paramString2, new DataVersion(4671, "main"), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 39 */         SharedConstants.getProtocolVersion(), 
/* 40 */         PackFormat.of(75, 0), 
/*    */ 
/*    */ 
/*    */         
/* 44 */         PackFormat.of(94, 1), new Date(), paramBoolean);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static WorldVersion createFromJson(JsonObject paramJsonObject) {
/* 54 */     JsonObject jsonObject = GsonHelper.getAsJsonObject(paramJsonObject, "pack_version");
/*    */     
/* 56 */     return new WorldVersion.Simple(
/* 57 */         GsonHelper.getAsString(paramJsonObject, "id"), 
/* 58 */         GsonHelper.getAsString(paramJsonObject, "name"), new DataVersion(
/*    */           
/* 60 */           GsonHelper.getAsInt(paramJsonObject, "world_version"), 
/* 61 */           GsonHelper.getAsString(paramJsonObject, "series_id", "main")), 
/*    */         
/* 63 */         GsonHelper.getAsInt(paramJsonObject, "protocol_version"), 
/* 64 */         PackFormat.of(
/* 65 */           GsonHelper.getAsInt(jsonObject, "resource_major"), 
/* 66 */           GsonHelper.getAsInt(jsonObject, "resource_minor")), 
/*    */         
/* 68 */         PackFormat.of(
/* 69 */           GsonHelper.getAsInt(jsonObject, "data_major"), 
/* 70 */           GsonHelper.getAsInt(jsonObject, "data_minor")), 
/*    */         
/* 72 */         Date.from(ZonedDateTime.parse(GsonHelper.getAsString(paramJsonObject, "build_time")).toInstant()), 
/* 73 */         GsonHelper.getAsBoolean(paramJsonObject, "stable"));
/*    */   }
/*    */   
/*    */   public static WorldVersion tryDetectVersion() {
/*    */     
/* 78 */     try { InputStream inputStream = DetectedVersion.class.getResourceAsStream("/version.json"); 
/* 79 */       try { if (inputStream == null)
/* 80 */         { LOGGER.warn("Missing version information!");
/* 81 */           WorldVersion worldVersion = BUILT_IN;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 87 */           if (inputStream != null) inputStream.close();  return worldVersion; }  InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); try { WorldVersion worldVersion = createFromJson(GsonHelper.parse(inputStreamReader)); inputStreamReader.close(); if (inputStream != null) inputStream.close();  return worldVersion; } catch (Throwable throwable) { try { inputStreamReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonParseException iOException)
/* 88 */     { throw new IllegalStateException("Game version information is corrupt", iOException); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\DetectedVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */