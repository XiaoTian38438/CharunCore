/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class TooltipDisplayComponentFix
/*     */   extends DataFix {
/*  23 */   private static final List<String> CONVERTED_ADDITIONAL_TOOLTIP_TYPES = List.of(new String[] { "minecraft:banner_patterns", "minecraft:bees", "minecraft:block_entity_data", "minecraft:block_state", "minecraft:bundle_contents", "minecraft:charged_projectiles", "minecraft:container", "minecraft:container_loot", "minecraft:firework_explosion", "minecraft:fireworks", "minecraft:instrument", "minecraft:map_id", "minecraft:painting/variant", "minecraft:pot_decorations", "minecraft:potion_contents", "minecraft:tropical_fish/pattern", "minecraft:written_book_content" });
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
/*     */   public TooltipDisplayComponentFix(Schema paramSchema) {
/*  44 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  49 */     Type type1 = getInputSchema().getType(References.DATA_COMPONENTS);
/*  50 */     Type type2 = getOutputSchema().getType(References.DATA_COMPONENTS);
/*  51 */     OpticFinder opticFinder1 = type1.findField("minecraft:can_place_on");
/*  52 */     OpticFinder opticFinder2 = type1.findField("minecraft:can_break");
/*  53 */     Type type3 = type2.findFieldType("minecraft:can_place_on");
/*  54 */     Type type4 = type2.findFieldType("minecraft:can_break");
/*  55 */     return fixTypeEverywhereTyped("TooltipDisplayComponentFix", type1, type2, paramTyped -> fix(paramTyped, paramOpticFinder1, paramOpticFinder2, paramType1, paramType2));
/*     */   }
/*     */   
/*     */   private static Typed<?> fix(Typed<?> paramTyped, OpticFinder<?> paramOpticFinder1, OpticFinder<?> paramOpticFinder2, Type<?> paramType1, Type<?> paramType2) {
/*  59 */     HashSet<String> hashSet = new HashSet();
/*  60 */     paramTyped = fixAdventureModePredicate(paramTyped, paramOpticFinder1, paramType1, "minecraft:can_place_on", hashSet);
/*  61 */     paramTyped = fixAdventureModePredicate(paramTyped, paramOpticFinder2, paramType2, "minecraft:can_break", hashSet);
/*     */     
/*  63 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*     */           paramDynamic = fixSimpleComponent(paramDynamic, "minecraft:trim", paramSet);
/*     */           paramDynamic = fixSimpleComponent(paramDynamic, "minecraft:unbreakable", paramSet);
/*     */           paramDynamic = fixComponentAndUnwrap(paramDynamic, "minecraft:dyed_color", "rgb", paramSet);
/*     */           paramDynamic = fixComponentAndUnwrap(paramDynamic, "minecraft:attribute_modifiers", "modifiers", paramSet);
/*     */           paramDynamic = fixComponentAndUnwrap(paramDynamic, "minecraft:enchantments", "levels", paramSet);
/*     */           paramDynamic = fixComponentAndUnwrap(paramDynamic, "minecraft:stored_enchantments", "levels", paramSet);
/*     */           paramDynamic = fixComponentAndUnwrap(paramDynamic, "minecraft:jukebox_playable", "song", paramSet);
/*     */           boolean bool1 = paramDynamic.get("minecraft:hide_tooltip").result().isPresent();
/*     */           paramDynamic = paramDynamic.remove("minecraft:hide_tooltip");
/*     */           boolean bool2 = paramDynamic.get("minecraft:hide_additional_tooltip").result().isPresent();
/*     */           paramDynamic = paramDynamic.remove("minecraft:hide_additional_tooltip");
/*     */           if (bool2) {
/*     */             for (String str : CONVERTED_ADDITIONAL_TOOLTIP_TYPES) {
/*     */               if (paramDynamic.get(str).result().isPresent()) {
/*     */                 paramSet.add(str);
/*     */               }
/*     */             } 
/*     */           }
/*     */           if (paramSet.isEmpty() && !bool1) {
/*     */             return paramDynamic;
/*     */           }
/*     */           Objects.requireNonNull(paramDynamic);
/*     */           return paramDynamic.set("minecraft:tooltip_display", paramDynamic.createMap(Map.of(paramDynamic.createString("hide_tooltip"), paramDynamic.createBoolean(bool1), paramDynamic.createString("hidden_components"), paramDynamic.createList(paramSet.stream().map(paramDynamic::createString)))));
/*     */         });
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
/*     */   private static Dynamic<?> fixSimpleComponent(Dynamic<?> paramDynamic, String paramString, Set<String> paramSet) {
/*  99 */     return fixRemainderComponent(paramDynamic, paramString, paramSet, UnaryOperator.identity());
/*     */   }
/*     */   
/*     */   private static Dynamic<?> fixComponentAndUnwrap(Dynamic<?> paramDynamic, String paramString1, String paramString2, Set<String> paramSet) {
/* 103 */     return fixRemainderComponent(paramDynamic, paramString1, paramSet, paramDynamic -> (Dynamic)DataFixUtils.orElse(paramDynamic.get(paramString).result(), paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> fixRemainderComponent(Dynamic<?> paramDynamic, String paramString, Set<String> paramSet, UnaryOperator<Dynamic<?>> paramUnaryOperator) {
/* 109 */     return paramDynamic.update(paramString, paramDynamic -> {
/*     */           boolean bool = paramDynamic.get("show_in_tooltip").asBoolean(true);
/*     */           if (!bool) {
/*     */             paramSet.add(paramString);
/*     */           }
/*     */           return paramUnaryOperator.apply(paramDynamic.remove("show_in_tooltip"));
/*     */         });
/*     */   }
/*     */   
/*     */   private static Typed<?> fixAdventureModePredicate(Typed<?> paramTyped, OpticFinder<?> paramOpticFinder, Type<?> paramType, String paramString, Set<String> paramSet) {
/* 119 */     return paramTyped.updateTyped(paramOpticFinder, paramType, paramTyped -> Util.writeAndReadTypedOrThrow(paramTyped, paramType, ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TooltipDisplayComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */