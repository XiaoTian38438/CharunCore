/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class TreeNodePosition
/*     */ {
/*     */   private final AdvancementNode node;
/*     */   private final TreeNodePosition parent;
/*     */   private final TreeNodePosition previousSibling;
/*     */   private final int childIndex;
/*  13 */   private final List<TreeNodePosition> children = Lists.newArrayList();
/*     */   private TreeNodePosition ancestor;
/*     */   private TreeNodePosition thread;
/*     */   private int x;
/*     */   private float y;
/*     */   private float mod;
/*     */   private float change;
/*     */   private float shift;
/*     */   
/*     */   public TreeNodePosition(AdvancementNode paramAdvancementNode, TreeNodePosition paramTreeNodePosition1, TreeNodePosition paramTreeNodePosition2, int paramInt1, int paramInt2) {
/*  23 */     if (paramAdvancementNode.advancement().display().isEmpty()) {
/*  24 */       throw new IllegalArgumentException("Can't position an invisible advancement!");
/*     */     }
/*  26 */     this.node = paramAdvancementNode;
/*  27 */     this.parent = paramTreeNodePosition1;
/*  28 */     this.previousSibling = paramTreeNodePosition2;
/*  29 */     this.childIndex = paramInt1;
/*  30 */     this.ancestor = this;
/*  31 */     this.x = paramInt2;
/*  32 */     this.y = -1.0F;
/*     */     
/*  34 */     TreeNodePosition treeNodePosition = null;
/*  35 */     for (AdvancementNode advancementNode : paramAdvancementNode.children()) {
/*  36 */       treeNodePosition = addChild(advancementNode, treeNodePosition);
/*     */     }
/*     */   }
/*     */   
/*     */   private TreeNodePosition addChild(AdvancementNode paramAdvancementNode, TreeNodePosition paramTreeNodePosition) {
/*  41 */     if (paramAdvancementNode.advancement().display().isPresent()) {
/*  42 */       paramTreeNodePosition = new TreeNodePosition(paramAdvancementNode, this, paramTreeNodePosition, this.children.size() + 1, this.x + 1);
/*  43 */       this.children.add(paramTreeNodePosition);
/*     */     } else {
/*  45 */       for (AdvancementNode advancementNode : paramAdvancementNode.children()) {
/*  46 */         paramTreeNodePosition = addChild(advancementNode, paramTreeNodePosition);
/*     */       }
/*     */     } 
/*  49 */     return paramTreeNodePosition;
/*     */   }
/*     */   
/*     */   private void firstWalk() {
/*  53 */     if (this.children.isEmpty()) {
/*  54 */       if (this.previousSibling != null) {
/*  55 */         this.previousSibling.y++;
/*     */       } else {
/*  57 */         this.y = 0.0F;
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     TreeNodePosition treeNodePosition = null;
/*  63 */     for (TreeNodePosition treeNodePosition1 : this.children) {
/*  64 */       treeNodePosition1.firstWalk();
/*  65 */       treeNodePosition = treeNodePosition1.apportion((treeNodePosition == null) ? treeNodePosition1 : treeNodePosition);
/*     */     } 
/*  67 */     executeShifts();
/*     */     
/*  69 */     float f = (((TreeNodePosition)this.children.get(0)).y + ((TreeNodePosition)this.children.get(this.children.size() - 1)).y) / 2.0F;
/*  70 */     if (this.previousSibling != null) {
/*  71 */       this.previousSibling.y++;
/*  72 */       this.mod = this.y - f;
/*     */     } else {
/*  74 */       this.y = f;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float secondWalk(float paramFloat1, int paramInt, float paramFloat2) {
/*  79 */     this.y += paramFloat1;
/*  80 */     this.x = paramInt;
/*     */     
/*  82 */     if (this.y < paramFloat2) {
/*  83 */       paramFloat2 = this.y;
/*     */     }
/*     */     
/*  86 */     for (TreeNodePosition treeNodePosition : this.children) {
/*  87 */       paramFloat2 = treeNodePosition.secondWalk(paramFloat1 + this.mod, paramInt + 1, paramFloat2);
/*     */     }
/*     */     
/*  90 */     return paramFloat2;
/*     */   }
/*     */   
/*     */   private void thirdWalk(float paramFloat) {
/*  94 */     this.y += paramFloat;
/*  95 */     for (TreeNodePosition treeNodePosition : this.children) {
/*  96 */       treeNodePosition.thirdWalk(paramFloat);
/*     */     }
/*     */   }
/*     */   
/*     */   private void executeShifts() {
/* 101 */     float f1 = 0.0F;
/* 102 */     float f2 = 0.0F;
/* 103 */     for (int i = this.children.size() - 1; i >= 0; i--) {
/* 104 */       TreeNodePosition treeNodePosition = this.children.get(i);
/* 105 */       treeNodePosition.y += f1;
/* 106 */       treeNodePosition.mod += f1;
/* 107 */       f2 += treeNodePosition.change;
/* 108 */       f1 += treeNodePosition.shift + f2;
/*     */     } 
/*     */   }
/*     */   
/*     */   private TreeNodePosition previousOrThread() {
/* 113 */     if (this.thread != null) {
/* 114 */       return this.thread;
/*     */     }
/* 116 */     if (!this.children.isEmpty()) {
/* 117 */       return this.children.get(0);
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   private TreeNodePosition nextOrThread() {
/* 123 */     if (this.thread != null) {
/* 124 */       return this.thread;
/*     */     }
/* 126 */     if (!this.children.isEmpty()) {
/* 127 */       return this.children.get(this.children.size() - 1);
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   private TreeNodePosition apportion(TreeNodePosition paramTreeNodePosition) {
/* 133 */     if (this.previousSibling == null) {
/* 134 */       return paramTreeNodePosition;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 139 */     TreeNodePosition treeNodePosition1 = this;
/* 140 */     TreeNodePosition treeNodePosition2 = this;
/* 141 */     TreeNodePosition treeNodePosition3 = this.previousSibling;
/* 142 */     TreeNodePosition treeNodePosition4 = this.parent.children.get(0);
/* 143 */     float f1 = this.mod;
/* 144 */     float f2 = this.mod;
/* 145 */     float f3 = treeNodePosition3.mod;
/* 146 */     float f4 = treeNodePosition4.mod;
/*     */     
/* 148 */     while (treeNodePosition3.nextOrThread() != null && treeNodePosition1.previousOrThread() != null) {
/* 149 */       treeNodePosition3 = treeNodePosition3.nextOrThread();
/* 150 */       treeNodePosition1 = treeNodePosition1.previousOrThread();
/* 151 */       treeNodePosition4 = treeNodePosition4.previousOrThread();
/* 152 */       treeNodePosition2 = treeNodePosition2.nextOrThread();
/* 153 */       treeNodePosition2.ancestor = this;
/* 154 */       float f = treeNodePosition3.y + f3 - treeNodePosition1.y + f1 + 1.0F;
/* 155 */       if (f > 0.0F) {
/* 156 */         treeNodePosition3.getAncestor(this, paramTreeNodePosition).moveSubtree(this, f);
/* 157 */         f1 += f;
/* 158 */         f2 += f;
/*     */       } 
/* 160 */       f3 += treeNodePosition3.mod;
/* 161 */       f1 += treeNodePosition1.mod;
/* 162 */       f4 += treeNodePosition4.mod;
/* 163 */       f2 += treeNodePosition2.mod;
/*     */     } 
/* 165 */     if (treeNodePosition3.nextOrThread() != null && treeNodePosition2.nextOrThread() == null) {
/* 166 */       treeNodePosition2.thread = treeNodePosition3.nextOrThread();
/* 167 */       treeNodePosition2.mod += f3 - f2;
/*     */     } else {
/* 169 */       if (treeNodePosition1.previousOrThread() != null && treeNodePosition4.previousOrThread() == null) {
/* 170 */         treeNodePosition4.thread = treeNodePosition1.previousOrThread();
/* 171 */         treeNodePosition4.mod += f1 - f4;
/*     */       } 
/* 173 */       paramTreeNodePosition = this;
/*     */     } 
/*     */     
/* 176 */     return paramTreeNodePosition;
/*     */   }
/*     */   
/*     */   private void moveSubtree(TreeNodePosition paramTreeNodePosition, float paramFloat) {
/* 180 */     float f = (paramTreeNodePosition.childIndex - this.childIndex);
/* 181 */     if (f != 0.0F) {
/* 182 */       paramTreeNodePosition.change -= paramFloat / f;
/* 183 */       this.change += paramFloat / f;
/*     */     } 
/* 185 */     paramTreeNodePosition.shift += paramFloat;
/* 186 */     paramTreeNodePosition.y += paramFloat;
/* 187 */     paramTreeNodePosition.mod += paramFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   private TreeNodePosition getAncestor(TreeNodePosition paramTreeNodePosition1, TreeNodePosition paramTreeNodePosition2) {
/* 192 */     if (this.ancestor != null && paramTreeNodePosition1.parent.children.contains(this.ancestor)) {
/* 193 */       return this.ancestor;
/*     */     }
/* 195 */     return paramTreeNodePosition2;
/*     */   }
/*     */ 
/*     */   
/*     */   private void finalizePosition() {
/* 200 */     this.node.advancement().display().ifPresent(paramDisplayInfo -> paramDisplayInfo.setLocation(this.x, this.y));
/*     */     
/* 202 */     if (!this.children.isEmpty()) {
/* 203 */       for (TreeNodePosition treeNodePosition : this.children) {
/* 204 */         treeNodePosition.finalizePosition();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void run(AdvancementNode paramAdvancementNode) {
/* 210 */     if (paramAdvancementNode.advancement().display().isEmpty()) {
/* 211 */       throw new IllegalArgumentException("Can't position children of an invisible root!");
/*     */     }
/* 213 */     TreeNodePosition treeNodePosition = new TreeNodePosition(paramAdvancementNode, null, null, 1, 0);
/* 214 */     treeNodePosition.firstWalk();
/* 215 */     float f = treeNodePosition.secondWalk(0.0F, 0, treeNodePosition.y);
/* 216 */     if (f < 0.0F) {
/* 217 */       treeNodePosition.thirdWalk(-f);
/*     */     }
/* 219 */     treeNodePosition.finalizePosition();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\TreeNodePosition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */