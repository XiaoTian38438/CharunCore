/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import org.apache.commons.lang3.mutable.MutableLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AcquirePoi
/*     */ {
/*     */   public static final int SCAN_RANGE = 48;
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> paramPredicate, MemoryModuleType<GlobalPos> paramMemoryModuleType, boolean paramBoolean, Optional<Byte> paramOptional, BiPredicate<ServerLevel, BlockPos> paramBiPredicate) {
/*  39 */     return create(paramPredicate, paramMemoryModuleType, paramMemoryModuleType, paramBoolean, paramOptional, paramBiPredicate);
/*     */   }
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> paramPredicate, MemoryModuleType<GlobalPos> paramMemoryModuleType, boolean paramBoolean, Optional<Byte> paramOptional) {
/*  43 */     return create(paramPredicate, paramMemoryModuleType, paramMemoryModuleType, paramBoolean, paramOptional, (paramServerLevel, paramBlockPos) -> true);
/*     */   }
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> paramPredicate, MemoryModuleType<GlobalPos> paramMemoryModuleType1, MemoryModuleType<GlobalPos> paramMemoryModuleType2, boolean paramBoolean, Optional<Byte> paramOptional, BiPredicate<ServerLevel, BlockPos> paramBiPredicate) {
/*  47 */     byte b1 = 5;
/*  48 */     byte b2 = 20;
/*     */ 
/*     */     
/*  51 */     MutableLong mutableLong = new MutableLong(0L);
/*  52 */     Long2ObjectOpenHashMap long2ObjectOpenHashMap = new Long2ObjectOpenHashMap();
/*     */     
/*  54 */     OneShot<PathfinderMob> oneShot = BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
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
/* 117 */     if (paramMemoryModuleType2 == paramMemoryModuleType1) {
/* 118 */       return oneShot;
/*     */     }
/*     */     
/* 121 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Path findPathToPois(Mob paramMob, Set<Pair<Holder<PoiType>, BlockPos>> paramSet) {
/* 127 */     if (paramSet.isEmpty()) {
/* 128 */       return null;
/*     */     }
/* 130 */     HashSet<BlockPos> hashSet = new HashSet();
/* 131 */     int i = 1;
/* 132 */     for (Pair<Holder<PoiType>, BlockPos> pair : paramSet) {
/* 133 */       i = Math.max(i, ((PoiType)((Holder)pair.getFirst()).value()).validRange());
/* 134 */       hashSet.add((BlockPos)pair.getSecond());
/*     */     } 
/* 136 */     return paramMob.getNavigation().createPath(hashSet, i);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JitteredLinearRetry
/*     */   {
/*     */     private static final int MIN_INTERVAL_INCREASE = 40;
/*     */     private static final int MAX_INTERVAL_INCREASE = 80;
/*     */     private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
/*     */     private final RandomSource random;
/*     */     private long previousAttemptTimestamp;
/*     */     private long nextScheduledAttemptTimestamp;
/*     */     private int currentDelay;
/*     */     
/*     */     JitteredLinearRetry(RandomSource param1RandomSource, long param1Long) {
/* 151 */       this.random = param1RandomSource;
/* 152 */       markAttempt(param1Long);
/*     */     }
/*     */     
/*     */     public void markAttempt(long param1Long) {
/* 156 */       this.previousAttemptTimestamp = param1Long;
/* 157 */       int i = this.currentDelay + this.random.nextInt(40) + 40;
/* 158 */       this.currentDelay = Math.min(i, 400);
/* 159 */       this.nextScheduledAttemptTimestamp = param1Long + this.currentDelay;
/*     */     }
/*     */     
/*     */     public boolean isStillValid(long param1Long) {
/* 163 */       return (param1Long - this.previousAttemptTimestamp < 400L);
/*     */     }
/*     */     
/*     */     public boolean shouldRetry(long param1Long) {
/* 167 */       return (param1Long >= this.nextScheduledAttemptTimestamp);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 172 */       return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */