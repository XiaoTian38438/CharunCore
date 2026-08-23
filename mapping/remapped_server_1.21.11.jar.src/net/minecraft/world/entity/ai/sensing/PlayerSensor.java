/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class PlayerSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 20 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
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
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 33 */     Objects.requireNonNull(paramLivingEntity);
/* 34 */     List list = (List)paramServerLevel.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(paramServerPlayer -> paramLivingEntity.closerThan((Entity)paramServerPlayer, getFollowDistance(paramLivingEntity))).sorted(Comparator.comparingDouble(paramLivingEntity::distanceToSqr)).collect(Collectors.toList());
/*    */     
/* 36 */     Brain brain = paramLivingEntity.getBrain();
/* 37 */     brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, list);
/*    */ 
/*    */     
/* 40 */     List<Player> list1 = (List)list.stream().filter(paramPlayer -> isEntityTargetable(paramServerLevel, paramLivingEntity, (LivingEntity)paramPlayer)).collect(Collectors.toList());
/* 41 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, list1.isEmpty() ? null : list1.get(0));
/*    */ 
/*    */     
/* 44 */     List<Player> list2 = list1.stream().filter(paramPlayer -> isEntityAttackable(paramServerLevel, paramLivingEntity, (LivingEntity)paramPlayer)).toList();
/* 45 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS, list2);
/*    */     
/* 47 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, list2.isEmpty() ? null : list2.get(0));
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getFollowDistance(LivingEntity paramLivingEntity) {
/* 52 */     return paramLivingEntity.getAttributeValue(Attributes.FOLLOW_RANGE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\PlayerSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */