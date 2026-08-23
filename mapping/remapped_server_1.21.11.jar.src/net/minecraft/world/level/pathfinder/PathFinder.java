/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathFinder
/*     */ {
/*     */   private static final float FUDGING = 1.5F;
/*  28 */   private final Node[] neighbors = new Node[32];
/*     */   
/*     */   private int maxVisitedNodes;
/*     */   private final NodeEvaluator nodeEvaluator;
/*  32 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   private BooleanSupplier captureDebug = () -> false;
/*     */   
/*     */   public PathFinder(NodeEvaluator paramNodeEvaluator, int paramInt) {
/*  36 */     this.nodeEvaluator = paramNodeEvaluator;
/*  37 */     this.maxVisitedNodes = paramInt;
/*     */   }
/*     */   
/*     */   public void setCaptureDebug(BooleanSupplier paramBooleanSupplier) {
/*  41 */     this.captureDebug = paramBooleanSupplier;
/*     */   }
/*     */   
/*     */   public void setMaxVisitedNodes(int paramInt) {
/*  45 */     this.maxVisitedNodes = paramInt;
/*     */   }
/*     */   
/*     */   public Path findPath(PathNavigationRegion paramPathNavigationRegion, Mob paramMob, Set<BlockPos> paramSet, float paramFloat1, int paramInt, float paramFloat2) {
/*  49 */     this.openSet.clear();
/*  50 */     this.nodeEvaluator.prepare(paramPathNavigationRegion, paramMob);
/*  51 */     Node node = this.nodeEvaluator.getStart();
/*  52 */     if (node == null) {
/*  53 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  57 */     Map<Target, BlockPos> map = (Map)paramSet.stream().collect(Collectors.toMap(paramBlockPos -> this.nodeEvaluator.getTarget(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ()), Function.identity()));
/*  58 */     Path path = findPath(node, map, paramFloat1, paramInt, paramFloat2);
/*     */     
/*  60 */     this.nodeEvaluator.done();
/*  61 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path findPath(Node paramNode, Map<Target, BlockPos> paramMap, float paramFloat1, int paramInt, float paramFloat2) {
/*  70 */     ProfilerFiller profilerFiller = Profiler.get();
/*  71 */     profilerFiller.push("find_path");
/*  72 */     profilerFiller.markForCharting(MetricCategory.PATH_FINDING);
/*  73 */     Set<Target> set = paramMap.keySet();
/*     */     
/*  75 */     paramNode.g = 0.0F;
/*  76 */     paramNode.h = getBestH(paramNode, set);
/*  77 */     paramNode.f = paramNode.h;
/*     */     
/*  79 */     this.openSet.clear();
/*  80 */     this.openSet.insert(paramNode);
/*     */     
/*  82 */     boolean bool = this.captureDebug.getAsBoolean();
/*  83 */     Set<Node> set1 = bool ? new HashSet() : Set.of();
/*     */ 
/*     */     
/*  86 */     byte b = 0;
/*     */     
/*  88 */     HashSet<Target> hashSet = Sets.newHashSetWithExpectedSize(set.size());
/*     */     
/*  90 */     int i = (int)(this.maxVisitedNodes * paramFloat2);
/*  91 */     while (!this.openSet.isEmpty() && ++b < i) {
/*  92 */       Node node = this.openSet.pop();
/*  93 */       node.closed = true;
/*     */ 
/*     */       
/*  96 */       for (Target target : set) {
/*  97 */         if (node.distanceManhattan(target) <= paramInt) {
/*  98 */           target.setReached();
/*  99 */           hashSet.add(target);
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       if (!hashSet.isEmpty()) {
/*     */         break;
/*     */       }
/*     */       
/* 107 */       if (bool) {
/* 108 */         set1.add(node);
/*     */       }
/*     */       
/* 111 */       if (node.distanceTo(paramNode) >= paramFloat1) {
/*     */         continue;
/*     */       }
/*     */       
/* 115 */       int j = this.nodeEvaluator.getNeighbors(this.neighbors, node);
/* 116 */       for (byte b1 = 0; b1 < j; b1++) {
/* 117 */         Node node1 = this.neighbors[b1];
/*     */         
/* 119 */         float f1 = distance(node, node1);
/* 120 */         node.walkedDistance += f1;
/*     */         
/* 122 */         float f2 = node.g + f1 + node1.costMalus;
/* 123 */         if (node1.walkedDistance < paramFloat1 && (!node1.inOpenSet() || f2 < node1.g)) {
/* 124 */           node1.cameFrom = node;
/* 125 */           node1.g = f2;
/* 126 */           node1.h = getBestH(node1, set) * 1.5F;
/*     */           
/* 128 */           if (node1.inOpenSet()) {
/* 129 */             this.openSet.changeCost(node1, node1.g + node1.h);
/*     */           } else {
/* 131 */             node1.f = node1.g + node1.h;
/* 132 */             this.openSet.insert(node1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     Optional<T> optional = !hashSet.isEmpty() ? hashSet.stream().map(paramTarget -> reconstructPath(paramTarget.getBestNode(), (BlockPos)paramMap.get(paramTarget), true)).min(Comparator.comparingInt(Path::getNodeCount)) : set.stream().map(paramTarget -> reconstructPath(paramTarget.getBestNode(), (BlockPos)paramMap.get(paramTarget), false)).min(Comparator.<T>comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));
/*     */     
/* 146 */     profilerFiller.pop();
/* 147 */     if (optional.isEmpty()) {
/* 148 */       return null;
/*     */     }
/* 150 */     Path path = (Path)optional.get();
/*     */     
/* 152 */     if (bool) {
/* 153 */       path.setDebug(this.openSet.getHeap(), (Node[])set1.toArray(paramInt -> new Node[paramInt]), set);
/*     */     }
/* 155 */     return path;
/*     */   }
/*     */   
/*     */   protected float distance(Node paramNode1, Node paramNode2) {
/* 159 */     return paramNode1.distanceTo(paramNode2);
/*     */   }
/*     */ 
/*     */   
/*     */   private float getBestH(Node paramNode, Set<Target> paramSet) {
/* 164 */     float f = Float.MAX_VALUE;
/* 165 */     for (Target target : paramSet) {
/* 166 */       float f1 = paramNode.distanceTo(target);
/* 167 */       target.updateBest(f1, paramNode);
/* 168 */       f = Math.min(f1, f);
/*     */     } 
/* 170 */     return f;
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node paramNode, BlockPos paramBlockPos, boolean paramBoolean) {
/* 174 */     ArrayList<Node> arrayList = Lists.newArrayList();
/* 175 */     Node node = paramNode;
/* 176 */     arrayList.add(0, node);
/* 177 */     while (node.cameFrom != null) {
/* 178 */       node = node.cameFrom;
/* 179 */       arrayList.add(0, node);
/*     */     } 
/* 181 */     return new Path(arrayList, paramBlockPos, paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\PathFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */