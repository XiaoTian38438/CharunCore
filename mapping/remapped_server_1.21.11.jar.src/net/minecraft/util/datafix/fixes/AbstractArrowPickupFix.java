/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public class AbstractArrowPickupFix
/*    */   extends DataFix
/*    */ {
/*    */   public AbstractArrowPickupFix(Schema paramSchema) {
/* 17 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Schema schema = getInputSchema();
/* 23 */     return fixTypeEverywhereTyped("AbstractArrowPickupFix", schema.getType(References.ENTITY), this::updateProjectiles);
/*    */   }
/*    */   
/*    */   private Typed<?> updateProjectiles(Typed<?> paramTyped) {
/* 27 */     paramTyped = updateEntity(paramTyped, "minecraft:arrow", AbstractArrowPickupFix::updatePickup);
/* 28 */     paramTyped = updateEntity(paramTyped, "minecraft:spectral_arrow", AbstractArrowPickupFix::updatePickup);
/* 29 */     paramTyped = updateEntity(paramTyped, "minecraft:trident", AbstractArrowPickupFix::updatePickup);
/* 30 */     return paramTyped;
/*    */   }
/*    */   
/*    */   private static Dynamic<?> updatePickup(Dynamic<?> paramDynamic) {
/* 34 */     if (paramDynamic.get("pickup").result().isPresent()) {
/* 35 */       return paramDynamic;
/*    */     }
/*    */     
/* 38 */     boolean bool = paramDynamic.get("player").asBoolean(true);
/* 39 */     return paramDynamic.set("pickup", paramDynamic.createByte((byte)(bool ? 1 : 0))).remove("player");
/*    */   }
/*    */   
/*    */   private Typed<?> updateEntity(Typed<?> paramTyped, String paramString, Function<Dynamic<?>, Dynamic<?>> paramFunction) {
/* 43 */     Type type1 = getInputSchema().getChoiceType(References.ENTITY, paramString);
/* 44 */     Type type2 = getOutputSchema().getChoiceType(References.ENTITY, paramString);
/* 45 */     return paramTyped.updateTyped(DSL.namedChoice(paramString, type1), type2, paramTyped -> paramTyped.update(DSL.remainderFinder(), paramFunction));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AbstractArrowPickupFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */