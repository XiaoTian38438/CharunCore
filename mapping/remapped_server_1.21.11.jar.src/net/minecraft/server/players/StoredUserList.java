/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class StoredUserList<K, V extends StoredUserEntry<K>>
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  30 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*     */   
/*     */   private final File file;
/*  33 */   private final Map<String, V> map = Maps.newHashMap();
/*     */   protected final NotificationService notificationService;
/*     */   
/*     */   public StoredUserList(File paramFile, NotificationService paramNotificationService) {
/*  37 */     this.file = paramFile;
/*  38 */     this.notificationService = paramNotificationService;
/*     */   }
/*     */   
/*     */   public File getFile() {
/*  42 */     return this.file;
/*     */   }
/*     */   
/*     */   public boolean add(V paramV) {
/*  46 */     String str = getKeyForUser(paramV.getUser());
/*  47 */     StoredUserEntry storedUserEntry = (StoredUserEntry)this.map.get(str);
/*  48 */     if (paramV.equals(storedUserEntry)) {
/*  49 */       return false;
/*     */     }
/*  51 */     this.map.put(str, paramV);
/*     */     try {
/*  53 */       save();
/*  54 */     } catch (IOException iOException) {
/*  55 */       LOGGER.warn("Could not save the list after adding a user.", iOException);
/*     */     } 
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public V get(K paramK) {
/*  61 */     removeExpired();
/*  62 */     return this.map.get(getKeyForUser(paramK));
/*     */   }
/*     */   
/*     */   public boolean remove(K paramK) {
/*  66 */     StoredUserEntry storedUserEntry = (StoredUserEntry)this.map.remove(getKeyForUser(paramK));
/*  67 */     if (storedUserEntry == null) {
/*  68 */       return false;
/*     */     }
/*     */     try {
/*  71 */       save();
/*  72 */     } catch (IOException iOException) {
/*  73 */       LOGGER.warn("Could not save the list after removing a user.", iOException);
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(StoredUserEntry<K> paramStoredUserEntry) {
/*  79 */     return remove(Objects.requireNonNull(paramStoredUserEntry.getUser()));
/*     */   }
/*     */   
/*     */   public void clear() {
/*  83 */     this.map.clear();
/*     */     try {
/*  85 */       save();
/*  86 */     } catch (IOException iOException) {
/*  87 */       LOGGER.warn("Could not save the list after removing a user.", iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String[] getUserList() {
/*  92 */     return (String[])this.map.keySet().toArray((Object[])new String[0]);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  96 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   protected String getKeyForUser(K paramK) {
/* 100 */     return paramK.toString();
/*     */   }
/*     */   
/*     */   protected boolean contains(K paramK) {
/* 104 */     return this.map.containsKey(getKeyForUser(paramK));
/*     */   }
/*     */   
/*     */   private void removeExpired() {
/* 108 */     ArrayList arrayList = Lists.newArrayList();
/* 109 */     for (StoredUserEntry storedUserEntry : this.map.values()) {
/* 110 */       if (storedUserEntry.hasExpired()) {
/* 111 */         arrayList.add(storedUserEntry.getUser());
/*     */       }
/*     */     } 
/* 114 */     for (K k : arrayList) {
/* 115 */       this.map.remove(getKeyForUser(k));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> getEntries() {
/* 122 */     return this.map.values();
/*     */   }
/*     */   
/*     */   public void save() throws IOException {
/* 126 */     JsonArray jsonArray = new JsonArray();
/* 127 */     Objects.requireNonNull(jsonArray); this.map.values().stream().map(paramStoredUserEntry -> { Objects.requireNonNull(paramStoredUserEntry); return (JsonObject)Util.make(new JsonObject(), paramStoredUserEntry::serialize); }).forEach(jsonArray::add);
/* 128 */     BufferedWriter bufferedWriter = Files.newWriter(this.file, StandardCharsets.UTF_8); 
/* 129 */     try { GSON.toJson((JsonElement)jsonArray, GSON.newJsonWriter(bufferedWriter));
/* 130 */       if (bufferedWriter != null) bufferedWriter.close();  }
/*     */     catch (Throwable throwable) { if (bufferedWriter != null)
/*     */         try { bufferedWriter.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 136 */      } public void load() throws IOException { if (!this.file.exists()) {
/*     */       return;
/*     */     }
/* 139 */     BufferedReader bufferedReader = Files.newReader(this.file, StandardCharsets.UTF_8); try {
/* 140 */       this.map.clear();
/* 141 */       JsonArray jsonArray = (JsonArray)GSON.fromJson(bufferedReader, JsonArray.class);
/* 142 */       if (jsonArray == null)
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         if (bufferedReader != null) bufferedReader.close();  return; }  for (JsonElement jsonElement : jsonArray) { JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "entry"); StoredUserEntry<K> storedUserEntry = createEntry(jsonObject); if (storedUserEntry.getUser() != null) this.map.put(getKeyForUser(storedUserEntry.getUser()), (V)storedUserEntry);  }  if (bufferedReader != null) bufferedReader.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (bufferedReader != null)
/*     */         try {
/*     */           bufferedReader.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     }  }
/*     */ 
/*     */   
/*     */   protected abstract StoredUserEntry<K> createEntry(JsonObject paramJsonObject);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\StoredUserList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */