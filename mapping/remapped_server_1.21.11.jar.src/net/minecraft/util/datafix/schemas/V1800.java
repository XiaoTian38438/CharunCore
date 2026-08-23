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
/*    */ public class V1800
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1800(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/*    */     
/* 22 */     paramSchema.registerSimple(map, "minecraft:panda");
/* 23 */     paramSchema.register(map, "minecraft:pillager", paramString -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*    */ 
/*    */ 
/*    */     
/* 27 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1800.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */