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
/*    */ public class V135
/*    */   extends Schema
/*    */ {
/*    */   public V135(int paramInt, Schema paramSchema) {
/* 19 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 24 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 26 */     paramSchema.registerType(false, References.PLAYER, () -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(paramSchema)), "ender_pearls", DSL.list(References.ENTITY_TREE.in(paramSchema)), "Inventory", DSL.list(References.ITEM_STACK.in(paramSchema)), "EnderItems", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     paramSchema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(paramSchema)), References.ENTITY.in(paramSchema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V135.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */