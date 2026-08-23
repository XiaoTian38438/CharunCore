/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class ItemLoreFix extends DataFix {
/*    */   public ItemLoreFix(Schema paramSchema) {
/* 14 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type type1 = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 21 */     Type type2 = getInputSchema().getType(References.TEXT_COMPONENT);
/* 22 */     OpticFinder opticFinder1 = type1.findField("tag");
/* 23 */     OpticFinder opticFinder2 = opticFinder1.type().findField("display");
/* 24 */     OpticFinder opticFinder3 = opticFinder2.type().findField("Lore");
/* 25 */     OpticFinder opticFinder4 = DSL.typeFinder(type2);
/*    */     
/* 27 */     return fixTypeEverywhereTyped("Item Lore componentize", type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemLoreFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */