/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class DismountOrSkipMounting {
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(int paramInt, BiPredicate<E, Entity> paramBiPredicate) {
/* 16 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.RIDE_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
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
/*    */   private static boolean isVehicleValid(LivingEntity paramLivingEntity, Entity paramEntity, int paramInt) {
/* 36 */     return (paramEntity.isAlive() && paramEntity
/* 37 */       .closerThan((Entity)paramLivingEntity, paramInt) && paramEntity
/* 38 */       .level() == paramLivingEntity.level());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\DismountOrSkipMounting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */