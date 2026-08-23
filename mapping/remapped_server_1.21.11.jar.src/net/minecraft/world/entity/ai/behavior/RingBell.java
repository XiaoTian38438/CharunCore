/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.block.BellBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RingBell {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 16 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.MEETING_POINT)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */   
/*    */   private static final float BELL_RING_CHANCE = 0.95F;
/*    */   public static final int RING_BELL_FROM_DISTANCE = 3;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\RingBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */