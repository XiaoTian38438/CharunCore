/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ObjectiveRenderTypeFix extends DataFix {
/*    */   public ObjectiveRenderTypeFix(Schema paramSchema) {
/* 13 */     super(paramSchema, false);
/*    */   }
/*    */   
/*    */   private static String getRenderType(String paramString) {
/* 17 */     return paramString.equals("health") ? "hearts" : "integer";
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type type = getInputSchema().getType(References.OBJECTIVE);
/* 23 */     return fixTypeEverywhereTyped("ObjectiveRenderTypeFix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ObjectiveRenderTypeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */