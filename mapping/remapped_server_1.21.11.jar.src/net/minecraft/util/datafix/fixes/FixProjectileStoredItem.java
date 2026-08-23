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
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class FixProjectileStoredItem
/*    */   extends DataFix {
/*    */   private static final String EMPTY_POTION = "minecraft:empty";
/*    */   
/*    */   public FixProjectileStoredItem(Schema paramSchema) {
/* 22 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type type1 = getInputSchema().getType(References.ENTITY);
/* 28 */     Type type2 = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 30 */     return fixTypeEverywhereTyped("Fix AbstractArrow item type", type1, type2, ExtraDataFixUtils.chainAllFilters(new Function[] {
/* 31 */             fixChoice("minecraft:trident", FixProjectileStoredItem::castUnchecked), 
/* 32 */             fixChoice("minecraft:arrow", FixProjectileStoredItem::fixArrow), 
/* 33 */             fixChoice("minecraft:spectral_arrow", FixProjectileStoredItem::fixSpectralArrow)
/*    */           }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Function<Typed<?>, Typed<?>> fixChoice(String paramString, SubFixer<?> paramSubFixer) {
/* 42 */     Type<?> type1 = getInputSchema().getChoiceType(References.ENTITY, paramString);
/* 43 */     Type<?> type2 = getOutputSchema().getChoiceType(References.ENTITY, paramString);
/*    */     
/* 45 */     return fixChoiceCap(paramString, paramSubFixer, type1, type2);
/*    */   }
/*    */   
/*    */   private static <T> Function<Typed<?>, Typed<?>> fixChoiceCap(String paramString, SubFixer<?> paramSubFixer, Type<?> paramType, Type<T> paramType1) {
/* 49 */     OpticFinder opticFinder = DSL.namedChoice(paramString, paramType);
/* 50 */     SubFixer<?> subFixer = paramSubFixer;
/* 51 */     return paramTyped -> paramTyped.updateTyped(paramOpticFinder, paramType, ());
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixArrow(Typed<?> paramTyped, Type<T> paramType) {
/* 55 */     return Util.writeAndReadTypedOrThrow(paramTyped, paramType, paramDynamic -> paramDynamic.set("item", createItemStack(paramDynamic, getArrowType(paramDynamic))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String getArrowType(Dynamic<?> paramDynamic) {
/* 61 */     return paramDynamic.get("Potion").asString("minecraft:empty").equals("minecraft:empty") ? "minecraft:arrow" : "minecraft:tipped_arrow";
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixSpectralArrow(Typed<?> paramTyped, Type<T> paramType) {
/* 65 */     return Util.writeAndReadTypedOrThrow(paramTyped, paramType, paramDynamic -> paramDynamic.set("item", createItemStack(paramDynamic, "minecraft:spectral_arrow")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> createItemStack(Dynamic<?> paramDynamic, String paramString) {
/* 71 */     return paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 72 */           .createString("id"), paramDynamic.createString(paramString), paramDynamic
/* 73 */           .createString("Count"), paramDynamic.createInt(1)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Typed<T> castUnchecked(Typed<?> paramTyped, Type<T> paramType) {
/* 79 */     return new Typed(paramType, paramTyped.getOps(), paramTyped.getValue());
/*    */   }
/*    */   
/*    */   private static interface SubFixer<F> {
/*    */     Typed<F> fix(Typed<?> param1Typed, Type<F> param1Type);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FixProjectileStoredItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */