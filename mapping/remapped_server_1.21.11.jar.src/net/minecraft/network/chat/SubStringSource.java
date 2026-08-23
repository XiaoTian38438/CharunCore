/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.StringDecomposer;
/*    */ 
/*    */ public class SubStringSource {
/*    */   private final String plainText;
/*    */   private final List<Style> charStyles;
/*    */   private final Int2IntFunction reverseCharModifier;
/*    */   
/*    */   private SubStringSource(String paramString, List<Style> paramList, Int2IntFunction paramInt2IntFunction) {
/* 19 */     this.plainText = paramString;
/* 20 */     this.charStyles = (List<Style>)ImmutableList.copyOf(paramList);
/* 21 */     this.reverseCharModifier = paramInt2IntFunction;
/*    */   }
/*    */   
/*    */   public String getPlainText() {
/* 25 */     return this.plainText;
/*    */   }
/*    */   
/*    */   public List<FormattedCharSequence> substring(int paramInt1, int paramInt2, boolean paramBoolean) {
/* 29 */     if (paramInt2 == 0) {
/* 30 */       return (List<FormattedCharSequence>)ImmutableList.of();
/*    */     }
/*    */     
/* 33 */     ArrayList<FormattedCharSequence> arrayList = Lists.newArrayList();
/*    */     
/* 35 */     Style style = this.charStyles.get(paramInt1);
/* 36 */     int i = paramInt1;
/* 37 */     for (byte b = 1; b < paramInt2; b++) {
/* 38 */       int j = paramInt1 + b;
/* 39 */       Style style1 = this.charStyles.get(j);
/* 40 */       if (!style1.equals(style)) {
/* 41 */         String str = this.plainText.substring(i, j);
/* 42 */         arrayList.add(paramBoolean ? FormattedCharSequence.backward(str, style, this.reverseCharModifier) : FormattedCharSequence.forward(str, style));
/* 43 */         style = style1;
/* 44 */         i = j;
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     if (i < paramInt1 + paramInt2) {
/* 49 */       String str = this.plainText.substring(i, paramInt1 + paramInt2);
/* 50 */       arrayList.add(paramBoolean ? FormattedCharSequence.backward(str, style, this.reverseCharModifier) : FormattedCharSequence.forward(str, style));
/*    */     } 
/*    */     
/* 53 */     return paramBoolean ? Lists.reverse(arrayList) : arrayList;
/*    */   }
/*    */   
/*    */   public static SubStringSource create(FormattedText paramFormattedText) {
/* 57 */     return create(paramFormattedText, paramInt -> paramInt, paramString -> paramString);
/*    */   }
/*    */   
/*    */   public static SubStringSource create(FormattedText paramFormattedText, Int2IntFunction paramInt2IntFunction, UnaryOperator<String> paramUnaryOperator) {
/* 61 */     StringBuilder stringBuilder = new StringBuilder();
/* 62 */     ArrayList<Style> arrayList = Lists.newArrayList();
/*    */     
/* 64 */     paramFormattedText.visit((paramStyle, paramString) -> { StringDecomposer.iterateFormatted(paramString, paramStyle, ()); return Optional.empty(); }Style.EMPTY);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     return new SubStringSource(paramUnaryOperator.apply(stringBuilder.toString()), arrayList, paramInt2IntFunction);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\SubStringSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */