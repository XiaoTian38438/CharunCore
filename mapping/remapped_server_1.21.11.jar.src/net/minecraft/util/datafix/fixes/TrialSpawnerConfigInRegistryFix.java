/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TrialSpawnerConfigInRegistryFix
/*     */   extends NamedEntityFix {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public TrialSpawnerConfigInRegistryFix(Schema paramSchema) {
/*  26 */     super(paramSchema, false, "TrialSpawnerConfigInRegistryFix", References.BLOCK_ENTITY, "minecraft:trial_spawner");
/*     */   }
/*     */   
/*     */   public Dynamic<?> fixTag(Dynamic<Tag> paramDynamic) {
/*  30 */     Optional<Dynamic> optional1 = paramDynamic.get("normal_config").result();
/*  31 */     if (optional1.isEmpty()) {
/*  32 */       return paramDynamic;
/*     */     }
/*     */     
/*  35 */     Optional<Dynamic> optional2 = paramDynamic.get("ominous_config").result();
/*  36 */     if (optional2.isEmpty()) {
/*  37 */       return paramDynamic;
/*     */     }
/*     */     
/*  40 */     Identifier identifier = VanillaTrialChambers.CONFIGS_TO_KEY.get(Pair.of(optional1.get(), optional2.get()));
/*     */     
/*  42 */     if (identifier == null) {
/*  43 */       return paramDynamic;
/*     */     }
/*     */     
/*  46 */     return paramDynamic
/*  47 */       .set("normal_config", paramDynamic.createString(identifier.withSuffix("/normal").toString()))
/*  48 */       .set("ominous_config", paramDynamic.createString(identifier.withSuffix("/ominous").toString()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Typed<?> fix(Typed<?> paramTyped) {
/*  53 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*     */           DynamicOps dynamicOps = paramDynamic.getOps();
/*     */           Dynamic<?> dynamic = fixTag(paramDynamic.convert((DynamicOps)NbtOps.INSTANCE));
/*     */           return dynamic.convert(dynamicOps);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class VanillaTrialChambers
/*     */   {
/*  64 */     public static final Map<Pair<Dynamic<Tag>, Dynamic<Tag>>, Identifier> CONFIGS_TO_KEY = new HashMap<>();
/*     */     
/*     */     static {
/*  67 */       register(Identifier.withDefaultNamespace("trial_chamber/breeze"), "{simultaneous_mobs: 1.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:breeze\"}}, weight: 1}], ticks_between_spawn: 20, total_mobs: 2.0f, total_mobs_added_per_player: 1.0f}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 2.0f, total_mobs: 4.0f}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  74 */       register(Identifier.withDefaultNamespace("trial_chamber/melee/husk"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:husk\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:husk\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_melee\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       register(Identifier.withDefaultNamespace("trial_chamber/melee/spider"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:spider\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  86 */       register(Identifier.withDefaultNamespace("trial_chamber/melee/zombie"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:zombie\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],spawn_potentials: [{data: {entity: {id: \"minecraft:zombie\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_melee\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       register(Identifier.withDefaultNamespace("trial_chamber/ranged/poison_skeleton"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       register(Identifier.withDefaultNamespace("trial_chamber/ranged/skeleton"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       register(Identifier.withDefaultNamespace("trial_chamber/ranged/stray"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       register(Identifier.withDefaultNamespace("trial_chamber/slow_ranged/poison_skeleton"), "{simultaneous_mobs: 4.0f, simultaneous_mobs_added_per_player: 2.0f, spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}}, weight: 1}], ticks_between_spawn: 160}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       register(Identifier.withDefaultNamespace("trial_chamber/slow_ranged/skeleton"), "{simultaneous_mobs: 4.0f, simultaneous_mobs_added_per_player: 2.0f, spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}}, weight: 1}], ticks_between_spawn: 160}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       register(Identifier.withDefaultNamespace("trial_chamber/slow_ranged/stray"), "{simultaneous_mobs: 4.0f, simultaneous_mobs_added_per_player: 2.0f, spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}}, weight: 1}], ticks_between_spawn: 160}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/baby_zombie"), "{simultaneous_mobs: 2.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {IsBaby: 1b, id: \"minecraft:zombie\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {IsBaby: 1b, id: \"minecraft:zombie\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_melee\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 136 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/cave_spider"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:cave_spider\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/silverfish"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:silverfish\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/slime"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {Size: 1, id: \"minecraft:slime\"}}, weight: 3}, {data: {entity: {Size: 2, id: \"minecraft:slime\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static void register(Identifier param1Identifier, String param1String1, String param1String2) {
/*     */       try {
/* 160 */         CompoundTag compoundTag1 = parse(param1String1);
/* 161 */         CompoundTag compoundTag2 = parse(param1String2);
/*     */         
/* 163 */         CompoundTag compoundTag3 = compoundTag1.copy().merge(compoundTag2);
/* 164 */         CompoundTag compoundTag4 = removeDefaults(compoundTag3.copy());
/*     */         
/* 166 */         Dynamic<Tag> dynamic = asDynamic(compoundTag1);
/* 167 */         CONFIGS_TO_KEY.put(Pair.of(dynamic, asDynamic(compoundTag2)), param1Identifier);
/* 168 */         CONFIGS_TO_KEY.put(Pair.of(dynamic, asDynamic(compoundTag3)), param1Identifier);
/* 169 */         CONFIGS_TO_KEY.put(Pair.of(dynamic, asDynamic(compoundTag4)), param1Identifier);
/* 170 */       } catch (RuntimeException runtimeException) {
/* 171 */         throw new IllegalStateException("Failed to parse NBT for " + String.valueOf(param1Identifier), runtimeException);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Dynamic<Tag> asDynamic(CompoundTag param1CompoundTag) {
/* 176 */       return new Dynamic((DynamicOps)NbtOps.INSTANCE, param1CompoundTag);
/*     */     }
/*     */     
/*     */     private static CompoundTag parse(String param1String) {
/*     */       try {
/* 181 */         return TagParser.parseCompoundFully(param1String);
/* 182 */       } catch (CommandSyntaxException commandSyntaxException) {
/* 183 */         throw new IllegalArgumentException("Failed to parse Trial Spawner NBT config: " + param1String, commandSyntaxException);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static CompoundTag removeDefaults(CompoundTag param1CompoundTag) {
/* 192 */       if (param1CompoundTag.getIntOr("spawn_range", 0) == 4) {
/* 193 */         param1CompoundTag.remove("spawn_range");
/*     */       }
/*     */       
/* 196 */       if (param1CompoundTag.getFloatOr("total_mobs", 0.0F) == 6.0F) {
/* 197 */         param1CompoundTag.remove("total_mobs");
/*     */       }
/*     */       
/* 200 */       if (param1CompoundTag.getFloatOr("simultaneous_mobs", 0.0F) == 2.0F) {
/* 201 */         param1CompoundTag.remove("simultaneous_mobs");
/*     */       }
/*     */       
/* 204 */       if (param1CompoundTag.getFloatOr("total_mobs_added_per_player", 0.0F) == 2.0F) {
/* 205 */         param1CompoundTag.remove("total_mobs_added_per_player");
/*     */       }
/*     */       
/* 208 */       if (param1CompoundTag.getFloatOr("simultaneous_mobs_added_per_player", 0.0F) == 1.0F) {
/* 209 */         param1CompoundTag.remove("simultaneous_mobs_added_per_player");
/*     */       }
/*     */       
/* 212 */       if (param1CompoundTag.getIntOr("ticks_between_spawn", 0) == 40) {
/* 213 */         param1CompoundTag.remove("ticks_between_spawn");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 221 */       return param1CompoundTag;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TrialSpawnerConfigInRegistryFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */