/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityFallDistanceFloatToDoubleFix
/*    */   extends DataFix {
/*    */   public EntityFallDistanceFloatToDoubleFix(Schema paramSchema, DSL.TypeReference paramTypeReference) {
/* 13 */     super(paramSchema, false);
/* 14 */     this.type = paramTypeReference;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     return fixTypeEverywhereTyped("EntityFallDistanceFloatToDoubleFixFor" + this.type.typeName(), getOutputSchema().getType(this.type), EntityFallDistanceFloatToDoubleFix::fixEntity);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixEntity(Typed<?> paramTyped) {
/* 23 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.renameAndFixField("FallDistance", "fall_distance", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityFallDistanceFloatToDoubleFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */