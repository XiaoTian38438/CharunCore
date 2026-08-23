/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ParticleUnflatteningFix
/*     */   extends DataFix {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public ParticleUnflatteningFix(Schema paramSchema) {
/*  28 */     super(paramSchema, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  33 */     Type type1 = getInputSchema().getType(References.PARTICLE);
/*  34 */     Type type2 = getOutputSchema().getType(References.PARTICLE);
/*  35 */     return writeFixAndRead("ParticleUnflatteningFix", type1, type2, this::fix);
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/*  39 */     Optional<String> optional = paramDynamic.asString().result();
/*  40 */     if (optional.isEmpty()) {
/*  41 */       return paramDynamic;
/*     */     }
/*     */     
/*  44 */     String str1 = optional.get();
/*  45 */     String[] arrayOfString = str1.split(" ", 2);
/*  46 */     String str2 = NamespacedSchema.ensureNamespaced(arrayOfString[0]);
/*  47 */     Dynamic<T> dynamic = paramDynamic.createMap(Map.of(paramDynamic.createString("type"), paramDynamic.createString(str2)));
/*  48 */     switch (str2) { case "minecraft:item": return 
/*  49 */           (arrayOfString.length > 1) ? updateItem(dynamic, arrayOfString[1]) : dynamic;
/*     */       case "minecraft:block": case "minecraft:block_marker": case "minecraft:falling_dust": case "minecraft:dust_pillar":
/*  51 */         return (arrayOfString.length > 1) ? updateBlock(dynamic, arrayOfString[1]) : dynamic;
/*  52 */       case "minecraft:dust": return (arrayOfString.length > 1) ? updateDust(dynamic, arrayOfString[1]) : dynamic;
/*     */       case "minecraft:dust_color_transition":
/*  54 */         return (arrayOfString.length > 1) ? updateDustTransition(dynamic, arrayOfString[1]) : dynamic;
/*  55 */       case "minecraft:sculk_charge": return (arrayOfString.length > 1) ? updateSculkCharge(dynamic, arrayOfString[1]) : dynamic;
/*  56 */       case "minecraft:vibration": return (arrayOfString.length > 1) ? updateVibration(dynamic, arrayOfString[1]) : dynamic;
/*  57 */       case "minecraft:shriek": return (arrayOfString.length > 1) ? updateShriek(dynamic, arrayOfString[1]) : dynamic; }
/*  58 */      return dynamic;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Dynamic<T> updateItem(Dynamic<T> paramDynamic, String paramString) {
/*  63 */     int i = paramString.indexOf("{");
/*  64 */     Dynamic dynamic = paramDynamic.createMap(
/*     */         
/*  66 */         Map.of(paramDynamic.createString("Count"), paramDynamic.createInt(1)));
/*     */     
/*  68 */     if (i == -1) {
/*  69 */       dynamic = dynamic.set("id", paramDynamic.createString(paramString));
/*     */     } else {
/*  71 */       dynamic = dynamic.set("id", paramDynamic.createString(paramString.substring(0, i)));
/*  72 */       Dynamic<?> dynamic1 = parseTag(paramDynamic.getOps(), paramString.substring(i));
/*  73 */       if (dynamic1 != null) {
/*  74 */         dynamic = dynamic.set("tag", dynamic1);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  79 */     return paramDynamic.set("item", dynamic);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> parseTag(DynamicOps<T> paramDynamicOps, String paramString) {
/*     */     try {
/*  84 */       return new Dynamic(paramDynamicOps, TagParser.create(paramDynamicOps).parseFully(paramString));
/*  85 */     } catch (Exception exception) {
/*  86 */       LOGGER.warn("Failed to parse tag: {}", paramString, exception);
/*     */       
/*  88 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateBlock(Dynamic<T> paramDynamic, String paramString) {
/*  93 */     int i = paramString.indexOf("[");
/*  94 */     Dynamic dynamic = paramDynamic.emptyMap();
/*  95 */     if (i == -1) {
/*  96 */       dynamic = dynamic.set("Name", paramDynamic.createString(NamespacedSchema.ensureNamespaced(paramString)));
/*     */     } else {
/*  98 */       dynamic = dynamic.set("Name", paramDynamic.createString(NamespacedSchema.ensureNamespaced(paramString.substring(0, i))));
/*  99 */       Map<Dynamic<T>, Dynamic<T>> map = parseBlockProperties(paramDynamic, paramString.substring(i));
/* 100 */       if (!map.isEmpty()) {
/* 101 */         dynamic = dynamic.set("Properties", paramDynamic
/* 102 */             .createMap(map));
/*     */       }
/*     */     } 
/*     */     
/* 106 */     return paramDynamic.set("block_state", dynamic);
/*     */   }
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> parseBlockProperties(Dynamic<T> paramDynamic, String paramString) {
/*     */     try {
/* 111 */       HashMap<Object, Object> hashMap = new HashMap<>();
/* 112 */       StringReader stringReader = new StringReader(paramString);
/*     */       
/* 114 */       stringReader.expect('[');
/* 115 */       stringReader.skipWhitespace();
/* 116 */       while (stringReader.canRead() && stringReader.peek() != ']') {
/* 117 */         stringReader.skipWhitespace();
/* 118 */         String str1 = stringReader.readString();
/* 119 */         stringReader.skipWhitespace();
/* 120 */         stringReader.expect('=');
/* 121 */         stringReader.skipWhitespace();
/* 122 */         String str2 = stringReader.readString();
/* 123 */         stringReader.skipWhitespace();
/* 124 */         hashMap.put(paramDynamic.createString(str1), paramDynamic.createString(str2));
/*     */         
/* 126 */         if (stringReader.canRead()) {
/* 127 */           if (stringReader.peek() == ',') {
/* 128 */             stringReader.skip();
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/* 134 */       stringReader.expect(']');
/* 135 */       return (Map)hashMap;
/* 136 */     } catch (Exception exception) {
/* 137 */       LOGGER.warn("Failed to parse block properties: {}", paramString, exception);
/* 138 */       return Map.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> readVector(Dynamic<T> paramDynamic, StringReader paramStringReader) throws CommandSyntaxException {
/* 143 */     float f1 = paramStringReader.readFloat();
/* 144 */     paramStringReader.expect(' ');
/* 145 */     float f2 = paramStringReader.readFloat();
/* 146 */     paramStringReader.expect(' ');
/* 147 */     float f3 = paramStringReader.readFloat();
/* 148 */     Objects.requireNonNull(paramDynamic); return paramDynamic.createList(Stream.<Float>of(new Float[] { Float.valueOf(f1), Float.valueOf(f2), Float.valueOf(f3) }).map(paramDynamic::createFloat));
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateDust(Dynamic<T> paramDynamic, String paramString) {
/*     */     try {
/* 153 */       StringReader stringReader = new StringReader(paramString);
/* 154 */       Dynamic<T> dynamic = readVector(paramDynamic, stringReader);
/* 155 */       stringReader.expect(' ');
/* 156 */       float f = stringReader.readFloat();
/*     */       
/* 158 */       return paramDynamic
/* 159 */         .set("color", dynamic)
/* 160 */         .set("scale", paramDynamic.createFloat(f));
/* 161 */     } catch (Exception exception) {
/* 162 */       LOGGER.warn("Failed to parse particle options: {}", paramString, exception);
/* 163 */       return paramDynamic;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateDustTransition(Dynamic<T> paramDynamic, String paramString) {
/*     */     try {
/* 169 */       StringReader stringReader = new StringReader(paramString);
/* 170 */       Dynamic<T> dynamic1 = readVector(paramDynamic, stringReader);
/* 171 */       stringReader.expect(' ');
/* 172 */       float f = stringReader.readFloat();
/* 173 */       stringReader.expect(' ');
/* 174 */       Dynamic<T> dynamic2 = readVector(paramDynamic, stringReader);
/*     */       
/* 176 */       return paramDynamic
/* 177 */         .set("from_color", dynamic1)
/* 178 */         .set("to_color", dynamic2)
/* 179 */         .set("scale", paramDynamic.createFloat(f));
/* 180 */     } catch (Exception exception) {
/* 181 */       LOGGER.warn("Failed to parse particle options: {}", paramString, exception);
/* 182 */       return paramDynamic;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateSculkCharge(Dynamic<T> paramDynamic, String paramString) {
/*     */     try {
/* 188 */       StringReader stringReader = new StringReader(paramString);
/* 189 */       float f = stringReader.readFloat();
/* 190 */       return paramDynamic.set("roll", paramDynamic.createFloat(f));
/* 191 */     } catch (Exception exception) {
/* 192 */       LOGGER.warn("Failed to parse particle options: {}", paramString, exception);
/* 193 */       return paramDynamic;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateVibration(Dynamic<T> paramDynamic, String paramString) {
/*     */     try {
/* 199 */       StringReader stringReader = new StringReader(paramString);
/* 200 */       float f1 = (float)stringReader.readDouble();
/* 201 */       stringReader.expect(' ');
/* 202 */       float f2 = (float)stringReader.readDouble();
/* 203 */       stringReader.expect(' ');
/* 204 */       float f3 = (float)stringReader.readDouble();
/* 205 */       stringReader.expect(' ');
/* 206 */       int i = stringReader.readInt();
/*     */ 
/*     */       
/* 209 */       Dynamic dynamic1 = paramDynamic.createIntList(IntStream.of(new int[] { Mth.floor(f1), Mth.floor(f2), Mth.floor(f3) }));
/* 210 */       Dynamic dynamic2 = paramDynamic.createMap(Map.of(paramDynamic
/* 211 */             .createString("type"), paramDynamic.createString("minecraft:block"), paramDynamic
/* 212 */             .createString("pos"), dynamic1));
/*     */ 
/*     */       
/* 215 */       return paramDynamic
/* 216 */         .set("destination", dynamic2)
/* 217 */         .set("arrival_in_ticks", paramDynamic.createInt(i));
/* 218 */     } catch (Exception exception) {
/* 219 */       LOGGER.warn("Failed to parse particle options: {}", paramString, exception);
/* 220 */       return paramDynamic;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateShriek(Dynamic<T> paramDynamic, String paramString) {
/*     */     try {
/* 226 */       StringReader stringReader = new StringReader(paramString);
/* 227 */       int i = stringReader.readInt();
/* 228 */       return paramDynamic.set("delay", paramDynamic.createInt(i));
/* 229 */     } catch (Exception exception) {
/* 230 */       LOGGER.warn("Failed to parse particle options: {}", paramString, exception);
/* 231 */       return paramDynamic;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ParticleUnflatteningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */