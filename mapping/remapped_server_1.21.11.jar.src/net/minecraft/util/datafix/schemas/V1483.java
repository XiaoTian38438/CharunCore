/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1483
/*    */   extends NamespacedSchema {
/*    */   public V1483(int paramInt, Schema paramSchema) {
/* 11 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 16 */     Map<String, Supplier> map = super.registerEntities(paramSchema);
/*    */     
/* 18 */     map.put("minecraft:pufferfish", map.remove("minecraft:puffer_fish"));
/*    */     
/* 20 */     return (Map)map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1483.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */