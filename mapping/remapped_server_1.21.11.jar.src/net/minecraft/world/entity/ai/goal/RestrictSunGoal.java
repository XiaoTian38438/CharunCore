/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*    */ 
/*    */ public class RestrictSunGoal extends Goal {
/*    */   public RestrictSunGoal(PathfinderMob paramPathfinderMob) {
/* 12 */     this.mob = paramPathfinderMob;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 17 */     return (this.mob.level().isBrightOutside() && this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && GoalUtils.hasGroundPathNavigation((Mob)this.mob));
/*    */   }
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public void start() {
/* 22 */     PathNavigation pathNavigation = this.mob.getNavigation(); if (pathNavigation instanceof GroundPathNavigation) { GroundPathNavigation groundPathNavigation = (GroundPathNavigation)pathNavigation;
/* 23 */       groundPathNavigation.setAvoidSun(true); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 29 */     if (GoalUtils.hasGroundPathNavigation((Mob)this.mob)) { PathNavigation pathNavigation = this.mob.getNavigation(); if (pathNavigation instanceof GroundPathNavigation) { GroundPathNavigation groundPathNavigation = (GroundPathNavigation)pathNavigation;
/* 30 */         groundPathNavigation.setAvoidSun(false); }
/*    */        }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RestrictSunGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */