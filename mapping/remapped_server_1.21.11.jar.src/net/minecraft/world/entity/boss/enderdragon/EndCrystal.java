/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class EndCrystal extends Entity {
/*  24 */   private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*  25 */   private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_SHOW_BOTTOM = true;
/*     */   public int time;
/*     */   
/*     */   public EndCrystal(EntityType<? extends EndCrystal> paramEntityType, Level paramLevel) {
/*  31 */     super(paramEntityType, paramLevel);
/*  32 */     this.blocksBuilding = true;
/*     */     
/*  34 */     this.time = this.random.nextInt(100000);
/*     */   }
/*     */   
/*     */   public EndCrystal(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  38 */     this(EntityType.END_CRYSTAL, paramLevel);
/*  39 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  44 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  49 */     paramBuilder.define(DATA_BEAM_TARGET, Optional.empty());
/*  50 */     paramBuilder.define(DATA_SHOW_BOTTOM, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  55 */     this.time++;
/*  56 */     applyEffectsFromBlocks();
/*  57 */     handlePortal();
/*     */     
/*  59 */     if (level() instanceof ServerLevel) {
/*  60 */       BlockPos blockPos = blockPosition();
/*  61 */       if (((ServerLevel)level()).getDragonFight() != null && level().getBlockState(blockPos).isAir()) {
/*  62 */         level().setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)level(), blockPos));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  69 */     paramValueOutput.storeNullable("beam_target", BlockPos.CODEC, getBeamTarget());
/*  70 */     paramValueOutput.putBoolean("ShowBottom", showsBottom());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  75 */     setBeamTarget(paramValueInput.read("beam_target", BlockPos.CODEC).orElse(null));
/*  76 */     setShowBottom(paramValueInput.getBooleanOr("ShowBottom", true));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/*  81 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtClient(DamageSource paramDamageSource) {
/*  86 */     if (isInvulnerableToBase(paramDamageSource)) {
/*  87 */       return false;
/*     */     }
/*  89 */     if (paramDamageSource.getEntity() instanceof EnderDragon) {
/*  90 */       return false;
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*  97 */     if (isInvulnerableToBase(paramDamageSource)) {
/*  98 */       return false;
/*     */     }
/* 100 */     if (paramDamageSource.getEntity() instanceof EnderDragon) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (!isRemoved()) {
/* 104 */       remove(Entity.RemovalReason.KILLED);
/*     */       
/* 106 */       if (!paramDamageSource.is(DamageTypeTags.IS_EXPLOSION)) {
/* 107 */         DamageSource damageSource = (paramDamageSource.getEntity() != null) ? damageSources().explosion(this, paramDamageSource.getEntity()) : null;
/* 108 */         paramServerLevel.explode(this, damageSource, null, getX(), getY(), getZ(), 6.0F, false, Level.ExplosionInteraction.BLOCK);
/*     */       } 
/*     */       
/* 111 */       onDestroyedBy(paramServerLevel, paramDamageSource);
/*     */     } 
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel paramServerLevel) {
/* 118 */     onDestroyedBy(paramServerLevel, damageSources().generic());
/* 119 */     super.kill(paramServerLevel);
/*     */   }
/*     */   
/*     */   private void onDestroyedBy(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/* 123 */     EndDragonFight endDragonFight = paramServerLevel.getDragonFight();
/* 124 */     if (endDragonFight != null) {
/* 125 */       endDragonFight.onCrystalDestroyed(this, paramDamageSource);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBeamTarget(BlockPos paramBlockPos) {
/* 130 */     getEntityData().set(DATA_BEAM_TARGET, Optional.ofNullable(paramBlockPos));
/*     */   }
/*     */   
/*     */   public BlockPos getBeamTarget() {
/* 134 */     return ((Optional<BlockPos>)getEntityData().get(DATA_BEAM_TARGET)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setShowBottom(boolean paramBoolean) {
/* 138 */     getEntityData().set(DATA_SHOW_BOTTOM, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public boolean showsBottom() {
/* 142 */     return ((Boolean)getEntityData().get(DATA_SHOW_BOTTOM)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 147 */     return (super.shouldRenderAtSqrDistance(paramDouble) || getBeamTarget() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 152 */     return new ItemStack((ItemLike)Items.END_CRYSTAL);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\EndCrystal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */