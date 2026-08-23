/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public abstract class IntrinsicHolderTagsProvider<T>
/*    */   extends TagsProvider<T> {
/*    */   private final Function<T, ResourceKey<T>> keyExtractor;
/*    */   
/*    */   public IntrinsicHolderTagsProvider(PackOutput paramPackOutput, ResourceKey<? extends Registry<T>> paramResourceKey, CompletableFuture<HolderLookup.Provider> paramCompletableFuture, Function<T, ResourceKey<T>> paramFunction) {
/* 17 */     super(paramPackOutput, paramResourceKey, paramCompletableFuture);
/* 18 */     this.keyExtractor = paramFunction;
/*    */   }
/*    */   
/*    */   public IntrinsicHolderTagsProvider(PackOutput paramPackOutput, ResourceKey<? extends Registry<T>> paramResourceKey, CompletableFuture<HolderLookup.Provider> paramCompletableFuture, CompletableFuture<TagsProvider.TagLookup<T>> paramCompletableFuture1, Function<T, ResourceKey<T>> paramFunction) {
/* 22 */     super(paramPackOutput, paramResourceKey, paramCompletableFuture, paramCompletableFuture1);
/* 23 */     this.keyExtractor = paramFunction;
/*    */   }
/*    */   
/*    */   protected TagAppender<T, T> tag(TagKey<T> paramTagKey) {
/* 27 */     TagBuilder tagBuilder = getOrCreateRawBuilder(paramTagKey);
/* 28 */     return TagAppender.<T>forBuilder(tagBuilder).map(this.keyExtractor);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\IntrinsicHolderTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */