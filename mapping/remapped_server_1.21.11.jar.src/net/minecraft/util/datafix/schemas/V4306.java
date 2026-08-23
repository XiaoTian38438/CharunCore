/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4306
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4306(int paramInt, Schema paramSchema) {
/* 14 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 20 */     map.remove("minecraft:potion");
/* 21 */     paramSchema.register(map, "minecraft:splash_potion", () -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 24 */     paramSchema.register(map, "minecraft:lingering_potion", () -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 27 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4306.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */