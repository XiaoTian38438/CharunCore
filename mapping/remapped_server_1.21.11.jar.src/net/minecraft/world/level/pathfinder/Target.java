/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class Target extends Node {
/*  6 */   private float bestHeuristic = Float.MAX_VALUE;
/*    */   private Node bestNode;
/*    */   private boolean reached;
/*    */   
/*    */   public Target(Node paramNode) {
/* 11 */     super(paramNode.x, paramNode.y, paramNode.z);
/*    */   }
/*    */   
/*    */   public Target(int paramInt1, int paramInt2, int paramInt3) {
/* 15 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */   
/*    */   public void updateBest(float paramFloat, Node paramNode) {
/* 19 */     if (paramFloat < this.bestHeuristic) {
/* 20 */       this.bestHeuristic = paramFloat;
/* 21 */       this.bestNode = paramNode;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Node getBestNode() {
/* 26 */     return this.bestNode;
/*    */   }
/*    */   
/*    */   public void setReached() {
/* 30 */     this.reached = true;
/*    */   }
/*    */   
/*    */   public boolean isReached() {
/* 34 */     return this.reached;
/*    */   }
/*    */   
/*    */   public static Target createFromStream(FriendlyByteBuf paramFriendlyByteBuf) {
/* 38 */     Target target = new Target(paramFriendlyByteBuf.readInt(), paramFriendlyByteBuf.readInt(), paramFriendlyByteBuf.readInt());
/* 39 */     readContents(paramFriendlyByteBuf, target);
/* 40 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\Target.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */