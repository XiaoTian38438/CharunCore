/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ import java.net.URLEncoder;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public abstract class HttpAuthenticationService
/*    */   implements AuthenticationService {
/*    */   private final Proxy proxy;
/*    */   
/*    */   protected HttpAuthenticationService(Proxy paramProxy) {
/* 16 */     Validate.notNull(paramProxy);
/* 17 */     this.proxy = paramProxy;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Proxy getProxy() {
/* 26 */     return this.proxy;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL constantURL(String paramString) {
/*    */     try {
/* 39 */       return new URL(paramString);
/* 40 */     } catch (MalformedURLException malformedURLException) {
/* 41 */       throw new Error("Couldn't create constant for " + paramString, malformedURLException);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String buildQuery(Map<String, Object> paramMap) {
/* 52 */     if (paramMap == null) {
/* 53 */       return "";
/*    */     }
/* 55 */     StringBuilder stringBuilder = new StringBuilder();
/*    */     
/* 57 */     for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
/* 58 */       if (stringBuilder.length() > 0) {
/* 59 */         stringBuilder.append('&');
/*    */       }
/*    */       
/* 62 */       stringBuilder.append(URLEncoder.encode((String)entry.getKey(), StandardCharsets.UTF_8));
/*    */       
/* 64 */       if (entry.getValue() != null) {
/* 65 */         stringBuilder.append('=');
/* 66 */         stringBuilder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     return stringBuilder.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL concatenateURL(URL paramURL, String paramString) {
/*    */     try {
/* 82 */       if (paramURL.getQuery() != null && paramURL.getQuery().length() > 0) {
/* 83 */         return new URL(paramURL.getProtocol(), paramURL.getHost(), paramURL.getPort(), paramURL.getFile() + "&" + paramURL.getFile());
/*    */       }
/* 85 */       return new URL(paramURL.getProtocol(), paramURL.getHost(), paramURL.getPort(), paramURL.getFile() + "?" + paramURL.getFile());
/*    */     }
/* 87 */     catch (MalformedURLException malformedURLException) {
/* 88 */       throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", malformedURLException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\HttpAuthenticationService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */