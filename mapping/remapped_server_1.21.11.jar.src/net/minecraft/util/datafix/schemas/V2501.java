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
/*    */ public class V2501
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2501(int paramInt, Schema paramSchema) {
/* 20 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   private static void registerFurnace(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 24 */     paramSchema.register(paramMap, paramString, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "CustomName", References.TEXT_COMPONENT.in(paramSchema), "RecipesUsed", DSL.compoundList(References.RECIPE.in(paramSchema), DSL.constType(DSL.intType()))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 33 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 34 */     registerFurnace(paramSchema, map, "minecraft:furnace");
/* 35 */     registerFurnace(paramSchema, map, "minecraft:smoker");
/* 36 */     registerFurnace(paramSchema, map, "minecraft:blast_furnace");
/* 37 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V2501.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */