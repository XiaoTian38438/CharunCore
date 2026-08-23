/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunOne<E extends LivingEntity>
/*    */   extends GateBehavior<E>
/*    */ {
/*    */   public RunOne(List<Pair<? extends BehaviorControl<? super E>, Integer>> paramList) {
/* 19 */     this(
/* 20 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), paramList);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RunOne(Map<MemoryModuleType<?>, MemoryStatus> paramMap, List<Pair<? extends BehaviorControl<? super E>, Integer>> paramList) {
/* 26 */     super(paramMap, 
/*    */         
/* 28 */         (Set<MemoryModuleType<?>>)ImmutableSet.of(), GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE, paramList);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\RunOne.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */