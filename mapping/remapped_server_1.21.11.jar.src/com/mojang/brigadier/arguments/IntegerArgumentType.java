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
/*    */ public class IntegerArgumentType
/*    */   implements ArgumentType<Integer>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "123", "-123" });
/*    */   
/*    */   private final int minimum;
/*    */   private final int maximum;
/*    */   
/*    */   private IntegerArgumentType(int paramInt1, int paramInt2) {
/* 20 */     this.minimum = paramInt1;
/* 21 */     this.maximum = paramInt2;
/*    */   }
/*    */   
/*    */   public static IntegerArgumentType integer() {
/* 25 */     return integer(-2147483648);
/*    */   }
/*    */   
/*    */   public static IntegerArgumentType integer(int paramInt) {
/* 29 */     return integer(paramInt, 2147483647);
/*    */   }
/*    */   
/*    */   public static IntegerArgumentType integer(int paramInt1, int paramInt2) {
/* 33 */     return new IntegerArgumentType(paramInt1, paramInt2);
/*    */   }
/*    */   
/*    */   public static int getInteger(CommandContext<?> paramCommandContext, String paramString) {
/* 37 */     return ((Integer)paramCommandContext.getArgument(paramString, int.class)).intValue();
/*    */   }
/*    */   
/*    */   public int getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public int getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 50 */     int i = paramStringReader.getCursor();
/* 51 */     int j = paramStringReader.readInt();
/* 52 */     if (j < this.minimum) {
/* 53 */       paramStringReader.setCursor(i);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(paramStringReader, Integer.valueOf(j), Integer.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (j > this.maximum) {
/* 57 */       paramStringReader.setCursor(i);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(paramStringReader, Integer.valueOf(j), Integer.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Integer.valueOf(j);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 65 */     if (this == paramObject) return true; 
/* 66 */     if (!(paramObject instanceof IntegerArgumentType)) return false;
/*    */     
/* 68 */     IntegerArgumentType integerArgumentType = (IntegerArgumentType)paramObject;
/* 69 */     return (this.maximum == integerArgumentType.maximum && this.minimum == integerArgumentType.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return 31 * this.minimum + this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == Integer.MIN_VALUE && this.maximum == Integer.MAX_VALUE)
/* 80 */       return "integer()"; 
/* 81 */     if (this.maximum == Integer.MAX_VALUE) {
/* 82 */       return "integer(" + this.minimum + ")";
/*    */     }
/* 84 */     return "integer(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\IntegerArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */