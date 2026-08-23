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
/*    */ public class V4302
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4302(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 21 */     paramSchema.registerSimple(map, "minecraft:test_block");
/* 22 */     paramSchema.register(map, "minecraft:test_instance_block", () -> DSL.optionalFields("data", DSL.optionalFields("error_message", References.TEXT_COMPONENT.in(paramSchema)), "errors", DSL.list(DSL.optionalFields("text", References.TEXT_COMPONENT.in(paramSchema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4302.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */