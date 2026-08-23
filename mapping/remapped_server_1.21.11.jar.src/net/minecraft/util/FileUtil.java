/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*  22 */   private static final Pattern COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);
/*     */   
/*     */   private static final int MAX_FILE_NAME = 255;
/*  25 */   private static final Pattern RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);
/*     */   
/*  27 */   private static final Pattern STRICT_PATH_SEGMENT_CHECK = Pattern.compile("[-._a-z0-9]+");
/*     */   
/*     */   public static String sanitizeName(String paramString) {
/*  30 */     for (char c : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
/*  31 */       paramString = paramString.replace(c, '_');
/*     */     }
/*     */     
/*  34 */     return paramString.replaceAll("[./\"]", "_");
/*     */   }
/*     */   
/*     */   public static String findAvailableName(Path paramPath, String paramString1, String paramString2) throws IOException {
/*  38 */     paramString1 = sanitizeName(paramString1);
/*     */     
/*  40 */     if (!isPathPartPortable(paramString1)) {
/*  41 */       paramString1 = "_" + paramString1 + "_";
/*     */     }
/*     */     
/*  44 */     Matcher matcher = COPY_COUNTER_PATTERN.matcher(paramString1);
/*  45 */     int i = 0;
/*  46 */     if (matcher.matches()) {
/*  47 */       paramString1 = matcher.group("name");
/*  48 */       i = Integer.parseInt(matcher.group("count"));
/*     */     } 
/*  50 */     if (paramString1.length() > 255 - paramString2.length()) {
/*  51 */       paramString1 = paramString1.substring(0, 255 - paramString2.length());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  57 */       String str = paramString1;
/*  58 */       if (i != 0) {
/*  59 */         String str1 = " (" + i + ")";
/*  60 */         int j = 255 - str1.length();
/*  61 */         if (str.length() > j) {
/*  62 */           str = str.substring(0, j);
/*     */         }
/*  64 */         str = str + str;
/*     */       } 
/*     */       
/*  67 */       str = str + str;
/*     */       
/*  69 */       Path path = paramPath.resolve(str);
/*     */       try {
/*  71 */         Path path1 = Files.createDirectory(path, (FileAttribute<?>[])new FileAttribute[0]);
/*  72 */         Files.deleteIfExists(path1);
/*  73 */         return paramPath.relativize(path1).toString();
/*  74 */       } catch (FileAlreadyExistsException fileAlreadyExistsException) {
/*  75 */         i++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isPathNormalized(Path paramPath) {
/*  81 */     Path path = paramPath.normalize();
/*  82 */     return path.equals(paramPath);
/*     */   }
/*     */   
/*     */   public static boolean isPathPortable(Path paramPath) {
/*  86 */     for (Path path : paramPath) {
/*  87 */       if (!isPathPartPortable(path.toString())) {
/*  88 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPathPartPortable(String paramString) {
/*  96 */     return !RESERVED_WINDOWS_FILENAMES.matcher(paramString).matches();
/*     */   }
/*     */   
/*     */   public static Path createPathToResource(Path paramPath, String paramString1, String paramString2) {
/* 100 */     String str = paramString1 + paramString1;
/* 101 */     Path path = Paths.get(str, new String[0]);
/*     */     
/* 103 */     if (path.endsWith(paramString2)) {
/* 104 */       throw new InvalidPathException(str, "empty resource name");
/*     */     }
/*     */     
/* 107 */     return paramPath.resolve(path);
/*     */   }
/*     */   
/*     */   public static String getFullResourcePath(String paramString) {
/* 111 */     return FilenameUtils.getFullPath(paramString).replace(File.separator, "/");
/*     */   }
/*     */   
/*     */   public static String normalizeResourcePath(String paramString) {
/* 115 */     return FilenameUtils.normalize(paramString).replace(File.separator, "/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataResult<List<String>> decomposePath(String paramString) {
/* 127 */     int i = paramString.indexOf('/');
/* 128 */     if (i == -1) {
/* 129 */       switch (paramString) { case "": case ".": case "..":  }  return 
/*     */ 
/*     */         
/* 132 */         !containsAllowedCharactersOnly(paramString) ? 
/* 133 */         DataResult.error(() -> "Invalid path '" + paramString + "'") : 
/*     */         
/* 135 */         DataResult.success(List.of(paramString));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 140 */     ArrayList<String> arrayList = new ArrayList();
/*     */     
/* 142 */     int j = 0;
/* 143 */     boolean bool = false;
/*     */     while (true) {
/* 145 */       String str = paramString.substring(j, i);
/* 146 */       switch (str) {
/*     */         case "":
/*     */         case ".":
/*     */         case "..":
/* 150 */           return DataResult.error(() -> "Invalid segment '" + paramString1 + "' in path '" + paramString2 + "'");
/*     */       } 
/* 152 */       if (!containsAllowedCharactersOnly(str)) {
/* 153 */         return DataResult.error(() -> "Invalid segment '" + paramString1 + "' in path '" + paramString2 + "'");
/*     */       }
/* 155 */       arrayList.add(str);
/*     */ 
/*     */ 
/*     */       
/* 159 */       if (bool) {
/* 160 */         return DataResult.success(arrayList);
/*     */       }
/* 162 */       j = i + 1;
/* 163 */       i = paramString.indexOf('/', j);
/* 164 */       if (i == -1) {
/* 165 */         i = paramString.length();
/* 166 */         bool = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Path resolvePath(Path paramPath, List<String> paramList) {
/* 173 */     int i = paramList.size();
/* 174 */     switch (i) { case 0:
/*     */       
/*     */       case 1:
/*     */        }
/* 178 */      String[] arrayOfString = new String[i - 1];
/* 179 */     for (byte b = 1; b < i; b++) {
/* 180 */       arrayOfString[b - 1] = paramList.get(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean containsAllowedCharactersOnly(String paramString) {
/* 188 */     return STRICT_PATH_SEGMENT_CHECK.matcher(paramString).matches();
/*     */   }
/*     */   
/*     */   public static boolean isValidPathSegment(String paramString) {
/* 192 */     return (!paramString.equals("..") && !paramString.equals(".") && containsAllowedCharactersOnly(paramString));
/*     */   }
/*     */   
/*     */   public static void validatePath(String... paramVarArgs) {
/* 196 */     if (paramVarArgs.length == 0) {
/* 197 */       throw new IllegalArgumentException("Path must have at least one element");
/*     */     }
/* 199 */     for (String str : paramVarArgs) {
/* 200 */       if (!isValidPathSegment(str)) {
/* 201 */         throw new IllegalArgumentException("Illegal segment " + str + " in path " + Arrays.toString(paramVarArgs));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void createDirectoriesSafe(Path paramPath) throws IOException {
/* 208 */     Files.createDirectories(Files.exists(paramPath, new java.nio.file.LinkOption[0]) ? paramPath.toRealPath(new java.nio.file.LinkOption[0]) : paramPath, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\FileUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */