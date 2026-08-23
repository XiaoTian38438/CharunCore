/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.PowerParticleOption;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DragonSittingFlamingPhase
/*     */   extends AbstractDragonSittingPhase
/*     */ {
/*     */   private static final int FLAME_DURATION = 200;
/*     */   private static final int SITTING_FLAME_ATTACKS_COUNT = 4;
/*     */   private static final int WARMUP_TIME = 10;
/*     */   
/*     */   public DragonSittingFlamingPhase(EnderDragon paramEnderDragon) {
/*  26 */     super(paramEnderDragon);
/*     */   }
/*     */   private int flameTicks; private int flameCount; private AreaEffectCloud flame;
/*     */   
/*     */   public void doClientTick() {
/*  31 */     this.flameTicks++;
/*     */     
/*  33 */     if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
/*  34 */       Vec3 vec3 = this.dragon.getHeadLookVector(1.0F).normalize();
/*  35 */       vec3.yRot(-0.7853982F);
/*  36 */       double d1 = this.dragon.head.getX();
/*  37 */       double d2 = this.dragon.head.getY(0.5D);
/*  38 */       double d3 = this.dragon.head.getZ();
/*  39 */       for (byte b = 0; b < 8; b++) {
/*  40 */         double d4 = d1 + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  41 */         double d5 = d2 + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  42 */         double d6 = d3 + this.dragon.getRandom().nextGaussian() / 2.0D;
/*  43 */         for (byte b1 = 0; b1 < 6; b1++) {
/*  44 */           this.dragon.level().addParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F), d4, d5, d6, -vec3.x * 0.07999999821186066D * b1, -vec3.y * 0.6000000238418579D, -vec3.z * 0.07999999821186066D * b1);
/*     */         }
/*  46 */         vec3.yRot(0.19634955F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doServerTick(ServerLevel paramServerLevel) {
/*  53 */     this.flameTicks++;
/*     */     
/*  55 */     if (this.flameTicks >= 200) {
/*  56 */       if (this.flameCount >= 4) {
/*  57 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
/*     */       } else {
/*  59 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
/*     */       } 
/*  61 */     } else if (this.flameTicks == 10) {
/*  62 */       Vec3 vec3 = (new Vec3(this.dragon.head.getX() - this.dragon.getX(), 0.0D, this.dragon.head.getZ() - this.dragon.getZ())).normalize();
/*  63 */       float f = 5.0F;
/*  64 */       double d1 = this.dragon.head.getX() + vec3.x * 5.0D / 2.0D;
/*  65 */       double d2 = this.dragon.head.getZ() + vec3.z * 5.0D / 2.0D;
/*  66 */       double d3 = this.dragon.head.getY(0.5D);
/*  67 */       double d4 = d3;
/*     */       
/*  69 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(d1, d4, d2);
/*  70 */       while (paramServerLevel.isEmptyBlock((BlockPos)mutableBlockPos)) {
/*  71 */         d4--;
/*  72 */         if (d4 < 0.0D) {
/*  73 */           d4 = d3;
/*     */           break;
/*     */         } 
/*  76 */         mutableBlockPos.set(d1, d4, d2);
/*     */       } 
/*  78 */       d4 = (Mth.floor(d4) + 1);
/*  79 */       this.flame = new AreaEffectCloud((Level)paramServerLevel, d1, d4, d2);
/*  80 */       this.flame.setOwner((LivingEntity)this.dragon);
/*  81 */       this.flame.setRadius(5.0F);
/*  82 */       this.flame.setDuration(200);
/*  83 */       this.flame.setCustomParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
/*  84 */       this.flame.setPotionDurationScale(0.25F);
/*  85 */       this.flame.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE));
/*  86 */       paramServerLevel.addFreshEntity((Entity)this.flame);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  92 */     this.flameTicks = 0;
/*  93 */     this.flameCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  98 */     if (this.flame != null) {
/*  99 */       this.flame.discard();
/* 100 */       this.flame = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonPhase<DragonSittingFlamingPhase> getPhase() {
/* 106 */     return EnderDragonPhase.SITTING_FLAMING;
/*     */   }
/*     */   
/*     */   public void resetFlameCount() {
/* 110 */     this.flameCount = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingFlamingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */