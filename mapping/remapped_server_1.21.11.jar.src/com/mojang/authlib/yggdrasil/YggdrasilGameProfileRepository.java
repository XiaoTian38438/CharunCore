/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.authlib.Environment;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.HttpAuthenticationService;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import com.mojang.authlib.exceptions.MinecraftClientException;
/*     */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*     */ import com.mojang.authlib.yggdrasil.response.NameAndId;
/*     */ import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class YggdrasilGameProfileRepository
/*     */   implements GameProfileRepository {
/*  27 */   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilGameProfileRepository.class);
/*     */   
/*     */   private static final int ENTRIES_PER_PAGE = 2;
/*     */   private static final int MAX_FAIL_COUNT = 3;
/*     */   private static final int DELAY_BETWEEN_PAGES = 100;
/*     */   private static final int DELAY_BETWEEN_FAILURES = 750;
/*     */   private final MinecraftClient client;
/*     */   private final URL searchPageUrl;
/*     */   private final String nameLookupUrl;
/*     */   
/*     */   public YggdrasilGameProfileRepository(Proxy paramProxy, Environment paramEnvironment) {
/*  38 */     this.client = MinecraftClient.unauthenticated(paramProxy);
/*  39 */     this.searchPageUrl = HttpAuthenticationService.constantURL(paramEnvironment
/*  40 */         .profilesHost() + "/minecraft/profile/lookup/bulk/byname");
/*  41 */     this.nameLookupUrl = paramEnvironment.profilesHost() + "/minecraft/profile/lookup/name/";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void findProfilesByNames(String[] paramArrayOfString, ProfileLookupCallback paramProfileLookupCallback) {
/*  48 */     Set set = (Set)Arrays.<String>stream(paramArrayOfString).filter(paramString -> !Strings.isNullOrEmpty(paramString)).collect(Collectors.toSet());
/*     */     
/*  50 */     boolean bool = false;
/*     */     
/*  52 */     label46: for (List list1 : Iterables.partition(set, 2)) {
/*  53 */       List list2 = list1.stream().map(YggdrasilGameProfileRepository::normalizeName).toList();
/*     */       
/*  55 */       byte b = 0;
/*     */ 
/*     */       
/*     */       while (true) {
/*  59 */         boolean bool1 = false;
/*     */         
/*     */         try {
/*  62 */           ProfileSearchResultsResponse profileSearchResultsResponse = (ProfileSearchResultsResponse)this.client.post(this.searchPageUrl, list2, ProfileSearchResultsResponse.class);
/*  63 */           List list = (profileSearchResultsResponse != null) ? profileSearchResultsResponse.profiles() : List.of();
/*  64 */           b = 0;
/*     */           
/*  66 */           LOGGER.debug("Page {} returned {} results, parsing", Integer.valueOf(0), Integer.valueOf(list.size()));
/*     */           
/*  68 */           HashSet<String> hashSet = new HashSet(list.size());
/*  69 */           for (NameAndId nameAndId : list) {
/*  70 */             LOGGER.debug("Successfully looked up profile {}", nameAndId);
/*  71 */             hashSet.add(normalizeName(nameAndId.name()));
/*  72 */             paramProfileLookupCallback.onProfileLookupSucceeded(nameAndId.name(), nameAndId.id());
/*     */           } 
/*     */           
/*  75 */           for (String str : list1) {
/*  76 */             if (hashSet.contains(normalizeName(str))) {
/*     */               continue;
/*     */             }
/*  79 */             LOGGER.debug("Couldn't find profile {}", str);
/*  80 */             paramProfileLookupCallback.onProfileLookupFailed(str, new ProfileNotFoundException("Server did not find the requested profile"));
/*     */           } 
/*     */           
/*     */           try {
/*  84 */             Thread.sleep(100L);
/*  85 */           } catch (InterruptedException interruptedException) {}
/*     */         }
/*  87 */         catch (MinecraftClientException minecraftClientException) {
/*  88 */           b++;
/*     */           
/*  90 */           if (b == 3) {
/*  91 */             for (String str : list1) {
/*  92 */               LOGGER.debug("Couldn't find profile {} because of a server error", str);
/*  93 */               paramProfileLookupCallback.onProfileLookupFailed(str, (Exception)minecraftClientException.toAuthenticationException());
/*     */             } 
/*     */           } else {
/*     */             try {
/*  97 */               Thread.sleep(750L);
/*  98 */             } catch (InterruptedException interruptedException) {}
/*     */             
/* 100 */             bool1 = true;
/*     */           } 
/*     */         } 
/* 103 */         if (!bool1)
/*     */           continue label46; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public Optional<NameAndId> findProfileByName(String paramString) {
/*     */     try {
/* 110 */       return Optional.ofNullable((NameAndId)this.client.get(HttpAuthenticationService.constantURL(this.nameLookupUrl + this.nameLookupUrl), NameAndId.class));
/* 111 */     } catch (MinecraftClientException minecraftClientException) {
/* 112 */       LOGGER.warn("Couldn't find profile with name: {}", paramString, minecraftClientException);
/* 113 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String normalizeName(String paramString) {
/* 118 */     return paramString.toLowerCase(Locale.ROOT);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilGameProfileRepository.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */