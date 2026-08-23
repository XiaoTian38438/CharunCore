/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SetWalkTargetFromAttackTargetIfTargetOutOfReach {
/*    */   private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;
/*    */   
/*    */   public static BehaviorControl<Mob> create(float paramFloat) {
/* 22 */     return create(paramLivingEntity -> Float.valueOf(paramFloat));
/*    */   }
/*    */   
/*    */   public static BehaviorControl<Mob> create(Function<LivingEntity, Float> paramFunction) {
/* 26 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.present(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromAttackTargetIfTargetOutOfReach.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */