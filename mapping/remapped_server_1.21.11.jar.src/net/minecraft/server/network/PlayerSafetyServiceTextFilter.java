/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.microsoft.aad.msal4j.ClientCredentialFactory;
/*     */ import com.microsoft.aad.msal4j.ClientCredentialParameters;
/*     */ import com.microsoft.aad.msal4j.ConfidentialClientApplication;
/*     */ import com.microsoft.aad.msal4j.IAuthenticationResult;
/*     */ import com.microsoft.aad.msal4j.IClientCertificate;
/*     */ import com.microsoft.aad.msal4j.IClientCredential;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public class PlayerSafetyServiceTextFilter
/*     */   extends ServerTextFilter {
/*     */   private final ConfidentialClientApplication client;
/*     */   private final ClientCredentialParameters clientParameters;
/*     */   private final Set<String> fullyFilteredEvents;
/*     */   private final int connectionReadTimeoutMs;
/*     */   
/*     */   private PlayerSafetyServiceTextFilter(URL paramURL, ServerTextFilter.MessageEncoder paramMessageEncoder, ServerTextFilter.IgnoreStrategy paramIgnoreStrategy, ExecutorService paramExecutorService, ConfidentialClientApplication paramConfidentialClientApplication, ClientCredentialParameters paramClientCredentialParameters, Set<String> paramSet, int paramInt) {
/*  34 */     super(paramURL, paramMessageEncoder, paramIgnoreStrategy, paramExecutorService);
/*  35 */     this.client = paramConfidentialClientApplication;
/*  36 */     this.clientParameters = paramClientCredentialParameters;
/*  37 */     this.fullyFilteredEvents = paramSet;
/*  38 */     this.connectionReadTimeoutMs = paramInt; } public static ServerTextFilter createTextFilterFromConfig(String paramString) {
/*     */     URL uRL;
/*     */     IClientCertificate iClientCertificate;
/*     */     ConfidentialClientApplication confidentialClientApplication;
/*  42 */     JsonObject jsonObject = GsonHelper.parse(paramString);
/*  43 */     URI uRI = URI.create(GsonHelper.getAsString(jsonObject, "apiServer"));
/*  44 */     String str1 = GsonHelper.getAsString(jsonObject, "apiPath");
/*  45 */     String str2 = GsonHelper.getAsString(jsonObject, "scope");
/*  46 */     String str3 = GsonHelper.getAsString(jsonObject, "serverId", "");
/*  47 */     String str4 = GsonHelper.getAsString(jsonObject, "applicationId");
/*  48 */     String str5 = GsonHelper.getAsString(jsonObject, "tenantId");
/*  49 */     String str6 = GsonHelper.getAsString(jsonObject, "roomId", "Java:Chat");
/*  50 */     String str7 = GsonHelper.getAsString(jsonObject, "certificatePath");
/*  51 */     String str8 = GsonHelper.getAsString(jsonObject, "certificatePassword", "");
/*  52 */     int i = GsonHelper.getAsInt(jsonObject, "hashesToDrop", -1);
/*  53 */     int j = GsonHelper.getAsInt(jsonObject, "maxConcurrentRequests", 7);
/*  54 */     JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "fullyFilteredEvents");
/*  55 */     HashSet<String> hashSet = new HashSet();
/*  56 */     jsonArray.forEach(paramJsonElement -> paramSet.add(GsonHelper.convertToString(paramJsonElement, "filteredEvent")));
/*  57 */     int k = GsonHelper.getAsInt(jsonObject, "connectionReadTimeoutMs", 2000);
/*     */ 
/*     */     
/*     */     try {
/*  61 */       uRL = uRI.resolve(str1).toURL();
/*  62 */     } catch (MalformedURLException malformedURLException) {
/*  63 */       throw new RuntimeException(malformedURLException);
/*     */     } 
/*     */     
/*  66 */     ServerTextFilter.MessageEncoder messageEncoder = (paramGameProfile, paramString3) -> {
/*     */         JsonObject jsonObject = new JsonObject();
/*     */         jsonObject.addProperty("userId", paramGameProfile.id().toString());
/*     */         jsonObject.addProperty("userDisplayName", paramGameProfile.name());
/*     */         jsonObject.addProperty("server", paramString1);
/*     */         jsonObject.addProperty("room", paramString2);
/*     */         jsonObject.addProperty("area", "JavaChatRealms");
/*     */         jsonObject.addProperty("data", paramString3);
/*     */         jsonObject.addProperty("language", "*");
/*     */         return jsonObject;
/*     */       };
/*  77 */     ServerTextFilter.IgnoreStrategy ignoreStrategy = ServerTextFilter.IgnoreStrategy.select(i);
/*     */     
/*  79 */     ExecutorService executorService = createWorkerPool(j);
/*     */ 
/*     */     
/*  82 */     try { InputStream inputStream = Files.newInputStream(Path.of(str7, new String[0]), new java.nio.file.OpenOption[0]); 
/*  83 */       try { iClientCertificate = ClientCredentialFactory.createFromCertificate(inputStream, str8);
/*  84 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception)
/*  85 */     { LOGGER.warn("Failed to open certificate file");
/*  86 */       return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  96 */       confidentialClientApplication = ((ConfidentialClientApplication.Builder)((ConfidentialClientApplication.Builder)ConfidentialClientApplication.builder(str4, (IClientCredential)iClientCertificate).sendX5c(true).executorService(executorService)).authority(String.format(Locale.ROOT, "https://login.microsoftonline.com/%s/", new Object[] { str5 }))).build();
/*  97 */     } catch (Exception exception) {
/*  98 */       LOGGER.warn("Failed to create confidential client application");
/*  99 */       return null;
/*     */     } 
/*     */     
/* 102 */     ClientCredentialParameters clientCredentialParameters = ClientCredentialParameters.builder(Set.of(str2)).build();
/* 103 */     return new PlayerSafetyServiceTextFilter(uRL, messageEncoder, ignoreStrategy, executorService, confidentialClientApplication, clientCredentialParameters, hashSet, k);
/*     */   }
/*     */   
/*     */   private IAuthenticationResult aquireIAuthenticationResult() {
/* 107 */     return this.client.acquireToken(this.clientParameters).join();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setAuthorizationProperty(HttpURLConnection paramHttpURLConnection) {
/* 112 */     IAuthenticationResult iAuthenticationResult = aquireIAuthenticationResult();
/* 113 */     paramHttpURLConnection.setRequestProperty("Authorization", "Bearer " + iAuthenticationResult.accessToken());
/*     */   }
/*     */ 
/*     */   
/*     */   protected FilteredText filterText(String paramString, ServerTextFilter.IgnoreStrategy paramIgnoreStrategy, JsonObject paramJsonObject) {
/* 118 */     JsonObject jsonObject = GsonHelper.getAsJsonObject(paramJsonObject, "result", null);
/* 119 */     if (jsonObject == null) {
/* 120 */       return FilteredText.fullyFiltered(paramString);
/*     */     }
/* 122 */     boolean bool = GsonHelper.getAsBoolean(jsonObject, "filtered", true);
/* 123 */     if (!bool) {
/* 124 */       return FilteredText.passThrough(paramString);
/*     */     }
/* 126 */     JsonArray jsonArray1 = GsonHelper.getAsJsonArray(jsonObject, "events", new JsonArray());
/* 127 */     for (JsonElement jsonElement : jsonArray1) {
/* 128 */       JsonObject jsonObject1 = jsonElement.getAsJsonObject();
/*     */       
/* 130 */       String str = GsonHelper.getAsString(jsonObject1, "id", "");
/* 131 */       if (this.fullyFilteredEvents.contains(str)) {
/* 132 */         return FilteredText.fullyFiltered(paramString);
/*     */       }
/*     */     } 
/*     */     
/* 136 */     JsonArray jsonArray2 = GsonHelper.getAsJsonArray(jsonObject, "redactedTextIndex", new JsonArray());
/* 137 */     return new FilteredText(paramString, parseMask(paramString, jsonArray2, paramIgnoreStrategy));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int connectionReadTimeout() {
/* 142 */     return this.connectionReadTimeoutMs;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\PlayerSafetyServiceTextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */