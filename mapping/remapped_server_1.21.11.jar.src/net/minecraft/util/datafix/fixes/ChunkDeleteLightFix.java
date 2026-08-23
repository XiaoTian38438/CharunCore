/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkDeleteLightFix extends DataFix {
/*    */   public ChunkDeleteLightFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type type = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder opticFinder = type.findField("sections");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteLightFix for " + getOutputSchema().getVersionKey(), type, paramTyped -> {
/*    */           paramTyped = paramTyped.update(DSL.remainderFinder(), ());
/*    */           return paramTyped.updateTyped(paramOpticFinder, ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkDeleteLightFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */