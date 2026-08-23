/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.datafix.fixes.References;
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
/*     */ public class V1451_6
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public static final String SPECIAL_OBJECTIVE_MARKER = "_special";
/*     */   
/*     */   public V1451_6(int paramInt, Schema paramSchema) {
/*  36 */     super(paramInt, paramSchema);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/*  41 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*     */     
/*  43 */     Supplier supplier = () -> DSL.compoundList(References.ITEM_NAME.in(paramSchema), DSL.constType(DSL.intType()));
/*     */     
/*  45 */     paramSchema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(new Pair[] { Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(paramSchema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", paramSupplier.get()), Pair.of("minecraft:used", paramSupplier.get()), Pair.of("minecraft:broken", paramSupplier.get()), Pair.of("minecraft:picked_up", paramSupplier.get()), Pair.of("minecraft:dropped", paramSupplier.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(paramSchema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))) })));
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
/*  59 */     Map<String, Supplier<TypeTemplate>> map = createCriterionTypes(paramSchema);
/*  60 */     paramSchema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", (TypeTemplate)DSL.taggedChoiceLazy("type", DSL.string(), paramMap), "DisplayName", References.TEXT_COMPONENT.in(paramSchema)), UNPACK_OBJECTIVE_ID, REPACK_OBJECTIVE_ID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Map<String, Supplier<TypeTemplate>> createCriterionTypes(Schema paramSchema) {
/*  70 */     Supplier supplier1 = () -> DSL.optionalFields("id", References.ITEM_NAME.in(paramSchema));
/*  71 */     Supplier supplier2 = () -> DSL.optionalFields("id", References.BLOCK_NAME.in(paramSchema));
/*  72 */     Supplier supplier3 = () -> DSL.optionalFields("id", References.ENTITY_NAME.in(paramSchema));
/*     */     
/*  74 */     HashMap<String, Supplier> hashMap = Maps.newHashMap();
/*  75 */     hashMap.put("minecraft:mined", supplier2);
/*     */     
/*  77 */     hashMap.put("minecraft:crafted", supplier1);
/*  78 */     hashMap.put("minecraft:used", supplier1);
/*  79 */     hashMap.put("minecraft:broken", supplier1);
/*  80 */     hashMap.put("minecraft:picked_up", supplier1);
/*  81 */     hashMap.put("minecraft:dropped", supplier1);
/*     */     
/*  83 */     hashMap.put("minecraft:killed", supplier3);
/*  84 */     hashMap.put("minecraft:killed_by", supplier3);
/*     */     
/*  86 */     hashMap.put("minecraft:custom", () -> DSL.optionalFields("id", DSL.constType(namespacedString())));
/*     */     
/*  88 */     hashMap.put("_special", () -> DSL.optionalFields("id", DSL.constType(DSL.string())));
/*  89 */     return (Map)hashMap;
/*     */   }
/*     */   
/*  92 */   protected static final Hook.HookFunction UNPACK_OBJECTIVE_ID = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> param1DynamicOps, T param1T) {
/*  95 */         Dynamic dynamic = new Dynamic(param1DynamicOps, param1T);
/*     */         
/*  97 */         return (T)((Dynamic)DataFixUtils.orElse(dynamic
/*  98 */             .get("CriteriaName").asString().result()
/*  99 */             .map(param1String -> {
/*     */                 int i = param1String.indexOf(':');
/*     */                 if (i < 0) {
/*     */                   return Pair.of("_special", param1String);
/*     */                 }
/*     */                 try {
/*     */                   Identifier identifier1 = Identifier.bySeparator(param1String.substring(0, i), '.');
/*     */                   Identifier identifier2 = Identifier.bySeparator(param1String.substring(i + 1), '.');
/*     */                   return Pair.of(identifier1.toString(), identifier2.toString());
/* 108 */                 } catch (Exception exception) {
/*     */                   
/*     */                   return Pair.of("_special", param1String);
/*     */                 } 
/* 112 */               }).map(param1Pair -> param1Dynamic.set("CriteriaType", param1Dynamic.createMap((Map)ImmutableMap.of(param1Dynamic.createString("type"), param1Dynamic.createString((String)param1Pair.getFirst()), param1Dynamic.createString("id"), param1Dynamic.createString((String)param1Pair.getSecond()))))), dynamic))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 121 */           .getValue();
/*     */       }
/*     */     };
/*     */   
/*     */   public static String packNamespacedWithDot(String paramString) {
/* 126 */     Identifier identifier = Identifier.tryParse(paramString);
/* 127 */     return (identifier != null) ? (identifier.getNamespace() + "." + identifier.getNamespace()) : paramString;
/*     */   }
/*     */   
/* 130 */   protected static final Hook.HookFunction REPACK_OBJECTIVE_ID = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> param1DynamicOps, T param1T) {
/* 133 */         Dynamic dynamic = new Dynamic(param1DynamicOps, param1T);
/*     */         
/* 135 */         Optional optional = dynamic.get("CriteriaType").get().result().flatMap(param1Dynamic2 -> {
/*     */               Optional<String> optional1 = param1Dynamic2.get("type").asString().result();
/*     */               
/*     */               Optional<String> optional2 = param1Dynamic2.get("id").asString().result();
/*     */               
/*     */               if (optional1.isPresent() && optional2.isPresent()) {
/*     */                 String str = optional1.get();
/*     */                 
/*     */                 return str.equals("_special") ? Optional.of(param1Dynamic1.createString(optional2.get())) : Optional.of(param1Dynamic2.createString(V1451_6.packNamespacedWithDot(str) + ":" + V1451_6.packNamespacedWithDot(str)));
/*     */               } 
/*     */               
/*     */               return Optional.empty();
/*     */             });
/*     */         
/* 149 */         return (T)((Dynamic)DataFixUtils.orElse(optional.map(param1Dynamic2 -> param1Dynamic1.set("CriteriaName", param1Dynamic2).remove("CriteriaType")), dynamic)).getValue();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1451_6.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */