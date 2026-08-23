/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ public class CombatTracker
/*     */ {
/*     */   public static final int RESET_DAMAGE_STATUS_TIME = 100;
/*     */   public static final int RESET_COMBAT_STATUS_TIME = 300;
/*  24 */   private static final Style INTENTIONAL_GAME_DESIGN_STYLE = Style.EMPTY
/*  25 */     .withClickEvent((ClickEvent)new ClickEvent.OpenUrl(CommonLinks.INTENTIONAL_GAME_DESIGN_BUG))
/*  26 */     .withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal("MCPE-28723")));
/*     */   
/*  28 */   private final List<CombatEntry> entries = Lists.newArrayList();
/*     */   private final LivingEntity mob;
/*     */   private int lastDamageTime;
/*     */   private int combatStartTime;
/*     */   private int combatEndTime;
/*     */   private boolean inCombat;
/*     */   private boolean takingDamage;
/*     */   
/*     */   public CombatTracker(LivingEntity paramLivingEntity) {
/*  37 */     this.mob = paramLivingEntity;
/*     */   }
/*     */   
/*     */   public void recordDamage(DamageSource paramDamageSource, float paramFloat) {
/*  41 */     recheckStatus();
/*     */     
/*  43 */     FallLocation fallLocation = FallLocation.getCurrentFallLocation(this.mob);
/*  44 */     CombatEntry combatEntry = new CombatEntry(paramDamageSource, paramFloat, fallLocation, (float)this.mob.fallDistance);
/*     */     
/*  46 */     this.entries.add(combatEntry);
/*  47 */     this.lastDamageTime = this.mob.tickCount;
/*  48 */     this.takingDamage = true;
/*     */     
/*  50 */     if (!this.inCombat && this.mob.isAlive() && shouldEnterCombat(paramDamageSource)) {
/*  51 */       this.inCombat = true;
/*  52 */       this.combatStartTime = this.mob.tickCount;
/*  53 */       this.combatEndTime = this.combatStartTime;
/*  54 */       this.mob.onEnterCombat();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldEnterCombat(DamageSource paramDamageSource) {
/*  59 */     return paramDamageSource.getEntity() instanceof LivingEntity;
/*     */   }
/*     */   
/*     */   private Component getMessageForAssistedFall(Entity paramEntity, Component paramComponent, String paramString1, String paramString2) {
/*  63 */     LivingEntity livingEntity = (LivingEntity)paramEntity; ItemStack itemStack = (paramEntity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY;
/*     */     
/*  65 */     if (!itemStack.isEmpty() && itemStack.has(DataComponents.CUSTOM_NAME)) {
/*  66 */       return (Component)Component.translatable(paramString1, new Object[] { this.mob.getDisplayName(), paramComponent, itemStack.getDisplayName() });
/*     */     }
/*     */     
/*  69 */     return (Component)Component.translatable(paramString2, new Object[] { this.mob.getDisplayName(), paramComponent });
/*     */   }
/*     */   
/*     */   private Component getFallMessage(CombatEntry paramCombatEntry, Entity paramEntity) {
/*  73 */     DamageSource damageSource = paramCombatEntry.source();
/*     */     
/*  75 */     if (damageSource.is(DamageTypeTags.IS_FALL) || damageSource.is(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL)) {
/*  76 */       FallLocation fallLocation = Objects.<FallLocation>requireNonNullElse(paramCombatEntry.fallLocation(), FallLocation.GENERIC);
/*  77 */       return (Component)Component.translatable(fallLocation.languageKey(), new Object[] { this.mob.getDisplayName() });
/*     */     } 
/*     */     
/*  80 */     Component component1 = getDisplayName(paramEntity);
/*  81 */     Entity entity = damageSource.getEntity();
/*  82 */     Component component2 = getDisplayName(entity);
/*     */ 
/*     */     
/*  85 */     if (component2 != null && !component2.equals(component1)) {
/*  86 */       return getMessageForAssistedFall(entity, component2, "death.fell.assist.item", "death.fell.assist");
/*     */     }
/*     */     
/*  89 */     if (component1 != null) {
/*  90 */       return getMessageForAssistedFall(paramEntity, component1, "death.fell.finish.item", "death.fell.finish");
/*     */     }
/*     */     
/*  93 */     return (Component)Component.translatable("death.fell.killer", new Object[] { this.mob.getDisplayName() });
/*     */   }
/*     */   
/*     */   private static Component getDisplayName(Entity paramEntity) {
/*  97 */     return (paramEntity == null) ? null : paramEntity.getDisplayName();
/*     */   }
/*     */   
/*     */   public Component getDeathMessage() {
/* 101 */     if (this.entries.isEmpty()) {
/* 102 */       return (Component)Component.translatable("death.attack.generic", new Object[] { this.mob.getDisplayName() });
/*     */     }
/*     */     
/* 105 */     CombatEntry combatEntry1 = this.entries.get(this.entries.size() - 1);
/* 106 */     DamageSource damageSource = combatEntry1.source();
/*     */     
/* 108 */     CombatEntry combatEntry2 = getMostSignificantFall();
/*     */     
/* 110 */     DeathMessageType deathMessageType = damageSource.type().deathMessageType();
/* 111 */     if (deathMessageType == DeathMessageType.FALL_VARIANTS && combatEntry2 != null) {
/* 112 */       return getFallMessage(combatEntry2, damageSource.getEntity());
/*     */     }
/*     */     
/* 115 */     if (deathMessageType == DeathMessageType.INTENTIONAL_GAME_DESIGN) {
/* 116 */       String str = "death.attack." + damageSource.getMsgId();
/* 117 */       MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets((Component)Component.translatable(str + ".link")).withStyle(INTENTIONAL_GAME_DESIGN_STYLE);
/* 118 */       return (Component)Component.translatable(str + ".message", new Object[] { this.mob.getDisplayName(), mutableComponent });
/*     */     } 
/*     */     
/* 121 */     return damageSource.getLocalizedDeathMessage(this.mob);
/*     */   }
/*     */   
/*     */   private CombatEntry getMostSignificantFall() {
/* 125 */     CombatEntry combatEntry1 = null;
/* 126 */     CombatEntry combatEntry2 = null;
/* 127 */     float f1 = 0.0F;
/* 128 */     float f2 = 0.0F;
/*     */     
/* 130 */     for (byte b = 0; b < this.entries.size(); b++) {
/* 131 */       CombatEntry combatEntry3 = this.entries.get(b);
/* 132 */       CombatEntry combatEntry4 = (b > 0) ? this.entries.get(b - 1) : null;
/*     */       
/* 134 */       DamageSource damageSource = combatEntry3.source();
/* 135 */       boolean bool = damageSource.is(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL);
/* 136 */       float f = bool ? Float.MAX_VALUE : combatEntry3.fallDistance();
/* 137 */       if ((damageSource.is(DamageTypeTags.IS_FALL) || bool) && f > 0.0F && (combatEntry1 == null || f > f2)) {
/* 138 */         if (b > 0) {
/* 139 */           combatEntry1 = combatEntry4;
/*     */         } else {
/* 141 */           combatEntry1 = combatEntry3;
/*     */         } 
/* 143 */         f2 = f;
/*     */       } 
/*     */       
/* 146 */       if (combatEntry3.fallLocation() != null && (combatEntry2 == null || combatEntry3.damage() > f1)) {
/* 147 */         combatEntry2 = combatEntry3;
/* 148 */         f1 = combatEntry3.damage();
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     if (f2 > 5.0F && combatEntry1 != null)
/* 153 */       return combatEntry1; 
/* 154 */     if (f1 > 5.0F && combatEntry2 != null) {
/* 155 */       return combatEntry2;
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCombatDuration() {
/* 162 */     if (this.inCombat) {
/* 163 */       return this.mob.tickCount - this.combatStartTime;
/*     */     }
/* 165 */     return this.combatEndTime - this.combatStartTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recheckStatus() {
/* 170 */     byte b = this.inCombat ? 300 : 100;
/*     */     
/* 172 */     if (this.takingDamage && (!this.mob.isAlive() || this.mob.tickCount - this.lastDamageTime > b)) {
/* 173 */       boolean bool = this.inCombat;
/* 174 */       this.takingDamage = false;
/* 175 */       this.inCombat = false;
/* 176 */       this.combatEndTime = this.mob.tickCount;
/*     */       
/* 178 */       if (bool) {
/* 179 */         this.mob.onLeaveCombat();
/*     */       }
/* 181 */       this.entries.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\damagesource\CombatTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */