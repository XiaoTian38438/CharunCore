/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public class YieldJobSite {
/*    */   public static BehaviorControl<Villager> create(float paramFloat) {
/* 23 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.POTENTIAL_JOB_SITE), (App)paramInstance.absent(MemoryModuleType.JOB_SITE), (App)paramInstance.present(MemoryModuleType.NEAREST_LIVING_ENTITIES), (App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET)).apply((Applicative)paramInstance, ()));
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
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean nearbyWantsJobsite(Holder<PoiType> paramHolder, Villager paramVillager, BlockPos paramBlockPos) {
/* 69 */     boolean bool = paramVillager.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
/* 70 */     if (bool) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     Optional<GlobalPos> optional = paramVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 75 */     Holder holder = paramVillager.getVillagerData().profession();
/*    */ 
/*    */     
/* 78 */     if (((VillagerProfession)holder.value()).heldJobSite().test(paramHolder)) {
/* 79 */       if (optional.isEmpty()) {
/* 80 */         return canReachPos((PathfinderMob)paramVillager, paramBlockPos, (PoiType)paramHolder.value());
/*    */       }
/* 82 */       return ((GlobalPos)optional.get()).pos().equals(paramBlockPos);
/*    */     } 
/* 84 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean canReachPos(PathfinderMob paramPathfinderMob, BlockPos paramBlockPos, PoiType paramPoiType) {
/* 88 */     Path path = paramPathfinderMob.getNavigation().createPath(paramBlockPos, paramPoiType.validRange());
/* 89 */     return (path != null && path.canReach());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\YieldJobSite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */