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
/*    */ public class ItemStackMapIdFix
/*    */   extends DataFix
/*    */ {
/*    */   public ItemStackMapIdFix(Schema paramSchema, boolean paramBoolean) {
/* 20 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 27 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 28 */     OpticFinder opticFinder2 = type.findField("tag");
/*    */     
/* 30 */     return fixTypeEverywhereTyped("ItemInstanceMapIdFix", type, paramTyped -> {
/*    */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/*    */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:filled_map")) {
/*    */             Dynamic dynamic1 = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*    */             Typed typed = paramTyped.getOrCreateTyped(paramOpticFinder2);
/*    */             Dynamic dynamic2 = (Dynamic)typed.get(DSL.remainderFinder());
/*    */             dynamic2 = dynamic2.set("map", dynamic2.createInt(dynamic1.get("Damage").asInt(0)));
/*    */             return paramTyped.set(paramOpticFinder2, typed.set(DSL.remainderFinder(), dynamic2));
/*    */           } 
/*    */           return paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackMapIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */