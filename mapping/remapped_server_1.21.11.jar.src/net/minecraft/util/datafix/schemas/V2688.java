/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V2688
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2688(int paramInt, Schema paramSchema) {
/* 14 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 20 */     paramSchema.registerSimple(map, "minecraft:glow_squid");
/*    */     
/* 22 */     paramSchema.register(map, "minecraft:glow_item_frame", paramString -> DSL.optionalFields("Item", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 25 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V2688.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */