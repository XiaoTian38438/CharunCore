/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SubTree<T>
/*     */   extends Climate.RTree.Node<T>
/*     */ {
/*     */   final Climate.RTree.Node<T>[] children;
/*     */   
/*     */   protected SubTree(List<? extends Climate.RTree.Node<T>> paramList) {
/* 116 */     this(Climate.RTree.buildParameterSpace(paramList), paramList);
/*     */   }
/*     */   
/*     */   protected SubTree(List<Climate.Parameter> paramList, List<? extends Climate.RTree.Node<T>> paramList1) {
/* 120 */     super(paramList);
/* 121 */     this.children = paramList1.<Climate.RTree.Node<T>>toArray((Climate.RTree.Node<T>[])new Climate.RTree.Node[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Climate.RTree.Leaf<T> search(long[] paramArrayOflong, Climate.RTree.Leaf<T> paramLeaf, Climate.DistanceMetric<T> paramDistanceMetric) {
/* 126 */     long l = (paramLeaf == null) ? Long.MAX_VALUE : paramDistanceMetric.distance(paramLeaf, paramArrayOflong);
/* 127 */     Climate.RTree.Leaf<T> leaf = paramLeaf;
/*     */     
/* 129 */     for (Climate.RTree.Node<T> node : this.children) {
/* 130 */       long l1 = paramDistanceMetric.distance(node, paramArrayOflong);
/* 131 */       if (l > l1) {
/*     */         
/* 133 */         Climate.RTree.Leaf<T> leaf1 = node.search(paramArrayOflong, leaf, paramDistanceMetric);
/* 134 */         long l2 = (node == leaf1) ? l1 : paramDistanceMetric.distance(leaf1, paramArrayOflong);
/* 135 */         if (l > l2) {
/* 136 */           l = l2;
/* 137 */           leaf = leaf1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 141 */     return leaf;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\Climate$RTree$SubTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */