/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongArgumentType
/*    */   implements ArgumentType<Long>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "123", "-123" });
/*    */   
/*    */   private final long minimum;
/*    */   private final long maximum;
/*    */   
/*    */   private LongArgumentType(long paramLong1, long paramLong2) {
/* 20 */     this.minimum = paramLong1;
/* 21 */     this.maximum = paramLong2;
/*    */   }
/*    */   
/*    */   public static LongArgumentType longArg() {
/* 25 */     return longArg(Long.MIN_VALUE);
/*    */   }
/*    */   
/*    */   public static LongArgumentType longArg(long paramLong) {
/* 29 */     return longArg(paramLong, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static LongArgumentType longArg(long paramLong1, long paramLong2) {
/* 33 */     return new LongArgumentType(paramLong1, paramLong2);
/*    */   }
/*    */   
/*    */   public static long getLong(CommandContext<?> paramCommandContext, String paramString) {
/* 37 */     return ((Long)paramCommandContext.getArgument(paramString, long.class)).longValue();
/*    */   }
/*    */   
/*    */   public long getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public long getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Long parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 50 */     int i = paramStringReader.getCursor();
/* 51 */     long l = paramStringReader.readLong();
/* 52 */     if (l < this.minimum) {
/* 53 */       paramStringReader.setCursor(i);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow().createWithContext(paramStringReader, Long.valueOf(l), Long.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (l > this.maximum) {
/* 57 */       paramStringReader.setCursor(i);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh().createWithContext(paramStringReader, Long.valueOf(l), Long.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Long.valueOf(l);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 65 */     if (this == paramObject) return true; 
/* 66 */     if (!(paramObject instanceof LongArgumentType)) return false;
/*    */     
/* 68 */     LongArgumentType longArgumentType = (LongArgumentType)paramObject;
/* 69 */     return (this.maximum == longArgumentType.maximum && this.minimum == longArgumentType.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return 31 * Long.hashCode(this.minimum) + Long.hashCode(this.maximum);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == Long.MIN_VALUE && this.maximum == Long.MAX_VALUE)
/* 80 */       return "longArg()"; 
/* 81 */     if (this.maximum == Long.MAX_VALUE) {
/* 82 */       return "longArg(" + this.minimum + ")";
/*    */     }
/* 84 */     return "longArg(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\LongArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */