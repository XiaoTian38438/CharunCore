/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Bogged extends AbstractSkeleton implements Shearable {
/*  34 */   private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(Bogged.class, EntityDataSerializers.BOOLEAN);
/*     */   private static final String SHEARED_TAG_NAME = "sheared";
/*     */   private static final boolean DEFAULT_SHEARED = false;
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  39 */     return AbstractSkeleton.createAttributes()
/*  40 */       .add(Attributes.MAX_HEALTH, 16.0D);
/*     */   }
/*     */   
/*     */   public Bogged(EntityType<? extends Bogged> paramEntityType, Level paramLevel) {
/*  44 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  49 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  51 */     paramBuilder.define(DATA_SHEARED, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  56 */     super.addAdditionalSaveData(paramValueOutput);
/*  57 */     paramValueOutput.putBoolean("sheared", isSheared());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  62 */     super.readAdditionalSaveData(paramValueInput);
/*  63 */     setSheared(paramValueInput.getBooleanOr("sheared", false));
/*     */   }
/*     */   
/*     */   public boolean isSheared() {
/*  67 */     return ((Boolean)this.entityData.get(DATA_SHEARED)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setSheared(boolean paramBoolean) {
/*  71 */     this.entityData.set(DATA_SHEARED, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/*  76 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*  77 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/*  78 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  79 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/*  80 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)paramPlayer);
/*  81 */         itemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot()); }
/*     */       
/*  83 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/*  86 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  91 */     return SoundEvents.BOGGED_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  96 */     return SoundEvents.BOGGED_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 101 */     return SoundEvents.BOGGED_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 106 */     return SoundEvents.BOGGED_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack paramItemStack1, float paramFloat, ItemStack paramItemStack2) {
/* 111 */     AbstractArrow abstractArrow = super.getArrow(paramItemStack1, paramFloat, paramItemStack2);
/* 112 */     if (abstractArrow instanceof Arrow) { Arrow arrow = (Arrow)abstractArrow;
/* 113 */       arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 100)); }
/*     */     
/* 115 */     return abstractArrow;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getHardAttackInterval() {
/* 120 */     return 50;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAttackInterval() {
/* 125 */     return 70;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel paramServerLevel, SoundSource paramSoundSource, ItemStack paramItemStack) {
/* 130 */     paramServerLevel.playSound(null, (Entity)this, SoundEvents.BOGGED_SHEAR, paramSoundSource, 1.0F, 1.0F);
/*     */     
/* 132 */     spawnShearedMushrooms(paramServerLevel, paramItemStack);
/* 133 */     setSheared(true);
/*     */   }
/*     */   
/*     */   private void spawnShearedMushrooms(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 137 */     dropFromShearingLootTable(paramServerLevel, BuiltInLootTables.BOGGED_SHEAR, paramItemStack, (paramServerLevel, paramItemStack) -> spawnAtLocation(paramServerLevel, paramItemStack, getBbHeight()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 142 */     return (!isSheared() && isAlive());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\skeleton\Bogged.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */