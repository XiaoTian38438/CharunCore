/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlendingDataFix
/*    */   extends DataFix {
/*    */   private final String name;
/* 20 */   private static final Set<String> STATUSES_TO_SKIP_BLENDING = Set.of("minecraft:empty", "minecraft:structure_starts", "minecraft:structure_references", "minecraft:biomes");
/*    */   
/*    */   public BlendingDataFix(Schema paramSchema) {
/* 23 */     super(paramSchema, false);
/* 24 */     this.name = "Blending Data Fix v" + paramSchema.getVersionKey();
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 29 */     Type type = getOutputSchema().getType(References.CHUNK);
/*    */     
/* 31 */     return fixTypeEverywhereTyped(this.name, type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updateChunkTag(Dynamic<?> paramDynamic, OptionalDynamic<?> paramOptionalDynamic) {
/* 37 */     paramDynamic = paramDynamic.remove("blending_data");
/* 38 */     boolean bool = "minecraft:overworld".equals(paramOptionalDynamic.get("dimension").asString().result().orElse(""));
/*    */     
/* 40 */     Optional<Dynamic> optional = paramDynamic.get("Status").result();
/* 41 */     if (bool && optional.isPresent()) {
/* 42 */       String str = NamespacedSchema.ensureNamespaced(((Dynamic)optional.get()).asString("empty"));
/* 43 */       Optional<Dynamic> optional1 = paramDynamic.get("below_zero_retrogen").result();
/*    */       
/* 45 */       if (!STATUSES_TO_SKIP_BLENDING.contains(str)) {
/*    */         
/* 47 */         paramDynamic = updateBlendingData(paramDynamic, 384, -64);
/* 48 */       } else if (optional1.isPresent()) {
/*    */         
/* 50 */         Dynamic dynamic = optional1.get();
/* 51 */         String str1 = NamespacedSchema.ensureNamespaced(dynamic.get("target_status").asString("empty"));
/* 52 */         if (!STATUSES_TO_SKIP_BLENDING.contains(str1)) {
/* 53 */           paramDynamic = updateBlendingData(paramDynamic, 256, 0);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 58 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private static Dynamic<?> updateBlendingData(Dynamic<?> paramDynamic, int paramInt1, int paramInt2) {
/* 62 */     return paramDynamic.set("blending_data", paramDynamic.createMap(Map.of(paramDynamic
/* 63 */             .createString("min_section"), paramDynamic.createInt(SectionPos.blockToSectionCoord(paramInt2)), paramDynamic
/* 64 */             .createString("max_section"), paramDynamic.createInt(SectionPos.blockToSectionCoord(paramInt2 + paramInt1)))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlendingDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */