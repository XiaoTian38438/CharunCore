/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackSpawnEggFix extends DataFix {
/*    */   private final String itemType;
/*    */   private static final Map<String, String> MAP;
/*    */   
/*    */   public ItemStackSpawnEggFix(Schema paramSchema, boolean paramBoolean, String paramString) {
/* 24 */     super(paramSchema, paramBoolean);
/* 25 */     this.itemType = paramString;
/*    */   }
/*    */   static {
/* 28 */     MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*    */           paramHashMap.put("minecraft:bat", "minecraft:bat_spawn_egg");
/*    */           paramHashMap.put("minecraft:blaze", "minecraft:blaze_spawn_egg");
/*    */           paramHashMap.put("minecraft:cave_spider", "minecraft:cave_spider_spawn_egg");
/*    */           paramHashMap.put("minecraft:chicken", "minecraft:chicken_spawn_egg");
/*    */           paramHashMap.put("minecraft:cow", "minecraft:cow_spawn_egg");
/*    */           paramHashMap.put("minecraft:creeper", "minecraft:creeper_spawn_egg");
/*    */           paramHashMap.put("minecraft:donkey", "minecraft:donkey_spawn_egg");
/*    */           paramHashMap.put("minecraft:elder_guardian", "minecraft:elder_guardian_spawn_egg");
/*    */           paramHashMap.put("minecraft:ender_dragon", "minecraft:ender_dragon_spawn_egg");
/*    */           paramHashMap.put("minecraft:enderman", "minecraft:enderman_spawn_egg");
/*    */           paramHashMap.put("minecraft:endermite", "minecraft:endermite_spawn_egg");
/*    */           paramHashMap.put("minecraft:evocation_illager", "minecraft:evocation_illager_spawn_egg");
/*    */           paramHashMap.put("minecraft:ghast", "minecraft:ghast_spawn_egg");
/*    */           paramHashMap.put("minecraft:guardian", "minecraft:guardian_spawn_egg");
/*    */           paramHashMap.put("minecraft:horse", "minecraft:horse_spawn_egg");
/*    */           paramHashMap.put("minecraft:husk", "minecraft:husk_spawn_egg");
/*    */           paramHashMap.put("minecraft:iron_golem", "minecraft:iron_golem_spawn_egg");
/*    */           paramHashMap.put("minecraft:llama", "minecraft:llama_spawn_egg");
/*    */           paramHashMap.put("minecraft:magma_cube", "minecraft:magma_cube_spawn_egg");
/*    */           paramHashMap.put("minecraft:mooshroom", "minecraft:mooshroom_spawn_egg");
/*    */           paramHashMap.put("minecraft:mule", "minecraft:mule_spawn_egg");
/*    */           paramHashMap.put("minecraft:ocelot", "minecraft:ocelot_spawn_egg");
/*    */           paramHashMap.put("minecraft:pufferfish", "minecraft:pufferfish_spawn_egg");
/*    */           paramHashMap.put("minecraft:parrot", "minecraft:parrot_spawn_egg");
/*    */           paramHashMap.put("minecraft:pig", "minecraft:pig_spawn_egg");
/*    */           paramHashMap.put("minecraft:polar_bear", "minecraft:polar_bear_spawn_egg");
/*    */           paramHashMap.put("minecraft:rabbit", "minecraft:rabbit_spawn_egg");
/*    */           paramHashMap.put("minecraft:sheep", "minecraft:sheep_spawn_egg");
/*    */           paramHashMap.put("minecraft:shulker", "minecraft:shulker_spawn_egg");
/*    */           paramHashMap.put("minecraft:silverfish", "minecraft:silverfish_spawn_egg");
/*    */           paramHashMap.put("minecraft:skeleton", "minecraft:skeleton_spawn_egg");
/*    */           paramHashMap.put("minecraft:skeleton_horse", "minecraft:skeleton_horse_spawn_egg");
/*    */           paramHashMap.put("minecraft:slime", "minecraft:slime_spawn_egg");
/*    */           paramHashMap.put("minecraft:snow_golem", "minecraft:snow_golem_spawn_egg");
/*    */           paramHashMap.put("minecraft:spider", "minecraft:spider_spawn_egg");
/*    */           paramHashMap.put("minecraft:squid", "minecraft:squid_spawn_egg");
/*    */           paramHashMap.put("minecraft:stray", "minecraft:stray_spawn_egg");
/*    */           paramHashMap.put("minecraft:turtle", "minecraft:turtle_spawn_egg");
/*    */           paramHashMap.put("minecraft:vex", "minecraft:vex_spawn_egg");
/*    */           paramHashMap.put("minecraft:villager", "minecraft:villager_spawn_egg");
/*    */           paramHashMap.put("minecraft:vindication_illager", "minecraft:vindication_illager_spawn_egg");
/*    */           paramHashMap.put("minecraft:witch", "minecraft:witch_spawn_egg");
/*    */           paramHashMap.put("minecraft:wither", "minecraft:wither_spawn_egg");
/*    */           paramHashMap.put("minecraft:wither_skeleton", "minecraft:wither_skeleton_spawn_egg");
/*    */           paramHashMap.put("minecraft:wolf", "minecraft:wolf_spawn_egg");
/*    */           paramHashMap.put("minecraft:zombie", "minecraft:zombie_spawn_egg");
/*    */           paramHashMap.put("minecraft:zombie_horse", "minecraft:zombie_horse_spawn_egg");
/*    */           paramHashMap.put("minecraft:zombie_pigman", "minecraft:zombie_pigman_spawn_egg");
/*    */           paramHashMap.put("minecraft:zombie_villager", "minecraft:zombie_villager_spawn_egg");
/*    */         });
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 82 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 84 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 85 */     OpticFinder opticFinder2 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/* 86 */     OpticFinder opticFinder3 = type.findField("tag");
/* 87 */     OpticFinder opticFinder4 = opticFinder3.type().findField("EntityTag");
/*    */     
/* 89 */     return fixTypeEverywhereTyped("ItemInstanceSpawnEggFix" + getOutputSchema().getVersionKey(), type, paramTyped -> {
/*    */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*    */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), this.itemType)) {
/*    */             Typed typed1 = paramTyped.getOrCreateTyped(paramOpticFinder2);
/*    */             Typed typed2 = typed1.getOrCreateTyped(paramOpticFinder3);
/*    */             Optional optional1 = typed2.getOptional(paramOpticFinder4);
/*    */             if (optional1.isPresent())
/*    */               return paramTyped.set(paramOpticFinder1, Pair.of(References.ITEM_NAME.typeName(), MAP.getOrDefault(optional1.get(), "minecraft:pig_spawn_egg"))); 
/*    */           } 
/*    */           return paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackSpawnEggFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */