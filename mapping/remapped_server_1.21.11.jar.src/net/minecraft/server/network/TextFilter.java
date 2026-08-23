/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface TextFilter
/*    */ {
/*  9 */   public static final TextFilter DUMMY = new TextFilter()
/*    */     {
/*    */       public CompletableFuture<FilteredText> processStreamMessage(String param1String) {
/* 12 */         return CompletableFuture.completedFuture(FilteredText.passThrough(param1String));
/*    */       }
/*    */ 
/*    */       
/*    */       public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> param1List) {
/* 17 */         return CompletableFuture.completedFuture((List<FilteredText>)param1List.stream().map(FilteredText::passThrough).collect(ImmutableList.toImmutableList()));
/*    */       }
/*    */     };
/*    */   
/*    */   default void join() {}
/*    */   
/*    */   default void leave() {}
/*    */   
/*    */   CompletableFuture<FilteredText> processStreamMessage(String paramString);
/*    */   
/*    */   CompletableFuture<List<FilteredText>> processMessageBundle(List<String> paramList);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\TextFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */