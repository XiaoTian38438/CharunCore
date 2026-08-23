/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SetEntityLookTarget {
/*    */   public static BehaviorControl<LivingEntity> create(MobCategory paramMobCategory, float paramFloat) {
/* 18 */     return create(paramLivingEntity -> paramMobCategory.equals(paramLivingEntity.getType().getCategory()), paramFloat);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(EntityType<?> paramEntityType, float paramFloat) {
/* 22 */     return create(paramLivingEntity -> paramEntityType.equals(paramLivingEntity.getType()), paramFloat);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(float paramFloat) {
/* 26 */     return create(paramLivingEntity -> true, paramFloat);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(Predicate<LivingEntity> paramPredicate, float paramFloat) {
/* 30 */     float f = paramFloat * paramFloat;
/*    */     
/* 32 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.LOOK_TARGET), (App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */