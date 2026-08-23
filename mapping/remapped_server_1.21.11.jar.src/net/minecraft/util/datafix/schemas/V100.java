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
/*    */ public class V100
/*    */   extends Schema
/*    */ {
/*    */   public V100(int paramInt, Schema paramSchema) {
/* 18 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 23 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 25 */     paramSchema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.and(DSL.optional(DSL.field("ArmorItems", DSL.list(References.ITEM_STACK.in(paramSchema)))), new TypeTemplate[] { DSL.optional(DSL.field("HandItems", DSL.list(References.ITEM_STACK.in(paramSchema)))), DSL.optional(DSL.field("body_armor_item", References.ITEM_STACK.in(paramSchema))), DSL.optional(DSL.field("saddle", References.ITEM_STACK.in(paramSchema))) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V100.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */