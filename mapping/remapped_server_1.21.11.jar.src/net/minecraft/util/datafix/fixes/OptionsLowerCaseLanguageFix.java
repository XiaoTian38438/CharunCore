/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class OptionsLowerCaseLanguageFix extends DataFix {
/*    */   public OptionsLowerCaseLanguageFix(Schema paramSchema, boolean paramBoolean) {
/* 13 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 18 */     return fixTypeEverywhereTyped("OptionsLowerCaseLanguageFix", getInputSchema().getType(References.OPTIONS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\OptionsLowerCaseLanguageFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */