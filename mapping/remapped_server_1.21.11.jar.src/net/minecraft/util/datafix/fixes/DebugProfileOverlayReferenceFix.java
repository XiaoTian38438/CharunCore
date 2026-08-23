/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class DebugProfileOverlayReferenceFix extends DataFix {
/*    */   public DebugProfileOverlayReferenceFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("DebugProfileOverlayReferenceFix", 
/* 16 */         getInputSchema().getType(References.DEBUG_PROFILE), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\DebugProfileOverlayReferenceFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */