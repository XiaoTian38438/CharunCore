/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockAttachedEntity
/*     */   extends Entity {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private int checkInterval;
/*     */   protected BlockPos pos;
/*     */   
/*     */   protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> paramEntityType, Level paramLevel) {
/*  29 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> paramEntityType, Level paramLevel, BlockPos paramBlockPos) {
/*  33 */     this(paramEntityType, paramLevel);
/*  34 */     this.pos = paramBlockPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void recalculateBoundingBox();
/*     */   
/*     */   public void tick() {
/*  41 */     Level level = level(); ServerLevel serverLevel = (ServerLevel)level;
/*  42 */     checkBelowWorld();
/*  43 */     if (level instanceof ServerLevel && this.checkInterval++ == 100) {
/*  44 */       this.checkInterval = 0;
/*  45 */       if (!isRemoved() && !survives()) {
/*  46 */         discard();
/*  47 */         dropItem(serverLevel, (Entity)null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean survives();
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/*  57 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean skipAttackInteraction(Entity paramEntity) {
/*  62 */     if (paramEntity instanceof Player) { Player player = (Player)paramEntity;
/*  63 */       if (!level().mayInteract((Entity)player, this.pos)) {
/*  64 */         return true;
/*     */       }
/*  66 */       return hurtOrSimulate(damageSources().playerAttack(player), 0.0F); }
/*     */     
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource paramDamageSource) {
/*  73 */     return !isInvulnerableToBase(paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*  78 */     if (isInvulnerableToBase(paramDamageSource)) {
/*  79 */       return false;
/*     */     }
/*  81 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && paramDamageSource.getEntity() instanceof net.minecraft.world.entity.Mob) {
/*  82 */       return false;
/*     */     }
/*  84 */     if (!isRemoved()) {
/*  85 */       kill(paramServerLevel);
/*  86 */       markHurt();
/*  87 */       dropItem(paramServerLevel, paramDamageSource.getEntity());
/*     */     } 
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion paramExplosion) {
/*  94 */     Entity entity = paramExplosion.getDirectSourceEntity();
/*  95 */     if (entity != null && entity.isInWater()) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (paramExplosion.shouldAffectBlocklikeEntities()) {
/*  99 */       return super.ignoreExplosion(paramExplosion);
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType paramMoverType, Vec3 paramVec3) {
/* 106 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!isRemoved() && paramVec3.lengthSqr() > 0.0D) {
/* 107 */         kill(serverLevel);
/* 108 */         dropItem(serverLevel, (Entity)null);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   public void push(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 114 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!isRemoved() && paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2 + paramDouble3 * paramDouble3 > 0.0D) {
/* 115 */         kill(serverLevel);
/* 116 */         dropItem(serverLevel, (Entity)null);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 122 */     paramValueOutput.store("block_pos", BlockPos.CODEC, getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 127 */     BlockPos blockPos = paramValueInput.read("block_pos", BlockPos.CODEC).orElse(null);
/* 128 */     if (blockPos == null || !blockPos.closerThan((Vec3i)blockPosition(), 16.0D)) {
/* 129 */       LOGGER.error("Block-attached entity at invalid position: {}", blockPos);
/*     */       return;
/*     */     } 
/* 132 */     this.pos = blockPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void dropItem(ServerLevel paramServerLevel, Entity paramEntity);
/*     */   
/*     */   protected boolean repositionEntityAfterLoad() {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPos(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 144 */     this.pos = BlockPos.containing(paramDouble1, paramDouble2, paramDouble3);
/* 145 */     recalculateBoundingBox();
/* 146 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 150 */     return this.pos;
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel paramServerLevel, LightningBolt paramLightningBolt) {}
/*     */   
/*     */   public void refreshDimensions() {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\BlockAttachedEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */