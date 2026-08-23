/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Slime;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface NearbySlimes
/*    */ {
/*    */   int count(int paramInt);
/*    */   
/*    */   static NearbySlimes closeTo(LivingEntity paramLivingEntity) {
/* 60 */     return paramInt -> {
/*    */         ArrayList arrayList = new ArrayList();
/*    */         paramLivingEntity.level().getEntities((EntityTypeTest)EntityType.SLIME, paramLivingEntity.getBoundingBox().inflate(2.0D), (), arrayList, paramInt);
/*    */         return arrayList.size();
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\OozingMobEffect$NearbySlimes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */