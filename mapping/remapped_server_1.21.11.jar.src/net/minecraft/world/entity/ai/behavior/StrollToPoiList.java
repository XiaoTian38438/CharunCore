/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class StrollToPoiList {
/*    */   public static BehaviorControl<Villager> create(MemoryModuleType<List<GlobalPos>> paramMemoryModuleType, float paramFloat, int paramInt1, int paramInt2, MemoryModuleType<GlobalPos> paramMemoryModuleType1) {
/* 17 */     MutableLong mutableLong = new MutableLong(0L);
/*    */     
/* 19 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(paramMemoryModuleType1), (App)paramInstance.present(paramMemoryModuleType2)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StrollToPoiList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */