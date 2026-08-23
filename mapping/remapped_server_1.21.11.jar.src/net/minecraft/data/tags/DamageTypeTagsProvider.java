/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ 
/*     */ public class DamageTypeTagsProvider extends KeyTagProvider<DamageType> {
/*     */   public DamageTypeTagsProvider(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/*  14 */     super(paramPackOutput, Registries.DAMAGE_TYPE, paramCompletableFuture);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTags(HolderLookup.Provider paramProvider) {
/*  19 */     tag(DamageTypeTags.DAMAGES_HELMET).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_BLOCK, DamageTypes.FALLING_STALACTITE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  25 */     tag(DamageTypeTags.BYPASSES_ARMOR).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.ON_FIRE, DamageTypes.IN_WALL, DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.FLY_INTO_WALL, DamageTypes.GENERIC, DamageTypes.WITHER, DamageTypes.DRAGON_BREATH, DamageTypes.STARVE, DamageTypes.FALL, DamageTypes.ENDER_PEARL, DamageTypes.FREEZE, DamageTypes.STALAGMITE, DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL, DamageTypes.SONIC_BOOM, DamageTypes.OUTSIDE_BORDER });
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
/*     */     
/*  47 */     tag(DamageTypeTags.BYPASSES_SHIELD).addTag(DamageTypeTags.BYPASSES_ARMOR).add((Object[])new ResourceKey[] { DamageTypes.CACTUS, DamageTypes.CAMPFIRE, DamageTypes.DRY_OUT, DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_STALACTITE, DamageTypes.HOT_FLOOR, DamageTypes.IN_FIRE, DamageTypes.LAVA, DamageTypes.LIGHTNING_BOLT, DamageTypes.SWEET_BERRY_BUSH });
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
/*     */ 
/*     */     
/*  60 */     tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     tag(DamageTypeTags.BYPASSES_EFFECTS).add(DamageTypes.STARVE);
/*     */ 
/*     */ 
/*     */     
/*  69 */     tag(DamageTypeTags.BYPASSES_RESISTANCE).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(DamageTypes.SONIC_BOOM);
/*     */ 
/*     */ 
/*     */     
/*  78 */     tag(DamageTypeTags.IS_FIRE).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE, DamageTypes.ON_FIRE, DamageTypes.LAVA, DamageTypes.HOT_FLOOR, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.FIREBALL });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     tag(DamageTypeTags.IS_PROJECTILE).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.MOB_PROJECTILE, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.THROWN, DamageTypes.WIND_CHARGE });
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
/*  99 */     tag(DamageTypeTags.WITCH_RESISTANT_TO).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     tag(DamageTypeTags.IS_EXPLOSION).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FIREWORKS, DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION, DamageTypes.BAD_RESPAWN_POINT });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     tag(DamageTypeTags.IS_FALL).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FALL, DamageTypes.ENDER_PEARL, DamageTypes.STALAGMITE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     tag(DamageTypeTags.IS_DROWNING).add(DamageTypes.DROWN);
/*     */ 
/*     */ 
/*     */     
/* 123 */     tag(DamageTypeTags.IS_FREEZING).add(DamageTypes.FREEZE);
/*     */ 
/*     */ 
/*     */     
/* 127 */     tag(DamageTypeTags.IS_LIGHTNING).add(DamageTypes.LIGHTNING_BOLT);
/*     */ 
/*     */ 
/*     */     
/* 131 */     tag(DamageTypeTags.NO_ANGER).add(DamageTypes.MOB_ATTACK_NO_AGGRO);
/*     */ 
/*     */ 
/*     */     
/* 135 */     tag(DamageTypeTags.NO_IMPACT).add(DamageTypes.DROWN);
/*     */ 
/*     */ 
/*     */     
/* 139 */     tag(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL).add(DamageTypes.FELL_OUT_OF_WORLD);
/*     */ 
/*     */ 
/*     */     
/* 143 */     tag(DamageTypeTags.WITHER_IMMUNE_TO).add(DamageTypes.DROWN);
/*     */ 
/*     */ 
/*     */     
/* 147 */     tag(DamageTypeTags.IGNITES_ARMOR_STANDS).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     tag(DamageTypeTags.BURNS_ARMOR_STANDS).add(DamageTypes.ON_FIRE);
/*     */ 
/*     */ 
/*     */     
/* 156 */     tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.MAGIC, DamageTypes.THORNS
/*     */ 
/*     */         
/* 159 */         }).addTag(DamageTypeTags.IS_EXPLOSION);
/*     */     
/* 161 */     tag(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH).add(DamageTypes.MAGIC);
/*     */ 
/*     */ 
/*     */     
/* 165 */     tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).addTag(DamageTypeTags.IS_EXPLOSION);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     tag(DamageTypeTags.NO_KNOCKBACK).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION, DamageTypes.BAD_RESPAWN_POINT, DamageTypes.IN_FIRE, DamageTypes.LIGHTNING_BOLT, DamageTypes.ON_FIRE, DamageTypes.LAVA, DamageTypes.HOT_FLOOR, DamageTypes.IN_WALL, DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.STARVE, DamageTypes.CACTUS, DamageTypes.FALL, DamageTypes.ENDER_PEARL, DamageTypes.FLY_INTO_WALL, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC, DamageTypes.MAGIC, DamageTypes.WITHER, DamageTypes.DRAGON_BREATH, DamageTypes.DRY_OUT, DamageTypes.SWEET_BERRY_BUSH, DamageTypes.FREEZE, DamageTypes.STALAGMITE, DamageTypes.OUTSIDE_BORDER, DamageTypes.GENERIC_KILL, DamageTypes.CAMPFIRE, DamageTypes.SPEAR });
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
/*     */     
/* 202 */     tag(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     tag(DamageTypeTags.CAN_BREAK_ARMOR_STAND).add(DamageTypes.PLAYER_EXPLOSION)
/*     */       
/* 212 */       .addTag(DamageTypeTags.IS_PLAYER_ATTACK);
/*     */     
/* 214 */     tag(DamageTypeTags.BYPASSES_WOLF_ARMOR).addTag(DamageTypeTags.BYPASSES_INVULNERABILITY).add((Object[])new ResourceKey[] { DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.DRY_OUT, DamageTypes.FREEZE, DamageTypes.IN_WALL, DamageTypes.INDIRECT_MAGIC, DamageTypes.MAGIC, DamageTypes.OUTSIDE_BORDER, DamageTypes.STARVE, DamageTypes.THORNS, DamageTypes.WITHER });
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
/*     */ 
/*     */ 
/*     */     
/* 228 */     tag(DamageTypeTags.IS_PLAYER_ATTACK).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.MACE_SMASH });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     tag(DamageTypeTags.BURN_FROM_STEPPING).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.CAMPFIRE, DamageTypes.HOT_FLOOR });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     tag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.CACTUS, DamageTypes.FREEZE, DamageTypes.HOT_FLOOR, DamageTypes.IN_FIRE, DamageTypes.LAVA, DamageTypes.LIGHTNING_BOLT, DamageTypes.ON_FIRE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 250 */     tag(DamageTypeTags.PANIC_CAUSES).addTag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES).add((Object[])new ResourceKey[] { DamageTypes.ARROW, DamageTypes.DRAGON_BREATH, DamageTypes.EXPLOSION, DamageTypes.FIREBALL, DamageTypes.FIREWORKS, DamageTypes.INDIRECT_MAGIC, DamageTypes.MAGIC, DamageTypes.MOB_ATTACK, DamageTypes.MOB_PROJECTILE, DamageTypes.PLAYER_EXPLOSION, DamageTypes.SONIC_BOOM, DamageTypes.STING, DamageTypes.THROWN, DamageTypes.TRIDENT, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.WIND_CHARGE, DamageTypes.WITHER, DamageTypes.WITHER_SKULL
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 269 */         }).addTag(DamageTypeTags.IS_PLAYER_ATTACK);
/*     */     
/* 271 */     tag(DamageTypeTags.IS_MACE_SMASH).add(DamageTypes.MACE_SMASH);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\tags\DamageTypeTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */