/*     */ package net.minecraft.world.entity.ai.sensing;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Sensor<E extends LivingEntity>
/*     */ {
/*  21 */   private static final RandomSource RANDOM = RandomSource.createThreadSafe();
/*     */   
/*     */   private static final int DEFAULT_SCAN_RATE = 20;
/*     */   
/*     */   private static final int DEFAULT_TARGETING_RANGE = 16;
/*  26 */   private static final TargetingConditions TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(16.0D);
/*  27 */   private static final TargetingConditions TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = TargetingConditions.forNonCombat().range(16.0D).ignoreInvisibilityTesting();
/*  28 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().range(16.0D);
/*  29 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = TargetingConditions.forCombat().range(16.0D).ignoreInvisibilityTesting();
/*  30 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT = TargetingConditions.forCombat().range(16.0D).ignoreLineOfSight();
/*  31 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT = TargetingConditions.forCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   
/*     */   private final int scanRate;
/*     */   private long timeToTick;
/*     */   
/*     */   public Sensor(int paramInt) {
/*  37 */     this.scanRate = paramInt;
/*  38 */     this.timeToTick = RANDOM.nextInt(paramInt);
/*     */   }
/*     */   
/*     */   public Sensor() {
/*  42 */     this(20);
/*     */   }
/*     */   
/*     */   public final void tick(ServerLevel paramServerLevel, E paramE) {
/*  46 */     if (--this.timeToTick <= 0L) {
/*  47 */       this.timeToTick = this.scanRate;
/*  48 */       updateTargetingConditionRanges(paramE);
/*  49 */       doTick(paramServerLevel, paramE);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateTargetingConditionRanges(E paramE) {
/*  54 */     double d = paramE.getAttributeValue(Attributes.FOLLOW_RANGE);
/*  55 */     TARGET_CONDITIONS.range(d);
/*  56 */     TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.range(d);
/*  57 */     ATTACK_TARGET_CONDITIONS.range(d);
/*  58 */     ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.range(d);
/*  59 */     ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.range(d);
/*  60 */     ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.range(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEntityTargetable(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  68 */     if (paramLivingEntity1.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, paramLivingEntity2))
/*     */     {
/*  70 */       return TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/*     */     }
/*  72 */     return TARGET_CONDITIONS.test(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEntityAttackable(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  77 */     if (paramLivingEntity1.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, paramLivingEntity2))
/*     */     {
/*  79 */       return ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/*     */     }
/*  81 */     return ATTACK_TARGET_CONDITIONS.test(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BiPredicate<ServerLevel, LivingEntity> wasEntityAttackableLastNTicks(LivingEntity paramLivingEntity, int paramInt) {
/*  86 */     return rememberPositives(paramInt, (paramServerLevel, paramLivingEntity2) -> isEntityAttackable(paramServerLevel, paramLivingEntity1, paramLivingEntity2));
/*     */   }
/*     */   
/*     */   public static boolean isEntityAttackableIgnoringLineOfSight(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  90 */     if (paramLivingEntity1.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, paramLivingEntity2))
/*     */     {
/*  92 */       return ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.test(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/*     */     }
/*  94 */     return ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.test(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, U> BiPredicate<T, U> rememberPositives(int paramInt, BiPredicate<T, U> paramBiPredicate) {
/* 103 */     AtomicInteger atomicInteger = new AtomicInteger(0);
/* 104 */     return (paramObject1, paramObject2) -> {
/*     */         if (paramBiPredicate.test(paramObject1, paramObject2)) {
/*     */           paramAtomicInteger.set(paramInt);
/*     */           return true;
/*     */         } 
/*     */         return (paramAtomicInteger.decrementAndGet() >= 0);
/*     */       };
/*     */   }
/*     */   
/*     */   protected abstract void doTick(ServerLevel paramServerLevel, E paramE);
/*     */   
/*     */   public abstract Set<MemoryModuleType<?>> requires();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\Sensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */