/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.PowerParticleOption;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonLandingPhase extends AbstractDragonPhaseInstance {
/*    */   private Vec3 targetLocation;
/*    */   
/*    */   public DragonLandingPhase(EnderDragon paramEnderDragon) {
/* 18 */     super(paramEnderDragon);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doClientTick() {
/* 23 */     Vec3 vec3 = this.dragon.getHeadLookVector(1.0F).normalize();
/* 24 */     vec3.yRot(-0.7853982F);
/*    */     
/* 26 */     double d1 = this.dragon.head.getX();
/* 27 */     double d2 = this.dragon.head.getY(0.5D);
/* 28 */     double d3 = this.dragon.head.getZ();
/* 29 */     for (byte b = 0; b < 8; b++) {
/* 30 */       RandomSource randomSource = this.dragon.getRandom();
/* 31 */       double d4 = d1 + randomSource.nextGaussian() / 2.0D;
/* 32 */       double d5 = d2 + randomSource.nextGaussian() / 2.0D;
/* 33 */       double d6 = d3 + randomSource.nextGaussian() / 2.0D;
/* 34 */       Vec3 vec31 = this.dragon.getDeltaMovement();
/* 35 */       this.dragon.level().addParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F), d4, d5, d6, -vec3.x * 0.07999999821186066D + vec31.x, -vec3.y * 0.30000001192092896D + vec31.y, -vec3.z * 0.07999999821186066D + vec31.z);
/* 36 */       vec3.yRot(0.19634955F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel paramServerLevel) {
/* 42 */     if (this.targetLocation == null) {
/* 43 */       this.targetLocation = Vec3.atBottomCenterOf((Vec3i)paramServerLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin())));
/*    */     }
/*    */     
/* 46 */     if (this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ()) < 1.0D) {
/* 47 */       ((DragonSittingFlamingPhase)this.dragon.getPhaseManager().<DragonSittingFlamingPhase>getPhase(EnderDragonPhase.SITTING_FLAMING)).resetFlameCount();
/* 48 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFlySpeed() {
/* 54 */     return 1.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getTurnSpeed() {
/* 59 */     float f1 = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
/* 60 */     float f2 = Math.min(f1, 40.0F);
/*    */     
/* 62 */     return f2 / f1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 67 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlyTargetLocation() {
/* 72 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonLandingPhase> getPhase() {
/* 77 */     return EnderDragonPhase.LANDING;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonLandingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */