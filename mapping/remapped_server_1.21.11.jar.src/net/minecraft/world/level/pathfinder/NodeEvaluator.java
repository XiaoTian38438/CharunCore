/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NodeEvaluator
/*     */ {
/*     */   protected PathfindingContext currentContext;
/*     */   protected Mob mob;
/*  22 */   protected final Int2ObjectMap<Node> nodes = (Int2ObjectMap<Node>)new Int2ObjectOpenHashMap();
/*     */   
/*     */   protected int entityWidth;
/*     */   
/*     */   protected int entityHeight;
/*     */   
/*     */   protected int entityDepth;
/*     */   
/*     */   protected boolean canPassDoors = true;
/*     */   protected boolean canOpenDoors;
/*     */   protected boolean canFloat;
/*     */   protected boolean canWalkOverFences;
/*     */   
/*     */   public void prepare(PathNavigationRegion paramPathNavigationRegion, Mob paramMob) {
/*  36 */     this.currentContext = new PathfindingContext((CollisionGetter)paramPathNavigationRegion, paramMob);
/*  37 */     this.mob = paramMob;
/*  38 */     this.nodes.clear();
/*     */     
/*  40 */     this.entityWidth = Mth.floor(paramMob.getBbWidth() + 1.0F);
/*  41 */     this.entityHeight = Mth.floor(paramMob.getBbHeight() + 1.0F);
/*  42 */     this.entityDepth = Mth.floor(paramMob.getBbWidth() + 1.0F);
/*     */   }
/*     */   
/*     */   public void done() {
/*  46 */     this.currentContext = null;
/*  47 */     this.mob = null;
/*     */   }
/*     */   
/*     */   protected Node getNode(BlockPos paramBlockPos) {
/*  51 */     return getNode(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   protected Node getNode(int paramInt1, int paramInt2, int paramInt3) {
/*  55 */     return (Node)this.nodes.computeIfAbsent(Node.createHash(paramInt1, paramInt2, paramInt3), paramInt4 -> new Node(paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Target getTargetNodeAt(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  63 */     return new Target(getNode(Mth.floor(paramDouble1), Mth.floor(paramDouble2), Mth.floor(paramDouble3)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathType(Mob paramMob, BlockPos paramBlockPos) {
/*  73 */     return getPathType(new PathfindingContext((CollisionGetter)paramMob.level(), paramMob), paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   public void setCanPassDoors(boolean paramBoolean) {
/*  77 */     this.canPassDoors = paramBoolean;
/*     */   }
/*     */   
/*     */   public void setCanOpenDoors(boolean paramBoolean) {
/*  81 */     this.canOpenDoors = paramBoolean;
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean paramBoolean) {
/*  85 */     this.canFloat = paramBoolean;
/*     */   }
/*     */   
/*     */   public void setCanWalkOverFences(boolean paramBoolean) {
/*  89 */     this.canWalkOverFences = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean canPassDoors() {
/*  93 */     return this.canPassDoors;
/*     */   }
/*     */   
/*     */   public boolean canOpenDoors() {
/*  97 */     return this.canOpenDoors;
/*     */   }
/*     */   
/*     */   public boolean canFloat() {
/* 101 */     return this.canFloat;
/*     */   }
/*     */   
/*     */   public boolean canWalkOverFences() {
/* 105 */     return this.canWalkOverFences;
/*     */   }
/*     */   
/*     */   public static boolean isBurningBlock(BlockState paramBlockState) {
/* 109 */     return (paramBlockState.is(BlockTags.FIRE) || paramBlockState
/* 110 */       .is(Blocks.LAVA) || paramBlockState
/* 111 */       .is(Blocks.MAGMA_BLOCK) || 
/* 112 */       CampfireBlock.isLitCampfire(paramBlockState) || paramBlockState
/* 113 */       .is(Blocks.LAVA_CAULDRON));
/*     */   }
/*     */   
/*     */   public abstract Node getStart();
/*     */   
/*     */   public abstract Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3);
/*     */   
/*     */   public abstract int getNeighbors(Node[] paramArrayOfNode, Node paramNode);
/*     */   
/*     */   public abstract PathType getPathTypeOfMob(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, Mob paramMob);
/*     */   
/*     */   public abstract PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\NodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */