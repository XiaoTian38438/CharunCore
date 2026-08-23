/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class BlockEntityIdFix extends DataFix {
/*    */   public BlockEntityIdFix(Schema paramSchema, boolean paramBoolean) {
/* 16 */     super(paramSchema, paramBoolean);
/*    */   } public static final Map<String, String> ID_MAP;
/*    */   static {
/* 19 */     ID_MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*    */           paramHashMap.put("Airportal", "minecraft:end_portal");
/*    */           paramHashMap.put("Banner", "minecraft:banner");
/*    */           paramHashMap.put("Beacon", "minecraft:beacon");
/*    */           paramHashMap.put("Cauldron", "minecraft:brewing_stand");
/*    */           paramHashMap.put("Chest", "minecraft:chest");
/*    */           paramHashMap.put("Comparator", "minecraft:comparator");
/*    */           paramHashMap.put("Control", "minecraft:command_block");
/*    */           paramHashMap.put("DLDetector", "minecraft:daylight_detector");
/*    */           paramHashMap.put("Dropper", "minecraft:dropper");
/*    */           paramHashMap.put("EnchantTable", "minecraft:enchanting_table");
/*    */           paramHashMap.put("EndGateway", "minecraft:end_gateway");
/*    */           paramHashMap.put("EnderChest", "minecraft:ender_chest");
/*    */           paramHashMap.put("FlowerPot", "minecraft:flower_pot");
/*    */           paramHashMap.put("Furnace", "minecraft:furnace");
/*    */           paramHashMap.put("Hopper", "minecraft:hopper");
/*    */           paramHashMap.put("MobSpawner", "minecraft:mob_spawner");
/*    */           paramHashMap.put("Music", "minecraft:noteblock");
/*    */           paramHashMap.put("Piston", "minecraft:piston");
/*    */           paramHashMap.put("RecordPlayer", "minecraft:jukebox");
/*    */           paramHashMap.put("Sign", "minecraft:sign");
/*    */           paramHashMap.put("Skull", "minecraft:skull");
/*    */           paramHashMap.put("Structure", "minecraft:structure_block");
/*    */           paramHashMap.put("Trap", "minecraft:dispenser");
/*    */         });
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 47 */     Type type1 = getInputSchema().getType(References.ITEM_STACK);
/* 48 */     Type type2 = getOutputSchema().getType(References.ITEM_STACK);
/*    */     
/* 50 */     TaggedChoice.TaggedChoiceType taggedChoiceType1 = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 51 */     TaggedChoice.TaggedChoiceType taggedChoiceType2 = getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
/*    */     
/* 53 */     return TypeRewriteRule.seq(
/* 54 */         convertUnchecked("item stack block entity name hook converter", type1, type2), 
/* 55 */         fixTypeEverywhere("BlockEntityIdFix", (Type)taggedChoiceType1, (Type)taggedChoiceType2, paramDynamicOps -> ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */