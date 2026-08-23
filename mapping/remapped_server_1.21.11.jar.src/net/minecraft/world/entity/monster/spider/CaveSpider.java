/*    */ package net.minecraft.world.entity.monster.spider;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class CaveSpider
/*    */   extends Spider
/*    */ {
/*    */   public CaveSpider(EntityType<? extends CaveSpider> paramEntityType, Level paramLevel) {
/* 23 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createCaveSpider() {
/* 27 */     return Spider.createAttributes()
/* 28 */       .add(Attributes.MAX_HEALTH, 12.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/* 33 */     if (super.doHurtTarget(paramServerLevel, paramEntity)) {
/* 34 */       if (paramEntity instanceof LivingEntity) {
/* 35 */         byte b = 0;
/* 36 */         if (level().getDifficulty() == Difficulty.NORMAL) {
/* 37 */           b = 7;
/* 38 */         } else if (level().getDifficulty() == Difficulty.HARD) {
/* 39 */           b = 15;
/*    */         } 
/*    */         
/* 42 */         if (b > 0) {
/* 43 */           ((LivingEntity)paramEntity).addEffect(new MobEffectInstance(MobEffects.POISON, b * 20, 0), (Entity)this);
/*    */         }
/*    */       } 
/*    */       
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 55 */     return paramSpawnGroupData;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getVehicleAttachmentPoint(Entity paramEntity) {
/* 60 */     if (paramEntity.getBbWidth() <= getBbWidth()) {
/* 61 */       return new Vec3(0.0D, 0.21875D * getScale(), 0.0D);
/*    */     }
/* 63 */     return super.getVehicleAttachmentPoint(paramEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\spider\CaveSpider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */