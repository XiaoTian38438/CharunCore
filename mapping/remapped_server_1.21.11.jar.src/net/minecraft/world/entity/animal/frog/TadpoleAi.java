/*    */ package net.minecraft.world.entity.animal.frog;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*    */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*    */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*    */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*    */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*    */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class TadpoleAi {
/*    */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*    */   
/*    */   protected static Brain<?> makeBrain(Brain<Tadpole> paramBrain) {
/* 31 */     initCoreActivity(paramBrain);
/* 32 */     initIdleActivity(paramBrain);
/*    */     
/* 34 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 35 */     paramBrain.setDefaultActivity(Activity.IDLE);
/* 36 */     paramBrain.useDefaultActivity();
/* 37 */     return paramBrain;
/*    */   }
/*    */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F; private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*    */   private static void initCoreActivity(Brain<Tadpole> paramBrain) {
/* 41 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void initIdleActivity(Brain<Tadpole> paramBrain) {
/* 50 */     paramBrain.addActivity(Activity.IDLE, ImmutableList.of(
/* 51 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 52 */           Pair.of(Integer.valueOf(1), new FollowTemptation(paramLivingEntity -> Float.valueOf(1.25F))), 
/* 53 */           Pair.of(Integer.valueOf(2), new GateBehavior(
/* 54 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*    */ 
/*    */               
/* 57 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*    */ 
/*    */               
/* 60 */               (List)ImmutableList.of(
/* 61 */                 Pair.of(RandomStroll.swim(0.5F), Integer.valueOf(2)), 
/* 62 */                 Pair.of(SetWalkTargetFromLookTarget.create(0.5F, 3), Integer.valueOf(3)), 
/* 63 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void updateActivity(Tadpole paramTadpole) {
/* 70 */     paramTadpole.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.IDLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\frog\TadpoleAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */