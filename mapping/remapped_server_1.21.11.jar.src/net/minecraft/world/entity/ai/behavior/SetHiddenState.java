/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ public class SetHiddenState
/*    */ {
/*    */   private static final int HIDE_TIMEOUT = 300;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(int paramInt1, int paramInt2) {
/* 21 */     int i = paramInt1 * 20;
/*    */ 
/*    */     
/* 24 */     MutableInt mutableInt = new MutableInt(0);
/*    */     
/* 26 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.HIDING_PLACE), (App)paramInstance.present(MemoryModuleType.HEARD_BELL_TIME)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetHiddenState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */