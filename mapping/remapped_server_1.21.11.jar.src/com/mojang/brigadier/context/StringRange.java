/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringRange
/*    */ {
/*    */   private final int start;
/*    */   private final int end;
/*    */   
/*    */   public StringRange(int paramInt1, int paramInt2) {
/* 15 */     this.start = paramInt1;
/* 16 */     this.end = paramInt2;
/*    */   }
/*    */   
/*    */   public static StringRange at(int paramInt) {
/* 20 */     return new StringRange(paramInt, paramInt);
/*    */   }
/*    */   
/*    */   public static StringRange between(int paramInt1, int paramInt2) {
/* 24 */     return new StringRange(paramInt1, paramInt2);
/*    */   }
/*    */   
/*    */   public static StringRange encompassing(StringRange paramStringRange1, StringRange paramStringRange2) {
/* 28 */     return new StringRange(Math.min(paramStringRange1.getStart(), paramStringRange2.getStart()), Math.max(paramStringRange1.getEnd(), paramStringRange2.getEnd()));
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 32 */     return this.start;
/*    */   }
/*    */   
/*    */   public int getEnd() {
/* 36 */     return this.end;
/*    */   }
/*    */   
/*    */   public String get(ImmutableStringReader paramImmutableStringReader) {
/* 40 */     return paramImmutableStringReader.getString().substring(this.start, this.end);
/*    */   }
/*    */   
/*    */   public String get(String paramString) {
/* 44 */     return paramString.substring(this.start, this.end);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 48 */     return (this.start == this.end);
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 52 */     return this.end - this.start;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 57 */     if (this == paramObject) {
/* 58 */       return true;
/*    */     }
/* 60 */     if (!(paramObject instanceof StringRange)) {
/* 61 */       return false;
/*    */     }
/* 63 */     StringRange stringRange = (StringRange)paramObject;
/* 64 */     return (this.start == stringRange.start && this.end == stringRange.end);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     return Objects.hash(new Object[] { Integer.valueOf(this.start), Integer.valueOf(this.end) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return "StringRange{start=" + this.start + ", end=" + this.end + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\StringRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */