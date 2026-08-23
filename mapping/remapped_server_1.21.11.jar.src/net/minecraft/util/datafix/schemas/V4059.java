/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
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
/*    */ public class V4059
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4059(int paramInt, Schema paramSchema) {
/* 20 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema paramSchema) {
/* 24 */     SequencedMap<String, Supplier<TypeTemplate>> sequencedMap = V3818_3.components(paramSchema);
/* 25 */     sequencedMap.remove("minecraft:food");
/* 26 */     sequencedMap.put("minecraft:use_remainder", () -> References.ITEM_STACK.in(paramSchema));
/*    */     
/* 28 */     sequencedMap.put("minecraft:equippable", () -> DSL.optionalFields("allowed_entities", DSL.or(References.ENTITY_NAME.in(paramSchema), DSL.list(References.ENTITY_NAME.in(paramSchema)))));
/*    */ 
/*    */     
/* 31 */     return sequencedMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 36 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/* 37 */     paramSchema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(paramSchema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4059.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */