/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends CacheLoader<UUID, Optional<ProfileResult>>
/*    */ {
/*    */   public Optional<ProfileResult> load(UUID paramUUID) {
/* 64 */     return Optional.ofNullable(YggdrasilMinecraftSessionService.this.fetchProfileUncached(paramUUID, false));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilMinecraftSessionService$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */