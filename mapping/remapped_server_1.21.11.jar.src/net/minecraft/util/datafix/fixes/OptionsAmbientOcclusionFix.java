/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAmbientOcclusionFix extends DataFix {
/*    */   public OptionsAmbientOcclusionFix(Schema paramSchema) {
/* 11 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsAmbientOcclusionFix", getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String updateValue(String paramString) {
/* 22 */     switch (paramString) { case "0": case "1": case "2":  }  return 
/*    */ 
/*    */       
/* 25 */       paramString;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsAmbientOcclusionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */