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
/*    */ public class FloatArgumentType
/*    */   implements ArgumentType<Float>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "1.2", ".5", "-1", "-.5", "-1234.56" });
/*    */   
/*    */   private final float minimum;
/*    */   private final float maximum;
/*    */   
/*    */   private FloatArgumentType(float paramFloat1, float paramFloat2) {
/* 20 */     this.minimum = paramFloat1;
/* 21 */     this.maximum = paramFloat2;
/*    */   }
/*    */   
/*    */   public static FloatArgumentType floatArg() {
/* 25 */     return floatArg(-3.4028235E38F);
/*    */   }
/*    */   
/*    */   public static FloatArgumentType floatArg(float paramFloat) {
/* 29 */     return floatArg(paramFloat, Float.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static FloatArgumentType floatArg(float paramFloat1, float paramFloat2) {
/* 33 */     return new FloatArgumentType(paramFloat1, paramFloat2);
/*    */   }
/*    */   
/*    */   public static float getFloat(CommandContext<?> paramCommandContext, String paramString) {
/* 37 */     return ((Float)paramCommandContext.getArgument(paramString, Float.class)).floatValue();
/*    */   }
/*    */   
/*    */   public float getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public float getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Float parse(StringReader paramStringReader) throws CommandSyntaxException {
/* 50 */     int i = paramStringReader.getCursor();
/* 51 */     float f = paramStringReader.readFloat();
/* 52 */     if (f < this.minimum) {
/* 53 */       paramStringReader.setCursor(i);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooLow().createWithContext(paramStringReader, Float.valueOf(f), Float.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (f > this.maximum) {
/* 57 */       paramStringReader.setCursor(i);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooHigh().createWithContext(paramStringReader, Float.valueOf(f), Float.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Float.valueOf(f);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 65 */     if (this == paramObject) return true; 
/* 66 */     if (!(paramObject instanceof FloatArgumentType)) return false;
/*    */     
/* 68 */     FloatArgumentType floatArgumentType = (FloatArgumentType)paramObject;
/* 69 */     return (this.maximum == floatArgumentType.maximum && this.minimum == floatArgumentType.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return (int)(31.0F * this.minimum + this.maximum);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == -3.4028235E38F && this.maximum == Float.MAX_VALUE)
/* 80 */       return "float()"; 
/* 81 */     if (this.maximum == Float.MAX_VALUE) {
/* 82 */       return "float(" + this.minimum + ")";
/*    */     }
/* 84 */     return "float(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\arguments\FloatArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */