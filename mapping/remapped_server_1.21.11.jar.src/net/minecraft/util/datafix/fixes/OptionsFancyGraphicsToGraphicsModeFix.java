/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsFancyGraphicsToGraphicsModeFix extends DataFix {
/*    */   public OptionsFancyGraphicsToGraphicsModeFix(Schema paramSchema) {
/* 11 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("fancyGraphics to graphicsMode", getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> fixGraphicsMode(Dynamic<T> paramDynamic) {
/* 22 */     if ("true".equals(paramDynamic.asString("true"))) {
/* 23 */       return paramDynamic.createString("1");
/*    */     }
/* 25 */     return paramDynamic.createString("0");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsFancyGraphicsToGraphicsModeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */