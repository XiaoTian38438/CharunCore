/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class BeehiveFieldRenameFix extends DataFix {
/*    */   public BeehiveFieldRenameFix(Schema paramSchema) {
/* 15 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixBeehive(Dynamic<?> paramDynamic) {
/* 20 */     return paramDynamic.remove("Bees");
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixBee(Dynamic<?> paramDynamic) {
/* 25 */     paramDynamic = paramDynamic.remove("EntityData");
/* 26 */     paramDynamic = paramDynamic.renameField("TicksInHive", "ticks_in_hive");
/* 27 */     paramDynamic = paramDynamic.renameField("MinOccupationTicks", "min_ticks_in_hive");
/* 28 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 33 */     Type type1 = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:beehive");
/* 34 */     OpticFinder opticFinder1 = DSL.namedChoice("minecraft:beehive", type1);
/*    */     
/* 36 */     List.ListType listType = (List.ListType)type1.findFieldType("Bees");
/* 37 */     Type type2 = listType.getElement();
/*    */     
/* 39 */     OpticFinder opticFinder2 = DSL.fieldFinder("Bees", (Type)listType);
/* 40 */     OpticFinder opticFinder3 = DSL.typeFinder(type2);
/*    */     
/* 42 */     Type type3 = getInputSchema().getType(References.BLOCK_ENTITY);
/* 43 */     Type type4 = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 44 */     return fixTypeEverywhereTyped("BeehiveFieldRenameFix", type3, type4, paramTyped -> ExtraDataFixUtils.cast(paramType, paramTyped.updateTyped(paramOpticFinder1, ())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BeehiveFieldRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */