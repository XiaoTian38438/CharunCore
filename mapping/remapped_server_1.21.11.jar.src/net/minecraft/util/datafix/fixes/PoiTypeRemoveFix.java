/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class PoiTypeRemoveFix
/*    */   extends AbstractPoiSectionFix {
/*    */   private final Predicate<String> typesToKeep;
/*    */   
/*    */   public PoiTypeRemoveFix(Schema paramSchema, String paramString, Predicate<String> paramPredicate) {
/* 13 */     super(paramSchema, paramString);
/* 14 */     this.typesToKeep = paramPredicate.negate();
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> paramStream) {
/* 19 */     return paramStream.filter(this::shouldKeepRecord);
/*    */   }
/*    */   
/*    */   private <T> boolean shouldKeepRecord(Dynamic<T> paramDynamic) {
/* 23 */     return paramDynamic.get("type").asString().result().filter(this.typesToKeep).isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\PoiTypeRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */