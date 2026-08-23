/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class BellBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final int DURATION = 50;
/*     */   private static final int GLOW_DURATION = 60;
/*     */   private static final int MIN_TICKS_BETWEEN_SEARCHES = 60;
/*     */   private static final int MAX_RESONATION_TICKS = 40;
/*     */   private static final int TICKS_BEFORE_RESONATION = 5;
/*     */   private static final int SEARCH_RADIUS = 48;
/*     */   private static final int HEAR_BELL_RADIUS = 32;
/*     */   private static final int HIGHLIGHT_RAIDERS_RADIUS = 48;
/*     */   private long lastRingTimestamp;
/*     */   public int ticks;
/*     */   public boolean shaking;
/*     */   public Direction clickDirection;
/*     */   private List<LivingEntity> nearbyEntities;
/*     */   private boolean resonating;
/*     */   private int resonationTicks;
/*     */   
/*     */   public BellBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  43 */     super(BlockEntityType.BELL, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int paramInt1, int paramInt2) {
/*  48 */     if (paramInt1 == 1) {
/*  49 */       updateEntities();
/*  50 */       this.resonationTicks = 0;
/*  51 */       this.clickDirection = Direction.from3DDataValue(paramInt2);
/*  52 */       this.ticks = 0;
/*  53 */       this.shaking = true;
/*  54 */       return true;
/*     */     } 
/*  56 */     return super.triggerEvent(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, BellBlockEntity paramBellBlockEntity, ResonationEndAction paramResonationEndAction) {
/*  65 */     if (paramBellBlockEntity.shaking) {
/*  66 */       paramBellBlockEntity.ticks++;
/*     */     }
/*     */     
/*  69 */     if (paramBellBlockEntity.ticks >= 50) {
/*  70 */       paramBellBlockEntity.shaking = false;
/*  71 */       paramBellBlockEntity.ticks = 0;
/*     */     } 
/*     */     
/*  74 */     if (paramBellBlockEntity.ticks >= 5 && paramBellBlockEntity.resonationTicks == 0 && areRaidersNearby(paramBlockPos, paramBellBlockEntity.nearbyEntities)) {
/*  75 */       paramBellBlockEntity.resonating = true;
/*  76 */       paramLevel.playSound(null, paramBlockPos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */     
/*  79 */     if (paramBellBlockEntity.resonating) {
/*  80 */       if (paramBellBlockEntity.resonationTicks < 40) {
/*  81 */         paramBellBlockEntity.resonationTicks++;
/*     */       } else {
/*  83 */         paramResonationEndAction.run(paramLevel, paramBlockPos, paramBellBlockEntity.nearbyEntities);
/*  84 */         paramBellBlockEntity.resonating = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clientTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, BellBlockEntity paramBellBlockEntity) {
/*  90 */     tick(paramLevel, paramBlockPos, paramBlockState, paramBellBlockEntity, BellBlockEntity::showBellParticles);
/*     */   }
/*     */   
/*     */   public static void serverTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, BellBlockEntity paramBellBlockEntity) {
/*  94 */     tick(paramLevel, paramBlockPos, paramBlockState, paramBellBlockEntity, BellBlockEntity::makeRaidersGlow);
/*     */   }
/*     */   
/*     */   public void onHit(Direction paramDirection) {
/*  98 */     BlockPos blockPos = getBlockPos();
/*     */     
/* 100 */     this.clickDirection = paramDirection;
/* 101 */     if (this.shaking) {
/* 102 */       this.ticks = 0;
/*     */     } else {
/* 104 */       this.shaking = true;
/*     */     } 
/*     */     
/* 107 */     this.level.blockEvent(blockPos, getBlockState().getBlock(), 1, paramDirection.get3DDataValue());
/*     */   }
/*     */   
/*     */   private void updateEntities() {
/* 111 */     BlockPos blockPos = getBlockPos();
/*     */     
/* 113 */     if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
/* 114 */       this.lastRingTimestamp = this.level.getGameTime();
/* 115 */       AABB aABB = (new AABB(blockPos)).inflate(48.0D);
/* 116 */       this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, aABB);
/*     */     } 
/*     */     
/* 119 */     if (!this.level.isClientSide()) {
/* 120 */       for (LivingEntity livingEntity : this.nearbyEntities) {
/* 121 */         if (!livingEntity.isAlive() || livingEntity.isRemoved()) {
/*     */           continue;
/*     */         }
/* 124 */         if (blockPos.closerToCenterThan((Position)livingEntity.position(), 32.0D)) {
/* 125 */           livingEntity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, Long.valueOf(this.level.getGameTime()));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean areRaidersNearby(BlockPos paramBlockPos, List<LivingEntity> paramList) {
/* 132 */     for (LivingEntity livingEntity : paramList) {
/* 133 */       if (!livingEntity.isAlive() || livingEntity.isRemoved()) {
/*     */         continue;
/*     */       }
/* 136 */       if (paramBlockPos.closerToCenterThan((Position)livingEntity.position(), 32.0D) && 
/* 137 */         livingEntity.getType().is(EntityTypeTags.RAIDERS)) {
/* 138 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   private static void makeRaidersGlow(Level paramLevel, BlockPos paramBlockPos, List<LivingEntity> paramList) {
/* 146 */     paramList.stream()
/* 147 */       .filter(paramLivingEntity -> isRaiderWithinRange(paramBlockPos, paramLivingEntity))
/* 148 */       .forEach(BellBlockEntity::glow);
/*     */   }
/*     */   
/*     */   private static void showBellParticles(Level paramLevel, BlockPos paramBlockPos, List<LivingEntity> paramList) {
/* 152 */     MutableInt mutableInt = new MutableInt(16700985);
/*     */     
/* 154 */     int i = (int)paramList.stream().filter(paramLivingEntity -> paramBlockPos.closerToCenterThan((Position)paramLivingEntity.position(), 48.0D)).count();
/*     */     
/* 156 */     paramList.stream()
/* 157 */       .filter(paramLivingEntity -> isRaiderWithinRange(paramBlockPos, paramLivingEntity))
/* 158 */       .forEach(paramLivingEntity -> {
/*     */           float f = 1.0F;
/*     */           double d1 = Math.sqrt((paramLivingEntity.getX() - paramBlockPos.getX()) * (paramLivingEntity.getX() - paramBlockPos.getX()) + (paramLivingEntity.getZ() - paramBlockPos.getZ()) * (paramLivingEntity.getZ() - paramBlockPos.getZ()));
/*     */           double d2 = (paramBlockPos.getX() + 0.5F) + 1.0D / d1 * (paramLivingEntity.getX() - paramBlockPos.getX());
/*     */           double d3 = (paramBlockPos.getZ() + 0.5F) + 1.0D / d1 * (paramLivingEntity.getZ() - paramBlockPos.getZ());
/*     */           int i = Mth.clamp((paramInt - 21) / -2, 3, 15);
/*     */           for (byte b = 0; b < i; b++) {
/*     */             int j = paramMutableInt.addAndGet(5);
/*     */             paramLevel.addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, j), d2, (paramBlockPos.getY() + 0.5F), d3, 0.0D, 0.0D, 0.0D);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isRaiderWithinRange(BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/* 173 */     return (paramLivingEntity.isAlive() && 
/* 174 */       !paramLivingEntity.isRemoved() && paramBlockPos
/* 175 */       .closerToCenterThan((Position)paramLivingEntity.position(), 48.0D) && paramLivingEntity
/* 176 */       .getType().is(EntityTypeTags.RAIDERS));
/*     */   }
/*     */   
/*     */   private static void glow(LivingEntity paramLivingEntity) {
/* 180 */     paramLivingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface ResonationEndAction {
/*     */     void run(Level param1Level, BlockPos param1BlockPos, List<LivingEntity> param1List);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BellBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */