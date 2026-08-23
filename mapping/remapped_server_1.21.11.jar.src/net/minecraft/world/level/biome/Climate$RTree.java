/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.Mth;
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
/*     */ public final class RTree<T>
/*     */ {
/*     */   private static final int CHILDREN_PER_NODE = 6;
/*     */   private final Node<T> root;
/*  69 */   private final ThreadLocal<Leaf<T>> lastResult = new ThreadLocal<>();
/*     */   
/*     */   private RTree(Node<T> paramNode) {
/*  72 */     this.root = paramNode;
/*     */   }
/*     */   
/*     */   static abstract class Node<T> {
/*     */     protected final Climate.Parameter[] parameterSpace;
/*     */     
/*     */     protected Node(List<Climate.Parameter> param2List) {
/*  79 */       this.parameterSpace = param2List.<Climate.Parameter>toArray(new Climate.Parameter[0]);
/*     */     }
/*     */     
/*     */     protected abstract Climate.RTree.Leaf<T> search(long[] param2ArrayOflong, Climate.RTree.Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric);
/*     */     
/*     */     protected long distance(long[] param2ArrayOflong) {
/*  85 */       long l = 0L;
/*  86 */       for (byte b = 0; b < 7; b++) {
/*  87 */         l += Mth.square(this.parameterSpace[b].distance(param2ArrayOflong[b]));
/*     */       }
/*  89 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  94 */       return Arrays.toString((Object[])this.parameterSpace);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Leaf<T> extends Node<T> {
/*     */     final T value;
/*     */     
/*     */     Leaf(Climate.ParameterPoint param2ParameterPoint, T param2T) {
/* 102 */       super(param2ParameterPoint.parameterSpace());
/* 103 */       this.value = param2T;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Leaf<T> search(long[] param2ArrayOflong, Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric) {
/* 108 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SubTree<T> extends Node<T> {
/*     */     final Climate.RTree.Node<T>[] children;
/*     */     
/*     */     protected SubTree(List<? extends Climate.RTree.Node<T>> param2List) {
/* 116 */       this(Climate.RTree.buildParameterSpace(param2List), param2List);
/*     */     }
/*     */     
/*     */     protected SubTree(List<Climate.Parameter> param2List, List<? extends Climate.RTree.Node<T>> param2List1) {
/* 120 */       super(param2List);
/* 121 */       this.children = param2List1.<Climate.RTree.Node<T>>toArray((Climate.RTree.Node<T>[])new Climate.RTree.Node[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Climate.RTree.Leaf<T> search(long[] param2ArrayOflong, Climate.RTree.Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric) {
/* 126 */       long l = (param2Leaf == null) ? Long.MAX_VALUE : param2DistanceMetric.distance(param2Leaf, param2ArrayOflong);
/* 127 */       Climate.RTree.Leaf<T> leaf = param2Leaf;
/*     */       
/* 129 */       for (Climate.RTree.Node<T> node : this.children) {
/* 130 */         long l1 = param2DistanceMetric.distance(node, param2ArrayOflong);
/* 131 */         if (l > l1) {
/*     */           
/* 133 */           Climate.RTree.Leaf<T> leaf1 = node.search(param2ArrayOflong, leaf, param2DistanceMetric);
/* 134 */           long l2 = (node == leaf1) ? l1 : param2DistanceMetric.distance(leaf1, param2ArrayOflong);
/* 135 */           if (l > l2) {
/* 136 */             l = l2;
/* 137 */             leaf = leaf1;
/*     */           } 
/*     */         } 
/*     */       } 
/* 141 */       return leaf;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> RTree<T> create(List<Pair<Climate.ParameterPoint, T>> paramList) {
/* 146 */     if (paramList.isEmpty()) {
/* 147 */       throw new IllegalArgumentException("Need at least one value to build the search tree.");
/*     */     }
/* 149 */     int i = ((Climate.ParameterPoint)((Pair)paramList.get(0)).getFirst()).parameterSpace().size();
/* 150 */     if (i != 7) {
/* 151 */       throw new IllegalStateException("Expecting parameter space to be 7, got " + i);
/*     */     }
/*     */     
/* 154 */     List<? extends Node<T>> list = (List)paramList.stream().map(paramPair -> new Leaf((Climate.ParameterPoint)paramPair.getFirst(), paramPair.getSecond())).collect(Collectors.toCollection(ArrayList::new));
/*     */     
/* 156 */     return new RTree<>(build(i, list));
/*     */   }
/*     */   
/*     */   private static <T> Node<T> build(int paramInt, List<? extends Node<T>> paramList) {
/* 160 */     if (paramList.isEmpty()) {
/* 161 */       throw new IllegalStateException("Need at least one child to build a node");
/*     */     }
/* 163 */     if (paramList.size() == 1) {
/* 164 */       return paramList.get(0);
/*     */     }
/* 166 */     if (paramList.size() <= 6) {
/* 167 */       paramList.sort(Comparator.comparingLong(paramNode -> {
/*     */               long l = 0L;
/*     */               for (byte b = 0; b < paramInt; b++) {
/*     */                 Climate.Parameter parameter = paramNode.parameterSpace[b];
/*     */                 l += Math.abs((parameter.min() + parameter.max()) / 2L);
/*     */               } 
/*     */               return l;
/*     */             }));
/* 175 */       return new SubTree<>(paramList);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     long l = Long.MAX_VALUE;
/* 183 */     byte b = -1;
/* 184 */     List<SubTree<T>> list = null;
/*     */     
/* 186 */     for (byte b1 = 0; b1 < paramInt; b1++) {
/* 187 */       sort(paramList, paramInt, b1, false);
/* 188 */       List<SubTree<T>> list1 = bucketize(paramList);
/*     */       
/* 190 */       long l1 = 0L;
/* 191 */       for (SubTree<T> subTree : list1) {
/* 192 */         l1 += cost(subTree.parameterSpace);
/*     */       }
/*     */       
/* 195 */       if (l > l1) {
/* 196 */         l = l1;
/* 197 */         b = b1;
/* 198 */         list = list1;
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     sort((List)list, paramInt, b, true);
/*     */     
/* 204 */     return new SubTree<>((List<? extends Node<T>>)list.stream().map(paramSubTree -> build(paramInt, Arrays.asList((Node<?>[])paramSubTree.children))).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   private static <T> void sort(List<? extends Node<T>> paramList, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 208 */     Comparator<Node<?>> comparator = comparator(paramInt2, paramBoolean);
/* 209 */     for (byte b = 1; b < paramInt1; b++) {
/* 210 */       comparator = comparator.thenComparing(comparator((paramInt2 + b) % paramInt1, paramBoolean));
/*     */     }
/* 212 */     paramList.sort(comparator);
/*     */   }
/*     */   
/*     */   private static <T> Comparator<Node<T>> comparator(int paramInt, boolean paramBoolean) {
/* 216 */     return Comparator.comparingLong(paramNode -> {
/*     */           Climate.Parameter parameter = paramNode.parameterSpace[paramInt];
/*     */           long l = (parameter.min() + parameter.max()) / 2L;
/*     */           return paramBoolean ? Math.abs(l) : l;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<SubTree<T>> bucketize(List<? extends Node<T>> paramList) {
/* 227 */     ArrayList<SubTree<T>> arrayList = Lists.newArrayList();
/*     */     
/* 229 */     ArrayList<Node> arrayList1 = Lists.newArrayList();
/* 230 */     int i = (int)Math.pow(6.0D, Math.floor(Math.log(paramList.size() - 0.01D) / Math.log(6.0D)));
/* 231 */     for (Node<T> node : paramList) {
/* 232 */       arrayList1.add(node);
/* 233 */       if (arrayList1.size() >= i) {
/* 234 */         arrayList.add(new SubTree((List)arrayList1));
/* 235 */         arrayList1 = Lists.newArrayList();
/*     */       } 
/*     */     } 
/* 238 */     if (!arrayList1.isEmpty()) {
/* 239 */       arrayList.add(new SubTree((List)arrayList1));
/*     */     }
/* 241 */     return arrayList;
/*     */   }
/*     */   
/*     */   private static long cost(Climate.Parameter[] paramArrayOfParameter) {
/* 245 */     long l = 0L;
/* 246 */     for (Climate.Parameter parameter : paramArrayOfParameter) {
/* 247 */       l += Math.abs(parameter.max() - parameter.min());
/*     */     }
/* 249 */     return l;
/*     */   }
/*     */   
/*     */   static <T> List<Climate.Parameter> buildParameterSpace(List<? extends Node<T>> paramList) {
/* 253 */     if (paramList.isEmpty()) {
/* 254 */       throw new IllegalArgumentException("SubTree needs at least one child");
/*     */     }
/* 256 */     byte b1 = 7;
/* 257 */     ArrayList<Climate.Parameter> arrayList = Lists.newArrayList();
/* 258 */     for (byte b2 = 0; b2 < 7; b2++) {
/* 259 */       arrayList.add((Object)null);
/*     */     }
/* 261 */     for (Node<T> node : paramList) {
/* 262 */       for (byte b = 0; b < 7; b++) {
/* 263 */         arrayList.set(b, node.parameterSpace[b].span(arrayList.get(b)));
/*     */       }
/*     */     } 
/* 266 */     return arrayList;
/*     */   }
/*     */   
/*     */   public T search(Climate.TargetPoint paramTargetPoint, Climate.DistanceMetric<T> paramDistanceMetric) {
/* 270 */     long[] arrayOfLong = paramTargetPoint.toParameterArray();
/* 271 */     Leaf<T> leaf = this.root.search(arrayOfLong, this.lastResult.get(), paramDistanceMetric);
/* 272 */     this.lastResult.set(leaf);
/* 273 */     return leaf.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\Climate$RTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */