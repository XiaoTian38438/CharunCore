/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class BlockPosFormatAndRenamesFix
/*    */   extends DataFix {
/* 18 */   private static final List<String> PATROLLING_MOBS = List.of("minecraft:witch", "minecraft:ravager", "minecraft:pillager", "minecraft:illusioner", "minecraft:evoker", "minecraft:vindicator");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPosFormatAndRenamesFix(Schema paramSchema) {
/* 28 */     super(paramSchema, true);
/*    */   }
/*    */   
/*    */   private Typed<?> fixFields(Typed<?> paramTyped, Map<String, String> paramMap) {
/* 32 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*    */           for (Map.Entry entry : paramMap.entrySet()) {
/*    */             paramDynamic = paramDynamic.renameAndFixField((String)entry.getKey(), (String)entry.getValue(), ExtraDataFixUtils::fixBlockPos);
/*    */           }
/*    */           return paramDynamic;
/*    */         });
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixMapSavedData(Dynamic<T> paramDynamic) {
/* 41 */     return paramDynamic
/* 42 */       .update("frames", paramDynamic -> paramDynamic.createList(paramDynamic.asStream().map(())))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 48 */       .update("banners", paramDynamic -> paramDynamic.createList(paramDynamic.asStream().map(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 59 */     ArrayList<TypeRewriteRule> arrayList = new ArrayList();
/* 60 */     addEntityRules(arrayList);
/* 61 */     addBlockEntityRules(arrayList);
/*    */     
/* 63 */     arrayList.add(writeFixAndRead("BlockPos format for map frames", getInputSchema().getType(References.SAVED_DATA_MAP_DATA), getOutputSchema().getType(References.SAVED_DATA_MAP_DATA), paramDynamic -> paramDynamic.update("data", this::fixMapSavedData)));
/*    */ 
/*    */ 
/*    */     
/* 67 */     Type<?> type = getInputSchema().getType(References.ITEM_STACK);
/* 68 */     arrayList.add(fixTypeEverywhereTyped("BlockPos format for compass target", type, ItemStackTagFix.createFixer(type, "minecraft:compass"::equals, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 73 */     return TypeRewriteRule.seq(arrayList);
/*    */   }
/*    */   
/*    */   private void addEntityRules(List<TypeRewriteRule> paramList) {
/* 77 */     paramList.add(createEntityFixer(References.ENTITY, "minecraft:bee", Map.of("HivePos", "hive_pos", "FlowerPos", "flower_pos")));
/*    */ 
/*    */ 
/*    */     
/* 81 */     paramList.add(createEntityFixer(References.ENTITY, "minecraft:end_crystal", Map.of("BeamTarget", "beam_target")));
/* 82 */     paramList.add(createEntityFixer(References.ENTITY, "minecraft:wandering_trader", Map.of("WanderTarget", "wander_target")));
/* 83 */     for (String str : PATROLLING_MOBS) {
/* 84 */       paramList.add(createEntityFixer(References.ENTITY, str, Map.of("PatrolTarget", "patrol_target")));
/*    */     }
/* 86 */     paramList.add(fixTypeEverywhereTyped("BlockPos format in Leash for mobs", getInputSchema().getType(References.ENTITY), paramTyped -> paramTyped.update(DSL.remainderFinder(), ())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void addBlockEntityRules(List<TypeRewriteRule> paramList) {
/* 92 */     paramList.add(createEntityFixer(References.BLOCK_ENTITY, "minecraft:beehive", Map.of("FlowerPos", "flower_pos")));
/* 93 */     paramList.add(createEntityFixer(References.BLOCK_ENTITY, "minecraft:end_gateway", Map.of("ExitPortal", "exit_portal")));
/*    */   }
/*    */   
/*    */   private TypeRewriteRule createEntityFixer(DSL.TypeReference paramTypeReference, String paramString, Map<String, String> paramMap) {
/* 97 */     String str = "BlockPos format in " + String.valueOf(paramMap.keySet()) + " for " + paramString + " (" + paramTypeReference.typeName() + ")";
/* 98 */     OpticFinder opticFinder = DSL.namedChoice(paramString, getInputSchema().getChoiceType(paramTypeReference, paramString));
/* 99 */     return fixTypeEverywhereTyped(str, getInputSchema().getType(paramTypeReference), paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockPosFormatAndRenamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */