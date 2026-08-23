/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ 
/*    */ public class StrollAroundPoi
/*    */ {
/*    */   private static final int MIN_TIME_BETWEEN_STROLLS = 180;
/*    */   private static final int STROLL_MAX_XZ_DIST = 8;
/*    */   private static final int STROLL_MAX_Y_DIST = 6;
/*    */   
/*    */   public static OneShot<PathfinderMob> create(MemoryModuleType<GlobalPos> paramMemoryModuleType, float paramFloat, int paramInt) {
/* 27 */     MutableLong mutableLong = new MutableLong(0L);
/*    */     
/* 29 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StrollAroundPoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */