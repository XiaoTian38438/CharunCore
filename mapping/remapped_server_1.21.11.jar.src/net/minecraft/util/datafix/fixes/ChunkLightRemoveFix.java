/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkLightRemoveFix extends DataFix {
/*    */   public ChunkLightRemoveFix(Schema paramSchema, boolean paramBoolean) {
/* 12 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type type1 = getInputSchema().getType(References.CHUNK);
/* 18 */     Type type2 = type1.findFieldType("Level");
/*    */     
/* 20 */     OpticFinder opticFinder = DSL.fieldFinder("Level", type2);
/*    */     
/* 22 */     return fixTypeEverywhereTyped("ChunkLightRemoveFix", type1, getOutputSchema().getType(References.CHUNK), paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkLightRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */