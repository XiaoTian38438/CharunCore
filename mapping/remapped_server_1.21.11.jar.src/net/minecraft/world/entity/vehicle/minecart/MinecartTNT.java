/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class MinecartTNT
/*     */   extends AbstractMinecart
/*     */ {
/*     */   private static final byte EVENT_PRIME = 10;
/*     */   private static final String TAG_EXPLOSION_POWER = "explosion_power";
/*     */   private static final String TAG_EXPLOSION_SPEED_FACTOR = "explosion_speed_factor";
/*     */   private static final String TAG_FUSE = "fuse";
/*     */   private static final float DEFAULT_EXPLOSION_POWER_BASE = 4.0F;
/*     */   private static final float DEFAULT_EXPLOSION_SPEED_FACTOR = 1.0F;
/*     */   private static final int NO_FUSE = -1;
/*     */   private DamageSource ignitionSource;
/*  42 */   private int fuse = -1;
/*  43 */   private float explosionPowerBase = 4.0F;
/*  44 */   private float explosionSpeedFactor = 1.0F;
/*     */   
/*     */   public MinecartTNT(EntityType<? extends MinecartTNT> paramEntityType, Level paramLevel) {
/*  47 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  52 */     return Blocks.TNT.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  57 */     super.tick();
/*     */     
/*  59 */     if (this.fuse > 0) {
/*  60 */       this.fuse--;
/*  61 */       level().addParticle((ParticleOptions)ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*  62 */     } else if (this.fuse == 0) {
/*  63 */       explode(this.ignitionSource, getDeltaMovement().horizontalDistanceSqr());
/*     */     } 
/*     */     
/*  66 */     if (this.horizontalCollision) {
/*  67 */       double d = getDeltaMovement().horizontalDistanceSqr();
/*  68 */       if (d >= 0.009999999776482582D) {
/*  69 */         explode(this.ignitionSource, d);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*  76 */     Entity entity = paramDamageSource.getDirectEntity();
/*     */     
/*  78 */     if (entity instanceof AbstractArrow) { AbstractArrow abstractArrow = (AbstractArrow)entity; if (abstractArrow.isOnFire()) {
/*  79 */         DamageSource damageSource = damageSources().explosion((Entity)this, paramDamageSource.getEntity());
/*  80 */         explode(damageSource, abstractArrow.getDeltaMovement().lengthSqr());
/*     */       }  }
/*  82 */      return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/*  87 */     double d = getDeltaMovement().horizontalDistanceSqr();
/*     */     
/*  89 */     if (damageSourceIgnitesTnt(paramDamageSource) || d >= 0.009999999776482582D) {
/*  90 */       if (this.fuse < 0) {
/*  91 */         primeFuse(paramDamageSource);
/*  92 */         this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
/*     */       } 
/*     */       return;
/*     */     } 
/*  96 */     destroy(paramServerLevel, getDropItem());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 101 */     return Items.TNT_MINECART;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 106 */     return new ItemStack((ItemLike)Items.TNT_MINECART);
/*     */   }
/*     */   
/*     */   protected void explode(DamageSource paramDamageSource, double paramDouble) {
/* 110 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 111 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 112 */         double d = Math.min(Math.sqrt(paramDouble), 5.0D);
/* 113 */         serverLevel.explode((Entity)this, paramDamageSource, null, getX(), getY(), getZ(), (float)(this.explosionPowerBase + this.explosionSpeedFactor * this.random.nextDouble() * 1.5D * d), false, Level.ExplosionInteraction.TNT);
/* 114 */         discard();
/* 115 */       } else if (isPrimed()) {
/* 116 */         discard();
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double paramDouble, float paramFloat, DamageSource paramDamageSource) {
/* 123 */     if (paramDouble >= 3.0D) {
/* 124 */       double d = paramDouble / 10.0D;
/* 125 */       explode(this.ignitionSource, d * d);
/*     */     } 
/*     */     
/* 128 */     return super.causeFallDamage(paramDouble, paramFloat, paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel paramServerLevel, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
/* 133 */     if (paramBoolean && this.fuse < 0) {
/* 134 */       primeFuse((DamageSource)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 140 */     if (paramByte == 10) {
/* 141 */       primeFuse((DamageSource)null);
/*     */     } else {
/* 143 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void primeFuse(DamageSource paramDamageSource) {
/* 148 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*     */         return;
/*     */       } }
/*     */     
/* 152 */     this.fuse = 80;
/*     */     
/* 154 */     if (!level().isClientSide()) {
/* 155 */       if (paramDamageSource != null && this.ignitionSource == null) {
/* 156 */         this.ignitionSource = damageSources().explosion((Entity)this, paramDamageSource.getEntity());
/*     */       }
/* 158 */       level().broadcastEntityEvent((Entity)this, (byte)10);
/* 159 */       if (!isSilent()) {
/* 160 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getFuse() {
/* 166 */     return this.fuse;
/*     */   }
/*     */   
/*     */   public boolean isPrimed() {
/* 170 */     return (this.fuse > -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState, float paramFloat) {
/* 175 */     if (isPrimed() && (paramBlockState.is(BlockTags.RAILS) || paramBlockGetter.getBlockState(paramBlockPos.above()).is(BlockTags.RAILS))) {
/* 176 */       return 0.0F;
/*     */     }
/*     */     
/* 179 */     return super.getBlockExplosionResistance(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFluidState, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldBlockExplode(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, float paramFloat) {
/* 184 */     if (isPrimed() && (paramBlockState.is(BlockTags.RAILS) || paramBlockGetter.getBlockState(paramBlockPos.above()).is(BlockTags.RAILS))) {
/* 185 */       return false;
/*     */     }
/*     */     
/* 188 */     return super.shouldBlockExplode(paramExplosion, paramBlockGetter, paramBlockPos, paramBlockState, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 193 */     super.readAdditionalSaveData(paramValueInput);
/* 194 */     this.fuse = paramValueInput.getIntOr("fuse", -1);
/* 195 */     this.explosionPowerBase = Mth.clamp(paramValueInput.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
/* 196 */     this.explosionSpeedFactor = Mth.clamp(paramValueInput.getFloatOr("explosion_speed_factor", 1.0F), 0.0F, 128.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 201 */     super.addAdditionalSaveData(paramValueOutput);
/* 202 */     paramValueOutput.putInt("fuse", this.fuse);
/*     */     
/* 204 */     if (this.explosionPowerBase != 4.0F) {
/* 205 */       paramValueOutput.putFloat("explosion_power", this.explosionPowerBase);
/*     */     }
/* 207 */     if (this.explosionSpeedFactor != 1.0F) {
/* 208 */       paramValueOutput.putFloat("explosion_speed_factor", this.explosionSpeedFactor);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldSourceDestroy(DamageSource paramDamageSource) {
/* 214 */     return damageSourceIgnitesTnt(paramDamageSource);
/*     */   }
/*     */   
/*     */   private static boolean damageSourceIgnitesTnt(DamageSource paramDamageSource) {
/* 218 */     Entity entity = paramDamageSource.getDirectEntity(); if (entity instanceof Projectile) { Projectile projectile = (Projectile)entity;
/* 219 */       return projectile.isOnFire(); }
/*     */     
/* 221 */     return (paramDamageSource.is(DamageTypeTags.IS_FIRE) || paramDamageSource.is(DamageTypeTags.IS_EXPLOSION));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartTNT.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */