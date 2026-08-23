/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class BegGoal
/*    */   extends Goal {
/*    */   private final Wolf wolf;
/*    */   private Player player;
/*    */   private final ServerLevel level;
/*    */   private final float lookDistance;
/*    */   private int lookTime;
/*    */   private final TargetingConditions begTargeting;
/*    */   
/*    */   public BegGoal(Wolf paramWolf, float paramFloat) {
/* 24 */     this.wolf = paramWolf;
/* 25 */     this.level = getServerLevel((Entity)paramWolf);
/* 26 */     this.lookDistance = paramFloat;
/* 27 */     this.begTargeting = TargetingConditions.forNonCombat().range(paramFloat);
/* 28 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 33 */     this.player = this.level.getNearestPlayer(this.begTargeting, (LivingEntity)this.wolf);
/* 34 */     if (this.player == null) {
/* 35 */       return false;
/*    */     }
/* 37 */     return playerHoldingInteresting(this.player);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 42 */     if (!this.player.isAlive()) {
/* 43 */       return false;
/*    */     }
/* 45 */     if (this.wolf.distanceToSqr((Entity)this.player) > (this.lookDistance * this.lookDistance)) {
/* 46 */       return false;
/*    */     }
/* 48 */     return (this.lookTime > 0 && playerHoldingInteresting(this.player));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 53 */     this.wolf.setIsInterested(true);
/* 54 */     this.lookTime = adjustedTickDelay(40 + this.wolf.getRandom().nextInt(40));
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 59 */     this.wolf.setIsInterested(false);
/* 60 */     this.player = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 65 */     this.wolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, this.wolf.getMaxHeadXRot());
/* 66 */     this.lookTime--;
/*    */   }
/*    */   
/*    */   private boolean playerHoldingInteresting(Player paramPlayer) {
/* 70 */     for (InteractionHand interactionHand : InteractionHand.values()) {
/* 71 */       ItemStack itemStack = paramPlayer.getItemInHand(interactionHand);
/* 72 */       if (itemStack.is(Items.BONE) || this.wolf.isFood(itemStack)) {
/* 73 */         return true;
/*    */       }
/*    */     } 
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\BegGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */