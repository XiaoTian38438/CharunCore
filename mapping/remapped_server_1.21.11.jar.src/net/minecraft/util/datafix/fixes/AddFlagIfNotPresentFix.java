/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class AddFlagIfNotPresentFix extends DataFix {
/*    */   private final String name;
/*    */   private final boolean flagValue;
/*    */   
/*    */   public AddFlagIfNotPresentFix(Schema paramSchema, DSL.TypeReference paramTypeReference, String paramString, boolean paramBoolean) {
/* 17 */     super(paramSchema, true);
/* 18 */     this.flagValue = paramBoolean;
/* 19 */     this.flagKey = paramString;
/* 20 */     this.name = "AddFlagIfNotPresentFix_" + this.flagKey + "=" + this.flagValue + " for " + paramSchema.getVersionKey();
/* 21 */     this.typeReference = paramTypeReference;
/*    */   }
/*    */   private final String flagKey; private final DSL.TypeReference typeReference;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type type = getInputSchema().getType(this.typeReference);
/*    */     
/* 28 */     return fixTypeEverywhereTyped(this.name, type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AddFlagIfNotPresentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */