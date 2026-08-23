/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ @Deprecated
/*    */ public class SetEntityLookTargetSometimes {
/*    */   public static BehaviorControl<LivingEntity> create(float paramFloat, UniformInt paramUniformInt) {
/* 20 */     return create(paramFloat, paramUniformInt, paramLivingEntity -> true);
/*    */   }
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(EntityType<?> paramEntityType, float paramFloat, UniformInt paramUniformInt) {
/* 24 */     return create(paramFloat, paramUniformInt, paramLivingEntity -> paramEntityType.equals(paramLivingEntity.getType()));
/*    */   }
/*    */   
/*    */   private static BehaviorControl<LivingEntity> create(float paramFloat, UniformInt paramUniformInt, Predicate<LivingEntity> paramPredicate) {
/* 28 */     float f = paramFloat * paramFloat;
/*    */     
/* 30 */     Ticker ticker = new Ticker(paramUniformInt);
/*    */     
/* 32 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.LOOK_TARGET), (App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Ticker
/*    */   {
/*    */     private final UniformInt interval;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private int ticksUntilNextStart;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Ticker(UniformInt param1UniformInt) {
/* 56 */       if (param1UniformInt.getMinValue() <= 1) {
/* 57 */         throw new IllegalArgumentException();
/*    */       }
/* 59 */       this.interval = param1UniformInt;
/*    */     }
/*    */     
/*    */     public boolean tickDownAndCheck(RandomSource param1RandomSource) {
/* 63 */       if (this.ticksUntilNextStart == 0) {
/* 64 */         this.ticksUntilNextStart = this.interval.sample(param1RandomSource) - 1;
/* 65 */         return false;
/*    */       } 
/*    */       
/* 68 */       return (--this.ticksUntilNextStart == 0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTargetSometimes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */