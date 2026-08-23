/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class BinaryHeap {
/*   6 */   private Node[] heap = new Node[128];
/*     */   
/*     */   private int size;
/*     */   
/*     */   public Node insert(Node paramNode) {
/*  11 */     if (paramNode.heapIdx >= 0) {
/*  12 */       throw new IllegalStateException("OW KNOWS!");
/*     */     }
/*     */     
/*  15 */     if (this.size == this.heap.length) {
/*  16 */       Node[] arrayOfNode = new Node[this.size << 1];
/*  17 */       System.arraycopy(this.heap, 0, arrayOfNode, 0, this.size);
/*  18 */       this.heap = arrayOfNode;
/*     */     } 
/*     */ 
/*     */     
/*  22 */     this.heap[this.size] = paramNode;
/*  23 */     paramNode.heapIdx = this.size;
/*  24 */     upHeap(this.size++);
/*     */     
/*  26 */     return paramNode;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  30 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public Node peek() {
/*  34 */     return this.heap[0];
/*     */   }
/*     */   
/*     */   public Node pop() {
/*  38 */     Node node = this.heap[0];
/*  39 */     this.heap[0] = this.heap[--this.size];
/*  40 */     this.heap[this.size] = null;
/*  41 */     if (this.size > 0) {
/*  42 */       downHeap(0);
/*     */     }
/*  44 */     node.heapIdx = -1;
/*  45 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Node paramNode) {
/*  50 */     this.heap[paramNode.heapIdx] = this.heap[--this.size];
/*  51 */     this.heap[this.size] = null;
/*  52 */     if (this.size > paramNode.heapIdx) {
/*  53 */       if ((this.heap[paramNode.heapIdx]).f < paramNode.f) {
/*  54 */         upHeap(paramNode.heapIdx);
/*     */       } else {
/*  56 */         downHeap(paramNode.heapIdx);
/*     */       } 
/*     */     }
/*     */     
/*  60 */     paramNode.heapIdx = -1;
/*     */   }
/*     */   
/*     */   public void changeCost(Node paramNode, float paramFloat) {
/*  64 */     float f = paramNode.f;
/*  65 */     paramNode.f = paramFloat;
/*  66 */     if (paramFloat < f) {
/*  67 */       upHeap(paramNode.heapIdx);
/*     */     } else {
/*  69 */       downHeap(paramNode.heapIdx);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int size() {
/*  74 */     return this.size;
/*     */   }
/*     */   
/*     */   private void upHeap(int paramInt) {
/*  78 */     Node node = this.heap[paramInt];
/*  79 */     float f = node.f;
/*  80 */     while (paramInt > 0) {
/*  81 */       int i = paramInt - 1 >> 1;
/*  82 */       Node node1 = this.heap[i];
/*  83 */       if (f < node1.f) {
/*  84 */         this.heap[paramInt] = node1;
/*  85 */         node1.heapIdx = paramInt;
/*  86 */         paramInt = i;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  91 */     this.heap[paramInt] = node;
/*  92 */     node.heapIdx = paramInt;
/*     */   }
/*     */   
/*     */   private void downHeap(int paramInt) {
/*  96 */     Node node = this.heap[paramInt];
/*  97 */     float f = node.f; while (true) {
/*     */       Node node2;
/*     */       float f2;
/* 100 */       int i = 1 + (paramInt << 1);
/* 101 */       int j = i + 1;
/*     */       
/* 103 */       if (i >= this.size) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 108 */       Node node1 = this.heap[i];
/* 109 */       float f1 = node1.f;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 114 */       if (j >= this.size) {
/*     */         
/* 116 */         node2 = null;
/* 117 */         f2 = Float.POSITIVE_INFINITY;
/*     */       } else {
/* 119 */         node2 = this.heap[j];
/* 120 */         f2 = node2.f;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 125 */       if (f1 < f2) {
/* 126 */         if (f1 < f) {
/* 127 */           this.heap[paramInt] = node1;
/* 128 */           node1.heapIdx = paramInt;
/* 129 */           paramInt = i;
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 134 */       if (f2 < f) {
/* 135 */         this.heap[paramInt] = node2;
/* 136 */         node2.heapIdx = paramInt;
/* 137 */         paramInt = j;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 144 */     this.heap[paramInt] = node;
/* 145 */     node.heapIdx = paramInt;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 149 */     return (this.size == 0);
/*     */   }
/*     */   
/*     */   public Node[] getHeap() {
/* 153 */     return Arrays.<Node>copyOf(this.heap, this.size);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\BinaryHeap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */