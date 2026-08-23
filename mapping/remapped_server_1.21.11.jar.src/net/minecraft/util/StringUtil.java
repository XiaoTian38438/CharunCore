/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtil
/*     */ {
/*  12 */   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
/*  13 */   private static final Pattern LINE_PATTERN = Pattern.compile("\\r\\n|\\v");
/*  14 */   private static final Pattern LINE_END_PATTERN = Pattern.compile("(?:\\r\\n|\\v)$");
/*     */   
/*     */   public static String formatTickDuration(int paramInt, float paramFloat) {
/*  17 */     int i = Mth.floor(paramInt / paramFloat);
/*  18 */     int j = i / 60;
/*  19 */     i %= 60;
/*  20 */     int k = j / 60;
/*  21 */     j %= 60;
/*     */     
/*  23 */     if (k > 0) {
/*  24 */       return String.format(Locale.ROOT, "%02d:%02d:%02d", new Object[] { Integer.valueOf(k), Integer.valueOf(j), Integer.valueOf(i) });
/*     */     }
/*  26 */     return String.format(Locale.ROOT, "%02d:%02d", new Object[] { Integer.valueOf(j), Integer.valueOf(i) });
/*     */   }
/*     */   
/*     */   public static String stripColor(String paramString) {
/*  30 */     return STRIP_COLOR_PATTERN.matcher(paramString).replaceAll("");
/*     */   }
/*     */   
/*     */   public static boolean isNullOrEmpty(String paramString) {
/*  34 */     return StringUtils.isEmpty(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String truncateStringIfNecessary(String paramString, int paramInt, boolean paramBoolean) {
/*  43 */     if (paramString.length() <= paramInt) {
/*  44 */       return paramString;
/*     */     }
/*     */     
/*  47 */     if (paramBoolean && paramInt > 3) {
/*  48 */       return paramString.substring(0, paramInt - 3) + "...";
/*     */     }
/*  50 */     return paramString.substring(0, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int lineCount(String paramString) {
/*  55 */     if (paramString.isEmpty()) {
/*  56 */       return 0;
/*     */     }
/*     */     
/*  59 */     Matcher matcher = LINE_PATTERN.matcher(paramString);
/*     */     
/*  61 */     byte b = 1;
/*  62 */     while (matcher.find()) {
/*  63 */       b++;
/*     */     }
/*  65 */     return b;
/*     */   }
/*     */   
/*     */   public static boolean endsWithNewLine(String paramString) {
/*  69 */     return LINE_END_PATTERN.matcher(paramString).find();
/*     */   }
/*     */   
/*     */   public static String trimChatMessage(String paramString) {
/*  73 */     return truncateStringIfNecessary(paramString, 256, false);
/*     */   }
/*     */   
/*     */   public static boolean isAllowedChatCharacter(int paramInt) {
/*  77 */     return (paramInt != 167 && paramInt >= 32 && paramInt != 127);
/*     */   }
/*     */   
/*     */   public static boolean isValidPlayerName(String paramString) {
/*  81 */     if (paramString.length() > 16) {
/*  82 */       return false;
/*     */     }
/*  84 */     return paramString.chars().filter(paramInt -> (paramInt <= 32 || paramInt >= 127)).findAny().isEmpty();
/*     */   }
/*     */   
/*     */   public static String filterText(String paramString) {
/*  88 */     return filterText(paramString, false);
/*     */   }
/*     */   
/*     */   public static String filterText(String paramString, boolean paramBoolean) {
/*  92 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/*  94 */     for (char c : paramString.toCharArray()) {
/*  95 */       if (isAllowedChatCharacter(c)) {
/*  96 */         stringBuilder.append(c);
/*  97 */       } else if (paramBoolean && c == '\n') {
/*  98 */         stringBuilder.append(c);
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isWhitespace(int paramInt) {
/* 107 */     return (Character.isWhitespace(paramInt) || Character.isSpaceChar(paramInt));
/*     */   }
/*     */   
/*     */   public static boolean isBlank(String paramString) {
/* 111 */     if (paramString == null || paramString.isEmpty()) {
/* 112 */       return true;
/*     */     }
/* 114 */     return paramString.chars().allMatch(StringUtil::isWhitespace);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\StringUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */