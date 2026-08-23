/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SpawnerDataFix
/*    */   extends DataFix {
/*    */   public SpawnerDataFix(Schema paramSchema) {
/* 17 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type type1 = getInputSchema().getType(References.UNTAGGED_SPAWNER);
/* 23 */     Type type2 = getOutputSchema().getType(References.UNTAGGED_SPAWNER);
/*    */     
/* 25 */     OpticFinder opticFinder1 = type1.findField("SpawnData");
/* 26 */     Type type3 = type2.findField("SpawnData").type();
/*    */     
/* 28 */     OpticFinder opticFinder2 = type1.findField("SpawnPotentials");
/* 29 */     Type type4 = type2.findField("SpawnPotentials").type();
/*    */     
/* 31 */     return fixTypeEverywhereTyped("Fix mob spawner data structure", type1, type2, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, paramType1, ()).updateTyped(paramOpticFinder2, paramType2, ()));
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
/*    */   private <T> Typed<T> wrapEntityToSpawnData(Type<T> paramType, Typed<?> paramTyped) {
/* 44 */     DynamicOps dynamicOps = paramTyped.getOps();
/*    */     
/* 46 */     return new Typed(paramType, dynamicOps, Pair.of(paramTyped.getValue(), new Dynamic(dynamicOps)));
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> Typed<T> wrapSpawnPotentialsToWeightedEntries(Type<T> paramType, Typed<?> paramTyped) {
/* 51 */     DynamicOps dynamicOps = paramTyped.getOps();
/* 52 */     List list1 = (List)paramTyped.getValue();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 61 */     List list2 = list1.stream().map(paramObject -> { Pair pair = (Pair)paramObject; int i = ((Number)((Dynamic)pair.getSecond()).get("Weight").asNumber().result().orElse(Integer.valueOf(1))).intValue(); Dynamic dynamic1 = new Dynamic(paramDynamicOps); dynamic1 = dynamic1.set("weight", dynamic1.createInt(i)); Dynamic dynamic2 = ((Dynamic)pair.getSecond()).remove("Weight").remove("Entity"); return Pair.of(Pair.of(pair.getFirst(), dynamic2), dynamic1); }).toList();
/* 62 */     return new Typed(paramType, dynamicOps, list2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\SpawnerDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */