/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InteractWithDoor
/*     */ {
/*     */   private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 20;
/*     */   private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 3.0D;
/*     */   private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2.0D;
/*     */   
/*     */   public static BehaviorControl<LivingEntity> create() {
/*  45 */     MutableObject mutableObject = new MutableObject();
/*  46 */     MutableInt mutableInt = new MutableInt(0);
/*     */     
/*  48 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.PATH), (App)paramInstance.registered(MemoryModuleType.DOORS_TO_CLOSE), (App)paramInstance.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, Node paramNode1, Node paramNode2, Set<GlobalPos> paramSet, Optional<List<LivingEntity>> paramOptional) {
/*  98 */     Iterator<GlobalPos> iterator = paramSet.iterator();
/*  99 */     while (iterator.hasNext()) {
/* 100 */       GlobalPos globalPos = iterator.next();
/* 101 */       BlockPos blockPos = globalPos.pos();
/*     */ 
/*     */       
/* 104 */       if (paramNode1 != null && paramNode1.asBlockPos().equals(blockPos)) {
/*     */         continue;
/*     */       }
/* 107 */       if (paramNode2 != null && paramNode2.asBlockPos().equals(blockPos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 111 */       if (isDoorTooFarAway(paramServerLevel, paramLivingEntity, globalPos)) {
/* 112 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 115 */       BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 116 */       if (!blockState.is(BlockTags.MOB_INTERACTABLE_DOORS, paramBlockStateBase -> paramBlockStateBase.getBlock() instanceof DoorBlock)) {
/* 117 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 120 */       DoorBlock doorBlock = (DoorBlock)blockState.getBlock();
/* 121 */       if (!doorBlock.isOpen(blockState)) {
/* 122 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 125 */       if (areOtherMobsComingThroughDoor(paramLivingEntity, blockPos, paramOptional)) {
/* 126 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 129 */       doorBlock.setOpen((Entity)paramLivingEntity, (Level)paramServerLevel, blockState, blockPos, false);
/* 130 */       iterator.remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areOtherMobsComingThroughDoor(LivingEntity paramLivingEntity, BlockPos paramBlockPos, Optional<List<LivingEntity>> paramOptional) {
/* 135 */     if (paramOptional.isEmpty()) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     return ((List)paramOptional.get()).stream()
/* 140 */       .filter(paramLivingEntity2 -> (paramLivingEntity2.getType() == paramLivingEntity1.getType()))
/* 141 */       .filter(paramLivingEntity -> paramBlockPos.closerToCenterThan((Position)paramLivingEntity.position(), 2.0D))
/* 142 */       .anyMatch(paramLivingEntity -> isMobComingThroughDoor(paramLivingEntity.getBrain(), paramBlockPos));
/*     */   }
/*     */   
/*     */   private static boolean isMobComingThroughDoor(Brain<?> paramBrain, BlockPos paramBlockPos) {
/* 146 */     if (!paramBrain.hasMemoryValue(MemoryModuleType.PATH)) {
/* 147 */       return false;
/*     */     }
/* 149 */     Path path = paramBrain.getMemory(MemoryModuleType.PATH).get();
/* 150 */     if (path.isDone())
/*     */     {
/* 152 */       return false;
/*     */     }
/*     */     
/* 155 */     Node node1 = path.getPreviousNode();
/* 156 */     if (node1 == null) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     Node node2 = path.getNextNode();
/* 161 */     return (paramBlockPos.equals(node1.asBlockPos()) || paramBlockPos.equals(node2.asBlockPos()));
/*     */   }
/*     */   
/*     */   private static boolean isDoorTooFarAway(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, GlobalPos paramGlobalPos) {
/* 165 */     return (paramGlobalPos.dimension() != paramServerLevel.dimension() || 
/* 166 */       !paramGlobalPos.pos().closerToCenterThan((Position)paramLivingEntity.position(), 3.0D));
/*     */   }
/*     */   
/*     */   private static Optional<Set<GlobalPos>> rememberDoorToClose(MemoryAccessor<OptionalBox.Mu, Set<GlobalPos>> paramMemoryAccessor, Optional<Set<GlobalPos>> paramOptional, ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 170 */     GlobalPos globalPos = GlobalPos.of(paramServerLevel.dimension(), paramBlockPos);
/*     */     
/* 172 */     return Optional.of(paramOptional.<Set<GlobalPos>>map(paramSet -> {
/*     */             paramSet.add(paramGlobalPos);
/*     */             return paramSet;
/* 175 */           }).orElseGet(() -> {
/*     */             HashSet hashSet = Sets.newHashSet((Object[])new GlobalPos[] { paramGlobalPos });
/*     */             paramMemoryAccessor.set(hashSet);
/*     */             return hashSet;
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\InteractWithDoor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */