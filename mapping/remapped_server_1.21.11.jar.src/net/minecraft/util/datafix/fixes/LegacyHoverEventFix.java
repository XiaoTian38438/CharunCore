/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegacyHoverEventFix
/*     */   extends DataFix
/*     */ {
/*     */   public LegacyHoverEventFix(Schema paramSchema) {
/*  31 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  37 */     Type<Pair<String, ?>> type = getInputSchema().getType(References.TEXT_COMPONENT).findFieldType("hoverEvent");
/*  38 */     return createFixer(getInputSchema().getTypeRaw(References.TEXT_COMPONENT), type);
/*     */   }
/*     */   
/*     */   private <C, H extends Pair<String, ?>> TypeRewriteRule createFixer(Type<C> paramType, Type<H> paramType1) {
/*  42 */     Type type = DSL.named(References.TEXT_COMPONENT.typeName(), DSL.or(
/*  43 */           DSL.or(
/*  44 */             DSL.string(), 
/*  45 */             (Type)DSL.list(paramType)), 
/*     */           
/*  47 */           DSL.and(
/*  48 */             DSL.optional((Type)DSL.field("extra", (Type)DSL.list(paramType))), 
/*  49 */             DSL.optional((Type)DSL.field("separator", paramType)), 
/*  50 */             DSL.optional((Type)DSL.field("hoverEvent", paramType1)), 
/*  51 */             DSL.remainderType())));
/*     */ 
/*     */ 
/*     */     
/*  55 */     if (!type.equals(getInputSchema().getType(References.TEXT_COMPONENT))) {
/*  56 */       throw new IllegalStateException("Text component type did not match, expected " + String.valueOf(type) + " but got " + String.valueOf(getInputSchema().getType(References.TEXT_COMPONENT)));
/*     */     }
/*     */     
/*  59 */     return fixTypeEverywhere("LegacyHoverEventFix", type, paramDynamicOps -> ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <H> H fixHoverEvent(Type<H> paramType, String paramString, Dynamic<?> paramDynamic) {
/*  88 */     if ("show_text".equals(paramString))
/*     */     {
/*  90 */       return fixShowTextHover(paramType, paramDynamic);
/*     */     }
/*  92 */     return createPlaceholderHover(paramType, paramDynamic);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <H> H fixShowTextHover(Type<H> paramType, Dynamic<?> paramDynamic) {
/*  97 */     Dynamic dynamic = paramDynamic.renameField("value", "contents");
/*  98 */     return (H)Util.readTypedOrThrow(paramType, dynamic).getValue();
/*     */   }
/*     */   
/*     */   private static <H> H createPlaceholderHover(Type<H> paramType, Dynamic<?> paramDynamic) {
/* 102 */     JsonElement jsonElement = (JsonElement)paramDynamic.convert((DynamicOps)JsonOps.INSTANCE).getValue();
/* 103 */     Dynamic dynamic = new Dynamic((DynamicOps)JavaOps.INSTANCE, Map.of("action", "show_text", "contents", 
/*     */           
/* 105 */           (String)Map.of("text", "Legacy hoverEvent: " + 
/* 106 */             GsonHelper.toStableString(jsonElement))));
/*     */ 
/*     */     
/* 109 */     return (H)Util.readTypedOrThrow(paramType, dynamic).getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LegacyHoverEventFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */