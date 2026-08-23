/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackCustomNameToOverrideComponentFix extends DataFix {
/*    */   public ItemStackCustomNameToOverrideComponentFix(Schema paramSchema) {
/* 23 */     super(paramSchema, false);
/*    */   }
/*    */   
/* 26 */   private static final Set<String> MAP_NAMES = Set.of(new String[] { "filled_map.buried_treasure", "filled_map.explorer_jungle", "filled_map.explorer_swamp", "filled_map.mansion", "filled_map.monument", "filled_map.trial_chambers", "filled_map.village_desert", "filled_map.village_plains", "filled_map.village_savanna", "filled_map.village_snowy", "filled_map.village_taiga" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final TypeRewriteRule makeRule() {
/* 42 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 44 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 46 */     OpticFinder opticFinder2 = type.findField("components");
/*    */     
/* 48 */     return fixTypeEverywhereTyped("ItemStack custom_name to item_name component fix", type, paramTyped -> {
/*    */           Optional optional1 = paramTyped.getOptional(paramOpticFinder1);
/*    */           Optional optional2 = optional1.map(Pair::getSecond);
/*    */           return optional2.filter(()).isPresent() ? paramTyped.updateTyped(paramOpticFinder2, ItemStackCustomNameToOverrideComponentFix::fixBanner) : (optional2.filter(()).isPresent() ? paramTyped.updateTyped(paramOpticFinder2, ItemStackCustomNameToOverrideComponentFix::fixMap) : paramTyped);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Typed<T> fixMap(Typed<T> paramTyped) {
/* 65 */     Objects.requireNonNull(MAP_NAMES); return fixCustomName(paramTyped, MAP_NAMES::contains);
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixBanner(Typed<T> paramTyped) {
/* 69 */     return fixCustomName(paramTyped, paramString -> paramString.equals("block.minecraft.ominous_banner"));
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixCustomName(Typed<T> paramTyped, Predicate<String> paramPredicate) {
/* 73 */     return Util.writeAndReadTypedOrThrow(paramTyped, paramTyped.getType(), paramDynamic -> {
/*    */           OptionalDynamic optionalDynamic = paramDynamic.get("minecraft:custom_name");
/*    */           Optional optional = optionalDynamic.asString().result().flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(paramPredicate);
/*    */           return optional.isPresent() ? paramDynamic.renameField("minecraft:custom_name", "minecraft:item_name") : paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackCustomNameToOverrideComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */