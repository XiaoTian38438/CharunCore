/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ForcePoiRebuild
/*    */   extends DataFix {
/*    */   public ForcePoiRebuild(Schema paramSchema, boolean paramBoolean) {
/* 17 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type type = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 24 */     if (!Objects.equals(type, getInputSchema().getType(References.POI_CHUNK))) {
/* 25 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 27 */     return fixTypeEverywhere("POI rebuild", type, paramDynamicOps -> ());
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> cap(Dynamic<T> paramDynamic) {
/* 31 */     return paramDynamic.update("Sections", paramDynamic -> paramDynamic.updateMapValues(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ForcePoiRebuild.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */