/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Arrow
/*     */   extends AbstractArrow {
/*     */   private static final int EXPOSED_POTION_DECAY_TIME = 600;
/*     */   private static final int NO_EFFECT_COLOR = -1;
/*  24 */   private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(Arrow.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final byte EVENT_POTION_PUFF = 0;
/*     */   
/*     */   public Arrow(EntityType<? extends Arrow> paramEntityType, Level paramLevel) {
/*  29 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public Arrow(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/*  33 */     super(EntityType.ARROW, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack1, paramItemStack2);
/*  34 */     updateColor();
/*     */   }
/*     */   
/*     */   public Arrow(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/*  38 */     super(EntityType.ARROW, paramLivingEntity, paramLevel, paramItemStack1, paramItemStack2);
/*  39 */     updateColor();
/*     */   }
/*     */   
/*     */   private PotionContents getPotionContents() {
/*  43 */     return (PotionContents)getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*     */   }
/*     */   
/*     */   private float getPotionDurationScale() {
/*  47 */     return ((Float)getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue();
/*     */   }
/*     */   
/*     */   private void setPotionContents(PotionContents paramPotionContents) {
/*  51 */     getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, paramPotionContents);
/*  52 */     updateColor();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setPickupItemStack(ItemStack paramItemStack) {
/*  57 */     super.setPickupItemStack(paramItemStack);
/*  58 */     updateColor();
/*     */   }
/*     */   
/*     */   private void updateColor() {
/*  62 */     PotionContents potionContents = getPotionContents();
/*  63 */     this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(potionContents.equals(PotionContents.EMPTY) ? -1 : potionContents.getColor()));
/*     */   }
/*     */   
/*     */   public void addEffect(MobEffectInstance paramMobEffectInstance) {
/*  67 */     setPotionContents(getPotionContents().withEffectAdded(paramMobEffectInstance));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  72 */     super.defineSynchedData(paramBuilder);
/*  73 */     paramBuilder.define(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     super.tick();
/*     */     
/*  80 */     if (level().isClientSide()) {
/*  81 */       if (isInGround()) {
/*  82 */         if (this.inGroundTime % 5 == 0) {
/*  83 */           makeParticle(1);
/*     */         }
/*     */       } else {
/*  86 */         makeParticle(2);
/*     */       }
/*     */     
/*  89 */     } else if (isInGround() && this.inGroundTime != 0 && 
/*  90 */       !getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
/*  91 */       level().broadcastEntityEvent((Entity)this, (byte)0);
/*  92 */       setPickupItemStack(new ItemStack((ItemLike)Items.ARROW));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeParticle(int paramInt) {
/*  99 */     int i = getColor();
/* 100 */     if (i == -1 || paramInt <= 0) {
/*     */       return;
/*     */     }
/* 103 */     for (byte b = 0; b < paramInt; b++) {
/* 104 */       level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 109 */     return ((Integer)this.entityData.get(ID_EFFECT_COLOR)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity paramLivingEntity) {
/* 114 */     super.doPostHurtEffects(paramLivingEntity);
/*     */     
/* 116 */     Entity entity = getEffectSource();
/*     */     
/* 118 */     PotionContents potionContents = getPotionContents();
/* 119 */     float f = getPotionDurationScale();
/* 120 */     potionContents.forEachEffect(paramMobEffectInstance -> paramLivingEntity.addEffect(paramMobEffectInstance, paramEntity), f);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getDefaultPickupItem() {
/* 125 */     return new ItemStack((ItemLike)Items.ARROW);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 130 */     if (paramByte == 0) {
/* 131 */       int i = getColor();
/* 132 */       if (i != -1) {
/* 133 */         float f1 = (i >> 16 & 0xFF) / 255.0F;
/* 134 */         float f2 = (i >> 8 & 0xFF) / 255.0F;
/* 135 */         float f3 = (i >> 0 & 0xFF) / 255.0F;
/*     */         
/* 137 */         for (byte b = 0; b < 20; b++) {
/* 138 */           level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f1, f2, f3), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */       } 
/*     */     } else {
/* 142 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\arrow\Arrow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */