/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringTagVisitor
/*     */   implements TagVisitor
/*     */ {
/*  13 */   private static final Pattern UNQUOTED_KEY_MATCH = Pattern.compile("[A-Za-z._]+[A-Za-z0-9._+-]*");
/*     */   
/*  15 */   private final StringBuilder builder = new StringBuilder();
/*     */   
/*     */   public String build() {
/*  18 */     return this.builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitString(StringTag paramStringTag) {
/*  23 */     this.builder.append(StringTag.quoteAndEscape(paramStringTag.value()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByte(ByteTag paramByteTag) {
/*  28 */     this.builder.append(paramByteTag.value()).append('b');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitShort(ShortTag paramShortTag) {
/*  33 */     this.builder.append(paramShortTag.value()).append('s');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInt(IntTag paramIntTag) {
/*  38 */     this.builder.append(paramIntTag.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLong(LongTag paramLongTag) {
/*  43 */     this.builder.append(paramLongTag.value()).append('L');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitFloat(FloatTag paramFloatTag) {
/*  48 */     this.builder.append(paramFloatTag.value()).append('f');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitDouble(DoubleTag paramDoubleTag) {
/*  53 */     this.builder.append(paramDoubleTag.value()).append('d');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag paramByteArrayTag) {
/*  58 */     this.builder.append("[B;");
/*  59 */     byte[] arrayOfByte = paramByteArrayTag.getAsByteArray();
/*  60 */     for (byte b = 0; b < arrayOfByte.length; b++) {
/*  61 */       if (b != 0) {
/*  62 */         this.builder.append(',');
/*     */       }
/*  64 */       this.builder.append(arrayOfByte[b]).append('B');
/*     */     } 
/*  66 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag paramIntArrayTag) {
/*  71 */     this.builder.append("[I;");
/*  72 */     int[] arrayOfInt = paramIntArrayTag.getAsIntArray();
/*  73 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*  74 */       if (b != 0) {
/*  75 */         this.builder.append(',');
/*     */       }
/*  77 */       this.builder.append(arrayOfInt[b]);
/*     */     } 
/*  79 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag paramLongArrayTag) {
/*  84 */     this.builder.append("[L;");
/*  85 */     long[] arrayOfLong = paramLongArrayTag.getAsLongArray();
/*  86 */     for (byte b = 0; b < arrayOfLong.length; b++) {
/*  87 */       if (b != 0) {
/*  88 */         this.builder.append(',');
/*     */       }
/*  90 */       this.builder.append(arrayOfLong[b]).append('L');
/*     */     } 
/*  92 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag paramListTag) {
/*  97 */     this.builder.append('[');
/*  98 */     for (byte b = 0; b < paramListTag.size(); b++) {
/*  99 */       if (b != 0) {
/* 100 */         this.builder.append(',');
/*     */       }
/* 102 */       paramListTag.get(b).accept(this);
/*     */     } 
/* 104 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCompound(CompoundTag paramCompoundTag) {
/* 109 */     this.builder.append('{');
/*     */     
/* 111 */     ArrayList<Map.Entry<String, Tag>> arrayList = new ArrayList<>(paramCompoundTag.entrySet());
/* 112 */     arrayList.sort((Comparator)Map.Entry.comparingByKey());
/* 113 */     for (byte b = 0; b < arrayList.size(); b++) {
/* 114 */       Map.Entry entry = arrayList.get(b);
/* 115 */       if (b != 0) {
/* 116 */         this.builder.append(',');
/*     */       }
/* 118 */       handleKeyEscape((String)entry.getKey());
/* 119 */       this.builder.append(':');
/* 120 */       ((Tag)entry.getValue()).accept(this);
/*     */     } 
/*     */     
/* 123 */     this.builder.append('}');
/*     */   }
/*     */   
/*     */   private void handleKeyEscape(String paramString) {
/* 127 */     if (!paramString.equalsIgnoreCase("true") && !paramString.equalsIgnoreCase("false") && UNQUOTED_KEY_MATCH.matcher(paramString).matches()) {
/* 128 */       this.builder.append(paramString);
/*     */     } else {
/* 130 */       StringTag.quoteAndEscape(paramString, this.builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd(EndTag paramEndTag) {
/* 136 */     this.builder.append("END");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\StringTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */