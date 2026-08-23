/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ 
/*    */ public class TriggerGate
/*    */ {
/*    */   public static <E extends LivingEntity> OneShot<E> triggerOneShuffled(List<Pair<? extends Trigger<? super E>, Integer>> paramList) {
/* 15 */     return triggerGate(paramList, GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends LivingEntity> OneShot<E> triggerGate(List<Pair<? extends Trigger<? super E>, Integer>> paramList, GateBehavior.OrderPolicy paramOrderPolicy, GateBehavior.RunningPolicy paramRunningPolicy) {
/* 21 */     ShufflingList shufflingList = new ShufflingList();
/* 22 */     paramList.forEach(paramPair -> paramShufflingList.add((Trigger)paramPair.getFirst(), ((Integer)paramPair.getSecond()).intValue()));
/*    */     
/* 24 */     return BehaviorBuilder.create(paramInstance -> paramInstance.point(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\TriggerGate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */