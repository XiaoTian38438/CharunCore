/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ 
/*     */ public class ChunkStructuresTemplateRenameFix extends DataFix {
/*  13 */   private static final ImmutableMap<String, Pair<String, ImmutableMap<String, String>>> RENAMES = ImmutableMap.builder()
/*  14 */     .put("EndCity", Pair.of("ECP", ImmutableMap.builder()
/*  15 */         .put("second_floor", "second_floor_1")
/*  16 */         .put("third_floor", "third_floor_1")
/*  17 */         .put("third_floor_c", "third_floor_2")
/*  18 */         .build()))
/*     */     
/*  20 */     .put("Mansion", Pair.of("WMP", ImmutableMap.builder()
/*  21 */         .put("carpet_south", "carpet_south_1")
/*  22 */         .put("carpet_west", "carpet_west_1")
/*  23 */         .put("indoors_door", "indoors_door_1")
/*  24 */         .put("indoors_wall", "indoors_wall_1")
/*  25 */         .build()))
/*     */     
/*  27 */     .put("Igloo", Pair.of("Iglu", ImmutableMap.builder()
/*  28 */         .put("minecraft:igloo/igloo_bottom", "minecraft:igloo/bottom")
/*  29 */         .put("minecraft:igloo/igloo_middle", "minecraft:igloo/middle")
/*  30 */         .put("minecraft:igloo/igloo_top", "minecraft:igloo/top")
/*  31 */         .build()))
/*     */     
/*  33 */     .put("Ocean_Ruin", Pair.of("ORP", ImmutableMap.builder()
/*  34 */         .put("minecraft:ruin/big_ruin1_brick", "minecraft:underwater_ruin/big_brick_1")
/*  35 */         .put("minecraft:ruin/big_ruin2_brick", "minecraft:underwater_ruin/big_brick_2")
/*  36 */         .put("minecraft:ruin/big_ruin3_brick", "minecraft:underwater_ruin/big_brick_3")
/*  37 */         .put("minecraft:ruin/big_ruin8_brick", "minecraft:underwater_ruin/big_brick_8")
/*  38 */         .put("minecraft:ruin/big_ruin1_cracked", "minecraft:underwater_ruin/big_cracked_1")
/*  39 */         .put("minecraft:ruin/big_ruin2_cracked", "minecraft:underwater_ruin/big_cracked_2")
/*  40 */         .put("minecraft:ruin/big_ruin3_cracked", "minecraft:underwater_ruin/big_cracked_3")
/*  41 */         .put("minecraft:ruin/big_ruin8_cracked", "minecraft:underwater_ruin/big_cracked_8")
/*  42 */         .put("minecraft:ruin/big_ruin1_mossy", "minecraft:underwater_ruin/big_mossy_1")
/*  43 */         .put("minecraft:ruin/big_ruin2_mossy", "minecraft:underwater_ruin/big_mossy_2")
/*  44 */         .put("minecraft:ruin/big_ruin3_mossy", "minecraft:underwater_ruin/big_mossy_3")
/*  45 */         .put("minecraft:ruin/big_ruin8_mossy", "minecraft:underwater_ruin/big_mossy_8")
/*  46 */         .put("minecraft:ruin/big_ruin_warm4", "minecraft:underwater_ruin/big_warm_4")
/*  47 */         .put("minecraft:ruin/big_ruin_warm5", "minecraft:underwater_ruin/big_warm_5")
/*  48 */         .put("minecraft:ruin/big_ruin_warm6", "minecraft:underwater_ruin/big_warm_6")
/*  49 */         .put("minecraft:ruin/big_ruin_warm7", "minecraft:underwater_ruin/big_warm_7")
/*  50 */         .put("minecraft:ruin/ruin1_brick", "minecraft:underwater_ruin/brick_1")
/*  51 */         .put("minecraft:ruin/ruin2_brick", "minecraft:underwater_ruin/brick_2")
/*  52 */         .put("minecraft:ruin/ruin3_brick", "minecraft:underwater_ruin/brick_3")
/*  53 */         .put("minecraft:ruin/ruin4_brick", "minecraft:underwater_ruin/brick_4")
/*  54 */         .put("minecraft:ruin/ruin5_brick", "minecraft:underwater_ruin/brick_5")
/*  55 */         .put("minecraft:ruin/ruin6_brick", "minecraft:underwater_ruin/brick_6")
/*  56 */         .put("minecraft:ruin/ruin7_brick", "minecraft:underwater_ruin/brick_7")
/*  57 */         .put("minecraft:ruin/ruin8_brick", "minecraft:underwater_ruin/brick_8")
/*  58 */         .put("minecraft:ruin/ruin1_cracked", "minecraft:underwater_ruin/cracked_1")
/*  59 */         .put("minecraft:ruin/ruin2_cracked", "minecraft:underwater_ruin/cracked_2")
/*  60 */         .put("minecraft:ruin/ruin3_cracked", "minecraft:underwater_ruin/cracked_3")
/*  61 */         .put("minecraft:ruin/ruin4_cracked", "minecraft:underwater_ruin/cracked_4")
/*  62 */         .put("minecraft:ruin/ruin5_cracked", "minecraft:underwater_ruin/cracked_5")
/*  63 */         .put("minecraft:ruin/ruin6_cracked", "minecraft:underwater_ruin/cracked_6")
/*  64 */         .put("minecraft:ruin/ruin7_cracked", "minecraft:underwater_ruin/cracked_7")
/*  65 */         .put("minecraft:ruin/ruin8_cracked", "minecraft:underwater_ruin/cracked_8")
/*  66 */         .put("minecraft:ruin/ruin1_mossy", "minecraft:underwater_ruin/mossy_1")
/*  67 */         .put("minecraft:ruin/ruin2_mossy", "minecraft:underwater_ruin/mossy_2")
/*  68 */         .put("minecraft:ruin/ruin3_mossy", "minecraft:underwater_ruin/mossy_3")
/*  69 */         .put("minecraft:ruin/ruin4_mossy", "minecraft:underwater_ruin/mossy_4")
/*  70 */         .put("minecraft:ruin/ruin5_mossy", "minecraft:underwater_ruin/mossy_5")
/*  71 */         .put("minecraft:ruin/ruin6_mossy", "minecraft:underwater_ruin/mossy_6")
/*  72 */         .put("minecraft:ruin/ruin7_mossy", "minecraft:underwater_ruin/mossy_7")
/*  73 */         .put("minecraft:ruin/ruin8_mossy", "minecraft:underwater_ruin/mossy_8")
/*  74 */         .put("minecraft:ruin/ruin_warm1", "minecraft:underwater_ruin/warm_1")
/*  75 */         .put("minecraft:ruin/ruin_warm2", "minecraft:underwater_ruin/warm_2")
/*  76 */         .put("minecraft:ruin/ruin_warm3", "minecraft:underwater_ruin/warm_3")
/*  77 */         .put("minecraft:ruin/ruin_warm4", "minecraft:underwater_ruin/warm_4")
/*  78 */         .put("minecraft:ruin/ruin_warm5", "minecraft:underwater_ruin/warm_5")
/*  79 */         .put("minecraft:ruin/ruin_warm6", "minecraft:underwater_ruin/warm_6")
/*  80 */         .put("minecraft:ruin/ruin_warm7", "minecraft:underwater_ruin/warm_7")
/*  81 */         .put("minecraft:ruin/ruin_warm8", "minecraft:underwater_ruin/warm_8")
/*     */         
/*  83 */         .put("minecraft:ruin/big_brick_1", "minecraft:underwater_ruin/big_brick_1")
/*  84 */         .put("minecraft:ruin/big_brick_2", "minecraft:underwater_ruin/big_brick_2")
/*  85 */         .put("minecraft:ruin/big_brick_3", "minecraft:underwater_ruin/big_brick_3")
/*  86 */         .put("minecraft:ruin/big_brick_8", "minecraft:underwater_ruin/big_brick_8")
/*  87 */         .put("minecraft:ruin/big_mossy_1", "minecraft:underwater_ruin/big_mossy_1")
/*  88 */         .put("minecraft:ruin/big_mossy_2", "minecraft:underwater_ruin/big_mossy_2")
/*  89 */         .put("minecraft:ruin/big_mossy_3", "minecraft:underwater_ruin/big_mossy_3")
/*  90 */         .put("minecraft:ruin/big_mossy_8", "minecraft:underwater_ruin/big_mossy_8")
/*  91 */         .put("minecraft:ruin/big_cracked_1", "minecraft:underwater_ruin/big_cracked_1")
/*  92 */         .put("minecraft:ruin/big_cracked_2", "minecraft:underwater_ruin/big_cracked_2")
/*  93 */         .put("minecraft:ruin/big_cracked_3", "minecraft:underwater_ruin/big_cracked_3")
/*  94 */         .put("minecraft:ruin/big_cracked_8", "minecraft:underwater_ruin/big_cracked_8")
/*  95 */         .put("minecraft:ruin/big_warm_4", "minecraft:underwater_ruin/big_warm_4")
/*  96 */         .put("minecraft:ruin/big_warm_5", "minecraft:underwater_ruin/big_warm_5")
/*  97 */         .put("minecraft:ruin/big_warm_6", "minecraft:underwater_ruin/big_warm_6")
/*  98 */         .put("minecraft:ruin/big_warm_7", "minecraft:underwater_ruin/big_warm_7")
/*  99 */         .build()))
/*     */     
/* 101 */     .build();
/*     */   
/*     */   public ChunkStructuresTemplateRenameFix(Schema paramSchema, boolean paramBoolean) {
/* 104 */     super(paramSchema, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 109 */     Type type = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 110 */     return fixTypeEverywhereTyped("ChunkStructuresTemplateRenameFix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixChildren));
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixChildren(Dynamic<?> paramDynamic) {
/* 114 */     return paramDynamic.update("Children", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Dynamic<?> fixTag(Dynamic<?> paramDynamic1, Dynamic<?> paramDynamic2) {
/* 120 */     String str = paramDynamic1.get("id").asString("");
/* 121 */     if (RENAMES.containsKey(str)) {
/* 122 */       Pair pair = (Pair)RENAMES.get(str);
/* 123 */       if (((String)pair.getFirst()).equals(paramDynamic2.get("id").asString(""))) {
/* 124 */         String str1 = paramDynamic2.get("Template").asString("");
/* 125 */         paramDynamic2 = paramDynamic2.set("Template", paramDynamic2.createString((String)((ImmutableMap)pair.getSecond()).getOrDefault(str1, str1)));
/*     */       } 
/*     */     } 
/* 128 */     return paramDynamic2;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkStructuresTemplateRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */