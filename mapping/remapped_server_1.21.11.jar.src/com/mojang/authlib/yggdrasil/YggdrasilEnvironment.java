/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.mojang.authlib.Environment;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public enum YggdrasilEnvironment
/*    */ {
/* 10 */   PROD("https://sessionserver.mojang.com", "https://api.minecraftservices.com", "https://api.mojang.com"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   STAGING("https://yggdrasil-auth-session-staging.mojang.zone", "https://api-staging.minecraftservices.com", "https://api-staging.mojang.com");
/*    */ 
/*    */ 
/*    */   
/*    */   private final Environment environment;
/*    */ 
/*    */ 
/*    */   
/*    */   YggdrasilEnvironment(String paramString1, String paramString2, String paramString3) {
/* 24 */     this.environment = new Environment(paramString1, paramString2, paramString3, name());
/*    */   }
/*    */   
/*    */   public Environment getEnvironment() {
/* 28 */     return this.environment;
/*    */   }
/*    */   
/*    */   public static Optional<Environment> fromString(@Nullable String paramString) {
/* 32 */     return 
/* 33 */       Stream.<YggdrasilEnvironment>of(values())
/* 34 */       .filter(paramYggdrasilEnvironment -> (paramString != null && paramString.equalsIgnoreCase(paramYggdrasilEnvironment.name())))
/* 35 */       .findFirst()
/* 36 */       .map(YggdrasilEnvironment::getEnvironment);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilEnvironment.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */