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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V1022
/*    */   extends Schema
/*    */ {
/*    */   public V1022(int paramInt, Schema paramSchema) {
/* 24 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 29 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 31 */     paramSchema.registerType(false, References.RECIPE, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 32 */     paramSchema.registerType(false, References.PLAYER, () -> DSL.optionalFields(new Pair[] { Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(paramSchema))), Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(paramSchema))), Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema))), Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(paramSchema))), Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(paramSchema)), Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(paramSchema)), Pair.of("recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(paramSchema)), "toBeDisplayed", DSL.list(References.RECIPE.in(paramSchema)))) }));
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
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     paramSchema.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(paramSchema))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1022.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */