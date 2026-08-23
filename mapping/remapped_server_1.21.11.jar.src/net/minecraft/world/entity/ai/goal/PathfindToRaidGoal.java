/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.EnumSet;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.entity.raid.Raids;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class PathfindToRaidGoal<T extends Raider>
/*    */   extends Goal {
/*    */   private static final int RECRUITMENT_SEARCH_TICK_DELAY = 20;
/*    */   private static final float SPEED_MODIFIER = 1.0F;
/*    */   private final T mob;
/*    */   private int recruitmentTick;
/*    */   
/*    */   public PathfindToRaidGoal(T paramT) {
/* 24 */     this.mob = paramT;
/* 25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 30 */     return (this.mob.getTarget() == null && 
/* 31 */       !this.mob.hasControllingPassenger() && this.mob
/* 32 */       .hasActiveRaid() && 
/* 33 */       !this.mob.getCurrentRaid().isOver() && 
/* 34 */       !getServerLevel(this.mob.level()).isVillage(this.mob.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 39 */     return (this.mob.hasActiveRaid() && 
/* 40 */       !this.mob.getCurrentRaid().isOver() && 
/* 41 */       !getServerLevel(this.mob.level()).isVillage(this.mob.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 46 */     if (this.mob.hasActiveRaid()) {
/* 47 */       Raid raid = this.mob.getCurrentRaid();
/* 48 */       if (((Raider)this.mob).tickCount > this.recruitmentTick) {
/* 49 */         this.recruitmentTick = ((Raider)this.mob).tickCount + 20;
/* 50 */         recruitNearby(raid);
/*    */       } 
/*    */       
/* 53 */       if (!this.mob.isPathFinding()) {
/* 54 */         Vec3 vec3 = DefaultRandomPos.getPosTowards((PathfinderMob)this.mob, 15, 4, Vec3.atBottomCenterOf((Vec3i)raid.getCenter()), 1.5707963705062866D);
/* 55 */         if (vec3 != null) {
/* 56 */           this.mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.0D);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void recruitNearby(Raid paramRaid) {
/* 63 */     if (paramRaid.isActive()) {
/* 64 */       ServerLevel serverLevel = getServerLevel(this.mob.level());
/* 65 */       HashSet hashSet = Sets.newHashSet();
/*    */       
/* 67 */       List list = serverLevel.getEntitiesOfClass(Raider.class, this.mob.getBoundingBox().inflate(16.0D), paramRaider -> (!paramRaider.hasActiveRaid() && Raids.canJoinRaid(paramRaider)));
/* 68 */       hashSet.addAll(list);
/*    */       
/* 70 */       for (Raider raider : hashSet)
/* 71 */         paramRaid.joinRaid(serverLevel, paramRaid.getGroupsSpawned(), raider, null, true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\PathfindToRaidGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */