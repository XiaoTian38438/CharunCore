/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuggestionContext<S>
/*    */ {
/*    */   public final CommandNode<S> parent;
/*    */   public final int startPos;
/*    */   
/*    */   public SuggestionContext(CommandNode<S> paramCommandNode, int paramInt) {
/* 13 */     this.parent = paramCommandNode;
/* 14 */     this.startPos = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\SuggestionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */