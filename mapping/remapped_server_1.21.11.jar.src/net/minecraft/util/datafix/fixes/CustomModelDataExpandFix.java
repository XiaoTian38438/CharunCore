/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class CustomModelDataExpandFix extends DataFix {
/*    */   public CustomModelDataExpandFix(Schema paramSchema) {
/* 14 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type type = getInputSchema().getType(References.DATA_COMPONENTS);
/*    */     
/* 21 */     return fixTypeEverywhereTyped("Custom Model Data expansion", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\CustomModelDataExpandFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */