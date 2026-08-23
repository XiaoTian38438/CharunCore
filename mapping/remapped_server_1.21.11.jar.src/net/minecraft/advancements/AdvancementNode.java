/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class AdvancementNode
/*    */ {
/*    */   private final AdvancementHolder holder;
/*    */   private final AdvancementNode parent;
/* 12 */   private final Set<AdvancementNode> children = (Set<AdvancementNode>)new ReferenceOpenHashSet();
/*    */   
/*    */   @VisibleForTesting
/*    */   public AdvancementNode(AdvancementHolder paramAdvancementHolder, AdvancementNode paramAdvancementNode) {
/* 16 */     this.holder = paramAdvancementHolder;
/* 17 */     this.parent = paramAdvancementNode;
/*    */   }
/*    */   
/*    */   public Advancement advancement() {
/* 21 */     return this.holder.value();
/*    */   }
/*    */   
/*    */   public AdvancementHolder holder() {
/* 25 */     return this.holder;
/*    */   }
/*    */   
/*    */   public AdvancementNode parent() {
/* 29 */     return this.parent;
/*    */   }
/*    */   
/*    */   public AdvancementNode root() {
/* 33 */     return getRoot(this);
/*    */   }
/*    */   
/*    */   public static AdvancementNode getRoot(AdvancementNode paramAdvancementNode) {
/* 37 */     AdvancementNode advancementNode = paramAdvancementNode;
/*    */     while (true) {
/* 39 */       AdvancementNode advancementNode1 = advancementNode.parent();
/* 40 */       if (advancementNode1 == null) {
/* 41 */         return advancementNode;
/*    */       }
/* 43 */       advancementNode = advancementNode1;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Iterable<AdvancementNode> children() {
/* 48 */     return this.children;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public void addChild(AdvancementNode paramAdvancementNode) {
/* 53 */     this.children.add(paramAdvancementNode);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 58 */     if (this == paramObject) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (paramObject instanceof AdvancementNode) { AdvancementNode advancementNode = (AdvancementNode)paramObject; if (this.holder.equals(advancementNode.holder)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.holder.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return this.holder.id().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\AdvancementNode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */