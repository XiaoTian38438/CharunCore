/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class StartHuntingHoglin {
/*    */   public static OneShot<Piglin> create() {
/* 10 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN), (App)paramInstance.absent(MemoryModuleType.ANGRY_AT), (App)paramInstance.absent(MemoryModuleType.HUNTED_RECENTLY), (App)paramInstance.registered(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS)).apply((Applicative)paramInstance, ()));
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
/*    */ 
/*    */   
/*    */   private static boolean hasHuntedRecently(AbstractPiglin paramAbstractPiglin) {
/* 33 */     return paramAbstractPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\StartHuntingHoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */