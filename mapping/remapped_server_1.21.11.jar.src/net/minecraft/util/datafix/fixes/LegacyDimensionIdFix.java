/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class LegacyDimensionIdFix extends DataFix {
/*    */   public LegacyDimensionIdFix(Schema paramSchema) {
/* 14 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 19 */     TypeRewriteRule typeRewriteRule1 = fixTypeEverywhereTyped("PlayerLegacyDimensionFix", getInputSchema().getType(References.PLAYER), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixPlayer));
/*    */ 
/*    */ 
/*    */     
/* 23 */     Type type = getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
/* 24 */     OpticFinder opticFinder = type.findField("data");
/*    */     
/* 26 */     TypeRewriteRule typeRewriteRule2 = fixTypeEverywhereTyped("MapLegacyDimensionFix", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     return TypeRewriteRule.seq(typeRewriteRule1, typeRewriteRule2);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixMap(Dynamic<T> paramDynamic) {
/* 36 */     return paramDynamic.update("dimension", this::fixDimensionId);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixPlayer(Dynamic<T> paramDynamic) {
/* 40 */     return paramDynamic.update("Dimension", this::fixDimensionId);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixDimensionId(Dynamic<T> paramDynamic) {
/* 44 */     return (Dynamic<T>)DataFixUtils.orElse(paramDynamic
/* 45 */         .asNumber().result().map(paramNumber -> { switch (paramNumber.intValue()) { case -1: case 1:  }  return paramDynamic.createString("minecraft:overworld"); }), paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LegacyDimensionIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */