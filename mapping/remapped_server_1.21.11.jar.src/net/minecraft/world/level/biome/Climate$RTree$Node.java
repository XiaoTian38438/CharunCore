/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class Node<T>
/*    */ {
/*    */   protected final Climate.Parameter[] parameterSpace;
/*    */   
/*    */   protected Node(List<Climate.Parameter> paramList) {
/* 79 */     this.parameterSpace = paramList.<Climate.Parameter>toArray(new Climate.Parameter[0]);
/*    */   }
/*    */   
/*    */   protected abstract Climate.RTree.Leaf<T> search(long[] paramArrayOflong, Climate.RTree.Leaf<T> paramLeaf, Climate.DistanceMetric<T> paramDistanceMetric);
/*    */   
/*    */   protected long distance(long[] paramArrayOflong) {
/* 85 */     long l = 0L;
/* 86 */     for (byte b = 0; b < 7; b++) {
/* 87 */       l += Mth.square(this.parameterSpace[b].distance(paramArrayOflong[b]));
/*    */     }
/* 89 */     return l;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return Arrays.toString((Object[])this.parameterSpace);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\Climate$RTree$Node.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */