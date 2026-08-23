/*     */ package com.mojang.brigadier;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringReader
/*     */   implements ImmutableStringReader
/*     */ {
/*     */   private static final char SYNTAX_ESCAPE = '\\';
/*     */   private static final char SYNTAX_DOUBLE_QUOTE = '"';
/*     */   private static final char SYNTAX_SINGLE_QUOTE = '\'';
/*     */   private final String string;
/*     */   private int cursor;
/*     */   
/*     */   public StringReader(StringReader paramStringReader) {
/*  17 */     this.string = paramStringReader.string;
/*  18 */     this.cursor = paramStringReader.cursor;
/*     */   }
/*     */   
/*     */   public StringReader(String paramString) {
/*  22 */     this.string = paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/*  27 */     return this.string;
/*     */   }
/*     */   
/*     */   public void setCursor(int paramInt) {
/*  31 */     this.cursor = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingLength() {
/*  36 */     return this.string.length() - this.cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalLength() {
/*  41 */     return this.string.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCursor() {
/*  46 */     return this.cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRead() {
/*  51 */     return this.string.substring(0, this.cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRemaining() {
/*  56 */     return this.string.substring(this.cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(int paramInt) {
/*  61 */     return (this.cursor + paramInt <= this.string.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead() {
/*  66 */     return canRead(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public char peek() {
/*  71 */     return this.string.charAt(this.cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public char peek(int paramInt) {
/*  76 */     return this.string.charAt(this.cursor + paramInt);
/*     */   }
/*     */   
/*     */   public char read() {
/*  80 */     return this.string.charAt(this.cursor++);
/*     */   }
/*     */   
/*     */   public void skip() {
/*  84 */     this.cursor++;
/*     */   }
/*     */   
/*     */   public static boolean isAllowedNumber(char paramChar) {
/*  88 */     return ((paramChar >= '0' && paramChar <= '9') || paramChar == '.' || paramChar == '-');
/*     */   }
/*     */   
/*     */   public static boolean isQuotedStringStart(char paramChar) {
/*  92 */     return (paramChar == '"' || paramChar == '\'');
/*     */   }
/*     */   
/*     */   public void skipWhitespace() {
/*  96 */     while (canRead() && Character.isWhitespace(peek())) {
/*  97 */       skip();
/*     */     }
/*     */   }
/*     */   
/*     */   public int readInt() throws CommandSyntaxException {
/* 102 */     int i = this.cursor;
/* 103 */     while (canRead() && isAllowedNumber(peek())) {
/* 104 */       skip();
/*     */     }
/* 106 */     String str = this.string.substring(i, this.cursor);
/* 107 */     if (str.isEmpty()) {
/* 108 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
/*     */     }
/*     */     try {
/* 111 */       return Integer.parseInt(str);
/* 112 */     } catch (NumberFormatException numberFormatException) {
/* 113 */       this.cursor = i;
/* 114 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public long readLong() throws CommandSyntaxException {
/* 119 */     int i = this.cursor;
/* 120 */     while (canRead() && isAllowedNumber(peek())) {
/* 121 */       skip();
/*     */     }
/* 123 */     String str = this.string.substring(i, this.cursor);
/* 124 */     if (str.isEmpty()) {
/* 125 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
/*     */     }
/*     */     try {
/* 128 */       return Long.parseLong(str);
/* 129 */     } catch (NumberFormatException numberFormatException) {
/* 130 */       this.cursor = i;
/* 131 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public double readDouble() throws CommandSyntaxException {
/* 136 */     int i = this.cursor;
/* 137 */     while (canRead() && isAllowedNumber(peek())) {
/* 138 */       skip();
/*     */     }
/* 140 */     String str = this.string.substring(i, this.cursor);
/* 141 */     if (str.isEmpty()) {
/* 142 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
/*     */     }
/*     */     try {
/* 145 */       return Double.parseDouble(str);
/* 146 */     } catch (NumberFormatException numberFormatException) {
/* 147 */       this.cursor = i;
/* 148 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float readFloat() throws CommandSyntaxException {
/* 153 */     int i = this.cursor;
/* 154 */     while (canRead() && isAllowedNumber(peek())) {
/* 155 */       skip();
/*     */     }
/* 157 */     String str = this.string.substring(i, this.cursor);
/* 158 */     if (str.isEmpty()) {
/* 159 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
/*     */     }
/*     */     try {
/* 162 */       return Float.parseFloat(str);
/* 163 */     } catch (NumberFormatException numberFormatException) {
/* 164 */       this.cursor = i;
/* 165 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAllowedInUnquotedString(char paramChar) {
/* 170 */     return ((paramChar >= '0' && paramChar <= '9') || (paramChar >= 'A' && paramChar <= 'Z') || (paramChar >= 'a' && paramChar <= 'z') || paramChar == '_' || paramChar == '-' || paramChar == '.' || paramChar == '+');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readUnquotedString() {
/* 178 */     int i = this.cursor;
/* 179 */     while (canRead() && isAllowedInUnquotedString(peek())) {
/* 180 */       skip();
/*     */     }
/* 182 */     return this.string.substring(i, this.cursor);
/*     */   }
/*     */   
/*     */   public String readQuotedString() throws CommandSyntaxException {
/* 186 */     if (!canRead()) {
/* 187 */       return "";
/*     */     }
/* 189 */     char c = peek();
/* 190 */     if (!isQuotedStringStart(c)) {
/* 191 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
/*     */     }
/* 193 */     skip();
/* 194 */     return readStringUntil(c);
/*     */   }
/*     */   
/*     */   public String readStringUntil(char paramChar) throws CommandSyntaxException {
/* 198 */     StringBuilder stringBuilder = new StringBuilder();
/* 199 */     boolean bool = false;
/* 200 */     while (canRead()) {
/* 201 */       char c = read();
/* 202 */       if (bool) {
/* 203 */         if (c == paramChar || c == '\\') {
/* 204 */           stringBuilder.append(c);
/* 205 */           bool = false; continue;
/*     */         } 
/* 207 */         setCursor(getCursor() - 1);
/* 208 */         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
/*     */       } 
/* 210 */       if (c == '\\') {
/* 211 */         bool = true; continue;
/* 212 */       }  if (c == paramChar) {
/* 213 */         return stringBuilder.toString();
/*     */       }
/* 215 */       stringBuilder.append(c);
/*     */     } 
/*     */ 
/*     */     
/* 219 */     throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
/*     */   }
/*     */   
/*     */   public String readString() throws CommandSyntaxException {
/* 223 */     if (!canRead()) {
/* 224 */       return "";
/*     */     }
/* 226 */     char c = peek();
/* 227 */     if (isQuotedStringStart(c)) {
/* 228 */       skip();
/* 229 */       return readStringUntil(c);
/*     */     } 
/* 231 */     return readUnquotedString();
/*     */   }
/*     */   
/*     */   public boolean readBoolean() throws CommandSyntaxException {
/* 235 */     int i = this.cursor;
/* 236 */     String str = readString();
/* 237 */     if (str.isEmpty()) {
/* 238 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
/*     */     }
/*     */     
/* 241 */     if (str.equals("true"))
/* 242 */       return true; 
/* 243 */     if (str.equals("false")) {
/* 244 */       return false;
/*     */     }
/* 246 */     this.cursor = i;
/* 247 */     throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void expect(char paramChar) throws CommandSyntaxException {
/* 252 */     if (!canRead() || peek() != paramChar) {
/* 253 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(paramChar));
/*     */     }
/* 255 */     skip();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\StringReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */