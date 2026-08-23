/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongestOnly<S>
/*    */   implements ErrorCollector<S>
/*    */ {
/*    */   private static class MutableErrorEntry<S>
/*    */   {
/* 31 */     SuggestionSupplier<S> suggestions = SuggestionSupplier.empty();
/* 32 */     Object reason = "empty";
/*    */   }
/*    */   
/* 35 */   private MutableErrorEntry<S>[] entries = (MutableErrorEntry<S>[])new MutableErrorEntry[16];
/*    */   
/*    */   private int nextErrorEntry;
/*    */   
/* 39 */   private int lastCursor = -1;
/*    */   
/*    */   private void discardErrorsFromShorterParse(int paramInt) {
/* 42 */     if (paramInt > this.lastCursor) {
/* 43 */       this.lastCursor = paramInt;
/* 44 */       this.nextErrorEntry = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish(int paramInt) {
/* 50 */     discardErrorsFromShorterParse(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void store(int paramInt, SuggestionSupplier<S> paramSuggestionSupplier, Object paramObject) {
/* 55 */     discardErrorsFromShorterParse(paramInt);
/*    */     
/* 57 */     if (paramInt == this.lastCursor) {
/* 58 */       addErrorEntry(paramSuggestionSupplier, paramObject);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addErrorEntry(SuggestionSupplier<S> paramSuggestionSupplier, Object paramObject) {
/* 63 */     int i = this.entries.length;
/* 64 */     if (this.nextErrorEntry >= i) {
/* 65 */       int k = Util.growByHalf(i, this.nextErrorEntry + 1);
/* 66 */       MutableErrorEntry[] arrayOfMutableErrorEntry = new MutableErrorEntry[k];
/* 67 */       System.arraycopy(this.entries, 0, arrayOfMutableErrorEntry, 0, i);
/* 68 */       this.entries = (MutableErrorEntry<S>[])arrayOfMutableErrorEntry;
/*    */     } 
/*    */     
/* 71 */     int j = this.nextErrorEntry++;
/* 72 */     MutableErrorEntry<S> mutableErrorEntry = this.entries[j];
/* 73 */     if (mutableErrorEntry == null) {
/* 74 */       mutableErrorEntry = new MutableErrorEntry<>();
/* 75 */       this.entries[j] = mutableErrorEntry;
/*    */     } 
/* 77 */     mutableErrorEntry.suggestions = paramSuggestionSupplier;
/* 78 */     mutableErrorEntry.reason = paramObject;
/*    */   }
/*    */   
/*    */   public List<ErrorEntry<S>> entries() {
/* 82 */     int i = this.nextErrorEntry;
/* 83 */     if (i == 0) {
/* 84 */       return List.of();
/*    */     }
/* 86 */     ArrayList<ErrorEntry<S>> arrayList = new ArrayList(i);
/* 87 */     for (byte b = 0; b < i; b++) {
/* 88 */       MutableErrorEntry<S> mutableErrorEntry = this.entries[b];
/* 89 */       arrayList.add(new ErrorEntry<>(this.lastCursor, mutableErrorEntry.suggestions, mutableErrorEntry.reason));
/*    */     } 
/* 91 */     return arrayList;
/*    */   }
/*    */   
/*    */   public int cursor() {
/* 95 */     return this.lastCursor;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\ErrorCollector$LongestOnly.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */