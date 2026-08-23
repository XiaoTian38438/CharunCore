/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TraderLlamaDefendWanderingTraderGoal
/*     */   extends TargetGoal
/*     */ {
/*     */   private final Llama llama;
/*     */   private LivingEntity ownerLastHurtBy;
/*     */   private int timestamp;
/*     */   
/*     */   public TraderLlamaDefendWanderingTraderGoal(Llama paramLlama) {
/* 137 */     super((Mob)paramLlama, false);
/* 138 */     this.llama = paramLlama;
/* 139 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 144 */     if (!this.llama.isLeashed()) {
/* 145 */       return false;
/*     */     }
/* 147 */     Entity entity = this.llama.getLeashHolder();
/* 148 */     if (!(entity instanceof WanderingTrader)) {
/* 149 */       return false;
/*     */     }
/*     */     
/* 152 */     WanderingTrader wanderingTrader = (WanderingTrader)entity;
/* 153 */     this.ownerLastHurtBy = wanderingTrader.getLastHurtByMob();
/* 154 */     int i = wanderingTrader.getLastHurtByMobTimestamp();
/* 155 */     return (i != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 160 */     this.mob.setTarget(this.ownerLastHurtBy);
/*     */     
/* 162 */     Entity entity = this.llama.getLeashHolder();
/* 163 */     if (entity instanceof WanderingTrader) {
/* 164 */       this.timestamp = ((WanderingTrader)entity).getLastHurtByMobTimestamp();
/*     */     }
/*     */     
/* 167 */     super.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\TraderLlama$TraderLlamaDefendWanderingTraderGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */