/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class DamageSource
/*     */ {
/*     */   private final Holder<DamageType> type;
/*     */   private final Entity causingEntity;
/*     */   private final Entity directEntity;
/*     */   private final Vec3 damageSourcePosition;
/*     */   
/*     */   public String toString() {
/*  23 */     return "DamageSource (" + type().msgId() + ")";
/*     */   }
/*     */   
/*     */   public float getFoodExhaustion() {
/*  27 */     return type().exhaustion();
/*     */   }
/*     */   
/*     */   public boolean isDirect() {
/*  31 */     return (this.causingEntity == this.directEntity);
/*     */   }
/*     */   
/*     */   private DamageSource(Holder<DamageType> paramHolder, Entity paramEntity1, Entity paramEntity2, Vec3 paramVec3) {
/*  35 */     this.type = paramHolder;
/*  36 */     this.causingEntity = paramEntity2;
/*  37 */     this.directEntity = paramEntity1;
/*  38 */     this.damageSourcePosition = paramVec3;
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> paramHolder, Entity paramEntity1, Entity paramEntity2) {
/*  42 */     this(paramHolder, paramEntity1, paramEntity2, null);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> paramHolder, Vec3 paramVec3) {
/*  46 */     this(paramHolder, null, null, paramVec3);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> paramHolder, Entity paramEntity) {
/*  50 */     this(paramHolder, paramEntity, paramEntity);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> paramHolder) {
/*  54 */     this(paramHolder, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getDirectEntity() {
/*  62 */     return this.directEntity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/*  70 */     return this.causingEntity;
/*     */   }
/*     */   
/*     */   public ItemStack getWeaponItem() {
/*  74 */     return (this.directEntity != null) ? this.directEntity.getWeaponItem() : null;
/*     */   }
/*     */   
/*     */   public Component getLocalizedDeathMessage(LivingEntity paramLivingEntity) {
/*  78 */     String str1 = "death.attack." + type().msgId();
/*  79 */     if (this.causingEntity != null || this.directEntity != null) {
/*  80 */       Component component = (this.causingEntity == null) ? this.directEntity.getDisplayName() : this.causingEntity.getDisplayName();
/*  81 */       Entity entity = this.causingEntity; LivingEntity livingEntity1 = (LivingEntity)entity; ItemStack itemStack = (entity instanceof LivingEntity) ? livingEntity1.getMainHandItem() : ItemStack.EMPTY;
/*     */       
/*  83 */       if (!itemStack.isEmpty() && itemStack.has(DataComponents.CUSTOM_NAME)) {
/*  84 */         return (Component)Component.translatable(str1 + ".item", new Object[] { paramLivingEntity.getDisplayName(), component, itemStack.getDisplayName() });
/*     */       }
/*  86 */       return (Component)Component.translatable(str1, new Object[] { paramLivingEntity.getDisplayName(), component });
/*     */     } 
/*     */ 
/*     */     
/*  90 */     LivingEntity livingEntity = paramLivingEntity.getKillCredit();
/*  91 */     String str2 = str1 + ".player";
/*  92 */     if (livingEntity != null) {
/*  93 */       return (Component)Component.translatable(str2, new Object[] { paramLivingEntity.getDisplayName(), livingEntity.getDisplayName() });
/*     */     }
/*  95 */     return (Component)Component.translatable(str1, new Object[] { paramLivingEntity.getDisplayName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMsgId() {
/* 100 */     return type().msgId();
/*     */   }
/*     */   
/*     */   public boolean scalesWithDifficulty() {
/* 104 */     switch (type().scaling()) { default: throw new MatchException(null, null);case NEVER: case WHEN_CAUSED_BY_LIVING_NON_PLAYER: return 
/*     */           
/* 106 */           (this.causingEntity instanceof LivingEntity && !(this.causingEntity instanceof Player));
/*     */       case ALWAYS:
/*     */         break; }
/*     */     
/*     */     return true;
/*     */   } public boolean isCreativePlayer() {
/* 112 */     Entity entity = getEntity(); if (entity instanceof Player) { Player player = (Player)entity; if ((player.getAbilities()).instabuild); }  return false;
/*     */   }
/*     */   
/*     */   public Vec3 getSourcePosition() {
/* 116 */     if (this.damageSourcePosition != null)
/* 117 */       return this.damageSourcePosition; 
/* 118 */     if (this.directEntity != null) {
/* 119 */       return this.directEntity.position();
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3 sourcePositionRaw() {
/* 125 */     return this.damageSourcePosition;
/*     */   }
/*     */   
/*     */   public boolean is(TagKey<DamageType> paramTagKey) {
/* 129 */     return this.type.is(paramTagKey);
/*     */   }
/*     */   
/*     */   public boolean is(ResourceKey<DamageType> paramResourceKey) {
/* 133 */     return this.type.is(paramResourceKey);
/*     */   }
/*     */   
/*     */   public DamageType type() {
/* 137 */     return (DamageType)this.type.value();
/*     */   }
/*     */   
/*     */   public Holder<DamageType> typeHolder() {
/* 141 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\damagesource\DamageSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */