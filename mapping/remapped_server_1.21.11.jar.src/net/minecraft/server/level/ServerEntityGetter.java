/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.EntityGetter;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ServerEntityGetter
/*    */   extends EntityGetter
/*    */ {
/*    */   default Player getNearestPlayer(TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity) {
/* 19 */     return getNearestEntity(players(), paramTargetingConditions, paramLivingEntity, paramLivingEntity.getX(), paramLivingEntity.getY(), paramLivingEntity.getZ());
/*    */   }
/*    */   
/*    */   default Player getNearestPlayer(TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 23 */     return getNearestEntity(players(), paramTargetingConditions, paramLivingEntity, paramDouble1, paramDouble2, paramDouble3);
/*    */   }
/*    */   
/*    */   default Player getNearestPlayer(TargetingConditions paramTargetingConditions, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 27 */     return getNearestEntity(players(), paramTargetingConditions, (LivingEntity)null, paramDouble1, paramDouble2, paramDouble3);
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> T getNearestEntity(Class<? extends T> paramClass, TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity, double paramDouble1, double paramDouble2, double paramDouble3, AABB paramAABB) {
/* 31 */     return getNearestEntity(getEntitiesOfClass(paramClass, paramAABB, paramLivingEntity -> true), paramTargetingConditions, paramLivingEntity, paramDouble1, paramDouble2, paramDouble3);
/*    */   }
/*    */   
/*    */   default LivingEntity getNearestEntity(TagKey<EntityType<?>> paramTagKey, TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity, double paramDouble1, double paramDouble2, double paramDouble3, AABB paramAABB) {
/* 35 */     double d = Double.MAX_VALUE;
/* 36 */     LivingEntity livingEntity = null;
/* 37 */     for (LivingEntity livingEntity1 : getEntitiesOfClass(LivingEntity.class, paramAABB, paramLivingEntity -> paramLivingEntity.getType().is(paramTagKey))) {
/* 38 */       if (!paramTargetingConditions.test(getLevel(), paramLivingEntity, livingEntity1)) {
/*    */         continue;
/*    */       }
/*    */       
/* 42 */       double d1 = livingEntity1.distanceToSqr(paramDouble1, paramDouble2, paramDouble3);
/* 43 */       if (d1 < d) {
/* 44 */         d = d1;
/* 45 */         livingEntity = livingEntity1;
/*    */       } 
/*    */     } 
/* 48 */     return livingEntity;
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> T getNearestEntity(List<? extends T> paramList, TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 52 */     double d = -1.0D;
/* 53 */     LivingEntity livingEntity = null;
/* 54 */     for (LivingEntity livingEntity1 : paramList) {
/* 55 */       if (!paramTargetingConditions.test(getLevel(), paramLivingEntity, livingEntity1)) {
/*    */         continue;
/*    */       }
/*    */       
/* 59 */       double d1 = livingEntity1.distanceToSqr(paramDouble1, paramDouble2, paramDouble3);
/* 60 */       if (d == -1.0D || d1 < d) {
/* 61 */         d = d1;
/* 62 */         livingEntity = livingEntity1;
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     return (T)livingEntity;
/*    */   }
/*    */   
/*    */   default List<Player> getNearbyPlayers(TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity, AABB paramAABB) {
/* 70 */     ArrayList<Player> arrayList = new ArrayList();
/* 71 */     for (Player player : players()) {
/* 72 */       if (paramAABB.contains(player.getX(), player.getY(), player.getZ()) && paramTargetingConditions.test(getLevel(), paramLivingEntity, (LivingEntity)player)) {
/* 73 */         arrayList.add(player);
/*    */       }
/*    */     } 
/*    */     
/* 77 */     return arrayList;
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> List<T> getNearbyEntities(Class<T> paramClass, TargetingConditions paramTargetingConditions, LivingEntity paramLivingEntity, AABB paramAABB) {
/* 81 */     List list = getEntitiesOfClass(paramClass, paramAABB, paramLivingEntity -> true);
/* 82 */     ArrayList<LivingEntity> arrayList = new ArrayList();
/*    */     
/* 84 */     for (LivingEntity livingEntity : list) {
/* 85 */       if (paramTargetingConditions.test(getLevel(), paramLivingEntity, livingEntity)) {
/* 86 */         arrayList.add(livingEntity);
/*    */       }
/*    */     } 
/*    */     
/* 90 */     return (List)arrayList;
/*    */   }
/*    */   
/*    */   ServerLevel getLevel();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ServerEntityGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */