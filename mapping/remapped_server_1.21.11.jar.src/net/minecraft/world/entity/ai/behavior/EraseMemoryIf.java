/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class EraseMemoryIf {
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> paramPredicate, MemoryModuleType<?> paramMemoryModuleType) {
/* 11 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\EraseMemoryIf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */