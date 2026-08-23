/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class ProjectileStoredWeaponFix
/*    */   extends DataFix
/*    */ {
/*    */   public ProjectileStoredWeaponFix(Schema paramSchema) {
/* 19 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type type1 = getInputSchema().getType(References.ENTITY);
/* 25 */     Type type2 = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 27 */     return fixTypeEverywhereTyped("Fix Arrow stored weapon", type1, type2, ExtraDataFixUtils.chainAllFilters(new Function[] {
/* 28 */             fixChoice("minecraft:arrow"), 
/* 29 */             fixChoice("minecraft:spectral_arrow")
/*    */           }));
/*    */   }
/*    */   
/*    */   private Function<Typed<?>, Typed<?>> fixChoice(String paramString) {
/* 34 */     Type<?> type1 = getInputSchema().getChoiceType(References.ENTITY, paramString);
/* 35 */     Type<?> type2 = getOutputSchema().getChoiceType(References.ENTITY, paramString);
/*    */     
/* 37 */     return fixChoiceCap(paramString, type1, type2);
/*    */   }
/*    */   
/*    */   private static <T> Function<Typed<?>, Typed<?>> fixChoiceCap(String paramString, Type<?> paramType, Type<T> paramType1) {
/* 41 */     OpticFinder opticFinder = DSL.namedChoice(paramString, paramType);
/* 42 */     return paramTyped -> paramTyped.updateTyped(paramOpticFinder, paramType, ());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ProjectileStoredWeaponFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */