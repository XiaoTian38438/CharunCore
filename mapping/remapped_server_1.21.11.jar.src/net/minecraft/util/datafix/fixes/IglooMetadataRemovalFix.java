/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class IglooMetadataRemovalFix extends DataFix {
/*    */   public IglooMetadataRemovalFix(Schema paramSchema, boolean paramBoolean) {
/* 12 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type type = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 18 */     return fixTypeEverywhereTyped("IglooMetadataRemovalFix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), IglooMetadataRemovalFix::fixTag));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixTag(Dynamic<T> paramDynamic) {
/* 22 */     boolean bool = ((Boolean)paramDynamic.get("Children").asStreamOpt().map(paramStream -> Boolean.valueOf(paramStream.allMatch(IglooMetadataRemovalFix::isIglooPiece))).result().orElse(Boolean.valueOf(false))).booleanValue();
/*    */     
/* 24 */     if (bool) {
/* 25 */       return paramDynamic.set("id", paramDynamic.createString("Igloo")).remove("Children");
/*    */     }
/* 27 */     return paramDynamic.update("Children", IglooMetadataRemovalFix::removeIglooPieces);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> removeIglooPieces(Dynamic<T> paramDynamic) {
/* 32 */     Objects.requireNonNull(paramDynamic); return paramDynamic.asStreamOpt().map(paramStream -> paramStream.filter(())).map(paramDynamic::createList).result().orElse(paramDynamic);
/*    */   }
/*    */   
/*    */   private static boolean isIglooPiece(Dynamic<?> paramDynamic) {
/* 36 */     return paramDynamic.get("id").asString("").equals("Iglu");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\IglooMetadataRemovalFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */