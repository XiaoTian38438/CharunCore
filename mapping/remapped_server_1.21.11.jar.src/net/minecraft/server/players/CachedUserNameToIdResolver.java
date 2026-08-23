/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class CachedUserNameToIdResolver
/*     */   implements UserNameToIdResolver
/*     */ {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int GAMEPROFILES_MRU_LIMIT = 1000;
/*     */   private static final int GAMEPROFILES_EXPIRATION_MONTHS = 1;
/*     */   private boolean resolveOfflineUsers = true;
/*  49 */   private final Map<String, GameProfileInfo> profilesByName = new ConcurrentHashMap<>();
/*  50 */   private final Map<UUID, GameProfileInfo> profilesByUUID = new ConcurrentHashMap<>();
/*     */   private final GameProfileRepository profileRepository;
/*  52 */   private final Gson gson = (new GsonBuilder()).create();
/*     */   private final File file;
/*  54 */   private final AtomicLong operationCount = new AtomicLong();
/*     */   
/*     */   public CachedUserNameToIdResolver(GameProfileRepository paramGameProfileRepository, File paramFile) {
/*  57 */     this.profileRepository = paramGameProfileRepository;
/*  58 */     this.file = paramFile;
/*     */     
/*  60 */     Lists.reverse(load()).forEach(this::safeAdd);
/*     */   }
/*     */   
/*     */   private void safeAdd(GameProfileInfo paramGameProfileInfo) {
/*  64 */     NameAndId nameAndId = paramGameProfileInfo.nameAndId();
/*  65 */     paramGameProfileInfo.setLastAccess(getNextOperation());
/*  66 */     this.profilesByName.put(nameAndId.name().toLowerCase(Locale.ROOT), paramGameProfileInfo);
/*  67 */     this.profilesByUUID.put(nameAndId.id(), paramGameProfileInfo);
/*     */   }
/*     */   
/*     */   private Optional<NameAndId> lookupGameProfile(GameProfileRepository paramGameProfileRepository, String paramString) {
/*  71 */     if (!StringUtil.isValidPlayerName(paramString)) {
/*  72 */       return createUnknownProfile(paramString);
/*     */     }
/*     */     
/*  75 */     Optional<NameAndId> optional = paramGameProfileRepository.findProfileByName(paramString).map(NameAndId::new);
/*  76 */     if (optional.isEmpty()) {
/*  77 */       return createUnknownProfile(paramString);
/*     */     }
/*  79 */     return optional;
/*     */   }
/*     */   
/*     */   private Optional<NameAndId> createUnknownProfile(String paramString) {
/*  83 */     if (this.resolveOfflineUsers) {
/*  84 */       return Optional.of(NameAndId.createOffline(paramString));
/*     */     }
/*  86 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resolveOfflineUsers(boolean paramBoolean) {
/*  91 */     this.resolveOfflineUsers = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(NameAndId paramNameAndId) {
/*  96 */     addInternal(paramNameAndId);
/*     */   }
/*     */   
/*     */   private GameProfileInfo addInternal(NameAndId paramNameAndId) {
/* 100 */     Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ROOT);
/* 101 */     calendar.setTime(new Date());
/* 102 */     calendar.add(2, 1);
/* 103 */     Date date = calendar.getTime();
/*     */     
/* 105 */     GameProfileInfo gameProfileInfo = new GameProfileInfo(paramNameAndId, date);
/* 106 */     safeAdd(gameProfileInfo);
/* 107 */     save();
/* 108 */     return gameProfileInfo;
/*     */   }
/*     */   
/*     */   private long getNextOperation() {
/* 112 */     return this.operationCount.incrementAndGet();
/*     */   }
/*     */   
/*     */   public Optional<NameAndId> get(String paramString) {
/*     */     Optional<?> optional;
/* 117 */     String str = paramString.toLowerCase(Locale.ROOT);
/* 118 */     GameProfileInfo gameProfileInfo = this.profilesByName.get(str);
/*     */     
/* 120 */     boolean bool = false;
/*     */     
/* 122 */     if (gameProfileInfo != null && (new Date()).getTime() >= gameProfileInfo.expirationDate.getTime()) {
/*     */       
/* 124 */       this.profilesByUUID.remove(gameProfileInfo.nameAndId().id());
/* 125 */       this.profilesByName.remove(gameProfileInfo.nameAndId().name().toLowerCase(Locale.ROOT));
/* 126 */       bool = true;
/* 127 */       gameProfileInfo = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (gameProfileInfo != null) {
/* 133 */       gameProfileInfo.setLastAccess(getNextOperation());
/* 134 */       optional = Optional.of(gameProfileInfo.nameAndId());
/*     */     } else {
/* 136 */       Optional<NameAndId> optional1 = lookupGameProfile(this.profileRepository, str);
/* 137 */       if (optional1.isPresent()) {
/* 138 */         optional = Optional.of(addInternal(optional1.get()).nameAndId());
/*     */         
/* 140 */         bool = false;
/*     */       } else {
/* 142 */         optional = Optional.empty();
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (bool) {
/* 147 */       save();
/*     */     }
/* 149 */     return (Optional)optional;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<NameAndId> get(UUID paramUUID) {
/* 154 */     GameProfileInfo gameProfileInfo = this.profilesByUUID.get(paramUUID);
/* 155 */     if (gameProfileInfo == null) {
/* 156 */       return Optional.empty();
/*     */     }
/* 158 */     gameProfileInfo.setLastAccess(getNextOperation());
/* 159 */     return Optional.of(gameProfileInfo.nameAndId());
/*     */   }
/*     */   
/*     */   private static DateFormat createDateFormat() {
/* 163 */     return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);
/*     */   }
/*     */   
/*     */   private List<GameProfileInfo> load() {
/* 167 */     ArrayList<GameProfileInfo> arrayList = Lists.newArrayList(); 
/* 168 */     try { BufferedReader bufferedReader = Files.newReader(this.file, StandardCharsets.UTF_8); 
/* 169 */       try { JsonArray jsonArray = (JsonArray)this.gson.fromJson(bufferedReader, JsonArray.class);
/* 170 */         if (jsonArray == null)
/* 171 */         { ArrayList<GameProfileInfo> arrayList1 = arrayList;
/*     */ 
/*     */ 
/*     */           
/* 175 */           if (bufferedReader != null) bufferedReader.close();  return arrayList1; }  DateFormat dateFormat = createDateFormat(); jsonArray.forEach(paramJsonElement -> { Objects.requireNonNull(paramList); readGameProfile(paramJsonElement, paramDateFormat).ifPresent(paramList::add); }); if (bufferedReader != null) bufferedReader.close();  } catch (Throwable throwable) { if (bufferedReader != null) try { bufferedReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (FileNotFoundException fileNotFoundException)
/*     */     {  }
/* 177 */     catch (IOException|com.google.gson.JsonParseException iOException)
/* 178 */     { LOGGER.warn("Failed to load profile cache {}", this.file, iOException); }
/*     */     
/* 180 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() {
/* 185 */     JsonArray jsonArray = new JsonArray();
/* 186 */     DateFormat dateFormat = createDateFormat();
/* 187 */     getTopMRUProfiles(1000).forEach(paramGameProfileInfo -> paramJsonArray.add(writeGameProfile(paramGameProfileInfo, paramDateFormat)));
/*     */     
/* 189 */     String str = this.gson.toJson((JsonElement)jsonArray); 
/* 190 */     try { BufferedWriter bufferedWriter = Files.newWriter(this.file, StandardCharsets.UTF_8); 
/* 191 */       try { bufferedWriter.write(str);
/* 192 */         if (bufferedWriter != null) bufferedWriter.close();  } catch (Throwable throwable) { if (bufferedWriter != null) try { bufferedWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Stream<GameProfileInfo> getTopMRUProfiles(int paramInt) {
/* 198 */     return ImmutableList.copyOf(this.profilesByUUID.values()).stream().sorted(Comparator.<GameProfileInfo, Comparable>comparing(GameProfileInfo::lastAccess).reversed()).limit(paramInt);
/*     */   }
/*     */   
/*     */   private static JsonElement writeGameProfile(GameProfileInfo paramGameProfileInfo, DateFormat paramDateFormat) {
/* 202 */     JsonObject jsonObject = new JsonObject();
/* 203 */     paramGameProfileInfo.nameAndId().appendTo(jsonObject);
/* 204 */     jsonObject.addProperty("expiresOn", paramDateFormat.format(paramGameProfileInfo.expirationDate()));
/* 205 */     return (JsonElement)jsonObject;
/*     */   }
/*     */   
/*     */   private static Optional<GameProfileInfo> readGameProfile(JsonElement paramJsonElement, DateFormat paramDateFormat) {
/* 209 */     if (paramJsonElement.isJsonObject()) {
/* 210 */       JsonObject jsonObject = paramJsonElement.getAsJsonObject();
/* 211 */       NameAndId nameAndId = NameAndId.fromJson(jsonObject);
/* 212 */       if (nameAndId != null) {
/* 213 */         JsonElement jsonElement = jsonObject.get("expiresOn");
/* 214 */         if (jsonElement != null) {
/* 215 */           String str = jsonElement.getAsString();
/*     */           try {
/* 217 */             Date date = paramDateFormat.parse(str);
/* 218 */             return Optional.of(new GameProfileInfo(nameAndId, date));
/* 219 */           } catch (ParseException parseException) {
/* 220 */             LOGGER.warn("Failed to parse date {}", str, parseException);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 225 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static class GameProfileInfo
/*     */   {
/*     */     private final NameAndId nameAndId;
/*     */     final Date expirationDate;
/*     */     private volatile long lastAccess;
/*     */     
/*     */     GameProfileInfo(NameAndId param1NameAndId, Date param1Date) {
/* 235 */       this.nameAndId = param1NameAndId;
/* 236 */       this.expirationDate = param1Date;
/*     */     }
/*     */     
/*     */     public NameAndId nameAndId() {
/* 240 */       return this.nameAndId;
/*     */     }
/*     */     
/*     */     public Date expirationDate() {
/* 244 */       return this.expirationDate;
/*     */     }
/*     */     
/*     */     public void setLastAccess(long param1Long) {
/* 248 */       this.lastAccess = param1Long;
/*     */     }
/*     */     
/*     */     public long lastAccess() {
/* 252 */       return this.lastAccess;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\CachedUserNameToIdResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */