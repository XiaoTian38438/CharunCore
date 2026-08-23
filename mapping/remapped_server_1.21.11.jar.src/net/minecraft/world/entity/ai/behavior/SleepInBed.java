/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SleepInBed extends Behavior<LivingEntity> {
/*    */   public static final int COOLDOWN_AFTER_BEING_WOKEN = 100;
/*    */   private long nextOkStartTime;
/*    */   
/*    */   public SleepInBed() {
/* 26 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 34 */     if (paramLivingEntity.isPassenger()) {
/* 35 */       return false;
/*    */     }
/* 37 */     Brain brain = paramLivingEntity.getBrain();
/*    */     
/* 39 */     GlobalPos globalPos = brain.getMemory(MemoryModuleType.HOME).get();
/* 40 */     if (paramServerLevel.dimension() != globalPos.dimension()) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     Optional<Long> optional = brain.getMemory(MemoryModuleType.LAST_WOKEN);
/* 45 */     if (optional.isPresent()) {
/* 46 */       long l = paramServerLevel.getGameTime() - ((Long)optional.get()).longValue();
/* 47 */       if (l > 0L && l < 100L)
/*    */       {
/* 49 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 53 */     BlockState blockState = paramServerLevel.getBlockState(globalPos.pos());
/* 54 */     return (globalPos.pos().closerToCenterThan((Position)paramLivingEntity.position(), 2.0D) && blockState.is(BlockTags.BEDS) && !((Boolean)blockState.getValue((Property)BedBlock.OCCUPIED)).booleanValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 59 */     Optional<GlobalPos> optional = paramLivingEntity.getBrain().getMemory(MemoryModuleType.HOME);
/*    */     
/* 61 */     if (optional.isEmpty()) {
/* 62 */       return false;
/*    */     }
/*    */     
/* 65 */     BlockPos blockPos = ((GlobalPos)optional.get()).pos();
/* 66 */     return (paramLivingEntity.getBrain().isActive(Activity.REST) && paramLivingEntity.getY() > blockPos.getY() + 0.4D && blockPos.closerToCenterThan((Position)paramLivingEntity.position(), 1.14D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 71 */     if (paramLong > this.nextOkStartTime) {
/* 72 */       Brain brain = paramLivingEntity.getBrain();
/*    */       
/* 74 */       if (brain.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
/* 75 */         Optional<?> optional; Set<GlobalPos> set = brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get();
/*    */         
/* 77 */         if (brain.hasMemoryValue(MemoryModuleType.NEAREST_LIVING_ENTITIES)) {
/* 78 */           optional = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*    */         } else {
/* 80 */           optional = Optional.empty();
/*    */         } 
/*    */         
/* 83 */         InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(paramServerLevel, paramLivingEntity, null, null, set, (Optional)optional);
/*    */       } 
/* 85 */       paramLivingEntity.startSleeping(((GlobalPos)paramLivingEntity.getBrain().getMemory(MemoryModuleType.HOME).get()).pos());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long paramLong) {
/* 91 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 96 */     if (paramLivingEntity.isSleeping()) {
/* 97 */       paramLivingEntity.stopSleeping();
/* 98 */       this.nextOkStartTime = paramLong + 40L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SleepInBed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */