/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class StrollThroughVillageGoal
/*    */   extends Goal {
/*    */   private static final int DISTANCE_THRESHOLD = 10;
/*    */   private final PathfinderMob mob;
/*    */   private final int interval;
/*    */   private BlockPos wantedPos;
/*    */   
/*    */   public StrollThroughVillageGoal(PathfinderMob paramPathfinderMob, int paramInt) {
/* 24 */     this.mob = paramPathfinderMob;
/* 25 */     this.interval = reducedTickDelay(paramInt);
/* 26 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 31 */     if (this.mob.hasControllingPassenger()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     if (this.mob.level().isBrightOutside()) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     if (this.mob.getRandom().nextInt(this.interval) != 0) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/*    */     
/* 45 */     BlockPos blockPos = this.mob.blockPosition();
/* 46 */     if (!serverLevel.isCloseToVillage(blockPos, 6)) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7, paramBlockPos -> -paramServerLevel.sectionsToVillage(SectionPos.of(paramBlockPos)));
/* 51 */     this.wantedPos = (vec3 == null) ? null : BlockPos.containing((Position)vec3);
/* 52 */     return (this.wantedPos != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 57 */     return (this.wantedPos != null && !this.mob.getNavigation().isDone() && this.mob.getNavigation().getTargetPos().equals(this.wantedPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 62 */     if (this.wantedPos == null) {
/*    */       return;
/*    */     }
/* 65 */     PathNavigation pathNavigation = this.mob.getNavigation();
/* 66 */     if (pathNavigation.isDone() && 
/* 67 */       !this.wantedPos.closerToCenterThan((Position)this.mob.position(), 10.0D)) {
/* 68 */       Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)this.wantedPos);
/*    */ 
/*    */       
/* 71 */       Vec3 vec32 = this.mob.position();
/* 72 */       Vec3 vec33 = vec32.subtract(vec31);
/*    */       
/* 74 */       vec31 = vec33.scale(0.4D).add(vec31);
/*    */       
/* 76 */       Vec3 vec34 = vec31.subtract(vec32).normalize().scale(10.0D).add(vec32);
/* 77 */       BlockPos blockPos = BlockPos.containing((Position)vec34);
/* 78 */       blockPos = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos);
/*    */       
/* 80 */       if (!pathNavigation.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D))
/*    */       {
/* 82 */         moveRandomly();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void moveRandomly() {
/* 89 */     RandomSource randomSource = this.mob.getRandom();
/* 90 */     BlockPos blockPos = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + randomSource.nextInt(16), 0, -8 + randomSource.nextInt(16)));
/* 91 */     this.mob.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\StrollThroughVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */