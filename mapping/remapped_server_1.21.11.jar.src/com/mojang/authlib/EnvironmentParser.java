/*    */ package com.mojang.authlib;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import javax.annotation.Nullable;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ public class EnvironmentParser
/*    */ {
/*    */   @Nullable
/*    */   private static String environmentOverride;
/*    */   private static final String PROP_PREFIX = "minecraft.api.";
/*    */   
/*    */   public static void setEnvironmentOverride(@Nullable String paramString) {
/* 18 */     environmentOverride = paramString;
/*    */   }
/*    */ 
/*    */   
/* 22 */   private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentParser.class);
/*    */   
/*    */   public static final String PROP_ENV = "minecraft.api.env";
/*    */   public static final String PROP_SESSION_HOST = "minecraft.api.session.host";
/*    */   public static final String PROP_SERVICES_HOST = "minecraft.api.services.host";
/*    */   public static final String PROP_PROFILES_HOST = "minecraft.api.profiles.host";
/*    */   
/*    */   public static Optional<Environment> getEnvironmentFromProperties() {
/* 30 */     String str = (environmentOverride != null) ? environmentOverride : System.getProperty("minecraft.api.env");
/* 31 */     Optional<Environment> optional = YggdrasilEnvironment.fromString(str);
/* 32 */     return optional.isPresent() ? optional : fromHostNames();
/*    */   }
/*    */ 
/*    */   
/*    */   private static Optional<Environment> fromHostNames() {
/* 37 */     String str1 = System.getProperty("minecraft.api.session.host");
/* 38 */     String str2 = System.getProperty("minecraft.api.services.host");
/* 39 */     String str3 = System.getProperty("minecraft.api.profiles.host");
/*    */     
/* 41 */     if (str2 != null && str1 != null && str3 != null) {
/* 42 */       return Optional.of(new Environment(str1, str2, str3, "properties"));
/*    */     }
/* 44 */     if (str2 != null || str1 != null || str3 != null) {
/* 45 */       LOGGER.info("Ignoring hosts properties. All need to be set: {}", 
/*    */           
/* 47 */           List.of("minecraft.api.services.host", "minecraft.api.session.host", "minecraft.api.profiles.host"));
/*    */     }
/*    */     
/* 50 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\EnvironmentParser.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */