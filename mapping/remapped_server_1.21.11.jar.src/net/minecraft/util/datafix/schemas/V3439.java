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
/*    */ public class V3439
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3439(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 21 */     register(map, "minecraft:sign", () -> sign(paramSchema));
/* 22 */     return map;
/*    */   }
/*    */   
/*    */   public static TypeTemplate sign(Schema paramSchema) {
/* 26 */     return DSL.optionalFields("front_text", 
/* 27 */         DSL.optionalFields("messages", 
/* 28 */           DSL.list(References.TEXT_COMPONENT.in(paramSchema)), "filtered_messages", 
/* 29 */           DSL.list(References.TEXT_COMPONENT.in(paramSchema))), "back_text", 
/*    */         
/* 31 */         DSL.optionalFields("messages", 
/* 32 */           DSL.list(References.TEXT_COMPONENT.in(paramSchema)), "filtered_messages", 
/* 33 */           DSL.list(References.TEXT_COMPONENT.in(paramSchema))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3439.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */