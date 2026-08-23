/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ 
/*    */ public class PoiCompetitorScan
/*    */ {
/*    */   public static BehaviorControl<Villager> create() {
/* 22 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.JOB_SITE), (App)paramInstance.present(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
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
/*    */   private static Villager selectWinner(Villager paramVillager1, Villager paramVillager2) {
/*    */     Villager villager1, villager2;
/* 42 */     if (paramVillager1.getVillagerXp() > paramVillager2.getVillagerXp()) {
/* 43 */       villager1 = paramVillager1;
/* 44 */       villager2 = paramVillager2;
/*    */     } else {
/* 46 */       villager1 = paramVillager2;
/* 47 */       villager2 = paramVillager1;
/*    */     } 
/*    */     
/* 50 */     villager2.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
/* 51 */     return villager1;
/*    */   }
/*    */   
/*    */   private static boolean competesForSameJobsite(GlobalPos paramGlobalPos, Holder<PoiType> paramHolder, Villager paramVillager) {
/* 55 */     Optional optional = paramVillager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 56 */     return (optional.isPresent() && paramGlobalPos
/* 57 */       .equals(optional.get()) && 
/* 58 */       hasMatchingProfession(paramHolder, paramVillager.getVillagerData().profession()));
/*    */   }
/*    */   
/*    */   private static boolean hasMatchingProfession(Holder<PoiType> paramHolder, Holder<VillagerProfession> paramHolder1) {
/* 62 */     return ((VillagerProfession)paramHolder1.value()).heldJobSite().test(paramHolder);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\PoiCompetitorScan.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */