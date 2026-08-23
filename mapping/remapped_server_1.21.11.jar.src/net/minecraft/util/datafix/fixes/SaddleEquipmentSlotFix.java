/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class SaddleEquipmentSlotFix
/*    */   extends DataFix {
/* 20 */   private static final Set<String> ENTITIES_WITH_SADDLE_ITEM = Set.of("minecraft:horse", "minecraft:skeleton_horse", "minecraft:zombie_horse", "minecraft:donkey", "minecraft:mule", "minecraft:camel", "minecraft:llama", "minecraft:trader_llama");
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
/* 31 */   private static final Set<String> ENTITIES_WITH_SADDLE_FLAG = Set.of("minecraft:pig", "minecraft:strider");
/*    */ 
/*    */   
/*    */   private static final String SADDLE_FLAG = "Saddle";
/*    */   
/*    */   private static final String NEW_SADDLE = "saddle";
/*    */ 
/*    */   
/*    */   public SaddleEquipmentSlotFix(Schema paramSchema) {
/* 40 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 45 */     TaggedChoice.TaggedChoiceType taggedChoiceType = getInputSchema().findChoiceType(References.ENTITY);
/* 46 */     OpticFinder opticFinder = DSL.typeFinder((Type)taggedChoiceType);
/*    */     
/* 48 */     Type type1 = getInputSchema().getType(References.ENTITY);
/* 49 */     Type type2 = getOutputSchema().getType(References.ENTITY);
/* 50 */     Type type3 = ExtraDataFixUtils.patchSubType(type1, type1, type2);
/*    */     
/* 52 */     return fixTypeEverywhereTyped("SaddleEquipmentSlotFix", type1, type2, paramTyped -> {
/*    */           String str = paramTyped.getOptional(paramOpticFinder).map(Pair::getFirst).map(NamespacedSchema::ensureNamespaced).orElse("");
/*    */           Typed typed = ExtraDataFixUtils.cast(paramType1, paramTyped);
/*    */           return ENTITIES_WITH_SADDLE_ITEM.contains(str) ? Util.writeAndReadTypedOrThrow(typed, paramType2, SaddleEquipmentSlotFix::fixEntityWithSaddleItem) : (ENTITIES_WITH_SADDLE_FLAG.contains(str) ? Util.writeAndReadTypedOrThrow(typed, paramType2, SaddleEquipmentSlotFix::fixEntityWithSaddleFlag) : ExtraDataFixUtils.cast(paramType2, paramTyped));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixEntityWithSaddleItem(Dynamic<?> paramDynamic) {
/* 66 */     if (paramDynamic.get("SaddleItem").result().isEmpty()) {
/* 67 */       return paramDynamic;
/*    */     }
/* 69 */     return fixDropChances(paramDynamic.renameField("SaddleItem", "saddle"));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixEntityWithSaddleFlag(Dynamic<?> paramDynamic) {
/* 73 */     boolean bool = paramDynamic.get("Saddle").asBoolean(false);
/* 74 */     paramDynamic = paramDynamic.remove("Saddle");
/* 75 */     if (!bool) {
/* 76 */       return paramDynamic;
/*    */     }
/*    */ 
/*    */     
/* 80 */     Dynamic dynamic = paramDynamic.emptyMap().set("id", paramDynamic.createString("minecraft:saddle")).set("count", paramDynamic.createInt(1));
/* 81 */     return fixDropChances(paramDynamic.set("saddle", dynamic));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixDropChances(Dynamic<?> paramDynamic) {
/* 87 */     Dynamic dynamic = paramDynamic.get("drop_chances").orElseEmptyMap().set("saddle", paramDynamic.createFloat(2.0F));
/* 88 */     return paramDynamic.set("drop_chances", dynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\SaddleEquipmentSlotFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */