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
/*    */ public class V4300
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4300(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 21 */     paramSchema.register(map, "minecraft:llama", paramString -> entityWithInventory(paramSchema));
/* 22 */     paramSchema.register(map, "minecraft:trader_llama", paramString -> entityWithInventory(paramSchema));
/* 23 */     paramSchema.register(map, "minecraft:donkey", paramString -> entityWithInventory(paramSchema));
/* 24 */     paramSchema.register(map, "minecraft:mule", paramString -> entityWithInventory(paramSchema));
/* 25 */     paramSchema.registerSimple(map, "minecraft:horse");
/* 26 */     paramSchema.registerSimple(map, "minecraft:skeleton_horse");
/* 27 */     paramSchema.registerSimple(map, "minecraft:zombie_horse");
/*    */     
/* 29 */     return map;
/*    */   }
/*    */   
/*    */   private static TypeTemplate entityWithInventory(Schema paramSchema) {
/* 33 */     return DSL.optionalFields("Items", 
/* 34 */         DSL.list(References.ITEM_STACK.in(paramSchema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4300.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */