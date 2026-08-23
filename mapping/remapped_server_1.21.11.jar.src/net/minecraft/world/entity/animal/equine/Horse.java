/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Horse extends AbstractHorse {
/*  40 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.INT);
/*     */   
/*  42 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.HORSE.getDimensions()
/*  43 */     .withAttachments(EntityAttachments.builder()
/*  44 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.HORSE.getHeight() + 0.125F, 0.0F))
/*     */     
/*  46 */     .scale(0.5F);
/*     */   
/*     */   private static final int DEFAULT_VARIANT = 0;
/*     */   
/*     */   public Horse(EntityType<? extends Horse> paramEntityType, Level paramLevel) {
/*  51 */     super((EntityType)paramEntityType, paramLevel);
/*  52 */     setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
/*  53 */     setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes(RandomSource paramRandomSource) {
/*  58 */     Objects.requireNonNull(paramRandomSource); getAttribute(Attributes.MAX_HEALTH).setBaseValue(generateMaxHealth(paramRandomSource::nextInt));
/*  59 */     Objects.requireNonNull(paramRandomSource); getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateSpeed(paramRandomSource::nextDouble));
/*  60 */     Objects.requireNonNull(paramRandomSource); getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(paramRandomSource::nextDouble));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  65 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  67 */     paramBuilder.define(DATA_ID_TYPE_VARIANT, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  72 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/*  74 */     paramValueOutput.putInt("Variant", getTypeVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  79 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/*  81 */     setTypeVariant(paramValueInput.getIntOr("Variant", 0));
/*     */   }
/*     */   
/*     */   private void setTypeVariant(int paramInt) {
/*  85 */     this.entityData.set(DATA_ID_TYPE_VARIANT, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   private int getTypeVariant() {
/*  89 */     return ((Integer)this.entityData.get(DATA_ID_TYPE_VARIANT)).intValue();
/*     */   }
/*     */   
/*     */   private void setVariantAndMarkings(Variant paramVariant, Markings paramMarkings) {
/*  93 */     setTypeVariant(paramVariant.getId() & 0xFF | paramMarkings.getId() << 8 & 0xFF00);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/*  97 */     return Variant.byId(getTypeVariant() & 0xFF);
/*     */   }
/*     */   
/*     */   private void setVariant(Variant paramVariant) {
/* 101 */     setTypeVariant(paramVariant.getId() & 0xFF | getTypeVariant() & 0xFFFFFF00);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/* 106 */     if (paramDataComponentType == DataComponents.HORSE_VARIANT) {
/* 107 */       return (T)castComponentValue(paramDataComponentType, getVariant());
/*     */     }
/*     */     
/* 110 */     return (T)super.get(paramDataComponentType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 115 */     applyImplicitComponentIfPresent(paramDataComponentGetter, DataComponents.HORSE_VARIANT);
/* 116 */     super.applyImplicitComponents(paramDataComponentGetter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> paramDataComponentType, T paramT) {
/* 121 */     if (paramDataComponentType == DataComponents.HORSE_VARIANT) {
/* 122 */       setVariant((Variant)castComponentValue(DataComponents.HORSE_VARIANT, paramT));
/* 123 */       return true;
/*     */     } 
/*     */     
/* 126 */     return super.applyImplicitComponent(paramDataComponentType, paramT);
/*     */   }
/*     */   
/*     */   public Markings getMarkings() {
/* 130 */     return Markings.byId((getTypeVariant() & 0xFF00) >> 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playGallopSound(SoundType paramSoundType) {
/* 135 */     super.playGallopSound(paramSoundType);
/* 136 */     if (this.random.nextInt(10) == 0) {
/* 137 */       playSound(SoundEvents.HORSE_BREATHE, paramSoundType.getVolume() * 0.6F, paramSoundType.getPitch());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 143 */     return SoundEvents.HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 148 */     return SoundEvents.HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 153 */     return SoundEvents.HORSE_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 158 */     return SoundEvents.HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAngrySound() {
/* 163 */     return SoundEvents.HORSE_ANGRY;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 168 */     boolean bool = (!isBaby() && isTamed() && paramPlayer.isSecondaryUseActive()) ? true : false;
/* 169 */     if (isVehicle() || bool) {
/* 170 */       return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */     }
/*     */     
/* 173 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*     */     
/* 175 */     if (!itemStack.isEmpty()) {
/* 176 */       if (isFood(itemStack)) {
/* 177 */         return fedFood(paramPlayer, itemStack);
/*     */       }
/*     */       
/* 180 */       if (!isTamed()) {
/* 181 */         makeMad();
/* 182 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 185 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal paramAnimal) {
/* 190 */     if (paramAnimal == this) {
/* 191 */       return false;
/*     */     }
/*     */     
/* 194 */     if (paramAnimal instanceof Donkey || paramAnimal instanceof Horse) {
/* 195 */       return (canParent() && ((AbstractHorse)paramAnimal).canParent());
/*     */     }
/*     */     
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 203 */     if (paramAgeableMob instanceof Donkey) {
/* 204 */       Mule mule = (Mule)EntityType.MULE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/* 205 */       if (mule != null) {
/* 206 */         setOffspringAttributes(paramAgeableMob, mule);
/*     */       }
/* 208 */       return (AgeableMob)mule;
/*     */     } 
/* 210 */     Horse horse1 = (Horse)paramAgeableMob;
/*     */     
/* 212 */     Horse horse2 = (Horse)EntityType.HORSE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/* 213 */     if (horse2 != null) {
/*     */       Variant variant; Markings markings;
/* 215 */       int i = this.random.nextInt(9);
/* 216 */       if (i < 4) {
/* 217 */         variant = getVariant();
/* 218 */       } else if (i < 8) {
/* 219 */         variant = horse1.getVariant();
/*     */       } else {
/* 221 */         variant = (Variant)Util.getRandom((Object[])Variant.values(), this.random);
/*     */       } 
/*     */ 
/*     */       
/* 225 */       int j = this.random.nextInt(5);
/* 226 */       if (j < 2) {
/* 227 */         markings = getMarkings();
/* 228 */       } else if (j < 4) {
/* 229 */         markings = horse1.getMarkings();
/*     */       } else {
/* 231 */         markings = (Markings)Util.getRandom((Object[])Markings.values(), this.random);
/*     */       } 
/*     */       
/* 234 */       horse2.setVariantAndMarkings(variant, markings);
/* 235 */       setOffspringAttributes(paramAgeableMob, horse2);
/*     */     } 
/* 237 */     return (AgeableMob)horse2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot paramEquipmentSlot) {
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void hurtArmor(DamageSource paramDamageSource, float paramFloat) {
/* 247 */     doHurtEquipment(paramDamageSource, paramFloat, new EquipmentSlot[] { EquipmentSlot.BODY });
/*     */   }
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*     */     HorseGroupData horseGroupData;
/*     */     Variant variant;
/* 252 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/*     */     
/* 254 */     if (paramSpawnGroupData instanceof HorseGroupData) {
/* 255 */       variant = ((HorseGroupData)paramSpawnGroupData).variant;
/*     */     } else {
/* 257 */       variant = (Variant)Util.getRandom((Object[])Variant.values(), randomSource);
/* 258 */       horseGroupData = new HorseGroupData(variant);
/*     */     } 
/* 260 */     setVariantAndMarkings(variant, (Markings)Util.getRandom((Object[])Markings.values(), randomSource));
/*     */     
/* 262 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, (SpawnGroupData)horseGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 267 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */   
/*     */   public static class HorseGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Variant variant;
/*     */     
/*     */     public HorseGroupData(Variant param1Variant) {
/* 274 */       super(true);
/* 275 */       this.variant = param1Variant;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\Horse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */