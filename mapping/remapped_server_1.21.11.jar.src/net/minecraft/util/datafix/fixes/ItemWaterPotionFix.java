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
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemWaterPotionFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemWaterPotionFix(Schema paramSchema, boolean paramBoolean) {
/* 19 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 26 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 27 */     OpticFinder opticFinder2 = type.findField("tag");
/*    */     
/* 29 */     return fixTypeEverywhereTyped("ItemWaterPotionFix", type, paramTyped -> {
/*    */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*    */           if (optional.isPresent()) {
/*    */             String str = (String)((Pair)optional.get()).getSecond();
/*    */             if ("minecraft:potion".equals(str) || "minecraft:splash_potion".equals(str) || "minecraft:lingering_potion".equals(str) || "minecraft:tipped_arrow".equals(str)) {
/*    */               Typed typed = paramTyped.getOrCreateTyped(paramOpticFinder2);
/*    */               Dynamic dynamic = (Dynamic)typed.get(DSL.remainderFinder());
/*    */               if (dynamic.get("Potion").asString().result().isEmpty())
/*    */                 dynamic = dynamic.set("Potion", dynamic.createString("minecraft:water")); 
/*    */               return paramTyped.set(paramOpticFinder2, typed.set(DSL.remainderFinder(), dynamic));
/*    */             } 
/*    */           } 
/*    */           return paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemWaterPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */