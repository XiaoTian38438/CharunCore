/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class UnflattenTextComponentFix extends DataFix {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public UnflattenTextComponentFix(Schema paramSchema) {
/* 21 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<Pair<String, String>> type = getInputSchema().getType(References.TEXT_COMPONENT);
/* 28 */     Type<?> type1 = getOutputSchema().getType(References.TEXT_COMPONENT);
/* 29 */     return createFixer(type, type1);
/*    */   }
/*    */   
/*    */   private <T> TypeRewriteRule createFixer(Type<Pair<String, String>> paramType, Type<T> paramType1) {
/* 33 */     return fixTypeEverywhere("UnflattenTextComponentFix", paramType, paramType1, paramDynamicOps -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> unflattenJson(DynamicOps<T> paramDynamicOps, String paramString) {
/*    */     try {
/* 40 */       JsonElement jsonElement = LenientJsonParser.parse(paramString);
/* 41 */       if (!jsonElement.isJsonNull()) {
/* 42 */         return new Dynamic(paramDynamicOps, JsonOps.INSTANCE.convertTo(paramDynamicOps, jsonElement));
/*    */       }
/* 44 */     } catch (Exception exception) {
/* 45 */       LOGGER.error("Failed to unflatten text component json: {}", paramString, exception);
/*    */     } 
/* 47 */     return new Dynamic(paramDynamicOps, paramDynamicOps.createString(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\UnflattenTextComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */