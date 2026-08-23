/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ 
/*    */ public class ForceUnmount extends Behavior<LivingEntity> {
/*    */   public ForceUnmount() {
/* 10 */     super((Map)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 15 */     return paramLivingEntity.isPassenger();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 20 */     paramLivingEntity.unRide();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\warden\ForceUnmount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */