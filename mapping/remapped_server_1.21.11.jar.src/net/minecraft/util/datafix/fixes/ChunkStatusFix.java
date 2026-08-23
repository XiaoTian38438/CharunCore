/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ChunkStatusFix extends DataFix {
/*    */   public ChunkStatusFix(Schema paramSchema, boolean paramBoolean) {
/* 15 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type type1 = getInputSchema().getType(References.CHUNK);
/* 21 */     Type type2 = type1.findFieldType("Level");
/*    */     
/* 23 */     OpticFinder opticFinder = DSL.fieldFinder("Level", type2);
/*    */     
/* 25 */     return fixTypeEverywhereTyped("ChunkStatusFix", type1, getOutputSchema().getType(References.CHUNK), paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkStatusFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */