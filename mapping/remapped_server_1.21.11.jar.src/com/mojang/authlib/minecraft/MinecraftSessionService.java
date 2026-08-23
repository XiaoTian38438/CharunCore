/*    */ package com.mojang.authlib.minecraft;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*    */ import com.mojang.authlib.properties.Property;
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.net.InetAddress;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
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
/*    */ public interface MinecraftSessionService
/*    */ {
/*    */   void joinServer(UUID paramUUID, String paramString1, String paramString2) throws AuthenticationException;
/*    */   
/*    */   @Nullable
/*    */   ProfileResult hasJoinedServer(String paramString1, String paramString2, @Nullable InetAddress paramInetAddress) throws AuthenticationUnavailableException;
/*    */   
/*    */   @Nullable
/*    */   Property getPackedTextures(GameProfile paramGameProfile);
/*    */   
/*    */   MinecraftProfileTextures unpackTextures(Property paramProperty);
/*    */   
/*    */   default MinecraftProfileTextures getTextures(GameProfile paramGameProfile) {
/* 73 */     Property property = getPackedTextures(paramGameProfile);
/* 74 */     return (property != null) ? unpackTextures(property) : MinecraftProfileTextures.EMPTY;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   ProfileResult fetchProfile(UUID paramUUID, boolean paramBoolean);
/*    */   
/*    */   String getSecurePropertyValue(Property paramProperty) throws InsecurePublicKeyException;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\minecraft\MinecraftSessionService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */