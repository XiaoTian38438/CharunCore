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
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.Util;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class WorldGenSettingsHeightAndBiomeFix extends DataFix {
/*    */   private static final String NAME = "WorldGenSettingsHeightAndBiomeFix";
/*    */   
/*    */   public WorldGenSettingsHeightAndBiomeFix(Schema paramSchema) {
/* 22 */     super(paramSchema, true);
/*    */   }
/*    */   public static final String WAS_PREVIOUSLY_INCREASED_KEY = "has_increased_height_already";
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type type1 = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 28 */     OpticFinder opticFinder = type1.findField("dimensions");
/*    */     
/* 30 */     Type type2 = getOutputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 31 */     Type type3 = type2.findFieldType("dimensions");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("WorldGenSettingsHeightAndBiomeFix", type1, type2, paramTyped -> {
/*    */           OptionalDynamic optionalDynamic = ((Dynamic)paramTyped.get(DSL.remainderFinder())).get("has_increased_height_already");
/*    */           boolean bool1 = optionalDynamic.result().isEmpty();
/*    */           boolean bool2 = optionalDynamic.asBoolean(true);
/*    */           return paramTyped.update(DSL.remainderFinder(), ()).updateTyped(paramOpticFinder, paramType, ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updateLayers(Dynamic<?> paramDynamic) {
/* 83 */     Dynamic dynamic = paramDynamic.createMap((Map)ImmutableMap.of(paramDynamic
/* 84 */           .createString("height"), paramDynamic
/* 85 */           .createInt(64), paramDynamic
/* 86 */           .createString("block"), paramDynamic
/* 87 */           .createString("minecraft:air")));
/*    */     
/* 89 */     return paramDynamic.createList(Stream.concat(Stream.of(dynamic), paramDynamic.asStream()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsHeightAndBiomeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */