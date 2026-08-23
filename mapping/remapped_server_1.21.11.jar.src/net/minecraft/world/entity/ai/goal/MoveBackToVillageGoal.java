/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveBackToVillageGoal
/*    */   extends RandomStrollGoal {
/*    */   private static final int MAX_XZ_DIST = 10;
/*    */   private static final int MAX_Y_DIST = 7;
/*    */   
/*    */   public MoveBackToVillageGoal(PathfinderMob paramPathfinderMob, double paramDouble, boolean paramBoolean) {
/* 18 */     super(paramPathfinderMob, paramDouble, 10, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/* 24 */     BlockPos blockPos = this.mob.blockPosition();
/*    */     
/* 26 */     if (serverLevel.isVillage(blockPos)) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     return super.canUse();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 35 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/* 36 */     BlockPos blockPos = this.mob.blockPosition();
/*    */     
/* 38 */     SectionPos sectionPos1 = SectionPos.of(blockPos);
/* 39 */     SectionPos sectionPos2 = BehaviorUtils.findSectionClosestToVillage(serverLevel, sectionPos1, 2);
/*    */     
/* 41 */     if (sectionPos2 != sectionPos1) {
/* 42 */       return DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)sectionPos2.center()), 1.5707963705062866D);
/*    */     }
/*    */     
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\MoveBackToVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */