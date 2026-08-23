/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public final class Client
/*     */ {
/*     */   private static final int PARTICLE_TICK_RATE = 20;
/*     */   private static final float IDLE_PARTICLE_CHANCE = 0.5F;
/*     */   private static final float AMBIENT_SOUND_CHANCE = 0.02F;
/*     */   private static final int ACTIVATION_PARTICLE_COUNT = 20;
/*     */   private static final int DEACTIVATION_PARTICLE_COUNT = 20;
/*     */   
/*     */   public static void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, VaultClientData paramVaultClientData, VaultSharedData paramVaultSharedData) {
/* 250 */     paramVaultClientData.updateDisplayItemSpin();
/*     */     
/* 252 */     if (paramLevel.getGameTime() % 20L == 0L) {
/* 253 */       emitConnectionParticlesForNearbyPlayers(paramLevel, paramBlockPos, paramBlockState, paramVaultSharedData);
/*     */     }
/*     */     
/* 256 */     emitIdleParticles(paramLevel, paramBlockPos, paramVaultSharedData, ((Boolean)paramBlockState.getValue((Property)VaultBlock.OMINOUS)).booleanValue() ? (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME : (ParticleOptions)ParticleTypes.SMALL_FLAME);
/* 257 */     playIdleSounds(paramLevel, paramBlockPos, paramVaultSharedData);
/*     */   }
/*     */   
/*     */   public static void emitActivationParticles(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, VaultSharedData paramVaultSharedData, ParticleOptions paramParticleOptions) {
/* 261 */     emitConnectionParticlesForNearbyPlayers(paramLevel, paramBlockPos, paramBlockState, paramVaultSharedData);
/* 262 */     RandomSource randomSource = paramLevel.random;
/* 263 */     for (byte b = 0; b < 20; b++) {
/* 264 */       Vec3 vec3 = randomPosInsideCage(paramBlockPos, randomSource);
/* 265 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/* 266 */       paramLevel.addParticle(paramParticleOptions, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void emitDeactivationParticles(Level paramLevel, BlockPos paramBlockPos, ParticleOptions paramParticleOptions) {
/* 271 */     RandomSource randomSource = paramLevel.random;
/* 272 */     for (byte b = 0; b < 20; b++) {
/* 273 */       Vec3 vec31 = randomPosCenterOfCage(paramBlockPos, randomSource);
/* 274 */       Vec3 vec32 = new Vec3(randomSource.nextGaussian() * 0.02D, randomSource.nextGaussian() * 0.02D, randomSource.nextGaussian() * 0.02D);
/* 275 */       paramLevel.addParticle(paramParticleOptions, vec31.x(), vec31.y(), vec31.z(), vec32.x(), vec32.y(), vec32.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitIdleParticles(Level paramLevel, BlockPos paramBlockPos, VaultSharedData paramVaultSharedData, ParticleOptions paramParticleOptions) {
/* 280 */     RandomSource randomSource = paramLevel.getRandom();
/* 281 */     if (randomSource.nextFloat() <= 0.5F) {
/* 282 */       Vec3 vec3 = randomPosInsideCage(paramBlockPos, randomSource);
/* 283 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/* 284 */       if (shouldDisplayActiveEffects(paramVaultSharedData)) {
/* 285 */         paramLevel.addParticle(paramParticleOptions, vec3.x(), vec3.y(), vec3.z(), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitConnectionParticlesForPlayer(Level paramLevel, Vec3 paramVec3, Player paramPlayer) {
/* 291 */     RandomSource randomSource = paramLevel.random;
/* 292 */     Vec3 vec3 = paramVec3.vectorTo(paramPlayer.position().add(0.0D, (paramPlayer.getBbHeight() / 2.0F), 0.0D));
/* 293 */     int i = Mth.nextInt(randomSource, 2, 5);
/* 294 */     for (byte b = 0; b < i; b++) {
/* 295 */       Vec3 vec31 = vec3.offsetRandom(randomSource, 1.0F);
/* 296 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.VAULT_CONNECTION, paramVec3.x(), paramVec3.y(), paramVec3.z(), vec31.x(), vec31.y(), vec31.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitConnectionParticlesForNearbyPlayers(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, VaultSharedData paramVaultSharedData) {
/* 301 */     Set<UUID> set = paramVaultSharedData.getConnectedPlayers();
/*     */     
/* 303 */     if (set.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 307 */     Vec3 vec3 = keyholePos(paramBlockPos, (Direction)paramBlockState.getValue((Property)VaultBlock.FACING));
/*     */     
/* 309 */     for (UUID uUID : set) {
/* 310 */       Player player = paramLevel.getPlayerByUUID(uUID);
/* 311 */       if (player == null || !isWithinConnectionRange(paramBlockPos, paramVaultSharedData, player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 315 */       emitConnectionParticlesForPlayer(paramLevel, vec3, player);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isWithinConnectionRange(BlockPos paramBlockPos, VaultSharedData paramVaultSharedData, Player paramPlayer) {
/* 320 */     return (paramPlayer.blockPosition().distSqr((Vec3i)paramBlockPos) <= Mth.square(paramVaultSharedData.connectedParticlesRange()));
/*     */   }
/*     */   
/*     */   private static void playIdleSounds(Level paramLevel, BlockPos paramBlockPos, VaultSharedData paramVaultSharedData) {
/* 324 */     if (!shouldDisplayActiveEffects(paramVaultSharedData)) {
/*     */       return;
/*     */     }
/*     */     
/* 328 */     RandomSource randomSource = paramLevel.getRandom();
/* 329 */     if (randomSource.nextFloat() <= 0.02F) {
/* 330 */       paramLevel.playLocalSound(paramBlockPos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, randomSource.nextFloat() * 0.25F + 0.75F, randomSource.nextFloat() + 0.5F, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean shouldDisplayActiveEffects(VaultSharedData paramVaultSharedData) {
/* 335 */     return paramVaultSharedData.hasDisplayItem();
/*     */   }
/*     */   
/*     */   private static Vec3 randomPosCenterOfCage(BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 339 */     return Vec3.atLowerCornerOf((Vec3i)paramBlockPos).add(Mth.nextDouble(paramRandomSource, 0.4D, 0.6D), Mth.nextDouble(paramRandomSource, 0.4D, 0.6D), Mth.nextDouble(paramRandomSource, 0.4D, 0.6D));
/*     */   }
/*     */   
/*     */   private static Vec3 randomPosInsideCage(BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 343 */     return Vec3.atLowerCornerOf((Vec3i)paramBlockPos).add(Mth.nextDouble(paramRandomSource, 0.1D, 0.9D), Mth.nextDouble(paramRandomSource, 0.25D, 0.75D), Mth.nextDouble(paramRandomSource, 0.1D, 0.9D));
/*     */   }
/*     */   
/*     */   private static Vec3 keyholePos(BlockPos paramBlockPos, Direction paramDirection) {
/* 347 */     return Vec3.atBottomCenterOf((Vec3i)paramBlockPos).add(paramDirection.getStepX() * 0.5D, 1.75D, paramDirection.getStepZ() * 0.5D);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity$Client.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */