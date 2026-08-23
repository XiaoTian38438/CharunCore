/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class OfferFlowerGoal
/*    */   extends Goal {
/* 17 */   private static final TargetingConditions OFFER_TARGET_CONTEXT = TargetingConditions.forNonCombat().range(6.0D);
/* 18 */   private static final Item OFFER_ITEM = Items.POPPY;
/*    */   
/*    */   public static final int OFFER_TICKS = 400;
/*    */   
/*    */   private final IronGolem golem;
/*    */   private LivingEntity entity;
/*    */   private int tick;
/*    */   
/*    */   public OfferFlowerGoal(IronGolem paramIronGolem) {
/* 27 */     this.golem = paramIronGolem;
/* 28 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 33 */     if (!this.golem.level().isBrightOutside()) {
/* 34 */       return false;
/*    */     }
/* 36 */     if (this.golem.getRandom().nextInt(8000) != 0) {
/* 37 */       return false;
/*    */     }
/* 39 */     this.entity = getServerLevel((Entity)this.golem).getNearestEntity(EntityTypeTags.CANDIDATE_FOR_IRON_GOLEM_GIFT, OFFER_TARGET_CONTEXT, (LivingEntity)this.golem, this.golem.getX(), this.golem.getY(), this.golem.getZ(), getGolemBoundingBox());
/* 40 */     return (this.entity != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 45 */     return (this.tick > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 50 */     this.tick = adjustedTickDelay(400);
/* 51 */     this.golem.offerFlower(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 56 */     this.golem.offerFlower(false);
/* 57 */     if (this.tick == 0) { LivingEntity livingEntity = this.entity; if (livingEntity instanceof Mob) { Mob mob = (Mob)livingEntity; if (mob.getType().is(EntityTypeTags.ACCEPTS_IRON_GOLEM_GIFT) && mob.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA).isEmpty() && getGolemBoundingBox().intersects(mob.getBoundingBox()))
/* 58 */         { mob.setItemSlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA, OFFER_ITEM.getDefaultInstance());
/* 59 */           mob.setGuaranteedDrop(CopperGolem.EQUIPMENT_SLOT_ANTENNA); }  }
/*    */        }
/* 61 */      this.entity = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 66 */     if (this.entity != null) {
/* 67 */       this.golem.getLookControl().setLookAt((Entity)this.entity, 30.0F, 30.0F);
/*    */     }
/* 69 */     this.tick--;
/*    */   }
/*    */   
/*    */   private AABB getGolemBoundingBox() {
/* 73 */     return this.golem.getBoundingBox().inflate(6.0D, 2.0D, 6.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\OfferFlowerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */