/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsedCommandNode<S>
/*    */ {
/*    */   private final CommandNode<S> node;
/*    */   private final StringRange range;
/*    */   
/*    */   public ParsedCommandNode(CommandNode<S> paramCommandNode, StringRange paramStringRange) {
/* 17 */     this.node = paramCommandNode;
/* 18 */     this.range = paramStringRange;
/*    */   }
/*    */   
/*    */   public CommandNode<S> getNode() {
/* 22 */     return this.node;
/*    */   }
/*    */   
/*    */   public StringRange getRange() {
/* 26 */     return this.range;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return this.node + "@" + this.range;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 36 */     if (this == paramObject) return true; 
/* 37 */     if (paramObject == null || getClass() != paramObject.getClass()) return false; 
/* 38 */     ParsedCommandNode parsedCommandNode = (ParsedCommandNode)paramObject;
/* 39 */     return (Objects.equals(this.node, parsedCommandNode.node) && 
/* 40 */       Objects.equals(this.range, parsedCommandNode.range));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 45 */     return Objects.hash(new Object[] { this.node, this.range });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\ParsedCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */