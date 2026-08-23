/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.notifications.EmptyNotificationService;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class OldUsersConverter
/*     */ {
/*  33 */   static final Logger LOGGER = LogUtils.getLogger();
/*  34 */   public static final File OLD_IPBANLIST = new File("banned-ips.txt");
/*  35 */   public static final File OLD_USERBANLIST = new File("banned-players.txt");
/*  36 */   public static final File OLD_OPLIST = new File("ops.txt");
/*  37 */   public static final File OLD_WHITELIST = new File("white-list.txt");
/*     */   
/*     */   static List<String> readOldListFormat(File paramFile, Map<String, String[]> paramMap) throws IOException {
/*  40 */     List<String> list = Files.readLines(paramFile, StandardCharsets.UTF_8);
/*  41 */     for (String str : list) {
/*  42 */       str = str.trim();
/*  43 */       if (str.startsWith("#") || str.isEmpty()) {
/*     */         continue;
/*     */       }
/*  46 */       String[] arrayOfString = str.split("\\|");
/*  47 */       paramMap.put(arrayOfString[0].toLowerCase(Locale.ROOT), arrayOfString);
/*     */     } 
/*  49 */     return list;
/*     */   }
/*     */   
/*     */   private static void lookupPlayers(MinecraftServer paramMinecraftServer, Collection<String> paramCollection, ProfileLookupCallback paramProfileLookupCallback) {
/*  53 */     String[] arrayOfString = (String[])paramCollection.stream().filter(paramString -> !StringUtil.isNullOrEmpty(paramString)).toArray(paramInt -> new String[paramInt]);
/*  54 */     if (paramMinecraftServer.usesAuthentication()) {
/*  55 */       paramMinecraftServer.services().profileRepository().findProfilesByNames(arrayOfString, paramProfileLookupCallback);
/*     */     } else {
/*  57 */       for (String str : arrayOfString) {
/*  58 */         paramProfileLookupCallback.onProfileLookupSucceeded(str, UUIDUtil.createOfflinePlayerUUID(str));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean convertUserBanlist(final MinecraftServer server) {
/*  64 */     final UserBanList bans = new UserBanList(PlayerList.USERBANLIST_FILE, (NotificationService)new EmptyNotificationService());
/*  65 */     if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
/*  66 */       if (userBanList.getFile().exists()) {
/*     */         try {
/*  68 */           userBanList.load();
/*  69 */         } catch (IOException iOException) {
/*  70 */           LOGGER.warn("Could not load existing file {}", userBanList.getFile().getName(), iOException);
/*     */         } 
/*     */       }
/*     */       try {
/*  74 */         final HashMap<String, String[]> userMap = Maps.newHashMap();
/*  75 */         readOldListFormat(OLD_USERBANLIST, (Map<String, String[]>)hashMap);
/*     */         
/*  77 */         ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(String param1String, UUID param1UUID) {
/*  80 */               NameAndId nameAndId = new NameAndId(param1UUID, param1String);
/*  81 */               server.services().nameToIdCache().add(nameAndId);
/*  82 */               String[] arrayOfString = (String[])userMap.get(nameAndId.name().toLowerCase(Locale.ROOT));
/*  83 */               if (arrayOfString == null) {
/*  84 */                 OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", nameAndId.name());
/*  85 */                 throw new OldUsersConverter.ConversionError("Profile not in the conversionlist");
/*     */               } 
/*     */               
/*  88 */               Date date1 = (arrayOfString.length > 1) ? OldUsersConverter.parseDate(arrayOfString[1], null) : null;
/*  89 */               String str1 = (arrayOfString.length > 2) ? arrayOfString[2] : null;
/*  90 */               Date date2 = (arrayOfString.length > 3) ? OldUsersConverter.parseDate(arrayOfString[3], null) : null;
/*  91 */               String str2 = (arrayOfString.length > 4) ? arrayOfString[4] : null;
/*  92 */               bans.add(new UserBanListEntry(nameAndId, date1, str1, date2, str2));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(String param1String, Exception param1Exception) {
/*  97 */               OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", param1String, param1Exception);
/*  98 */               if (!(param1Exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/*  99 */                 throw new OldUsersConverter.ConversionError("Could not request user " + param1String + " from backend systems", param1Exception);
/*     */               }
/*     */             }
/*     */           };
/* 103 */         lookupPlayers(server, hashMap.keySet(), profileLookupCallback);
/* 104 */         userBanList.save();
/* 105 */         renameOldFile(OLD_USERBANLIST);
/* 106 */       } catch (IOException iOException) {
/* 107 */         LOGGER.warn("Could not read old user banlist to convert it!", iOException);
/* 108 */         return false;
/* 109 */       } catch (ConversionError conversionError) {
/* 110 */         LOGGER.error("Conversion failed, please try again later", conversionError);
/* 111 */         return false;
/*     */       } 
/* 113 */       return true;
/*     */     } 
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertIpBanlist(MinecraftServer paramMinecraftServer) {
/* 119 */     IpBanList ipBanList = new IpBanList(PlayerList.IPBANLIST_FILE, (NotificationService)new EmptyNotificationService());
/* 120 */     if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
/* 121 */       if (ipBanList.getFile().exists()) {
/*     */         try {
/* 123 */           ipBanList.load();
/* 124 */         } catch (IOException iOException) {
/* 125 */           LOGGER.warn("Could not load existing file {}", ipBanList.getFile().getName(), iOException);
/*     */         } 
/*     */       }
/*     */       try {
/* 129 */         HashMap<String, String[]> hashMap = Maps.newHashMap();
/* 130 */         readOldListFormat(OLD_IPBANLIST, (Map<String, String[]>)hashMap);
/*     */         
/* 132 */         for (String str1 : hashMap.keySet()) {
/* 133 */           String[] arrayOfString = hashMap.get(str1);
/* 134 */           Date date1 = (arrayOfString.length > 1) ? parseDate(arrayOfString[1], null) : null;
/* 135 */           String str2 = (arrayOfString.length > 2) ? arrayOfString[2] : null;
/* 136 */           Date date2 = (arrayOfString.length > 3) ? parseDate(arrayOfString[3], null) : null;
/* 137 */           String str3 = (arrayOfString.length > 4) ? arrayOfString[4] : null;
/* 138 */           ipBanList.add(new IpBanListEntry(str1, date1, str2, date2, str3));
/*     */         } 
/* 140 */         ipBanList.save();
/* 141 */         renameOldFile(OLD_IPBANLIST);
/* 142 */       } catch (IOException iOException) {
/* 143 */         LOGGER.warn("Could not parse old ip banlist to convert it!", iOException);
/* 144 */         return false;
/*     */       } 
/* 146 */       return true;
/*     */     } 
/* 148 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertOpsList(final MinecraftServer server) {
/* 152 */     final ServerOpList opsList = new ServerOpList(PlayerList.OPLIST_FILE, (NotificationService)new EmptyNotificationService());
/* 153 */     if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
/* 154 */       if (serverOpList.getFile().exists()) {
/*     */         try {
/* 156 */           serverOpList.load();
/* 157 */         } catch (IOException iOException) {
/* 158 */           LOGGER.warn("Could not load existing file {}", serverOpList.getFile().getName(), iOException);
/*     */         } 
/*     */       }
/*     */       try {
/* 162 */         List<String> list = Files.readLines(OLD_OPLIST, StandardCharsets.UTF_8);
/* 163 */         ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(String param1String, UUID param1UUID) {
/* 166 */               NameAndId nameAndId = new NameAndId(param1UUID, param1String);
/* 167 */               server.services().nameToIdCache().add(nameAndId);
/* 168 */               opsList.add(new ServerOpListEntry(nameAndId, server.operatorUserPermissions(), false));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(String param1String, Exception param1Exception) {
/* 173 */               OldUsersConverter.LOGGER.warn("Could not lookup oplist entry for {}", param1String, param1Exception);
/* 174 */               if (!(param1Exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/* 175 */                 throw new OldUsersConverter.ConversionError("Could not request user " + param1String + " from backend systems", param1Exception);
/*     */               }
/*     */             }
/*     */           };
/* 179 */         lookupPlayers(server, list, profileLookupCallback);
/* 180 */         serverOpList.save();
/* 181 */         renameOldFile(OLD_OPLIST);
/* 182 */       } catch (IOException iOException) {
/* 183 */         LOGGER.warn("Could not read old oplist to convert it!", iOException);
/* 184 */         return false;
/* 185 */       } catch (ConversionError conversionError) {
/* 186 */         LOGGER.error("Conversion failed, please try again later", conversionError);
/* 187 */         return false;
/*     */       } 
/* 189 */       return true;
/*     */     } 
/* 191 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertWhiteList(final MinecraftServer server) {
/* 195 */     final UserWhiteList whitelist = new UserWhiteList(PlayerList.WHITELIST_FILE, (NotificationService)new EmptyNotificationService());
/* 196 */     if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
/* 197 */       if (userWhiteList.getFile().exists()) {
/*     */         try {
/* 199 */           userWhiteList.load();
/* 200 */         } catch (IOException iOException) {
/* 201 */           LOGGER.warn("Could not load existing file {}", userWhiteList.getFile().getName(), iOException);
/*     */         } 
/*     */       }
/*     */       try {
/* 205 */         List<String> list = Files.readLines(OLD_WHITELIST, StandardCharsets.UTF_8);
/* 206 */         ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(String param1String, UUID param1UUID) {
/* 209 */               NameAndId nameAndId = new NameAndId(param1UUID, param1String);
/* 210 */               server.services().nameToIdCache().add(nameAndId);
/* 211 */               whitelist.add(new UserWhiteListEntry(nameAndId));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(String param1String, Exception param1Exception) {
/* 216 */               OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", param1String, param1Exception);
/* 217 */               if (!(param1Exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/* 218 */                 throw new OldUsersConverter.ConversionError("Could not request user " + param1String + " from backend systems", param1Exception);
/*     */               }
/*     */             }
/*     */           };
/* 222 */         lookupPlayers(server, list, profileLookupCallback);
/* 223 */         userWhiteList.save();
/* 224 */         renameOldFile(OLD_WHITELIST);
/* 225 */       } catch (IOException iOException) {
/* 226 */         LOGGER.warn("Could not read old whitelist to convert it!", iOException);
/* 227 */         return false;
/* 228 */       } catch (ConversionError conversionError) {
/* 229 */         LOGGER.error("Conversion failed, please try again later", conversionError);
/* 230 */         return false;
/*     */       } 
/* 232 */       return true;
/*     */     } 
/* 234 */     return true;
/*     */   }
/*     */   
/*     */   public static UUID convertMobOwnerIfNecessary(final MinecraftServer server, String paramString) {
/* 238 */     if (StringUtil.isNullOrEmpty(paramString) || paramString.length() > 16) {
/*     */       try {
/* 240 */         return UUID.fromString(paramString);
/* 241 */       } catch (IllegalArgumentException illegalArgumentException) {
/* 242 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 246 */     Optional<?> optional = server.services().nameToIdCache().get(paramString).map(NameAndId::id);
/* 247 */     if (optional.isPresent()) {
/* 248 */       return (UUID)optional.get();
/*     */     }
/* 250 */     if (server.isSingleplayer() || !server.usesAuthentication()) {
/* 251 */       return UUIDUtil.createOfflinePlayerUUID(paramString);
/*     */     }
/* 253 */     final ArrayList<NameAndId> profiles = new ArrayList();
/* 254 */     ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback()
/*     */       {
/*     */         public void onProfileLookupSucceeded(String param1String, UUID param1UUID) {
/* 257 */           NameAndId nameAndId = new NameAndId(param1UUID, param1String);
/* 258 */           server.services().nameToIdCache().add(nameAndId);
/* 259 */           profiles.add(nameAndId);
/*     */         }
/*     */ 
/*     */         
/*     */         public void onProfileLookupFailed(String param1String, Exception param1Exception) {
/* 264 */           OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", param1String, param1Exception);
/*     */         }
/*     */       };
/* 267 */     lookupPlayers(server, Lists.newArrayList((Object[])new String[] { paramString }, ), profileLookupCallback);
/* 268 */     if (!arrayList.isEmpty()) {
/* 269 */       return ((NameAndId)arrayList.getFirst()).id();
/*     */     }
/*     */     
/* 272 */     return null;
/*     */   }
/*     */   
/*     */   private static class ConversionError extends RuntimeException {
/*     */     ConversionError(String param1String, Throwable param1Throwable) {
/* 277 */       super(param1String, param1Throwable);
/*     */     }
/*     */     
/*     */     ConversionError(String param1String) {
/* 281 */       super(param1String);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean convertPlayers(final DedicatedServer server) {
/* 286 */     final File worldPlayerDirectory = getWorldPlayersDirectory((MinecraftServer)server);
/* 287 */     final File worldNewPlayerDirectory = new File(file1.getParentFile(), "playerdata");
/* 288 */     final File unknownPlayerDirectory = new File(file1.getParentFile(), "unknownplayers");
/* 289 */     if (!file1.exists() || !file1.isDirectory()) {
/* 290 */       return true;
/*     */     }
/* 292 */     File[] arrayOfFile = file1.listFiles();
/* 293 */     ArrayList<String> arrayList = Lists.newArrayList();
/* 294 */     for (File file : arrayOfFile) {
/* 295 */       String str = file.getName();
/* 296 */       if (str.toLowerCase(Locale.ROOT).endsWith(".dat")) {
/*     */ 
/*     */         
/* 299 */         String str1 = str.substring(0, str.length() - ".dat".length());
/* 300 */         if (!str1.isEmpty()) {
/* 301 */           arrayList.add(str1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     try {
/* 306 */       final String[] names = arrayList.<String>toArray(new String[arrayList.size()]);
/* 307 */       ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback()
/*     */         {
/*     */           public void onProfileLookupSucceeded(String param1String, UUID param1UUID) {
/* 310 */             NameAndId nameAndId = new NameAndId(param1UUID, param1String);
/* 311 */             server.services().nameToIdCache().add(nameAndId);
/* 312 */             movePlayerFile(worldNewPlayerDirectory, getFileNameForProfile(param1String), param1UUID.toString());
/*     */           }
/*     */ 
/*     */           
/*     */           public void onProfileLookupFailed(String param1String, Exception param1Exception) {
/* 317 */             OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", param1String, param1Exception);
/* 318 */             if (param1Exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException) {
/* 319 */               String str = getFileNameForProfile(param1String);
/* 320 */               movePlayerFile(unknownPlayerDirectory, str, str);
/*     */             } else {
/* 322 */               throw new OldUsersConverter.ConversionError("Could not request user " + param1String + " from backend systems", param1Exception);
/*     */             } 
/*     */           }
/*     */           
/*     */           private void movePlayerFile(File param1File, String param1String1, String param1String2) {
/* 327 */             File file1 = new File(worldPlayerDirectory, param1String1 + ".dat");
/* 328 */             File file2 = new File(param1File, param1String2 + ".dat");
/* 329 */             OldUsersConverter.ensureDirectoryExists(param1File);
/* 330 */             if (!file1.renameTo(file2)) {
/* 331 */               throw new OldUsersConverter.ConversionError("Could not convert file for " + param1String1);
/*     */             }
/*     */           }
/*     */           
/*     */           private String getFileNameForProfile(String param1String) {
/* 336 */             String str = null;
/* 337 */             for (String str1 : names) {
/* 338 */               if (str1 != null && str1.equalsIgnoreCase(param1String)) {
/* 339 */                 str = str1;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 343 */             if (str == null) {
/* 344 */               throw new OldUsersConverter.ConversionError("Could not find the filename for " + param1String + " anymore");
/*     */             }
/* 346 */             return str;
/*     */           }
/*     */         };
/* 349 */       lookupPlayers((MinecraftServer)server, Lists.newArrayList((Object[])arrayOfString), profileLookupCallback);
/* 350 */     } catch (ConversionError conversionError) {
/* 351 */       LOGGER.error("Conversion failed, please try again later", conversionError);
/* 352 */       return false;
/*     */     } 
/*     */     
/* 355 */     return true;
/*     */   }
/*     */   
/*     */   static void ensureDirectoryExists(File paramFile) {
/* 359 */     if (paramFile.exists()) {
/* 360 */       if (paramFile.isDirectory()) {
/*     */         return;
/*     */       }
/* 363 */       throw new ConversionError("Can't create directory " + paramFile.getName() + " in world save directory.");
/*     */     } 
/*     */     
/* 366 */     if (!paramFile.mkdirs()) {
/* 367 */       throw new ConversionError("Can't create directory " + paramFile.getName() + " in world save directory.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean serverReadyAfterUserconversion(MinecraftServer paramMinecraftServer) {
/* 373 */     boolean bool = areOldUserlistsRemoved();
/* 374 */     bool = (bool && areOldPlayersConverted(paramMinecraftServer));
/* 375 */     return bool;
/*     */   }
/*     */   
/*     */   private static boolean areOldUserlistsRemoved() {
/* 379 */     boolean bool1 = false;
/* 380 */     if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
/* 381 */       bool1 = true;
/*     */     }
/* 383 */     boolean bool2 = false;
/* 384 */     if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
/* 385 */       bool2 = true;
/*     */     }
/* 387 */     boolean bool3 = false;
/* 388 */     if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
/* 389 */       bool3 = true;
/*     */     }
/* 391 */     boolean bool4 = false;
/* 392 */     if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
/* 393 */       bool4 = true;
/*     */     }
/*     */     
/* 396 */     if (bool1 || bool2 || bool3 || bool4) {
/* 397 */       LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
/* 398 */       LOGGER.warn("** please remove the following files and restart the server:");
/* 399 */       if (bool1) {
/* 400 */         LOGGER.warn("* {}", OLD_USERBANLIST.getName());
/*     */       }
/* 402 */       if (bool2) {
/* 403 */         LOGGER.warn("* {}", OLD_IPBANLIST.getName());
/*     */       }
/* 405 */       if (bool3) {
/* 406 */         LOGGER.warn("* {}", OLD_OPLIST.getName());
/*     */       }
/* 408 */       if (bool4) {
/* 409 */         LOGGER.warn("* {}", OLD_WHITELIST.getName());
/*     */       }
/* 411 */       return false;
/*     */     } 
/* 413 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areOldPlayersConverted(MinecraftServer paramMinecraftServer) {
/* 417 */     File file = getWorldPlayersDirectory(paramMinecraftServer);
/* 418 */     if (file.exists() && file.isDirectory() && ((
/* 419 */       file.list()).length > 0 || !file.delete())) {
/* 420 */       LOGGER.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
/* 421 */       LOGGER.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
/* 422 */       LOGGER.warn("** please restart the server and if the problem persists, remove the directory '{}'", file.getPath());
/* 423 */       return false;
/*     */     } 
/*     */     
/* 426 */     return true;
/*     */   }
/*     */   
/*     */   private static File getWorldPlayersDirectory(MinecraftServer paramMinecraftServer) {
/* 430 */     return paramMinecraftServer.getWorldPath(LevelResource.PLAYER_OLD_DATA_DIR).toFile();
/*     */   }
/*     */   
/*     */   private static void renameOldFile(File paramFile) {
/* 434 */     File file = new File(paramFile.getName() + ".converted");
/* 435 */     paramFile.renameTo(file);
/*     */   }
/*     */   
/*     */   static Date parseDate(String paramString, Date paramDate) {
/*     */     Date date;
/*     */     try {
/* 441 */       date = BanListEntry.DATE_FORMAT.parse(paramString);
/* 442 */     } catch (ParseException parseException) {
/* 443 */       date = paramDate;
/*     */     } 
/* 445 */     return date;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\OldUsersConverter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */