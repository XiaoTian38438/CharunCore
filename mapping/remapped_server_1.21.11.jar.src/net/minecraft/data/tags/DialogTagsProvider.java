/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.tags.DialogTags;
/*    */ 
/*    */ public class DialogTagsProvider
/*    */   extends KeyTagProvider<Dialog> {
/*    */   public DialogTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 13 */     super(paramPackOutput, Registries.DIALOG, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider paramProvider) {
/* 19 */     tag(DialogTags.PAUSE_SCREEN_ADDITIONS);
/*    */     
/* 21 */     tag(DialogTags.QUICK_ACTIONS);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\DialogTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */