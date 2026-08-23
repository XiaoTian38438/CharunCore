/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V4301
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4301(int paramInt, Schema paramSchema) {
/* 18 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 23 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/* 24 */     paramSchema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.optional(DSL.field("equipment", DSL.optionalFields(new Pair[] { Pair.of("mainhand", References.ITEM_STACK.in(paramSchema)), Pair.of("offhand", References.ITEM_STACK.in(paramSchema)), Pair.of("feet", References.ITEM_STACK.in(paramSchema)), Pair.of("legs", References.ITEM_STACK.in(paramSchema)), Pair.of("chest", References.ITEM_STACK.in(paramSchema)), Pair.of("head", References.ITEM_STACK.in(paramSchema)), Pair.of("body", References.ITEM_STACK.in(paramSchema)), Pair.of("saddle", References.ITEM_STACK.in(paramSchema)) }))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4301.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */