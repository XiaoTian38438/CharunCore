/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StopAdmiringIfItemTooFarAway<E extends Piglin> {
/*    */   public static BehaviorControl<LivingEntity> create(int paramInt) {
/* 13 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.ADMIRING_ITEM), (App)paramInstance.registered(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\StopAdmiringIfItemTooFarAway.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */