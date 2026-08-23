/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonSittingScanningPhase extends AbstractDragonSittingPhase {
/*    */   private static final int SITTING_SCANNING_IDLE_TICKS = 100;
/*    */   private static final int SITTING_ATTACK_Y_VIEW_RANGE = 10;
/*    */   private static final int SITTING_ATTACK_VIEW_RANGE = 20;
/*    */   private static final int SITTING_CHARGE_VIEW_RANGE = 150;
/* 16 */   private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0D);
/*    */   
/*    */   private final TargetingConditions scanTargeting;
/*    */   private int scanningTime;
/*    */   
/*    */   public DragonSittingScanningPhase(EnderDragon paramEnderDragon) {
/* 22 */     super(paramEnderDragon);
/*    */     
/* 24 */     this.scanTargeting = TargetingConditions.forCombat().range(20.0D).selector((paramLivingEntity, paramServerLevel) -> (Math.abs(paramLivingEntity.getY() - paramEnderDragon.getY()) <= 10.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {
/* 29 */     this.scanningTime++;
/* 30 */     Player player = paramServerLevel.getNearestPlayer(this.scanTargeting, (LivingEntity)this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*    */     
/* 32 */     if (player != null) {
/* 33 */       if (this.scanningTime > 25) {
/* 34 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_ATTACKING);
/*    */       } else {
/* 36 */         Vec3 vec31 = (new Vec3(player.getX() - this.dragon.getX(), 0.0D, player.getZ() - this.dragon.getZ())).normalize();
/* 37 */         Vec3 vec32 = (new Vec3(Mth.sin((this.dragon.getYRot() * 0.017453292F)), 0.0D, -Mth.cos((this.dragon.getYRot() * 0.017453292F)))).normalize();
/* 38 */         float f1 = (float)vec32.dot(vec31);
/* 39 */         float f2 = (float)(Math.acos(f1) * 57.2957763671875D) + 0.5F;
/*    */         
/* 41 */         if (f2 < 0.0F || f2 > 10.0F) {
/* 42 */           double d1 = player.getX() - this.dragon.head.getX();
/* 43 */           double d2 = player.getZ() - this.dragon.head.getZ();
/* 44 */           double d3 = Mth.clamp(Mth.wrapDegrees(180.0D - Mth.atan2(d1, d2) * 57.2957763671875D - this.dragon.getYRot()), -100.0D, 100.0D);
/*    */           
/* 46 */           this.dragon.yRotA *= 0.8F;
/*    */           
/* 48 */           float f3 = (float)Math.sqrt(d1 * d1 + d2 * d2) + 1.0F;
/* 49 */           float f4 = f3;
/* 50 */           if (f3 > 40.0F) {
/* 51 */             f3 = 40.0F;
/*    */           }
/* 53 */           this.dragon.yRotA += (float)d3 * 0.7F / f3 / f4;
/* 54 */           this.dragon.setYRot(this.dragon.getYRot() + this.dragon.yRotA);
/*    */         } 
/*    */       } 
/* 57 */     } else if (this.scanningTime >= 100) {
/* 58 */       player = paramServerLevel.getNearestPlayer(CHARGE_TARGETING, (LivingEntity)this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 59 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
/* 60 */       if (player != null) {
/* 61 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.CHARGING_PLAYER);
/* 62 */         ((DragonChargePlayerPhase)this.dragon.getPhaseManager().<DragonChargePlayerPhase>getPhase(EnderDragonPhase.CHARGING_PLAYER)).setTarget(new Vec3(player.getX(), player.getY(), player.getZ()));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 69 */     this.scanningTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonSittingScanningPhase> getPhase() {
/* 74 */     return EnderDragonPhase.SITTING_SCANNING;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingScanningPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */