/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Splitter;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.StreamSupport;
/*    */ import org.apache.commons.lang3.math.NumberUtils;
/*    */ 
/*    */ public class LevelFlatGeneratorInfoFix extends DataFix {
/*    */   public LevelFlatGeneratorInfoFix(Schema paramSchema, boolean paramBoolean) {
/* 20 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   private static final String GENERATOR_OPTIONS = "generatorOptions";
/*    */   @VisibleForTesting
/*    */   static final String DEFAULT = "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/* 27 */   private static final Splitter SPLITTER = Splitter.on(';').limit(5);
/* 28 */   private static final Splitter LAYER_SPLITTER = Splitter.on(',');
/* 29 */   private static final Splitter OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2);
/* 30 */   private static final Splitter AMOUNT_SPLITTER = Splitter.on('*').limit(2);
/* 31 */   private static final Splitter BLOCK_SPLITTER = Splitter.on(':').limit(3);
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 35 */     return fixTypeEverywhereTyped("LevelFlatGeneratorInfoFix", getInputSchema().getType(References.LEVEL), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fix));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> paramDynamic) {
/* 39 */     if (paramDynamic.get("generatorName").asString("").equalsIgnoreCase("flat"))
/* 40 */       return paramDynamic.update("generatorOptions", paramDynamic -> {
/*    */             Objects.requireNonNull(paramDynamic); return (Dynamic)DataFixUtils.orElse(paramDynamic.asString().map(this::fixString).map(paramDynamic::createString).result(), paramDynamic);
/* 42 */           });  return paramDynamic;
/*    */   } @VisibleForTesting
/*    */   String fixString(String paramString) {
/*    */     byte b;
/*    */     String str2;
/* 47 */     if (paramString.isEmpty()) {
/* 48 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*    */     }
/*    */     
/* 51 */     Iterator<String> iterator = SPLITTER.split(paramString).iterator();
/*    */     
/* 53 */     String str1 = iterator.next();
/*    */ 
/*    */     
/* 56 */     if (iterator.hasNext()) {
/* 57 */       b = NumberUtils.toInt(str1, 0);
/* 58 */       str2 = iterator.next();
/*    */     } else {
/* 60 */       b = 0;
/* 61 */       str2 = str1;
/*    */     } 
/*    */     
/* 64 */     if (!b || b > 3) {
/* 65 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*    */     }
/*    */     
/* 68 */     StringBuilder stringBuilder = new StringBuilder();
/*    */     
/* 70 */     Splitter splitter = (b < 3) ? OLD_AMOUNT_SPLITTER : AMOUNT_SPLITTER;
/*    */     
/* 72 */     stringBuilder.append(StreamSupport.stream(LAYER_SPLITTER.split(str2).spliterator(), false).map(paramString -> {
/*    */             boolean bool1;
/*    */             
/*    */             String str1;
/*    */             
/*    */             List<String> list1 = paramSplitter.splitToList(paramString);
/*    */             
/*    */             if (list1.size() == 2) {
/*    */               bool1 = NumberUtils.toInt(list1.get(0));
/*    */               str1 = list1.get(1);
/*    */             } else {
/*    */               bool1 = true;
/*    */               str1 = list1.get(0);
/*    */             } 
/*    */             List<String> list2 = BLOCK_SPLITTER.splitToList(str1);
/*    */             byte b = ((String)list2.get(0)).equals("minecraft") ? 1 : 0;
/*    */             String str2 = list2.get(b);
/*    */             int i = (paramInt == 3) ? EntityBlockStateFix.getBlockId("minecraft:" + str2) : NumberUtils.toInt(str2, 0);
/*    */             int j = b + 1;
/*    */             boolean bool2 = (list2.size() > j) ? NumberUtils.toInt(list2.get(j), 0) : false;
/*    */             return ((bool1 == true) ? "" : ("" + bool1 + "*")) + ((bool1 == true) ? "" : ("" + bool1 + "*"));
/* 93 */           }).collect(Collectors.joining(",")));
/*    */     
/* 95 */     while (iterator.hasNext()) {
/* 96 */       stringBuilder.append(';').append(iterator.next());
/*    */     }
/*    */     
/* 99 */     return stringBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\LevelFlatGeneratorInfoFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */