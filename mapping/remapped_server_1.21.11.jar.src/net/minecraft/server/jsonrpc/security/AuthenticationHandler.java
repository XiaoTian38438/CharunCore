/*     */ package net.minecraft.server.jsonrpc.security;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @Sharable
/*     */ public class AuthenticationHandler
/*     */   extends ChannelDuplexHandler {
/*  27 */   private final Logger LOGGER = LogUtils.getLogger();
/*  28 */   private static final AttributeKey<Boolean> AUTHENTICATED_KEY = AttributeKey.valueOf("authenticated");
/*  29 */   private static final AttributeKey<Boolean> ATTR_WEBSOCKET_ALLOWED = AttributeKey.valueOf("websocket_auth_allowed");
/*     */   private static final String SUBPROTOCOL_VALUE = "minecraft-v1";
/*     */   private static final String SUBPROTOCOL_HEADER_PREFIX = "minecraft-v1,";
/*     */   public static final String BEARER_PREFIX = "Bearer ";
/*     */   private final SecurityConfig securityConfig;
/*     */   private final Set<String> allowedOrigins;
/*     */   
/*     */   public AuthenticationHandler(SecurityConfig paramSecurityConfig, String paramString) {
/*  37 */     this.securityConfig = paramSecurityConfig;
/*  38 */     this.allowedOrigins = Sets.newHashSet((Object[])paramString.split(","));
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) throws Exception {
/*  43 */     String str = getClientIp(paramChannelHandlerContext);
/*  44 */     if (paramObject instanceof HttpRequest) { HttpRequest httpRequest = (HttpRequest)paramObject;
/*  45 */       SecurityCheckResult securityCheckResult = performSecurityChecks(httpRequest);
/*     */       
/*  47 */       if (securityCheckResult.isAllowed()) {
/*  48 */         paramChannelHandlerContext.channel().attr(AUTHENTICATED_KEY).set(Boolean.valueOf(true));
/*  49 */         if (securityCheckResult.isTokenSentInSecWebsocketProtocol()) {
/*  50 */           paramChannelHandlerContext.channel().attr(ATTR_WEBSOCKET_ALLOWED).set(Boolean.TRUE);
/*     */         }
/*     */       } else {
/*  53 */         this.LOGGER.debug("Authentication rejected for connection with ip {}: {}", str, securityCheckResult.getReason());
/*  54 */         paramChannelHandlerContext.channel().attr(AUTHENTICATED_KEY).set(Boolean.valueOf(false));
/*  55 */         sendUnauthorizedResponse(paramChannelHandlerContext, securityCheckResult.getReason());
/*     */         
/*     */         return;
/*     */       }  }
/*     */     
/*  60 */     Boolean bool = (Boolean)paramChannelHandlerContext.channel().attr(AUTHENTICATED_KEY).get();
/*     */     
/*  62 */     if (Boolean.TRUE.equals(bool)) {
/*  63 */       super.channelRead(paramChannelHandlerContext, paramObject);
/*     */     } else {
/*  65 */       this.LOGGER.debug("Dropping unauthenticated connection with ip {}", str);
/*  66 */       paramChannelHandlerContext.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, ChannelPromise paramChannelPromise) throws Exception {
/*  72 */     if (paramObject instanceof HttpResponse) { HttpResponse httpResponse = (HttpResponse)paramObject; if (httpResponse.status().code() == HttpResponseStatus.SWITCHING_PROTOCOLS.code() && 
/*  73 */         paramChannelHandlerContext.channel().attr(ATTR_WEBSOCKET_ALLOWED).get() != null && ((Boolean)paramChannelHandlerContext.channel().attr(ATTR_WEBSOCKET_ALLOWED).get()).equals(Boolean.TRUE)) {
/*  74 */         httpResponse.headers().set((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, "minecraft-v1");
/*     */       } }
/*     */     
/*  77 */     super.write(paramChannelHandlerContext, paramObject, paramChannelPromise);
/*     */   }
/*     */   
/*     */   private SecurityCheckResult performSecurityChecks(HttpRequest paramHttpRequest) {
/*  81 */     String str1 = parseTokenInAuthorizationHeader(paramHttpRequest);
/*  82 */     if (str1 != null) {
/*  83 */       if (isValidApiKey(str1)) {
/*  84 */         return SecurityCheckResult.allowed();
/*     */       }
/*  86 */       return SecurityCheckResult.denied("Invalid API key");
/*     */     } 
/*     */     
/*  89 */     String str2 = parseTokenInSecWebsocketProtocolHeader(paramHttpRequest);
/*  90 */     if (str2 != null) {
/*  91 */       if (!isAllowedOriginHeader(paramHttpRequest)) {
/*  92 */         return SecurityCheckResult.denied("Origin Not Allowed");
/*     */       }
/*     */       
/*  95 */       if (isValidApiKey(str2)) {
/*  96 */         return SecurityCheckResult.allowed(true);
/*     */       }
/*  98 */       return SecurityCheckResult.denied("Invalid API key");
/*     */     } 
/*     */ 
/*     */     
/* 102 */     return SecurityCheckResult.denied("Missing API key");
/*     */   }
/*     */   
/*     */   private boolean isAllowedOriginHeader(HttpRequest paramHttpRequest) {
/* 106 */     String str = paramHttpRequest.headers().get((CharSequence)HttpHeaderNames.ORIGIN);
/*     */     
/* 108 */     if (str == null || str.isEmpty()) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     return this.allowedOrigins.contains(str);
/*     */   }
/*     */   
/*     */   private String parseTokenInAuthorizationHeader(HttpRequest paramHttpRequest) {
/* 116 */     String str = paramHttpRequest.headers().get((CharSequence)HttpHeaderNames.AUTHORIZATION);
/*     */     
/* 118 */     if (str != null && str.startsWith("Bearer ")) {
/* 119 */       return str.substring("Bearer ".length()).trim();
/*     */     }
/*     */     
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   private String parseTokenInSecWebsocketProtocolHeader(HttpRequest paramHttpRequest) {
/* 126 */     String str = paramHttpRequest.headers().get((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/*     */     
/* 128 */     if (str != null && str.startsWith("minecraft-v1,")) {
/* 129 */       return str.substring("minecraft-v1,".length()).trim();
/*     */     }
/*     */     
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isValidApiKey(String paramString) {
/* 136 */     if (paramString.isEmpty()) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     byte[] arrayOfByte1 = paramString.getBytes(StandardCharsets.UTF_8);
/* 141 */     byte[] arrayOfByte2 = this.securityConfig.secretKey().getBytes(StandardCharsets.UTF_8);
/*     */ 
/*     */     
/* 144 */     return MessageDigest.isEqual(arrayOfByte1, arrayOfByte2);
/*     */   }
/*     */   
/*     */   private String getClientIp(ChannelHandlerContext paramChannelHandlerContext) {
/* 148 */     InetSocketAddress inetSocketAddress = (InetSocketAddress)paramChannelHandlerContext.channel().remoteAddress();
/* 149 */     return inetSocketAddress.getAddress().getHostAddress();
/*     */   }
/*     */   
/*     */   private void sendUnauthorizedResponse(ChannelHandlerContext paramChannelHandlerContext, String paramString) {
/* 153 */     String str = "{\"error\":\"Unauthorized\",\"message\":\"" + paramString + "\"}";
/* 154 */     byte[] arrayOfByte = str.getBytes(StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED, Unpooled.wrappedBuffer(arrayOfByte));
/*     */ 
/*     */     
/* 162 */     defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONTENT_TYPE, "application/json");
/* 163 */     defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(arrayOfByte.length));
/* 164 */     defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.CONNECTION, "close");
/*     */     
/* 166 */     paramChannelHandlerContext.writeAndFlush(defaultFullHttpResponse).addListener(paramFuture -> paramChannelHandlerContext.close());
/*     */   }
/*     */   
/*     */   private static class SecurityCheckResult {
/*     */     private final boolean allowed;
/*     */     private final String reason;
/*     */     private final boolean tokenSentInSecWebsocketProtocol;
/*     */     
/*     */     private SecurityCheckResult(boolean param1Boolean1, String param1String, boolean param1Boolean2) {
/* 175 */       this.allowed = param1Boolean1;
/* 176 */       this.reason = param1String;
/* 177 */       this.tokenSentInSecWebsocketProtocol = param1Boolean2;
/*     */     }
/*     */     
/*     */     public static SecurityCheckResult allowed() {
/* 181 */       return new SecurityCheckResult(true, null, false);
/*     */     }
/*     */     
/*     */     public static SecurityCheckResult allowed(boolean param1Boolean) {
/* 185 */       return new SecurityCheckResult(true, null, param1Boolean);
/*     */     }
/*     */     
/*     */     public static SecurityCheckResult denied(String param1String) {
/* 189 */       return new SecurityCheckResult(false, param1String, false);
/*     */     }
/*     */     
/* 192 */     public boolean isAllowed() { return this.allowed; }
/* 193 */     public String getReason() { return this.reason; } public boolean isTokenSentInSecWebsocketProtocol() {
/* 194 */       return this.tokenSentInSecWebsocketProtocol;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\security\AuthenticationHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */