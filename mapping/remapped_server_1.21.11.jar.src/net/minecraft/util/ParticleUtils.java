/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ParticleUtils {
/*     */   public static void spawnParticlesOnBlockFaces(Level paramLevel, BlockPos paramBlockPos, ParticleOptions paramParticleOptions, IntProvider paramIntProvider) {
/*  20 */     for (Direction direction : Direction.values()) {
/*  21 */       spawnParticlesOnBlockFace(paramLevel, paramBlockPos, paramParticleOptions, paramIntProvider, direction, () -> getRandomSpeedRanges(paramLevel.random), 0.55D);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void spawnParticlesOnBlockFace(Level paramLevel, BlockPos paramBlockPos, ParticleOptions paramParticleOptions, IntProvider paramIntProvider, Direction paramDirection, Supplier<Vec3> paramSupplier, double paramDouble) {
/*  26 */     int i = paramIntProvider.sample(paramLevel.random);
/*  27 */     for (byte b = 0; b < i; b++) {
/*  28 */       spawnParticleOnFace(paramLevel, paramBlockPos, paramDirection, paramParticleOptions, paramSupplier.get(), paramDouble);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Vec3 getRandomSpeedRanges(RandomSource paramRandomSource) {
/*  33 */     return new Vec3(Mth.nextDouble(paramRandomSource, -0.5D, 0.5D), Mth.nextDouble(paramRandomSource, -0.5D, 0.5D), Mth.nextDouble(paramRandomSource, -0.5D, 0.5D));
/*     */   }
/*     */   
/*     */   public static void spawnParticlesAlongAxis(Direction.Axis paramAxis, Level paramLevel, BlockPos paramBlockPos, double paramDouble, ParticleOptions paramParticleOptions, UniformInt paramUniformInt) {
/*  37 */     Vec3 vec3 = Vec3.atCenterOf((Vec3i)paramBlockPos);
/*     */     
/*  39 */     boolean bool1 = (paramAxis == Direction.Axis.X) ? true : false;
/*  40 */     boolean bool2 = (paramAxis == Direction.Axis.Y) ? true : false;
/*  41 */     boolean bool3 = (paramAxis == Direction.Axis.Z) ? true : false;
/*     */     
/*  43 */     int i = paramUniformInt.sample(paramLevel.random);
/*  44 */     for (byte b = 0; b < i; b++) {
/*  45 */       double d1 = vec3.x + Mth.nextDouble(paramLevel.random, -1.0D, 1.0D) * (bool1 ? 0.5D : paramDouble);
/*  46 */       double d2 = vec3.y + Mth.nextDouble(paramLevel.random, -1.0D, 1.0D) * (bool2 ? 0.5D : paramDouble);
/*  47 */       double d3 = vec3.z + Mth.nextDouble(paramLevel.random, -1.0D, 1.0D) * (bool3 ? 0.5D : paramDouble);
/*  48 */       double d4 = bool1 ? Mth.nextDouble(paramLevel.random, -1.0D, 1.0D) : 0.0D;
/*  49 */       double d5 = bool2 ? Mth.nextDouble(paramLevel.random, -1.0D, 1.0D) : 0.0D;
/*  50 */       double d6 = bool3 ? Mth.nextDouble(paramLevel.random, -1.0D, 1.0D) : 0.0D;
/*     */       
/*  52 */       paramLevel.addParticle(paramParticleOptions, d1, d2, d3, d4, d5, d6);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void spawnParticleOnFace(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection, ParticleOptions paramParticleOptions, Vec3 paramVec3, double paramDouble) {
/*  57 */     Vec3 vec3 = Vec3.atCenterOf((Vec3i)paramBlockPos);
/*  58 */     int i = paramDirection.getStepX();
/*  59 */     int j = paramDirection.getStepY();
/*  60 */     int k = paramDirection.getStepZ();
/*  61 */     double d1 = vec3.x + ((i == 0) ? Mth.nextDouble(paramLevel.random, -0.5D, 0.5D) : (i * paramDouble));
/*  62 */     double d2 = vec3.y + ((j == 0) ? Mth.nextDouble(paramLevel.random, -0.5D, 0.5D) : (j * paramDouble));
/*  63 */     double d3 = vec3.z + ((k == 0) ? Mth.nextDouble(paramLevel.random, -0.5D, 0.5D) : (k * paramDouble));
/*  64 */     double d4 = (i == 0) ? paramVec3.x() : 0.0D;
/*  65 */     double d5 = (j == 0) ? paramVec3.y() : 0.0D;
/*  66 */     double d6 = (k == 0) ? paramVec3.z() : 0.0D;
/*     */     
/*  68 */     paramLevel.addParticle(paramParticleOptions, d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */   
/*     */   public static void spawnParticleBelow(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource, ParticleOptions paramParticleOptions) {
/*  72 */     double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/*  73 */     double d2 = paramBlockPos.getY() - 0.05D;
/*  74 */     double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*     */     
/*  76 */     paramLevel.addParticle(paramParticleOptions, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public static void spawnParticleInBlock(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, int paramInt, ParticleOptions paramParticleOptions) {
/*  80 */     double d1 = 0.5D;
/*  81 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/*  82 */     double d2 = blockState.isAir() ? 1.0D : blockState.getShape((BlockGetter)paramLevelAccessor, paramBlockPos).max(Direction.Axis.Y);
/*  83 */     spawnParticles(paramLevelAccessor, paramBlockPos, paramInt, 0.5D, d2, true, paramParticleOptions);
/*     */   }
/*     */   
/*     */   public static void spawnParticles(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean, ParticleOptions paramParticleOptions) {
/*  87 */     RandomSource randomSource = paramLevelAccessor.getRandom();
/*  88 */     for (byte b = 0; b < paramInt; b++) {
/*  89 */       double d1 = randomSource.nextGaussian() * 0.02D;
/*  90 */       double d2 = randomSource.nextGaussian() * 0.02D;
/*  91 */       double d3 = randomSource.nextGaussian() * 0.02D;
/*     */       
/*  93 */       double d4 = 0.5D - paramDouble1;
/*  94 */       double d5 = paramBlockPos.getX() + d4 + randomSource.nextDouble() * paramDouble1 * 2.0D;
/*  95 */       double d6 = paramBlockPos.getY() + randomSource.nextDouble() * paramDouble2;
/*  96 */       double d7 = paramBlockPos.getZ() + d4 + randomSource.nextDouble() * paramDouble1 * 2.0D;
/*     */       
/*  98 */       if (paramBoolean || !paramLevelAccessor.getBlockState(BlockPos.containing(d5, d6, d7).below()).isAir()) {
/*  99 */         paramLevelAccessor.addParticle(paramParticleOptions, d5, d6, d7, d1, d2, d3);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void spawnSmashAttackParticles(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, int paramInt) {
/* 105 */     Vec3 vec3 = paramBlockPos.getCenter().add(0.0D, 0.5D, 0.0D);
/* 106 */     BlockParticleOption blockParticleOption = new BlockParticleOption(ParticleTypes.DUST_PILLAR, paramLevelAccessor.getBlockState(paramBlockPos));
/*     */     byte b;
/* 108 */     for (b = 0; b < paramInt / 3.0F; b++) {
/* 109 */       double d1 = vec3.x + paramLevelAccessor.getRandom().nextGaussian() / 2.0D;
/* 110 */       double d2 = vec3.y;
/* 111 */       double d3 = vec3.z + paramLevelAccessor.getRandom().nextGaussian() / 2.0D;
/*     */       
/* 113 */       double d4 = paramLevelAccessor.getRandom().nextGaussian() * 0.20000000298023224D;
/* 114 */       double d5 = paramLevelAccessor.getRandom().nextGaussian() * 0.20000000298023224D;
/* 115 */       double d6 = paramLevelAccessor.getRandom().nextGaussian() * 0.20000000298023224D;
/*     */       
/* 117 */       paramLevelAccessor.addParticle((ParticleOptions)blockParticleOption, d1, d2, d3, d4, d5, d6);
/*     */     } 
/*     */     
/* 120 */     for (b = 0; b < paramInt / 1.5F; b++) {
/* 121 */       double d1 = vec3.x + 3.5D * Math.cos(b) + paramLevelAccessor.getRandom().nextGaussian() / 2.0D;
/* 122 */       double d2 = vec3.y;
/* 123 */       double d3 = vec3.z + 3.5D * Math.sin(b) + paramLevelAccessor.getRandom().nextGaussian() / 2.0D;
/*     */       
/* 125 */       double d4 = paramLevelAccessor.getRandom().nextGaussian() * 0.05000000074505806D;
/* 126 */       double d5 = paramLevelAccessor.getRandom().nextGaussian() * 0.05000000074505806D;
/* 127 */       double d6 = paramLevelAccessor.getRandom().nextGaussian() * 0.05000000074505806D;
/*     */       
/* 129 */       paramLevelAccessor.addParticle((ParticleOptions)blockParticleOption, d1, d2, d3, d4, d5, d6);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ParticleUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */