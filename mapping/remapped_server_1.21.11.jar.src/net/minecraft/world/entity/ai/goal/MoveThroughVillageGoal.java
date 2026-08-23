/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MoveThroughVillageGoal
/*     */   extends Goal
/*     */ {
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*  32 */   private final List<BlockPos> visited = Lists.newArrayList(); private Path path; private BlockPos poiPos; private final boolean onlyAtNight;
/*     */   private final int distanceToPoi;
/*     */   private final BooleanSupplier canDealWithDoors;
/*     */   
/*     */   public MoveThroughVillageGoal(PathfinderMob paramPathfinderMob, double paramDouble, boolean paramBoolean, int paramInt, BooleanSupplier paramBooleanSupplier) {
/*  37 */     this.mob = paramPathfinderMob;
/*  38 */     this.speedModifier = paramDouble;
/*  39 */     this.onlyAtNight = paramBoolean;
/*  40 */     this.distanceToPoi = paramInt;
/*  41 */     this.canDealWithDoors = paramBooleanSupplier;
/*  42 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     
/*  44 */     if (!GoalUtils.hasGroundPathNavigation((Mob)paramPathfinderMob)) {
/*  45 */       throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  51 */     if (!GoalUtils.hasGroundPathNavigation((Mob)this.mob)) {
/*  52 */       return false;
/*     */     }
/*  54 */     updateVisited();
/*     */     
/*  56 */     if (this.onlyAtNight && this.mob.level().isBrightOutside()) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/*  61 */     BlockPos blockPos = this.mob.blockPosition();
/*     */     
/*  63 */     if (!serverLevel.isCloseToVillage(blockPos, 6)) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7, paramBlockPos2 -> {
/*     */           if (!paramServerLevel.isVillage(paramBlockPos2)) {
/*     */             return Double.NEGATIVE_INFINITY;
/*     */           }
/*     */           Optional optional = paramServerLevel.getPoiManager().find((), this::hasNotVisited, paramBlockPos2, 10, PoiManager.Occupancy.IS_OCCUPIED);
/*     */           return ((Double)optional.map(()).orElse(Double.valueOf(Double.NEGATIVE_INFINITY))).doubleValue();
/*     */         });
/*  74 */     if (vec3 == null) {
/*  75 */       return false;
/*     */     }
/*  77 */     Optional<BlockPos> optional = serverLevel.getPoiManager().find(paramHolder -> paramHolder.is(PoiTypeTags.VILLAGE), this::hasNotVisited, BlockPos.containing((Position)vec3), 10, PoiManager.Occupancy.IS_OCCUPIED);
/*  78 */     if (optional.isEmpty()) {
/*  79 */       return false;
/*     */     }
/*  81 */     this.poiPos = ((BlockPos)optional.get()).immutable();
/*     */     
/*  83 */     PathNavigation pathNavigation = this.mob.getNavigation();
/*  84 */     pathNavigation.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
/*  85 */     this.path = pathNavigation.createPath(this.poiPos, 0);
/*  86 */     pathNavigation.setCanOpenDoors(true);
/*  87 */     if (this.path == null) {
/*  88 */       Vec3 vec31 = DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)this.poiPos), 1.5707963705062866D);
/*  89 */       if (vec31 == null) {
/*  90 */         return false;
/*     */       }
/*  92 */       pathNavigation.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
/*  93 */       this.path = this.mob.getNavigation().createPath(vec31.x, vec31.y, vec31.z, 0);
/*  94 */       pathNavigation.setCanOpenDoors(true);
/*  95 */       if (this.path == null) {
/*  96 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 100 */     for (byte b = 0; b < this.path.getNodeCount(); b++) {
/* 101 */       Node node = this.path.getNode(b);
/* 102 */       BlockPos blockPos1 = new BlockPos(node.x, node.y + 1, node.z);
/* 103 */       if (DoorBlock.isWoodenDoor(this.mob.level(), blockPos1)) {
/*     */         
/* 105 */         this.path = this.mob.getNavigation().createPath(node.x, node.y, node.z, 0);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 110 */     return (this.path != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 115 */     if (this.mob.getNavigation().isDone()) {
/* 116 */       return false;
/*     */     }
/* 118 */     return !this.poiPos.closerToCenterThan((Position)this.mob.position(), (this.mob.getBbWidth() + this.distanceToPoi));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 123 */     this.mob.getNavigation().moveTo(this.path, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 128 */     if (this.mob.getNavigation().isDone() || this.poiPos.closerToCenterThan((Position)this.mob.position(), this.distanceToPoi)) {
/* 129 */       this.visited.add(this.poiPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasNotVisited(BlockPos paramBlockPos) {
/* 134 */     for (BlockPos blockPos : this.visited) {
/* 135 */       if (Objects.equals(paramBlockPos, blockPos)) {
/* 136 */         return false;
/*     */       }
/*     */     } 
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   private void updateVisited() {
/* 143 */     if (this.visited.size() > 15)
/* 144 */       this.visited.remove(0); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\MoveThroughVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */