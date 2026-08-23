/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ChunkStatusFix2 extends DataFix {
/* 16 */   private static final Map<String, String> RENAMES_AND_DOWNGRADES = (Map<String, String>)ImmutableMap.builder()
/* 17 */     .put("structure_references", "empty")
/* 18 */     .put("biomes", "empty")
/* 19 */     .put("base", "surface")
/* 20 */     .put("carved", "carvers")
/* 21 */     .put("liquid_carved", "liquid_carvers")
/* 22 */     .put("decorated", "features")
/* 23 */     .put("lighted", "light")
/* 24 */     .put("mobs_spawned", "spawn")
/* 25 */     .put("finalized", "heightmaps")
/* 26 */     .put("fullchunk", "full")
/* 27 */     .build();
/*    */   
/*    */   public ChunkStatusFix2(Schema paramSchema, boolean paramBoolean) {
/* 30 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 35 */     Type type1 = getInputSchema().getType(References.CHUNK);
/* 36 */     Type type2 = type1.findFieldType("Level");
/*    */     
/* 38 */     OpticFinder opticFinder = DSL.fieldFinder("Level", type2);
/*    */     
/* 40 */     return fixTypeEverywhereTyped("ChunkStatusFix2", type1, getOutputSchema().getType(References.CHUNK), paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkStatusFix2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */