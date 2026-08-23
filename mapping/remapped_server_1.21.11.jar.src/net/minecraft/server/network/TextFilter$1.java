/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements TextFilter
/*    */ {
/*    */   public CompletableFuture<FilteredText> processStreamMessage(String paramString) {
/* 12 */     return CompletableFuture.completedFuture(FilteredText.passThrough(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> paramList) {
/* 17 */     return CompletableFuture.completedFuture((List<FilteredText>)paramList.stream().map(FilteredText::passThrough).collect(ImmutableList.toImmutableList()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\TextFilter$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */