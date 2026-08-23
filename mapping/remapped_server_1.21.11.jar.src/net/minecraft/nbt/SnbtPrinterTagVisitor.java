/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class SnbtPrinterTagVisitor implements TagVisitor {
/*     */   static {
/*  21 */     KEY_ORDER = (Map<String, List<String>>)Util.make(Maps.newHashMap(), paramHashMap -> {
/*     */           paramHashMap.put("{}", Lists.newArrayList((Object[])new String[] { "DataVersion", "author", "size", "data", "entities", "palette", "palettes" }));
/*     */           paramHashMap.put("{}.data.[].{}", Lists.newArrayList((Object[])new String[] { "pos", "state", "nbt" }));
/*     */           paramHashMap.put("{}.entities.[].{}", Lists.newArrayList((Object[])new String[] { "blockPos", "pos" }));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Map<String, List<String>> KEY_ORDER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final Set<String> NO_INDENTATION = Sets.newHashSet((Object[])new String[] { "{}.size.[]", "{}.data.[].{}", "{}.palette.[].{}", "{}.entities.[].{}" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
/*     */   
/*  51 */   private static final String NAME_VALUE_SEPARATOR = String.valueOf(':');
/*  52 */   private static final String ELEMENT_SEPARATOR = String.valueOf(',');
/*     */   
/*     */   private static final String LIST_OPEN = "[";
/*     */   
/*     */   private static final String LIST_CLOSE = "]";
/*     */   private static final String LIST_TYPE_SEPARATOR = ";";
/*     */   private static final String ELEMENT_SPACING = " ";
/*     */   private static final String STRUCT_OPEN = "{";
/*     */   private static final String STRUCT_CLOSE = "}";
/*     */   private static final String NEWLINE = "\n";
/*     */   private final String indentation;
/*     */   private final int depth;
/*     */   private final List<String> path;
/*  65 */   private String result = "";
/*     */   
/*     */   public SnbtPrinterTagVisitor() {
/*  68 */     this("    ", 0, Lists.newArrayList());
/*     */   }
/*     */   
/*     */   public SnbtPrinterTagVisitor(String paramString, int paramInt, List<String> paramList) {
/*  72 */     this.indentation = paramString;
/*  73 */     this.depth = paramInt;
/*  74 */     this.path = paramList;
/*     */   }
/*     */   
/*     */   public String visit(Tag paramTag) {
/*  78 */     paramTag.accept(this);
/*     */     
/*  80 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitString(StringTag paramStringTag) {
/*  85 */     this.result = StringTag.quoteAndEscape(paramStringTag.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByte(ByteTag paramByteTag) {
/*  90 */     this.result = "" + paramByteTag.value() + "b";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitShort(ShortTag paramShortTag) {
/*  95 */     this.result = "" + paramShortTag.value() + "s";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInt(IntTag paramIntTag) {
/* 100 */     this.result = String.valueOf(paramIntTag.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLong(LongTag paramLongTag) {
/* 105 */     this.result = "" + paramLongTag.value() + "L";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitFloat(FloatTag paramFloatTag) {
/* 110 */     this.result = "" + paramFloatTag.value() + "f";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitDouble(DoubleTag paramDoubleTag) {
/* 115 */     this.result = "" + paramDoubleTag.value() + "d";
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag paramByteArrayTag) {
/* 120 */     StringBuilder stringBuilder = (new StringBuilder("[")).append("B").append(";");
/*     */     
/* 122 */     byte[] arrayOfByte = paramByteArrayTag.getAsByteArray();
/* 123 */     for (byte b = 0; b < arrayOfByte.length; b++) {
/* 124 */       stringBuilder.append(" ").append(arrayOfByte[b]).append("B");
/*     */       
/* 126 */       if (b != arrayOfByte.length - 1) {
/* 127 */         stringBuilder.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 131 */     stringBuilder.append("]");
/* 132 */     this.result = stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag paramIntArrayTag) {
/* 137 */     StringBuilder stringBuilder = (new StringBuilder("[")).append("I").append(";");
/*     */     
/* 139 */     int[] arrayOfInt = paramIntArrayTag.getAsIntArray();
/* 140 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 141 */       stringBuilder.append(" ").append(arrayOfInt[b]);
/* 142 */       if (b != arrayOfInt.length - 1) {
/* 143 */         stringBuilder.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 147 */     stringBuilder.append("]");
/* 148 */     this.result = stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag paramLongArrayTag) {
/* 153 */     String str = "L";
/* 154 */     StringBuilder stringBuilder = (new StringBuilder("[")).append("L").append(";");
/*     */     
/* 156 */     long[] arrayOfLong = paramLongArrayTag.getAsLongArray();
/* 157 */     for (byte b = 0; b < arrayOfLong.length; b++) {
/* 158 */       stringBuilder.append(" ").append(arrayOfLong[b]).append("L");
/* 159 */       if (b != arrayOfLong.length - 1) {
/* 160 */         stringBuilder.append(ELEMENT_SEPARATOR);
/*     */       }
/*     */     } 
/*     */     
/* 164 */     stringBuilder.append("]");
/* 165 */     this.result = stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag paramListTag) {
/* 170 */     if (paramListTag.isEmpty()) {
/* 171 */       this.result = "[]";
/*     */       
/*     */       return;
/*     */     } 
/* 175 */     StringBuilder stringBuilder = new StringBuilder("[");
/* 176 */     pushPath("[]");
/*     */     
/* 178 */     String str = NO_INDENTATION.contains(pathString()) ? "" : this.indentation;
/* 179 */     if (!str.isEmpty()) {
/* 180 */       stringBuilder.append("\n");
/*     */     }
/*     */     
/* 183 */     for (byte b = 0; b < paramListTag.size(); b++) {
/* 184 */       stringBuilder.append(Strings.repeat(str, this.depth + 1));
/* 185 */       stringBuilder.append((new SnbtPrinterTagVisitor(str, this.depth + 1, this.path)).visit(paramListTag.get(b)));
/* 186 */       if (b != paramListTag.size() - 1) {
/* 187 */         stringBuilder.append(ELEMENT_SEPARATOR).append(str.isEmpty() ? " " : "\n");
/*     */       }
/*     */     } 
/* 190 */     if (!str.isEmpty()) {
/* 191 */       stringBuilder.append("\n").append(Strings.repeat(str, this.depth));
/*     */     }
/* 193 */     stringBuilder.append("]");
/*     */     
/* 195 */     this.result = stringBuilder.toString();
/* 196 */     popPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCompound(CompoundTag paramCompoundTag) {
/* 201 */     if (paramCompoundTag.isEmpty()) {
/* 202 */       this.result = "{}";
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 207 */     StringBuilder stringBuilder = new StringBuilder("{");
/* 208 */     pushPath("{}");
/*     */     
/* 210 */     String str = NO_INDENTATION.contains(pathString()) ? "" : this.indentation;
/* 211 */     if (!str.isEmpty()) {
/* 212 */       stringBuilder.append("\n");
/*     */     }
/*     */     
/* 215 */     List<String> list = getKeys(paramCompoundTag);
/* 216 */     for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
/* 217 */       String str1 = iterator.next();
/* 218 */       Tag tag = paramCompoundTag.get(str1);
/*     */       
/* 220 */       pushPath(str1);
/* 221 */       stringBuilder.append(Strings.repeat(str, this.depth + 1))
/* 222 */         .append(handleEscapePretty(str1))
/* 223 */         .append(NAME_VALUE_SEPARATOR)
/* 224 */         .append(" ")
/* 225 */         .append((new SnbtPrinterTagVisitor(str, this.depth + 1, this.path)).visit(tag));
/*     */       
/* 227 */       popPath();
/*     */       
/* 229 */       if (iterator.hasNext()) {
/* 230 */         stringBuilder.append(ELEMENT_SEPARATOR).append(str.isEmpty() ? " " : "\n");
/*     */       }
/*     */     } 
/* 233 */     if (!str.isEmpty()) {
/* 234 */       stringBuilder.append("\n").append(Strings.repeat(str, this.depth));
/*     */     }
/* 236 */     stringBuilder.append("}");
/* 237 */     this.result = stringBuilder.toString();
/* 238 */     popPath();
/*     */   }
/*     */   
/*     */   private void popPath() {
/* 242 */     this.path.remove(this.path.size() - 1);
/*     */   }
/*     */   
/*     */   private void pushPath(String paramString) {
/* 246 */     this.path.add(paramString);
/*     */   }
/*     */   
/*     */   protected List<String> getKeys(CompoundTag paramCompoundTag) {
/* 250 */     HashSet<? extends String> hashSet = Sets.newHashSet(paramCompoundTag.keySet());
/* 251 */     ArrayList<String> arrayList = Lists.newArrayList();
/*     */     
/* 253 */     List list = KEY_ORDER.get(pathString());
/* 254 */     if (list != null) {
/* 255 */       for (String str : list) {
/* 256 */         if (hashSet.remove(str)) {
/* 257 */           arrayList.add(str);
/*     */         }
/*     */       } 
/* 260 */       if (!hashSet.isEmpty()) {
/* 261 */         Objects.requireNonNull(arrayList); hashSet.stream().sorted().forEach(arrayList::add);
/*     */       } 
/*     */     } else {
/* 264 */       arrayList.addAll(hashSet);
/* 265 */       Collections.sort(arrayList);
/*     */     } 
/* 267 */     return arrayList;
/*     */   }
/*     */   
/*     */   public String pathString() {
/* 271 */     return String.join(".", (Iterable)this.path);
/*     */   }
/*     */   
/*     */   protected static String handleEscapePretty(String paramString) {
/* 275 */     if (SIMPLE_VALUE.matcher(paramString).matches()) {
/* 276 */       return paramString;
/*     */     }
/*     */     
/* 279 */     return StringTag.quoteAndEscape(paramString);
/*     */   }
/*     */   
/*     */   public void visitEnd(EndTag paramEndTag) {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\SnbtPrinterTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */