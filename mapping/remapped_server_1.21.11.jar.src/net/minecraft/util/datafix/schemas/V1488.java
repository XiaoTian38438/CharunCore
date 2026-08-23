/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1488
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1488(int paramInt, Schema paramSchema) {
/* 14 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 20 */     paramSchema.register(map, "minecraft:command_block", () -> DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(paramSchema), "LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 24 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1488.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */