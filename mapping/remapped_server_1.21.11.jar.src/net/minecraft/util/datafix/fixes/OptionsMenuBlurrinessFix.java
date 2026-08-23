/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsMenuBlurrinessFix extends DataFix {
/*    */   public OptionsMenuBlurrinessFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("OptionsMenuBlurrinessFix", 
/* 16 */         getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int convertToIntRange(String paramString) {
/*    */     try {
/* 28 */       return Math.round(Float.parseFloat(paramString) * 10.0F);
/* 29 */     } catch (NumberFormatException numberFormatException) {
/* 30 */       return 5;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsMenuBlurrinessFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */