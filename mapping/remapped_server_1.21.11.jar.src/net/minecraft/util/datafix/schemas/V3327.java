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
/*    */ public class V3327
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3327(int paramInt, Schema paramSchema) {
/* 17 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 22 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 23 */     paramSchema.register(map, "minecraft:decorated_pot", () -> DSL.optionalFields("shards", DSL.list(References.ITEM_NAME.in(paramSchema)), "item", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     paramSchema.register(map, "minecraft:suspicious_sand", () -> DSL.optionalFields("item", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3327.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */