/*     */ package com.mojang.authlib.minecraft.client;
/*     */ 
/*     */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*     */ import com.mojang.authlib.exceptions.MinecraftClientHttpException;
/*     */ import com.mojang.authlib.yggdrasil.response.ErrorResponse;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinecraftClient
/*     */ {
/*  29 */   private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftClient.class);
/*     */   
/*     */   public static final int CONNECT_TIMEOUT_MS = 5000;
/*     */   public static final int READ_TIMEOUT_MS = 5000;
/*     */   @Nullable
/*     */   private final String accessToken;
/*     */   private final Proxy proxy;
/*  36 */   private final ObjectMapper objectMapper = ObjectMapper.create();
/*     */   
/*     */   public MinecraftClient(@Nullable String paramString, Proxy paramProxy) {
/*  39 */     this.accessToken = paramString;
/*  40 */     this.proxy = (Proxy)Validate.notNull(paramProxy);
/*     */   }
/*     */   
/*     */   public static MinecraftClient unauthenticated(Proxy paramProxy) {
/*  44 */     return new MinecraftClient(null, paramProxy);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public <T> T get(URL paramURL, Class<T> paramClass) {
/*  49 */     Validate.notNull(paramURL);
/*  50 */     Validate.notNull(paramClass);
/*  51 */     HttpURLConnection httpURLConnection = createUrlConnection(paramURL);
/*  52 */     if (this.accessToken != null) {
/*  53 */       httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.accessToken);
/*     */     }
/*     */     
/*  56 */     return readInputStream(paramURL, paramClass, httpURLConnection);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public <T> T post(URL paramURL, Class<T> paramClass) {
/*  61 */     Validate.notNull(paramURL);
/*  62 */     Validate.notNull(paramClass);
/*  63 */     HttpURLConnection httpURLConnection = postInternal(paramURL, new byte[0]);
/*  64 */     return readInputStream(paramURL, paramClass, httpURLConnection);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public <T> T post(URL paramURL, Object paramObject, Class<T> paramClass) {
/*  69 */     Validate.notNull(paramURL);
/*  70 */     Validate.notNull(paramObject);
/*  71 */     Validate.notNull(paramClass);
/*  72 */     String str = this.objectMapper.writeValueAsString(paramObject);
/*  73 */     byte[] arrayOfByte = str.getBytes(StandardCharsets.UTF_8);
/*  74 */     HttpURLConnection httpURLConnection = postInternal(paramURL, arrayOfByte);
/*  75 */     return readInputStream(paramURL, paramClass, httpURLConnection);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private <T> T readInputStream(URL paramURL, Class<T> paramClass, HttpURLConnection paramHttpURLConnection) {
/*  80 */     InputStream inputStream = null;
/*     */     try {
/*  82 */       int i = paramHttpURLConnection.getResponseCode();
/*     */ 
/*     */       
/*  85 */       if (i < 400) {
/*  86 */         inputStream = paramHttpURLConnection.getInputStream();
/*  87 */         String str1 = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
/*  88 */         if (str1.isEmpty()) {
/*  89 */           return null;
/*     */         }
/*  91 */         return (T)this.objectMapper.readValue(str1, (Class)paramClass);
/*     */       } 
/*  93 */       String str = paramHttpURLConnection.getContentType();
/*  94 */       inputStream = paramHttpURLConnection.getErrorStream();
/*     */       
/*  96 */       if (inputStream != null) {
/*  97 */         String str1 = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
/*  98 */         if (str != null && str.startsWith("text/html")) {
/*  99 */           LOGGER.error("Got an error with a html body connecting to {}: {}", paramURL.toString(), str1);
/* 100 */           throw new MinecraftClientHttpException(i);
/*     */         } 
/* 102 */         ErrorResponse errorResponse = this.objectMapper.<ErrorResponse>readValue(str1, ErrorResponse.class);
/* 103 */         throw new MinecraftClientHttpException(i, errorResponse);
/*     */       } 
/* 105 */       throw new MinecraftClientHttpException(i);
/*     */     
/*     */     }
/* 108 */     catch (IOException iOException) {
/*     */       
/* 110 */       throw new MinecraftClientException(MinecraftClientException.ErrorType.SERVICE_UNAVAILABLE, "Failed to read from " + String.valueOf(paramURL) + " due to " + iOException
/* 111 */           .getMessage(), iOException);
/*     */     } finally {
/* 113 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private HttpURLConnection postInternal(URL paramURL, byte[] paramArrayOfbyte) {
/* 119 */     HttpURLConnection httpURLConnection = createUrlConnection(paramURL);
/* 120 */     OutputStream outputStream = null;
/*     */     try {
/* 122 */       httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
/* 123 */       httpURLConnection.setRequestProperty("Content-Length", "" + paramArrayOfbyte.length);
/* 124 */       if (this.accessToken != null) {
/* 125 */         httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.accessToken);
/*     */       }
/* 127 */       httpURLConnection.setRequestMethod("POST");
/* 128 */       httpURLConnection.setDoOutput(true);
/* 129 */       outputStream = httpURLConnection.getOutputStream();
/* 130 */       IOUtils.write(paramArrayOfbyte, outputStream);
/* 131 */     } catch (IOException iOException) {
/* 132 */       throw new MinecraftClientException(MinecraftClientException.ErrorType.SERVICE_UNAVAILABLE, "Failed to POST " + String.valueOf(paramURL), iOException);
/*     */     } finally {
/* 134 */       IOUtils.closeQuietly(outputStream);
/*     */     } 
/* 136 */     return httpURLConnection;
/*     */   }
/*     */ 
/*     */   
/*     */   private HttpURLConnection createUrlConnection(URL paramURL) {
/*     */     try {
/* 142 */       LOGGER.debug("Connecting to {}", paramURL);
/* 143 */       HttpURLConnection httpURLConnection = (HttpURLConnection)paramURL.openConnection(this.proxy);
/* 144 */       httpURLConnection.setConnectTimeout(5000);
/* 145 */       httpURLConnection.setReadTimeout(5000);
/* 146 */       httpURLConnection.setUseCaches(false);
/* 147 */       return httpURLConnection;
/* 148 */     } catch (IOException iOException) {
/* 149 */       throw new MinecraftClientException(MinecraftClientException.ErrorType.SERVICE_UNAVAILABLE, "Failed connecting to " + String.valueOf(paramURL), iOException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\client\MinecraftClient.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */