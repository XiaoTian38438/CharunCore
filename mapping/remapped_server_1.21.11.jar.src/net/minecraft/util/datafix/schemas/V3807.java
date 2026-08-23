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
/*    */ public class V3807
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3807(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 21 */     paramSchema.register(map, "minecraft:vault", () -> DSL.optionalFields("config", DSL.optionalFields("key_item", References.ITEM_STACK.in(paramSchema)), "server_data", DSL.optionalFields("items_to_eject", DSL.list(References.ITEM_STACK.in(paramSchema))), "shared_data", DSL.optionalFields("display_item", References.ITEM_STACK.in(paramSchema))));
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
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3807.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */