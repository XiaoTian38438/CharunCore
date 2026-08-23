/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffectUtil;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ElderGuardian extends Guardian {
/* 21 */   public static final float ELDER_SIZE_SCALE = EntityType.ELDER_GUARDIAN.getWidth() / EntityType.GUARDIAN.getWidth();
/*    */   
/*    */   private static final int EFFECT_INTERVAL = 1200;
/*    */   private static final int EFFECT_RADIUS = 50;
/*    */   private static final int EFFECT_DURATION = 6000;
/*    */   private static final int EFFECT_AMPLIFIER = 2;
/*    */   private static final int EFFECT_DISPLAY_LIMIT = 1200;
/*    */   
/*    */   public ElderGuardian(EntityType<? extends ElderGuardian> paramEntityType, Level paramLevel) {
/* 30 */     super((EntityType)paramEntityType, paramLevel);
/*    */     
/* 32 */     setPersistenceRequired();
/*    */ 
/*    */     
/* 35 */     if (this.randomStrollGoal != null) {
/* 36 */       this.randomStrollGoal.setInterval(400);
/*    */     }
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 41 */     return Guardian.createAttributes()
/* 42 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 43 */       .add(Attributes.ATTACK_DAMAGE, 8.0D)
/* 44 */       .add(Attributes.MAX_HEALTH, 80.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAttackDuration() {
/* 49 */     return 60;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 54 */     return isInWater() ? SoundEvents.ELDER_GUARDIAN_AMBIENT : SoundEvents.ELDER_GUARDIAN_AMBIENT_LAND;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 59 */     return isInWater() ? SoundEvents.ELDER_GUARDIAN_HURT : SoundEvents.ELDER_GUARDIAN_HURT_LAND;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 64 */     return isInWater() ? SoundEvents.ELDER_GUARDIAN_DEATH : SoundEvents.ELDER_GUARDIAN_DEATH_LAND;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getFlopSound() {
/* 69 */     return SoundEvents.ELDER_GUARDIAN_FLOP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 74 */     super.customServerAiStep(paramServerLevel);
/*    */ 
/*    */     
/* 77 */     if ((this.tickCount + getId()) % 1200 == 0) {
/* 78 */       MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.MINING_FATIGUE, 6000, 2);
/* 79 */       List list = MobEffectUtil.addEffectToPlayersAround(paramServerLevel, (Entity)this, position(), 50.0D, mobEffectInstance, 1200);
/* 80 */       list.forEach(paramServerPlayer -> paramServerPlayer.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, isSilent() ? 0.0F : 1.0F)));
/*    */     } 
/*    */ 
/*    */     
/* 84 */     if (!hasHome())
/* 85 */       setHomeTo(blockPosition(), 16); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\ElderGuardian.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */