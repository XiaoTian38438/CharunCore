/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class PrimedTnt extends Entity implements TraceableEntity {
/*  33 */   private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.INT);
/*  34 */   private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.BLOCK_STATE);
/*     */   private static final short DEFAULT_FUSE_TIME = 80;
/*     */   private static final float DEFAULT_EXPLOSION_POWER = 4.0F;
/*  37 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.TNT.defaultBlockState();
/*     */   
/*     */   private static final String TAG_BLOCK_STATE = "block_state";
/*     */   public static final String TAG_FUSE = "fuse";
/*     */   private static final String TAG_EXPLOSION_POWER = "explosion_power";
/*     */   
/*  43 */   private static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator()
/*     */     {
/*     */       public boolean shouldBlockExplode(Explosion param1Explosion, BlockGetter param1BlockGetter, BlockPos param1BlockPos, BlockState param1BlockState, float param1Float) {
/*  46 */         if (param1BlockState.is(Blocks.NETHER_PORTAL)) {
/*  47 */           return false;
/*     */         }
/*  49 */         return super.shouldBlockExplode(param1Explosion, param1BlockGetter, param1BlockPos, param1BlockState, param1Float);
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Float> getBlockExplosionResistance(Explosion param1Explosion, BlockGetter param1BlockGetter, BlockPos param1BlockPos, BlockState param1BlockState, FluidState param1FluidState) {
/*  54 */         if (param1BlockState.is(Blocks.NETHER_PORTAL)) {
/*  55 */           return Optional.empty();
/*     */         }
/*  57 */         return super.getBlockExplosionResistance(param1Explosion, param1BlockGetter, param1BlockPos, param1BlockState, param1FluidState);
/*     */       }
/*     */     };
/*     */   
/*     */   private EntityReference<LivingEntity> owner;
/*     */   private boolean usedPortal;
/*  63 */   private float explosionPower = 4.0F;
/*     */   
/*     */   public PrimedTnt(EntityType<? extends PrimedTnt> paramEntityType, Level paramLevel) {
/*  66 */     super(paramEntityType, paramLevel);
/*  67 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   public PrimedTnt(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, LivingEntity paramLivingEntity) {
/*  71 */     this(EntityType.TNT, paramLevel);
/*     */     
/*  73 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */     
/*  75 */     double d = paramLevel.random.nextDouble() * 6.2831854820251465D;
/*     */     
/*  77 */     setDeltaMovement(
/*  78 */         -Math.sin(d) * 0.02D, 0.20000000298023224D, 
/*     */         
/*  80 */         -Math.cos(d) * 0.02D);
/*     */ 
/*     */     
/*  83 */     setFuse(80);
/*     */     
/*  85 */     this.xo = paramDouble1;
/*  86 */     this.yo = paramDouble2;
/*  87 */     this.zo = paramDouble3;
/*  88 */     this.owner = EntityReference.of((UniquelyIdentifyable)paramLivingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  93 */     paramBuilder.define(DATA_FUSE_ID, Integer.valueOf(80));
/*  94 */     paramBuilder.define(DATA_BLOCK_STATE_ID, DEFAULT_BLOCK_STATE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  99 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 104 */     return !isRemoved();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 109 */     return 0.04D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 114 */     handlePortal();
/* 115 */     applyGravity();
/* 116 */     move(MoverType.SELF, getDeltaMovement());
/* 117 */     applyEffectsFromBlocks();
/* 118 */     setDeltaMovement(getDeltaMovement().scale(0.98D));
/*     */     
/* 120 */     if (onGround())
/*     */     {
/* 122 */       setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/*     */     }
/*     */     
/* 125 */     int i = getFuse() - 1;
/* 126 */     setFuse(i);
/* 127 */     if (i <= 0) {
/* 128 */       discard();
/* 129 */       if (!level().isClientSide()) {
/* 130 */         explode();
/*     */       }
/*     */     } else {
/* 133 */       updateInWaterStateAndDoFluidPushing();
/* 134 */       if (level().isClientSide()) {
/* 135 */         level().addParticle((ParticleOptions)ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void explode() {
/* 141 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 142 */         level().explode(this, Explosion.getDefaultDamageSource(level(), this), this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null, getX(), getY(0.0625D), getZ(), this.explosionPower, false, Level.ExplosionInteraction.TNT);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 148 */     paramValueOutput.putShort("fuse", (short)getFuse());
/* 149 */     paramValueOutput.store("block_state", BlockState.CODEC, getBlockState());
/* 150 */     if (this.explosionPower != 4.0F) {
/* 151 */       paramValueOutput.putFloat("explosion_power", this.explosionPower);
/*     */     }
/* 153 */     EntityReference.store(this.owner, paramValueOutput, "owner");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 158 */     setFuse(paramValueInput.getShortOr("fuse", (short)80));
/* 159 */     setBlockState(paramValueInput.read("block_state", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE));
/* 160 */     this.explosionPower = Mth.clamp(paramValueInput.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
/* 161 */     this.owner = EntityReference.read(paramValueInput, "owner");
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getOwner() {
/* 166 */     return EntityReference.getLivingEntity(this.owner, level());
/*     */   }
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity paramEntity) {
/* 171 */     super.restoreFrom(paramEntity);
/* 172 */     if (paramEntity instanceof PrimedTnt) { PrimedTnt primedTnt = (PrimedTnt)paramEntity;
/* 173 */       this.owner = primedTnt.owner; }
/*     */   
/*     */   }
/*     */   
/*     */   public void setFuse(int paramInt) {
/* 178 */     this.entityData.set(DATA_FUSE_ID, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public int getFuse() {
/* 182 */     return ((Integer)this.entityData.get(DATA_FUSE_ID)).intValue();
/*     */   }
/*     */   
/*     */   public void setBlockState(BlockState paramBlockState) {
/* 186 */     this.entityData.set(DATA_BLOCK_STATE_ID, paramBlockState);
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 190 */     return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID);
/*     */   }
/*     */   
/*     */   private void setUsedPortal(boolean paramBoolean) {
/* 194 */     this.usedPortal = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition paramTeleportTransition) {
/* 199 */     Entity entity = super.teleport(paramTeleportTransition);
/* 200 */     if (entity instanceof PrimedTnt) { PrimedTnt primedTnt = (PrimedTnt)entity;
/* 201 */       primedTnt.setUsedPortal(true); }
/*     */     
/* 203 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 208 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\item\PrimedTnt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */