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
/*    */ public class DoubleArgumentType
/*    */   implements ArgumentType<Double>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "1.2", ".5", "-1", "-.5", "-1234.56" });
/*    */   
/*    */   private final double minimum;
/*    */   private final double maximum;
/*    */   
/*    */   private DoubleArgumentType(double paramDouble1, double paramDouble2) {
/* 20 */     this.minimum = paramDouble1;
/* 21 */     this.maximum = paramDouble2;
/*    */   }
/*    */   
/*    */   public static DoubleArgumentType doubleArg() {
/* 25 */     return doubleArg(-1.7976931348623157E308D);
/*    */   }
/*    */   
/*    */   public static DoubleArgumentType doubleArg(double paramDouble) {
/* 29 */     return doubleArg(paramDouble, Double.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static DoubleArgumentType doubleArg(double paramDouble1, double paramDouble2) {
/* 33 */     return new DoubleArgumentType(paramDouble1, paramDouble2);
/*    */   }
/*    */   
/*    */   public static double getDouble(CommandContext<?> paramCommandContext, String paramString) {
/* 37 */     return ((Double)paramCommandContext.getArgument(paramString, Double.class)).doubleValue();
/*    */   }
/*    */   
/*    */   public double getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public double getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 50 */     int i = paramStringReader.getCursor();
/* 51 */     double d = paramStringReader.readDouble();
/* 52 */     if (d < this.minimum) {
/* 53 */       paramStringReader.setCursor(i);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooLow().createWithContext(paramStringReader, Double.valueOf(d), Double.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (d > this.maximum) {
/* 57 */       paramStringReader.setCursor(i);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooHigh().createWithContext(paramStringReader, Double.valueOf(d), Double.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Double.valueOf(d);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 65 */     if (this == paramObject) return true; 
/* 66 */     if (!(paramObject instanceof DoubleArgumentType)) return false;
/*    */     
/* 68 */     DoubleArgumentType doubleArgumentType = (DoubleArgumentType)paramObject;
/* 69 */     return (this.maximum == doubleArgumentType.maximum && this.minimum == doubleArgumentType.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return (int)(31.0D * this.minimum + this.maximum);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == -1.7976931348623157E308D && this.maximum == Double.MAX_VALUE)
/* 80 */       return "double()"; 
/* 81 */     if (this.maximum == Double.MAX_VALUE) {
/* 82 */       return "double(" + this.minimum + ")";
/*    */     }
/* 84 */     return "double(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\DoubleArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */