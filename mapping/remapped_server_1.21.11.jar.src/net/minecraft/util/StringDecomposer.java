/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class StringDecomposer
/*     */ {
/*     */   private static final char REPLACEMENT_CHAR = '�';
/*  11 */   private static final Optional<Object> STOP_ITERATION = Optional.of(Unit.INSTANCE);
/*     */   
/*     */   private static boolean feedChar(Style paramStyle, FormattedCharSink paramFormattedCharSink, int paramInt, char paramChar) {
/*  14 */     if (Character.isSurrogate(paramChar)) {
/*  15 */       return paramFormattedCharSink.accept(paramInt, paramStyle, 65533);
/*     */     }
/*  17 */     return paramFormattedCharSink.accept(paramInt, paramStyle, paramChar);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean iterate(String paramString, Style paramStyle, FormattedCharSink paramFormattedCharSink) {
/*  22 */     int i = paramString.length();
/*  23 */     for (byte b = 0; b < i; b++) {
/*  24 */       char c = paramString.charAt(b);
/*  25 */       if (Character.isHighSurrogate(c)) {
/*  26 */         if (b + 1 >= i) {
/*  27 */           if (!paramFormattedCharSink.accept(b, paramStyle, 65533)) {
/*  28 */             return false;
/*     */           }
/*     */           break;
/*     */         } 
/*  32 */         char c1 = paramString.charAt(b + 1);
/*  33 */         if (Character.isLowSurrogate(c1)) {
/*  34 */           if (!paramFormattedCharSink.accept(b, paramStyle, Character.toCodePoint(c, c1))) {
/*  35 */             return false;
/*     */           }
/*  37 */           b++;
/*     */         }
/*  39 */         else if (!paramFormattedCharSink.accept(b, paramStyle, 65533)) {
/*  40 */           return false;
/*     */         }
/*     */       
/*  43 */       } else if (!feedChar(paramStyle, paramFormattedCharSink, b, c)) {
/*  44 */         return false;
/*     */       } 
/*     */     } 
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean iterateBackwards(String paramString, Style paramStyle, FormattedCharSink paramFormattedCharSink) {
/*  51 */     int i = paramString.length();
/*  52 */     for (int j = i - 1; j >= 0; j--) {
/*  53 */       char c = paramString.charAt(j);
/*  54 */       if (Character.isLowSurrogate(c)) {
/*  55 */         if (j - 1 < 0) {
/*  56 */           if (!paramFormattedCharSink.accept(0, paramStyle, 65533)) {
/*  57 */             return false;
/*     */           }
/*     */           break;
/*     */         } 
/*  61 */         char c1 = paramString.charAt(j - 1);
/*  62 */         if (Character.isHighSurrogate(c1)) {
/*  63 */           j--;
/*  64 */           if (!paramFormattedCharSink.accept(j, paramStyle, Character.toCodePoint(c1, c))) {
/*  65 */             return false;
/*     */           }
/*     */         }
/*  68 */         else if (!paramFormattedCharSink.accept(j, paramStyle, 65533)) {
/*  69 */           return false;
/*     */         }
/*     */       
/*  72 */       } else if (!feedChar(paramStyle, paramFormattedCharSink, j, c)) {
/*  73 */         return false;
/*     */       } 
/*     */     } 
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean iterateFormatted(String paramString, Style paramStyle, FormattedCharSink paramFormattedCharSink) {
/*  80 */     return iterateFormatted(paramString, 0, paramStyle, paramFormattedCharSink);
/*     */   }
/*     */   
/*     */   public static boolean iterateFormatted(String paramString, int paramInt, Style paramStyle, FormattedCharSink paramFormattedCharSink) {
/*  84 */     return iterateFormatted(paramString, paramInt, paramStyle, paramStyle, paramFormattedCharSink);
/*     */   }
/*     */   
/*     */   public static boolean iterateFormatted(String paramString, int paramInt, Style paramStyle1, Style paramStyle2, FormattedCharSink paramFormattedCharSink) {
/*  88 */     int i = paramString.length();
/*  89 */     Style style = paramStyle1;
/*  90 */     for (int j = paramInt; j < i; j++) {
/*  91 */       char c = paramString.charAt(j);
/*  92 */       if (c == '§') {
/*  93 */         if (j + 1 >= i) {
/*     */           break;
/*     */         }
/*  96 */         char c1 = paramString.charAt(j + 1);
/*  97 */         ChatFormatting chatFormatting = ChatFormatting.getByCode(c1);
/*  98 */         if (chatFormatting != null) {
/*  99 */           style = (chatFormatting == ChatFormatting.RESET) ? paramStyle2 : style.applyLegacyFormat(chatFormatting);
/*     */         }
/* 101 */         j++;
/* 102 */       } else if (Character.isHighSurrogate(c)) {
/* 103 */         if (j + 1 >= i) {
/* 104 */           if (!paramFormattedCharSink.accept(j, style, 65533)) {
/* 105 */             return false;
/*     */           }
/*     */           break;
/*     */         } 
/* 109 */         char c1 = paramString.charAt(j + 1);
/* 110 */         if (Character.isLowSurrogate(c1)) {
/* 111 */           if (!paramFormattedCharSink.accept(j, style, Character.toCodePoint(c, c1))) {
/* 112 */             return false;
/*     */           }
/* 114 */           j++;
/*     */         }
/* 116 */         else if (!paramFormattedCharSink.accept(j, style, 65533)) {
/* 117 */           return false;
/*     */         }
/*     */       
/* 120 */       } else if (!feedChar(style, paramFormattedCharSink, j, c)) {
/* 121 */         return false;
/*     */       } 
/*     */     } 
/* 124 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean iterateFormatted(FormattedText paramFormattedText, Style paramStyle, FormattedCharSink paramFormattedCharSink) {
/* 128 */     return paramFormattedText.visit((paramStyle, paramString) -> iterateFormatted(paramString, 0, paramStyle, paramFormattedCharSink) ? Optional.empty() : STOP_ITERATION, paramStyle).isEmpty();
/*     */   }
/*     */   
/*     */   public static String filterBrokenSurrogates(String paramString) {
/* 132 */     StringBuilder stringBuilder = new StringBuilder();
/* 133 */     iterate(paramString, Style.EMPTY, (paramInt1, paramStyle, paramInt2) -> {
/*     */           paramStringBuilder.appendCodePoint(paramInt2);
/*     */           return true;
/*     */         });
/* 137 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getPlainText(FormattedText paramFormattedText) {
/* 141 */     StringBuilder stringBuilder = new StringBuilder();
/* 142 */     iterateFormatted(paramFormattedText, Style.EMPTY, (paramInt1, paramStyle, paramInt2) -> {
/*     */           paramStringBuilder.appendCodePoint(paramInt2);
/*     */           return true;
/*     */         });
/* 146 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\StringDecomposer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */