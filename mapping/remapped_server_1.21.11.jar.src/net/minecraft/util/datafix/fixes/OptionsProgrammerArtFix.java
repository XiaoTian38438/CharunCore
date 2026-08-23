/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsProgrammerArtFix extends DataFix {
/*    */   public OptionsProgrammerArtFix(Schema paramSchema) {
/* 11 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsProgrammerArtFix", getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> Dynamic<T> fixList(Dynamic<T> paramDynamic) {
/* 24 */     return paramDynamic.asString().result().map(paramString -> paramDynamic.createString(paramString.replace("\"programer_art\"", "\"programmer_art\""))).orElse(paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsProgrammerArtFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */