/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ItemStackEnchantmentNamesFix extends DataFix {
/*    */   static {
/* 17 */     MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> {
/*    */           paramInt2ObjectOpenHashMap.put(0, "minecraft:protection");
/*    */           paramInt2ObjectOpenHashMap.put(1, "minecraft:fire_protection");
/*    */           paramInt2ObjectOpenHashMap.put(2, "minecraft:feather_falling");
/*    */           paramInt2ObjectOpenHashMap.put(3, "minecraft:blast_protection");
/*    */           paramInt2ObjectOpenHashMap.put(4, "minecraft:projectile_protection");
/*    */           paramInt2ObjectOpenHashMap.put(5, "minecraft:respiration");
/*    */           paramInt2ObjectOpenHashMap.put(6, "minecraft:aqua_affinity");
/*    */           paramInt2ObjectOpenHashMap.put(7, "minecraft:thorns");
/*    */           paramInt2ObjectOpenHashMap.put(8, "minecraft:depth_strider");
/*    */           paramInt2ObjectOpenHashMap.put(9, "minecraft:frost_walker");
/*    */           paramInt2ObjectOpenHashMap.put(10, "minecraft:binding_curse");
/*    */           paramInt2ObjectOpenHashMap.put(16, "minecraft:sharpness");
/*    */           paramInt2ObjectOpenHashMap.put(17, "minecraft:smite");
/*    */           paramInt2ObjectOpenHashMap.put(18, "minecraft:bane_of_arthropods");
/*    */           paramInt2ObjectOpenHashMap.put(19, "minecraft:knockback");
/*    */           paramInt2ObjectOpenHashMap.put(20, "minecraft:fire_aspect");
/*    */           paramInt2ObjectOpenHashMap.put(21, "minecraft:looting");
/*    */           paramInt2ObjectOpenHashMap.put(22, "minecraft:sweeping");
/*    */           paramInt2ObjectOpenHashMap.put(32, "minecraft:efficiency");
/*    */           paramInt2ObjectOpenHashMap.put(33, "minecraft:silk_touch");
/*    */           paramInt2ObjectOpenHashMap.put(34, "minecraft:unbreaking");
/*    */           paramInt2ObjectOpenHashMap.put(35, "minecraft:fortune");
/*    */           paramInt2ObjectOpenHashMap.put(48, "minecraft:power");
/*    */           paramInt2ObjectOpenHashMap.put(49, "minecraft:punch");
/*    */           paramInt2ObjectOpenHashMap.put(50, "minecraft:flame");
/*    */           paramInt2ObjectOpenHashMap.put(51, "minecraft:infinity");
/*    */           paramInt2ObjectOpenHashMap.put(61, "minecraft:luck_of_the_sea");
/*    */           paramInt2ObjectOpenHashMap.put(62, "minecraft:lure");
/*    */           paramInt2ObjectOpenHashMap.put(65, "minecraft:loyalty");
/*    */           paramInt2ObjectOpenHashMap.put(66, "minecraft:impaling");
/*    */           paramInt2ObjectOpenHashMap.put(67, "minecraft:riptide");
/*    */           paramInt2ObjectOpenHashMap.put(68, "minecraft:channeling");
/*    */           paramInt2ObjectOpenHashMap.put(70, "minecraft:mending");
/*    */           paramInt2ObjectOpenHashMap.put(71, "minecraft:vanishing_curse");
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Int2ObjectMap<String> MAP;
/*    */ 
/*    */   
/*    */   public ItemStackEnchantmentNamesFix(Schema paramSchema, boolean paramBoolean) {
/* 61 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 66 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/* 67 */     OpticFinder opticFinder = type.findField("tag");
/* 68 */     return fixTypeEverywhereTyped("ItemStackEnchantmentFix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 74 */     Objects.requireNonNull(paramDynamic); Optional<Dynamic> optional = paramDynamic.get("ench").asStreamOpt().map(paramStream -> paramStream.map(())).map(paramDynamic::createList).result();
/*    */     
/* 76 */     if (optional.isPresent()) {
/* 77 */       paramDynamic = paramDynamic.remove("ench").set("Enchantments", optional.get());
/*    */     }
/*    */     
/* 80 */     return paramDynamic.update("StoredEnchantments", paramDynamic -> {
/*    */           Objects.requireNonNull(paramDynamic);
/*    */           return (Dynamic)DataFixUtils.orElse(paramDynamic.asStreamOpt().map(()).map(paramDynamic::createList).result(), paramDynamic);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackEnchantmentNamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */