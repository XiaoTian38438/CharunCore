/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.SequencedMap;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
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
/*    */ public class V3818_3
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3818_3(int paramInt, Schema paramSchema) {
/* 25 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema paramSchema) {
/* 29 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/* 30 */     linkedHashMap.put("minecraft:bees", () -> DSL.list(DSL.optionalFields("entity_data", References.ENTITY_TREE.in(paramSchema))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     linkedHashMap.put("minecraft:block_entity_data", () -> References.BLOCK_ENTITY.in(paramSchema));
/* 36 */     linkedHashMap.put("minecraft:bundle_contents", () -> DSL.list(References.ITEM_STACK.in(paramSchema)));
/* 37 */     linkedHashMap.put("minecraft:can_break", () -> DSL.optionalFields("predicates", DSL.list(DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(paramSchema), DSL.list(References.BLOCK_NAME.in(paramSchema)))))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     linkedHashMap.put("minecraft:can_place_on", () -> DSL.optionalFields("predicates", DSL.list(DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(paramSchema), DSL.list(References.BLOCK_NAME.in(paramSchema)))))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     linkedHashMap.put("minecraft:charged_projectiles", () -> DSL.list(References.ITEM_STACK.in(paramSchema)));
/* 48 */     linkedHashMap.put("minecraft:container", () -> DSL.list(DSL.optionalFields("item", References.ITEM_STACK.in(paramSchema))));
/*    */ 
/*    */     
/* 51 */     linkedHashMap.put("minecraft:entity_data", () -> References.ENTITY_TREE.in(paramSchema));
/* 52 */     linkedHashMap.put("minecraft:pot_decorations", () -> DSL.list(References.ITEM_NAME.in(paramSchema)));
/* 53 */     linkedHashMap.put("minecraft:food", () -> DSL.optionalFields("using_converts_to", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 56 */     linkedHashMap.put("minecraft:custom_name", () -> References.TEXT_COMPONENT.in(paramSchema));
/* 57 */     linkedHashMap.put("minecraft:item_name", () -> References.TEXT_COMPONENT.in(paramSchema));
/* 58 */     linkedHashMap.put("minecraft:lore", () -> DSL.list(References.TEXT_COMPONENT.in(paramSchema)));
/* 59 */     linkedHashMap.put("minecraft:written_book_content", () -> DSL.optionalFields("pages", DSL.list(DSL.or(DSL.optionalFields("raw", References.TEXT_COMPONENT.in(paramSchema), "filtered", References.TEXT_COMPONENT.in(paramSchema)), References.TEXT_COMPONENT.in(paramSchema)))));
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
/* 70 */     return (SequencedMap)linkedHashMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 75 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/* 76 */     paramSchema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(paramSchema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3818_3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */