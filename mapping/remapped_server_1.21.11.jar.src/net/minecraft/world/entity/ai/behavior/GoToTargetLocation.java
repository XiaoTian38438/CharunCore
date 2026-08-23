/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class GoToTargetLocation {
/*    */   private static BlockPos getNearbyPos(Mob paramMob, BlockPos paramBlockPos) {
/* 11 */     RandomSource randomSource = (paramMob.level()).random;
/* 12 */     return paramBlockPos.offset(getRandomOffset(randomSource), 0, getRandomOffset(randomSource));
/*    */   }
/*    */   
/*    */   private static int getRandomOffset(RandomSource paramRandomSource) {
/* 16 */     return paramRandomSource.nextInt(3) - 1;
/*    */   }
/*    */   
/*    */   public static <E extends Mob> OneShot<E> create(MemoryModuleType<BlockPos> paramMemoryModuleType, int paramInt, float paramFloat) {
/* 20 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(paramMemoryModuleType), (App)paramInstance.absent(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\GoToTargetLocation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */