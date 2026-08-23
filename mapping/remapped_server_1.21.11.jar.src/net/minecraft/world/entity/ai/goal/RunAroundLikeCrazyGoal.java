/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RunAroundLikeCrazyGoal
/*    */   extends Goal {
/*    */   private final AbstractHorse horse;
/*    */   private final double speedModifier;
/*    */   private double posX;
/*    */   private double posY;
/*    */   private double posZ;
/*    */   
/*    */   public RunAroundLikeCrazyGoal(AbstractHorse paramAbstractHorse, double paramDouble) {
/* 20 */     this.horse = paramAbstractHorse;
/* 21 */     this.speedModifier = paramDouble;
/* 22 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     if (this.horse.isMobControlled() || this.horse.isTamed() || !this.horse.isVehicle()) {
/* 28 */       return false;
/*    */     }
/* 30 */     Vec3 vec3 = DefaultRandomPos.getPos((PathfinderMob)this.horse, 5, 4);
/* 31 */     if (vec3 == null) {
/* 32 */       return false;
/*    */     }
/* 34 */     this.posX = vec3.x;
/* 35 */     this.posY = vec3.y;
/* 36 */     this.posZ = vec3.z;
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 42 */     this.horse.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 47 */     return (!this.horse.isTamed() && !this.horse.getNavigation().isDone() && this.horse.isVehicle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 52 */     if (!this.horse.isTamed() && this.horse.getRandom().nextInt(adjustedTickDelay(50)) == 0) {
/* 53 */       Entity entity = this.horse.getFirstPassenger();
/* 54 */       if (entity == null) {
/*    */         return;
/*    */       }
/*    */       
/* 58 */       if (entity instanceof Player) { Player player = (Player)entity;
/* 59 */         int i = this.horse.getTemper();
/* 60 */         int j = this.horse.getMaxTemper();
/* 61 */         if (j > 0 && this.horse.getRandom().nextInt(j) < i) {
/* 62 */           this.horse.tameWithName(player);
/*    */           return;
/*    */         } 
/* 65 */         this.horse.modifyTemper(5); }
/*    */ 
/*    */       
/* 68 */       this.horse.ejectPassengers();
/* 69 */       this.horse.makeMad();
/* 70 */       this.horse.level().broadcastEntityEvent((Entity)this.horse, (byte)6);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RunAroundLikeCrazyGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */