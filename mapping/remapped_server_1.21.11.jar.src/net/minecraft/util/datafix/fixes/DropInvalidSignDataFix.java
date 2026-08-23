/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class DropInvalidSignDataFix extends DataFix {
/*    */   private final String entityName;
/*    */   
/*    */   public DropInvalidSignDataFix(Schema paramSchema, String paramString) {
/* 22 */     super(paramSchema, false);
/* 23 */     this.entityName = paramString;
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 27 */     paramDynamic = paramDynamic.update("front_text", DropInvalidSignDataFix::fixText);
/* 28 */     paramDynamic = paramDynamic.update("back_text", DropInvalidSignDataFix::fixText);
/*    */     
/* 30 */     for (String str : BlockEntitySignDoubleSidedEditableTextFix.FIELDS_TO_DROP) {
/* 31 */       paramDynamic = paramDynamic.remove(str);
/*    */     }
/* 33 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> fixText(Dynamic<T> paramDynamic) {
/* 38 */     Optional<Stream> optional = paramDynamic.get("filtered_messages").asStreamOpt().result();
/* 39 */     if (optional.isEmpty()) {
/* 40 */       return paramDynamic;
/*    */     }
/*    */     
/* 43 */     Dynamic dynamic = LegacyComponentDataFixUtils.createEmptyComponent(paramDynamic.getOps());
/* 44 */     List list1 = ((Stream)paramDynamic.get("messages").asStreamOpt().result().orElse(Stream.of(new Dynamic[0]))).toList();
/*    */ 
/*    */ 
/*    */     
/* 48 */     List list2 = Streams.mapWithIndex(optional.get(), (paramDynamic2, paramLong) -> { Dynamic dynamic = (paramLong < paramList.size()) ? paramList.get((int)paramLong) : paramDynamic1; return paramDynamic2.equals(paramDynamic1) ? dynamic : paramDynamic2; }).toList();
/* 49 */     if (list2.equals(list1)) {
/* 50 */       return paramDynamic.remove("filtered_messages");
/*    */     }
/* 52 */     return paramDynamic.set("filtered_messages", paramDynamic.createList(list2.stream()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 58 */     Type type1 = getInputSchema().getType(References.BLOCK_ENTITY);
/* 59 */     Type type2 = getInputSchema().getChoiceType(References.BLOCK_ENTITY, this.entityName);
/* 60 */     OpticFinder opticFinder = DSL.namedChoice(this.entityName, type2);
/*    */     
/* 62 */     return fixTypeEverywhereTyped("DropInvalidSignDataFix for " + this.entityName, type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder, paramType, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\DropInvalidSignDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */