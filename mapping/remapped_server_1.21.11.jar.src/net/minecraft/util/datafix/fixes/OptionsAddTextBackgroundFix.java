/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAddTextBackgroundFix extends DataFix {
/*    */   public OptionsAddTextBackgroundFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsAddTextBackgroundFix", getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private double calculateBackground(String paramString) {
/*    */     try {
/* 26 */       double d = 0.9D * Double.parseDouble(paramString) + 0.1D;
/* 27 */       return d / 2.0D;
/* 28 */     } catch (NumberFormatException numberFormatException) {
/* 29 */       return 0.5D;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsAddTextBackgroundFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */