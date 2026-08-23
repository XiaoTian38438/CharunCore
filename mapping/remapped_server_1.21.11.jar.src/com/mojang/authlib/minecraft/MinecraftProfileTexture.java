/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.io.FilenameUtils;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*    */ 
/*    */ public class MinecraftProfileTexture
/*    */ {
/*    */   public enum Type {
/* 14 */     SKIN,
/* 15 */     CAPE,
/* 16 */     ELYTRA;
/*    */   }
/*    */ 
/*    */   
/* 20 */   public static final int PROFILE_TEXTURE_COUNT = (Type.values()).length;
/*    */   
/*    */   @SerializedName("url")
/*    */   private final String url;
/*    */   @SerializedName("metadata")
/*    */   private final Map<String, String> metadata;
/*    */   
/*    */   public MinecraftProfileTexture(String paramString, Map<String, String> paramMap) {
/* 28 */     this.url = paramString;
/* 29 */     this.metadata = paramMap;
/*    */   }
/*    */   
/*    */   public String getUrl() {
/* 33 */     return this.url;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getMetadata(String paramString) {
/* 38 */     if (this.metadata == null) {
/* 39 */       return null;
/*    */     }
/* 41 */     return this.metadata.get(paramString);
/*    */   }
/*    */   
/*    */   public String getHash() {
/*    */     try {
/* 46 */       return FilenameUtils.getBaseName((new URL(this.url)).getPath());
/* 47 */     } catch (MalformedURLException malformedURLException) {
/* 48 */       throw new IllegalArgumentException("Invalid profile texture url");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return (new ToStringBuilder(this))
/* 55 */       .append("url", this.url)
/* 56 */       .append("hash", getHash())
/* 57 */       .toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\MinecraftProfileTexture.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */