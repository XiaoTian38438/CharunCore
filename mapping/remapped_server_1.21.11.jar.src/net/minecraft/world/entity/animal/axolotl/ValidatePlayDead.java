/*    */ package net.minecraft.world.entity.animal.axolotl;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class ValidatePlayDead {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 10 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.PLAY_DEAD_TICKS), (App)paramInstance.registered(MemoryModuleType.HURT_BY_ENTITY)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\axolotl\ValidatePlayDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */