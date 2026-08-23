/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DropChancesFormatFix extends DataFix {
/* 13 */   private static final List<String> ARMOR_SLOT_NAMES = List.of("feet", "legs", "chest", "head");
/* 14 */   private static final List<String> HAND_SLOT_NAMES = List.of("mainhand", "offhand");
/*    */   
/*    */   private static final float DEFAULT_CHANCE = 0.085F;
/*    */   
/*    */   public DropChancesFormatFix(Schema paramSchema) {
/* 19 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 25 */     return fixTypeEverywhereTyped("DropChancesFormatFix", getInputSchema().getType(References.ENTITY), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
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
/*    */   
/*    */   private static Dynamic<?> addSlotChances(Dynamic<?> paramDynamic, List<Float> paramList, List<String> paramList1) {
/* 51 */     for (byte b = 0; b < paramList1.size() && b < paramList.size(); b++) {
/* 52 */       String str = paramList1.get(b);
/* 53 */       float f = ((Float)paramList.get(b)).floatValue();
/* 54 */       if (f != 0.085F) {
/* 55 */         paramDynamic = paramDynamic.set(str, paramDynamic.createFloat(f));
/*    */       }
/*    */     } 
/* 58 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private static List<Float> parseDropChances(OptionalDynamic<?> paramOptionalDynamic) {
/* 62 */     return paramOptionalDynamic.asStream()
/* 63 */       .map(paramDynamic -> Float.valueOf(paramDynamic.asFloat(0.085F)))
/* 64 */       .toList();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\DropChancesFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */