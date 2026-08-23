/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V1458
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1458(int paramInt, Schema paramSchema) {
/* 20 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 25 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 27 */     paramSchema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(paramSchema), DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(paramSchema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), paramMap))));
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
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 40 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 41 */     paramSchema.register(map, "minecraft:beacon", () -> nameable(paramSchema));
/* 42 */     paramSchema.register(map, "minecraft:banner", () -> nameable(paramSchema));
/* 43 */     paramSchema.register(map, "minecraft:brewing_stand", () -> nameableInventory(paramSchema));
/* 44 */     paramSchema.register(map, "minecraft:chest", () -> nameableInventory(paramSchema));
/* 45 */     paramSchema.register(map, "minecraft:trapped_chest", () -> nameableInventory(paramSchema));
/* 46 */     paramSchema.register(map, "minecraft:dispenser", () -> nameableInventory(paramSchema));
/* 47 */     paramSchema.register(map, "minecraft:dropper", () -> nameableInventory(paramSchema));
/* 48 */     paramSchema.register(map, "minecraft:enchanting_table", () -> nameable(paramSchema));
/* 49 */     paramSchema.register(map, "minecraft:furnace", () -> nameableInventory(paramSchema));
/* 50 */     paramSchema.register(map, "minecraft:hopper", () -> nameableInventory(paramSchema));
/* 51 */     paramSchema.register(map, "minecraft:shulker_box", () -> nameableInventory(paramSchema));
/* 52 */     return map;
/*    */   }
/*    */   
/*    */   public static TypeTemplate nameableInventory(Schema paramSchema) {
/* 56 */     return DSL.optionalFields("Items", 
/* 57 */         DSL.list(References.ITEM_STACK.in(paramSchema)), "CustomName", References.TEXT_COMPONENT
/* 58 */         .in(paramSchema));
/*    */   }
/*    */ 
/*    */   
/*    */   public static TypeTemplate nameable(Schema paramSchema) {
/* 63 */     return DSL.optionalFields("CustomName", References.TEXT_COMPONENT
/* 64 */         .in(paramSchema));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1458.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */