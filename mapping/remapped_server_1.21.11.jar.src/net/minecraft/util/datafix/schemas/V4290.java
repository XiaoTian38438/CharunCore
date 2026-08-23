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
/*    */ 
/*    */ 
/*    */ public class V4290
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4290(int paramInt, Schema paramSchema) {
/* 22 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 27 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/* 28 */     paramSchema.registerType(true, References.TEXT_COMPONENT, () -> DSL.or(DSL.or(DSL.constType(DSL.string()), DSL.list(References.TEXT_COMPONENT.in(paramSchema))), DSL.optionalFields("extra", DSL.list(References.TEXT_COMPONENT.in(paramSchema)), "separator", References.TEXT_COMPONENT.in(paramSchema), "hoverEvent", (TypeTemplate)DSL.taggedChoice("action", DSL.string(), Map.of("show_text", DSL.optionalFields("contents", References.TEXT_COMPONENT.in(paramSchema)), "show_item", DSL.optionalFields("contents", DSL.or(References.ITEM_STACK.in(paramSchema), References.ITEM_NAME.in(paramSchema))), "show_entity", DSL.optionalFields("type", References.ENTITY_NAME.in(paramSchema), "name", References.TEXT_COMPONENT.in(paramSchema)))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4290.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */