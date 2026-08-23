/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MaceItem
/*     */   extends Item
/*     */ {
/*     */   private static final int DEFAULT_ATTACK_DAMAGE = 5;
/*     */   private static final float DEFAULT_ATTACK_SPEED = -3.4F;
/*     */   public static final float SMASH_ATTACK_FALL_THRESHOLD = 1.5F;
/*     */   private static final float SMASH_ATTACK_HEAVY_THRESHOLD = 5.0F;
/*     */   public static final float SMASH_ATTACK_KNOCKBACK_RADIUS = 3.5F;
/*     */   private static final float SMASH_ATTACK_KNOCKBACK_POWER = 0.7F;
/*     */   
/*     */   public MaceItem(Item.Properties paramProperties) {
/*  40 */     super(paramProperties);
/*     */   }
/*     */   
/*     */   public static ItemAttributeModifiers createAttributes() {
/*  44 */     return ItemAttributeModifiers.builder()
/*  45 */       .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 5.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  46 */       .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.4000000953674316D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/*  47 */       .build();
/*     */   }
/*     */   
/*     */   public static Tool createToolProperties() {
/*  51 */     return new Tool(List.of(), 1.0F, 2, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void hurtEnemy(ItemStack paramItemStack, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  56 */     if (canSmashAttack(paramLivingEntity2)) {
/*  57 */       ServerLevel serverLevel = (ServerLevel)paramLivingEntity2.level();
/*     */       
/*  59 */       paramLivingEntity2.setDeltaMovement(paramLivingEntity2.getDeltaMovement().with(Direction.Axis.Y, 0.009999999776482582D));
/*  60 */       if (paramLivingEntity2 instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramLivingEntity2;
/*  61 */         serverPlayer.currentImpulseImpactPos = calculateImpactPosition(serverPlayer);
/*  62 */         serverPlayer.setIgnoreFallDamageFromCurrentImpulse(true);
/*  63 */         serverPlayer.connection.send((Packet)new ClientboundSetEntityMotionPacket((Entity)serverPlayer)); }
/*     */ 
/*     */       
/*  66 */       if (paramLivingEntity1.onGround()) {
/*  67 */         if (paramLivingEntity2 instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramLivingEntity2;
/*  68 */           serverPlayer.setSpawnExtraParticlesOnFall(true); }
/*     */         
/*  70 */         SoundEvent soundEvent = (paramLivingEntity2.fallDistance > 5.0D) ? SoundEvents.MACE_SMASH_GROUND_HEAVY : SoundEvents.MACE_SMASH_GROUND;
/*  71 */         serverLevel.playSound(null, paramLivingEntity2.getX(), paramLivingEntity2.getY(), paramLivingEntity2.getZ(), soundEvent, paramLivingEntity2.getSoundSource(), 1.0F, 1.0F);
/*     */       } else {
/*  73 */         serverLevel.playSound(null, paramLivingEntity2.getX(), paramLivingEntity2.getY(), paramLivingEntity2.getZ(), SoundEvents.MACE_SMASH_AIR, paramLivingEntity2.getSoundSource(), 1.0F, 1.0F);
/*     */       } 
/*     */       
/*  76 */       knockback((Level)serverLevel, (Entity)paramLivingEntity2, (Entity)paramLivingEntity1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3 calculateImpactPosition(ServerPlayer paramServerPlayer) {
/*  81 */     if (paramServerPlayer.isIgnoringFallDamageFromCurrentImpulse() && paramServerPlayer.currentImpulseImpactPos != null && paramServerPlayer.currentImpulseImpactPos.y <= 
/*     */       
/*  83 */       (paramServerPlayer.position()).y)
/*     */     {
/*  85 */       return paramServerPlayer.currentImpulseImpactPos;
/*     */     }
/*  87 */     return paramServerPlayer.position();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postHurtEnemy(ItemStack paramItemStack, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  94 */     if (canSmashAttack(paramLivingEntity2)) {
/*  95 */       paramLivingEntity2.resetFallDistance();
/*     */     }
/*     */   }
/*     */   
/*     */   public float getAttackDamageBonus(Entity paramEntity, float paramFloat, DamageSource paramDamageSource) {
/*     */     LivingEntity livingEntity;
/*     */     double d4;
/* 102 */     Entity entity = paramDamageSource.getDirectEntity(); if (entity instanceof LivingEntity) { livingEntity = (LivingEntity)entity; }
/* 103 */     else { return 0.0F; }
/*     */     
/* 105 */     if (!canSmashAttack(livingEntity)) {
/* 106 */       return 0.0F;
/*     */     }
/*     */     
/* 109 */     double d1 = 3.0D;
/* 110 */     double d2 = 8.0D;
/*     */     
/* 112 */     double d3 = livingEntity.fallDistance;
/*     */ 
/*     */     
/* 115 */     if (d3 <= 3.0D) {
/* 116 */       d4 = 4.0D * d3;
/* 117 */     } else if (d3 <= 8.0D) {
/* 118 */       d4 = 12.0D + 2.0D * (d3 - 3.0D);
/*     */     } else {
/* 120 */       d4 = 22.0D + d3 - 8.0D;
/*     */     } 
/* 122 */     Level level = livingEntity.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 123 */       return (float)(d4 + EnchantmentHelper.modifyFallBasedDamage(serverLevel, livingEntity.getWeaponItem(), paramEntity, paramDamageSource, 0.0F) * d3); }
/*     */     
/* 125 */     return (float)d4;
/*     */   }
/*     */   
/*     */   private static void knockback(Level paramLevel, Entity paramEntity1, Entity paramEntity2) {
/* 129 */     paramLevel.levelEvent(2013, paramEntity2.getOnPos(), 750);
/*     */     
/* 131 */     paramLevel.getEntitiesOfClass(LivingEntity.class, paramEntity2.getBoundingBox().inflate(3.5D), knockbackPredicate(paramEntity1, paramEntity2))
/* 132 */       .forEach(paramLivingEntity -> {
/*     */           Vec3 vec31 = paramLivingEntity.position().subtract(paramEntity1.position());
/*     */           double d = getKnockbackPower(paramEntity2, paramLivingEntity, vec31);
/*     */           Vec3 vec32 = vec31.normalize().scale(d);
/*     */           if (d > 0.0D) {
/*     */             paramLivingEntity.push(vec32.x, 0.699999988079071D, vec32.z);
/*     */             if (paramLivingEntity instanceof ServerPlayer) {
/*     */               ServerPlayer serverPlayer = (ServerPlayer)paramLivingEntity;
/*     */               serverPlayer.connection.send((Packet)new ClientboundSetEntityMotionPacket((Entity)serverPlayer));
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static Predicate<LivingEntity> knockbackPredicate(Entity paramEntity1, Entity paramEntity2) {
/* 147 */     return paramLivingEntity -> {
/*     */         // Byte code:
/*     */         //   0: aload_2
/*     */         //   1: invokevirtual isSpectator : ()Z
/*     */         //   4: ifne -> 11
/*     */         //   7: iconst_1
/*     */         //   8: goto -> 12
/*     */         //   11: iconst_0
/*     */         //   12: istore_3
/*     */         //   13: aload_2
/*     */         //   14: aload_0
/*     */         //   15: if_acmpeq -> 27
/*     */         //   18: aload_2
/*     */         //   19: aload_1
/*     */         //   20: if_acmpeq -> 27
/*     */         //   23: iconst_1
/*     */         //   24: goto -> 28
/*     */         //   27: iconst_0
/*     */         //   28: istore #4
/*     */         //   30: aload_0
/*     */         //   31: aload_2
/*     */         //   32: invokevirtual isAlliedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*     */         //   35: ifne -> 42
/*     */         //   38: iconst_1
/*     */         //   39: goto -> 43
/*     */         //   42: iconst_0
/*     */         //   43: istore #5
/*     */         //   45: aload_2
/*     */         //   46: instanceof net/minecraft/world/entity/TamableAnimal
/*     */         //   49: ifeq -> 93
/*     */         //   52: aload_2
/*     */         //   53: checkcast net/minecraft/world/entity/TamableAnimal
/*     */         //   56: astore #8
/*     */         //   58: aload_1
/*     */         //   59: instanceof net/minecraft/world/entity/LivingEntity
/*     */         //   62: ifeq -> 93
/*     */         //   65: aload_1
/*     */         //   66: checkcast net/minecraft/world/entity/LivingEntity
/*     */         //   69: astore #7
/*     */         //   71: aload #8
/*     */         //   73: invokevirtual isTame : ()Z
/*     */         //   76: ifeq -> 93
/*     */         //   79: aload #8
/*     */         //   81: aload #7
/*     */         //   83: invokevirtual isOwnedBy : (Lnet/minecraft/world/entity/LivingEntity;)Z
/*     */         //   86: ifeq -> 93
/*     */         //   89: iconst_1
/*     */         //   90: goto -> 94
/*     */         //   93: iconst_0
/*     */         //   94: ifne -> 101
/*     */         //   97: iconst_1
/*     */         //   98: goto -> 102
/*     */         //   101: iconst_0
/*     */         //   102: istore #6
/*     */         //   104: aload_2
/*     */         //   105: instanceof net/minecraft/world/entity/decoration/ArmorStand
/*     */         //   108: ifeq -> 125
/*     */         //   111: aload_2
/*     */         //   112: checkcast net/minecraft/world/entity/decoration/ArmorStand
/*     */         //   115: astore #8
/*     */         //   117: aload #8
/*     */         //   119: invokevirtual isMarker : ()Z
/*     */         //   122: ifne -> 129
/*     */         //   125: iconst_1
/*     */         //   126: goto -> 130
/*     */         //   129: iconst_0
/*     */         //   130: istore #7
/*     */         //   132: aload_1
/*     */         //   133: aload_2
/*     */         //   134: invokevirtual distanceToSqr : (Lnet/minecraft/world/entity/Entity;)D
/*     */         //   137: ldc2_w 3.5
/*     */         //   140: ldc2_w 2.0
/*     */         //   143: invokestatic pow : (DD)D
/*     */         //   146: dcmpg
/*     */         //   147: ifgt -> 154
/*     */         //   150: iconst_1
/*     */         //   151: goto -> 155
/*     */         //   154: iconst_0
/*     */         //   155: istore #8
/*     */         //   157: aload_2
/*     */         //   158: instanceof net/minecraft/world/entity/player/Player
/*     */         //   161: ifeq -> 193
/*     */         //   164: aload_2
/*     */         //   165: checkcast net/minecraft/world/entity/player/Player
/*     */         //   168: astore #10
/*     */         //   170: aload #10
/*     */         //   172: invokevirtual isCreative : ()Z
/*     */         //   175: ifeq -> 193
/*     */         //   178: aload #10
/*     */         //   180: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */         //   183: getfield flying : Z
/*     */         //   186: ifeq -> 193
/*     */         //   189: iconst_1
/*     */         //   190: goto -> 194
/*     */         //   193: iconst_0
/*     */         //   194: ifne -> 201
/*     */         //   197: iconst_1
/*     */         //   198: goto -> 202
/*     */         //   201: iconst_0
/*     */         //   202: istore #9
/*     */         //   204: iload_3
/*     */         //   205: ifeq -> 242
/*     */         //   208: iload #4
/*     */         //   210: ifeq -> 242
/*     */         //   213: iload #5
/*     */         //   215: ifeq -> 242
/*     */         //   218: iload #6
/*     */         //   220: ifeq -> 242
/*     */         //   223: iload #7
/*     */         //   225: ifeq -> 242
/*     */         //   228: iload #8
/*     */         //   230: ifeq -> 242
/*     */         //   233: iload #9
/*     */         //   235: ifeq -> 242
/*     */         //   238: iconst_1
/*     */         //   239: goto -> 243
/*     */         //   242: iconst_0
/*     */         //   243: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #148	-> 0
/*     */         //   #149	-> 13
/*     */         //   #150	-> 30
/*     */         //   #151	-> 45
/*     */         //   #152	-> 104
/*     */         //   #153	-> 132
/*     */         //   #154	-> 157
/*     */         //   #156	-> 204
/*     */       };
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
/*     */   private static double getKnockbackPower(Entity paramEntity, LivingEntity paramLivingEntity, Vec3 paramVec3) {
/* 161 */     return (3.5D - paramVec3.length()) * 0.699999988079071D * ((paramEntity.fallDistance > 5.0D) ? 2 : true) * (1.0D - paramLivingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
/*     */   }
/*     */   
/*     */   public static boolean canSmashAttack(LivingEntity paramLivingEntity) {
/* 165 */     return (paramLivingEntity.fallDistance > 1.5D && !paramLivingEntity.isFallFlying());
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource getItemDamageSource(LivingEntity paramLivingEntity) {
/* 170 */     if (canSmashAttack(paramLivingEntity)) {
/* 171 */       return paramLivingEntity.damageSources().mace((Entity)paramLivingEntity);
/*     */     }
/* 173 */     return super.getItemDamageSource(paramLivingEntity);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\MaceItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */