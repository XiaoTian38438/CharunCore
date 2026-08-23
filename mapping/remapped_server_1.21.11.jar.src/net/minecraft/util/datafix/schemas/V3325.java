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
/*    */ public class V3325
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3325(int paramInt, Schema paramSchema) {
/* 16 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/*    */     
/* 23 */     paramSchema.register(map, "minecraft:item_display", paramString -> DSL.optionalFields("item", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 26 */     paramSchema.register(map, "minecraft:block_display", paramString -> DSL.optionalFields("block_state", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */     
/* 29 */     paramSchema.register(map, "minecraft:text_display", () -> DSL.optionalFields("text", References.TEXT_COMPONENT.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 33 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3325.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */