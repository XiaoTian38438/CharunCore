/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public abstract class KeyTagProvider<T>
/*    */   extends TagsProvider<T>
/*    */ {
/*    */   protected KeyTagProvider(PackOutput paramPackOutput, ResourceKey<? extends Registry<T>> paramResourceKey, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 15 */     super(paramPackOutput, paramResourceKey, paramCompletableFuture);
/*    */   }
/*    */   
/*    */   protected TagAppender<ResourceKey<T>, T> tag(TagKey<T> paramTagKey) {
/* 19 */     TagBuilder tagBuilder = getOrCreateRawBuilder(paramTagKey);
/* 20 */     return TagAppender.forBuilder(tagBuilder);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\KeyTagProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */