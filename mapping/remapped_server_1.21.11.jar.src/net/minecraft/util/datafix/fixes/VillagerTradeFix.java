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
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class VillagerTradeFix
/*    */   extends DataFix
/*    */ {
/*    */   public VillagerTradeFix(Schema paramSchema) {
/* 19 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type type = getInputSchema().getType(References.VILLAGER_TRADE);
/* 25 */     OpticFinder opticFinder1 = type.findField("buy");
/* 26 */     OpticFinder opticFinder2 = type.findField("buyB");
/* 27 */     OpticFinder opticFinder3 = type.findField("sell");
/*    */     
/* 29 */     OpticFinder opticFinder4 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 30 */     Function function = paramTyped -> updateItemStack(paramOpticFinder, paramTyped);
/*    */     
/* 32 */     return fixTypeEverywhereTyped("Villager trade fix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, paramFunction).updateTyped(paramOpticFinder2, paramFunction).updateTyped(paramOpticFinder3, paramFunction));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> updateItemStack(OpticFinder<Pair<String, String>> paramOpticFinder, Typed<?> paramTyped) {
/* 41 */     return paramTyped.update(paramOpticFinder, paramPair -> paramPair.mapSecond(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\VillagerTradeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */