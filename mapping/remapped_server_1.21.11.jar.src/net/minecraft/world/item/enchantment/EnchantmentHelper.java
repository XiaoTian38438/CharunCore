/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.random.WeightedRandom;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class EnchantmentHelper {
/*     */   public static int getItemEnchantmentLevel(Holder<Enchantment> paramHolder, ItemStack paramItemStack) {
/*  51 */     ItemEnchantments itemEnchantments = (ItemEnchantments)paramItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/*  52 */     return itemEnchantments.getLevel(paramHolder);
/*     */   }
/*     */   
/*     */   public static ItemEnchantments updateEnchantments(ItemStack paramItemStack, Consumer<ItemEnchantments.Mutable> paramConsumer) {
/*  56 */     DataComponentType<ItemEnchantments> dataComponentType = getComponentType(paramItemStack);
/*  57 */     ItemEnchantments itemEnchantments1 = (ItemEnchantments)paramItemStack.get(dataComponentType);
/*  58 */     if (itemEnchantments1 == null) {
/*  59 */       return ItemEnchantments.EMPTY;
/*     */     }
/*  61 */     ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(itemEnchantments1);
/*  62 */     paramConsumer.accept(mutable);
/*  63 */     ItemEnchantments itemEnchantments2 = mutable.toImmutable();
/*  64 */     paramItemStack.set(dataComponentType, itemEnchantments2);
/*  65 */     return itemEnchantments2;
/*     */   }
/*     */   
/*     */   public static boolean canStoreEnchantments(ItemStack paramItemStack) {
/*  69 */     return paramItemStack.has(getComponentType(paramItemStack));
/*     */   }
/*     */   
/*     */   public static void setEnchantments(ItemStack paramItemStack, ItemEnchantments paramItemEnchantments) {
/*  73 */     paramItemStack.set(getComponentType(paramItemStack), paramItemEnchantments);
/*     */   }
/*     */   
/*     */   public static ItemEnchantments getEnchantmentsForCrafting(ItemStack paramItemStack) {
/*  77 */     return (ItemEnchantments)paramItemStack.getOrDefault(getComponentType(paramItemStack), ItemEnchantments.EMPTY);
/*     */   }
/*     */   
/*     */   private static DataComponentType<ItemEnchantments> getComponentType(ItemStack paramItemStack) {
/*  81 */     return paramItemStack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
/*     */   }
/*     */   
/*     */   public static boolean hasAnyEnchantments(ItemStack paramItemStack) {
/*  85 */     return (!((ItemEnchantments)paramItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty() || 
/*  86 */       !((ItemEnchantments)paramItemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty());
/*     */   }
/*     */   
/*     */   public static int processDurabilityChange(ServerLevel paramServerLevel, ItemStack paramItemStack, int paramInt) {
/*  90 */     MutableFloat mutableFloat = new MutableFloat(paramInt);
/*  91 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyDurabilityChange(paramServerLevel, paramInt, paramItemStack, paramMutableFloat));
/*  92 */     return mutableFloat.intValue();
/*     */   }
/*     */   
/*     */   public static int processAmmoUse(ServerLevel paramServerLevel, ItemStack paramItemStack1, ItemStack paramItemStack2, int paramInt) {
/*  96 */     MutableFloat mutableFloat = new MutableFloat(paramInt);
/*  97 */     runIterationOnItem(paramItemStack1, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyAmmoCount(paramServerLevel, paramInt, paramItemStack, paramMutableFloat));
/*  98 */     return mutableFloat.intValue();
/*     */   }
/*     */   
/*     */   public static int processBlockExperience(ServerLevel paramServerLevel, ItemStack paramItemStack, int paramInt) {
/* 102 */     MutableFloat mutableFloat = new MutableFloat(paramInt);
/* 103 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyBlockExperience(paramServerLevel, paramInt, paramItemStack, paramMutableFloat));
/* 104 */     return mutableFloat.intValue();
/*     */   }
/*     */   
/*     */   public static int processMobExperience(ServerLevel paramServerLevel, Entity paramEntity1, Entity paramEntity2, int paramInt) {
/* 108 */     if (paramEntity1 instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)paramEntity1;
/* 109 */       MutableFloat mutableFloat = new MutableFloat(paramInt);
/* 110 */       runIterationOnEquipment(livingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).modifyMobExperience(paramServerLevel, paramInt, paramEnchantedItemInUse.itemStack(), paramEntity, paramMutableFloat));
/* 111 */       return mutableFloat.intValue(); }
/*     */     
/* 113 */     return paramInt;
/*     */   }
/*     */   
/*     */   public static ItemStack createBook(EnchantmentInstance paramEnchantmentInstance) {
/* 117 */     ItemStack itemStack = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/* 118 */     itemStack.enchant(paramEnchantmentInstance.enchantment(), paramEnchantmentInstance.level());
/* 119 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runIterationOnItem(ItemStack paramItemStack, EnchantmentVisitor paramEnchantmentVisitor) {
/* 128 */     ItemEnchantments itemEnchantments = (ItemEnchantments)paramItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 129 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 130 */       paramEnchantmentVisitor.accept((Holder<Enchantment>)entry.getKey(), entry.getIntValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void runIterationOnItem(ItemStack paramItemStack, EquipmentSlot paramEquipmentSlot, LivingEntity paramLivingEntity, EnchantmentInSlotVisitor paramEnchantmentInSlotVisitor) {
/* 140 */     if (paramItemStack.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     ItemEnchantments itemEnchantments = (ItemEnchantments)paramItemStack.get(DataComponents.ENCHANTMENTS);
/* 145 */     if (itemEnchantments == null || itemEnchantments.isEmpty()) {
/*     */       return;
/*     */     }
/* 148 */     EnchantedItemInUse enchantedItemInUse = new EnchantedItemInUse(paramItemStack, paramEquipmentSlot, paramLivingEntity);
/* 149 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 150 */       Holder<Enchantment> holder = (Holder)entry.getKey();
/* 151 */       if (((Enchantment)holder.value()).matchingSlot(paramEquipmentSlot)) {
/* 152 */         paramEnchantmentInSlotVisitor.accept(holder, entry.getIntValue(), enchantedItemInUse);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void runIterationOnEquipment(LivingEntity paramLivingEntity, EnchantmentInSlotVisitor paramEnchantmentInSlotVisitor) {
/* 158 */     for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
/* 159 */       runIterationOnItem(paramLivingEntity.getItemBySlot(equipmentSlot), equipmentSlot, paramLivingEntity, paramEnchantmentInSlotVisitor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isImmuneToDamage(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, DamageSource paramDamageSource) {
/* 164 */     MutableBoolean mutableBoolean = new MutableBoolean();
/* 165 */     runIterationOnEquipment(paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> paramMutableBoolean.setValue((paramMutableBoolean.isTrue() || ((Enchantment)paramHolder.value()).isImmuneToDamage(paramServerLevel, paramInt, (Entity)paramLivingEntity, paramDamageSource))));
/* 166 */     return mutableBoolean.isTrue();
/*     */   }
/*     */   
/*     */   public static float getDamageProtection(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, DamageSource paramDamageSource) {
/* 170 */     MutableFloat mutableFloat = new MutableFloat(0.0F);
/* 171 */     runIterationOnEquipment(paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).modifyDamageProtection(paramServerLevel, paramInt, paramEnchantedItemInUse.itemStack(), (Entity)paramLivingEntity, paramDamageSource, paramMutableFloat));
/* 172 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyDamage(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity, DamageSource paramDamageSource, float paramFloat) {
/* 176 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 177 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyDamage(paramServerLevel, paramInt, paramItemStack, paramEntity, paramDamageSource, paramMutableFloat));
/* 178 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyFallBasedDamage(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity, DamageSource paramDamageSource, float paramFloat) {
/* 182 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 183 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyFallBasedDamage(paramServerLevel, paramInt, paramItemStack, paramEntity, paramDamageSource, paramMutableFloat));
/* 184 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyArmorEffectiveness(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity, DamageSource paramDamageSource, float paramFloat) {
/* 188 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 189 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyArmorEffectivness(paramServerLevel, paramInt, paramItemStack, paramEntity, paramDamageSource, paramMutableFloat));
/* 190 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyKnockback(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity, DamageSource paramDamageSource, float paramFloat) {
/* 194 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 195 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyKnockback(paramServerLevel, paramInt, paramItemStack, paramEntity, paramDamageSource, paramMutableFloat));
/* 196 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffects(ServerLevel paramServerLevel, Entity paramEntity, DamageSource paramDamageSource) {
/* 200 */     Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 201 */       doPostAttackEffectsWithItemSource(paramServerLevel, paramEntity, paramDamageSource, livingEntity.getWeaponItem()); }
/*     */     else
/* 203 */     { doPostAttackEffectsWithItemSource(paramServerLevel, paramEntity, paramDamageSource, null); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void doLungeEffects(ServerLevel paramServerLevel, Entity paramEntity) {
/* 208 */     if (paramEntity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)paramEntity;
/* 209 */       runIterationOnItem(paramEntity.getWeaponItem(), EquipmentSlot.MAINHAND, livingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).doLunge(paramServerLevel, paramInt, paramEnchantedItemInUse, paramEntity)); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffectsWithItemSource(ServerLevel paramServerLevel, Entity paramEntity, DamageSource paramDamageSource, ItemStack paramItemStack) {
/* 214 */     doPostAttackEffectsWithItemSourceOnBreak(paramServerLevel, paramEntity, paramDamageSource, paramItemStack, null);
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffectsWithItemSourceOnBreak(ServerLevel paramServerLevel, Entity paramEntity, DamageSource paramDamageSource, ItemStack paramItemStack, Consumer<Item> paramConsumer) {
/* 218 */     if (paramEntity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)paramEntity;
/* 219 */       runIterationOnEquipment(livingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).doPostAttack(paramServerLevel, paramInt, paramEnchantedItemInUse, EnchantmentTarget.VICTIM, paramEntity, paramDamageSource)); }
/*     */     
/* 221 */     if (paramItemStack != null) {
/* 222 */       Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 223 */         runIterationOnItem(paramItemStack, EquipmentSlot.MAINHAND, livingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).doPostAttack(paramServerLevel, paramInt, paramEnchantedItemInUse, EnchantmentTarget.ATTACKER, paramEntity, paramDamageSource)); }
/* 224 */       else if (paramConsumer != null)
/* 225 */       { EnchantedItemInUse enchantedItemInUse = new EnchantedItemInUse(paramItemStack, null, null, paramConsumer);
/* 226 */         runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).doPostAttack(paramServerLevel, paramInt, paramEnchantedItemInUse, EnchantmentTarget.ATTACKER, paramEntity, paramDamageSource)); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void runLocationChangedEffects(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 232 */     runIterationOnEquipment(paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).runLocationChangedEffects(paramServerLevel, paramInt, paramEnchantedItemInUse, paramLivingEntity));
/*     */   }
/*     */   
/*     */   public static void runLocationChangedEffects(ServerLevel paramServerLevel, ItemStack paramItemStack, LivingEntity paramLivingEntity, EquipmentSlot paramEquipmentSlot) {
/* 236 */     runIterationOnItem(paramItemStack, paramEquipmentSlot, paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).runLocationChangedEffects(paramServerLevel, paramInt, paramEnchantedItemInUse, paramLivingEntity));
/*     */   }
/*     */   
/*     */   public static void stopLocationBasedEffects(LivingEntity paramLivingEntity) {
/* 240 */     runIterationOnEquipment(paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).stopLocationBasedEffects(paramInt, paramEnchantedItemInUse, paramLivingEntity));
/*     */   }
/*     */   
/*     */   public static void stopLocationBasedEffects(ItemStack paramItemStack, LivingEntity paramLivingEntity, EquipmentSlot paramEquipmentSlot) {
/* 244 */     runIterationOnItem(paramItemStack, paramEquipmentSlot, paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).stopLocationBasedEffects(paramInt, paramEnchantedItemInUse, paramLivingEntity));
/*     */   }
/*     */   
/*     */   public static void tickEffects(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 248 */     runIterationOnEquipment(paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> ((Enchantment)paramHolder.value()).tick(paramServerLevel, paramInt, paramEnchantedItemInUse, (Entity)paramLivingEntity));
/*     */   }
/*     */   
/*     */   public static int getEnchantmentLevel(Holder<Enchantment> paramHolder, LivingEntity paramLivingEntity) {
/* 252 */     Collection<ItemStack> collection = ((Enchantment)paramHolder.value()).getSlotItems(paramLivingEntity).values();
/* 253 */     int i = 0;
/* 254 */     for (ItemStack itemStack : collection) {
/* 255 */       int j = getItemEnchantmentLevel(paramHolder, itemStack);
/* 256 */       if (j > i) {
/* 257 */         i = j;
/*     */       }
/*     */     } 
/* 260 */     return i;
/*     */   }
/*     */   
/*     */   public static int processProjectileCount(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity, int paramInt) {
/* 264 */     MutableFloat mutableFloat = new MutableFloat(paramInt);
/* 265 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyProjectileCount(paramServerLevel, paramInt, paramItemStack, paramEntity, paramMutableFloat));
/* 266 */     return Math.max(0, mutableFloat.intValue());
/*     */   }
/*     */   
/*     */   public static float processProjectileSpread(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity, float paramFloat) {
/* 270 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 271 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyProjectileSpread(paramServerLevel, paramInt, paramItemStack, paramEntity, paramMutableFloat));
/* 272 */     return Math.max(0.0F, mutableFloat.floatValue());
/*     */   }
/*     */   
/*     */   public static int getPiercingCount(ServerLevel paramServerLevel, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 276 */     MutableFloat mutableFloat = new MutableFloat(0.0F);
/* 277 */     runIterationOnItem(paramItemStack1, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyPiercingCount(paramServerLevel, paramInt, paramItemStack, paramMutableFloat));
/* 278 */     return Math.max(0, mutableFloat.intValue());
/*     */   }
/*     */   
/*     */   public static void onProjectileSpawned(ServerLevel paramServerLevel, ItemStack paramItemStack, Projectile paramProjectile, Consumer<Item> paramConsumer) {
/* 282 */     Entity entity = paramProjectile.getOwner(); LivingEntity livingEntity2 = (LivingEntity)entity, livingEntity1 = (entity instanceof LivingEntity) ? livingEntity2 : null;
/* 283 */     EnchantedItemInUse enchantedItemInUse = new EnchantedItemInUse(paramItemStack, null, livingEntity1, paramConsumer);
/* 284 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).onProjectileSpawned(paramServerLevel, paramInt, paramEnchantedItemInUse, (Entity)paramProjectile));
/*     */   }
/*     */   
/*     */   public static void onHitBlock(ServerLevel paramServerLevel, ItemStack paramItemStack, LivingEntity paramLivingEntity, Entity paramEntity, EquipmentSlot paramEquipmentSlot, Vec3 paramVec3, BlockState paramBlockState, Consumer<Item> paramConsumer) {
/* 288 */     EnchantedItemInUse enchantedItemInUse = new EnchantedItemInUse(paramItemStack, paramEquipmentSlot, paramLivingEntity, paramConsumer);
/* 289 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).onHitBlock(paramServerLevel, paramInt, paramEnchantedItemInUse, paramEntity, paramVec3, paramBlockState));
/*     */   }
/*     */   
/*     */   public static int modifyDurabilityToRepairFromXp(ServerLevel paramServerLevel, ItemStack paramItemStack, int paramInt) {
/* 293 */     MutableFloat mutableFloat = new MutableFloat(paramInt);
/* 294 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyDurabilityToRepairFromXp(paramServerLevel, paramInt, paramItemStack, paramMutableFloat));
/* 295 */     return Math.max(0, mutableFloat.intValue());
/*     */   }
/*     */   
/*     */   public static float processEquipmentDropChance(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, DamageSource paramDamageSource, float paramFloat) {
/* 299 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 300 */     RandomSource randomSource = paramLivingEntity.getRandom();
/* 301 */     runIterationOnEquipment(paramLivingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> {
/*     */           LootContext lootContext = Enchantment.damageContext(paramServerLevel, paramInt, (Entity)paramLivingEntity, paramDamageSource);
/*     */ 
/*     */ 
/*     */           
/*     */           ((Enchantment)paramHolder.value()).<TargetedConditionalEffect<EnchantmentValueEffect>>getEffects(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach(());
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 311 */     Entity entity = paramDamageSource.getEntity();
/* 312 */     if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 313 */       runIterationOnEquipment(livingEntity, (paramHolder, paramInt, paramEnchantedItemInUse) -> {
/*     */             LootContext lootContext = Enchantment.damageContext(paramServerLevel, paramInt, (Entity)paramLivingEntity, paramDamageSource);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             ((Enchantment)paramHolder.value()).<TargetedConditionalEffect<EnchantmentValueEffect>>getEffects(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach(());
/*     */           }); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static void forEachModifier(ItemStack paramItemStack, EquipmentSlotGroup paramEquipmentSlotGroup, BiConsumer<Holder<Attribute>, AttributeModifier> paramBiConsumer) {
/* 329 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).<EnchantmentAttributeEffect>getEffects(EnchantmentEffectComponents.ATTRIBUTES).forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void forEachModifier(ItemStack paramItemStack, EquipmentSlot paramEquipmentSlot, BiConsumer<Holder<Attribute>, AttributeModifier> paramBiConsumer) {
/* 342 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).<EnchantmentAttributeEffect>getEffects(EnchantmentEffectComponents.ATTRIBUTES).forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFishingLuckBonus(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity) {
/* 355 */     MutableFloat mutableFloat = new MutableFloat(0.0F);
/* 356 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyFishingLuckBonus(paramServerLevel, paramInt, paramItemStack, paramEntity, paramMutableFloat));
/* 357 */     return Math.max(0, mutableFloat.intValue());
/*     */   }
/*     */   
/*     */   public static float getFishingTimeReduction(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity) {
/* 361 */     MutableFloat mutableFloat = new MutableFloat(0.0F);
/* 362 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyFishingTimeReduction(paramServerLevel, paramInt, paramItemStack, paramEntity, paramMutableFloat));
/* 363 */     return Math.max(0.0F, mutableFloat.floatValue());
/*     */   }
/*     */   
/*     */   public static int getTridentReturnToOwnerAcceleration(ServerLevel paramServerLevel, ItemStack paramItemStack, Entity paramEntity) {
/* 367 */     MutableFloat mutableFloat = new MutableFloat(0.0F);
/* 368 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyTridentReturnToOwnerAcceleration(paramServerLevel, paramInt, paramItemStack, paramEntity, paramMutableFloat));
/* 369 */     return Math.max(0, mutableFloat.intValue());
/*     */   }
/*     */   
/*     */   public static float modifyCrossbowChargingTime(ItemStack paramItemStack, LivingEntity paramLivingEntity, float paramFloat) {
/* 373 */     MutableFloat mutableFloat = new MutableFloat(paramFloat);
/* 374 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyCrossbowChargeTime(paramLivingEntity.getRandom(), paramInt, paramMutableFloat));
/* 375 */     return Math.max(0.0F, mutableFloat.floatValue());
/*     */   }
/*     */   
/*     */   public static float getTridentSpinAttackStrength(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 379 */     MutableFloat mutableFloat = new MutableFloat(0.0F);
/* 380 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> ((Enchantment)paramHolder.value()).modifyTridentSpinAttackStrength(paramLivingEntity.getRandom(), paramInt, paramMutableFloat));
/* 381 */     return mutableFloat.floatValue();
/*     */   }
/*     */   
/*     */   public static boolean hasTag(ItemStack paramItemStack, TagKey<Enchantment> paramTagKey) {
/* 385 */     ItemEnchantments itemEnchantments = (ItemEnchantments)paramItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 386 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 387 */       Holder holder = (Holder)entry.getKey();
/* 388 */       if (holder.is(paramTagKey)) {
/* 389 */         return true;
/*     */       }
/*     */     } 
/* 392 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean has(ItemStack paramItemStack, DataComponentType<?> paramDataComponentType) {
/* 396 */     MutableBoolean mutableBoolean = new MutableBoolean(false);
/* 397 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> {
/*     */           if (((Enchantment)paramHolder.value()).effects().has(paramDataComponentType)) {
/*     */             paramMutableBoolean.setTrue();
/*     */           }
/*     */         });
/* 402 */     return mutableBoolean.booleanValue();
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> pickHighestLevel(ItemStack paramItemStack, DataComponentType<List<T>> paramDataComponentType) {
/* 406 */     Pair<List<T>, Integer> pair = getHighestLevel(paramItemStack, paramDataComponentType);
/* 407 */     if (pair != null) {
/* 408 */       List<T> list = (List)pair.getFirst();
/* 409 */       int i = ((Integer)pair.getSecond()).intValue();
/* 410 */       return Optional.of(list.get(Math.min(i, list.size()) - 1));
/*     */     } 
/* 412 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static <T> Pair<T, Integer> getHighestLevel(ItemStack paramItemStack, DataComponentType<T> paramDataComponentType) {
/* 416 */     MutableObject mutableObject = new MutableObject();
/* 417 */     runIterationOnItem(paramItemStack, (paramHolder, paramInt) -> {
/*     */           if (paramMutableObject.get() == null || ((Integer)((Pair)paramMutableObject.get()).getSecond()).intValue() < paramInt) {
/*     */             Object object = ((Enchantment)paramHolder.value()).effects().get(paramDataComponentType);
/*     */             if (object != null) {
/*     */               paramMutableObject.setValue(Pair.of(object, Integer.valueOf(paramInt)));
/*     */             }
/*     */           } 
/*     */         });
/* 425 */     return (Pair<T, Integer>)mutableObject.get();
/*     */   }
/*     */   
/*     */   public static Optional<EnchantedItemInUse> getRandomItemWith(DataComponentType<?> paramDataComponentType, LivingEntity paramLivingEntity, Predicate<ItemStack> paramPredicate) {
/* 429 */     ArrayList<EnchantedItemInUse> arrayList = new ArrayList();
/* 430 */     for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
/* 431 */       ItemStack itemStack = paramLivingEntity.getItemBySlot(equipmentSlot);
/* 432 */       if (!paramPredicate.test(itemStack)) {
/*     */         continue;
/*     */       }
/* 435 */       ItemEnchantments itemEnchantments = (ItemEnchantments)itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 436 */       for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 437 */         Holder holder = (Holder)entry.getKey();
/* 438 */         if (((Enchantment)holder.value()).effects().has(paramDataComponentType) && ((Enchantment)holder.value()).matchingSlot(equipmentSlot)) {
/* 439 */           arrayList.add(new EnchantedItemInUse(itemStack, equipmentSlot, paramLivingEntity));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 444 */     return Util.getRandomSafe(arrayList, paramLivingEntity.getRandom());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getEnchantmentCost(RandomSource paramRandomSource, int paramInt1, int paramInt2, ItemStack paramItemStack) {
/* 455 */     Enchantable enchantable = (Enchantable)paramItemStack.get(DataComponents.ENCHANTABLE);
/* 456 */     if (enchantable == null)
/*     */     {
/* 458 */       return 0;
/*     */     }
/*     */     
/* 461 */     if (paramInt2 > 15) {
/* 462 */       paramInt2 = 15;
/*     */     }
/* 464 */     int i = paramRandomSource.nextInt(8) + 1 + (paramInt2 >> 1) + paramRandomSource.nextInt(paramInt2 + 1);
/* 465 */     if (paramInt1 == 0) {
/* 466 */       return Math.max(i / 3, 1);
/*     */     }
/* 468 */     if (paramInt1 == 1) {
/* 469 */       return i * 2 / 3 + 1;
/*     */     }
/* 471 */     return Math.max(i, paramInt2 * 2);
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(RandomSource paramRandomSource, ItemStack paramItemStack, int paramInt, RegistryAccess paramRegistryAccess, Optional<? extends HolderSet<Enchantment>> paramOptional) {
/* 475 */     return enchantItem(paramRandomSource, paramItemStack, paramInt, paramOptional.<Stream<Holder<Enchantment>>>map(HolderSet::stream).orElseGet(() -> paramRegistryAccess.lookupOrThrow(Registries.ENCHANTMENT).listElements().map(())));
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(RandomSource paramRandomSource, ItemStack paramItemStack, int paramInt, Stream<Holder<Enchantment>> paramStream) {
/* 479 */     List<EnchantmentInstance> list = selectEnchantment(paramRandomSource, paramItemStack, paramInt, paramStream);
/* 480 */     if (paramItemStack.is(Items.BOOK)) {
/* 481 */       paramItemStack = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/*     */     }
/*     */     
/* 484 */     for (EnchantmentInstance enchantmentInstance : list) {
/* 485 */       paramItemStack.enchant(enchantmentInstance.enchantment(), enchantmentInstance.level());
/*     */     }
/*     */     
/* 488 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> selectEnchantment(RandomSource paramRandomSource, ItemStack paramItemStack, int paramInt, Stream<Holder<Enchantment>> paramStream) {
/* 492 */     ArrayList<EnchantmentInstance> arrayList = Lists.newArrayList();
/*     */ 
/*     */     
/* 495 */     Enchantable enchantable = (Enchantable)paramItemStack.get(DataComponents.ENCHANTABLE);
/* 496 */     if (enchantable == null) {
/* 497 */       return arrayList;
/*     */     }
/*     */     
/* 500 */     paramInt += 1 + paramRandomSource.nextInt(enchantable.value() / 4 + 1) + paramRandomSource.nextInt(enchantable.value() / 4 + 1);
/*     */ 
/*     */     
/* 503 */     float f = (paramRandomSource.nextFloat() + paramRandomSource.nextFloat() - 1.0F) * 0.15F;
/* 504 */     paramInt = Mth.clamp(Math.round(paramInt + paramInt * f), 1, 2147483647);
/*     */     
/* 506 */     List<EnchantmentInstance> list = getAvailableEnchantmentResults(paramInt, paramItemStack, paramStream);
/* 507 */     if (!list.isEmpty()) {
/* 508 */       Objects.requireNonNull(arrayList); WeightedRandom.getRandomItem(paramRandomSource, list, EnchantmentInstance::weight).ifPresent(arrayList::add);
/*     */       
/* 510 */       while (paramRandomSource.nextInt(50) <= paramInt) {
/* 511 */         if (!arrayList.isEmpty()) {
/* 512 */           filterCompatibleEnchantments(list, arrayList.getLast());
/*     */         }
/*     */         
/* 515 */         if (list.isEmpty()) {
/*     */           break;
/*     */         }
/*     */         
/* 519 */         Objects.requireNonNull(arrayList); WeightedRandom.getRandomItem(paramRandomSource, list, EnchantmentInstance::weight).ifPresent(arrayList::add);
/* 520 */         paramInt /= 2;
/*     */       } 
/*     */     } 
/* 523 */     return arrayList;
/*     */   }
/*     */   
/*     */   public static void filterCompatibleEnchantments(List<EnchantmentInstance> paramList, EnchantmentInstance paramEnchantmentInstance) {
/* 527 */     paramList.removeIf(paramEnchantmentInstance2 -> !Enchantment.areCompatible(paramEnchantmentInstance1.enchantment(), paramEnchantmentInstance2.enchantment()));
/*     */   }
/*     */   
/*     */   public static boolean isEnchantmentCompatible(Collection<Holder<Enchantment>> paramCollection, Holder<Enchantment> paramHolder) {
/* 531 */     for (Holder<Enchantment> holder : paramCollection) {
/* 532 */       if (!Enchantment.areCompatible(holder, paramHolder)) {
/* 533 */         return false;
/*     */       }
/*     */     } 
/* 536 */     return true;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> getAvailableEnchantmentResults(int paramInt, ItemStack paramItemStack, Stream<Holder<Enchantment>> paramStream) {
/* 540 */     ArrayList<EnchantmentInstance> arrayList = Lists.newArrayList();
/*     */     
/* 542 */     boolean bool = paramItemStack.is(Items.BOOK);
/* 543 */     paramStream
/*     */       
/* 545 */       .filter(paramHolder -> (((Enchantment)paramHolder.value()).isPrimaryItem(paramItemStack) || paramBoolean))
/* 546 */       .forEach(paramHolder -> {
/*     */           Enchantment enchantment = (Enchantment)paramHolder.value();
/*     */           
/*     */           for (int i = enchantment.getMaxLevel(); i >= enchantment.getMinLevel(); i--) {
/*     */             if (paramInt >= enchantment.getMinCost(i) && paramInt <= enchantment.getMaxCost(i)) {
/*     */               paramList.add(new EnchantmentInstance(paramHolder, i));
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         });
/* 556 */     return arrayList;
/*     */   }
/*     */   
/*     */   public static void enchantItemFromProvider(ItemStack paramItemStack, RegistryAccess paramRegistryAccess, ResourceKey<EnchantmentProvider> paramResourceKey, DifficultyInstance paramDifficultyInstance, RandomSource paramRandomSource) {
/* 560 */     EnchantmentProvider enchantmentProvider = (EnchantmentProvider)paramRegistryAccess.lookupOrThrow(Registries.ENCHANTMENT_PROVIDER).getValue(paramResourceKey);
/* 561 */     if (enchantmentProvider != null)
/* 562 */       updateEnchantments(paramItemStack, paramMutable -> paramEnchantmentProvider.enchant(paramItemStack, paramMutable, paramRandomSource, paramDifficultyInstance)); 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EnchantmentVisitor {
/*     */     void accept(Holder<Enchantment> param1Holder, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EnchantmentInSlotVisitor {
/*     */     void accept(Holder<Enchantment> param1Holder, int param1Int, EnchantedItemInUse param1EnchantedItemInUse);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */