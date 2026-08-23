/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextComponentTagVisitor
/*     */   implements TagVisitor {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int INLINE_LIST_THRESHOLD = 8;
/*     */   
/*     */   private static final int MAX_DEPTH = 64;
/*     */   private static final int MAX_LENGTH = 128;
/*  24 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_KEY = ChatFormatting.AQUA;
/*  25 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_STRING = ChatFormatting.GREEN;
/*  26 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER = ChatFormatting.GOLD;
/*  27 */   private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER_TYPE = ChatFormatting.RED;
/*     */   
/*  29 */   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
/*     */   
/*     */   private static final String LIST_OPEN = "[";
/*     */   private static final String LIST_CLOSE = "]";
/*     */   private static final String LIST_TYPE_SEPARATOR = ";";
/*     */   private static final String ELEMENT_SPACING = " ";
/*     */   private static final String STRUCT_OPEN = "{";
/*     */   private static final String STRUCT_CLOSE = "}";
/*     */   private static final String NEWLINE = "\n";
/*     */   private static final String NAME_VALUE_SEPARATOR = ": ";
/*  39 */   private static final String ELEMENT_SEPARATOR = String.valueOf(',');
/*  40 */   private static final String WRAPPED_ELEMENT_SEPARATOR = ELEMENT_SEPARATOR + "\n";
/*  41 */   private static final String SPACED_ELEMENT_SEPARATOR = ELEMENT_SEPARATOR + " ";
/*  42 */   private static final Component FOLDED = (Component)Component.literal("<...>").withStyle(ChatFormatting.GRAY);
/*  43 */   private static final Component BYTE_TYPE = (Component)Component.literal("b").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  44 */   private static final Component SHORT_TYPE = (Component)Component.literal("s").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  45 */   private static final Component INT_TYPE = (Component)Component.literal("I").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  46 */   private static final Component LONG_TYPE = (Component)Component.literal("L").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  47 */   private static final Component FLOAT_TYPE = (Component)Component.literal("f").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  48 */   private static final Component DOUBLE_TYPE = (Component)Component.literal("d").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  49 */   private static final Component BYTE_ARRAY_TYPE = (Component)Component.literal("B").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*     */   
/*     */   private final String indentation;
/*     */   
/*     */   private int indentDepth;
/*     */   private int depth;
/*  55 */   private final MutableComponent result = Component.empty();
/*     */   
/*     */   public TextComponentTagVisitor(String paramString) {
/*  58 */     this.indentation = paramString;
/*     */   }
/*     */   
/*     */   public Component visit(Tag paramTag) {
/*  62 */     paramTag.accept(this);
/*     */     
/*  64 */     return (Component)this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitString(StringTag paramStringTag) {
/*  69 */     String str1 = StringTag.quoteAndEscape(paramStringTag.value());
/*  70 */     String str2 = str1.substring(0, 1);
/*  71 */     MutableComponent mutableComponent = Component.literal(str1.substring(1, str1.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_STRING);
/*  72 */     this.result.append(str2).append((Component)mutableComponent).append(str2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByte(ByteTag paramByteTag) {
/*  77 */     this.result.append((Component)Component.literal(String.valueOf(paramByteTag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(BYTE_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitShort(ShortTag paramShortTag) {
/*  82 */     this.result.append((Component)Component.literal(String.valueOf(paramShortTag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(SHORT_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInt(IntTag paramIntTag) {
/*  87 */     this.result.append((Component)Component.literal(String.valueOf(paramIntTag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLong(LongTag paramLongTag) {
/*  92 */     this.result.append((Component)Component.literal(String.valueOf(paramLongTag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(LONG_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitFloat(FloatTag paramFloatTag) {
/*  97 */     this.result.append((Component)Component.literal(String.valueOf(paramFloatTag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(FLOAT_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitDouble(DoubleTag paramDoubleTag) {
/* 102 */     this.result.append((Component)Component.literal(String.valueOf(paramDoubleTag.value())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER)).append(DOUBLE_TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag paramByteArrayTag) {
/* 107 */     this.result.append("[").append(BYTE_ARRAY_TYPE).append(";");
/*     */     
/* 109 */     byte[] arrayOfByte = paramByteArrayTag.getAsByteArray();
/* 110 */     for (byte b = 0; b < arrayOfByte.length && b < ''; b++) {
/* 111 */       MutableComponent mutableComponent = Component.literal(String.valueOf(arrayOfByte[b])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */       
/* 113 */       this.result.append(" ").append((Component)mutableComponent).append(BYTE_ARRAY_TYPE);
/*     */       
/* 115 */       if (b != arrayOfByte.length - 1) {
/* 116 */         this.result.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 120 */     if (arrayOfByte.length > 128) {
/* 121 */       this.result.append(FOLDED);
/*     */     }
/*     */     
/* 124 */     this.result.append("]");
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag paramIntArrayTag) {
/* 129 */     this.result.append("[").append(INT_TYPE).append(";");
/*     */     
/* 131 */     int[] arrayOfInt = paramIntArrayTag.getAsIntArray();
/* 132 */     for (byte b = 0; b < arrayOfInt.length && b < ''; b++) {
/* 133 */       this.result.append(" ").append((Component)Component.literal(String.valueOf(arrayOfInt[b])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
/* 134 */       if (b != arrayOfInt.length - 1) {
/* 135 */         this.result.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 139 */     if (arrayOfInt.length > 128) {
/* 140 */       this.result.append(FOLDED);
/*     */     }
/*     */     
/* 143 */     this.result.append("]");
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag paramLongArrayTag) {
/* 148 */     this.result.append("[").append(LONG_TYPE).append(";");
/*     */     
/* 150 */     long[] arrayOfLong = paramLongArrayTag.getAsLongArray();
/* 151 */     for (byte b = 0; b < arrayOfLong.length && b < ''; b++) {
/* 152 */       MutableComponent mutableComponent = Component.literal(String.valueOf(arrayOfLong[b])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/* 153 */       this.result.append(" ").append((Component)mutableComponent).append(LONG_TYPE);
/* 154 */       if (b != arrayOfLong.length - 1) {
/* 155 */         this.result.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 159 */     if (arrayOfLong.length > 128) {
/* 160 */       this.result.append(FOLDED);
/*     */     }
/*     */     
/* 163 */     this.result.append("]");
/*     */   }
/*     */   
/*     */   private static boolean shouldWrapListElements(ListTag paramListTag) {
/* 167 */     if (paramListTag.size() >= 8) {
/* 168 */       return false;
/*     */     }
/* 170 */     for (Tag tag : paramListTag) {
/* 171 */       if (!(tag instanceof NumericTag)) {
/* 172 */         return true;
/*     */       }
/*     */     } 
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag paramListTag) {
/* 180 */     if (paramListTag.isEmpty()) {
/* 181 */       this.result.append("[]"); return;
/*     */     } 
/* 183 */     if (this.depth >= 64) {
/* 184 */       this.result.append("[").append(FOLDED).append("]");
/*     */       
/*     */       return;
/*     */     } 
/* 188 */     if (!shouldWrapListElements(paramListTag)) {
/* 189 */       this.result.append("[");
/* 190 */       for (byte b1 = 0; b1 < paramListTag.size(); b1++) {
/* 191 */         if (b1 != 0) {
/* 192 */           this.result.append(SPACED_ELEMENT_SEPARATOR);
/*     */         }
/* 194 */         appendSubTag(paramListTag.get(b1), false);
/*     */       } 
/* 196 */       this.result.append("]");
/*     */       
/*     */       return;
/*     */     } 
/* 200 */     this.result.append("[");
/* 201 */     if (!this.indentation.isEmpty()) {
/* 202 */       this.result.append("\n");
/*     */     }
/* 204 */     String str = Strings.repeat(this.indentation, this.indentDepth + 1);
/* 205 */     for (byte b = 0; b < paramListTag.size() && b < ''; b++) {
/* 206 */       this.result.append(str);
/* 207 */       appendSubTag(paramListTag.get(b), true);
/* 208 */       if (b != paramListTag.size() - 1) {
/* 209 */         this.result.append(this.indentation.isEmpty() ? SPACED_ELEMENT_SEPARATOR : WRAPPED_ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/* 212 */     if (paramListTag.size() > 128) {
/* 213 */       this.result.append(str).append(FOLDED);
/*     */     }
/* 215 */     if (!this.indentation.isEmpty()) {
/* 216 */       this.result.append("\n" + Strings.repeat(this.indentation, this.indentDepth));
/*     */     }
/* 218 */     this.result.append("]");
/*     */   }
/*     */   
/*     */   public void visitCompound(CompoundTag paramCompoundTag) {
/*     */     ArrayList<Comparable> arrayList;
/* 223 */     if (paramCompoundTag.isEmpty()) {
/* 224 */       this.result.append("{}"); return;
/*     */     } 
/* 226 */     if (this.depth >= 64) {
/* 227 */       this.result.append("{").append(FOLDED).append("}");
/*     */       
/*     */       return;
/*     */     } 
/* 231 */     this.result.append("{");
/*     */     
/* 233 */     Set<String> set = paramCompoundTag.keySet();
/* 234 */     if (LOGGER.isDebugEnabled()) {
/* 235 */       ArrayList<Comparable> arrayList1 = Lists.newArrayList(paramCompoundTag.keySet());
/* 236 */       Collections.sort(arrayList1);
/* 237 */       arrayList = arrayList1;
/*     */     } 
/*     */     
/* 240 */     if (!this.indentation.isEmpty()) {
/* 241 */       this.result.append("\n");
/*     */     }
/*     */     
/* 244 */     String str = Strings.repeat(this.indentation, this.indentDepth + 1);
/* 245 */     for (Iterator<Comparable> iterator = arrayList.iterator(); iterator.hasNext(); ) {
/* 246 */       String str1 = (String)iterator.next();
/* 247 */       this.result.append(str)
/* 248 */         .append(handleEscapePretty(str1))
/* 249 */         .append(": ");
/* 250 */       appendSubTag(paramCompoundTag.get(str1), true);
/*     */       
/* 252 */       if (iterator.hasNext()) {
/* 253 */         this.result.append(this.indentation.isEmpty() ? SPACED_ELEMENT_SEPARATOR : WRAPPED_ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/* 256 */     if (!this.indentation.isEmpty()) {
/* 257 */       this.result.append("\n" + Strings.repeat(this.indentation, this.indentDepth));
/*     */     }
/* 259 */     this.result.append("}");
/*     */   }
/*     */   
/*     */   private void appendSubTag(Tag paramTag, boolean paramBoolean) {
/* 263 */     if (paramBoolean) {
/* 264 */       this.indentDepth++;
/*     */     }
/* 266 */     this.depth++;
/*     */     try {
/* 268 */       paramTag.accept(this);
/*     */     } finally {
/* 270 */       if (paramBoolean) {
/* 271 */         this.indentDepth--;
/*     */       }
/* 273 */       this.depth--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Component handleEscapePretty(String paramString) {
/* 278 */     if (SIMPLE_VALUE.matcher(paramString).matches()) {
/* 279 */       return (Component)Component.literal(paramString).withStyle(SYNTAX_HIGHLIGHTING_KEY);
/*     */     }
/*     */     
/* 282 */     String str1 = StringTag.quoteAndEscape(paramString);
/* 283 */     String str2 = str1.substring(0, 1);
/* 284 */     MutableComponent mutableComponent = Component.literal(str1.substring(1, str1.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_KEY);
/* 285 */     return (Component)Component.literal(str2).append((Component)mutableComponent).append(str2);
/*     */   }
/*     */   
/*     */   public void visitEnd(EndTag paramEndTag) {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\TextComponentTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */