/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class StructureSettingsFlattenFix extends DataFix {
/*    */   public StructureSettingsFlattenFix(Schema paramSchema) {
/* 15 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type type = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/*    */     
/* 22 */     OpticFinder opticFinder = type.findField("dimensions");
/*    */     
/* 24 */     return fixTypeEverywhereTyped("StructureSettingsFlatten", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Pair<Dynamic<?>, Dynamic<?>> fixDimension(Pair<Dynamic<?>, Dynamic<?>> paramPair) {
/* 32 */     Dynamic dynamic = (Dynamic)paramPair.getSecond();
/* 33 */     return Pair.of(paramPair.getFirst(), dynamic
/* 34 */         .update("generator", paramDynamic -> paramDynamic.update("settings", ())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixStructures(Dynamic<?> paramDynamic) {
/* 42 */     Dynamic dynamic = paramDynamic.get("structures").orElseEmptyMap().updateMapValues(paramPair -> paramPair.mapSecond(()));
/*    */ 
/*    */     
/* 45 */     return (Dynamic)DataFixUtils.orElse(paramDynamic
/* 46 */         .get("stronghold").result().map(paramDynamic3 -> paramDynamic1.set("minecraft:stronghold", paramDynamic3.set("type", paramDynamic2.createString("minecraft:concentric_rings")))), dynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\StructureSettingsFlattenFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */