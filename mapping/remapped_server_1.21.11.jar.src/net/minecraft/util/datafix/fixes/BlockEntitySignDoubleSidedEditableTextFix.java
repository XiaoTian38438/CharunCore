/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class BlockEntitySignDoubleSidedEditableTextFix
/*    */   extends NamedEntityWriteReadFix {
/* 13 */   public static final List<String> FIELDS_TO_DROP = List.of("Text1", "Text2", "Text3", "Text4", "FilteredText1", "FilteredText2", "FilteredText3", "FilteredText4", "Color", "GlowingText");
/*    */ 
/*    */   
/*    */   public static final String FILTERED_CORRECT = "_filtered_correct";
/*    */ 
/*    */   
/*    */   private static final String DEFAULT_COLOR = "black";
/*    */ 
/*    */   
/*    */   public BlockEntitySignDoubleSidedEditableTextFix(Schema paramSchema, String paramString1, String paramString2) {
/* 23 */     super(paramSchema, true, paramString1, References.BLOCK_ENTITY, paramString2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 32 */     paramDynamic = paramDynamic.set("front_text", fixFrontTextTag(paramDynamic)).set("back_text", createDefaultText(paramDynamic)).set("is_waxed", paramDynamic.createBoolean(false)).set("_filtered_correct", paramDynamic.createBoolean(true));
/* 33 */     for (String str : FIELDS_TO_DROP) {
/* 34 */       paramDynamic = paramDynamic.remove(str);
/*    */     }
/* 36 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixFrontTextTag(Dynamic<T> paramDynamic) {
/* 40 */     Dynamic dynamic = LegacyComponentDataFixUtils.createEmptyComponent(paramDynamic.getOps());
/* 41 */     List list1 = getLines(paramDynamic, "Text").map(paramOptional -> (Dynamic)paramOptional.orElse(paramDynamic)).toList();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     Dynamic<T> dynamic1 = paramDynamic.emptyMap().set("messages", paramDynamic.createList(list1.stream())).set("color", paramDynamic.get("Color").result().orElse(paramDynamic.createString("black"))).set("has_glowing_text", paramDynamic.get("GlowingText").result().orElse(paramDynamic.createBoolean(false)));
/*    */     
/* 48 */     List list2 = getLines(paramDynamic, "FilteredText").toList();
/* 49 */     if (list2.stream().anyMatch(Optional::isPresent)) {
/* 50 */       dynamic1 = dynamic1.set("filtered_messages", paramDynamic.createList(Streams.mapWithIndex(list2.stream(), (paramOptional, paramLong) -> {
/*    */                 Dynamic dynamic = paramList.get((int)paramLong);
/*    */                 
/*    */                 return paramOptional.orElse(dynamic);
/*    */               })));
/*    */     }
/* 56 */     return dynamic1;
/*    */   }
/*    */   
/*    */   private static <T> Stream<Optional<Dynamic<T>>> getLines(Dynamic<T> paramDynamic, String paramString) {
/* 60 */     return Stream.of((Optional<Dynamic<T>>[])new Optional[] { paramDynamic
/* 61 */           .get(paramString + "1").result(), paramDynamic
/* 62 */           .get(paramString + "2").result(), paramDynamic
/* 63 */           .get(paramString + "3").result(), paramDynamic
/* 64 */           .get(paramString + "4").result() });
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> createDefaultText(Dynamic<T> paramDynamic) {
/* 69 */     return paramDynamic.emptyMap()
/* 70 */       .set("messages", createEmptyLines(paramDynamic))
/* 71 */       .set("color", paramDynamic.createString("black"))
/* 72 */       .set("has_glowing_text", paramDynamic.createBoolean(false));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> createEmptyLines(Dynamic<T> paramDynamic) {
/* 76 */     Dynamic dynamic = LegacyComponentDataFixUtils.createEmptyComponent(paramDynamic.getOps());
/* 77 */     return paramDynamic.createList(Stream.of(new Dynamic[] { dynamic, dynamic, dynamic, dynamic }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntitySignDoubleSidedEditableTextFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */