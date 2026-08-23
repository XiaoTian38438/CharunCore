/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1906
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1906(int paramInt, Schema paramSchema) {
/* 14 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/*    */     
/* 21 */     registerInventory(paramSchema, map, "minecraft:barrel");
/* 22 */     registerInventory(paramSchema, map, "minecraft:smoker");
/* 23 */     registerInventory(paramSchema, map, "minecraft:blast_furnace");
/*    */     
/* 25 */     paramSchema.register(map, "minecraft:lectern", paramString -> DSL.optionalFields("Book", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 29 */     paramSchema.registerSimple(map, "minecraft:bell");
/*    */     
/* 31 */     return map;
/*    */   }
/*    */   
/*    */   protected static void registerInventory(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 35 */     paramSchema.register(paramMap, paramString, () -> V1458.nameableInventory(paramSchema));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1906.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */