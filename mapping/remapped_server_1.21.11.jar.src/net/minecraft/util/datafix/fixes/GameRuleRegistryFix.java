/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class GameRuleRegistryFix extends DataFix {
/*     */   public GameRuleRegistryFix(Schema paramSchema) {
/*  12 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  17 */     return fixTypeEverywhereTyped("GameRuleRegistryFix", getInputSchema().getType(References.LEVEL), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> paramDynamic) {
/* 105 */     return convertInteger(paramDynamic, -2147483648, 2147483647);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> paramDynamic, int paramInt) {
/* 109 */     return convertInteger(paramDynamic, paramInt, 2147483647);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> paramDynamic, int paramInt1, int paramInt2) {
/* 113 */     String str = paramDynamic.asString("");
/*     */     try {
/* 115 */       int i = Integer.parseInt(str);
/* 116 */       return paramDynamic.createInt(Mth.clamp(i, paramInt1, paramInt2));
/* 117 */     } catch (NumberFormatException numberFormatException) {
/* 118 */       return paramDynamic;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertBoolean(Dynamic<?> paramDynamic) {
/* 123 */     return paramDynamic.createBoolean(Boolean.parseBoolean(paramDynamic.asString("")));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertBooleanInverted(Dynamic<?> paramDynamic) {
/* 127 */     return paramDynamic.createBoolean(!Boolean.parseBoolean(paramDynamic.asString("")));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\GameRuleRegistryFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */