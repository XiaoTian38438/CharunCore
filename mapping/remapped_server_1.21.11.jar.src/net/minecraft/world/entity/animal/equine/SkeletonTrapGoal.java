/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.monster.skeleton.Skeleton;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public class SkeletonTrapGoal extends Goal {
/*     */   public SkeletonTrapGoal(SkeletonHorse paramSkeletonHorse) {
/*  25 */     this.horse = paramSkeletonHorse;
/*     */   }
/*     */   private final SkeletonHorse horse;
/*     */   
/*     */   public boolean canUse() {
/*  30 */     return this.horse.level().hasNearbyAlivePlayer(this.horse.getX(), this.horse.getY(), this.horse.getZ(), 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  35 */     ServerLevel serverLevel = (ServerLevel)this.horse.level();
/*  36 */     DifficultyInstance difficultyInstance = serverLevel.getCurrentDifficultyAt(this.horse.blockPosition());
/*  37 */     this.horse.setTrap(false);
/*  38 */     this.horse.setTamed(true);
/*  39 */     this.horse.setAge(0);
/*  40 */     LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create((Level)serverLevel, EntitySpawnReason.TRIGGERED);
/*  41 */     if (lightningBolt == null) {
/*     */       return;
/*     */     }
/*  44 */     lightningBolt.snapTo(this.horse.getX(), this.horse.getY(), this.horse.getZ());
/*  45 */     lightningBolt.setVisualOnly(true);
/*  46 */     serverLevel.addFreshEntity((Entity)lightningBolt);
/*  47 */     Skeleton skeleton = createSkeleton(difficultyInstance, this.horse);
/*  48 */     if (skeleton == null) {
/*     */       return;
/*     */     }
/*  51 */     skeleton.startRiding((Entity)this.horse);
/*     */     
/*  53 */     serverLevel.addFreshEntityWithPassengers((Entity)skeleton);
/*     */     
/*  55 */     for (byte b = 0; b < 3; b++) {
/*  56 */       AbstractHorse abstractHorse = createHorse(difficultyInstance);
/*  57 */       if (abstractHorse != null) {
/*     */ 
/*     */         
/*  60 */         Skeleton skeleton1 = createSkeleton(difficultyInstance, abstractHorse);
/*  61 */         if (skeleton1 != null) {
/*     */ 
/*     */           
/*  64 */           skeleton1.startRiding((Entity)abstractHorse);
/*  65 */           abstractHorse.push(this.horse.getRandom().triangle(0.0D, 1.1485D), 0.0D, this.horse.getRandom().triangle(0.0D, 1.1485D));
/*  66 */           serverLevel.addFreshEntityWithPassengers((Entity)abstractHorse);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private AbstractHorse createHorse(DifficultyInstance paramDifficultyInstance) {
/*  71 */     SkeletonHorse skeletonHorse = (SkeletonHorse)EntityType.SKELETON_HORSE.create(this.horse.level(), EntitySpawnReason.TRIGGERED);
/*  72 */     if (skeletonHorse != null) {
/*  73 */       skeletonHorse.finalizeSpawn((ServerLevelAccessor)this.horse.level(), paramDifficultyInstance, EntitySpawnReason.TRIGGERED, (SpawnGroupData)null);
/*  74 */       skeletonHorse.setPos(this.horse.getX(), this.horse.getY(), this.horse.getZ());
/*  75 */       skeletonHorse.invulnerableTime = 60;
/*  76 */       skeletonHorse.setPersistenceRequired();
/*  77 */       skeletonHorse.setTamed(true);
/*  78 */       skeletonHorse.setAge(0);
/*     */     } 
/*  80 */     return skeletonHorse;
/*     */   }
/*     */   
/*     */   private Skeleton createSkeleton(DifficultyInstance paramDifficultyInstance, AbstractHorse paramAbstractHorse) {
/*  84 */     Skeleton skeleton = (Skeleton)EntityType.SKELETON.create(paramAbstractHorse.level(), EntitySpawnReason.TRIGGERED);
/*  85 */     if (skeleton != null) {
/*  86 */       skeleton.finalizeSpawn((ServerLevelAccessor)paramAbstractHorse.level(), paramDifficultyInstance, EntitySpawnReason.TRIGGERED, null);
/*  87 */       skeleton.setPos(paramAbstractHorse.getX(), paramAbstractHorse.getY(), paramAbstractHorse.getZ());
/*  88 */       skeleton.invulnerableTime = 60;
/*  89 */       skeleton.setPersistenceRequired();
/*     */       
/*  91 */       if (skeleton.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/*  92 */         skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)Items.IRON_HELMET));
/*     */       }
/*     */       
/*  95 */       enchant(skeleton, EquipmentSlot.MAINHAND, paramDifficultyInstance);
/*  96 */       enchant(skeleton, EquipmentSlot.HEAD, paramDifficultyInstance);
/*     */     } 
/*  98 */     return skeleton;
/*     */   }
/*     */   
/*     */   private void enchant(Skeleton paramSkeleton, EquipmentSlot paramEquipmentSlot, DifficultyInstance paramDifficultyInstance) {
/* 102 */     ItemStack itemStack = paramSkeleton.getItemBySlot(paramEquipmentSlot);
/* 103 */     itemStack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 104 */     EnchantmentHelper.enchantItemFromProvider(itemStack, paramSkeleton.level().registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, paramDifficultyInstance, paramSkeleton.getRandom());
/* 105 */     paramSkeleton.setItemSlot(paramEquipmentSlot, itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\SkeletonTrapGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */