/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemBannerColorFix
/*    */   extends DataFix {
/*    */   public ItemBannerColorFix(Schema paramSchema, boolean paramBoolean) {
/* 22 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 29 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 30 */     OpticFinder opticFinder2 = type.findField("tag");
/* 31 */     OpticFinder opticFinder3 = opticFinder2.type().findField("BlockEntityTag");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("ItemBannerColorFix", type, paramTyped -> {
/*    */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*    */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:banner")) {
/*    */             Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*    */             Optional<Typed> optional1 = paramTyped.getOptionalTyped(paramOpticFinder2);
/*    */             if (optional1.isPresent()) {
/*    */               Typed typed = optional1.get();
/*    */               Optional<Typed> optional2 = typed.getOptionalTyped(paramOpticFinder3);
/*    */               if (optional2.isPresent()) {
/*    */                 Typed typed1 = optional2.get();
/*    */                 Dynamic dynamic1 = (Dynamic)typed.get(DSL.remainderFinder());
/*    */                 Dynamic dynamic2 = (Dynamic)typed1.getOrCreate(DSL.remainderFinder());
/*    */                 if (dynamic2.get("Base").asNumber().result().isPresent()) {
/*    */                   dynamic = dynamic.set("Damage", dynamic.createShort((short)(dynamic2.get("Base").asInt(0) & 0xF)));
/*    */                   Optional<Dynamic> optional3 = dynamic1.get("display").result();
/*    */                   if (optional3.isPresent()) {
/*    */                     Dynamic dynamic3 = optional3.get();
/*    */                     Dynamic dynamic4 = dynamic3.createMap((Map)ImmutableMap.of(dynamic3.createString("Lore"), dynamic3.createList(Stream.of(dynamic3.createString("(+NBT")))));
/*    */                     if (Objects.equals(dynamic3, dynamic4))
/*    */                       return paramTyped.set(DSL.remainderFinder(), dynamic); 
/*    */                   } 
/*    */                   dynamic2.remove("Base");
/*    */                   return paramTyped.set(DSL.remainderFinder(), dynamic).set(paramOpticFinder2, typed.set(paramOpticFinder3, typed1.set(DSL.remainderFinder(), dynamic2)));
/*    */                 } 
/*    */               } 
/*    */             } 
/*    */             return paramTyped.set(DSL.remainderFinder(), dynamic);
/*    */           } 
/*    */           return paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemBannerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */