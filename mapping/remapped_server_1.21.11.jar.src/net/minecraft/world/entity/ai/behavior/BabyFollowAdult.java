/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class BabyFollowAdult {
/*    */   public static OneShot<LivingEntity> create(UniformInt paramUniformInt, float paramFloat) {
/* 13 */     return create(paramUniformInt, paramLivingEntity -> Float.valueOf(paramFloat), MemoryModuleType.NEAREST_VISIBLE_ADULT, false);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(UniformInt paramUniformInt, Function<LivingEntity, Float> paramFunction, MemoryModuleType<? extends LivingEntity> paramMemoryModuleType, boolean paramBoolean) {
/* 17 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(paramMemoryModuleType), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\BabyFollowAdult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */