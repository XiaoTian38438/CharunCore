/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ interface ParticleEmission
/*     */ {
/*     */   public static final ParticleEmission NONE = (paramLevel, paramRandomSource, paramBlockPos, paramBoolean) -> {
/*     */     
/*     */     };
/*     */   public static final ParticleEmission SMALL_FLAMES;
/*     */   public static final ParticleEmission FLAMES_AND_SMOKE;
/*     */   public static final ParticleEmission SMOKE_INSIDE_AND_TOP_FACE;
/*     */   
/*     */   static {
/* 279 */     SMALL_FLAMES = ((paramLevel, paramRandomSource, paramBlockPos, paramBoolean) -> {
/*     */         if (paramRandomSource.nextInt(2) == 0) {
/*     */           Vec3 vec3 = paramBlockPos.getCenter().offsetRandom(paramRandomSource, 0.9F);
/*     */           addParticle(paramBoolean ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME, vec3, paramLevel);
/*     */         } 
/*     */       });
/* 285 */     FLAMES_AND_SMOKE = ((paramLevel, paramRandomSource, paramBlockPos, paramBoolean) -> {
/*     */         Vec3 vec3 = paramBlockPos.getCenter().offsetRandom(paramRandomSource, 1.0F);
/*     */         addParticle(ParticleTypes.SMOKE, vec3, paramLevel);
/*     */         addParticle(paramBoolean ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, vec3, paramLevel);
/*     */       });
/* 290 */     SMOKE_INSIDE_AND_TOP_FACE = ((paramLevel, paramRandomSource, paramBlockPos, paramBoolean) -> {
/*     */         Vec3 vec3 = paramBlockPos.getCenter().offsetRandom(paramRandomSource, 0.9F);
/*     */         if (paramRandomSource.nextInt(3) == 0) {
/*     */           addParticle(ParticleTypes.SMOKE, vec3, paramLevel);
/*     */         }
/*     */         if (paramLevel.getGameTime() % 20L == 0L) {
/*     */           Vec3 vec31 = paramBlockPos.getCenter().add(0.0D, 0.5D, 0.0D);
/*     */           int i = paramLevel.getRandom().nextInt(4) + 20;
/*     */           for (byte b = 0; b < i; b++) {
/*     */             addParticle(ParticleTypes.SMOKE, vec31, paramLevel);
/*     */           }
/*     */         } 
/*     */       });
/*     */   }
/*     */   
/*     */   private static void addParticle(SimpleParticleType paramSimpleParticleType, Vec3 paramVec3, Level paramLevel) {
/* 306 */     paramLevel.addParticle((ParticleOptions)paramSimpleParticleType, paramVec3.x(), paramVec3.y(), paramVec3.z(), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   void emit(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerState$ParticleEmission.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */