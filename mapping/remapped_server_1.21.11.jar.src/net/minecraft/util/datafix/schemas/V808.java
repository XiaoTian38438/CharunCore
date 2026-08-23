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
/*    */ public class V808
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V808(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   protected static void registerInventory(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 19 */     paramSchema.register(paramMap, paramString, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 26 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/*    */     
/* 28 */     registerInventory(paramSchema, map, "minecraft:shulker_box");
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V808.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */