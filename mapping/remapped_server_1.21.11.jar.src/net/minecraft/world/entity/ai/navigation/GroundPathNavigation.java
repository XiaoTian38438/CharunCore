/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GroundPathNavigation extends PathNavigation {
/*     */   private boolean avoidSun;
/*     */   
/*     */   public GroundPathNavigation(Mob paramMob, Level paramLevel) {
/*  25 */     super(paramMob, paramLevel);
/*     */   }
/*     */   private boolean canPathToTargetsBelowSurface;
/*     */   
/*     */   protected PathFinder createPathFinder(int paramInt) {
/*  30 */     this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/*  31 */     return new PathFinder(this.nodeEvaluator, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canUpdatePath() {
/*  36 */     return (this.mob.onGround() || this.mob.isInLiquid() || this.mob.isPassenger());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getTempMobPos() {
/*  41 */     return new Vec3(this.mob.getX(), getSurfaceY(), this.mob.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createPath(BlockPos paramBlockPos, int paramInt) {
/*  46 */     LevelChunk levelChunk = this.level.getChunkSource().getChunkNow(SectionPos.blockToSectionCoord(paramBlockPos.getX()), SectionPos.blockToSectionCoord(paramBlockPos.getZ()));
/*  47 */     if (levelChunk == null) {
/*  48 */       return null;
/*     */     }
/*     */     
/*  51 */     if (!this.canPathToTargetsBelowSurface) {
/*  52 */       paramBlockPos = findSurfacePosition(levelChunk, paramBlockPos, paramInt);
/*     */     }
/*     */     
/*  55 */     return super.createPath(paramBlockPos, paramInt);
/*     */   }
/*     */   final BlockPos findSurfacePosition(LevelChunk paramLevelChunk, BlockPos paramBlockPos, int paramInt) {
/*     */     BlockPos.MutableBlockPos mutableBlockPos;
/*  59 */     if (paramLevelChunk.getBlockState(paramBlockPos).isAir()) {
/*  60 */       BlockPos.MutableBlockPos mutableBlockPos1 = paramBlockPos.mutable().move(Direction.DOWN);
/*  61 */       while (mutableBlockPos1.getY() >= this.level.getMinY() && paramLevelChunk.getBlockState((BlockPos)mutableBlockPos1).isAir()) {
/*  62 */         mutableBlockPos1.move(Direction.DOWN);
/*     */       }
/*     */       
/*  65 */       if (mutableBlockPos1.getY() >= this.level.getMinY()) {
/*  66 */         return mutableBlockPos1.above();
/*     */       }
/*     */       
/*  69 */       mutableBlockPos1.setY(paramBlockPos.getY() + 1);
/*  70 */       while (mutableBlockPos1.getY() <= this.level.getMaxY() && paramLevelChunk.getBlockState((BlockPos)mutableBlockPos1).isAir()) {
/*  71 */         mutableBlockPos1.move(Direction.UP);
/*     */       }
/*  73 */       mutableBlockPos = mutableBlockPos1;
/*     */     } 
/*     */     
/*  76 */     if (paramLevelChunk.getBlockState((BlockPos)mutableBlockPos).isSolid()) {
/*  77 */       BlockPos.MutableBlockPos mutableBlockPos1 = mutableBlockPos.mutable().move(Direction.UP);
/*  78 */       while (mutableBlockPos1.getY() <= this.level.getMaxY() && paramLevelChunk.getBlockState((BlockPos)mutableBlockPos1).isSolid()) {
/*  79 */         mutableBlockPos1.move(Direction.UP);
/*     */       }
/*  81 */       return mutableBlockPos1.immutable();
/*     */     } 
/*  83 */     return (BlockPos)mutableBlockPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createPath(Entity paramEntity, int paramInt) {
/*  88 */     return createPath(paramEntity.blockPosition(), paramInt);
/*     */   }
/*     */   
/*     */   private int getSurfaceY() {
/*  92 */     if (!this.mob.isInWater() || !canFloat()) {
/*  93 */       return Mth.floor(this.mob.getY() + 0.5D);
/*     */     }
/*     */ 
/*     */     
/*  97 */     int i = this.mob.getBlockY();
/*  98 */     BlockState blockState = this.level.getBlockState(BlockPos.containing(this.mob.getX(), i, this.mob.getZ()));
/*  99 */     byte b = 0;
/* 100 */     while (blockState.is(Blocks.WATER)) {
/* 101 */       i++;
/* 102 */       blockState = this.level.getBlockState(BlockPos.containing(this.mob.getX(), i, this.mob.getZ()));
/* 103 */       if (++b > 16) {
/* 104 */         return this.mob.getBlockY();
/*     */       }
/*     */     } 
/* 107 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void trimPath() {
/* 112 */     super.trimPath();
/*     */     
/* 114 */     if (this.avoidSun) {
/* 115 */       if (this.level.canSeeSky(BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()))) {
/*     */         return;
/*     */       }
/*     */       
/* 119 */       for (byte b = 0; b < this.path.getNodeCount(); b++) {
/* 120 */         Node node = this.path.getNode(b);
/* 121 */         if (this.level.canSeeSky(new BlockPos(node.x, node.y, node.z))) {
/* 122 */           this.path.truncateNodes(b);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canNavigateGround() {
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean hasValidPathType(PathType paramPathType) {
/* 135 */     if (paramPathType == PathType.WATER) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     if (paramPathType == PathType.LAVA) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     if (paramPathType == PathType.OPEN) {
/* 144 */       return false;
/*     */     }
/*     */     
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   public void setAvoidSun(boolean paramBoolean) {
/* 151 */     this.avoidSun = paramBoolean;
/*     */   }
/*     */   
/*     */   public void setCanWalkOverFences(boolean paramBoolean) {
/* 155 */     this.nodeEvaluator.setCanWalkOverFences(paramBoolean);
/*     */   }
/*     */   
/*     */   public void setCanPathToTargetsBelowSurface(boolean paramBoolean) {
/* 159 */     this.canPathToTargetsBelowSurface = paramBoolean;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\navigation\GroundPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */