/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface FormattedCharSequence
/*    */ {
/*    */   public static final FormattedCharSequence EMPTY = paramFormattedCharSink -> true;
/*    */   
/*    */   static FormattedCharSequence codepoint(int paramInt, Style paramStyle) {
/* 16 */     return paramFormattedCharSink -> paramFormattedCharSink.accept(0, paramStyle, paramInt);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence forward(String paramString, Style paramStyle) {
/* 20 */     if (paramString.isEmpty()) {
/* 21 */       return EMPTY;
/*    */     }
/* 23 */     return paramFormattedCharSink -> StringDecomposer.iterate(paramString, paramStyle, paramFormattedCharSink);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence forward(String paramString, Style paramStyle, Int2IntFunction paramInt2IntFunction) {
/* 27 */     if (paramString.isEmpty()) {
/* 28 */       return EMPTY;
/*    */     }
/* 30 */     return paramFormattedCharSink -> StringDecomposer.iterate(paramString, paramStyle, decorateOutput(paramFormattedCharSink, paramInt2IntFunction));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence backward(String paramString, Style paramStyle) {
/* 34 */     if (paramString.isEmpty()) {
/* 35 */       return EMPTY;
/*    */     }
/* 37 */     return paramFormattedCharSink -> StringDecomposer.iterateBackwards(paramString, paramStyle, paramFormattedCharSink);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence backward(String paramString, Style paramStyle, Int2IntFunction paramInt2IntFunction) {
/* 41 */     if (paramString.isEmpty()) {
/* 42 */       return EMPTY;
/*    */     }
/* 44 */     return paramFormattedCharSink -> StringDecomposer.iterateBackwards(paramString, paramStyle, decorateOutput(paramFormattedCharSink, paramInt2IntFunction));
/*    */   }
/*    */   
/*    */   static FormattedCharSink decorateOutput(FormattedCharSink paramFormattedCharSink, Int2IntFunction paramInt2IntFunction) {
/* 48 */     return (paramInt1, paramStyle, paramInt2) -> paramFormattedCharSink.accept(paramInt1, paramStyle, ((Integer)paramInt2IntFunction.apply(Integer.valueOf(paramInt2))).intValue());
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite() {
/* 52 */     return EMPTY;
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(FormattedCharSequence paramFormattedCharSequence) {
/* 56 */     return paramFormattedCharSequence;
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(FormattedCharSequence paramFormattedCharSequence1, FormattedCharSequence paramFormattedCharSequence2) {
/* 60 */     return fromPair(paramFormattedCharSequence1, paramFormattedCharSequence2);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(FormattedCharSequence... paramVarArgs) {
/* 64 */     return fromList((List<FormattedCharSequence>)ImmutableList.copyOf((Object[])paramVarArgs));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(List<FormattedCharSequence> paramList) {
/* 68 */     int i = paramList.size();
/* 69 */     switch (i) {
/*    */       case 0:
/* 71 */         return EMPTY;
/*    */       case 1:
/* 73 */         return paramList.get(0);
/*    */       case 2:
/* 75 */         return fromPair(paramList.get(0), paramList.get(1));
/*    */     } 
/* 77 */     return fromList((List<FormattedCharSequence>)ImmutableList.copyOf(paramList));
/*    */   }
/*    */ 
/*    */   
/*    */   static FormattedCharSequence fromPair(FormattedCharSequence paramFormattedCharSequence1, FormattedCharSequence paramFormattedCharSequence2) {
/* 82 */     return paramFormattedCharSink -> (paramFormattedCharSequence1.accept(paramFormattedCharSink) && paramFormattedCharSequence2.accept(paramFormattedCharSink));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence fromList(List<FormattedCharSequence> paramList) {
/* 86 */     return paramFormattedCharSink -> {
/*    */         for (FormattedCharSequence formattedCharSequence : paramList) {
/*    */           if (!formattedCharSequence.accept(paramFormattedCharSink))
/*    */             return false; 
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */   
/*    */   boolean accept(FormattedCharSink paramFormattedCharSink);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\FormattedCharSequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */