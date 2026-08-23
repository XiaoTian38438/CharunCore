/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextComponentHoverAndClickEventFix
/*     */   extends DataFix
/*     */ {
/*     */   public TextComponentHoverAndClickEventFix(Schema paramSchema) {
/*  32 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  37 */     Type<Pair<String, ?>> type = getInputSchema().getType(References.TEXT_COMPONENT).findFieldType("hoverEvent");
/*  38 */     return createFixer(getInputSchema().getTypeRaw(References.TEXT_COMPONENT), getOutputSchema().getType(References.TEXT_COMPONENT), type);
/*     */   }
/*     */ 
/*     */   
/*     */   private <C1, C2, H extends Pair<String, ?>> TypeRewriteRule createFixer(Type<C1> paramType, Type<C2> paramType1, Type<H> paramType2) {
/*  43 */     Type type1 = DSL.named(References.TEXT_COMPONENT.typeName(), DSL.or(
/*  44 */           DSL.or(
/*  45 */             DSL.string(), 
/*  46 */             (Type)DSL.list(paramType)), 
/*     */           
/*  48 */           DSL.and(
/*  49 */             DSL.optional((Type)DSL.field("extra", (Type)DSL.list(paramType))), 
/*  50 */             DSL.optional((Type)DSL.field("separator", paramType)), 
/*  51 */             DSL.optional((Type)DSL.field("hoverEvent", paramType2)), 
/*  52 */             DSL.remainderType())));
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (!type1.equals(getInputSchema().getType(References.TEXT_COMPONENT))) {
/*  57 */       throw new IllegalStateException("Text component type did not match, expected " + String.valueOf(type1) + " but got " + String.valueOf(getInputSchema().getType(References.TEXT_COMPONENT)));
/*     */     }
/*     */     
/*  60 */     Type type2 = ExtraDataFixUtils.patchSubType(type1, type1, paramType1);
/*     */     
/*  62 */     return fixTypeEverywhere("TextComponentHoverAndClickEventFix", type1, paramType1, paramDynamicOps -> ());
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
/*     */   private static Dynamic<?> fixTextComponent(Dynamic<?> paramDynamic) {
/*  85 */     return paramDynamic
/*  86 */       .renameAndFixField("hoverEvent", "hover_event", TextComponentHoverAndClickEventFix::fixHoverEvent)
/*     */       
/*  88 */       .renameAndFixField("clickEvent", "click_event", TextComponentHoverAndClickEventFix::fixClickEvent);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> copyFields(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2, String... paramVarArgs) {
/*  92 */     for (String str : paramVarArgs) {
/*  93 */       paramDynamic1 = Dynamic.copyField(paramDynamic2, str, paramDynamic1, str);
/*     */     }
/*  95 */     return paramDynamic1;
/*     */   } private static Dynamic<?> fixHoverEvent(Dynamic<?> paramDynamic) {
/*     */     Dynamic<?> dynamic;
/*     */     Optional optional;
/*  99 */     String str = paramDynamic.get("action").asString("");
/* 100 */     switch (str) { case "show_text":
/*     */       
/*     */       case "show_item":
/* 103 */         dynamic = paramDynamic.get("contents").orElseEmptyMap();
/* 104 */         optional = dynamic.asString().result();
/* 105 */         return optional.isPresent() ? 
/* 106 */           paramDynamic.renameField("contents", "id") : 
/*     */           
/* 108 */           copyFields(paramDynamic.remove("contents"), dynamic, new String[] { "id", "count", "components" });
/*     */       
/*     */       case "show_entity":
/* 111 */         dynamic = paramDynamic.get("contents").orElseEmptyMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return paramDynamic;
/*     */   }
/*     */   private static <T> Dynamic<T> fixClickEvent(Dynamic<T> paramDynamic) {
/*     */     Integer integer;
/*     */     int i;
/* 121 */     String str1 = paramDynamic.get("action").asString("");
/* 122 */     String str2 = paramDynamic.get("value").asString("");
/* 123 */     switch (str1) { case "open_url": return 
/*     */ 
/*     */           
/* 126 */           !validateUri(str2) ? 
/* 127 */           null : 
/*     */           
/* 129 */           paramDynamic.renameField("value", "url");
/*     */       case "open_file": 
/*     */       case "run_command":
/*     */       case "suggest_command":
/* 133 */         return !validateChat(str2) ? 
/* 134 */           null : 
/*     */           
/* 136 */           paramDynamic.renameField("value", "command");
/*     */       
/*     */       case "change_page":
/* 139 */         integer = paramDynamic.get("value").result().map(TextComponentHoverAndClickEventFix::parseOldPage).orElse(null);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 144 */         i = Math.max(integer.intValue(), 1);
/* 145 */         return (integer == null) ? null : paramDynamic.remove("value").set("page", paramDynamic.createInt(i)); }
/*     */     
/* 147 */     return paramDynamic;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Integer parseOldPage(Dynamic<?> paramDynamic) {
/* 152 */     Optional<Number> optional = paramDynamic.asNumber().result();
/*     */     
/* 154 */     if (optional.isPresent()) {
/* 155 */       return Integer.valueOf(((Number)optional.get()).intValue());
/*     */     }
/*     */     try {
/* 158 */       return Integer.valueOf(Integer.parseInt(paramDynamic.asString("")));
/* 159 */     } catch (Exception exception) {
/* 160 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean validateUri(String paramString) {
/*     */     try {
/* 166 */       URI uRI = new URI(paramString);
/* 167 */       String str1 = uRI.getScheme();
/* 168 */       if (str1 == null) {
/* 169 */         return false;
/*     */       }
/* 171 */       String str2 = str1.toLowerCase(Locale.ROOT);
/* 172 */       return ("http".equals(str2) || "https".equals(str2));
/* 173 */     } catch (URISyntaxException uRISyntaxException) {
/* 174 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean validateChat(String paramString) {
/* 179 */     for (byte b = 0; b < paramString.length(); b++) {
/* 180 */       char c = paramString.charAt(b);
/* 181 */       if (c == '§' || c < ' ' || c == '') {
/* 182 */         return false;
/*     */       }
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TextComponentHoverAndClickEventFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */