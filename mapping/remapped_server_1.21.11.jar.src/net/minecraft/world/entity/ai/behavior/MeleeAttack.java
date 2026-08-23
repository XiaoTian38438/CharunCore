/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class MeleeAttack {
/*    */   public static <T extends Mob> OneShot<T> create(int paramInt) {
/* 17 */     return create(paramMob -> true, paramInt);
/*    */   }
/*    */   
/*    */   public static <T extends Mob> OneShot<T> create(Predicate<T> paramPredicate, int paramInt) {
/* 21 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.present(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.absent(MemoryModuleType.ATTACK_COOLING_DOWN), (App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
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
/*    */   
/*    */   private static boolean isHoldingUsableNonMeleeWeapon(Mob paramMob) {
/* 42 */     Objects.requireNonNull(paramMob); return paramMob.isHolding(paramMob::canUseNonMeleeWeapon);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\MeleeAttack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */