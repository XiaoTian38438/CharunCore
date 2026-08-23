/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class SavedDataFeaturePoolElementFix extends DataFix {
/*  21 */   private static final Pattern INDEX_PATTERN = Pattern.compile("\\[(\\d+)\\]");
/*  22 */   private static final Set<String> PIECE_TYPE = Sets.newHashSet((Object[])new String[] { "minecraft:jigsaw", "minecraft:nvi", "minecraft:pcp", "minecraft:bastionremnant", "minecraft:runtime" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   private static final Set<String> FEATURES = Sets.newHashSet((Object[])new String[] { "minecraft:tree", "minecraft:flower", "minecraft:block_pile", "minecraft:random_patch" });
/*     */   
/*     */   public SavedDataFeaturePoolElementFix(Schema paramSchema) {
/*  32 */     super(paramSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  37 */     return writeFixAndRead("SavedDataFeaturePoolElementFix", getInputSchema().getType(References.STRUCTURE_FEATURE), getOutputSchema().getType(References.STRUCTURE_FEATURE), SavedDataFeaturePoolElementFix::fixTag);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixTag(Dynamic<T> paramDynamic) {
/*  41 */     return paramDynamic.update("Children", SavedDataFeaturePoolElementFix::updateChildren);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> updateChildren(Dynamic<T> paramDynamic) {
/*  45 */     Objects.requireNonNull(paramDynamic); return paramDynamic.asStreamOpt().map(SavedDataFeaturePoolElementFix::updateChildren).map(paramDynamic::createList).result().orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   private static Stream<? extends Dynamic<?>> updateChildren(Stream<? extends Dynamic<?>> paramStream) {
/*  49 */     return paramStream.map(paramDynamic -> {
/*     */           String str = paramDynamic.get("id").asString("");
/*     */           if (!PIECE_TYPE.contains(str)) {
/*     */             return paramDynamic;
/*     */           }
/*     */           OptionalDynamic optionalDynamic = paramDynamic.get("pool_element");
/*     */           return !optionalDynamic.get("element_type").asString("").equals("minecraft:feature_pool_element") ? paramDynamic : paramDynamic.update("pool_element", ());
/*     */         });
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
/*     */   private static <T> OptionalDynamic<T> get(Dynamic<T> paramDynamic, String... paramVarArgs) {
/*  68 */     if (paramVarArgs.length == 0) {
/*  69 */       throw new IllegalArgumentException("Missing path");
/*     */     }
/*     */     
/*  72 */     OptionalDynamic<T> optionalDynamic = paramDynamic.get(paramVarArgs[0]);
/*  73 */     for (byte b = 1; b < paramVarArgs.length; b++) {
/*  74 */       String str = paramVarArgs[b];
/*     */       
/*  76 */       Matcher matcher = INDEX_PATTERN.matcher(str);
/*  77 */       if (matcher.matches()) {
/*  78 */         int i = Integer.parseInt(matcher.group(1));
/*  79 */         List<Dynamic> list = optionalDynamic.asList(Function.identity());
/*  80 */         if (i >= 0 && i < list.size()) {
/*  81 */           optionalDynamic = new OptionalDynamic(paramDynamic.getOps(), DataResult.success(list.get(i)));
/*     */         } else {
/*  83 */           optionalDynamic = new OptionalDynamic(paramDynamic.getOps(), DataResult.error(() -> "Missing id:" + paramInt));
/*     */         } 
/*     */       } else {
/*  86 */         optionalDynamic = optionalDynamic.get(str);
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     return optionalDynamic;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static Dynamic<?> fixFeature(Dynamic<?> paramDynamic) {
/*  95 */     Optional<String> optional = getReplacement(
/*  96 */         get((Dynamic)paramDynamic, new String[] { "type" }).asString(""), 
/*  97 */         get((Dynamic)paramDynamic, new String[] { "name" }).asString(""), 
/*     */         
/*  99 */         get((Dynamic)paramDynamic, new String[] { "config", "state_provider", "type" }).asString(""), 
/* 100 */         get((Dynamic)paramDynamic, new String[] { "config", "state_provider", "state", "Name" }).asString(""), 
/* 101 */         get((Dynamic)paramDynamic, new String[] { "config", "state_provider", "entries", "[0]", "data", "Name" }).asString(""), 
/*     */         
/* 103 */         get((Dynamic)paramDynamic, new String[] { "config", "foliage_placer", "type" }).asString(""), 
/* 104 */         get((Dynamic)paramDynamic, new String[] { "config", "leaves_provider", "state", "Name" }).asString(""));
/*     */ 
/*     */     
/* 107 */     if (optional.isPresent()) {
/* 108 */       return paramDynamic.createString(optional.get());
/*     */     }
/* 110 */     return paramDynamic;
/*     */   }
/*     */   
/*     */   private static Optional<String> getReplacement(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7) {
/*     */     String str;
/* 115 */     if (!paramString1.isEmpty()) {
/* 116 */       str = paramString1;
/* 117 */     } else if (!paramString2.isEmpty()) {
/* 118 */       if ("minecraft:normal_tree".equals(paramString2)) {
/* 119 */         str = "minecraft:tree";
/*     */       } else {
/* 121 */         str = paramString2;
/*     */       } 
/*     */     } else {
/* 124 */       return Optional.empty();
/*     */     } 
/*     */     
/* 127 */     if (FEATURES.contains(str)) {
/* 128 */       if ("minecraft:random_patch".equals(str)) {
/* 129 */         if ("minecraft:simple_state_provider".equals(paramString3)) {
/* 130 */           if ("minecraft:sweet_berry_bush".equals(paramString4))
/* 131 */             return Optional.of("minecraft:patch_berry_bush"); 
/* 132 */           if ("minecraft:cactus".equals(paramString4)) {
/* 133 */             return Optional.of("minecraft:patch_cactus");
/*     */           }
/* 135 */         } else if ("minecraft:weighted_state_provider".equals(paramString3) && (
/* 136 */           "minecraft:grass".equals(paramString5) || "minecraft:fern".equals(paramString5))) {
/* 137 */           return Optional.of("minecraft:patch_taiga_grass");
/*     */         }
/*     */       
/* 140 */       } else if ("minecraft:block_pile".equals(str)) {
/* 141 */         if ("minecraft:simple_state_provider".equals(paramString3) || "minecraft:rotated_block_provider".equals(paramString3)) {
/* 142 */           if ("minecraft:hay_block".equals(paramString4))
/* 143 */             return Optional.of("minecraft:pile_hay"); 
/* 144 */           if ("minecraft:melon".equals(paramString4))
/* 145 */             return Optional.of("minecraft:pile_melon"); 
/* 146 */           if ("minecraft:snow".equals(paramString4)) {
/* 147 */             return Optional.of("minecraft:pile_snow");
/*     */           }
/* 149 */         } else if ("minecraft:weighted_state_provider".equals(paramString3)) {
/* 150 */           if ("minecraft:packed_ice".equals(paramString5) || "minecraft:blue_ice".equals(paramString5))
/* 151 */             return Optional.of("minecraft:pile_ice"); 
/* 152 */           if ("minecraft:jack_o_lantern".equals(paramString5) || "minecraft:pumpkin".equals(paramString5))
/* 153 */             return Optional.of("minecraft:pile_pumpkin"); 
/*     */         } 
/*     */       } else {
/* 156 */         if ("minecraft:flower".equals(str))
/* 157 */           return Optional.of("minecraft:flower_plain"); 
/* 158 */         if ("minecraft:tree".equals(str)) {
/* 159 */           if ("minecraft:acacia_foliage_placer".equals(paramString6))
/* 160 */             return Optional.of("minecraft:acacia"); 
/* 161 */           if ("minecraft:blob_foliage_placer".equals(paramString6) && "minecraft:oak_leaves".equals(paramString7))
/* 162 */             return Optional.of("minecraft:oak"); 
/* 163 */           if ("minecraft:pine_foliage_placer".equals(paramString6))
/* 164 */             return Optional.of("minecraft:pine"); 
/* 165 */           if ("minecraft:spruce_foliage_placer".equals(paramString6)) {
/* 166 */             return Optional.of("minecraft:spruce");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 171 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\SavedDataFeaturePoolElementFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */