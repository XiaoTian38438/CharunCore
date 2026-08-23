/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TridentItem
/*     */   extends Item
/*     */   implements ProjectileItem {
/*     */   public static final int THROW_THRESHOLD_TIME = 10;
/*     */   public static final float BASE_DAMAGE = 8.0F;
/*     */   public static final float PROJECTILE_SHOOT_POWER = 2.5F;
/*     */   
/*     */   public TridentItem(Item.Properties paramProperties) {
/*  40 */     super(paramProperties);
/*     */   }
/*     */   
/*     */   public static ItemAttributeModifiers createAttributes() {
/*  44 */     return ItemAttributeModifiers.builder()
/*  45 */       .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  46 */       .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9000000953674316D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  47 */       .build();
/*     */   }
/*     */   
/*     */   public static Tool createToolProperties() {
/*  51 */     return new Tool(List.of(), 1.0F, 2, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack paramItemStack) {
/*  56 */     return ItemUseAnimation.TRIDENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/*  61 */     return 72000;
/*     */   }
/*     */   
/*     */   public boolean releaseUsing(ItemStack paramItemStack, Level paramLevel, LivingEntity paramLivingEntity, int paramInt) {
/*     */     Player player;
/*  66 */     if (paramLivingEntity instanceof Player) { player = (Player)paramLivingEntity; }
/*  67 */     else { return false; }
/*     */ 
/*     */     
/*  70 */     int i = getUseDuration(paramItemStack, paramLivingEntity) - paramInt;
/*  71 */     if (i < 10) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     float f = EnchantmentHelper.getTridentSpinAttackStrength(paramItemStack, (LivingEntity)player);
/*  76 */     if (f > 0.0F && !player.isInWaterOrRain()) {
/*  77 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (paramItemStack.nextDamageWillBreak()) {
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     Holder holder = EnchantmentHelper.pickHighestLevel(paramItemStack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
/*     */     
/*  87 */     player.awardStat(Stats.ITEM_USED.get(this));
/*     */     
/*  89 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  90 */       paramItemStack.hurtWithoutBreaking(1, player);
/*     */       
/*  92 */       if (f == 0.0F) {
/*  93 */         ItemStack itemStack = paramItemStack.consumeAndReturn(1, (LivingEntity)player);
/*  94 */         ThrownTrident thrownTrident = (ThrownTrident)Projectile.spawnProjectileFromRotation(ThrownTrident::new, serverLevel, itemStack, (LivingEntity)player, 0.0F, 2.5F, 1.0F);
/*     */         
/*  96 */         if (player.hasInfiniteMaterials()) {
/*  97 */           thrownTrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
/*     */         }
/*     */         
/* 100 */         paramLevel.playSound(null, (Entity)thrownTrident, (SoundEvent)holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
/* 101 */         return true;
/*     */       }  }
/*     */ 
/*     */     
/* 105 */     if (f > 0.0F) {
/* 106 */       float f1 = player.getYRot();
/* 107 */       float f2 = player.getXRot();
/*     */ 
/*     */       
/* 110 */       float f3 = -Mth.sin((f1 * 0.017453292F)) * Mth.cos((f2 * 0.017453292F));
/* 111 */       float f4 = -Mth.sin((f2 * 0.017453292F));
/* 112 */       float f5 = Mth.cos((f1 * 0.017453292F)) * Mth.cos((f2 * 0.017453292F));
/* 113 */       float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
/* 114 */       f3 *= f / f6;
/* 115 */       f4 *= f / f6;
/* 116 */       f5 *= f / f6;
/* 117 */       player.push(f3, f4, f5);
/*     */       
/* 119 */       player.startAutoSpinAttack(20, 8.0F, paramItemStack);
/* 120 */       if (player.onGround()) {
/* 121 */         float f7 = 1.1999999F;
/* 122 */         player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999284744263D, 0.0D));
/*     */       } 
/*     */       
/* 125 */       paramLevel.playSound(null, (Entity)player, (SoundEvent)holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
/* 126 */       return true;
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 134 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 135 */     if (itemStack.nextDamageWillBreak())
/*     */     {
/* 137 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/* 139 */     if (EnchantmentHelper.getTridentSpinAttackStrength(itemStack, (LivingEntity)paramPlayer) > 0.0F && !paramPlayer.isInWaterOrRain())
/*     */     {
/* 141 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/* 143 */     paramPlayer.startUsingItem(paramInteractionHand);
/* 144 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 149 */     ThrownTrident thrownTrident = new ThrownTrident(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), paramItemStack.copyWithCount(1));
/* 150 */     thrownTrident.pickup = AbstractArrow.Pickup.ALLOWED;
/*     */     
/* 152 */     return (Projectile)thrownTrident;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\TridentItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */