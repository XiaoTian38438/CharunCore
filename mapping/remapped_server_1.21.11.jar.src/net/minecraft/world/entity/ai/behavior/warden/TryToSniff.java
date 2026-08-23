/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class TryToSniff {
/* 14 */   private static final IntProvider SNIFF_COOLDOWN = (IntProvider)UniformInt.of(100, 200);
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 17 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.IS_SNIFFING), (App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.absent(MemoryModuleType.SNIFF_COOLDOWN), (App)paramInstance.present(MemoryModuleType.NEAREST_ATTACKABLE), (App)paramInstance.absent(MemoryModuleType.DISTURBANCE_LOCATION)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\warden\TryToSniff.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */