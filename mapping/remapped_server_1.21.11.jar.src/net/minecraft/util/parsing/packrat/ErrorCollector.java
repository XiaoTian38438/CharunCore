/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ public interface ErrorCollector<S>
/*    */ {
/*    */   void store(int paramInt, SuggestionSupplier<S> paramSuggestionSupplier, Object paramObject);
/*    */   
/*    */   default void store(int paramInt, Object paramObject) {
/* 13 */     store(paramInt, SuggestionSupplier.empty(), paramObject);
/*    */   }
/*    */   
/*    */   void finish(int paramInt);
/*    */   
/*    */   public static class Nop<S>
/*    */     implements ErrorCollector<S>
/*    */   {
/*    */     public void store(int param1Int, SuggestionSupplier<S> param1SuggestionSupplier, Object param1Object) {}
/*    */     
/*    */     public void finish(int param1Int) {}
/*    */   }
/*    */   
/*    */   public static class LongestOnly<S>
/*    */     implements ErrorCollector<S>
/*    */   {
/*    */     private static class MutableErrorEntry<S>
/*    */     {
/* 31 */       SuggestionSupplier<S> suggestions = SuggestionSupplier.empty();
/* 32 */       Object reason = "empty";
/*    */     }
/*    */     
/* 35 */     private MutableErrorEntry<S>[] entries = (MutableErrorEntry<S>[])new MutableErrorEntry[16];
/*    */     
/*    */     private int nextErrorEntry;
/*    */     
/* 39 */     private int lastCursor = -1;
/*    */     
/*    */     private void discardErrorsFromShorterParse(int param1Int) {
/* 42 */       if (param1Int > this.lastCursor) {
/* 43 */         this.lastCursor = param1Int;
/* 44 */         this.nextErrorEntry = 0;
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void finish(int param1Int) {
/* 50 */       discardErrorsFromShorterParse(param1Int);
/*    */     }
/*    */ 
/*    */     
/*    */     public void store(int param1Int, SuggestionSupplier<S> param1SuggestionSupplier, Object param1Object) {
/* 55 */       discardErrorsFromShorterParse(param1Int);
/*    */       
/* 57 */       if (param1Int == this.lastCursor) {
/* 58 */         addErrorEntry(param1SuggestionSupplier, param1Object);
/*    */       }
/*    */     }
/*    */     
/*    */     private void addErrorEntry(SuggestionSupplier<S> param1SuggestionSupplier, Object param1Object) {
/* 63 */       int i = this.entries.length;
/* 64 */       if (this.nextErrorEntry >= i) {
/* 65 */         int k = Util.growByHalf(i, this.nextErrorEntry + 1);
/* 66 */         MutableErrorEntry[] arrayOfMutableErrorEntry = new MutableErrorEntry[k];
/* 67 */         System.arraycopy(this.entries, 0, arrayOfMutableErrorEntry, 0, i);
/* 68 */         this.entries = (MutableErrorEntry<S>[])arrayOfMutableErrorEntry;
/*    */       } 
/*    */       
/* 71 */       int j = this.nextErrorEntry++;
/* 72 */       MutableErrorEntry<S> mutableErrorEntry = this.entries[j];
/* 73 */       if (mutableErrorEntry == null) {
/* 74 */         mutableErrorEntry = new MutableErrorEntry<>();
/* 75 */         this.entries[j] = mutableErrorEntry;
/*    */       } 
/* 77 */       mutableErrorEntry.suggestions = param1SuggestionSupplier;
/* 78 */       mutableErrorEntry.reason = param1Object;
/*    */     }
/*    */     
/*    */     public List<ErrorEntry<S>> entries() {
/* 82 */       int i = this.nextErrorEntry;
/* 83 */       if (i == 0) {
/* 84 */         return List.of();
/*    */       }
/* 86 */       ArrayList<ErrorEntry<S>> arrayList = new ArrayList(i);
/* 87 */       for (byte b = 0; b < i; b++) {
/* 88 */         MutableErrorEntry<S> mutableErrorEntry = this.entries[b];
/* 89 */         arrayList.add(new ErrorEntry<>(this.lastCursor, mutableErrorEntry.suggestions, mutableErrorEntry.reason));
/*    */       } 
/* 91 */       return arrayList;
/*    */     }
/*    */     
/*    */     public int cursor() {
/* 95 */       return this.lastCursor;
/*    */     }
/*    */   }
/*    */   
/*    */   private static class MutableErrorEntry<S> {
/*    */     SuggestionSupplier<S> suggestions;
/*    */     Object reason;
/*    */     
/*    */     MutableErrorEntry() {
/*    */       this.suggestions = SuggestionSupplier.empty();
/*    */       this.reason = "empty";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\ErrorCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */