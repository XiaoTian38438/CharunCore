/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChestedHorsesInventoryZeroIndexingFix extends DataFix {
/*    */   public ChestedHorsesInventoryZeroIndexingFix(Schema paramSchema) {
/* 16 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 21 */     OpticFinder<Pair<String, Pair<Either<Pair<String, String>, Unit>, Pair<Either<?, Unit>, Dynamic<?>>>>> opticFinder = DSL.typeFinder(getInputSchema().getType(References.ITEM_STACK));
/* 22 */     Type<?> type = getInputSchema().getType(References.ENTITY);
/*    */     
/* 24 */     return TypeRewriteRule.seq(
/* 25 */         horseLikeInventoryIndexingFixer(opticFinder, type, "minecraft:llama"), new TypeRewriteRule[] {
/* 26 */           horseLikeInventoryIndexingFixer(opticFinder, type, "minecraft:trader_llama"), 
/* 27 */           horseLikeInventoryIndexingFixer(opticFinder, type, "minecraft:mule"), 
/* 28 */           horseLikeInventoryIndexingFixer(opticFinder, type, "minecraft:donkey")
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private TypeRewriteRule horseLikeInventoryIndexingFixer(OpticFinder<Pair<String, Pair<Either<Pair<String, String>, Unit>, Pair<Either<?, Unit>, Dynamic<?>>>>> paramOpticFinder, Type<?> paramType, String paramString) {
/* 34 */     Type type = getInputSchema().getChoiceType(References.ENTITY, paramString);
/* 35 */     OpticFinder opticFinder1 = DSL.namedChoice(paramString, type);
/* 36 */     OpticFinder opticFinder2 = type.findField("Items");
/* 37 */     return fixTypeEverywhereTyped("Fix non-zero indexing in chest horse type " + paramString, paramType, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChestedHorsesInventoryZeroIndexingFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */