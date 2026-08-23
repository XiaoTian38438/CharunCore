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
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemShulkerBoxColorFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemShulkerBoxColorFix(Schema paramSchema, boolean paramBoolean) {
/* 20 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */   
/* 23 */   public static final String[] NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box" };
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 44 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 46 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 47 */     OpticFinder opticFinder2 = type.findField("tag");
/* 48 */     OpticFinder opticFinder3 = opticFinder2.type().findField("BlockEntityTag");
/*    */     
/* 50 */     return fixTypeEverywhereTyped("ItemShulkerBoxColorFix", type, paramTyped -> {
/*    */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*    */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:shulker_box")) {
/*    */             Optional<Typed> optional1 = paramTyped.getOptionalTyped(paramOpticFinder2);
/*    */             if (optional1.isPresent()) {
/*    */               Typed typed = optional1.get();
/*    */               Optional<Typed> optional2 = typed.getOptionalTyped(paramOpticFinder3);
/*    */               if (optional2.isPresent()) {
/*    */                 Typed typed1 = optional2.get();
/*    */                 Dynamic dynamic = (Dynamic)typed1.get(DSL.remainderFinder());
/*    */                 int i = dynamic.get("Color").asInt(0);
/*    */                 dynamic.remove("Color");
/*    */                 return paramTyped.set(paramOpticFinder2, typed.set(paramOpticFinder3, typed1.set(DSL.remainderFinder(), dynamic))).set(paramOpticFinder1, Pair.of(References.ITEM_NAME.typeName(), NAMES_BY_COLOR[i % 16]));
/*    */               } 
/*    */             } 
/*    */           } 
/*    */           return paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemShulkerBoxColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */