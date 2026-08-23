/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V3439_1
/*    */   extends NamespacedSchema {
/*    */   public V3439_1(int paramInt, Schema paramSchema) {
/* 11 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 17 */     register(map, "minecraft:hanging_sign", () -> V3439.sign(paramSchema));
/* 18 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3439_1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */