/*    */ package com.mojang.brigadier.builder;
/*    */ 
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ 
/*    */ 
/*    */ public class LiteralArgumentBuilder<S>
/*    */   extends ArgumentBuilder<S, LiteralArgumentBuilder<S>>
/*    */ {
/*    */   private final String literal;
/*    */   
/*    */   protected LiteralArgumentBuilder(String paramString) {
/* 13 */     this.literal = paramString;
/*    */   }
/*    */   
/*    */   public static <S> LiteralArgumentBuilder<S> literal(String paramString) {
/* 17 */     return new LiteralArgumentBuilder<>(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   protected LiteralArgumentBuilder<S> getThis() {
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public String getLiteral() {
/* 26 */     return this.literal;
/*    */   }
/*    */ 
/*    */   
/*    */   public LiteralCommandNode<S> build() {
/* 31 */     LiteralCommandNode<S> literalCommandNode = new LiteralCommandNode(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
/*    */     
/* 33 */     for (CommandNode<S> commandNode : getArguments()) {
/* 34 */       literalCommandNode.addChild(commandNode);
/*    */     }
/*    */     
/* 37 */     return literalCommandNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\builder\LiteralArgumentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */