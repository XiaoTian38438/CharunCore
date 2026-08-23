/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FleeSunGoal
/*    */   extends Goal {
/*    */   protected final PathfinderMob mob;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   private final Level level;
/*    */   
/*    */   public FleeSunGoal(PathfinderMob paramPathfinderMob, double paramDouble) {
/* 22 */     this.mob = paramPathfinderMob;
/* 23 */     this.speedModifier = paramDouble;
/* 24 */     this.level = paramPathfinderMob.level();
/* 25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 30 */     if (this.mob.getTarget() != null) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (!this.level.isBrightOutside()) {
/* 34 */       return false;
/*    */     }
/* 36 */     if (!this.mob.isOnFire()) {
/* 37 */       return false;
/*    */     }
/* 39 */     if (!this.level.canSeeSky(this.mob.blockPosition())) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (!this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     return setWantedPos();
/*    */   }
/*    */   
/*    */   protected boolean setWantedPos() {
/* 50 */     Vec3 vec3 = getHidePos();
/* 51 */     if (vec3 == null) {
/* 52 */       return false;
/*    */     }
/* 54 */     this.wantedX = vec3.x;
/* 55 */     this.wantedY = vec3.y;
/* 56 */     this.wantedZ = vec3.z;
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 62 */     return !this.mob.getNavigation().isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 67 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*    */   }
/*    */   
/*    */   protected Vec3 getHidePos() {
/* 71 */     RandomSource randomSource = this.mob.getRandom();
/* 72 */     BlockPos blockPos = this.mob.blockPosition();
/*    */     
/* 74 */     for (byte b = 0; b < 10; b++) {
/* 75 */       BlockPos blockPos1 = blockPos.offset(randomSource.nextInt(20) - 10, randomSource.nextInt(6) - 3, randomSource.nextInt(20) - 10);
/*    */       
/* 77 */       if (!this.level.canSeeSky(blockPos1) && this.mob.getWalkTargetValue(blockPos1) < 0.0F) {
/* 78 */         return Vec3.atBottomCenterOf((Vec3i)blockPos1);
/*    */       }
/*    */     } 
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\FleeSunGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */