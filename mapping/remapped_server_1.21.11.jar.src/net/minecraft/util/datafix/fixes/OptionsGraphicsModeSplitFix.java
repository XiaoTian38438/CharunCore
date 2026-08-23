/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsGraphicsModeSplitFix extends DataFix {
/*    */   private final String newFieldName;
/*    */   private final String valueIfFast;
/*    */   private final String valueIfFancy;
/*    */   private final String valueIfFabulous;
/*    */   
/*    */   public OptionsGraphicsModeSplitFix(Schema paramSchema, String paramString1, String paramString2, String paramString3, String paramString4) {
/* 16 */     super(paramSchema, true);
/* 17 */     this.newFieldName = paramString1;
/* 18 */     this.valueIfFast = paramString2;
/* 19 */     this.valueIfFancy = paramString3;
/* 20 */     this.valueIfFabulous = paramString4;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     return fixTypeEverywhereTyped("graphicsMode split to " + this.newFieldName, getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String getValue(String paramString) {
/* 34 */     switch (paramString) { case "2": case "0":  }  return 
/*    */ 
/*    */       
/* 37 */       this.valueIfFancy;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsGraphicsModeSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */