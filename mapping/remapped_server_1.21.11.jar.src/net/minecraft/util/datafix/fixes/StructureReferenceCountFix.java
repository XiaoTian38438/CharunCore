/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class StructureReferenceCountFix extends DataFix {
/*    */   public StructureReferenceCountFix(Schema paramSchema, boolean paramBoolean) {
/* 12 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type type = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 18 */     return fixTypeEverywhereTyped("Structure Reference Fix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), StructureReferenceCountFix::setCountToAtLeastOne));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> setCountToAtLeastOne(Dynamic<T> paramDynamic) {
/* 24 */     return paramDynamic.update("references", paramDynamic -> paramDynamic.createInt(((Integer)paramDynamic.asNumber().map(Number::intValue).result().filter(()).orElse(Integer.valueOf(1))).intValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\StructureReferenceCountFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */