/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class MobEffectIdFix extends DataFix {
/*     */   static {
/*  24 */     ID_MAP = (Int2ObjectMap<String>)Util.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> {
/*     */           paramInt2ObjectOpenHashMap.put(1, "minecraft:speed");
/*     */           paramInt2ObjectOpenHashMap.put(2, "minecraft:slowness");
/*     */           paramInt2ObjectOpenHashMap.put(3, "minecraft:haste");
/*     */           paramInt2ObjectOpenHashMap.put(4, "minecraft:mining_fatigue");
/*     */           paramInt2ObjectOpenHashMap.put(5, "minecraft:strength");
/*     */           paramInt2ObjectOpenHashMap.put(6, "minecraft:instant_health");
/*     */           paramInt2ObjectOpenHashMap.put(7, "minecraft:instant_damage");
/*     */           paramInt2ObjectOpenHashMap.put(8, "minecraft:jump_boost");
/*     */           paramInt2ObjectOpenHashMap.put(9, "minecraft:nausea");
/*     */           paramInt2ObjectOpenHashMap.put(10, "minecraft:regeneration");
/*     */           paramInt2ObjectOpenHashMap.put(11, "minecraft:resistance");
/*     */           paramInt2ObjectOpenHashMap.put(12, "minecraft:fire_resistance");
/*     */           paramInt2ObjectOpenHashMap.put(13, "minecraft:water_breathing");
/*     */           paramInt2ObjectOpenHashMap.put(14, "minecraft:invisibility");
/*     */           paramInt2ObjectOpenHashMap.put(15, "minecraft:blindness");
/*     */           paramInt2ObjectOpenHashMap.put(16, "minecraft:night_vision");
/*     */           paramInt2ObjectOpenHashMap.put(17, "minecraft:hunger");
/*     */           paramInt2ObjectOpenHashMap.put(18, "minecraft:weakness");
/*     */           paramInt2ObjectOpenHashMap.put(19, "minecraft:poison");
/*     */           paramInt2ObjectOpenHashMap.put(20, "minecraft:wither");
/*     */           paramInt2ObjectOpenHashMap.put(21, "minecraft:health_boost");
/*     */           paramInt2ObjectOpenHashMap.put(22, "minecraft:absorption");
/*     */           paramInt2ObjectOpenHashMap.put(23, "minecraft:saturation");
/*     */           paramInt2ObjectOpenHashMap.put(24, "minecraft:glowing");
/*     */           paramInt2ObjectOpenHashMap.put(25, "minecraft:levitation");
/*     */           paramInt2ObjectOpenHashMap.put(26, "minecraft:luck");
/*     */           paramInt2ObjectOpenHashMap.put(27, "minecraft:unluck");
/*     */           paramInt2ObjectOpenHashMap.put(28, "minecraft:slow_falling");
/*     */           paramInt2ObjectOpenHashMap.put(29, "minecraft:conduit_power");
/*     */           paramInt2ObjectOpenHashMap.put(30, "minecraft:dolphins_grace");
/*     */           paramInt2ObjectOpenHashMap.put(31, "minecraft:bad_omen");
/*     */           paramInt2ObjectOpenHashMap.put(32, "minecraft:hero_of_the_village");
/*     */           paramInt2ObjectOpenHashMap.put(33, "minecraft:darkness");
/*     */         });
/*     */   }
/*  60 */   private static final Set<String> MOB_EFFECT_INSTANCE_CARRIER_ITEMS = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
/*     */ 
/*     */   
/*     */   private static final Int2ObjectMap<String> ID_MAP;
/*     */ 
/*     */ 
/*     */   
/*     */   public MobEffectIdFix(Schema paramSchema) {
/*  68 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Optional<Dynamic<T>> getAndConvertMobEffectId(Dynamic<T> paramDynamic, String paramString) {
/*  77 */     Objects.requireNonNull(paramDynamic); return paramDynamic.get(paramString).asNumber().result().map(paramNumber -> (String)ID_MAP.get(paramNumber.intValue())).map(paramDynamic::createString);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectIdField(Dynamic<T> paramDynamic1, String paramString1, Dynamic<T> paramDynamic2, String paramString2) {
/*  81 */     Optional<Dynamic<T>> optional = getAndConvertMobEffectId(paramDynamic1, paramString1);
/*  82 */     return paramDynamic2.replaceField(paramString1, paramString2, optional);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectIdField(Dynamic<T> paramDynamic, String paramString1, String paramString2) {
/*  86 */     return updateMobEffectIdField(paramDynamic, paramString1, paramDynamic, paramString2);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectInstance(Dynamic<T> paramDynamic) {
/*  90 */     paramDynamic = updateMobEffectIdField(paramDynamic, "Id", "id");
/*  91 */     paramDynamic = paramDynamic.renameField("Ambient", "ambient");
/*  92 */     paramDynamic = paramDynamic.renameField("Amplifier", "amplifier");
/*  93 */     paramDynamic = paramDynamic.renameField("Duration", "duration");
/*  94 */     paramDynamic = paramDynamic.renameField("ShowParticles", "show_particles");
/*  95 */     paramDynamic = paramDynamic.renameField("ShowIcon", "show_icon");
/*     */     
/*  97 */     Optional optional = paramDynamic.get("HiddenEffect").result().map(MobEffectIdFix::updateMobEffectInstance);
/*  98 */     return paramDynamic.replaceField("HiddenEffect", "hidden_effect", optional);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateMobEffectInstanceList(Dynamic<T> paramDynamic, String paramString1, String paramString2) {
/* 102 */     Optional optional = paramDynamic.get(paramString1).asStreamOpt().result().map(paramStream -> paramDynamic.createList(paramStream.map(MobEffectIdFix::updateMobEffectInstance)));
/* 103 */     return paramDynamic.replaceField(paramString1, paramString2, optional);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateSuspiciousStewEntry(Dynamic<T> paramDynamic1, Dynamic<T> paramDynamic2) {
/* 107 */     paramDynamic2 = updateMobEffectIdField(paramDynamic1, "EffectId", paramDynamic2, "id");
/*     */     
/* 109 */     Optional optional = paramDynamic1.get("EffectDuration").result();
/* 110 */     return paramDynamic2.replaceField("EffectDuration", "duration", optional);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateSuspiciousStewEntry(Dynamic<T> paramDynamic) {
/* 114 */     return updateSuspiciousStewEntry(paramDynamic, paramDynamic);
/*     */   }
/*     */   
/*     */   private Typed<?> updateNamedChoice(Typed<?> paramTyped, DSL.TypeReference paramTypeReference, String paramString, Function<Dynamic<?>, Dynamic<?>> paramFunction) {
/* 118 */     Type type1 = getInputSchema().getChoiceType(paramTypeReference, paramString);
/* 119 */     Type type2 = getOutputSchema().getChoiceType(paramTypeReference, paramString);
/* 120 */     return paramTyped.updateTyped(DSL.namedChoice(paramString, type1), type2, paramTyped -> paramTyped.update(DSL.remainderFinder(), paramFunction));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule blockEntityFixer() {
/* 124 */     Type type = getInputSchema().getType(References.BLOCK_ENTITY);
/* 125 */     return fixTypeEverywhereTyped("BlockEntityMobEffectIdFix", type, paramTyped -> updateNamedChoice(paramTyped, References.BLOCK_ENTITY, "minecraft:beacon", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixMooshroomTag(Dynamic<T> paramDynamic) {
/* 135 */     Dynamic<T> dynamic1 = paramDynamic.emptyMap();
/* 136 */     Dynamic<T> dynamic2 = updateSuspiciousStewEntry(paramDynamic, dynamic1);
/*     */     
/* 138 */     if (!dynamic2.equals(dynamic1)) {
/* 139 */       paramDynamic = paramDynamic.set("stew_effects", paramDynamic.createList(Stream.of(dynamic2)));
/*     */     }
/* 141 */     return paramDynamic.remove("EffectId").remove("EffectDuration");
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixArrowTag(Dynamic<T> paramDynamic) {
/* 145 */     return updateMobEffectInstanceList(paramDynamic, "CustomPotionEffects", "custom_potion_effects");
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixAreaEffectCloudTag(Dynamic<T> paramDynamic) {
/* 149 */     return updateMobEffectInstanceList(paramDynamic, "Effects", "effects");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateLivingEntityTag(Dynamic<?> paramDynamic) {
/* 154 */     return updateMobEffectInstanceList(paramDynamic, "ActiveEffects", "active_effects");
/*     */   }
/*     */   
/*     */   private TypeRewriteRule entityFixer() {
/* 158 */     Type type = getInputSchema().getType(References.ENTITY);
/* 159 */     return fixTypeEverywhereTyped("EntityMobEffectIdFix", type, paramTyped -> {
/*     */           paramTyped = updateNamedChoice(paramTyped, References.ENTITY, "minecraft:mooshroom", MobEffectIdFix::fixMooshroomTag);
/*     */           paramTyped = updateNamedChoice(paramTyped, References.ENTITY, "minecraft:arrow", MobEffectIdFix::fixArrowTag);
/*     */           paramTyped = updateNamedChoice(paramTyped, References.ENTITY, "minecraft:area_effect_cloud", MobEffectIdFix::fixAreaEffectCloudTag);
/*     */           return paramTyped.update(DSL.remainderFinder(), MobEffectIdFix::updateLivingEntityTag);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeRewriteRule playerFixer() {
/* 169 */     Type type = getInputSchema().getType(References.PLAYER);
/* 170 */     return fixTypeEverywhereTyped("PlayerMobEffectIdFix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), MobEffectIdFix::updateLivingEntityTag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixSuspiciousStewTag(Dynamic<T> paramDynamic) {
/* 179 */     Optional optional = paramDynamic.get("Effects").asStreamOpt().result().map(paramStream -> paramDynamic.createList(paramStream.map(MobEffectIdFix::updateSuspiciousStewEntry)));
/*     */     
/* 181 */     return paramDynamic.replaceField("Effects", "effects", optional);
/*     */   }
/*     */   
/*     */   private TypeRewriteRule itemStackFixer() {
/* 185 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*     */     
/* 187 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/* 188 */     OpticFinder opticFinder2 = type.findField("tag");
/* 189 */     return fixTypeEverywhereTyped("ItemStackMobEffectIdFix", type, paramTyped -> {
/*     */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*     */           if (optional.isPresent()) {
/*     */             String str = (String)((Pair)optional.get()).getSecond();
/*     */             if (str.equals("minecraft:suspicious_stew")) {
/*     */               return paramTyped.updateTyped(paramOpticFinder2, ());
/*     */             }
/*     */             if (MOB_EFFECT_INSTANCE_CARRIER_ITEMS.contains(str)) {
/*     */               return paramTyped.updateTyped(paramOpticFinder2, ());
/*     */             }
/*     */           } 
/*     */           return paramTyped;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 206 */     return TypeRewriteRule.seq(
/* 207 */         blockEntityFixer(), new TypeRewriteRule[] {
/* 208 */           entityFixer(), 
/* 209 */           playerFixer(), 
/* 210 */           itemStackFixer()
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\MobEffectIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */