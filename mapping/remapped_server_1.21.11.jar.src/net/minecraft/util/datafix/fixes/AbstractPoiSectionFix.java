/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public abstract class AbstractPoiSectionFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AbstractPoiSectionFix(Schema paramSchema, String paramString) {
/* 21 */     super(paramSchema, false);
/* 22 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type type = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 29 */     if (!Objects.equals(type, getInputSchema().getType(References.POI_CHUNK))) {
/* 30 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 32 */     return fixTypeEverywhere(this.name, type, paramDynamicOps -> ());
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> cap(Dynamic<T> paramDynamic) {
/* 36 */     return paramDynamic.update("Sections", paramDynamic -> paramDynamic.updateMapValues(()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> processSection(Dynamic<?> paramDynamic) {
/* 42 */     return paramDynamic.update("Records", this::processSectionRecords);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> processSectionRecords(Dynamic<T> paramDynamic) {
/* 46 */     return (Dynamic<T>)DataFixUtils.orElse(paramDynamic.asStreamOpt().result().map(paramStream -> paramDynamic.createList(processRecords(paramStream))), paramDynamic);
/*    */   }
/*    */   
/*    */   protected abstract <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> paramStream);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AbstractPoiSectionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */