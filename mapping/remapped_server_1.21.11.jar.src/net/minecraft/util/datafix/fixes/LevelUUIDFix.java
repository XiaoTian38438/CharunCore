/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LevelUUIDFix
/*    */   extends AbstractUUIDFix {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public LevelUUIDFix(Schema paramSchema) {
/* 19 */     super(paramSchema, References.LEVEL);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type type = getInputSchema().getType(this.typeReference);
/*    */     
/* 26 */     OpticFinder opticFinder1 = type.findField("CustomBossEvents");
/* 27 */     OpticFinder opticFinder2 = DSL.typeFinder(DSL.and(
/* 28 */           DSL.optional((Type)DSL.field("Name", getInputSchema().getTypeRaw(References.TEXT_COMPONENT))), 
/* 29 */           DSL.remainderType()));
/*    */ 
/*    */     
/* 32 */     return fixTypeEverywhereTyped("LevelUUIDFix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()).updateTyped(paramOpticFinder1, ()));
/*    */   }
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
/*    */   private Dynamic<?> updateWanderingTrader(Dynamic<?> paramDynamic) {
/* 46 */     return replaceUUIDString(paramDynamic, "WanderingTraderId", "WanderingTraderId").orElse(paramDynamic);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateDragonFight(Dynamic<?> paramDynamic) {
/* 50 */     return paramDynamic.update("DimensionData", paramDynamic -> paramDynamic.updateMapValues(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateCustomBossEvent(Dynamic<?> paramDynamic) {
/* 60 */     return paramDynamic.update("Players", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LevelUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */