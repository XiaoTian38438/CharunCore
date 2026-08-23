/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ public class StartCelebratingIfTargetDead
/*    */ {
/*    */   public static BehaviorControl<LivingEntity> create(int paramInt, BiPredicate<LivingEntity, LivingEntity> paramBiPredicate) {
/* 19 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.registered(MemoryModuleType.ANGRY_AT), (App)paramInstance.absent(MemoryModuleType.CELEBRATE_LOCATION), (App)paramInstance.registered(MemoryModuleType.DANCING)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StartCelebratingIfTargetDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */