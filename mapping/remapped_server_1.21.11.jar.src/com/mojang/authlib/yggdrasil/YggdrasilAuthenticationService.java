/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.mojang.authlib.Environment;
/*    */ import com.mojang.authlib.EnvironmentParser;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.minecraft.UserApiService;
/*    */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ import javax.annotation.Nullable;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class YggdrasilAuthenticationService
/*    */   extends HttpAuthenticationService
/*    */ {
/* 19 */   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilAuthenticationService.class);
/*    */   
/*    */   private final Environment environment;
/*    */   private final ServicesKeySet servicesKeySet;
/*    */   
/*    */   public YggdrasilAuthenticationService(Proxy paramProxy) {
/* 25 */     this(paramProxy, null, determineEnvironment());
/*    */   }
/*    */   
/*    */   public YggdrasilAuthenticationService(Proxy paramProxy, Environment paramEnvironment) {
/* 29 */     this(paramProxy, null, paramEnvironment);
/*    */   }
/*    */   
/*    */   private YggdrasilAuthenticationService(Proxy paramProxy, @Nullable ServicesKeySet paramServicesKeySet, Environment paramEnvironment) {
/* 33 */     super(paramProxy);
/* 34 */     this.environment = paramEnvironment;
/* 35 */     LOGGER.info("Environment: {}", paramEnvironment);
/*    */     
/* 37 */     MinecraftClient minecraftClient = MinecraftClient.unauthenticated(paramProxy);
/* 38 */     URL uRL = HttpAuthenticationService.constantURL(paramEnvironment.servicesHost() + "/publickeys");
/* 39 */     this.servicesKeySet = (paramServicesKeySet != null) ? paramServicesKeySet : YggdrasilServicesKeyInfo.get(uRL, minecraftClient);
/*    */   }
/*    */   
/*    */   public static YggdrasilAuthenticationService createOffline(Proxy paramProxy) {
/* 43 */     return new YggdrasilAuthenticationService(paramProxy, ServicesKeySet.EMPTY, determineEnvironment());
/*    */   }
/*    */   
/*    */   public static YggdrasilAuthenticationService createOffline(Proxy paramProxy, Environment paramEnvironment) {
/* 47 */     return new YggdrasilAuthenticationService(paramProxy, ServicesKeySet.EMPTY, paramEnvironment);
/*    */   }
/*    */   
/*    */   private static Environment determineEnvironment() {
/* 51 */     return 
/* 52 */       EnvironmentParser.getEnvironmentFromProperties()
/* 53 */       .orElse(YggdrasilEnvironment.PROD.getEnvironment());
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecraftSessionService createMinecraftSessionService() {
/* 58 */     return new YggdrasilMinecraftSessionService(this.servicesKeySet, getProxy(), this.environment);
/*    */   }
/*    */ 
/*    */   
/*    */   public GameProfileRepository createProfileRepository() {
/* 63 */     return new YggdrasilGameProfileRepository(getProxy(), this.environment);
/*    */   }
/*    */   
/*    */   public UserApiService createUserApiService(String paramString) {
/* 67 */     return new YggdrasilUserApiService(paramString, getProxy(), this.environment);
/*    */   }
/*    */   
/*    */   public ServicesKeySet getServicesKeySet() {
/* 71 */     return this.servicesKeySet;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilAuthenticationService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */