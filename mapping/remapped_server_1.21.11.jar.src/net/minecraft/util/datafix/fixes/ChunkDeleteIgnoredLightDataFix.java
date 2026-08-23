/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkDeleteIgnoredLightDataFix extends DataFix {
/*    */   public ChunkDeleteIgnoredLightDataFix(Schema paramSchema) {
/* 12 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type type = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder opticFinder = type.findField("sections");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteIgnoredLightDataFix", type, paramTyped -> {
/*    */           boolean bool = ((Dynamic)paramTyped.get(DSL.remainderFinder())).get("isLightOn").asBoolean(false);
/*    */           return !bool ? paramTyped.updateTyped(paramOpticFinder, ()) : paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkDeleteIgnoredLightDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */