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
/*    */ public class V1929
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1929(int paramInt, Schema paramSchema) {
/* 16 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 22 */     paramSchema.register(map, "minecraft:wandering_trader", paramString -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(paramSchema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     paramSchema.register(map, "minecraft:trader_llama", paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema)), "SaddleItem", References.ITEM_STACK.in(paramSchema), "DecorItem", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1929.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */